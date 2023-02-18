package me.reversee.blockbattle.mechanics;

import me.reversee.blockbattle.Blockbattle;
import org.bukkit.Material;
import org.bukkit.Particle;
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
            Material downBlock = Material.getMaterial(comboConfig.getString(combos_ + ".down"));
            Material upBlock = Material.getMaterial(comboConfig.getString(combos_ + ".up"));
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
                    downBlock,
                    upBlock,
                    selfDestruct,
                    cancelEvent,
                    results,
                    particle
            );
            comboList.add(combo);
        }
    }
}
