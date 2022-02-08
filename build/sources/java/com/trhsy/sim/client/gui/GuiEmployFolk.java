package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.PathBox;
import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FarmingBox;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.MiningBox;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.jobs.Vocation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiEmployFolk
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:28
 * ========================================
 **/
public class GuiEmployFolk extends GuiScreen {
    FolkData theFolk;
    V3 controlBoxLocation;
    String buildDirection = "";
    MiningBox miningBox;
    FarmingBox farmingBox;
    PathBox pathBox;
    Vocation vocation;
    private int mouseCount = 0;
    private ArrayList<GuiButton> selectedFolks = new ArrayList();
    private int maxEmployees = 1;

    public GuiEmployFolk(V3 controlBoxLocation, String dir, Vocation vocation) {
        this.controlBoxLocation = controlBoxLocation;
        this.buildDirection = dir;
        this.vocation = vocation;
        if (this.vocation == Vocation.BUILDER) {
            this.maxEmployees = 1;
        }

    }

    public GuiEmployFolk(MiningBox b, Vocation v) {
        try {
            this.controlBoxLocation = b.location;
            this.vocation = v;
            this.miningBox = b;
            this.maxEmployees = 1;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public GuiEmployFolk(FarmingBox b, Vocation v) {
        try {
            this.controlBoxLocation = b.location;
            this.vocation = v;
            this.farmingBox = b;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public GuiEmployFolk(PathBox thePathBox, Vocation v) {
        try {
            this.controlBoxLocation = thePathBox.location;
            this.vocation = v;
            this.pathBox = thePathBox;
            this.maxEmployees = 1;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 200, this.height - 30, "Cancel"));
        this.buttonList.add(new GuiButton(1000, this.width / 2, this.height - 30, "OK"));
        ArrayList folks = FolkData.getFolkUnemployed(false);

        try {
            int x = 10;
            int y = 40;
            int idx = 1;

            for(int f = 0; f < folks.size(); ++f) {
                FolkData folk = (FolkData)folks.get(f);
                String xp = "";
                //int ixp = false;
                int ixp;
                if (this.vocation == Vocation.BUILDER) {
                    ixp = (int)Math.floor((double)folk.levelBuilder);
                    xp = " (" + ixp + ")";
                } else if (this.vocation == Vocation.MINER) {
                    ixp = (int)Math.floor((double)folk.levelMiner);
                    xp = " (" + ixp + ")";
                } else if (this.vocation == Vocation.SOLDIER) {
                    ixp = (int)Math.floor((double)folk.levelSoldier);
                    xp = " (" + ixp + ")";
                }

                this.buttonList.add(new GuiButton(idx, x, y, 110, 20, folk.name + xp));
                ++idx;
                x += 110;
                if (x + 110 > this.width) {
                    x = 10;
                    y += 20;
                }

                if (y + 20 > this.height - 50) {
                    break;
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }

    @Override
    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();

        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawCenteredString(this.fontRendererObj, "Choose who you'd like to Employ as a " + this.vocation.toString(), this.width / 2, 17, 16777215);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        super.drawScreen(i, j, f);
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                if (guibutton.id > 0 && guibutton.id < 1000 && this.selectedFolks.size() < this.maxEmployees) {
                    this.selectedFolks.add(guibutton);
                    guibutton.enabled = false;
                }

                if (guibutton.id == 1000) {
                    if (ModSimukraft.states.credits <= 0.0F && ModSimukraft.gameMode != GameMode.CREATIVE) {
                        ModSimukraft.sendChat("You need some Sim-u-Credits to employ folks.");
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                        return;
                    }

                    if (ModSimukraft.gameMode == GameMode.CREATIVE && this.vocation == Vocation.MERCHANT) {
                        ModSimukraft.sendChat("Builder's merchant cannot be hired in creative mode");
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                        return;
                    }

                    ArrayList<FolkData> efolks = new ArrayList();

                    for(int w = 0; w < this.selectedFolks.size(); ++w) {
                        GuiButton button = (GuiButton)this.selectedFolks.get(w);
                        String folkname = button.displayString;
                        if (folkname.contains("(")) {
                            folkname = button.displayString.substring(0, button.displayString.indexOf(" (")).trim();
                        }

                        FolkData f = FolkData.getFolkByName(folkname);
                        f.statusText = "Going to my new job...";
                        efolks.add(f);
                        this.hireFolks(efolks);
                    }
                }

            }
        }
    }

    public void hireFolks(ArrayList<FolkData> efolks) {
        for(int i = 0; i < efolks.size(); ++i) {
            FolkData efolk = (FolkData)efolks.get(i);
            efolk.employedAt = this.controlBoxLocation;
            efolk.setTheirJob(this.vocation);
            if (ModSimukraft.isDayTime()) {
                efolk.gotoXYZ(efolk.employedAt, (GotoMethod)null);
            }
        }

        this.mc.currentScreen = null;
        GuiBuildingConstructor ui;
        if (this.vocation == Vocation.BUILDER) {
            ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
            this.mc.displayGuiScreen(ui);
        } else if (this.vocation == Vocation.TERRAFORMER) {
            ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
            this.mc.displayGuiScreen(ui);
        } else if (this.vocation == Vocation.MINER) {
            GuiMining uiGuiMining = new GuiMining(this.miningBox, efolks);
            this.mc.displayGuiScreen(uiGuiMining);
        } else if (this.vocation == Vocation.CROPFARMER) {
            GuiFarming uiFarming = new GuiFarming(this.farmingBox, (FolkData)efolks.get(0));
            this.mc.displayGuiScreen(uiFarming);
        } else if (this.vocation == Vocation.PATHBUILDER) {
            GuiPathBox uiGuiPathBox = new GuiPathBox(this.pathBox, efolks);
            this.mc.displayGuiScreen(uiGuiPathBox);
        } else {
            GuiControlBox uiGuiControlBox = new GuiControlBox(this.controlBoxLocation, (FolkData)efolks.get(0));
            this.mc.displayGuiScreen(uiGuiControlBox);
        }

    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        // TODO document why this method is empty
    }
}
