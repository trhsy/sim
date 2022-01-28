package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

/**
 * ========================================
 *
 * @ClassName GuiShowEmployees
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:36
 * ========================================
 **/
public class GuiShowEmployees extends GuiScreen {
    public boolean running = true;
    private int mouseCount = 0;

    public boolean doesGuiPauseGame() {
        return true;
    }

    public GuiRunMod() {
    }

    public void initGui() {
        System.out.println("Initializing GUI");
        this.buttonList.add(new GuiButton(0, this.width / 2 - 75, 40, "Do NOT run Sim-U-Kraft"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 75, 90, "Normal Mode"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 75, 140, "Creative Mode"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 75, 190, "Hardcore Mode"));
    }

    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, "Please choose the game mode for Sim-U-Kraft", this.width / 2, 20, 16777215);
            this.drawCenteredString(this.fontRendererObj, "This mode switches off Sim-U-kraft for this world", this.width / 2, 60, 16776960);
            this.drawCenteredString(this.fontRendererObj, "Ideal for beginners and experts. Not too challenging.", this.width / 2, 110, 16776960);
            this.drawCenteredString(this.fontRendererObj, "No money needed, everything free, no blocks required, be creative!", this.width / 2, 160, 16776960);
            this.drawCenteredString(this.fontRendererObj, "Builders require ALL blocks, harder gameplay", this.width / 2, 210, 16776960);
        } catch (Exception var5) {
            System.out.println("Caught Exception while drawing strings/screen");
        }

        super.drawScreen(i, j, f);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 0) {
            ModSimukraft.states.gameModeNumber = 10;
            System.out.println("Turning off Sim-U-Kraft Reloaded");
        } else if (guibutton.id == 1) {
            ModSimukraft.states.gameModeNumber = 0;
            System.out.println("Playing Sim-U-Kraft Reloaded in normal mode");
            FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        } else if (guibutton.id == 2) {
            ModSimukraft.states.gameModeNumber = 1;
        } else if (guibutton.id == 3) {
            ModSimukraft.states.gameModeNumber = 2;
        }

        ModSimukraft.states.saveStates();
        this.running = false;
        this.mc.currentScreen = null;
        this.mc.setIngameFocus();
    }
}

