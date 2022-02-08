package com.trhsy.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.boards.RedstoneBoardRobot;
import com.trhsy.buildcraft.api.core.IZone;
import com.trhsy.cofh.api.energy.IEnergyStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * ========================================
 *
 * @ClassName EntityRobotBase
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:46
 * ========================================
 **/
public abstract class EntityRobotBase extends EntityLiving implements IInventory, IFluidHandler {
    public static final int MAX_ENERGY = 100000;
    public static final int SAFETY_ENERGY = 25000;
    public static final long NULL_ROBOT_ID = 9223372036854775807L;

    public EntityRobotBase(World par1World) {
        super(par1World);
    }

    public abstract void setItemInUse(ItemStack var1);

    public abstract void setItemActive(boolean var1);

    public abstract boolean isMoving();

    public abstract IDockingStation getLinkedStation();

    public abstract RedstoneBoardRobot getBoard();

    public abstract void aimItemAt(int var1, int var2, int var3);

    public abstract int getEnergy();

    public abstract IEnergyStorage getBattery();

    public abstract IDockingStation getDockingStation();

    public abstract void dock(IDockingStation var1);

    public abstract void undock();

    public abstract IZone getZoneToWork();

    public abstract boolean containsItems();

    public abstract boolean hasFreeSlot();

    public abstract void unreachableEntityDetected(Entity var1);

    public abstract boolean isKnownUnreachable(Entity var1);

    public abstract long getRobotId();

    public abstract IRobotRegistry getRegistry();

    public abstract void releaseResources();

    public abstract void onChunkUnload();
}
