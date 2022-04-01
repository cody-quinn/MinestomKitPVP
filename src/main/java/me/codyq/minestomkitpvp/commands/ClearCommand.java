package me.codyq.minestomkitpvp.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear");
        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) player.getInventory().clear();
            else sender.sendMessage(Component.text("You cannot run this command.", NamedTextColor.RED));
        });
    }

}
