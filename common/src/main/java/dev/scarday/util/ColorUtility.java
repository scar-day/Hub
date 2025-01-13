package dev.scarday.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ColorUtility {
    private MiniMessage MM = MiniMessage.miniMessage();

    public Component colorize(@NotNull String message) {
        return MM.deserialize(message);
    }
}
