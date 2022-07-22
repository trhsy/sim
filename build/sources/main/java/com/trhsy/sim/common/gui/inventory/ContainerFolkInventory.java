package com.trhsy.sim.common.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * npc的库存
 */
public class ContainerFolkInventory extends Container {
    public boolean updateState;

    public ContainerFolkInventory() {
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }
}
