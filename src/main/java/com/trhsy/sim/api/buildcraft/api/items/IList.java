package com.trhsy.sim.api.buildcraft.api.items;

import net.minecraft.item.ItemStack;

public interface IList extends INamedItem {
    /** @deprecated */
    @Deprecated
    String getLabel(ItemStack var1);

    boolean matches(ItemStack var1, ItemStack var2);
}
