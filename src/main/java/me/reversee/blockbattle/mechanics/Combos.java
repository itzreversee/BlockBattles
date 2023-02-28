package me.reversee.blockbattle.mechanics;

import me.reversee.blockbattle.Blockbattle;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Combos {

    private static Blockbattle plugin;
    public static void setPlugin(Blockbattle _plugin)  { plugin = _plugin;}

    public static List<Combo> comboList = new ArrayList<Combo>();

    public static void loadComboMap() {
        FileConfiguration comboConfig = plugin.getComboConfig();
        Set<String> combos = comboConfig.getKeys(false);
        for (Object combos_ : combos) {
            List<Material> blocks = new ArrayList<Material>();
            for (Object block_ : comboConfig.getList(combos_.toString() + ".blocks")) {
                blocks.add(Material.valueOf(block_.toString()));
            }
            boolean ignoreOrder = comboConfig.getBoolean(combos_ + ".ignore_order");
            String direction = comboConfig.getString(combos_ + ".direction");
            boolean selfDestruct = comboConfig.getBoolean(combos_ + ".self_destruct");
            boolean cancelEvent = comboConfig.getBoolean(combos_ + ".cancel_event");
            Particle particle = Particle.valueOf(comboConfig.getString(combos_ + ".particle"));
            HashMap<String, String> results = new HashMap<String, String>();
            for (String rs : comboConfig.getConfigurationSection(combos_.toString() + ".result").getKeys(false)) {
                if (rs.equalsIgnoreCase("give_item")) {
                    results.put(rs, comboConfig.getList(combos_.toString() + ".result." + rs).toString());
                }
                results.put(rs, comboConfig.getString(combos_.toString() + ".result." + rs ));
            }
            Combo combo = new Combo(
                    combos_.toString(),
                    blocks,
                    ignoreOrder,
                    direction,
                    selfDestruct,
                    cancelEvent,
                    results,
                    particle
            );
            comboList.add(combo);
        }
    }

    public static Combo getComboByBlocks(List<Block> blocks_placed) {
        if (blocks_placed == null) { return null; }

        List<Material> materials_placed = new ArrayList<Material>();
        for (Block block : blocks_placed) { materials_placed.add(block.getType()); }

        for (Combo c : comboList) {
            List<Material> blocks_needed = c.blocks;
            for (Block block : blocks_placed) {
                if (blocks_needed.size() > materials_placed.size()) { continue; }
                List<Material> match_blocks = materials_placed.subList(materials_placed.size()-blocks_needed.size(), materials_placed.size());
                if (
                        (!c.ignore_order && blocks_needed.equals(match_blocks))
                        || (c.ignore_order && (blocks_needed.containsAll(match_blocks)) && match_blocks.containsAll(blocks_needed))
                ) {
                    if (c.direction.equals("vertical")) {
                        List<Block> last_blocks = blocks_placed.subList(blocks_placed.size()-blocks_needed.size(), blocks_placed.size());
                        int lastBlockY = last_blocks.get(0).getY() - 1;
                        for (Block b : last_blocks) {
                            lastBlockY++;
                            if (lastBlockY != b.getY()) {
                                return null;
                            }
                        }
                        return c;
                    } /*else if (c.direction.equals("horizontal")) { // TODO: FIX THIS
                        List<Block> last_blocks = blocks_placed.subList(blocks_placed.size()-blocks_needed.size(), blocks_placed.size());
                        int lastBlockX = last_blocks.get(0).getX();
                        int lastBlockXp = last_blocks.get(0).getX();
                        int lastBlockXn = last_blocks.get(0).getX();
                        int lastBlockZ = last_blocks.get(0).getZ();
                        int lastBlockZp = last_blocks.get(0).getZ();
                        int lastBlockZn = last_blocks.get(0).getZ();
                        for (Block b : last_blocks) {
                            plugin.getLogger().info("target: " + b.getX() + ", " + b.getZ() + " | lbxp" + lastBlockXp + " | lbxn" + lastBlockXn + " | lbzp" + lastBlockZp + " | lbzn" + lastBlockZn);
                            if (
                                    !(
                                            (
                                                    (lastBlockXp == b.getX() || lastBlockXn == b.getX())
                                                            && (lastBlockZp != b.getZ() || lastBlockZn != b.getZ())
                                            )
                                                    || (
                                                            (lastBlockXp != b.getX() || lastBlockXn != b.getX())
                                                                    && (lastBlockZp == b.getZ() || lastBlockZn == b.getZ())
                                            )
                                    )
                            ) {
                                plugin.getLogger().info("cancel");
                                return null;
                            }
                            lastBlockXp++;
                            lastBlockXn--;
                            lastBlockZp++;
                            lastBlockZn--;
                        }
                        return c;
                    } */
                }
            }
        }

        return null;
    }
}
