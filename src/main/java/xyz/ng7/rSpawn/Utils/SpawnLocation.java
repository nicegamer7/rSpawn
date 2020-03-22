package xyz.ng7.rSpawn.Utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnLocation {
    private FileConfiguration c;
    private World w;

    public SpawnLocation(FileConfiguration config, World world) {
        c = config;
        w = world;
    }

    private int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public Location gen() {
        boolean regen;
        int x, y, z;
        int xRadius, zRadius;
        List<String> dB = c.getStringList("disabled-biomes");

        do {
            regen = false;

            xRadius = c.getInt("radius.x");
            zRadius = c.getInt("radius.z");

            x = randomInt(-xRadius, xRadius);
            z = randomInt(-zRadius, zRadius);
            y = w.getHighestBlockYAt(x, z);

            for (String b: dB) if (w.getBiome(x, y, z) == Biome.valueOf(b)) {
                regen = true;
                break;
            }
        } while (regen);

        return new Location(w, x + 0.5, y, z + 0.5);
    }
}
