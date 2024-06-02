package dev.scarday;

import dev.scarday.command.HubCommand;
import dev.scarday.configuration.Configuration;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    private static Main instance;
    private static Configuration config;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();

        registerCommands();
    }

    public void loadConfig() {
        config = new Configuration();
        config.saveDefaultConfig();
    }

    public void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new HubCommand());
    }

    @Override
    public void onDisable() {
    }

    public static Configuration getConfig() {
        return config;
    }

    public static Main getInstance() {
        return instance;
    }

}
