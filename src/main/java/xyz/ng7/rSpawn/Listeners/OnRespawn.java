package xyz.ng7.rSpawn.Listeners;

import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.WorldGroup;
import com.onarandombox.multiverseinventories.share.Sharables;
import java.util.List;
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

    public OnRespawn(rSpawn r) {
        this.c = r.getConfigFile();
        this.i = r.getInventories();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        List<WorldGroup> lwg = this.i.getGroupManager().getGroupsForWorld(p.getWorld().getName());

        for (WorldGroup fg: lwg) if (fg.isSharing(Sharables.SPAWN_LOCATION)) {
            g = fg.getName();
            break;
        }

        if (g != null) {
            if (e.isBedSpawn()) return;

            Location sp = Sharables.getPlayerSpawnLocation(p);
            if (sp != null) {
                if (sp.getY() != sp.getWorld().getHighestBlockYAt(sp)) {
                    sp.setY(sp.getWorld().getHighestBlockYAt(sp));
                    Sharables.setPlayerSpawnLocation(p, sp);
                }

                e.setRespawnLocation(sp);
            } else {
                Sharables.setPlayerSpawnLocation(p, new SpawnLocation(this.c, p.getWorld()).gen());
                e.setRespawnLocation(Sharables.getPlayerSpawnLocation(p));
            }
        }
    }
}
