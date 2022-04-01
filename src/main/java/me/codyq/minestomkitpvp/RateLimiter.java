package me.codyq.minestomkitpvp;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RateLimiter {
    private static final boolean ENABLED = true;
    private static final long RATE_MS = 3_000;
    private static final Map<InetAddress, Long> LOGIN_LIMITER = new ConcurrentHashMap<>();

    public static void register(EventNode<Event> node) {
        node.addListener(AsyncPlayerPreLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            final SocketAddress socketAddress = player.getPlayerConnection().getRemoteAddress();
            if (socketAddress instanceof InetSocketAddress inetSocketAddress) {
                final InetAddress address = inetSocketAddress.getAddress();
                final long lastLogin = LOGIN_LIMITER.getOrDefault(address, 0L);
                final long currentTime = System.currentTimeMillis();
                if (ENABLED && currentTime - lastLogin < RATE_MS) {
                    player.kick("You are logging too fast!");
                } else {
                    LOGIN_LIMITER.put(address, currentTime);
                    // Retrieve skin async
                    new Thread(() -> {
                        final PlayerSkin skin = PlayerSkin.fromUsername(player.getUsername());
                        if (skin != null && player.isOnline()) player.scheduleNextTick(entity -> player.setSkin(skin));
                    }).start();
                }
            }
        });
    }
}
