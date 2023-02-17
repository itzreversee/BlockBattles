package me.reversee.blockbattle;

import me.reversee.blockbattle.commands.ArenaCommand;
import me.reversee.blockbattle.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Arrays;

public final class Blockbattle extends JavaPlugin {
    // FileConfiguration config = this.getConfig(); // needs to be implemented, https://github.com/itzreversee/EssentialsY/blob/main/src/main/java/me/reversee/essentialsy/PluginTools.java
    @Override
    public void onEnable() {
        getLogger().info("Enabled BlockBattle!");
        registerCommands();

        // register listeners
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BlockListener(), this);

        // this.saveDefaultConfig();
    }

    void registerCommands() {
        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("arena").setTabCompleter(new ArenaCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled BlockBattle!");
    }
}
