package com.trhsy.sim.client.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * npc的库存
 */
public class NPCInventory extends Container {
    public boolean updateState;

    public NPCInventory() {
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }
}
