package org.hark7.fishingPlugin.database;

import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.Settings;
import org.hark7.fishingPlugin.type.item.Fishable.*;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleDatabase;

import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQLiteDatabaseを管理するクラス。
 * <p>
 * このクラスは、プレイヤーの釣りレベル、経験値、および釣った魚の数をデータベースに保存し、取得します。
 */
public class PlayerDataController extends SimpleDatabase {
    private final FishingPlugin plugin;

    /**
     * コンストラクタ
     *
     * @param plugin FishingPluginのインスタンス
     * @throws IllegalArgumentException pluginがnullの場合
     */
    public PlayerDataController(FishingPlugin plugin) {
        super();
        if (plugin == null) {
            throw new IllegalArgumentException("FishingPlugin cannot be null");
        }
        this.plugin = plugin;
        connect();
    }

    private void connect() {
        var path = Settings.Database.databasePath;
        if (path == null || path.isEmpty()) {
            plugin.getLogger().severe("Database path is not set in the configuration.");
            return;
        }
        super.connect(path);
    }

    @Override
    protected void onConnected() {
        createPlayersTable();
        createCountsTable();
    }

    /**
     * プレイヤーデータテーブルを作成します。
     */
    private void createPlayersTable() {
        createTable(TableCreator.of("players")
                .add("uuid", "TEXT")
                .addNotNull("name", "TEXT")
                .addDefault("level", "INTEGER", "0")
                .addDefault("exp", "INTEGER", "0")
                .setPrimaryColumn("uuid"));
    }

    /**
     * 釣りデータのカウントテーブルを作成します。
     */
    private void createCountsTable() {
        update("CREATE TABLE IF NOT EXISTS counts (" +
                "uuid TEXT PRIMARY KEY," +
                "scrap INTEGER NOT NULL DEFAULT 0," +
                "common INTEGER NOT NULL DEFAULT 0," +
                "uncommon INTEGER NOT NULL DEFAULT 0," +
                "rare INTEGER NOT NULL DEFAULT 0," +
                "epic INTEGER NOT NULL DEFAULT 0," +
                "legendary INTEGER NOT NULL DEFAULT 0," +
                "FOREIGN KEY(uuid) REFERENCES players(uuid)" +
                ")"
        );
    }

    /**
     * プレイヤーデータテーブルのバージョンを更新します。
     * <p>
     * 既存のテーブルを新しい形式に変換し、古いテーブルを削除します。
     */
    public void updatePlayersTableVersion() {
        createTable(TableCreator.of("players_new")
                .add("uuid", "TEXT")
                .addNotNull("name", "TEXT")
                .addDefault("level", "INTEGER", "0")
                .addDefault("exp", "INTEGER", "0")
                .setPrimaryColumn("uuid"));
        update("INSERT INTO players_new (uuid, name, level, exp) " +
                "SELECT uuid, name, level, exp FROM players"
        );
        update("DROP TABLE players");
        update("ALTER TABLE players_new RENAME TO players");
    }

    /**
     * 釣りデータのカウントテーブルのバージョンを更新します。
     * <p>
     * 既存のテーブルを新しい形式に変換し、古いテーブルを削除します。
     */
    public void updateCountsTableVersion() {
        update("CREATE TABLE IF NOT EXISTS counts_new (" +
                "uuid TEXT PRIMARY KEY," +
                "scrap INTEGER NOT NULL DEFAULT 0," +
                "common INTEGER NOT NULL DEFAULT 0," +
                "uncommon INTEGER NOT NULL DEFAULT 0," +
                "rare INTEGER NOT NULL DEFAULT 0," +
                "epic INTEGER NOT NULL DEFAULT 0," +
                "legendary INTEGER NOT NULL DEFAULT 0," +
                "FOREIGN KEY(uuid) REFERENCES players(uuid)" +
                ")"
        );
        update("INSERT INTO counts_new (uuid, scrap, common, uncommon, rare, epic, legendary) " +
                "SELECT uuid, scrap, common, uncommon, rare, epic, legendary FROM counts"
        );
        update("DROP TABLE counts");
        update("ALTER TABLE counts_new RENAME TO counts");
    }

    /**
     * プレイヤーデータを取得します。
     * <p>
     * 非同期で実行してください。
     *
     * @param playerUUID プレイヤーのUUID
     * @return PlayerDataオブジェクト、存在しない場合はnull
     */
    public PlayerData getPlayerData(UUID playerUUID) {
        final String sql = "SELECT p.uuid, p.name, p.level, p.exp, " +
                "c.scrap, c.common, c.uncommon, c.rare, c.epic, c.legendary " +
                "FROM players p LEFT JOIN counts c ON p.uuid = c.uuid WHERE p.uuid = '" + playerUUID.toString() + "'";
        final var count = new ConcurrentHashMap<Rarity, Integer>();
        try (var rs = query(sql)) {
            for (Rarity rarity : Rarity.values()) {
                count.put(rarity, rs.getInt(getRarityColumnName(rarity))); // 初期化
            }
            return new PlayerData(
                    rs.getString("name"),
                    rs.getInt("level"),
                    rs.getInt("exp"),
                    count
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 全てのプレイヤーデータを取得します。
     * <p>
     * 非同期で実行してください。
     *
     * @return プレイヤーのUUIDとPlayerDataのマップ
     */
    public Map<UUID, PlayerData> playerDataMap() {
        Map<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();
        final String sql = "SELECT p.uuid, p.name, p.level, p.exp, " +
                "c.scrap, c.common, c.uncommon, c.rare, c.epic, c.legendary " +
                "FROM players p LEFT JOIN counts c ON p.uuid = c.uuid";
        try (var rs = query(sql)) {
            while (rs.next()) {
                UUID playerUUID = UUID.fromString(rs.getString("uuid"));
                var count = new ConcurrentHashMap<Rarity, Integer>();
                count.put(Rarity.SCRAP, rs.getInt("scrap"));
                count.put(Rarity.COMMON, rs.getInt("common"));
                count.put(Rarity.UNCOMMON, rs.getInt("uncommon"));
                count.put(Rarity.RARE, rs.getInt("rare"));
                count.put(Rarity.EPIC, rs.getInt("epic"));
                count.put(Rarity.LEGENDARY, rs.getInt("legendary"));
                PlayerData data = new PlayerData(
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getInt("exp"),
                        count
                );
                playerDataMap.put(playerUUID, data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Map.copyOf(playerDataMap);
    }

    /**
     * プレイヤーデータを設定します。
     * <p>
     * プレイヤーのUUID、名前、レベル、経験値、および釣った魚の数をデータベースに保存します。
     * 非同期で実行してください。
     *
     * @param playerUUID プレイヤーのUUID
     * @param playerData PlayerDataオブジェクト
     */
    public void insertPlayerData(UUID playerUUID, PlayerData playerData) {
        String playerSql =
                "INSERT INTO players (uuid, name, level, exp) VALUES (?, ?, ?, ?) " +
                        "ON CONFLICT(uuid) DO UPDATE SET name=excluded.name, level=excluded.level, exp=excluded.exp";
        String countSql =
                "INSERT INTO counts (uuid, scrap, common, uncommon, rare, epic, legendary) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT(uuid) DO UPDATE SET " +
                        "scrap=excluded.scrap, common=excluded.common, " +
                        "uncommon=excluded.uncommon, rare=excluded.rare, " +
                        "epic=excluded.epic, legendary=excluded.legendary";
        insert("players", SerializedMap.ofArray(
                "uuid", playerUUID.toString(),
                "name", playerData.playerName(),
                "level", playerData.level(),
                "exp", playerData.exp()));
        insert("counts", SerializedMap.ofArray(
                "uuid", playerUUID.toString(),
                "scrap", playerData.count(Rarity.SCRAP),
                "common", playerData.count(Rarity.COMMON),
                "uncommon", playerData.count(Rarity.UNCOMMON),
                "rare", playerData.count(Rarity.RARE),
                "epic", playerData.count(Rarity.EPIC),
                "legendary", playerData.count(Rarity.LEGENDARY)
        ));
    }

    public void savePlayerData(UUID playerUUID, PlayerData playerData) {
        var name = playerData.playerName();
        var level = playerData.level();
        var exp = playerData.exp();
        var uuid = playerUUID.toString();
        String sql = "UPDATE players SET name = '" + name + "', level = " + level + ", exp = " + exp
                + " WHERE uuid = '" + uuid + "'";
        update(sql);

        var scrap = playerData.count(Rarity.SCRAP);
        var common = playerData.count(Rarity.COMMON);
        var uncommon = playerData.count(Rarity.UNCOMMON);
        var rare = playerData.count(Rarity.RARE);
        var epic = playerData.count(Rarity.EPIC);
        var legendary = playerData.count(Rarity.LEGENDARY);

        String countSql = "UPDATE counts SET scrap =" + scrap + ", common = " + common + ", uncommon = " + uncommon
                + ", rare = " + rare + ", epic = " + epic + ", legendary = " + legendary
                + " WHERE uuid = " + "'" + uuid + "'";
        update(countSql);
    }

    /**
     * プレイヤーの経験値を設定します。
     * <p>
     * 非同期で実行してください。
     *
     * @param playerUUID プレイヤーのUUID
     * @param exp        設定する経験値
     */
    public void savePlayerExp(UUID playerUUID, int exp) {
        String sql = "UPDATE players SET exp = " + exp + " WHERE uuid = '" + playerUUID.toString() + "'";
        update(sql);
    }

    /**
     * プレイヤーのレベルを設定します。
     * <p>
     * 非同期で実行してください。
     *
     * @param playerUUID プレイヤーのUUID
     * @param level      設定するレベル
     */
    public void savePlayerLevel(UUID playerUUID, int level) {
        String sql = "UPDATE players SET level = " + level + " WHERE uuid = '" + playerUUID.toString() + "'";
        update(sql);
    }

    /**
     * プレイヤーの釣った魚の数を設定します。
     * <p>
     * 指定されたレアリティの魚の数を更新します。
     * 非同期で実行してください。
     *
     * @param playerUUID プレイヤーのUUID
     * @param rarity     釣った魚のレアリティ
     * @param count      設定する数
     */
    public void savePlayerCount(UUID playerUUID, Rarity rarity, int count) {
        String sql = "UPDATE counts SET " + getRarityColumnName(rarity)
                + " = " + count + " WHERE uuid = '" + playerUUID.toString() + "'";
        update(sql);
    }

    /**
     * 指定されたレアリティのカラム名を取得します。
     *
     * @param rarity レアリティ
     * @return レアリティに対応するカラム名
     */
    private String getRarityColumnName(Rarity rarity) {
        return rarity.name().toLowerCase();
    }
}
