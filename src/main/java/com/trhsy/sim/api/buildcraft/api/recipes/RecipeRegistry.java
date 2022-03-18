package com.trhsy.sim.api.buildcraft.api.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class RecipeRegistry {
    public static IRecipeManager<ItemStack> assemblyTable;
    public static IRecipeManager<ItemStack> integrationTable;
    public static IRecipeManager<FluidStack> refinery;
    public static IProgrammingRecipeManager programmingTable;

    private RecipeRegistry() {
    }
}
