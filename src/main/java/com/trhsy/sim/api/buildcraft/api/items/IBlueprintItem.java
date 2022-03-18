package com.trhsy.sim.api.buildcraft.api.items;

import net.minecraft.item.ItemStack;

public interface IBlueprintItem extends INamedItem {
    IBlueprintItem.Type getType(ItemStack var1);

    public static enum Type {
        TEMPLATE,
        BLUEPRINT;

        private Type() {
        }
    }
}

