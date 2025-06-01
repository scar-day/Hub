package dev.scarday.command;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.scarday.Main;
import dev.scarday.config.Configuration;
import dev.scarday.util.ColorUtility;
import lombok.val;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class HubCommand implements RawCommand {
    Main instance;
    Configuration configuration;

    public HubCommand(Main instance) {
        this.instance = instance;
        this.configuration = instance.getConfig();
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

        if (!(source instanceof Player player)) {
            val args = invocation.arguments().split(" ");
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                instance.reloadConfig();
                source.sendMessage(ColorUtility.colorize("<green>Successfully reloaded config!"));
                return;
            }

            instance.getLogger().info("Command is available only to the player!");
            return;
        }

        val servers = configuration.getMultiHub()
                .getServers();

        if (servers.isEmpty()) {
            val serversEmpty = configuration.getMessages()
                    .getListEmpty();

            sendTitle(player, configuration.getTitle().getError());

            player.sendMessage(ColorUtility.colorize(serversEmpty));
            return;
        }

        val type = configuration.getMultiHub()
                .getType();

        Optional<RegisteredServer> serverInfo = instance.getServerFactory().getServer(servers, type);

        serverInfo.ifPresentOrElse(server -> sendPlayerServer(player, server),
                () -> player.sendMessage(ColorUtility.colorize("<red>Не удалось найти нужный вам сервер.")));
    }

    private void sendPlayerServer(Player player, @NotNull RegisteredServer server) {
        val isSendMessage = configuration.getMessages()
                .isSendMessage();

        if (player.getCurrentServer().isPresent()) {
            val currentServer = player.getCurrentServer().get();
            val registeredServer = currentServer.getServer();

            if (registeredServer == server) {
                sendTitle(player, configuration.getTitle().getConnected());

                player.sendMessage(ColorUtility.colorize(configuration.getMessages().getConnected()));
                return;
            }
        }

        if (isSendMessage) {
            sendTitle(player, configuration.getTitle().getConnect());
            val message = configuration.getMessages()
                    .getConnect();

            player.sendMessage(ColorUtility.colorize(message));
        }

        player.createConnectionRequest(server)
                .fireAndForget();
    }

    private void sendTitle(Player player, String text) {
        val split = text.split(";");

        val title = split[0].isEmpty() ? "" : split[0];
        val subtitle = split.length > 1 ? split[1] : "";

        player.showTitle(Title.title(ColorUtility.colorize(title), ColorUtility.colorize(subtitle)));
    }
}