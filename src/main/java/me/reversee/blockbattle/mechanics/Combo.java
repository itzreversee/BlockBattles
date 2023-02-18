package me.reversee.blockbattle.mechanics;

import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.HashMap;

public class Combo {
    public Combo(String name, Material downBlock, Material upBlock, boolean self_destruct, boolean cancel_event, HashMap<String, String> result, Particle particle) {
        this.name = name;
        this.downBlock = downBlock;
        this.upBlock = upBlock;
        this.self_destruct = self_destruct;
        this.cancel_event = cancel_event;
        this.result = result;
        this.particle = particle;
    }
    public String name;
    public Material downBlock;
    public Material upBlock;
    public boolean self_destruct;
    public boolean cancel_event;
    public HashMap<String, String> result;

    public Particle particle;

}
