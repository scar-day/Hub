package dev.scarday.command;

import dev.scarday.Main;
import dev.scarday.multihub.Type;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static dev.scarday.util.ColorUtil.colorize;

public class HubCommand extends Command {
    Main instance;

    public HubCommand(Main instance) {
        super("hub", null, "лобби");

        this.instance = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer player)) {
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

        if (type == Type.NONE) {
            val serverName = servers.get(0);

            val serverInfo = instance.getProxy()
                    .getServerInfo(serverName);

            sendPlayerServer(player, serverInfo);
        } else if (type == Type.RANDOM) {
            val random = ThreadLocalRandom.current();
            val serverName = servers.get(random.nextInt(servers.size()));

            val serverInfo = instance.getProxy()
                    .getServerInfo(serverName);

            sendPlayerServer(player, serverInfo);
        } else if (type == Type.FILL) {
            val multiServers = servers.stream()
                    .map(server -> ProxyServer.getInstance().getServerInfo(server))
                    .filter(Objects::nonNull)
                    .toList();

            multiServers.stream()
                    .min(Comparator.comparing(server -> server.getPlayers().size()))
                    .ifPresent(leastLoadedServers -> sendPlayerServer(player, leastLoadedServers));
        }
    }

    private void sendPlayerServer(ProxiedPlayer player, @Nullable ServerInfo server) {
        if (server == null) {
            player.sendMessage(new TextComponent(instance.getConfig()
                    .getMessages()
                    .getNoFoundServer())
            );
            return;
        }

        val isSendMessage = instance.getConfig().getMessages().isSendMessage();

        if (isSendMessage) {
            player.sendMessage(new TextComponent(colorize(instance.getConfig()
                    .getMessages()
                    .getConnect()
            )));
        }

        player.connect(server);
    }
}
