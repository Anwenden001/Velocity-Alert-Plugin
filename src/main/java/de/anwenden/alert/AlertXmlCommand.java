package de.anwenden.alert;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static de.anwenden.alert.Alert.server;

public class AlertXmlCommand implements SimpleCommand {


    @Override
    public void execute(final Invocation invocation) {
        String[] args = invocation.arguments();
        String argsAsString = String.join(" ", args); // Connect an argument with a blank space

        MiniMessage serializer = MiniMessage.miniMessage();

        TextComponent textComponent = Alert.settings.getAlertXmlDefault().append( serializer.deserialize(argsAsString));

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
