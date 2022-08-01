package com.trhsy.sim.common.gui.blocks;

import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @ClassName GuiCityBoxTaxes
 * @Description todo 城市箱税
 * @Author Tian
 * @Date 2022/4/414:14
 **/
public class GuiCityBoxTaxes extends GuiScreen {
    private int mouseCount = 0;
    private EntityPlayer playerWhoClickedIt = null;
    private GuiCityBox cityBoxGui;
    private int guiID = 0;
    int taxPercentage;

    public GuiCityBoxTaxes(V3 location, EntityPlayer thePlayer, GuiCityBox gui) {
        try {
            this.playerWhoClickedIt = thePlayer;
            this.cityBoxGui = gui;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiCityBoxTaxes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, "返回"));
        this.buttonList.add(new GuiButton(1, this.width / 2, this.height / 2 - 40, 60, 20, String.valueOf(this.taxPercentage)));
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, "税", this.width / 2, 17, 16777215);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            //var5.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = this.cityBoxGui;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUICITYBOXTAXES-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.setIngameFocus();
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
}

