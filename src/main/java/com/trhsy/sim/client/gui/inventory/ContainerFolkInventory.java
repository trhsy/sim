package com.trhsy.sim.client.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerFolkInventory extends Container {
    public boolean updateState;

    public ContainerFolkInventory() {
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }
}
