package com.trhsy.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import jdk.management.resource.ResourceId;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collection;

/**
 * ========================================
 *
 * @ClassName IRobotRegistry
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:47
 * ========================================
 **/
public interface IRobotRegistry {
    long getNextRobotId();

    void registerRobot(EntityRobotBase var1);

    void killRobot(EntityRobotBase var1);

    void unloadRobot(EntityRobotBase var1);

    EntityRobotBase getLoadedRobot(long var1);

    boolean isTaken(ResourceId var1);

    long robotIdTaking(ResourceId var1);

    EntityRobotBase robotTaking(ResourceId var1);

    boolean take(ResourceId var1, EntityRobotBase var2);

    boolean take(ResourceId var1, long var2);

    void release(ResourceId var1);

    void releaseResources(EntityRobotBase var1);

    IDockingStation getStation(int var1, int var2, int var3, ForgeDirection var4);

    Collection<IDockingStation> getStations();

    void registerStation(IDockingStation var1);

    void removeStation(IDockingStation var1);

    void take(IDockingStation var1, long var2);

    void release(IDockingStation var1, long var2);

    void writeToNBT(NBTTagCompound var1);

    void readFromNBT(NBTTagCompound var1);
}
