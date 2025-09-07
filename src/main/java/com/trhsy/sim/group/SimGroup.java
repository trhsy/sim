package com.trhsy.sim.group;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.BlocksLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class SimGroup extends ItemGroup {
    public SimGroup() {
        super(ModSim.MODID);
    }

    public SimGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlocksLoader.CONSTRUCTOR_BOX.asItem());
    }
}
