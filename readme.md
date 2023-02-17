# Block Battle
My own take on "Block Battles" in Minecraft!
Plugin for paper version ```1.19.3```

## Installation
 - Get compiled jar file from `target/` or newest release
 - Copy it to your minecraft server's `plugins/` directory
 - Reload or restart

## Usage
 - Create arena using `/arena create <name>`
 - Add yourself to the arena using `/arena add_player <player>`
 - Start the arena `/arena start <name>`
 - Play!

## Block Placements:
 - Combos: (Place one block on another, must be in order, down, up)
   - Sponge + Ice -> Sets Rain and gives you Packed Ice
   - Magma Block on the Ice Block -> Cancels rain ( must be used right after placing the Ice)
   - Oak Fence + Soul Lantern -> 2x Netherite Block
   - Oak Fence + Glowstone -> Sets time to day (3000)
   - Oak Fence + Shroomlight -> Sets time to night (16000)
   - Twisting Vines + Shroomlight -> Gives you 4x Netherrack
   - Stone + Dripstone -> Gives you 4x Gravel
   - Gravel + Soul Lantern -> Gives you Lantern
 - Interactions:
   - Place furnace and click on the fuel slot -> Gives you 2x Coal, destroys furnace


## Compile
 - Install Maven
 - Sync if needed
 - run ```mvn package```

