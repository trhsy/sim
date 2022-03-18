package com.trhsy.sim.api.buildcraft.api.robots;

import net.minecraft.world.World;

public interface IRobotRegistryProvider {
    IRobotRegistry getRegistry(World var1);
}
