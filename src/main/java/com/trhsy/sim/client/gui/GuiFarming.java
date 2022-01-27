package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.enums.FarmType;
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

    public boolean func_73868_f() {
        return false;
    }

    public void func_73866_w_() {
        try {
            if (this.theFarmingBox.level == 0) {
                this.theFarmingBox.level = 1;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return;
        }

        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, "Done"));
        if (this.theFolk == null) {
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, "Hire Farmer"));
        } else {
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, "Fire " + this.theFolk.name));
        }

        try {
            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 100, this.theFarmingBox.farmType.toString() + " farm"));
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        try {
            GuiButton b;
            switch(this.theFarmingBox.level) {
                case 1:
                    if (this.theFarmingBox.farmType != FarmType.SUGAR && this.theFarmingBox.farmType != FarmType.CACTUS) {
                        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, 140, "Upgrade to Level " + (this.theFarmingBox.level + 1)));
                    } else {
                        this.field_146292_n.add(b = new GuiButton(3, this.field_146294_l / 2 - 100, 140, "Fully upgraded"));
                        b.field_146124_l = false;
                    }
                    break;
                case 2:
                    this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, 140, "Upgrade to Level " + (this.theFarmingBox.level + 1)));
                    break;
                case 3:
                    this.field_146292_n.add(b = new GuiButton(3, this.field_146294_l / 2 - 100, 140, "Fully upgraded"));
                    b.field_146124_l = false;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        super.func_73866_w_();
    }

    public void func_73863_a(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        try {
            this.func_146276_q_();
            if (this.theFarmingBox == null) {
                this.func_73732_a(this.field_146289_q, "ERROR WITH FARMING BOX, Place 3 markers, then place Farming box", this.field_146294_l / 2, 17, 16777215);
                return;
            }

            this.func_73732_a(this.field_146289_q, "Level " + this.theFarmingBox.level + " " + this.theFarmingBox.farmType.toString() + " Farm", this.field_146294_l / 2, 17, 8454016);

            try {
                if (this.theFarmingBox.marker1XYZ == null) {
                    this.func_73732_a(this.field_146289_q, "Error: No markers placed - 3 markers are needed to farm an area.", this.field_146294_l / 2, 27, 16711680);
                }
            } catch (Exception var5) {
                this.func_73732_a(this.field_146289_q, "Error: Place markers BEFORE the farming box.", this.field_146294_l / 2, 27, 16711680);
            }

            if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.SUGAR) {
                this.func_73732_a(this.field_146289_q, "Cannot upgrade sugar cane farms above level 1", this.field_146294_l / 2, 130, 16777215);
            } else if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.CACTUS) {
                this.func_73732_a(this.field_146289_q, "Cannot upgrade cactus farms above level 1", this.field_146294_l / 2, 130, 16777215);
            } else if (this.theFarmingBox.level < 3) {
                if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                    this.func_73732_a(this.field_146289_q, "Upgrade will cost " + ModSimukraft.displayMoney(this.getUpgradeCost()) + " credits", this.field_146294_l / 2, 130, 16777215);
                } else {
                    this.func_73732_a(this.field_146289_q, "Upgrade is Free", this.field_146294_l / 2, 130, 16777215);
                }
            }

            super.func_73863_a(i, j, f);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            } else {
                if (guibutton.field_146126_j.contentEquals("Hire Farmer")) {
                    GuiEmployFolk ui = new GuiEmployFolk(this.theFarmingBox, Vocation.CROPFARMER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.startsWith("Fire ")) {
                    this.theFolk.selfFire();
                    guibutton.field_146124_l = false;
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                } else if (guibutton.field_146127_k == 2) {
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

                    guibutton.field_146126_j = this.theFarmingBox.farmType.toString() + " farm";
                } else if (guibutton.field_146127_k == 3) {
                    float cash = ModSimukraft.states.credits;
                    if (ModSimukraft.gameMode == GameMode.CREATIVE) {
                        cash = 1000.0F;
                    }

                    if (this.getUpgradeCost() > cash) {
                        guibutton.field_146126_j = "NOT ENOUGH";
                        guibutton.field_146124_l = false;
                    } else {
                        if (this.theFarmingBox.getSizeLength() >= 4 && this.theFarmingBox.getSizeWidth() >= 4) {
                            if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                                GameStates var10000 = ModSimukraft.states;
                                var10000.credits -= this.getUpgradeCost();
                            }

                            ModSimukraft.farmToUpgradeCounter = 0;
                            ModSimukraft.farmToUpgrade = this.theFarmingBox;
                            this.field_146297_k.field_71462_r = null;
                            this.field_146297_k.func_71381_h();
                            return;
                        }

                        guibutton.field_146126_j = "TOO SMALL";
                        guibutton.field_146124_l = false;
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

