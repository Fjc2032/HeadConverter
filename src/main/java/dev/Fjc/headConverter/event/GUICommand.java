package dev.Fjc.headConverter.event;

import dev.Fjc.headConverter.data.TextBuilder;
import dev.Fjc.headConverter.gui.InventoryDumper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextBuilder.noPerms);
            return false;
        }

        if (args.length == 0) {
            player.openInventory(InventoryDumper.build(player, 18));
            player.sendMessage(TextBuilder.open);
            return true;
        }
        if (args.length == 1) {
            player.openInventory(InventoryDumper.build(player, Integer.parseInt(args[0])));
            return true;
        }

        return false;
    }
}
