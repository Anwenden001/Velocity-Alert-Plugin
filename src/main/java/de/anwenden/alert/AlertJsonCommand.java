package de.anwenden.alert;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;


public class AlertJsonCommand implements SimpleCommand {

    private final Settings settings;

    public AlertJsonCommand(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void execute(final Invocation invocation) {
        String[] args = invocation.arguments();
        String argsAsString = String.join(" ", args); // Connect an argument with a blank space

        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        TextComponent textComponent = this.settings.getAlertJsonDefault().append(serializer.deserialize(argsAsString).colorIfAbsent(this.settings.getDefaultColor()));

        for (Player player : settings.getProxy().getAllPlayers()) {
            player.sendMessage(textComponent);
        }
        settings.getProxy().getConsoleCommandSource().sendMessage(textComponent);
    }


    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("alert.command.permission");
    }
}
