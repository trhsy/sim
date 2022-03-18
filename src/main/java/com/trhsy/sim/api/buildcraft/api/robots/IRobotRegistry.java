package com.trhsy.sim.api.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


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

    void registerRobot( EntityRobotBase var1);

    void killRobot( EntityRobotBase var1);

    void unloadRobot( EntityRobotBase var1);

    EntityRobotBase getLoadedRobot(long var1);

    boolean isTaken( ResourceId var1);

    long robotIdTaking( ResourceId var1);

    EntityRobotBase robotTaking( ResourceId var1);

    boolean take( ResourceId var1, EntityRobotBase var2);

    boolean take( ResourceId var1, long var2);

    void release(ResourceId var1);

    void releaseResources(EntityRobotBase var1);

    DockingStation getStation(int var1, int var2, int var3, ForgeDirection var4);

    Collection< DockingStation> getStations();

    void registerStation( DockingStation var1);

    void removeStation( DockingStation var1);

    void take( DockingStation var1, long var2);

    void release(DockingStation var1, long var2);

    void writeToNBT(NBTTagCompound var1);

    void readFromNBT(NBTTagCompound var1);

    void registryMarkDirty();
}
