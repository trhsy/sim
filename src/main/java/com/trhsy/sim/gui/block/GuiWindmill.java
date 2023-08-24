package com.trhsy.sim.gui.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.ContainerWindmill;
import com.trhsy.sim.entity.TileEntityWindmill;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.network.client.PacketOpenWindmillGui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;


/**
 * @ClassName GuiWindmill
 * @Description todo
 * @Author TRHSY
 * @Date 2023/8/2012:02
 **/
public class GuiWindmill extends GuiContainer {
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(ModSim.MODID, "textures/gui/windmill.png");
    /**
     * 绑定到此GUI的玩家库存。
     */
    private InventoryPlayer playerInventory;
    /**
     * 实体库存
     */
    private IInventory tileWindmill;

    public GuiWindmill(InventoryPlayer inventory, IInventory chestInventory) {
        super(new ContainerWindmill(inventory, chestInventory));
        try {
            this.playerInventory = inventory;
            this.tileWindmill = chestInventory;
        }catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiWindmill出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public GuiWindmill(PacketOpenWindmillGui message) {
        //Entity entity=FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid);
        super(new ContainerWindmill(new InventoryPlayer((EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid)), new InventoryPlayer((EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid))));
        this.playerInventory = new InventoryPlayer((EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        try {
            //风车
            String s = I18n.format("tile.windmill.name");
            this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
            //物品栏
            this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 106 + 2, 4210752);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("drawGuiContainerForegroundLayer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 绘制Gui容器背景层
     *
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        try {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            //绑定纹理
            this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            //设置宽高
            this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize - 11);
            if (TileEntityWindmill.isBurning(this.tileWindmill)) {
                //获得烧伤左刻度
                //int k = this.getBurnLeftScaled(13);
                //this.drawTexturedModalRect(this.ySize+10,  + 36 + 12 - k, 176, 12 - k, 14, k + 1);
            }
            int l = this.getCookProgressScaled(24);
            this.drawTexturedModalRect(i+79, j+30,176,14, l + 1, 16);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("drawGuiContainerBackgroundLayer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 缩放烹饪进度
     *
     * @param pixels
     * @return
     */
    private int getCookProgressScaled(int pixels) {
        int i = this.tileWindmill.getField(2);//257
        int j = this.tileWindmill.getField(3);//300
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels) {
        int i = this.tileWindmill.getField(1);

        if (i == 0) {
            i = 200;
        }

        return this.tileWindmill.getField(0) * pixels / i;
    }

}
