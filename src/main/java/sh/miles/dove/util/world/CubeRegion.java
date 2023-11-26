package sh.miles.dove.util.world;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A Cubed region that has no specific affiliation with a world. 3D with or without decimal precision
 *
 * @author Illusion, Miles
 */
public class CubeRegion implements Iterable<BlockVector> {

    protected final double minX;
    protected final double minY;
    protected final double minZ;
    protected final double maxX;
    protected final double maxY;
    protected final double maxZ;

    public CubeRegion(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public CubeRegion(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public CubeRegion(@NotNull final BlockVector min, @NotNull final BlockVector max) {
        this(min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
    }

    public CubeRegion(@NotNull final Location one, @NotNull final Location two) {
        this(Math.min(one.getX(), two.getX()), Math.min(one.getY(), two.getY()), Math.min(one.getZ(), two.getZ()), Math.max(one.getX(), two.getX()), Math.max(one.getY(), two.getY()), Math.max(one.getZ(), two.getZ()));
    }

    public CubeRegion(@NotNull final Location location, final int radius) {
        this(location.getX() - radius, location.getY() - radius, location.getZ() - radius, location.getX() + radius, location.getY() + radius, location.getZ() + radius);
    }

    public CubeRegion(@NotNull final Vector vector, final int radius) {
        this(vector.getX() - radius, vector.getY() - radius, vector.getZ() - radius, vector.getX() + radius, vector.getY() + radius, vector.getZ() + radius);
    }

    public CubeRegion(@NotNull final Vector min, @NotNull final Vector max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    /**
     * Checks whether a x, y, and z location are at a point within this CubeRegion
     *
     * @param x x
     * @param y y
     * @param z z
     * @return true if the coordinates are within this CubeRegion
     */
    public boolean contains(final double x, final double y, final double z) {
        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ);
    }

    /**
     * Check whether a vector is contained within this given CubeRegion
     *
     * @param vector the vector
     * @return true if the vector is in the region
     */
    public boolean contains(@NotNull final Vector vector) {
        return (vector.getX() >= minX && vector.getX() <= maxX) && (vector.getY() >= minY && vector.getY() <= maxY) && (vector.getZ() >= minZ && vector.getZ() <= maxZ);
    }

    /**
     * Check whether a location is contained within this given CubeRegion
     *
     * @param location the location
     * @return true if the vector is in the region
     */
    public boolean contains(@NotNull final Location location) {
        return (location.getX() >= minX && location.getX() <= maxX) && (location.getY() >= minY && location.getY() <= maxY) && (location.getZ() >= minZ && location.getZ() <= maxZ);
    }

    /**
     * Checks whether the given CubeRegion is fully within this CubeRegion
     *
     * @param other the region to check if is fully within this region
     * @return true if the given cuboid is fully within this region, otherwise false
     */
    public boolean contains(@NotNull final CubeRegion other) {
        return contains(other.maxX, other.maxY, other.maxZ) && contains(other.minX, other.minY, other.minZ);
    }

    /**
     * Checks if this cube is intersected at any point b y another region
     *
     * @param other the region to check for an intersection with
     * @return ture if there is an interaction otherwise false
     */
    public boolean intersect(CubeRegion other) {
        // copied from https://bukkit.org/threads/checking-if-two-cuboids-intersect.291432/, thanks to @Syd
        return intersect(other.minX, other.maxX, this.minX, this.maxX) && intersect(other.minY, other.maxY, this.minY, this.maxY) && intersect(other.minZ, other.maxZ, this.minZ, this.maxZ);
    }

    /**
     * Checks if the given two dimensions intersect
     *
     * @param minA the min value of the first dimension
     * @param maxA the max value of the first dimension
     * @param minB the min value of the second dimension
     * @param maxB the max value of the second dimension
     * @return true if the two dimensions intersect otherwise false
     */
    public static boolean intersect(final double minA, final double maxA, final double minB, final double maxB) {
        return minA <= maxB && maxA >= minB;
    }

    /**
     * Floors all the values in this cuboid to the next integer number removing all decimals
     *
     * @return the CubeRegion with floored values
     */
    @NotNull
    public CubeRegion floor() {
        return new CubeRegion(Math.floor(minX), Math.floor(minY), Math.floor(minZ), Math.floor(maxX), Math.floor(maxY), Math.floor(maxZ));
    }

    /**
     * Ceils all the values in this cuboid to the next integer number removing all decimals
     *
     * @return the CubeRegion with floored values
     */
    @NotNull
    public CubeRegion ceil() {
        return new CubeRegion(Math.ceil(minX), Math.ceil(minY), Math.ceil(minZ), Math.ceil(maxX), Math.ceil(maxY), Math.ceil(maxZ));
    }

    /**
     * Allows direct modification to the CubeRegion's values then constructs a new region with the modified values
     *
     * @return the CubeRegion with the modified values
     */
    @NotNull
    public CubeRegion modify(@NotNull final CubeRegionModifier modifier) {
        return modifier.modify(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public double getWidth() {
        return maxX - minX;
    }

    public double getLength() {
        return maxZ - minZ;
    }

    public double getHeight() {
        return maxY - minY;
    }

    public double getVolume() {
        return getWidth() * getLength() * getHeight();
    }

    public int getBlockWidth() {
        return (int) maxX - (int) minX;
    }

    public int getBlockLength() {
        return (int) maxZ - (int) minZ;
    }

    public int getBlockHeight() {
        return (int) maxY - (int) minY;
    }

    public long getBlockVolume() {
        return (long) getBlockWidth() * getBlockLength() * getBlockHeight();
    }

    /**
     * Obtains the center point of the CubeRegion
     *
     * @return the vector at the center point
     */
    @NotNull
    public BlockVector getCenter() {
        return new BlockVector((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2);
    }

    /**
     * Creates a copy of this current region
     *
     * @return a copy of this current region
     */
    @NotNull
    public CubeRegion copy() {
        return new CubeRegion(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Expands the CubeRegion by the specified amount, returning a new CubeRegion.
     *
     * @param x the amount to expand in the x direction
     * @param y the amount to expand in the y direction
     * @param z the amount to expand in the z direction
     * @return the new CubeRegion
     */
    @NotNull
    public CubeRegion add(int x, int y, int z) {
        return new CubeRegion(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);
    }

    /**
     * Expands the CubeRegion by the specified amount, returning a new CubeRegion.
     *
     * @param vector the amount to expand in each direction
     * @return the new CubeRegion
     */
    @NotNull
    public CubeRegion add(Vector vector) {
        return add(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    /**
     * Shrinks the CubeRegion by the specified amount, returning a new CubeRegion.
     *
     * @param x the amount to shrink in the x direction
     * @param y the amount to shrink in the y direction
     * @param z the amount to shrink in the z direction
     * @return the new CubeRegion
     */
    @NotNull
    public CubeRegion subtract(int x, int y, int z) {
        return new CubeRegion(minX - x, minY - y, minZ - z, maxX - x, maxY - y, maxZ - z);
    }

    /**
     * Shrinks the CubeRegion by the specified amount, returning a new CubeRegion.
     *
     * @param vector the amount to shrink in each direction
     * @return the new CubeRegion
     */
    @NotNull
    public CubeRegion subtract(Vector vector) {
        return subtract(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @NotNull
    public BlockVector getMin() {
        return new BlockVector(minX, minY, minZ);
    }

    @NotNull
    public BlockVector getMax() {
        return new BlockVector(maxX, maxY, maxZ);
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public int getMinBlockX() {
        return (int) minX;
    }

    public int getMinBlockY() {
        return (int) minY;
    }

    public int getMinBlockZ() {
        return (int) minZ;
    }

    public int getMaxBlockX() {
        return (int) maxX;
    }

    public int getMaxBlockY() {
        return (int) maxY;
    }

    public int getMaxBlockZ() {
        return (int) maxZ;
    }

    /**
     * Displays a wireframe of the cuboid using particles.
     *
     * @param world the world
     * @param color the color of the particles
     */
    public void displayParticles(@NotNull final World world, @NotNull final Color color) {
        // Basically wire-mesh between all corners
        List<Location> locations = new ArrayList<>();

        locations.add(new Location(world, minX, minY, minZ));
        locations.add(new Location(world, minX, minY, maxZ));
        locations.add(new Location(world, minX, maxY, minZ));
        locations.add(new Location(world, minX, maxY, maxZ));
        locations.add(new Location(world, maxX, minY, minZ));
        locations.add(new Location(world, maxX, minY, maxZ));
        locations.add(new Location(world, maxX, maxY, minZ));
        locations.add(new Location(world, maxX, maxY, maxZ));

        for (int index = 0; index < locations.size(); index++) {
            Location start = locations.get(index);
            for (int index2 = index + 1; index2 < locations.size(); index2++) {
                Location finish = locations.get(index2);
                drawParticleLineBetween(start, finish, color);
            }
        }

    }

    /**
     * Draws a line of particles between two locations.
     *
     * @param start  the start location
     * @param finish the finish location
     * @param color  the color of the particles
     */
    private void drawParticleLineBetween(Location start, Location finish, Color color) {
        double offset = 0.1;
        double distance = start.distance(finish);
        double steps = distance / offset;

        Vector direction = finish.toVector().subtract(start.toVector()).normalize().multiply(offset);

        for (int index = 0; index < steps; index++) {
            Location location = start.clone().add(direction.clone().multiply(index));
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(color, 1));
        }
    }

    @NotNull
    @Override
    public Iterator<BlockVector> iterator() {
        return new CubeRegionIterator(this);
    }

    @Override
    public String toString() {
        return "CubeRegion{" + "minX=" + minX + ", minY=" + minY + ", minZ=" + minZ + ", maxX=" + maxX + ", maxY=" + maxY + ", maxZ=" + maxZ + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final CubeRegion that)) return false;
        return Double.compare(minX, that.minX) == 0 && Double.compare(minY, that.minY) == 0 && Double.compare(minZ, that.minZ) == 0 && Double.compare(maxX, that.maxX) == 0 && Double.compare(maxY, that.maxY) == 0 && Double.compare(maxZ, that.maxZ) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Handles direct modification of a CubeRegion
     */
    public interface CubeRegionModifier {
        /**
         * Provides previous min and max values for the CubeRegion being modified
         *
         * @param minX the minX
         * @param minY the minY
         * @param minZ the minZ
         * @param maxX the maxX
         * @param maxY the maxY
         * @param maxZ the maxZ
         * @return the new cube region
         */
        @NotNull
        CubeRegion modify(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ);
    }

    /**
     * An iterator for a CubeRegion
     */
    private static class CubeRegionIterator implements Iterator<BlockVector> {
        private final CubeRegion region;
        private int index;

        public CubeRegionIterator(@NotNull final CubeRegion cubeRegion) {
            this.region = cubeRegion;
        }

        @Override
        public boolean hasNext() {
            return index < region.getVolume();
        }

        @Override
        public BlockVector next() {
            if (index > region.getBlockLength()) {
                throw new NoSuchElementException("There is no next element within this iterator");
            }
            int x = (int) (index % region.getWidth() + region.minX);
            int y = (int) (index / region.getWidth() % region.getHeight() + region.minY);
            int z = (int) (index / region.getWidth() / region.getHeight() + region.minZ);
            index++;
            return new BlockVector(x, y, z);
        }
    }

}
