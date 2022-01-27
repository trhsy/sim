package com.trhsy.buildcraft.api.boards;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName RedstoneBoardNBT
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:43
 * ========================================
 **/
public abstract class RedstoneBoardNBT<T> {
    private static Random rand = new Random();

    public RedstoneBoardNBT() {
    }

    public abstract String getID();

    public abstract void addInformation(ItemStack var1, EntityPlayer var2, List<?> var3, boolean var4);

    public abstract IRedstoneBoard<T> create(NBTTagCompound var1, T var2);

    @SideOnly(Side.CLIENT)
    public abstract void registerIcons(IIconRegister var1);

    @SideOnly(Side.CLIENT)
    public abstract IIcon getIcon(NBTTagCompound var1);

    public void createBoard(NBTTagCompound nbt) {
        nbt.func_74778_a("id", this.getID());
    }

    public int getParameterNumber(NBTTagCompound nbt) {
        return !nbt.func_74764_b("parameters") ? 0 : nbt.func_150295_c("parameters", 10).func_74745_c();
    }

    public float nextFloat(int difficulty) {
        return 1.0F - (float)Math.pow((double)rand.nextFloat(), (double)(1.0F / (float)difficulty));
    }
}