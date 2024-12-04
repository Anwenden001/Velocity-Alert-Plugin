package de.anwenden.alert;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Data;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

import static net.kyori.adventure.text.format.TextColor.fromHexString;

@Data
public class Settings {

    private final Path dataDirectory;

    private HashMap<String, String> hashMap;

    private final Logger logger;
    private final ProxyServer proxy;
    private boolean isDefaultSet = false;

    public Settings(Path dataDirectory, Logger logger, ProxyServer server) {
        this.dataDirectory = dataDirectory;
        this.hashMap = new HashMap<>();
        this.logger = logger;
        this.proxy = server;

        load();
    }

    public String getAlertDefault() {
        return hashMap.getOrDefault("alert_default", "Legacy");
    }

    public String getAlertRawDefault() {
        return hashMap.getOrDefault("alertraw_default", "Json");
    }

    public TextComponent getAlertJsonDefault() {
        String s = hashMap.getOrDefault("alert_json", "{\"color\":\"red\",\"text\": \"[ALERT] \"}"
        );
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return (TextComponent) serializer.deserialize(s);
    }

    public TextComponent getAlertXmlDefault() {
        String s = hashMap.getOrDefault("alert_xml", "<red>[ALERT]</red> ");
        MiniMessage serializer = MiniMessage.miniMessage();
        return (TextComponent) serializer.deserialize(s);
    }

    public TextComponent getAlertLegacyDefault() {
        String s = hashMap.getOrDefault("alert_legacy", "&c[ALERT] &f");
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
        return serializer.deserialize(s.replace('&', '§'));
    }

    public TextColor getDefaultColor(){
        String s = hashMap.getOrDefault("default_message_color", "ffffff");
        return fromHexString("#"+ s);
    }

    public void load() {
        hashMap = new HashMap<>();
        File file = dataDirectory.toFile();

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
                if (checkIsAValidElement(line)) {
                    String[] split = line.split("=");
                    hashMap.put(split[0].trim(), split[1].trim().substring(1, split[1].trim().length() - 1));
                }
            }
            scanner.close();
        } else {
            create(file, dataDirectory.getParent());
        }
    }

    private void create(File file, Path parent) {

        logger.info("Creating settings...");
        try {
            //Result of 'File.mkdirs()' is ignored
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
                            # This is the configuration file for the Alert Plugin for Velocity.
                            # This configuration file is generated in version 1.7.
                                                        
                                                        
                            # Here you can change the code format of the /alert and /alertraw commands.
                            # You can choose between "legacy", "json" and "xml".
                            # If you change the default, for example from "legacy" to "xml", you can still use the legacy formatting by using the /alertlegacy command.
                                                        
                            # "legacy" is the default option set in the config file for /alert. Color codes can be used with '&'.
                            # To use hex colors in legacy, format the color code as follows: &x&1&2&3&4&5&6 for the hex color #123456.
                            # "json" is the default config file option for /alertraw. This uses Minecraft JSON formatting.
                            # "xml" is a code format that allows you to enter color and format codes as text in these <> brackets. (for example <yellow> or <bold>)
                            # To use HEX colors in xml, format the color code as follows <#00AAAA> for the HEX color #00AAAA.
                                                        
                            alert_default = "legacy"
                            alertraw_default = "json"
                                                       
                                                        
                            # In this section you can customize the alert message prefixes and colors.                                                        
                            alert_legacy = "&c[ALERT] &f"
                            alert_json = "{"color":"red","text": "[ALERT] "}"
                            alert_xml = "<red>[ALERT]</red> "
                            
                            
                            # Here you can change the default color of the message to your liking. You must use HEX codes to change the colors. The default is white (#ffffff).
                            default_message_color = "ffffff"
                                                        
                            # If you have changed any configuration, you can use the /alertreload command to load it.
                            # If you have any questions or have found a problem, please report it to https://github.com/Anwenden001/Velocity-Alert-Plugin/issues
                            """;

            fileWriter.write(s);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    public boolean checkIsAValidElement(String line) {
        String[] split = line.split("=");
        if (split.length == 2) {
            if (hashMap.containsKey(split[0])) {
                this.getLogger().error("Error loading settings Double use of a value");
            } else {
                if (split[1].trim().isEmpty() || split[0].trim().isEmpty()) {
                    return false;
                }
                if (split[0].trim().charAt(0) == '#') {
                    return false;
                }
                return split[1].trim().charAt(0) == '"' && split[1].trim().charAt(split[1].trim().length() - 1) == '"';
            }
        }
        return false;
    }

}
