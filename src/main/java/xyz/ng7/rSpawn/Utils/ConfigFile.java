package xyz.ng7.rSpawn.Utils;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.ng7.rSpawn.rSpawn;

public class ConfigFile {
    private rSpawn rS;

    public ConfigFile(rSpawn rSpawn) {
        rS = rSpawn;
    }

    public FileConfiguration run() {
        FileConfiguration c = null;

        try {
            if (!rS.getDataFolder().exists()) rS.getDataFolder().mkdirs();

            File f = new File(rS.getDataFolder(), "config.yml");

            if(!f.exists()) rS.saveDefaultConfig();

            c = rS.getConfig();
        } catch(Exception e) {
            rS.getLogger().severe("Cannot read data folder. Disabling plugin.");
            rS.getPluginLoader().disablePlugin(rS);
        }

        return c;
    }
}
