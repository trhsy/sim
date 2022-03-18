package com.trhsy.sim.api.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName ResourceId
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:48
 * ========================================
 **/
public abstract class ResourceId {
    protected ResourceId() {
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("resourceName", RobotManager.getResourceIdName(this.getClass()));
    }

    protected void readFromNBT(NBTTagCompound nbt) {
    }

    public static ResourceId load(NBTTagCompound nbt) {
        try {
            Class cls;
            if (nbt.hasKey("class")) {
                cls = RobotManager.getResourceIdByLegacyClassName(nbt.getString("class"));
            } else {
                cls = RobotManager.getResourceIdByName(nbt.getString("resourceName"));
            }

            ResourceId id = ( ResourceId)cls.newInstance();
            id.readFromNBT(nbt);
            return id;
        } catch (Throwable var3) {
            var3.printStackTrace();
            return null;
        }
    }
}