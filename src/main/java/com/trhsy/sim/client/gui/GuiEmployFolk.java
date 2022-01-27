package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.PathBox;
import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.MiningBox;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.GotoMethod;
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

    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 200, this.field_146295_m - 30, "Cancel"));
        this.field_146292_n.add(new GuiButton(1000, this.field_146294_l / 2, this.field_146295_m - 30, "OK"));
        ArrayList folks = FolkData.getFolkUnemployed(false);

        try {
            int x = 10;
            int y = 40;
            int idx = 1;

            for(int f = 0; f < folks.size(); ++f) {
                FolkData folk = (FolkData)folks.get(f);
                String xp = "";
                int ixp = false;
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

                this.field_146292_n.add(new GuiButton(idx, x, y, 110, 20, folk.name + xp));
                ++idx;
                x += 110;
                if (x + 110 > this.field_146294_l) {
                    x = 10;
                    y += 20;
                }

                if (y + 20 > this.field_146295_m - 50) {
                    break;
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }

    public void func_73863_a(int i, int j, float f) {
        this.func_146276_q_();

        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_73732_a(this.field_146289_q, "Choose who you'd like to Employ as a " + this.vocation.toString(), this.field_146294_l / 2, 17, 16777215);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        super.func_73863_a(i, j, f);
    }

    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            } else {
                if (guibutton.field_146127_k > 0 && guibutton.field_146127_k < 1000 && this.selectedFolks.size() < this.maxEmployees) {
                    this.selectedFolks.add(guibutton);
                    guibutton.field_146124_l = false;
                }

                if (guibutton.field_146127_k == 1000) {
                    if (ModSimukraft.states.credits <= 0.0F && ModSimukraft.gameMode != GameMode.CREATIVE) {
                        ModSimukraft.sendChat("You need some Sim-u-Credits to employ folks.");
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                        return;
                    }

                    if (ModSimukraft.gameMode == GameMode.CREATIVE && this.vocation == Vocation.MERCHANT) {
                        ModSimukraft.sendChat("Builder's merchant cannot be hired in creative mode");
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                        return;
                    }

                    ArrayList<FolkData> efolks = new ArrayList();

                    for(int w = 0; w < this.selectedFolks.size(); ++w) {
                        GuiButton button = (GuiButton)this.selectedFolks.get(w);
                        String folkname = button.field_146126_j;
                        if (folkname.contains("(")) {
                            folkname = button.field_146126_j.substring(0, button.field_146126_j.indexOf(" (")).trim();
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

        this.field_146297_k.field_71462_r = null;
        GuiBuildingConstructor ui;
        if (this.vocation == Vocation.BUILDER) {
            ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
            this.field_146297_k.displayGuiScreen(ui);
        } else if (this.vocation == Vocation.TERRAFORMER) {
            ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
            this.field_146297_k.displayGuiScreen(ui);
        } else if (this.vocation == Vocation.MINER) {
            GuiMining ui = new GuiMining(this.miningBox, efolks);
            this.field_146297_k.displayGuiScreen(ui);
        } else if (this.vocation == Vocation.CROPFARMER) {
            GuiFarming ui = new GuiFarming(this.farmingBox, (FolkData)efolks.get(0));
            this.field_146297_k.displayGuiScreen(ui);
        } else if (this.vocation == Vocation.PATHBUILDER) {
            GuiPathBox ui = new GuiPathBox(this.pathBox, efolks);
            this.field_146297_k.displayGuiScreen(ui);
        } else {
            GuiControlBox ui = new GuiControlBox(this.controlBoxLocation, (FolkData)efolks.get(0));
            this.field_146297_k.displayGuiScreen(ui);
        }

    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_73876_c() {
    }
}
