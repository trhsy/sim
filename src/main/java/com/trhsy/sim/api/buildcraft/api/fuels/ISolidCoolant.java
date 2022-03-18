package com.trhsy.sim.api.buildcraft.api.fuels;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * ========================================
 *
 * @ClassName ISolidCoolant
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:32
 * ========================================
 **/
public interface ISolidCoolant {
    FluidStack getFluidFromSolidCoolant(ItemStack var1);
}
