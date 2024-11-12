package dev.scarday.util;

import lombok.val;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class ColorUtil {
    public static String colorize(@NotNull String message) {
        val mm = MiniMessage.miniMessage();
        val deserialize = mm.deserialize(message);

        return LegacyComponentSerializer.legacySection()
                .serialize(deserialize);
    }
}
