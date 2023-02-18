package me.reversee.blockbattle;

import me.reversee.blockbattle.commands.ArenaCommand;
import me.reversee.blockbattle.listeners.BlockListener;
import me.reversee.blockbattle.mechanics.Combos;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.Arrays;

public final class Blockbattle extends JavaPlugin {
    private File combosConfigFile;
    private FileConfiguration combosConfig;

    @Override
    public void onEnable() {
        getLogger().info("Enabled BlockBattle!");
        registerCommands();

        // register listeners
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BlockListener(this), this);

        saveConfigs();
        Combos.setPlugin(this);
        Combos.loadComboMap();
    }
    void saveConfigs() {
        this.saveDefaultConfig();

        combosConfigFile = new File(getDataFolder(), "combos.yml");
        if (!combosConfigFile.exists()) {
            combosConfigFile.getParentFile().mkdirs();
            saveResource("combos.yml", false);
        }
        combosConfig = new YamlConfiguration();
        try {
            combosConfig.load(combosConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadConfigs() {
        this.reloadConfig();
        this.saveConfig();
        try {
            combosConfig.load(combosConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Combos.loadComboMap();
    }

    public FileConfiguration getComboConfig() {
        return this.combosConfig;
    }

    void registerCommands() {
        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("arena").setTabCompleter(new ArenaCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled BlockBattle!");
    }
}
