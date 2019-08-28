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
        boolean a;
        int x, z;
        List<String> dB = c.getStringList("disabled-biomes");

        do {
            a = false;

            x = randomInt(-(c.getInt("radius.x")), c.getInt("radius.x"));
            z = randomInt(-(c.getInt("radius.z")), c.getInt("radius.z"));

            for (String b: dB) if (w.getBiome(x, z) == Biome.valueOf(b)) {
                a = true;
                break;
            }
        } while (a);

        return new Location(w, x + 0.5, w.getHighestBlockYAt(x, z), z + 0.5);
    }
}
