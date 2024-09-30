package dev.scarday.command;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.scarday.Main;
import dev.scarday.multihub.Type;
import lombok.val;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static dev.scarday.util.ColorUtil.colorize;

public class HubCommand implements RawCommand {
    Main instance;
    public HubCommand(Main instance) {
        this.instance = instance;
    }

    public CommandMeta meta() {
        val commandManager = instance.getServer().getCommandManager();

        return commandManager.metaBuilder("hub")
                .aliases("lobby")
                .plugin(instance)
                .build();
    }

    @Override
    public void execute(Invocation invocation) {
        val source = invocation.source();

        if (!(source instanceof Player player)) {
            instance.getLogger().info("Command is available only to the player!");
            return;
        }

        val servers = instance.getConfig()
                .getMultiHub()
                .getServers();

        if (servers.isEmpty()) {
            val serversEmpty = instance.getConfig()
                    .getMessages()
                    .getListEmpty();

            player.sendMessage(Component.text(colorize(serversEmpty)));
            return;
        }

        val type = instance.getConfig()
                .getMultiHub()
                .getType();

        if (type == Type.NONE) {
            val serverName = servers.get(0);

            val serverInfo = instance.getServer().getServer(serverName);

            if (serverInfo.isEmpty()) {
                player.sendMessage(Component.text(instance.getConfig()
                        .getMessages()
                        .getNoFoundServer()));
                return;
            }

            sendPlayerServer(player, serverInfo.get());
        } else if (type == Type.RANDOM) {
            val random = ThreadLocalRandom.current();
            val serverName = servers.get(random.nextInt(servers.size()));

            val serverInfo = instance.getServer().getServer(serverName);

            if (serverInfo.isEmpty()) {
                player.sendMessage(Component.text(instance.getConfig()
                        .getMessages()
                        .getNoFoundServer()));
                return;
            }

            sendPlayerServer(player, serverInfo.get());
        } else if (type == Type.FILL) {
            val multiServers = servers.stream()
                    .map(server -> instance.getServer().getServer(server))
                    .flatMap(Optional::stream)
                    .toList();

            multiServers.stream()
                    .min(Comparator.comparingInt(server -> server.getPlayersConnected().size()))
                    .ifPresent(leastLoadedServer -> sendPlayerServer(player, leastLoadedServer));
        }
    }

    private void sendPlayerServer(Player player, @Nullable RegisteredServer server) {
        if (server == null) {
            player.sendMessage(Component.text(instance.getConfig()
                    .getMessages()
                    .getNoFoundServer())
            );
            return;
        }

        val isSendMessage = instance.getConfig()
                .getMessages()
                .isSendMessage();

        if (isSendMessage) {
            player.sendMessage(Component.text(colorize(instance.getConfig()
                    .getMessages()
                    .getConnect()
            )));
        }

        player.createConnectionRequest(server);
    }

}
