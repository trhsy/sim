package com.trhsy.sim.api.buildcraft.api.core;


import java.util.List;

public interface IPathProvider {
    List<BlockIndex> getPath();

    void removeFromWorld();
}
