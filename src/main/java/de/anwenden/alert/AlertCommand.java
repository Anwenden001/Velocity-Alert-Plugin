package de.anwenden.alert;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.aopalliance.intercept.Invocation;

import java.util.Arrays;
import java.util.List;

import static de.anwenden.alert.Alert.prefix;
import static de.anwenden.alert.Alert.server;


public class AlertCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String argsAsString = String.join(" ", args); // Connect an argument with a blank space

        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
        TextComponent textComponent = (TextComponent) serializer.deserialize("§c"+ prefix + "§f" + argsAsString.replace('&','§'));

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
