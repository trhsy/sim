package com.trhsy.buildcraft.api.core;/**
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && o.getClass() == StackKey.class) {
            StackKey k = (StackKey)o;
            if (!(this.stack == null ^ k.stack == null) && !(this.fluidStack == null ^ k.fluidStack == null)) {
                if (this.stack != null && (this.stack.func_77973_b() != k.stack.func_77973_b() || this.stack.func_77981_g() && this.stack.func_77960_j() != k.stack.func_77960_j() || !this.objectsEqual(this.stack.func_77978_p(), k.stack.func_77978_p()))) {
                    return false;
                } else {
                    return this.fluidStack == null || this.fluidStack.fluidID == k.fluidStack.fluidID && this.fluidStack.amount == k.fluidStack.amount && this.objectsEqual(this.fluidStack.tag, k.fluidStack.tag);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 7;
        if (this.stack != null) {
            result = 31 * result + this.stack.func_77973_b().hashCode();
            result = 31 * result + this.stack.func_77960_j();
            result = 31 * result + this.objectHashCode(this.stack.func_77978_p());
        }

        result = 31 * result + 7;
        if (this.fluidStack != null) {
            result = 31 * result + this.fluidStack.fluidID;
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
        return new StackKey(this.stack != null ? this.stack.func_77946_l() : null, this.fluidStack != null ? this.fluidStack.copy() : null);
    }
}
