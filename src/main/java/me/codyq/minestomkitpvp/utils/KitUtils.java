package me.codyq.minestomkitpvp.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public final class KitUtils {
    private static final ItemStack HELMET_ITEM = ItemStack.of(Material.IRON_HELMET);
    private static final ItemStack CHESTPLATE_ITEM = ItemStack.of(Material.IRON_CHESTPLATE);
    private static final ItemStack LEGGINGS_ITEM = ItemStack.of(Material.IRON_LEGGINGS);
    private static final ItemStack BOOTS_ITEM = ItemStack.of(Material.IRON_BOOTS);

    private static final ItemStack SWORD_ITEM = ItemStack.of(Material.IRON_SWORD);
    private static final ItemStack BOW_ITEM = ItemStack.of(Material.BOW);
    private static final ItemStack ARROW_ITEM = ItemStack.builder(Material.ARROW).amount(32).build();
    private static final ItemStack GOLDEN_APPLE_ITEM = ItemStack.builder(Material.GOLDEN_APPLE).amount(3).build();
    private static final ItemStack COOKED_BEEF_ITEM = ItemStack.builder(Material.COOKED_BEEF).amount(16).build();

    private KitUtils() {
    }

    public static void applyKit(@NotNull Player player) {
        final PlayerInventory inventory = player.getInventory();

        inventory.setHelmet(HELMET_ITEM);
        inventory.setChestplate(CHESTPLATE_ITEM);
        inventory.setLeggings(LEGGINGS_ITEM);
        inventory.setBoots(BOOTS_ITEM);

        inventory.setItemStack(0, SWORD_ITEM);
        inventory.setItemStack(1, BOW_ITEM);
        inventory.setItemStack(2, ARROW_ITEM);
        inventory.setItemStack(3, GOLDEN_APPLE_ITEM);
        inventory.setItemStack(4, COOKED_BEEF_ITEM);
    }
}
