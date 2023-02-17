package me.reversee.blockbattle;

import org.bukkit.entity.Player;

import java.util.*;

public class Arenas {
    private static HashMap<String, Arena> ArenaList = new HashMap<String, Arena>();
    private static HashMap<Player, Arena> PlayersInArenas = new HashMap<Player, Arena>();

    public static boolean addArena(String name, Arena arena) {
        try {
            ArenaList.put(name, arena);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean removeArena(String name) {
        try {
            ArenaList.remove(name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Arena getArena(String name) {
        try {
            return ArenaList.get(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean addPlayer(Arena _arena, Player _player) {
        try {
            if (PlayersInArenas.containsKey(_player)) { return false; }
            _arena.addPlayer(_player);
            PlayersInArenas.put(_player, _arena);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean removePlayer(Arena _arena, Player _player) {
        try {
            if (!PlayersInArenas.containsKey(_player)) { return false; }
            _arena.removePlayer(_player);
            PlayersInArenas.remove(_player);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Arena getArenaByPlayer(Player player) {
        if (PlayersInArenas.containsKey(player)) {
            return PlayersInArenas.get(player);
        }
        return null;
    }

    public static boolean startArena(String name) {
        if (!Objects.requireNonNull(getArena(name)).startArena()) {
            return false;
        }
        return true;
    }
    public static boolean stopArena(String name) {
        if (!Objects.requireNonNull(getArena(name)).stopArena()) {
            return false;
        }
        return true;
    }

    public static List<Arena> getArenas() { return new ArrayList<Arena>(ArenaList.values()); }
    public static List<String> getArenasAsStrings() {
        List<String> arena_strings = new ArrayList<String>();
        for (Arena a : ArenaList.values()) {
            arena_strings.add(String.valueOf(a.getName()));
        }
        return arena_strings;
    }
}
