package dev.scarday.command;

import dev.scarday.Main;
import dev.scarday.util.ColorUtil;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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

        val server = instance.getConfig()
                .getServer();

        val servers = instance.getProxy()
                .getServersCopy()
                .keySet();

        if (!servers.contains(server)) {
            val noFound = instance.getConfig()
                    .getMessages().getNoFoundServer();

            commandSender.sendMessage(new TextComponent(ColorUtil.colorize(noFound)));
            return;
        }

        val serverInfo = instance.getProxy()
                .getServerInfo(server);

        val connected = instance.getConfig()
                .getMessages().getConnected();

        if (player.getServer().getInfo() == serverInfo) {
            commandSender.sendMessage(new TextComponent(ColorUtil.colorize(connected)));
            return;
        }

        player.sendMessage(new TextComponent(ColorUtil.colorize(
                instance.getConfig().getMessages().getConnect())));
        player.connect(serverInfo);
    }
}
