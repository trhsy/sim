package com.trhsy.sim.api.buildcraft.api.tiles;


import com.trhsy.sim.api.buildcraft.api.core.IAreaProvider;

public interface ITileAreaProvider extends IAreaProvider {
    boolean isValidFromLocation(int var1, int var2, int var3);
}
