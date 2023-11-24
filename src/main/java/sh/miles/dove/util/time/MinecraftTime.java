package sh.miles.dove.util.time;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * A Utility class to help with durations between non-standard time values such as ticks.
 */
public final class MinecraftTime {

    public static final int MILLISECONDS_PER_TICK = 50;

    private final long current;

    private MinecraftTime(long current) {
        this.current = current;
    }

    public long ticksBetween(long now) {
        long between = now - current;
        return between / 50;
    }

    public long ticksBetween(MinecraftTime time) {
        return ticksBetween(time.current);
    }

    public Instant toInstant() {
        return Instant.ofEpochMilli(this.current);
    }

    public static MinecraftTime now() {
        return new MinecraftTime(System.currentTimeMillis());
    }

    public static MinecraftTime ofEpochMillis(final long current) {
        return new MinecraftTime(current);
    }

    public static MinecraftTime fromInstant(@NotNull final Instant instant) {
        return new MinecraftTime(instant.toEpochMilli());
    }
}
