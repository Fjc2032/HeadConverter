package dev.Fjc.headConverter;

import dev.Fjc.headConverter.event.GUICommand;
import dev.Fjc.headConverter.event.HeadExchanger;
import dev.Fjc.headConverter.gui.InventoryDumper;
import org.bukkit.plugin.java.JavaPlugin;

public final class HeadConverter extends JavaPlugin {

    @Override
    public void onEnable() {
        initialize();
    }

    @Override
    public void onDisable() {
        InventoryDumper.disable();
        getLogger().info("Plugin is shutting down.");
    }

    private void initialize() {
        getServer().getPluginManager().registerEvents(new HeadExchanger(), this);
        getServer().getPluginCommand("petexchanger").setExecutor(new GUICommand());

        getLogger().info("Everything has completed.");
    }

}
