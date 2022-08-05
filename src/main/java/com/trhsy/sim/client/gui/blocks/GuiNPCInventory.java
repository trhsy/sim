package com.trhsy.sim.client.gui.blocks;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.gui.inventory.NPCInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiNPCInventory extends GuiContainer {
    private static final String TEXTURE_PATH = ModSim.MODID + ":" + "textures/gui/container/gui_metal_furnace.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
    protected NPCInventory inventory;

    public GuiNPCInventory(NPCInventory inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 176;
        this.ySize = 156;
        this.inventory = inventorySlotsIn;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
    }
}
