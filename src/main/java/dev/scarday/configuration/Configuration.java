package dev.scarday.configuration;

import dev.scarday.Main;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Configuration {
    public static String fileName = "config.yml";
    private static net.md_5.bungee.config.Configuration config;

    public void saveDefaultConfig() {
        try {
            if (!Main.getInstance().getDataFolder().exists()) {
                Main.getInstance().getDataFolder().mkdir();
            }
            File file = new File(Main.getInstance().getDataFolder(), fileName);
            if (!file.exists()) {
                Files.copy(Main.getInstance().getResourceAsStream(fileName), file.toPath());
            }
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadConfig() {
        loadConfig();
    }
    public  void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(Main.getInstance().getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public net.md_5.bungee.config.Configuration getConfig() {
        return config;
    }
}
