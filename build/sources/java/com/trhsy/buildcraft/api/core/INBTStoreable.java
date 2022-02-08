package com.trhsy.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.nbt.NBTTagCompound;

/**
 * ========================================
 *
 * @ClassName INBTStoreable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:03
 * ========================================
 **/
public interface INBTStoreable {
    void readFromNBT(NBTTagCompound var1);

    void writeToNBT(NBTTagCompound var1);
}
