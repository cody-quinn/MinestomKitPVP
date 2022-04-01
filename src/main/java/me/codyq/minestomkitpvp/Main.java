package me.codyq.minestomkitpvp;

import io.github.bloepiloepi.pvp.PvpExtension;
import me.codyq.minestomkitpvp.commands.GameModeCommand;
import me.codyq.minestomkitpvp.commands.TeleportCommand;
import me.codyq.minestomkitpvp.utils.KitUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerRespawnEvent;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.DimensionTypeManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

        SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(fullBrightDimension);

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

        // Starting the benchmark task to run every 40 ticks
        BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();
        benchmarkManager.enable(Duration.ofMillis(Long.MAX_VALUE));

        AtomicReference<TickMonitor> lastTick = new AtomicReference<>();
        globalEventHandler.addListener(ServerTickMonitorEvent.class, event -> lastTick.set(event.getTickMonitor()));

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
            if (players.isEmpty())
                return;

            long ramUsage = benchmarkManager.getUsedMemory();
            ramUsage /= 1e6; // bytes to MB

            TickMonitor tickMonitor = lastTick.get();

            final Component header = Component.text("Minestom demo")
                    .append(Component.newline()).append(Component.newline())
                    .append(Component.text("RAM USAGE: " + ramUsage + " MB").append(Component.newline())
                            .append(Component.text("TICK TIME: " + MathUtils.round(tickMonitor.getTickTime(), 2) + "ms")));

            final Component footer = Component.text("Project: minestom.net").append(Component.newline()).append(Component.text("Source: github.com/Minestom/Minestom"))
                    .append(Component.newline()).append(Component.newline())
                    .append(benchmarkManager.getCpuMonitoringMessage());
            Audiences.players().sendPlayerListHeaderAndFooter(header, footer);
        }).repeat(40, TimeUnit.SERVER_TICK).schedule();

        // Pogchamps
        globalEventHandler.addListener(PlayerLoginEvent.class, (event) -> {
            final Player player = event.getPlayer();
            player.setGameMode(GameMode.ADVENTURE);
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
        final String host = System.getProperty("host", "0.0.0.0");
        final int port = Integer.getInteger("port", 25565);
        if (Boolean.getBoolean("auth")) MojangAuth.init();

        minecraftServer.start(host, port);
    }
}
