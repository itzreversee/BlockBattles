package me.reversee.blockbattle.commands;

import me.reversee.blockbattle.Arena;
import me.reversee.blockbattle.Arenas;
import me.reversee.blockbattle.Blockbattle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

import static java.lang.Math.floor;
import static me.reversee.blockbattle.Arenas.*;

public class ArenaCommand implements CommandExecutor, TabCompleter {

    Blockbattle plugin;

    public ArenaCommand(Blockbattle _plugin) {
        this.plugin = _plugin;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        // testing

        HashMap<String, String> combo_list = new HashMap<>();
        List<String> comboKeys = new ArrayList<>(plugin.getComboConfig().getKeys(true));
        HashMap<String, Object> comboVals = new HashMap<>(plugin.getComboConfig().getValues(true));
        HashMap<String, Object> rain_combo_items = new HashMap<>(plugin.getComboConfig().getConfigurationSection("rain_combo").getValues(true));
        HashMap<String, Object> rain_combo_items_result = new HashMap<>(plugin.getComboConfig().getConfigurationSection("rain_combo.result").getValues(true));

        //Player p = (Player) sender; // DELETE SOON
        /*for (Map.Entry<String, Object> set : rain_combo_items.entrySet()) {
            p.sendMessage(String.valueOf(
                    set.getKey() + "=" + set.getValue()
            ));
        }
        for (Map.Entry<String, Object> set : rain_combo_items_result.entrySet()) {
            p.sendMessage(String.valueOf(
                    set.getKey() + "=" + set.getValue()
            ));
        }*/

        // actual commands

        if (!sender.hasPermission("blockbattle.arena")) {
            sender.sendMessage("You do not have permission to use this command!");
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        if (args.length < 2 &&
               (   !Objects.equals(args[0].toLowerCase(), "list")
                && !Objects.equals(args[0].toLowerCase(), "reload")
                && !Objects.equals(args[0].toLowerCase(), "help")
                && !Objects.equals(args[0].toLowerCase(), "dev")
               )
        ) {
            sender.sendMessage("Please complete the command!");
            return true;
        }
        if (args.length < 3 &&
                ( Objects.equals(args[0].toLowerCase(), "add_player")
                 || Objects.equals(args[0].toLowerCase(), "remove_player"))
        ) {
            sender.sendMessage("Please complete the command!");
            return true;
        }

        if (Objects.equals(args[0].toLowerCase(), "create")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Can not execute this command as console!");
                return true;
            }
            Player player = (Player) sender;
            Location location = player.getLocation();
            String locationString = String.valueOf("X: " + floor(location.getX()) + " Y: " + floor(location.getY()) + " Z: " + floor(location.getZ()));
            sender.sendMessage("Creating arena \"" + args[1] + "\" at location: " + locationString);
            Arena arena = new Arena();
            arena.setName(args[1]);
            arena.setLocation(location);
            arena.addPlayer(player);
            Arenas.addArena(String.valueOf(args[1]), arena);
        }

        if (Objects.equals(args[0].toLowerCase(), "delete")) {
            Arena arena = Arenas.getArena(args[1]);
            if (arena == null) {
                sender.sendMessage("Could not find the arena!");
                return true;
            }
            boolean x = Arenas.removeArena(String.valueOf(args[1]));
            if (x) { sender.sendMessage("Deleted arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to delete arena \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "start")) {
            Arena arena = Arenas.getArena(args[1]);
            if (arena == null) {
                sender.sendMessage("Could not find the arena!");
                return true;
            }
            boolean x = Arenas.startArena(String.valueOf(args[1]));
            if (x) { sender.sendMessage("Started arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to start arena \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "stop")) {
            Arena arena = Arenas.getArena(args[1]);
            if (arena == null) {
                sender.sendMessage("Could not find the arena!");
                return true;
            }
            boolean x = Arenas.stopArena(String.valueOf(args[1]));
            if (x) { sender.sendMessage("Stopped arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to stop arena \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "add_player")) {
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage("Can't find the player");
                return true;
            }
            Arena arena = Arenas.getArena(args[1]);
            if (arena == null) {
                sender.sendMessage("Could not find the arena!");
                return true;
            }
            boolean x = Arenas.addPlayer(arena, player);
            if (x) { sender.sendMessage("Added player \"" + String.valueOf(args[2]) + "\""); }
            else { sender.sendMessage("Failed to add player \"" + String.valueOf(args[2]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "remove_player")) {
            Arena arena = Arenas.getArena(args[1]);
            if (arena == null) {
                sender.sendMessage("Could not find the arena!");
                return true;
            }
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage("Can't find the player");
                return true;
            }
            boolean x = Arenas.removePlayer(arena, player);
            if (x) { sender.sendMessage("Removed player \"" + String.valueOf(args[2]) + "\""); }
            else { sender.sendMessage("Failed to remove player \"" + String.valueOf(args[2]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "give_item_pool")) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Can't find the player");
                return true;
            }
            PlayerInventory inv = player.getInventory();

            List<Material> items = generateItemPool();

            for (Material mat : items) {
                inv.addItem(new ItemStack(mat));
            }
        }

        if (Objects.equals(args[0].toLowerCase(), "clean")) {
            Arena arena = Arenas.getArena(args[1]);
            if (arena == null) {
                sender.sendMessage("Could not find the arena!");
                return true;
            }
            boolean x = arena.cleanupArena();
            if (x) { sender.sendMessage("Cleaned up arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to clean up \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "reload")) {
            sender.sendMessage("Reloading Configuration");
            plugin.reloadConfigs();
        }
        if (Objects.equals(args[0].toLowerCase(), "list")) {
            sender.sendMessage("Created arenas: " + String.valueOf(Arenas.getArenasAsStrings()));
        }
        if (Objects.equals(args[0].toLowerCase(), "help")) {
            sendHelp(sender);
        }

        if (Objects.equals(args[0].toLowerCase(), "dev")) {
            sender.sendMessage("allowBreakingBlocks: " + Objects.requireNonNull(plugin.getConfig().getString("allowBreakingBlocks")));
            sender.sendMessage("rain_combo.down: " +Objects.requireNonNull(plugin.getComboConfig().getString("rain_combo.down")));
            sender.sendMessage("rain_combo.up: " +Objects.requireNonNull(plugin.getComboConfig().getString("rain_combo.up")));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> sub_commands = Arrays.asList(
                "create",
                "delete",
                "add_player",
                "remove_player",
                "give_item_pool",
                "clean",
                "start",
                "stop",
                "list",
                "help"
        );

        if (args.length == 1) { // /arena <here>
            return sub_commands;
        }
        if (args.length == 2 &&// /arena <delete/start/stop> <here>
                  (Objects.equals(args[0].toLowerCase(), "delete")
                || Objects.equals(args[0].toLowerCase(), "add_player")
                || Objects.equals(args[0].toLowerCase(), "remove_player")
                || Objects.equals(args[0].toLowerCase(), "clean")
                || Objects.equals(args[0].toLowerCase(), "start")
                || Objects.equals(args[0].toLowerCase(), "stop"))
        ) {
            return Arenas.getArenasAsStrings();
        }

        // /arena give_item_pool
        if (args.length == 2 && Objects.equals(args[0], "give_item_pool")) {
            return getOnlinePlayers();
        }

        // /arena <add_player/remove_player> <arena> <here>
        if (args.length == 3 &&
                (Objects.equals(args[0].toLowerCase(), "add_player")
              || Objects.equals(args[0].toLowerCase(), "remove_player"))
        ) {
            return getOnlinePlayers();
        }

        return new ArrayList<String>(); // make sure that no suggestions appear
    }

    List<String> getOnlinePlayers() {
        List<String> players = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            players.add(p.getName());
        }
        return players;
    }

    void sendHelp(org.bukkit.command.CommandSender sender) {
        sender.sendMessage(ChatColor.BLUE + "Block Battles plugin");
        sender.sendMessage(ChatColor.BOLD + "Available commands: ");
        sender.sendMessage(" /arena create <arena_name> - Creates a new arena");
        sender.sendMessage(" /arena delete <arena_name> - Deletes arena");
        sender.sendMessage(" /arena add_player <arena_name> <player_name> - Adds a player to the arena");
        sender.sendMessage(" /arena remove_player <arena_name> <player_name> - Removes player from the arena");
        sender.sendMessage(" /arena give_item_pool <player_name> - Gives random items from item pool defined in the config");
        sender.sendMessage(" /arena clean <arena_name> - Cleans the arena");
        sender.sendMessage(" /arena start <arena_name> - Starts the arena");
        sender.sendMessage(" /arena stop  <arena_name> - Stops the arena");
        sender.sendMessage(" /arena reload - Reloads configuration files");
        sender.sendMessage(" /arena list - Lists all created arenas");
        sender.sendMessage(" /arena help - Displays this help");
    }

    List<Material> generateItemPool() {
        List<Object> base_itemPool = new ArrayList<>(Objects.requireNonNull(plugin.getConfig().getList("base_itemPool")));
        List<Object> combo_itemPool = new ArrayList<>(Objects.requireNonNull(plugin.getConfig().getList("combo_itemPool")));
        List<Object> onetime_itemPool = new ArrayList<>(Objects.requireNonNull(plugin.getConfig().getList("onetime_itemPool")));

        int base_count    = plugin.getConfig().getInt("base_count");
        int combo_count   = plugin.getConfig().getInt("combo_count");
        int onetime_count = plugin.getConfig().getInt("onetime_count");

        boolean allowRepeating         = plugin.getConfig().getBoolean("allowRepeating");

        List<Material> materials = new ArrayList<Material>();

        Random random = new Random();

        int bI = 0;
        while (!(bI == base_count)) {
            plugin.getLogger().info(String.valueOf(bI));
            Material m = Material.getMaterial(String.valueOf(base_itemPool.get(random.nextInt(base_itemPool.size()))));
            if (!materials.contains(m) || allowRepeating) {
                materials.add(m);
                bI++;
            } else if (bI == base_itemPool.size()) {
                break;
            }
        }

        int bC = 0;
        while (!(bC == combo_count)) {
            Material m = Material.getMaterial(String.valueOf(combo_itemPool.get(random.nextInt(combo_itemPool.size()))));
            if (!materials.contains(m) || allowRepeating) {
                materials.add(m);
                bC++;
            } else if (bI == base_itemPool.size()) {
                break;
            }
        }

        int bO = 0;
        while (!(bO == onetime_count)) {
            Material m = Material.getMaterial(String.valueOf(onetime_itemPool.get(random.nextInt(onetime_itemPool.size()))));
            if (!materials.contains(m) || allowRepeating) {
                materials.add(m);
                bO++;
            } else if (bI == base_itemPool.size()) {
                break;
            }
        }

        return materials;
    }

}
