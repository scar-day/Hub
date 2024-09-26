package dev.scarday.config;

import dev.scarday.multihub.Type;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Configuration extends OkaeriConfig {
    private Messages messages = new Messages();

    @Getter
    public static class Messages extends OkaeriConfig {
        private boolean sendMessage = true;
        private String connect = "&aПодключаю вас в хаб";
        private String connected = "&cВы итак в этом хабе!";
        private String noFoundServer = "&cПохоже что данного сервера нету в списке, сообщите администрации!";

        private String listEmpty = "&cНе удалось найти нужный хаб, сообщите администрации!";
    }

    private MultiHub multiHub = new MultiHub();
    @Getter
    public static class MultiHub extends OkaeriConfig {
        @Comment("Список серверов куда будем закидывать игрока")
        private List<String> servers = Arrays.asList("lobby", "lobby2");
                @Comment(
                        {
                                " ",
                                "NONE - по первому индексу",
                                "RANDOM - пробегаемся по списку",
                                "FILL - по заполняемости, если у сервера мало онлайн перекинет туда.",
                                " "
                        }
                )
        private Type type = Type.NONE;
    }
}