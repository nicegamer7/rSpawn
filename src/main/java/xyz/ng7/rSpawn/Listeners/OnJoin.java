package xyz.ng7.rSpawn.Listeners;

import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.WorldGroup;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import xyz.ng7.rSpawn.MultiverseInventories.SpawnPointSharable;
import xyz.ng7.rSpawn.rSpawn;
import xyz.ng7.rSpawn.Utils.SpawnLocation;

public class OnJoin implements Listener {
    private FileConfiguration c;
    private MultiverseInventories i;
    private String g;

    public OnJoin(rSpawn r) {
        this.c = r.getConfigFile();
        this.i = r.getInventories();
    }

    @EventHandler
    public void onJoin(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        List<WorldGroup> lwg = this.i.getGroupManager().getGroupsForWorld(p.getWorld().getName());

        for (WorldGroup fg: lwg) if (fg.isSharing(SpawnPointSharable.SPAWN_POINT)) {
            g = fg.getName();
            break;
        }

        if (g != null && SpawnPointSharable.getPlayerSpawnPoint(p) == null) {
            Location sp = new SpawnLocation(this.c, p.getWorld()).gen();
            SpawnPointSharable.setPlayerSpawnPoint(p, sp);
            p.teleport(sp);
        }
    }
}
