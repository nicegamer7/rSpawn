package xyz.ng7.rSpawn.Utils;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.ng7.rSpawn.rSpawn;

public class ConfigFile {
    private rSpawn r;

    public ConfigFile(rSpawn r) {
        this.r = r;
    }

    public FileConfiguration run() {
        FileConfiguration c = null;

        try {
            if (!this.r.getDataFolder().exists()) this.r.getDataFolder().mkdirs();

            File f = new File(this.r.getDataFolder(), "config.yml");

            if(!f.exists()) this.r.saveDefaultConfig();

            c = this.r.getConfig();
        } catch (Exception e) {
            this.r.getLogger().severe("Cannot read data folder. Disabling plugin.");
            this.r.getPluginLoader().disablePlugin(this.r);
        }

        return c;
    }
}
