package xyz.ng7.rSpawn.MultiverseInventories;

import com.onarandombox.multiverseinventories.profile.PlayerProfile;
import com.onarandombox.multiverseinventories.share.ProfileEntry;
import com.onarandombox.multiverseinventories.share.Sharable;
import com.onarandombox.multiverseinventories.share.SharableHandler;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.ng7.rSpawn.rSpawn;

public class SpawnPointSharable {
    public static final Sharable<Location> SPAWN_POINT = new Sharable.Builder<>("spawn_point", Location.class, new SharableHandler<Location>() {
                @Override
                public void updateProfile(PlayerProfile profile, Player player) {
                    profile.set(SPAWN_POINT, getPlayerSpawnPoint(player));
                }

                @Override
                public boolean updatePlayer(Player player, PlayerProfile profile) {
                    Location loc = profile.get(SPAWN_POINT);

                    if (loc == null) return false;

                    setPlayerSpawnPoint(player, loc);

                    return true;
                }
            }
    ).defaultSerializer(new ProfileEntry(false, "spawnPoint")).altName("spawn").build();

    private static final NamespacedKey SPAWN_POINT_KEY = new NamespacedKey(rSpawn.getPlugin(rSpawn.class), "spawn_point");

    private static final LocationTagType LOCATION_TAG_TYPE = new LocationTagType();

    private static class LocationTagType implements PersistentDataType<PersistentDataContainer, Location> {

        private static NamespacedKey WORLD_KEY = new NamespacedKey(SPAWN_POINT_KEY.getKey(), "world_uuid");
        private static NamespacedKey X_KEY = new NamespacedKey(SPAWN_POINT_KEY.getKey(), "x");
        private static NamespacedKey Y_KEY = new NamespacedKey(SPAWN_POINT_KEY.getKey(), "y");
        private static NamespacedKey Z_KEY = new NamespacedKey(SPAWN_POINT_KEY.getKey(), "z");

        @Override
        public @NotNull
        Class<PersistentDataContainer> getPrimitiveType() {
            return PersistentDataContainer.class;
        }

        @Override
        public @NotNull
        Class<Location> getComplexType() {
            return Location.class;
        }

        @NotNull
        @Override
        public PersistentDataContainer toPrimitive(@NotNull Location complex, @NotNull PersistentDataAdapterContext context) {
            PersistentDataContainer primitive = context.newPersistentDataContainer();

            World world = complex.getWorld();
            if (world != null) primitive.set(WORLD_KEY, UUID_TAG_TYPE, world.getUID());

            primitive.set(X_KEY, PersistentDataType.DOUBLE, complex.getX());
            primitive.set(Y_KEY, PersistentDataType.DOUBLE, complex.getY());
            primitive.set(Z_KEY, PersistentDataType.DOUBLE, complex.getZ());

            return primitive;
        }

        @NotNull
        @Override
        public Location fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
            UUID worldUid = primitive.get(WORLD_KEY, UUID_TAG_TYPE);
            Double x = primitive.get(X_KEY, PersistentDataType.DOUBLE);
            Double y = primitive.get(Y_KEY, PersistentDataType.DOUBLE);
            Double z = primitive.get(Z_KEY, PersistentDataType.DOUBLE);

            return new Location(worldUid != null ? Bukkit.getWorld(worldUid) : null,
                    x != null ? x : 0D, y != null ? y : 0D, z != null ? z : 0D);
        }
    }

    private static UUIDTagType UUID_TAG_TYPE = new UUIDTagType();

    private static class UUIDTagType implements PersistentDataType<byte[], UUID> {

        @Override
        public @NotNull Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public @NotNull Class<UUID> getComplexType() {
            return UUID.class;
        }

        @Override
        public @NotNull byte[] toPrimitive(@NotNull UUID complex, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(complex.getMostSignificantBits());
            bb.putLong(complex.getLeastSignificantBits());
            return bb.array();
        }

        @Override
        public @NotNull UUID fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(primitive);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }

    public static Location getPlayerSpawnPoint(Player player) {
        return player.getPersistentDataContainer().get(SPAWN_POINT_KEY, LOCATION_TAG_TYPE);
    }

    public static void setPlayerSpawnPoint(Player player, Location spawnPoint) {
        player.getPersistentDataContainer().set(SPAWN_POINT_KEY, LOCATION_TAG_TYPE, spawnPoint);
    }
}