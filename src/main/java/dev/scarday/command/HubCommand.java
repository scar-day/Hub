package dev.scarday.command;

import dev.scarday.Main;
import dev.scarday.util.ColorUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Set;

public class HubCommand extends Command {
    public HubCommand() {
        super("hub", null, "лобби");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if ((commandSender instanceof ProxiedPlayer p)) {
            String lobby = Main.getConfig().getConfig().getString("server");
            Set<String> servers = Main.getInstance().getProxy().getServersCopy().keySet();

            if (!servers.contains(lobby)) {
                String no_found = Main.getConfig().getConfig().getString("messages.no-found-server");

                commandSender.sendMessage(new TextComponent(ColorUtil.colorize(no_found)));
                return;
            }

            ServerInfo serverInfo = Main.getInstance().getProxy().getServerInfo(lobby);
            String error_message = Main.getConfig().getConfig().getString("messages.connected");

            if (p.getServer().getInfo() == serverInfo) {
                commandSender.sendMessage(new TextComponent(ColorUtil.colorize(error_message)));
                return;
            }

            String successfully = Main.getConfig().getConfig().getString("messages.connect");

            p.sendMessage(new TextComponent(ColorUtil.colorize(successfully)));
            p.connect(serverInfo);
        } else {
            String console_message = Main.getConfig().getConfig().getString("messages.console-message");
            commandSender.sendMessage(new TextComponent(ColorUtil.colorize(console_message)));
        }
    }
}
