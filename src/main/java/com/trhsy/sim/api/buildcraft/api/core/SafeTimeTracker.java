package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName SafeTimeTracker
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:12
 * ========================================
 **/
public class SafeTimeTracker {
    private long lastMark = -9223372036854775808L;
    private long duration = -1L;
    private long randomRange = 0L;
    private long lastRandomDelay = 0L;
    private long internalDelay = 1L;

    /** @deprecated */
    public SafeTimeTracker() {
    }

    public SafeTimeTracker(long delay) {
        this.internalDelay = delay;
    }

    public SafeTimeTracker(long delay, long random) {
        this.internalDelay = delay;
        this.randomRange = random;
    }

    public boolean markTimeIfDelay(World world) {
        return this.markTimeIfDelay(world, this.internalDelay);
    }

    /** @deprecated */
    public boolean markTimeIfDelay(World world, long delay) {
        if (world == null) {
            return false;
        } else {
            long currentTime = world.getTotalWorldTime();
            if (currentTime < this.lastMark) {
                this.lastMark = currentTime;
                return false;
            } else if (this.lastMark + delay + this.lastRandomDelay <= currentTime) {
                this.duration = currentTime - this.lastMark;
                this.lastMark = currentTime;
                this.lastRandomDelay = (long)((int)(Math.random() * (double)this.randomRange));
                return true;
            } else {
                return false;
            }
        }
    }

    public long durationOfLastDelay() {
        return this.duration > 0L ? this.duration : 0L;
    }

    public void markTime(World world) {
        this.lastMark = world.getTotalWorldTime();
    }
}
