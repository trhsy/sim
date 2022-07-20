package com.trhsy.sim.common.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerFolkInventory extends Container {
    public boolean updateState;

    public ContainerFolkInventory() {
    }

    @Override
    public boolean func_75145_c(EntityPlayer player) {
        return false;
    }
}
