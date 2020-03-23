package xyz.ng7.rSpawn.Utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnLocation {
    private World w;
    private int x, z;
    private List<String> disabledBiomes;

    public SpawnLocation(FileConfiguration config, World world) {
        w = world;
        x = config.getInt("radius.x");
        z = config.getInt("radius.z");
        disabledBiomes = config.getStringList("disabled-biomes");
    }

    public int randomInt(boolean x) {
        if (x) return ThreadLocalRandom.current().nextInt(-this.x, this.x + 1);
        return ThreadLocalRandom.current().nextInt(-this.z, this.z + 1);
    }

    public Location gen() {
        boolean regen;
        int x, y, z;

        do {
            regen = false;

            x = randomInt(true);
            z = randomInt(false);
            y = w.getHighestBlockYAt(x, z);

            for (String b: this.disabledBiomes) if (w.getBiome(x, y, z) == Biome.valueOf(b)) {
                regen = true;
                break;
            }
        } while (regen);

        return new Location(w, x - 0.5, y, z - 0.5);
    }
}
