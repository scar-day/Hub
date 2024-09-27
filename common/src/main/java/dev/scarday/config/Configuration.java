package dev.scarday.config;

import dev.scarday.multihub.Type;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Configuration extends OkaeriConfig {
    Messages messages = new Messages();

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Messages extends OkaeriConfig {
        boolean sendMessage = true;
        String connect = "&aПодключаю вас в хаб";
        String connected = "&cВы итак в этом хабе!";
        String noFoundServer = "&cПохоже что данного сервера нету в списке, сообщите администрации!";

        String listEmpty = "&cНе удалось найти нужный хаб, сообщите администрации!";
    }

    MultiHub multiHub = new MultiHub();
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MultiHub extends OkaeriConfig {
        @Comment("Список серверов куда будем закидывать игрока")
        List<String> servers = Arrays.asList("lobby", "lobby2");

        @Comment({
                " ",
                "NONE - по первому индексу",
                "RANDOM - пробегаемся по списку",
                "FILL - по заполняемости, если у сервера мало онлайн перекинет туда.",
                " "
        })
        Type type = Type.NONE;
    }
}