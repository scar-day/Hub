package dev.scarday.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ColorUtility {
    public Component colorize(@NotNull String message) {
        val mm = MiniMessage.miniMessage();

        return mm.deserialize(message);
    }

    public String colorizeToString(@NotNull Component message) {
        return LegacyComponentSerializer.legacySection().serialize(message);
    }
}
