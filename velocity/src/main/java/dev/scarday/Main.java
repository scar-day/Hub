package dev.scarday;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.scarday.command.HubCommand;
import dev.scarday.config.Configuration;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.serdes.adventure.SerdesAdventure;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import javax.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "hub", name = "Hub", version = BuildConstant.VERSION, authors = "ScarDay")
@Getter
public class Main {
    final ProxyServer proxy;
    final Logger logger;
    File dataDirectory;

    @NotNull
    Configuration config = new Configuration();

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = server;
        this.logger = logger;

        this.dataDirectory = dataDirectory
                .toFile();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        initConfig();
        registerCommands();
    }

    private void registerCommands() {
        val commandManager = getProxy().getCommandManager();
        val command = new HubCommand(this);

        commandManager.register(command.meta(), command);
    }

    private void initConfig() {
        try {
            this.config = ConfigManager.create(Configuration.class, (it) -> {
                it.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesAdventure());
                it.withBindFile(new File(getDataDirectory(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });

            logger.info("Configuration 'config.yml' is loaded!");
        } catch (Exception exception) {
            getLogger().error("Error loading config.yml", exception);
        }
    }

    public void reloadConfig() {
        this.config = (Configuration) config.load();
    }

}
