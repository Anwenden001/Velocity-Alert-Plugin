package de.anwenden.alert;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;


public class Settings {

    private final Path dataDirectory;

    private final HashMap<String, String> settings;

    public Settings(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.settings = new HashMap<>();
        load();
    }

    public String getAlertDefault() {
        return settings.getOrDefault("alert_default", "Legacy");
    }

    public String getAlertRawDefault() {
        return settings.getOrDefault("alert_raw_default", "Json");
    }

    public TextComponent getAlertJsonDefault() {
        String s = settings.getOrDefault("alert_json_default", "{\"text\": \"[ALERT] \"}");
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return (TextComponent) serializer.deserialize(s);
    }

    public TextComponent getAlertXmlDefault() {
        String s = settings.getOrDefault("alert_xml_default", "<#f60002>[ALERT]</#f60002> ");
        MiniMessage serializer = MiniMessage.miniMessage();
        return (TextComponent) serializer.deserialize(s);
    }

    public TextComponent getAlertLegacyDefault() {
        String s = settings.getOrDefault("alert_legacy_default", "&4[ALERT] &f");
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
        return serializer.deserialize(s.replace('&','§'));
    }


    private void load() {
        File file = new File(dataDirectory.toFile(), "config.txt");

        var logger = Alert.getLogger();

        logger.error(file.toString());
        if (file.exists()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }
            if (scanner == null) {
                logger.info("Not found config file");
                return;
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split("=");
                if (split.length == 2 && split[0].trim().equals("prefix")) {
                    settings.put(split[0].trim(), split[1].trim());
                }
            }
            scanner.close();
        } else {
            create(file);
        }
    }

    private void create(File file) {

        var logger = Alert.getLogger();

        try {
            if (file.createNewFile()) {
                logger.info("File created: {}", file.getAbsolutePath());
            } else {
                logger.error("File already exists.{}", file.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("prefix" + "= [ALERT] \n");
            fileWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
