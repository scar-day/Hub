package dev.scarday.command;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.scarday.Main;
import lombok.val;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static dev.scarday.util.ColorUtil.colorize;

public class HubCommand implements RawCommand {
    Main instance;
    public HubCommand(Main instance) {
        this.instance = instance;
    }

    public CommandMeta meta() {
        val commandManager = instance.getProxy().getCommandManager();

        return commandManager.metaBuilder("hub")
                .aliases("lobby")
                .plugin(instance)
                .build();
    }

    @Override
    public void execute(Invocation invocation) {
        val source = invocation.source();

        if (!(source instanceof Player)) {
            instance.getLogger().info("Command is available only to the player!");
            return;
        }

        val player = (Player) source;

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

        val proxy = instance.getProxy();

        Optional<RegisteredServer> serverInfo = Optional.empty();
        switch (type) {
            case FILL: {
                val multiServers = servers.stream()
                        .map(proxy::getServer)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

                serverInfo = multiServers.stream()
                        .min(Comparator.comparingInt(server -> server.getPlayersConnected().size()));
            }
            case RANDOM: {
                val random = ThreadLocalRandom.current();
                val serverName = servers.get(random.nextInt(servers.size()));

                serverInfo = proxy.getServer(serverName);
            }
            case NONE: {
                val serverName = servers.get(0);

                serverInfo = proxy.getServer(serverName);
            }
        }

        serverInfo.ifPresentOrElse(server -> sendPlayerServer(player, server),
                () -> player.sendMessage(Component.text(colorize("<red>Не удалось найти сервер."))));
    }

    private void sendPlayerServer(Player player, @NotNull RegisteredServer server) {
        val isSendMessage = instance.getConfig()
                .getMessages()
                .isSendMessage();

        if (isSendMessage) {
            val message = instance.getConfig()
                    .getMessages()
                    .getConnect();
            player.sendMessage(Component.text(colorize(message)));
        }

        player.createConnectionRequest(server)
                .fireAndForget();
    }


}
