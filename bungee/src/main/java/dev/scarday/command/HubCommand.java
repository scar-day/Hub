package dev.scarday.command;

import dev.scarday.Main;
import dev.scarday.config.Configuration;
import dev.scarday.util.ColorUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HubCommand extends Command { // shitting in BaseComponent!!
    Main instance;
    Configuration configuration;

    public HubCommand(Main instance) {
        super("hub", null, "лобби");

        this.instance = instance;
        this.configuration = instance.getConfig();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {

                instance.reloadConfig();
                val message = ColorUtility.colorize("<green>Successfully reloaded!");

                commandSender.sendMessage(new TextComponent(ColorUtility.colorizeToString(message)));
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

            player.sendMessage(new TextComponent(ColorUtility.colorizeToString(message)));
            return;
        }

        val proxy = instance.getProxy();

        Optional<ServerInfo> serverInfo = Optional.empty();

        try {
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
        } catch (Exception e) {
            title(player, configuration.getTitle().getError());
            val message = ColorUtility.colorize(configuration.getMessages().getNoFoundServer());
            player.sendMessage(new TextComponent(ColorUtility.colorizeToString(message)));
        }

        serverInfo.ifPresentOrElse(
                server -> sendPlayerServer(player, server),
                () -> {
                    val componentMessage = ColorUtility.colorize("<red>Не удалось найти нужный вам сервер.");

                    player.sendMessage(new TextComponent(ColorUtility.colorizeToString(componentMessage)));
                });
    }

    private void sendPlayerServer(ProxiedPlayer player, ServerInfo server) {
        val isSendMessage = configuration.getMessages().isSendMessage();

        if (player.getServer().getInfo() == server) {
            title(player, configuration.getTitle().getConnected());

            val message = configuration.getMessages()
                    .getConnected();

            val componentMessage = ColorUtility.colorize(message);

            player.sendMessage(new TextComponent(ColorUtility.colorizeToString(componentMessage)));
            return;
        }

        if (isSendMessage) {
            val message = configuration.getMessages()
                    .getConnect();

            val componentMessage = ColorUtility.colorize(message);

            title(player, configuration.getTitle().getConnect());
            player.sendMessage(new TextComponent(ColorUtility.colorizeToString(componentMessage)));
        }

        player.connect(server);
    }

    private void title(ProxiedPlayer player, String text) {
        val titleInstance = instance.getProxy().createTitle();
        val split = text.split(";");

        val title = split[0].isEmpty() ? "" : split[0];
        val subtitle = split.length > 1 ? split[1] : "";

        val titleComponent = ColorUtility.colorize(title);
        val subtitleComponent = ColorUtility.colorize(subtitle);

        titleInstance
                .title(new TextComponent(ColorUtility.colorizeToString(titleComponent)))
                .subTitle(new TextComponent(ColorUtility.colorizeToString(subtitleComponent)))
                .send(player);
    }
}
