package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.entity.PathBox;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.jobs.Vocation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiPathBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:35
 * ========================================
 **/
public class GuiPathBox extends GuiScreen {
    ArrayList<FolkData> theWorkers = new ArrayList();
    PathBox thePathBox = null;
    private GuiTextField tfSize;
    private int mouseCount = 0;
    private int page = 0;

    public GuiPathBox(PathBox pathBlock, ArrayList<FolkData> folks) {
        this.thePathBox = pathBlock;
        this.theWorkers = folks;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        if (this.tfSize != null) {
            this.tfSize.updateCursorCounter();
        }

    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, "Done"));
        if (this.thePathBox != null) {
            if (this.thePathBox.marker1XYZ != null) {
                if (this.page == 0) {
                    if (this.theWorkers != null && this.theWorkers.size() != 0) {
                        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, "Fire " + ((FolkData)this.theWorkers.get(0)).name));
                        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 60, "Choose path type"));
                    } else {
                        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, "Hire Path Builder"));
                    }
                } else if (this.page == 1) {
                    this.buttonList.add(new GuiButton(1, 10, 20, "Wooden Bridge"));
                }

            }
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, "Path Constructor", this.width / 2, 17, 16777215);

            try {
                if (this.thePathBox.marker1XYZ == null) {
                    this.drawCenteredString(this.fontRendererObj, "Error: No marker placed - place a marker down first", this.width / 2, 27, 16711680);
                }
            } catch (Exception var5) {
                this.drawCenteredString(this.fontRendererObj, "Error: No marker placed - place a marker down first", this.width / 2, 27, 16711680);
            }

            super.drawScreen(i, j, f);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                if (guibutton.displayString.contentEquals("Hire Path Builder")) {
                    GuiEmployFolk ui = new GuiEmployFolk(this.thePathBox, Vocation.PATHBUILDER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.startsWith("Fire ")) {
                    for(int i = 0; i < this.theWorkers.size(); ++i) {
                        FolkData folk = (FolkData)this.theWorkers.get(i);
                        folk.selfFire();
                    }

                    guibutton.enabled = false;
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else if (guibutton.displayString.contentEquals("Choose path type")) {
                    this.page = 1;
                    this.initGui();
                } else if (this.page == 1) {
                    this.thePathBox.pathType = guibutton.displayString;
                    ModSim.sendChat("Path constructor set to " + guibutton.displayString);
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                }

            }
        }
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }
}

