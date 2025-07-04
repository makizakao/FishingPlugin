package org.hark7.fishingPlugin.util;

import net.kyori.adventure.text.Component;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.Settings;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.SerializeUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.settings.SimpleLocalization;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;
import java.util.*;
import java.util.jar.JarFile;

public class CustomLang {
    private static final String defaultLanguage = Settings.Language.defaultLanguage;
    private static final String[] languages = Settings.Language.languages;
    private static final Map<String, YamlConfig> languageFiles = new HashMap<>();

    public static void init(FishingPlugin plugin, File pluginFile) {
        tryCreateLanguageFiles(plugin, pluginFile);
        for (String lang : languages) {
            var langFile = YamlConfig.fromFile(new File(
                    plugin.getDataFolder() + "/localization", lang + ".yml"));
            languageFiles.put(lang, langFile);
        }
    }

    private static void tryCreateLanguageFiles(FishingPlugin plugin, File pluginFile) {
        var folder = "localization";
        var destDir = new File(plugin.getDataFolder(), folder);
        if(destDir.exists()) {
            plugin.getLogger().info("Localization folder already exists.");
        } else {
            destDir.mkdirs();
        }
        try (var jar = new JarFile(pluginFile)) {
            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                var name = entry.getName();
                if (name.startsWith(folder + "/") && !entry.isDirectory()) {
                    String fileName = name.substring(folder.length() + 1);
                    File destFile = new File(destDir, fileName);
                    if (!destFile.exists()) {
                        plugin.saveResource(name, false);
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create localization files: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static String[] ofArray(String path, String lang) {
        return Optional.ofNullable(languageFiles.get(lang)).orElse(languageFiles.get(defaultLanguage))
                .getIsInList(path, String.class).getList().toArray(new String[0]);
    }

    public static String[] ofArray(String path) {
        return ofArray(path, Settings.Language.defaultLanguage);
    }

    public static List<SimpleComponent> ofSimpleComponentList(String path, String lang, Object... variables) {
        return Common.convert(ofList(path, lang, variables), SimpleComponent::of);
    }

    public static List<SimpleComponent> ofSimpleComponentList(String path, Object... variables) {
        return ofSimpleComponentList(path, Settings.Language.defaultLanguage, variables);
    }

    public static List<String> ofList(String path, String lang, Object... variables) {
        return Arrays.asList(ofArray(path, lang, variables));
    }

    public static List<String> ofList(String path, Object... variables) {
        return ofList(path, Settings.Language.defaultLanguage, variables);
    }

    public static String[] ofArray(String path, String lang, Object... variables) {
        return of(path, lang, variables).split("\n");
    }

    public static SimpleComponent ofSimpleComponent(String path, String lang, Object... variables) {
        return SimpleComponent.of(of(path, lang, variables));
    }

    public static SimpleComponent ofSimpleComponent(String path, Object... variables) {
        return ofSimpleComponent(path, Settings.Language.defaultLanguage, variables);
    }

    public static Component ofComponent(String path, String lang, Object... variables) {
        return Component.text(of(path, lang, variables));
    }

    public static Component ofComponent(String path, Object... variables) {
        return ofComponent(path, Settings.Language.defaultLanguage, variables);
    }

    public static String of(String path, String lang, Object... variables) {
        String key = getStringStrict(path, lang);
        key = Messenger.replacePrefixes(key);
        key = translate(key, variables);
        return key;
    }

    public static String of(String path, Object... variables) {
        return of(path, Settings.Language.defaultLanguage, variables);
    }

    private static String translate(String key, Object... variables) {
        Valid.checkNotNull(key, "Cannot translate a null key with variables " + Common.join(variables));
        if (variables != null) {
            for (int i = 0; i < variables.length; ++i) {
                Object variable = variables[i];
                variable = Common.getOrDefaultStrict(SerializeUtil.serialize(SerializeUtil.Mode.YAML, variable), SimpleLocalization.NONE);
                Valid.checkNotNull(variable, "Failed to replace {" + i + "} as " + variable + " (raw = " + variables[i] + ")");
                key = key.replace("{" + i + "}", variable.toString());
            }
        }

        return key;
    }

    private static String getStringStrict(String path, String lang) {
        String key = Optional.ofNullable(languageFiles.get(lang))
                .orElse(languageFiles.get(defaultLanguage)).getString(path);
        Valid.checkNotNull(key, "Missing localization key");
        return key;
    }
}

