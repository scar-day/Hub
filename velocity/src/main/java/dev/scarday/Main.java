package dev.scarday;

import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.scarday.command.HubCommand;
import dev.scarday.configuration.Config;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.serdes.adventure.SerdesAdventure;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;
import lombok.val;
import org.slf4j.Logger;
import javax.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;

import java.io.File;
import java.nio.file.Path;

@Getter
public class Main {
    private final ProxyServer server;
    private final Logger logger;
    @Getter
    private Config config;

    private final File dataDirectory;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
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
        val commandManager = server.getCommandManager();
        val command = new HubCommand(this);

        commandManager.register(command.meta(), command);
        logger.info("Command '{}' is success loaded!", command.meta().getAliases()
                .toArray()[0]
        );
    }

    private void initConfig() {
        try {
            this.config = ConfigManager.create(Config.class, (it) -> {
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
}
