package me.codyq.minestomkitpvp;

import io.github.bloepiloepi.pvp.PvpExtension;
import me.codyq.minestomkitpvp.commands.GameModeCommand;
import me.codyq.minestomkitpvp.commands.TeleportCommand;
import me.codyq.minestomkitpvp.utils.KitUtils;
import me.codyq.minestomkitpvp.utils.PositionUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerRespawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.DimensionTypeManager;

public class Main {

    private static final DimensionType fullBrightDimension = DimensionType
            .builder(NamespaceID.from("minestom:fullbright"))
            .ambientLight(2.0f)
            .build();

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        DimensionTypeManager dimensionTypeManager = MinecraftServer.getDimensionTypeManager();
        dimensionTypeManager.addDimension(fullBrightDimension);

        CommandManager commandManager = MinecraftServer.getCommandManager();
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(fullBrightDimension);
        instanceContainer.setChunkGenerator(new FlatGenerator());

        globalEventHandler.addListener(PlayerLoginEvent.class, (event) -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(PositionUtils.getRandomPos(instanceContainer));
            KitUtils.applyKit(player);
        });

        globalEventHandler.addListener(PlayerRespawnEvent.class, (event) -> {
            final Player player = event.getPlayer();
            player.setRespawnPoint(PositionUtils.getRandomPos(instanceContainer));
            KitUtils.applyKit(player);
        });

        // Registering commands
        commandManager.register(new TeleportCommand());
        commandManager.register(new GameModeCommand());

        // Adding MinestomPVP
        PvpExtension.init();
        globalEventHandler.addChild(PvpExtension.events());

        OptifineSupport.enable();

        // Starting the server
        final String address = System.getenv("ADDRESS");
        final int port = Integer.parseInt(System.getenv("PORT"));
        final boolean enableMojangAuth = Boolean.parseBoolean(System.getenv("MOJANG_AUTH"));

        if (enableMojangAuth) {
            MojangAuth.init();
        }

        minecraftServer.start(address, port);
    }

}
