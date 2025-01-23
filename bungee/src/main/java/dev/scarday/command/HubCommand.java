package dev.scarday.command;

import dev.scarday.Main;
import dev.scarday.config.Configuration;
import dev.scarday.util.ColorUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HubCommand extends Command {
    Main instance;
    Configuration configuration;
    BungeeAudiences audience;

    public HubCommand(Main instance) {
        super("hub", null, "лобби");

        this.instance = instance;
        this.configuration = instance.getConfig();
        this.audience = instance.getAdventure();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                instance.reloadConfig();
                val message = ColorUtility.colorize("<green>Successfully reloaded config!");

                instance.getLogger()
                        .info(LegacyComponentSerializer.legacySection().serialize(message));
                return;
            }

            instance.getLogger()
                    .info("Command is available only to the player!");
            return;
        }

        val player = (ProxiedPlayer) commandSender;

        val servers = configuration.getMultiHub()
                .getServers();

        if (servers.isEmpty()) {
            val serversEmpty = configuration.getMessages()
                    .getListEmpty();

            title(player, configuration.getTitle().getError());

            val message = ColorUtility.colorize(serversEmpty);

            sendMessage(player, message);
            return;
        }

        val proxy = instance.getProxy();

        Optional<ServerInfo> serverInfo = Optional.empty();

        switch (configuration.getMultiHub().getType()) {
            case FILL: {
                serverInfo = servers.stream()
                        .map(proxy::getServerInfo)
                        .filter(Objects::nonNull)
                        .min(Comparator.comparingInt(server -> server.getPlayers().size()));
                break;
            }
            case RANDOM: {
                val random = ThreadLocalRandom.current();
                val serverName = servers.get(random.nextInt(servers.size()));
                serverInfo = Optional.ofNullable(proxy.getServerInfo(serverName));
                break;
            }
            case NONE: {
                val serverName = servers.get(0);
                serverInfo = Optional.ofNullable(proxy.getServerInfo(serverName));
                break;
            }
        }

        serverInfo.ifPresentOrElse(
                server -> sendPlayerServer(player, server),
                () -> sendMessage(player, ColorUtility.colorize("<red>Не удалось найти нужный вам сервер.")));
    }

    private void sendPlayerServer(ProxiedPlayer player, ServerInfo server) {
        val isSendMessage = configuration.getMessages().isSendMessage();

        if (player.getServer().getInfo() == server) {
            title(player, configuration.getTitle().getConnected());

            val message = configuration.getMessages()
                    .getConnected();

            val componentMessage = ColorUtility.colorize(message);

            sendMessage(player, componentMessage);
            return;
        }

        if (isSendMessage) {
            val message = configuration.getMessages()
                    .getConnect();

            val componentMessage = ColorUtility.colorize(message);

            title(player, configuration.getTitle().getConnect());

            sendMessage(player, componentMessage);
        }

        player.connect(server);
    }

    private void title(ProxiedPlayer player, String text) {
        val split = text.split(";");

        val title = split[0].isEmpty() ? "" : split[0];
        val subtitle = split.length > 1 ? split[1] : "";

        val titleComponent = ColorUtility.colorize(title);
        val subtitleComponent = ColorUtility.colorize(subtitle);

        val titleAdventure = Title.title(titleComponent, subtitleComponent);

        audience.player(player)
                .showTitle(titleAdventure);
    }

    private void sendMessage(ProxiedPlayer player, Component message) {
        audience.player(player)
                .sendMessage(message);
    }
}
