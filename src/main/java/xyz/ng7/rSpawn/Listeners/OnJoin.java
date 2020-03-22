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
import org.bukkit.event.player.PlayerChangedWorldEvent;
import xyz.ng7.rSpawn.rSpawn;
import xyz.ng7.rSpawn.Utils.SpawnLocation;

public class OnJoin implements Listener {
    private FileConfiguration c;
    private MultiverseInventories i;
    private String g;

    public OnJoin(rSpawn rS) {
        c = rS.getConfigFile();
        i = rS.getInventories();
    }

    @EventHandler
    public void onJoin(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        List<WorldGroup> lwg = i.getGroupManager().getGroupsForWorld(p.getWorld().getName());

        for (WorldGroup fg: lwg) if (fg.isSharing(Sharables.SPAWN_LOCATION)) {
            g = fg.getName();
            break;
        }

        if (g != null) {
            Location sp = Sharables.getPlayerSpawnLocation(p);

            if (sp == null) {
                Sharables.setPlayerSpawnLocation(p, new SpawnLocation(c, p.getWorld()).gen());
                p.teleport(Sharables.getPlayerSpawnLocation(p));
            }
        }
    }
}
