package com.trhsy.sim.api.buildcraft.api.blocks;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IColorRemovable {
    boolean removeColorFromBlock(World var1, int var2, int var3, int var4, ForgeDirection var5);
}
