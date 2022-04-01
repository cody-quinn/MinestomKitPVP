package me.codyq.minestomkitpvp.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

public class ProjectCommand extends Command {

    public ProjectCommand() {
        super("project");
        setDefaultExecutor(Project.MINESTOM.executor);

        ArgumentEnum<Project> projectArgument = ArgumentType.Enum("project", Project.class);
        addSyntax((sender, context) -> context.get(projectArgument).executor.apply(sender, context), projectArgument);
    }

    private enum Project {
        MINESTOM((sender, context) -> {
            sender.sendMessage(Component.join(
                    JoinConfiguration.separator(Component.newline()),
                    Component.text("Minestom is a complete rewrite of Minecraft server software, open-source and without any code from Mojang.", NamedTextColor.GREEN),
                    Component.text("Website: " , NamedTextColor.GREEN).append(Component.text("https://minestom.net", NamedTextColor.DARK_GREEN)
                            .hoverEvent(HoverEvent.showText(Component.text("Go to https://minestom.net")))
                            .clickEvent(ClickEvent.openUrl("https://minestom.net"))
                    ),
                    Component.text("GitHub: " , NamedTextColor.GREEN).append(Component.text("https://github.com/Minestom/Minestom", NamedTextColor.DARK_GREEN)
                            .hoverEvent(HoverEvent.showText(Component.text("Go to https://github.com/Minestom/Minestom")))
                            .clickEvent(ClickEvent.openUrl("https://github.com/Minestom/Minestom"))
                    )
            ));
        }),
        KITPVP((sender, context) -> {
            sender.sendMessage(Component.join(
                    JoinConfiguration.separator(Component.newline()),
                    Component.text("MinestomKitPVP is a testing server for PVP and general Minestom usage.", NamedTextColor.GREEN),
                    Component.text("GitHub: " , NamedTextColor.GREEN).append(Component.text("https://github.com/CatDevz/MinestomKitPVP", NamedTextColor.DARK_GREEN)
                            .hoverEvent(HoverEvent.showText(Component.text("Go to https://github.com/CatDevz/MinestomKitPVP")))
                            .clickEvent(ClickEvent.openUrl("https://github.com/CatDevz/MinestomKitPVP"))
                    )
            ));
        });

        private final CommandExecutor executor;

        Project(@NotNull CommandExecutor executor) {
            this.executor = executor;
        }
    }

}
