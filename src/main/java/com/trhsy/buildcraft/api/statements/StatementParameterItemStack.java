package com.trhsy.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName StatementParameterItemStack
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:55
 * ========================================
 **/
public class StatementParameterItemStack implements IStatementParameter {
    protected ItemStack stack;

    public StatementParameterItemStack() {
    }

    @Override
    public IIcon getIcon() {
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        return this.stack;
    }

    @Override
    public void onClick(IStatementContainer source, IStatement stmt, ItemStack stack, StatementMouseClick mouse) {
        if (stack != null) {
            this.stack = stack.copy();
            this.stack.stackSize = 1;
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        if (this.stack != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            this.stack.writeToNBT(tagCompound);
            compound.setTag("stack", tagCompound);
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("stack"));
    }

    public boolean equals(Object object) {
        if (!(object instanceof StatementParameterItemStack)) {
            return false;
        } else {
            StatementParameterItemStack param = (StatementParameterItemStack)object;
            return ItemStack.areItemStacksEqual(this.stack, param.stack) && ItemStack.areItemStackTagsEqual(this.stack, param.stack);
        }
    }

    @Override
    public String getDescription() {
        return this.stack != null ? this.stack.getDisplayName() : "";
    }

    @Override
    public String getUniqueTag() {
        return "buildcraft:stack";
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
    }

    @Override
    public IStatementParameter rotateLeft() {
        return this;
    }
}