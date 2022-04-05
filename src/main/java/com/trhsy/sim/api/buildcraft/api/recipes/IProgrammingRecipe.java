package com.trhsy.sim.api.buildcraft.api.recipes;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IProgrammingRecipe {
    String getId();

    List<ItemStack> getOptions(int var1, int var2);

    int getEnergyCost(ItemStack var1);

    boolean canCraft(ItemStack var1);

    ItemStack craft(ItemStack var1, ItemStack var2);
}
