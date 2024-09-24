package dev.scarday.config;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;

@Getter
public class Configuration extends OkaeriConfig {
    private String server = "hub";

    private Messages messages = new Messages();

    @Getter
    public class Messages extends OkaeriConfig {
        private boolean sendMessage = true;
        private String connect = "&aПодключаю вас в хаб";
        private String connected = "&cВы итак в этом хабе!";
        private String noFoundServer = "&cПохоже что данного сервера нету в списке, сообщите администрации!";
    }
}