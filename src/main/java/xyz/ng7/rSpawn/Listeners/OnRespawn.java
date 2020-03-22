package xyz.ng7.rSpawn.Listeners;

import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.WorldGroup;
import java.util.List;

import com.onarandombox.multiverseinventories.share.Sharables;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.ng7.rSpawn.rSpawn;
import xyz.ng7.rSpawn.Utils.SpawnLocation;

public class OnRespawn implements Listener {
    private FileConfiguration c;
    private MultiverseInventories i;
    private String g;

    public OnRespawn(rSpawn rS) {
        c = rS.getConfigFile();
        i = rS.getInventories();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        List<WorldGroup> lwg = i.getGroupManager().getGroupsForWorld(p.getWorld().getName());

        for (WorldGroup fg: lwg) if (fg.isSharing(Sharables.SPAWN_LOCATION)) {
            g = fg.getName();
            break;
        }

        if (g != null) {
            Location sp = Sharables.getPlayerSpawnLocation(p);

            if (e.isBedSpawn()) return;
            else if (sp != null) e.setRespawnLocation(sp);
            else {
                Sharables.setPlayerSpawnLocation(p, new SpawnLocation(c, p.getWorld()).gen());
                e.setRespawnLocation(Sharables.getPlayerSpawnLocation(p));
            }
        }
    }
}
