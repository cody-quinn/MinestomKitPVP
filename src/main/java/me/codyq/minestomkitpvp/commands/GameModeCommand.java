package me.codyq.minestomkitpvp.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameModeCommand extends Command {

    public GameModeCommand() {
        super("gamemode");

        setCondition(Conditions::playerOnly);
//        setCondition((sender, ignored) -> sender.hasPermission("minestom.gamemode"));

        var gamemodeArg = ArgumentType.Enum("gamemode", GameMode.class);

        addSyntax(this::onGameMode, gamemodeArg);
    }

    private void onGameMode(CommandSender commandSender, CommandContext commandContext) {
        final Player player = (Player) commandSender;
        player.setGameMode(commandContext.get("gamemode"));
    }
}
