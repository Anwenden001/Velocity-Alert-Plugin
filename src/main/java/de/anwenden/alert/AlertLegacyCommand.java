package de.anwenden.alert;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import static de.anwenden.alert.Alert.server;

public class AlertLegacyCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String argsAsString = String.join(" ", args); // Connect an argument with a blank space


        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
        TextComponent textComponent =  Alert.settings.getAlertLegacyDefault().append(serializer.deserialize(argsAsString.replace('&','§')));

        for (Player player : server.getAllPlayers()) {
            player.sendMessage(textComponent);
        }
        server.getConsoleCommandSource().sendMessage(textComponent);

    }


    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("alert.command.permission");
    }
}
