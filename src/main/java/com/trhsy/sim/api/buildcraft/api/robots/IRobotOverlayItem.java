package com.trhsy.sim.api.buildcraft.api.robots;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

public interface IRobotOverlayItem {
    boolean isValidRobotOverlay(ItemStack var1);

    @SideOnly(Side.CLIENT)
    void renderRobotOverlay(ItemStack var1, TextureManager var2);
}
