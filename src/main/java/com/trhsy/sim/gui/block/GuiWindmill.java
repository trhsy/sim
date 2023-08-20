package com.trhsy.sim.gui.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.network.client.PacketOpenWindmillGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;


/**
 * @ClassName GuiWindmill
 * @Description todo
 * @Author TRHSY
 * @Date 2023/8/2012:02
 **/
public class GuiWindmill extends GuiContainer {
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(ModSim.MODID, "textures/gui/windmill.png");
    /** The player inventory bound to this GUI. */
    private InventoryPlayer playerInventory;
    public GuiWindmill(PacketOpenWindmillGui message) {
        //Entity entity=FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid);
        super(new ContainerFurnace(new InventoryPlayer((EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid)), new InventoryPlayer((EntityPlayer)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid))));
        this.playerInventory=new InventoryPlayer((EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid));
    }


/*    public void initGui() {
        super.initGui();
    }*/

/*    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawGuiContainerBackgroundLayer(partialTicks, (this.width - this.xSize) / 2, (this.height - this.ySize) / 2);
    }*/

    /**
     * 在背景屏幕上绘制渐变（如果存在）或在背景上绘制平面 background.png
     */
/*
    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent(this));
    }
*/

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        //绑定纹理
        this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
    }

    private int getCookProgressScaled(int pixels) {
//        int i = this.tileFurnace.getField(2);
//        int j = this.tileFurnace.getField(3);
        int j = 1, i = 0;
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //风车
        String s = I18n.format("tile.windmill.name");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
}
