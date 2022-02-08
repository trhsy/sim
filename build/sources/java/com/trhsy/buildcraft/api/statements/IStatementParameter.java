package com.trhsy.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName IStatementParameter
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:52
 * ========================================
 **/
public interface IStatementParameter {
    String getUniqueTag();

    @SideOnly(Side.CLIENT)
    IIcon getIcon();

    ItemStack getItemStack();

    @SideOnly(Side.CLIENT)
    void registerIcons(IIconRegister var1);

    String getDescription();

    void onClick(IStatementContainer var1, IStatement var2, ItemStack var3, StatementMouseClick var4);

    void readFromNBT(NBTTagCompound var1);

    void writeToNBT(NBTTagCompound var1);

    IStatementParameter rotateLeft();
}
