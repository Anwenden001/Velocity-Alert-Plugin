package de.anwenden.alert;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ReloadSettingsCommand implements SimpleCommand {
    @Override
    public void execute(final Invocation invocation) {
        Alert.settings.load();
        CommandSource source = invocation.source();
        source.sendMessage(Component.text("Alert reloaded!", NamedTextColor.DARK_RED));

    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("alert.command.permission");
    }
}
