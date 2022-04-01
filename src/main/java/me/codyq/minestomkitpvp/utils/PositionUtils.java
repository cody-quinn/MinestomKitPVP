package me.codyq.minestomkitpvp.utils;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;

import java.util.Random;

public class PositionUtils {

    public static Pos getRandomPos(InstanceContainer instanceContainer) {
        final Random random = new Random();

        final int x = 260 + random.nextInt(-150, 150);
        final int z = 60 + random.nextInt(-150, 150);
        final int y = 60;
//        int y = 120;
//        for (; y > 0; y--) {
//            if (!instanceContainer.getBlock(x, y, z).equals(Block.AIR)) {
//                break;
//            }
//        }

        return new Pos(x, y, z);
    }

}
