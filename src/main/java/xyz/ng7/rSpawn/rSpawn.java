package xyz.ng7.rSpawn;

import com.onarandombox.multiverseinventories.MultiverseInventories;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.ng7.rSpawn.Listeners.OnJoin;
import xyz.ng7.rSpawn.Listeners.OnRespawn;
import xyz.ng7.rSpawn.MultiverseInventories.SpawnPointSharable;
import xyz.ng7.rSpawn.Utils.ConfigFile;

public class rSpawn extends JavaPlugin {
    private FileConfiguration config;
    private SpawnPointSharable sharable;
    private MultiverseInventories inventories;

    public rSpawn() {
        config = new ConfigFile(this).run();
        inventories = (MultiverseInventories) getServer().getPluginManager().getPlugin("Multiverse-Inventories");

        if (inventories == null) {
            getLogger().severe("Multiverse Inventories not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        } else sharable = new SpawnPointSharable(this);
    }

    private void checkConfigVersion() {
        int version = 1;

        if(!config.contains("version") || config.getInt("version") < version) {
            getLogger().severe("Your config version is outdated. Please move your config file to another location and restart the server, then copy the old settings to new config file.");
            getLogger().severe("Plugin will be disabled to prevent errors.");
            getPluginLoader().disablePlugin(this);
        }
    }

    private void regListeners() {
        getServer().getPluginManager().registerEvents(new OnJoin(this), this);
        getServer().getPluginManager().registerEvents(new OnRespawn(this), this);
    }

    @Override
    public void onEnable() {
        checkConfigVersion();
        regListeners();
    }

    public FileConfiguration getConfigFile() {
        return config;
    }

    public MultiverseInventories getInventories() {
        return inventories;
    }
}