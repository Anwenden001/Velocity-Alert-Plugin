package de.anwenden.alert;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Data;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Data
@Plugin(id = "alert", name = "Alert", version = "1.7", authors = "Anwenden & Blockyward")
public class Alert {

    private final Settings settings;

    @Inject
    public Alert(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {

        this.settings = new Settings((new File(dataDirectory.toFile(), "config.txt").toPath()), logger, server);

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = settings.getProxy().getCommandManager();


        ReloadSettingsCommand reloadSettingsCommand = new ReloadSettingsCommand(settings);
        reloadSettingsCommand.setDefault("alert");
        reloadSettingsCommand.setDefault("alertraw");
        commandManager.register(commandManager.metaBuilder("alertLegacy").build(), new AlertLegacyCommand(settings));
        commandManager.register(commandManager.metaBuilder("alertXml").build(), new AlertXmlCommand(settings));
        commandManager.register(commandManager.metaBuilder("alertJson").build(), new AlertJsonCommand(settings));
        commandManager.register(commandManager.metaBuilder("alertReload").build(), reloadSettingsCommand);
    }

}

