package dev.Fjc.headConverter.data;

import dev.Fjc.headConverter.gui.InventoryDumper;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an abstract class that manages heads.
 */
public abstract class HeadFinder {

    private static @NotNull List<ItemStack> leftovers = new ArrayList<>();

    /**
     * Returns a list of heads from a given inventory, as ItemStacks
     * @param inventory The given inventory
     * @return A list of ItemStack heads
     */
    public static List<ItemStack> getAllValidHeads(Inventory inventory) {
        List<ItemStack> filter = new ArrayList<>();

        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;

            if (item.getType() != Material.PLAYER_HEAD) {
                leftovers.add(item);
                continue;
            }

            var meta = item.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) {
                leftovers.add(item);
                continue;
            }

            String name = PlainTextComponentSerializer.plainText().serialize(meta.displayName())
                    .toLowerCase();
            if (!name.contains("pet")) {
                leftovers.add(item);
                continue;
            }
            filter.add(item);
        }
        return filter;
    }

    /**
     * Returns any items that did not match the predicate conditions of the stream
     * from {@link HeadFinder#getAllValidHeads(Inventory)}
     * @return A list of leftover items
     */
    public static List<ItemStack> getLeftovers() {
        return leftovers.stream()
                .filter(Objects::nonNull)
                .filter(obj -> !InventoryDumper.unmodifiablies.contains(obj))
                .toList();
    }

    public static void clearLeftovers() {
        leftovers.clear();
    }
}
