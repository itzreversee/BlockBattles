package me.reversee.blockbattle.commands;

import me.reversee.blockbattle.Arena;
import me.reversee.blockbattle.Arenas;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.floor;
import static me.reversee.blockbattle.Arenas.*;

public class ArenaCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can not execute this command as console!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("blockbattle.arena")) {
            sender.sendMessage("You do not have permission to use this command!");
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        if (args.length < 2 && !Objects.equals(args[0].toLowerCase(), "list")) {
            sender.sendMessage("Please complete the command!");
            return true;
        }

        if (Objects.equals(args[0].toLowerCase(), "create")) {
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
            boolean x = Arenas.removeArena(String.valueOf(args[1]));
            if (x) { sender.sendMessage("Deleted arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to delete arena \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "start")) {
            boolean x = Arenas.startArena(String.valueOf(args[1]));
            if (x) { sender.sendMessage("Started arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to start arena \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "stop")) {
            boolean x = Arenas.stopArena(String.valueOf(args[1]));
            if (x) { sender.sendMessage("Stopped arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to stop arena \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "add_player")) {
            boolean x = Arenas.addPlayer(
                    Objects.requireNonNull(Arenas.getArena(args[1])),
                    Bukkit.getPlayer(args[2])
            );
            if (x) { sender.sendMessage("Added player \"" + String.valueOf(args[2]) + "\""); }
            else { sender.sendMessage("Failed to add player \"" + String.valueOf(args[2]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "remove_player")) {
            boolean x = Arenas.removePlayer(
                    Objects.requireNonNull(Arenas.getArena(args[1])),
                    Bukkit.getPlayer(args[2])
            );
            if (x) { sender.sendMessage("Removed player \"" + String.valueOf(args[2]) + "\""); }
            else { sender.sendMessage("Failed to remove player \"" + String.valueOf(args[2]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "clean")) {
            boolean x = Objects.requireNonNull(Arenas.getArena(args[1])).cleanupArena();
            if (x) { sender.sendMessage("Cleaned up arena \"" + String.valueOf(args[1]) + "\""); }
            else { sender.sendMessage("Failed to clean up \"" + String.valueOf(args[1]) + "\""); }
        }

        if (Objects.equals(args[0].toLowerCase(), "list")) {
            sender.sendMessage("Created arenas: " + String.valueOf(Arenas.getArenasAsStrings()));
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
                "clean",
                "start",
                "stop",
                "list"
        );

        if (args.length == 1) { // /arena <here>
            return sub_commands;
        }

        if (args.length == 2 // /arena <delete/start/stop> <here>
                && !Objects.equals(args[0].toLowerCase(), "create")
                && !Objects.equals(args[0].toLowerCase(), "list")
        ) {
            return Arenas.getArenasAsStrings();
        }

        // /arena <add_player/remove_player> <arena> <here>
        if (args.length == 3 && ( Objects.equals(args[0].toLowerCase(), "add_player")  || Objects.equals(args[0].toLowerCase(), "remove_player") )
        ) {
            List<String> players = new ArrayList<String>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }

        return new ArrayList<String>(); // make sure that no suggestions appear
    }
}
