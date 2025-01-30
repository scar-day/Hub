package dev.scarday;

import dev.scarday.command.HubCommand;
import dev.scarday.config.Configuration;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBungee;
import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

@Getter
public class Main extends Plugin {
    private Configuration config;
    private BungeeAudiences adventure;

    public @NonNull BungeeAudiences getAdventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Cannot retrieve audience provider while plugin is not enabled");
        }
        return this.adventure;
    }


    @Override
    public void onEnable() {
        this.adventure = BungeeAudiences.create(this);
        loadConfig();
        registerCommands();
    }

    @Override
    public void onDisable() {}

    private void loadConfig() {
        this.config = ConfigManager.create(Configuration.class, (it) -> {
            it.withConfigurer(new YamlBungeeConfigurer(), new SerdesBungee());
            it.withBindFile(new File(getDataFolder(), "config.yml"));
            it.saveDefaults();
            it.load(true);
        });
    }

    private void registerCommands() {
        val command = new HubCommand(this);
        getProxy().getPluginManager().registerCommand(this, command);
    }

    public void reloadConfig() {
        this.config = (Configuration) config.load();
    }
}
