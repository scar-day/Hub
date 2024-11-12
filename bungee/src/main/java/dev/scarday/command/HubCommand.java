package dev.scarday.command;

import dev.scarday.Main;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static dev.scarday.util.ColorUtil.colorize;

public class HubCommand extends Command {
    Main instance;

    public HubCommand(Main instance) {
        super("hub", null, "лобби");

        this.instance = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                instance.reloadConfig();
                return;
            }
            instance.getLogger()
                    .info("Command is available only to the player!");
            return;
        }

        val servers = instance.getConfig()
                .getMultiHub()
                .getServers();

        if (servers.isEmpty()) {
            val serversEmpty = instance.getConfig()
                    .getMessages()
                    .getListEmpty();

            commandSender.sendMessage(new TextComponent(colorize(serversEmpty)));
            return;
        }

        val type = instance.getConfig()
                .getMultiHub()
                .getType();

        val proxy = instance.getProxy();

        Optional<ServerInfo> serverInfo = Optional.empty();
        switch (type) {
            case FILL: {
                val multiServers = servers.stream()
                        .map(proxy::getServerInfo)
                        .collect(Collectors.toList());

                serverInfo = multiServers.stream()
                        .min(Comparator.comparingInt(server -> server.getPlayers().size()));
            }
            case RANDOM: {
                val random = ThreadLocalRandom.current();
                val serverName = servers.get(random.nextInt(servers.size()));

                serverInfo = Optional.of(proxy.getServerInfo(serverName));
            }
            case NONE: {
                val serverName = servers.get(0);

                serverInfo = Optional.of(proxy.getServerInfo(serverName));
            }
        }

        val player = (ProxiedPlayer) commandSender;

        serverInfo.ifPresentOrElse(
                server -> sendPlayerServer(player, server),
                () -> player.sendMessage(new TextComponent(colorize("<red>Не удалось найти сервер."))));
    }

    private void sendPlayerServer(ProxiedPlayer player, ServerInfo server) {
        val isSendMessage = instance.getConfig().getMessages().isSendMessage();

        if (isSendMessage) {
            val message = instance.getConfig()
                    .getMessages()
                    .getConnect();
            player.sendMessage(new TextComponent(colorize(message)));
        }

        player.connect(server);
    }
}
