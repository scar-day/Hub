package dev.scarday.command;

import dev.scarday.Main;
import dev.scarday.multihub.Type;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import javax.annotation.Nullable;
import java.util.Random;

import static dev.scarday.util.ColorUtil.colorize;

public class HubCommand extends Command {
    Main instance;

    public HubCommand(Main instance) {
        super("hub", null, "лобби");

        this.instance = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer player)) {
            instance.getSLF4JLogger()
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
            val random = new Random();
            val serverName = servers.get(random.nextInt(servers.size()));

            val serverInfo = instance.getProxy()
                    .getServerInfo(serverName);

            sendPlayerServer(player, serverInfo);
        } else if (type == Type.FILL) {
            // 26.09.2024 написать по заполняемости.
            return;
        }
    }

    private void sendPlayerServer(ProxiedPlayer player, @Nullable ServerInfo server) {
        if (server == null) {
            player.sendMessage(new TextComponent(colorize(instance.getConfig()
                    .getMessages()
                    .getNoFoundServer())
            ));
            return;
        }

        if (instance.getConfig().getMessages().isSendMessage()) {
            player.sendMessage(new TextComponent(colorize(instance.getConfig()
                    .getMessages()
                    .getConnect())
            ));
        }

        player.connect(server);
    }
}
