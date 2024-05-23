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
import java.nio.file.Path;


@Plugin(id = "alert", name = "Alert", version = "1.5", authors = "Anwenden & Blockyward")
public class Alert {

    public static ProxyServer server;
    public static String prefix;
    private static Logger logger;
    public static Settings settings;

    @Inject
    public Alert(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {


        Alert.server = server;
        Alert.logger = logger;

        settings = new Settings((new File(dataDirectory.toFile(), "config.txt").toPath()));


    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();

        String s = "alert";
        switch (settings.getAlertDefault().toLowerCase()) {
            case "json":
                commandManager.register(commandManager.metaBuilder(s).build(), new AlertJsonCommand());
                break;
            case "xml":
                commandManager.register(commandManager.metaBuilder(s).build(), new AlertXmlCommand());
                break;
            default:
                commandManager.register(commandManager.metaBuilder(s).build(), new AlertLegacyCommand());
                break;
        }

        s = "alertraw";
        switch (settings.getAlertRawDefault().toLowerCase()) {
            case "legacy":
                commandManager.register(commandManager.metaBuilder(s).build(), new AlertLegacyCommand());
                break;
            case "xml":
                commandManager.register(commandManager.metaBuilder(s).build(), new AlertXmlCommand());
                break;
            default:
                commandManager.register(commandManager.metaBuilder(s).build(), new AlertJsonCommand());
                break;
        }

        commandManager.register(commandManager.metaBuilder("alertLegacy").build(), new AlertLegacyCommand());
        commandManager.register(commandManager.metaBuilder("alertXml").build(), new AlertXmlCommand());
        commandManager.register(commandManager.metaBuilder("alertJson").build(), new AlertJsonCommand());

        commandManager.register(commandManager.metaBuilder("alertReload").build(), new ReloadSettingsCommand());
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static void setServer(ProxyServer server) {
        Alert.server = server;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        Alert.prefix = prefix;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Alert.logger = logger;
    }
}

