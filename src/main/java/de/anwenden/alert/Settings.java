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


public class Settings {

    private final Path dataDirectory;

    private HashMap<String, String> hashMap;

    public Settings(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.hashMap = new HashMap<>();
        load();
    }

    public String getAlertDefault() {
        return hashMap.getOrDefault("alert_default", "Legacy");
    }

    public String getAlertRawDefault() {
        return hashMap.getOrDefault("alert_raw_default", "Json");
    }

    public TextComponent getAlertJsonDefault() {
        String s = hashMap.getOrDefault("alert_json_default", "{\"text\": \"[ALERT] \"}");
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return (TextComponent) serializer.deserialize(s);
    }

    public TextComponent getAlertXmlDefault() {
        String s = hashMap.getOrDefault("alert_xml_default", "<#f60002>[ALERT]</#f60002> ");
        MiniMessage serializer = MiniMessage.miniMessage();
        return (TextComponent) serializer.deserialize(s);
    }

    public TextComponent getAlertLegacyDefault() {
        String s = hashMap.getOrDefault("alert_legacy_default", "&4[ALERT] &f");
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
        return serializer.deserialize(s.replace('&', '§'));
    }


    public void load() {
        hashMap = new HashMap<>();
        File file = dataDirectory.toFile();
        var logger = Alert.getLogger();

        logger.info("Loading settings...");
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
                if (split.length == 2) {
                    if (hashMap.containsKey(split[0])) {
                        logger.info("Error loading settings Double use of a value");
                    } else {
                        if(split[1].trim().isEmpty() || split[0].trim().isEmpty()) {
                            continue;
                        }
                        if(split[1].trim().charAt(0) == '"' && split[1].trim().charAt(split[1].trim().length() -1) == '"') {
                            hashMap.put(split[0].trim(), split[1].trim().substring(1, split[1].trim().length() - 1));

                        }
                    }
                }
            }
            scanner.close();
        } else {
            create(file, dataDirectory.getParent());
        }
    }

    private void create(File file, Path parent) {

        var logger = Alert.getLogger();
        logger.info("Creating settings...");
        try {
            parent.toFile().mkdirs();

            if (file.createNewFile()) {
                logger.info("File created: {}", file.getAbsolutePath());
            } else {
                logger.error("File already exists.{}", file.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            String s =
                    """
                            alert = "legacy"
                            alertraw = "json"
                                                
                            alert_legacy_default = "&4[ALERT] &f"
                            alert_json_default = "{"text": "[ALERT] "}"
                            alert_xml_default = ""<#f60002>[ALERT]</#f60002> "
                            """;

            fileWriter.write(s);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
