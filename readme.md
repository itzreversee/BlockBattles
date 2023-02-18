# Block Battle
My own take on "Block Battles" in Minecraft!
Plugin for paper version ```1.19.3```

## Installation
 - Get compiled jar file from `target/` or newest release
 - Copy it to your minecraft server's `plugins/` directory
 - Start your server
 - Adjust `config.yml` and `combos.yml` to your needs
 - Use `/arena reload` to reload

## Usage
 - Create arena using `/arena create <name>`
 - Add yourself to the arena using `/arena add_player <player>`
 - Start the arena `/arena start <name>`
 - Play!

## Block Placements (default config):
 - Combos: (Place one block on another, must be in order, down, up)
   - Sponge + Ice -> Sets Rain and gives you Packed Ice
   - Magma Block on the Ice Block -> Cancels rain ( must be used right after placing the Ice)
   - Oak Fence + Soul Lantern -> 2x Netherite Block
   - Oak Fence + Glowstone -> Sets time to day (3000)
   - Oak Fence + Shroomlight -> Sets time to night (16000)
   - Twisting Vines + Shroomlight -> Gives you 4x Netherrack
   - Stone + Dripstone -> Gives you 4x Gravel
   - Gravel + Soul Lantern -> Gives you Lantern

## Compile
 - Install Maven
 - Sync if needed
 - run ```mvn package```

