package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.FarmingBox;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.jobs.Vocation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

/**
 * ========================================
 *
 * @ClassName GuiFarming
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:29
 * ========================================
 **/
public class GuiFarming extends GuiScreen {
    FolkData theFolk = null;
    FarmingBox theFarmingBox = null;
    private int mouseCount = 0;

    public GuiFarming(FarmingBox farmingBox, FolkData folk) {
        this.theFarmingBox = farmingBox;
        this.theFolk = folk;
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        try {
            if (this.theFarmingBox.level == 0) {
                this.theFarmingBox.level = 1;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return;
        }

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, "Done"));
        if (this.theFolk == null) {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, "Hire Farmer"));
        } else {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, "Fire " + this.theFolk.name));
        }

        try {
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 100, this.theFarmingBox.farmType.toString() + " farm"));
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        try {
            GuiButton b;
            switch(this.theFarmingBox.level) {
                case 1:
                    if (this.theFarmingBox.farmType != FarmType.SUGAR && this.theFarmingBox.farmType != FarmType.CACTUS) {
                        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, 140, "Upgrade to Level " + (this.theFarmingBox.level + 1)));
                    } else {
                        this.buttonList.add(b = new GuiButton(3, this.width / 2 - 100, 140, "Fully upgraded"));
                        b.enabled = false;
                    }
                    break;
                case 2:
                    this.buttonList.add(new GuiButton(3, this.width / 2 - 100, 140, "Upgrade to Level " + (this.theFarmingBox.level + 1)));
                    break;
                case 3:
                    this.buttonList.add(b = new GuiButton(3, this.width / 2 - 100, 140, "Fully upgraded"));
                    b.enabled = false;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        super.initGui();
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        try {
            this.drawDefaultBackground();
            if (this.theFarmingBox == null) {
                this.drawCenteredString(this.fontRendererObj, "ERROR WITH FARMING BOX, Place 3 markers, then place Farming box", this.width / 2, 17, 16777215);
                return;
            }

            this.drawCenteredString(this.fontRendererObj, "Level " + this.theFarmingBox.level + " " + this.theFarmingBox.farmType.toString() + " Farm", this.width / 2, 17, 8454016);

            try {
                if (this.theFarmingBox.marker1XYZ == null) {
                    this.drawCenteredString(this.fontRendererObj, "Error: No markers placed - 3 markers are needed to farm an area.", this.width / 2, 27, 16711680);
                }
            } catch (Exception var5) {
                this.drawCenteredString(this.fontRendererObj, "Error: Place markers BEFORE the farming box.", this.width / 2, 27, 16711680);
            }

            if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.SUGAR) {
                this.drawCenteredString(this.fontRendererObj, "Cannot upgrade sugar cane farms above level 1", this.width / 2, 130, 16777215);
            } else if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.CACTUS) {
                this.drawCenteredString(this.fontRendererObj, "Cannot upgrade cactus farms above level 1", this.width / 2, 130, 16777215);
            } else if (this.theFarmingBox.level < 3) {
                if (ModSim.gameMode != GameMode.CREATIVE) {
                    this.drawCenteredString(this.fontRendererObj, "Upgrade will cost " + ModSim.displayMoney(this.getUpgradeCost()) + " credits", this.width / 2, 130, 16777215);
                } else {
                    this.drawCenteredString(this.fontRendererObj, "Upgrade is Free", this.width / 2, 130, 16777215);
                }
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
                if (guibutton.displayString.contentEquals("Hire Farmer")) {
                    GuiEmployFolk ui = new GuiEmployFolk(this.theFarmingBox, Vocation.CROPFARMER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.startsWith("Fire ")) {
                    this.theFolk.selfFire();
                    guibutton.enabled = false;
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else if (guibutton.id == 2) {
                    if (this.theFarmingBox.farmType == FarmType.POTATO) {
                        this.theFarmingBox.farmType = FarmType.PUMPKIN;
                    } else if (this.theFarmingBox.farmType == FarmType.PUMPKIN) {
                        this.theFarmingBox.farmType = FarmType.MELON;
                    } else if (this.theFarmingBox.farmType == FarmType.MELON) {
                        this.theFarmingBox.farmType = FarmType.WHEAT;
                    } else if (this.theFarmingBox.farmType == FarmType.WHEAT) {
                        this.theFarmingBox.farmType = FarmType.CARROT;
                    } else if (this.theFarmingBox.farmType == FarmType.CARROT) {
                        this.theFarmingBox.farmType = FarmType.CUSTOM;
                    } else if (this.theFarmingBox.farmType == FarmType.CUSTOM) {
                        this.theFarmingBox.farmType = FarmType.SUGAR;
                    } else if (this.theFarmingBox.farmType == FarmType.SUGAR) {
                        this.theFarmingBox.farmType = FarmType.CACTUS;
                    } else if (this.theFarmingBox.farmType == FarmType.CACTUS) {
                        this.theFarmingBox.farmType = FarmType.POTATO;
                    }

                    guibutton.displayString = this.theFarmingBox.farmType.toString() + " farm";
                } else if (guibutton.id == 3) {
                    float cash = ModSim.states.credits;
                    if (ModSim.gameMode == GameMode.CREATIVE) {
                        cash = 1000.0F;
                    }

                    if (this.getUpgradeCost() > cash) {
                        guibutton.displayString = "NOT ENOUGH";
                        guibutton.enabled = false;
                    } else {
                        if (this.theFarmingBox.getSizeLength() >= 4 && this.theFarmingBox.getSizeWidth() >= 4) {
                            if (ModSim.gameMode != GameMode.CREATIVE) {
                                GameStates var10000 = ModSim.states;
                                var10000.credits -= this.getUpgradeCost();
                            }

                            ModSim.farmToUpgradeCounter = 0;
                            ModSim.farmToUpgrade = this.theFarmingBox;
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        }

                        guibutton.displayString = "TOO SMALL";
                        guibutton.enabled = false;
                    }
                }

            }
        }
    }

    private Float getUpgradeCost() {
        Float ret = (float)(this.theFarmingBox.getSizeLength() * this.theFarmingBox.getSizeWidth());
        ret = ret / 15.0F;
        return ret;
    }
}

