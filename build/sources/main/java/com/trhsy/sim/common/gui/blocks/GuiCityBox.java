package com.trhsy.sim.common.gui.blocks;

import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.gui.other.GuiBeamPlayerTo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @ClassName GuiCityBox
 * @Description todo
 * @Author Tian
 * @Date 2022/4/414:13
 **/
public class GuiCityBox extends GuiScreen {
    private int mouseCount = 0;
    public V3 location;
    public Building theBuilding = null;
    public FolkData theFolk = null;
    private EntityPlayer playerWhoClickedIt = null;
    GuiCityBoxTaxes taxesGui = null;

    public GuiCityBox(V3 location, EntityPlayer thePlayer) {
        this.location = location.clone();
        Building.loadAllBuildings();
        this.theBuilding = Building.getBuilding(location);
        this.playerWhoClickedIt = thePlayer;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, "完成"));
        this.buttonList.add(new GuiButton(30, this.width - 110, this.height - 30, 100, 20, "把我传送到..."));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 - 70, 100, 20, "税和租金"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 50, this.height / 2 - 30, 100, 20, "工作时间"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height / 2 + 10, 100, 20, "信息"));
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, "城市控制面板", this.width / 2, 17, 16777215);
            super.drawScreen(i, j, f);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                if (guibutton.id == 30) {
                    GuiScreen ui = new GuiBeamPlayerTo(this.playerWhoClickedIt);
                    this.mc.displayGuiScreen((GuiScreen)null);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("税和租金")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.mc.displayGuiScreen(this.taxesGui);
                } else if (guibutton.displayString.contentEquals("租金")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.mc.displayGuiScreen(this.taxesGui);
                } else if (guibutton.displayString.contentEquals("工作时间")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.mc.displayGuiScreen(this.taxesGui);
                } else if (guibutton.displayString.contentEquals("信息")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.mc.displayGuiScreen(this.taxesGui);
                }

            }
        }
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.mc.setIngameFocus();
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
}
