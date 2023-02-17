package me.reversee.blockbattle.listeners;

import me.reversee.blockbattle.Arena;
import me.reversee.blockbattle.Arenas;
import me.reversee.blockbattle.Blockbattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.lang.Math.floor;

public class BlockListener implements Listener{

    private Blockbattle plugin;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        HumanEntity he = event.getWhoClicked();
        String playerName = he.getName();
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) { return; }
        Arena arena = Arenas.getArenaByPlayer(player);
        if (arena == null) { return; }
        if (!arena.isStarted()) { return; }
        switch (event.getSlotType()) {
            case RESULT:
            case FUEL:
                event.setCancelled(true);
                player.sendMessage("Secret Move: Get Coal from Furnace");
                Objects.requireNonNull(
                        Objects.requireNonNull(
                                event.getClickedInventory()
                        ).getLocation()
                ).getBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.COAL, 2));
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Arena arena = Arenas.getArenaByPlayer(player);
        if (arena == null) { return; }
        if (!arena.isStarted()) { return; }
        event.setCancelled(true);
        event.getPlayer().sendMessage("Don't break");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Arena arena = Arenas.getArenaByPlayer(player);
        if (arena != null && !arena.isStarted()) { return; }
        if (arena == null) {
            //event.setCancelled(true);
            //player.sendMessage("You are not in any arena!");
            return;
        }
        /*
        if (!(arena.getCurrentPlayer() == player)) {
            event.setCancelled(true);
            player.sendMessage("It isn't your turn!");
            return;
        } */

        Material block = event.getBlockPlaced().getType();
        Location location = event.getBlockPlaced().getLocation();

        Block lastBlockPlaced = arena.lastBlockPlaced;
        arena.lastBlockPlaced = event.getBlockPlaced();
        arena.blocksPlaced.add(event.getBlockPlaced());

        //event.getPlayer().sendMessage(
        //        "Placed: " + block.toString() +
        //                " | x: " + floor(location.getBlockX()) +
        //                " | y: " + floor(location.getBlockY()) +
        //                " | z: " + floor(location.getBlockZ())
        //);

        // check for combo
        if (lastBlockPlaced != null) {
            if ( (lastBlockPlaced.getType() == Material.SPONGE && block == Material.ICE)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Sponge + Ice = Rain + Packed Ice");
                player.getInventory().addItem(new ItemStack(Material.PACKED_ICE,1));
                player.getWorld().setStorm(true);
                player.getWorld().setWeatherDuration(400);
                event.setCancelled(false);
                return;
            }

            if ( (lastBlockPlaced.getType() == Material.ICE && block == Material.MAGMA_BLOCK)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Magma Block on Ice = Counter Rain");
                player.getWorld().setStorm(false);
                player.getWorld().setWeatherDuration(0);
                event.setCancelled(false);
                return;
            }

            if ( (lastBlockPlaced.getType() == Material.OAK_FENCE && block == Material.SOUL_LANTERN)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Oak Fence + Soul Lantern = 2x Netherite Block");
                player.getInventory().addItem(new ItemStack(Material.NETHERITE_BLOCK,2));
                event.setCancelled(false);
                return;
            }

            if ( (lastBlockPlaced.getType() == Material.OAK_FENCE && block == Material.GLOWSTONE)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Oak Fence + Glowstone = Day");
                player.getWorld().setTime(3000);
                event.setCancelled(false);
                return;
            }

            if ( (lastBlockPlaced.getType() == Material.OAK_FENCE && block == Material.SHROOMLIGHT)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Oak Fence + Shroomlight = Night");
                player.getWorld().setTime(16000);
                event.setCancelled(false);
                return;
            }

            if ( (lastBlockPlaced.getType() == Material.TWISTING_VINES && block == Material.SHROOMLIGHT)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Twisting Vines + Shroomlight = 4x Netherrack");
                player.getInventory().addItem(new ItemStack(Material.NETHERRACK, 4));
                event.setCancelled(false);
                return;
            }

            if ( (lastBlockPlaced.getType() == Material.STONE && block == Material.POINTED_DRIPSTONE)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Stone + Pointed Dripstone = 4x Gravel");
                player.getInventory().addItem(new ItemStack(Material.GRAVEL, 4));
                event.setCancelled(false);
                return;
            }

            if ( (lastBlockPlaced.getType() == Material.GRAVEL && block == Material.SOUL_LANTERN)
                    && (lastBlockPlaced.getLocation().getBlockY() == location.getBlockY() - 1)
            ) {
                player.sendMessage("Combo: Gravel + Soul Lantern = 1x Lantern");
                player.getInventory().addItem(new ItemStack(Material.LANTERN, 1));
                event.setCancelled(false);
                return;
            }
        }

        // cancel
        event.setCancelled(false);
    }
}
