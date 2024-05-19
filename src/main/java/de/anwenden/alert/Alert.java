package de.anwenden.alert;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;


@Plugin(id = "alert", name = "Alert", version = "1.0.4")
public class Alert {

    public static ProxyServer server;
    private final Logger logger;

    @Inject
    public Alert(ProxyServer server, Logger logger) {
        Alert.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("alert").build(), new AlertCommand());
    }
}

