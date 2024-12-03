package dev.scarday.config;

import dev.scarday.multihub.Type;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Configuration extends OkaeriConfig {
    Messages messages = new Messages();

    @Getter
    @FieldDefaults(level = AccessLevel.PACKAGE)
    public static class Messages extends OkaeriConfig {
        boolean sendMessage = true;
        String connect = "<green>Подключаю вас в хаб";
        String connected = "<red>Вы итак в этом хабе!";
        String noFoundServer = "<red>Похоже что данного сервера нету в списке, сообщите администрации!";

        String listEmpty = "<red>Не удалось найти нужный хаб, сообщите администрации!";
    }

    Title title = new Title();

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Title extends OkaeriConfig {
        @Comments(value = @Comment(value = "до ; это title, после subtitle"))
        String connect = "<green>Подключаю вас!;<white>Ожидайте.";
        String connected = "<red>Вы итак на этом сервере!";

        String error = "<red>Не удалось вас подключить!;<red>Читайте чат!";
    }

    MultiHub multiHub = new MultiHub();
    @Getter
    @FieldDefaults(level = AccessLevel.PACKAGE)
    public static class MultiHub extends OkaeriConfig {
        @Comment("Список серверов куда будем закидывать игрока")
        List<String> servers = List.of("lobby", "lobby2");

        @Comment({
                " ",
                "NONE - по первому индексу в листе",
                "RANDOM - Рандомный сервер из списка",
                "FILL - по заполняемости сервера, закинет на самый не заполненный сервер",
                " "
        })
        Type type = Type.NONE;
    }
}