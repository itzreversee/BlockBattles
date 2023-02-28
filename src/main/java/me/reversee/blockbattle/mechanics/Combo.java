package me.reversee.blockbattle.mechanics;

import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.HashMap;
import java.util.List;

public class Combo {
    public Combo(String name, List<Material> blocks, boolean ignore_order, String direction, boolean self_destruct, boolean cancel_event, HashMap<String, String> result, Particle particle) {
        this.name = name;
        this.blocks = blocks;
        this.ignore_order = ignore_order;
        this.direction = direction;
        this.self_destruct = self_destruct;
        this.cancel_event = cancel_event;
        this.result = result;
        this.particle = particle;
    }
    public String name;
    public List<Material> blocks;
    public boolean ignore_order;
    public String direction;
    public boolean self_destruct;
    public boolean cancel_event;
    public HashMap<String, String> result;

    public Particle particle;

}
