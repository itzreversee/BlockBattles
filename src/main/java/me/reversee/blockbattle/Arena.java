package me.reversee.blockbattle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private Location _location;
    private String _name;
    private List<Player> _players = new ArrayList<Player>();
    private Player _currentTurnPlayer;

    public Block lastBlockPlaced;
    public List<Block> blocksPlaced = new ArrayList<Block>();

    private boolean _started = false;

    public void setLocation(Location location) { _location = location; }
    public void setName(String name) { _name = name; }
    public void addPlayer(Player player) {
        try {
            _players.add(player);
        } catch (Exception e) {
            player.sendMessage(e.getMessage());
        }
    }
    public void removePlayer(Player player) {
        _players.remove(player);
    }

    public boolean startArena() {
        if (_started) { _currentTurnPlayer = _players.get(0); return false; }
        _started = true; return true;
    }
    public boolean stopArena() {
        if (!_started) { _currentTurnPlayer = null; return false; }
        _started = false; return true;
    }

    public boolean isStarted() {
        return _started;
    }

    public boolean cleanupArena() {
        for (Block block : blocksPlaced) {
            block.setType(Material.AIR);
        }
        return true;
    }

    public void switchTurn() {
        if (_currentTurnPlayer == _players.get(0)) {
            _currentTurnPlayer = _players.get(1);
        } else {
            _currentTurnPlayer = _players.get(0);
        }
    }

    public Player getCurrentPlayer() {
        return _currentTurnPlayer;
    }

    public Location getLocation() { return _location; }
    public String getName() { return _name; }
    public List<Player> getPlayers() { return _players; }

}
