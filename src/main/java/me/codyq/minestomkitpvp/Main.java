package me.codyq.minestomkitpvp;

import io.github.bloepiloepi.pvp.PvpExtension;
import me.codyq.minestomkitpvp.commands.GameModeCommand;
import me.codyq.minestomkitpvp.commands.TeleportCommand;
import me.codyq.minestomkitpvp.utils.KitUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerRespawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.DimensionTypeManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final List<Pos> spawnLocations = new ArrayList<>();

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

        try {
            Scanner scanner = new Scanner(new File("spawns.csv"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] elements = line.split(",");
                if (elements.length >= 3) {
                    float x = Float.parseFloat(elements[0]);
                    float y = Float.parseFloat(elements[1]);
                    float z = Float.parseFloat(elements[2]);
                    spawnLocations.add(new Pos(x, y, z));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed loading spawns");
            e.printStackTrace();
            return;
        }

        final Random random = new Random();

        globalEventHandler.addListener(PlayerLoginEvent.class, (event) -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(spawnLocations.get(random.nextInt(spawnLocations.size())));
            KitUtils.applyKit(player);
        });

        globalEventHandler.addListener(PlayerRespawnEvent.class, (event) -> {
            final Player player = event.getPlayer();
            player.setRespawnPoint(spawnLocations.get(random.nextInt(spawnLocations.size())));
            KitUtils.applyKit(player);
        });

        globalEventHandler.addListener(PlayerBlockBreakEvent.class, (event) -> event.setCancelled(true));
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, (event) -> event.setCancelled(true));

        // Registering commands
        commandManager.register(new TeleportCommand());
        commandManager.register(new GameModeCommand());

        ChatFilter.init();

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
