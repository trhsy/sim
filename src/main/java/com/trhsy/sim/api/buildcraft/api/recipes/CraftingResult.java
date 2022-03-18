package com.trhsy.sim.api.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName CraftingResult
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:42
 * ========================================
 **/
public class CraftingResult<T> {
    public T crafted = null;
    public ArrayList<ItemStack> usedItems = new ArrayList();
    public ArrayList<FluidStack> usedFluids = new ArrayList();
    public int energyCost = 0;
    public long craftingTime = 0L;
    public IFlexibleRecipe<T> recipe;

    public CraftingResult() {
    }
}