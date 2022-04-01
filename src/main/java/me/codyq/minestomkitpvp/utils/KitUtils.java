package me.codyq.minestomkitpvp.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class KitUtils {

    public static void applyKit(Player player) {
        final PlayerInventory inventory = player.getInventory();

        inventory.setHelmet(ItemStack.of(Material.IRON_HELMET));
        inventory.setChestplate(ItemStack.of(Material.IRON_CHESTPLATE));
        inventory.setLeggings(ItemStack.of(Material.IRON_LEGGINGS));
        inventory.setBoots(ItemStack.of(Material.IRON_BOOTS));

        inventory.setItemStack(0, ItemStack.of(Material.IRON_SWORD));
        inventory.setItemStack(1, ItemStack.of(Material.BOW));
        inventory.setItemStack(2, ItemStack.of(Material.ARROW).withAmount(32));
        inventory.setItemStack(3, ItemStack.of(Material.GOLDEN_APPLE).withAmount(3));
        inventory.setItemStack(4, ItemStack.of(Material.COOKED_BEEF).withAmount(16));
    }

}
