package dev.scarday.hub.factory;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.scarday.hub.type.Type;

import java.util.List;
import java.util.Optional;

public interface ServerFactory {
    Optional<RegisteredServer> getServer(List<String> servers, Type type);
}
