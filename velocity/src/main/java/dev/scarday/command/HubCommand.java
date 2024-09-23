package dev.scarday.command;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import dev.scarday.Main;
import lombok.val;
import net.kyori.adventure.text.Component;

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

        if (source instanceof Player player) {
            instance.getServer().getPlayer(player.getUsername());

            val optionalServer = instance.getServer()
                    .getServer(instance.getConfig()
                            .getServer());

            if (optionalServer.isEmpty()) {
                player.sendMessage(Component.text(instance.getConfig()
                        .getMessages()
                        .getNoFoundServer())
                );
                return;
            }

            val server = optionalServer.get();

            val userServer = player.getCurrentServer();

            if (userServer.isPresent()) {
                val serverFind = server.getServerInfo();

                val userServerInfo = userServer.get()
                        .getServerInfo();

                if (userServerInfo == serverFind) {
                    player.sendMessage(Component.text(instance.getConfig()
                            .getMessages()
                            .getConnected())
                    );
                    return;
                }

                if (instance.getConfig().getMessages().isSendMessage()) {
                    player.sendMessage(Component.text(instance.getConfig()
                            .getMessages()
                            .getConnect())
                    );
                    player.createConnectionRequest(server);
                }
            }
        } else {
            instance.getLogger().info("Command is available only to the player!");
        }
    }


}
