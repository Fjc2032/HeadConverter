package dev.Fjc.headConverter.event;

import dev.Fjc.headConverter.data.HeadFinder;
import dev.Fjc.headConverter.gui.InventoryDumper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadConverter implements Listener {

    @EventHandler
    public void afterHeadRemove(InventoryCloseEvent event) {
        String parser = PlainTextComponentSerializer.plainText().serialize(InventoryDumper.name);
        if (!event.getView().title().equals(Component.text(parser))) return;
        HumanEntity entity = event.getPlayer();
        if (!(entity instanceof Player player)) return;

        Inventory inventory = event.getInventory();

        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            if (!HeadFinder.getAllValidHeads(inventory).contains(item)) continue;

            String name = PlainTextComponentSerializer.plainText().serialize(item.displayName());
            int level = getLevel(name);
            if (level >= 1) dispatchCommand(player, level);
        }
    }

    private int getLevel(String text) {
        Matcher matcher = Pattern.compile("Lvl (\\d{1,2})").matcher(text);
        if (matcher.find()) return Integer.parseInt(matcher.group(1));

        return 0;
    }

    //Syntax is silver give <player> <amount>
    //Amount accepts any double
    private void dispatchCommand(Player player, int amount) {
        double total = amount * 0.975;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "silver give " + player.getName() + " " + total);
    }
}
