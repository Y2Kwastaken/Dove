package sh.miles.dove.util.world;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A CubeRegion which is bound to a specific world
 */
public final class WorldCubeRegion extends CubeRegion {

    private final UUID worldUid;

    public WorldCubeRegion(@NotNull final UUID worldUid, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.worldUid = worldUid;
    }

    public WorldCubeRegion(@NotNull final UUID worldUid, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.worldUid = worldUid;
    }

    public WorldCubeRegion(@NotNull final UUID worldUid, @NotNull final BlockVector min, @NotNull final BlockVector max) {
        super(min, max);
        this.worldUid = worldUid;
    }

    public WorldCubeRegion(@NotNull final UUID worldUid, @NotNull final Location one, @NotNull final Location two) {
        super(one, two);
        this.worldUid = worldUid;
    }

    public WorldCubeRegion(@NotNull final UUID worldUid, @NotNull final Location location, final int radius) {
        super(location, radius);
        this.worldUid = worldUid;
    }

    public WorldCubeRegion(@NotNull final UUID worldUid, @NotNull final Vector vector, final int radius) {
        super(vector, radius);
        this.worldUid = worldUid;
    }

    public WorldCubeRegion(@NotNull final UUID worldUid, @NotNull final Vector min, @NotNull final Vector max) {
        super(min, max);
        this.worldUid = worldUid;
    }

    @Override
    public boolean contains(@NotNull final Location location) {
        return location.getWorld().getUID().equals(this.worldUid) && super.contains(location);
    }

    /**
     * Gets all corner chunks within this region
     *
     * @return the corner chunks of this region
     */
    public Set<Chunk> getCornerChunks() {
        final Set<Chunk> chunks = new HashSet<>();
        final World world = worldOrThrow();
        // get for corner block's chunks of arbitrary y values
        chunks.add(world.getChunkAt(((int) (minX)) >> 4, (int) (minZ) >> 4));
        chunks.add(world.getChunkAt(((int) (minX)) >> 4, (int) (maxZ) >> 4));
        chunks.add(world.getChunkAt(((int) (maxX)) >> 4, (int) (minZ) >> 4));
        chunks.add(world.getChunkAt(((int) (maxX)) >> 4, (int) (maxZ) >> 4));
        return chunks;
    }

    /**
     * Displays a wireframe of the cuboid using particles.
     *
     * @param color the color of the particles
     */
    public void displayParticles(@NotNull final Color color) {
        displayParticles(worldOrThrow(), color);
    }

    /**
     * Gets all intersecting chunks within this region
     *
     * @return the intersecting chunks of this region
     */
    public Set<Chunk> getAllIntersectingChunks() {
        final Set<Chunk> chunks = new HashSet<>();
        final World world = worldOrThrow();
        final int minChunkX = ((int) minX) >> 4;
        final int minChunkZ = ((int) minZ) >> 4;
        final int maxChunkX = ((int) maxX) >> 4;
        final int maxChunkZ = ((int) maxZ) >> 4;
        for (int chunkX = minChunkX; chunkX < maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ < maxChunkZ; chunkZ++) {
                chunks.add(world.getChunkAt(chunkX, chunkZ));
            }
        }
        return chunks;
    }

    @NotNull
    public UUID getWorldUid() {
        return worldUid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final WorldCubeRegion that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(worldUid, that.worldUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), worldUid);
    }

    private World worldOrThrow() {
        final World world = Bukkit.getWorld(this.worldUid);
        if (world == null) {
            throw new IllegalStateException("Attempted to obtain unloaded or nonexistent world");
        }
        return world;
    }
}
