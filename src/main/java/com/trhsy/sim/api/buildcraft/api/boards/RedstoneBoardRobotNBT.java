package com.trhsy.sim.api.buildcraft.api.boards;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


import com.trhsy.sim.api.buildcraft.api.robots.EntityRobotBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * ========================================
 *
 * @ClassName RedstoneBoardRobotNBT
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:46
 * ========================================
 **/
public abstract class RedstoneBoardRobotNBT extends RedstoneBoardNBT<EntityRobotBase> {
    public RedstoneBoardRobotNBT() {
    }

    @Override
    public RedstoneBoardRobot create(NBTTagCompound nbt, EntityRobotBase robot) {
        return this.create(robot);
    }

    public abstract RedstoneBoardRobot create(EntityRobotBase var1);

    public abstract ResourceLocation getRobotTexture();
}
