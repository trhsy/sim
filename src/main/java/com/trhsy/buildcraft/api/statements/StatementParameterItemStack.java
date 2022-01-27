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

    public IIcon getIcon() {
        return null;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public void onClick(IStatementContainer source, IStatement stmt, ItemStack stack, StatementMouseClick mouse) {
        if (stack != null) {
            this.stack = stack.func_77946_l();
            this.stack.field_77994_a = 1;
        }

    }

    public void writeToNBT(NBTTagCompound compound) {
        if (this.stack != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            this.stack.func_77955_b(tagCompound);
            compound.func_74782_a("stack", tagCompound);
        }

    }

    public void readFromNBT(NBTTagCompound compound) {
        this.stack = ItemStack.func_77949_a(compound.func_74775_l("stack"));
    }

    public boolean equals(Object object) {
        if (!(object instanceof StatementParameterItemStack)) {
            return false;
        } else {
            StatementParameterItemStack param = (StatementParameterItemStack)object;
            return ItemStack.func_77989_b(this.stack, param.stack) && ItemStack.func_77970_a(this.stack, param.stack);
        }
    }

    public String getDescription() {
        return this.stack != null ? this.stack.func_82833_r() : "";
    }

    public String getUniqueTag() {
        return "buildcraft:stack";
    }

    public void registerIcons(IIconRegister iconRegister) {
    }

    public IStatementParameter rotateLeft() {
        return this;
    }
}