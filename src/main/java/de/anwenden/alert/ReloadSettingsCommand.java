package de.anwenden.alert;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.concurrent.locks.ReentrantLock;

public class ReloadSettingsCommand implements SimpleCommand {

    private final Settings settings;
    ReentrantLock lock = new ReentrantLock();

    public ReloadSettingsCommand(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void execute(final Invocation invocation) {
        this.settings.load();
        setDefault("alert");
        setDefault("alertraw");

        CommandSource source = invocation.source();
        source.sendMessage(Component.text("Alert reloaded!", NamedTextColor.DARK_RED));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("alert.command.permission");
    }

    private void setDefault(String s) {
        lock.lock();
        if (settings.isDefaultSet()) {
            settings.getProxy().getCommandManager().unregister(s);
        }
        settings.setDefaultSet(false);
        try {
            String valueOfDefault;
            if (s.equals("alert")) {
                valueOfDefault = settings.getAlertDefault().toLowerCase();
            } else if (s.equals("alertraw")) {
                valueOfDefault = settings.getAlertRawDefault().toLowerCase();
            } else {
                settings.getLogger().error("WTF Unsupported command pls write an issue.");
                return;
            }
            switch (valueOfDefault) {
                case "legacy":
                    settings.getProxy().getCommandManager().register(settings.getProxy().getCommandManager().metaBuilder(s).build(), new AlertLegacyCommand(settings));
                    break;
                case "xml":
                    settings.getProxy().getCommandManager().register(settings.getProxy().getCommandManager().metaBuilder(s).build(), new AlertXmlCommand(settings));
                    break;
                default:
                    settings.getProxy().getCommandManager().register(settings.getProxy().getCommandManager().metaBuilder(s).build(), new AlertJsonCommand(settings));
                    break;
            }
            settings.setDefaultSet(true);
        } finally {
            lock.unlock();
        }
    }
}
