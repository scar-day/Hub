package dev.scarday;

import dev.scarday.command.HubCommand;
import dev.scarday.config.Configuration;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBungee;
import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import lombok.Getter;
import lombok.val;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

@Getter
public final class Main extends Plugin {
    private Configuration config;

    @Override
    public void onEnable() {
        loadConfig();
        registerCommands();
    }

    @Override
    public void onDisable() {}

    public void loadConfig() {
        this.config = ConfigManager.create(Configuration.class, (it) -> {
            it.withConfigurer(new YamlBungeeConfigurer(), new SerdesBungee());
            it.withBindFile(new File(getDataFolder(), "config.yml"));
            it.saveDefaults();
            it.load(true);
        });

        getLogger().info("Configuration 'config.yml' is loaded!");
    }

    public void registerCommands() {
        val command = new HubCommand(this);
        getProxy().getPluginManager().registerCommand(this, command);
    }

    public void reloadConfig() {
        this.config = (Configuration) config.load();
    }
}
