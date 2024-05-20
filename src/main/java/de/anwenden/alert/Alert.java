package de.anwenden.alert;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


@Plugin(id = "alert", name = "Alert", version = "1.5-snapshot", authors = "Anwenden & Blockyward")
public class Alert {

    public static ProxyServer server;
    public static String prefix;
    private final Logger logger;

    @Inject
    public Alert(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {


        Alert.server = server;
        this.logger = logger;

        File file = new File(dataDirectory.toFile(), "config.txt");

        logger.error(file.toString());
        if(file.exists()){
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }
            if(scanner == null){
                logger.info("Not found config file");
                return;
            }
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] split = line.split("=");
                if(split.length == 2 && split[0].trim().equals("prefix")){
                    prefix = split[1].trim() + " ";
                    break;
                }
            }
            scanner.close();
        }else {
            prefix = "[ALERT] ";

//            file.mkdir();
            try {
                if(file.createNewFile()) {
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

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("alert").build(), new AlertCommand());
        commandManager.register(commandManager.metaBuilder("alertLegacy").build(), new AlertLegacyCommand());
        commandManager.register(commandManager.metaBuilder("alertXml").build(), new AlertXmlCommand());
        commandManager.register(commandManager.metaBuilder("alertJson").build(), new AlertJsonCommand());
        commandManager.register(commandManager.metaBuilder("alertraw").build(), new AlertJsonCommand());


    }
}

