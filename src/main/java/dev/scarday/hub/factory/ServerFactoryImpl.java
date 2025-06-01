package dev.scarday.hub.factory;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.scarday.Main;
import dev.scarday.hub.type.Type;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ServerFactoryImpl implements ServerFactory {
    Main instance;

    public Optional<RegisteredServer> getServer(List<String> servers, Type typeHub) {
        ProxyServer proxy = instance.getProxy();
        return switch (typeHub) {
            case FILL: {
                List<RegisteredServer> multiServers = servers.stream()
                        .map(proxy::getServer)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

                yield multiServers.stream()
                        .min(Comparator.comparingInt(server -> server.getPlayersConnected().size()));
            }
            case RANDOM: {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                String serverName = servers.get(random.nextInt(servers.size()));

                yield proxy.getServer(serverName);
            }
            case NONE: {
                val serverName = servers.get(0);

                yield proxy.getServer(serverName);
            }
        };
    }
}
