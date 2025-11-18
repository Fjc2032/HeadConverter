package dev.Fjc.headConverter.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryDumper {

    public static Component name = Component.text("Pet Exchanger");

    public static List<ItemStack> unmodifiablies = new ArrayList<>();

    public static Inventory build(Player player, int size) {
        Inventory inventory = Bukkit.createInventory(player, size, name);
        addItems(inventory);

        return inventory;
    }

    public static void addItems(Inventory inventory) {

        ItemStack info = ItemStack.of(Material.PAPER, 1);
        ItemMeta infometa = info.getItemMeta();

        infometa.displayName(Component.text("Dump your old pets in here to receive currency in exchange."));
        infometa.lore(List.of(
                format("Higher level pets will get you more currency.", 40, 200, 40),
                format("Be careful - once you throw a pet away you cannot get it back!!!", 224, 40, 10),
                format("You've been warned", 200, 40, 200)
        ));

        ItemStack close = ItemStack.of(Material.RED_WOOL);
        ItemMeta closemeta = close.getItemMeta();

        closemeta.displayName(Component.text("Exit").color(TextColor.color(224, 27, 2)));
        closemeta.lore(List.of(Component.text("Click here to exit the GUI.")));

        info.setItemMeta(infometa);
        close.setItemMeta(closemeta);

        inventory.setItem(0, info);
        inventory.setItem(1, close);
        unmodifiablies.add(info);
        unmodifiablies.add(close);
    }

    public static Component format(String text, int red, int green, int blue) {
        return Component.text(text).colorIfAbsent(TextColor.color(red, green, blue));
    }

    public static void disable() {
        name = null;
        unmodifiablies = null;
    }
}
