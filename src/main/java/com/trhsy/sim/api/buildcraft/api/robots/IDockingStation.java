package com.trhsy.sim.api.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.api.buildcraft.api.core.BlockIndex;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IDockingStation
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:47
 * ========================================
 **/
public interface IDockingStation {
    int x();

    int y();

    int z();

    ForgeDirection side();

    EntityRobotBase robotTaking();

    long robotIdTaking();

    long linkedId();

    boolean isTaken();

    void writeToNBT(NBTTagCompound var1);

    void readFromNBT(NBTTagCompound var1);

    BlockIndex index();

    boolean take(EntityRobotBase var1);
}
