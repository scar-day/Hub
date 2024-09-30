package dev.scarday.util;

import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ColorUtil {
    public static String colorize(@NotNull String message) {
        val deserialize = MiniMessage.get()
                .deserialize(message);

        return LegacyComponentSerializer.legacySection().serialize(deserialize);
    }
}
