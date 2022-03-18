package com.trhsy.sim.api.buildcraft.api.items;


import com.trhsy.sim.api.buildcraft.api.core.BlockIndex;
import com.trhsy.sim.api.buildcraft.api.core.IBox;
import com.trhsy.sim.api.buildcraft.api.core.IZone;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public interface IMapLocation extends INamedItem {
    MapLocationType getType(ItemStack var1);

    BlockIndex getPoint(ItemStack var1);

    IBox getBox(ItemStack var1);

    IZone getZone(ItemStack var1);

    List<BlockIndex> getPath(ItemStack var1);

    ForgeDirection getPointSide(ItemStack var1);

    public static enum MapLocationType {
        CLEAN,
        SPOT,
        AREA,
        PATH,
        ZONE;

        private MapLocationType() {
        }
    }
}