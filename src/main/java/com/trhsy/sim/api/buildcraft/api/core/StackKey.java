package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * ========================================
 *
 * @ClassName StackKey
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:13
 * ========================================
 **/
public final class StackKey {
    public final ItemStack stack;
    public final FluidStack fluidStack;

    public StackKey(FluidStack fluidStack) {
        this((ItemStack)null, fluidStack);
    }

    public StackKey(ItemStack stack) {
        this(stack, (FluidStack)null);
    }

    public StackKey(ItemStack stack, FluidStack fluidStack) {
        this.stack = stack;
        this.fluidStack = fluidStack;
    }

    public static StackKey stack(Item item, int amount, int damage) {
        return new StackKey(new ItemStack(item, amount, damage));
    }

    public static StackKey stack(Block block, int amount, int damage) {
        return new StackKey(new ItemStack(block, amount, damage));
    }

    public static StackKey stack(Item item) {
        return new StackKey(new ItemStack(item, 1, 0));
    }

    public static StackKey stack(Block block) {
        return new StackKey(new ItemStack(block, 1, 0));
    }

    public static StackKey stack(ItemStack itemStack) {
        return new StackKey(itemStack);
    }

    public static StackKey fluid(Fluid fluid, int amount) {
        return new StackKey(new FluidStack(fluid, amount));
    }

    public static StackKey fluid(Fluid fluid) {
        return new StackKey(new FluidStack(fluid, 1000));
    }

    public static StackKey fluid(FluidStack fluidStack) {
        return new StackKey(fluidStack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && o.getClass() == StackKey.class) {
            StackKey k = (StackKey)o;
            if (!(this.stack == null ^ k.stack == null) && !(this.fluidStack == null ^ k.fluidStack == null)) {
                if (this.stack != null && (this.stack.getItem() != k.stack.getItem() || this.stack.getHasSubtypes() && this.stack.getItemDamage() != k.stack.getItemDamage() || !this.objectsEqual(this.stack.getTagCompound(), k.stack.getTagCompound()))) {
                    return false;
                } else {
                    return this.fluidStack == null || this.fluidStack.getFluid().getID() == k.fluidStack.getFluid().getID() && this.fluidStack.amount == k.fluidStack.amount && this.objectsEqual(this.fluidStack.tag, k.fluidStack.tag);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 7;
        if (this.stack != null) {
            result = 31 * result + this.stack.getItem().hashCode();
            result = 31 * result + this.stack.getItemDamage();
            result = 31 * result + this.objectHashCode(this.stack.getTagCompound());
        }

        result = 31 * result + 7;
        if (this.fluidStack != null) {
            result = 31 * result + this.fluidStack.getFluid().getID();
            result = 31 * result + this.fluidStack.amount;
            result = 31 * result + this.objectHashCode(this.fluidStack.tag);
        }

        return result;
    }

    private boolean objectsEqual(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        } else {
            return o1 != null && o2 != null ? o1.equals(o2) : false;
        }
    }

    private int objectHashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public StackKey copy() {
        return new StackKey(this.stack != null ? this.stack.copy() : null, this.fluidStack != null ? this.fluidStack.copy() : null);
    }
}
