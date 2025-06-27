package org.hark7.fishingPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.bukkit.Bukkit.getLogger;

public class PlayerDataController {
    private Connection connection;

    public PlayerDataController(FishingPlugin plugin) {
        try {
            var dbFile = new File(plugin.getDataFolder(), "fishing_plugin.db");
            if (!dbFile.exists()) {
                if (!dbFile.getParentFile().mkdirs()) {
                    getLogger().severe("Failed to create directory for database file.");
                }
                if (!dbFile.createNewFile()) {
                    getLogger().severe("Failed to create database file.");
                }
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            try (Statement statement = connection.createStatement()) {
                statement.setQueryTimeout(10);
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS players (" +
                        "uuid TEXT PRIMARY KEY," +
                        "name TEXT NOT NULL," +
                        "level INTEGER NOT NULL," +
                        "exp INTEGER NOT NULL," +
                        "scrap_count INTEGER NOT NULL," +
                        "common_count INTEGER NOT NULL," +
                        "uncommon_count INTEGER NOT NULL," +
                        "rare_count INTEGER NOT NULL," +
                        "epic_count INTEGER NOT NULL," +
                        "legendary_count INTEGER NOT NULL" +
                        ")"
                );
            }
        } catch (IOException | SQLException e) {
            getLogger().severe("Error initializing PlayerDataController: " + e.getMessage());
        }
    }

    public synchronized PlayerData getPlayerData(UUID playerUUID) {
        String sql = "SELECT * FROM players WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    var count = new ConcurrentHashMap<CustomFish.Rarity, Integer>();
                    count.put(CustomFish.Rarity.SCRAP, rs.getInt("scrap_count"));
                    count.put(CustomFish.Rarity.COMMON, rs.getInt("common_count"));
                    count.put(CustomFish.Rarity.UNCOMMON, rs.getInt("uncommon_count"));
                    count.put(CustomFish.Rarity.RARE, rs.getInt("rare_count"));
                    count.put(CustomFish.Rarity.EPIC, rs.getInt("epic_count"));
                    count.put(CustomFish.Rarity.LEGENDARY, rs.getInt("legendary_count"));
                    return new PlayerData(
                            rs.getString("name"),
                            rs.getInt("level"),
                            rs.getInt("exp"),
                            count
                    );
                }
            }
        } catch (SQLException e) {
            getLogger().severe("Error retrieving player data for " + playerUUID + ": " + e.getMessage());
        }
        return null;
    }

    public synchronized Map<UUID, PlayerData> getPlayerDataMap() {
        Map<UUID, PlayerData> playerDataMap = new HashMap<>();
        String sql = "SELECT * FROM players";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                UUID playerUUID = UUID.fromString(rs.getString("uuid"));
                var count = new ConcurrentHashMap<CustomFish.Rarity, Integer>();
                count.put(CustomFish.Rarity.SCRAP, rs.getInt("scrap_count"));
                count.put(CustomFish.Rarity.COMMON, rs.getInt("common_count"));
                count.put(CustomFish.Rarity.UNCOMMON, rs.getInt("uncommon_count"));
                count.put(CustomFish.Rarity.RARE, rs.getInt("rare_count"));
                count.put(CustomFish.Rarity.EPIC, rs.getInt("epic_count"));
                count.put(CustomFish.Rarity.LEGENDARY, rs.getInt("legendary_count"));
                PlayerData data = new PlayerData(
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getInt("exp"),
                        count
                );
                playerDataMap.put(playerUUID, data);
            }
        } catch (SQLException e) {
            getLogger().severe("Error retrieving player data: " + e.getMessage());
        }
        return Map.copyOf(playerDataMap);
    }

    public synchronized void setPlayerData(UUID playerUUID, PlayerData playerData) {
        String sql =
                "INSERT INTO players (uuid, name, level, exp, scrap_count, common_count, uncommon_count, rare_count, epic_count, legendary_count) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET " +
                "name=excluded.name, level=excluded.level, exp=excluded.exp, " +
                "scrap_count=excluded.scrap_count, common_count=excluded.common_count, " +
                "uncommon_count=excluded.uncommon_count, rare_count=excluded.rare_count, " +
                "epic_count=excluded.epic_count, legendary_count=excluded.legendary_count";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, playerData.getPlayerName());
            ps.setInt(3, playerData.getLevel());
            ps.setInt(4, playerData.getExp());
            var count = playerData.getCounts();
            ps.setInt(5, count.getOrDefault(CustomFish.Rarity.SCRAP, 0));
            ps.setInt(6, count.getOrDefault(CustomFish.Rarity.COMMON, 0));
            ps.setInt(7, count.getOrDefault(CustomFish.Rarity.UNCOMMON, 0));
            ps.setInt(8, count.getOrDefault(CustomFish.Rarity.RARE, 0));
            ps.setInt(9, count.getOrDefault(CustomFish.Rarity.EPIC, 0));
            ps.setInt(10, count.getOrDefault(CustomFish.Rarity.LEGENDARY, 0));
            ps.executeUpdate();
        } catch (SQLException e) {
            getLogger().severe("Error saving player data: " + e.getMessage());
        }
    }

    public synchronized void setPlayerExp(UUID playerUUID, int exp) {
        String sql = "UPDATE players SET exp = ? WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, exp);
            ps.setString(2, playerUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            getLogger().severe("Error updating experience for player " + playerUUID + ": " + e.getMessage());
        }
    }

    public synchronized void setPlayerLevel(UUID playerUUID, int level) {
        String sql = "UPDATE players SET level = ? WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, level);
            ps.setString(2, playerUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            getLogger().severe("Error updating level for player " + playerUUID + ": " + e.getMessage());
        }
    }

    public synchronized void setPlayerCount(UUID playerUUID, CustomFish.Rarity rarity, int count) {
        String sql = "UPDATE players SET " + getRarityColumnName(rarity) + " = ? WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, count);
            ps.setString(2, playerUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            getLogger().severe("Error updating count for player " + playerUUID + ": " + e.getMessage());
        }
    }

    private String getRarityColumnName(CustomFish.Rarity rarity) {
        return switch (rarity) {
            case SCRAP -> "scrap_count";
            case COMMON -> "common_count";
            case UNCOMMON -> "uncommon_count";
            case RARE -> "rare_count";
            case EPIC -> "epic_count";
            case LEGENDARY -> "legendary_count";
        };
    }

    public synchronized void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                getLogger().severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
