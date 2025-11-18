package dev.Fjc.headConverter.event;

import dev.Fjc.headConverter.data.HeadFinder;
import dev.Fjc.headConverter.data.TextBuilder;
import dev.Fjc.headConverter.gui.InventoryDumper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadExchanger implements Listener {

    @EventHandler
    public void stopModifications(InventoryClickEvent event) {
        String parser = PlainTextComponentSerializer.plainText().serialize(InventoryDumper.name);
        if (!event.getView().title().equals(Component.text(parser))) return;

        ItemStack target = event.getCurrentItem();

        for (ItemStack item : InventoryDumper.unmodifiablies) {
            if (target == null) continue;
            if (target.isSimilar(item)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void afterHeadRemove(InventoryCloseEvent event) {
        String parser = PlainTextComponentSerializer.plainText().serialize(InventoryDumper.name);
        if (!event.getView().title().equals(Component.text(parser))) return;
        HumanEntity entity = event.getPlayer();
        if (!(entity instanceof Player player)) return;

        HeadFinder.clearLeftovers();
        Inventory inventory = event.getInventory();

        List<ItemStack> validItems = HeadFinder.getAllValidHeads(inventory);
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;

            String name = PlainTextComponentSerializer.plainText().serialize(item.displayName());
            if (!validItems.contains(item)) {
                player.sendMessage(TextBuilder.badItem);
                continue;
            }

            Integer level = getLevel(name);
            if (level == null) {
                var lore = item.lore();
                if (lore == null) {
                    player.sendMessage(TextBuilder.badItem);
                    continue;
                }
                level = getLevelFromLore(lore);
                if (level == null) {
                    player.sendMessage(TextBuilder.badItem);
                    continue;
                }
            }
            if (level >= 1) {
                dispatchCommand(player, level);
            }
            if (level == 0) player.sendMessage(TextBuilder.badItem);
        }
        returnItems(player);

    }

    private @Nullable Integer getLevel(String text) {
        text = ChatColor.stripColor(text);

        Matcher matcher = Pattern.compile("(?i)\\blvl\\s*(\\d+)").matcher(text);
        if (matcher.find()) {
            Bukkit.getLogger().info("[HeadConverter] [DEBUG] Found a match!");
            return Integer.parseInt(matcher.group(1));
        }

        return null;
    }

    private @Nullable Integer getLevelFromLore(List<Component> lore) {
        for (Component component : lore) {
            String initial = PlainTextComponentSerializer.plainText().serialize(component);
            initial = ChatColor.stripColor(initial);

            Matcher matcher = Pattern.compile("(?i)\\blvl\\s*(\\d+)").matcher(initial);
            if (matcher.find()) return Integer.parseInt(matcher.group(1));
        }

        return null;
    }

    //Syntax is silver give <player> <amount>
    //Amount accepts any double
    private void dispatchCommand(Player player, int amount) {
        int total = Math.max((int) (amount * 0.075), 1);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "silver give " + player.getName() + " " + total);
        player.sendMessage("You received " + total + " silver for trading in a level " + amount + " pet.");
    }

    private void returnItems(Player player) {
        for (ItemStack item : HeadFinder.getLeftovers()) {
            HashMap<Integer, ItemStack> extras = player.getInventory().addItem(item);
            if (!extras.isEmpty()) {
                for (ItemStack dropped : extras.values()) {
                    World world = player.getWorld();
                    String serialized = PlainTextComponentSerializer.plainText().serialize(dropped.displayName());
                    world.dropItemNaturally(
                            player.getLocation(),
                            dropped,
                            onDrop -> player.sendMessage("Your item " + serialized + " did not fit in your inventory, and was dropped on the ground.")
                    );
                }
            }
        }
        HeadFinder.clearLeftovers();
    }
}
