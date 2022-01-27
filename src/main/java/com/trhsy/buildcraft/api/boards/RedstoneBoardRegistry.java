package com.trhsy.buildcraft.api.boards;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;

/**
 * ========================================
 *
 * @ClassName RedstoneBoardRegistry
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:44
 * ========================================
 **/
public abstract class RedstoneBoardRegistry {
    public static RedstoneBoardRegistry instance;

    public RedstoneBoardRegistry() {
    }

    public abstract void registerBoardClass(RedstoneBoardNBT<?> var1, float var2);

    public abstract void createRandomBoard(NBTTagCompound var1);

    public abstract RedstoneBoardNBT getRedstoneBoard(NBTTagCompound var1);

    public abstract RedstoneBoardNBT<?> getRedstoneBoard(String var1);

    public abstract void registerIcons(IIconRegister var1);

    public abstract Collection<RedstoneBoardNBT<?>> getAllBoardNBTs();

}
