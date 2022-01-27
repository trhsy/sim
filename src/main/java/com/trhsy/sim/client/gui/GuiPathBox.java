package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.PathBox;
import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
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

    public boolean func_73868_f() {
        return false;
    }

    public void func_73876_c() {
        if (this.tfSize != null) {
            this.tfSize.func_146178_a();
        }

    }

    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, "Done"));
        if (this.thePathBox != null) {
            if (this.thePathBox.marker1XYZ != null) {
                if (this.page == 0) {
                    if (this.theWorkers != null && this.theWorkers.size() != 0) {
                        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, "Fire " + ((FolkData)this.theWorkers.get(0)).name));
                        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 60, "Choose path type"));
                    } else {
                        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, "Hire Path Builder"));
                    }
                } else if (this.page == 1) {
                    this.field_146292_n.add(new GuiButton(1, 10, 20, "Wooden Bridge"));
                }

            }
        }
    }

    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, "Path Constructor", this.field_146294_l / 2, 17, 16777215);

            try {
                if (this.thePathBox.marker1XYZ == null) {
                    this.func_73732_a(this.field_146289_q, "Error: No marker placed - place a marker down first", this.field_146294_l / 2, 27, 16711680);
                }
            } catch (Exception var5) {
                this.func_73732_a(this.field_146289_q, "Error: No marker placed - place a marker down first", this.field_146294_l / 2, 27, 16711680);
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
                if (guibutton.field_146126_j.contentEquals("Hire Path Builder")) {
                    GuiEmployFolk ui = new GuiEmployFolk(this.thePathBox, Vocation.PATHBUILDER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.startsWith("Fire ")) {
                    for(int i = 0; i < this.theWorkers.size(); ++i) {
                        FolkData folk = (FolkData)this.theWorkers.get(i);
                        folk.selfFire();
                    }

                    guibutton.field_146124_l = false;
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                } else if (guibutton.field_146126_j.contentEquals("Choose path type")) {
                    this.page = 1;
                    this.func_73866_w_();
                } else if (this.page == 1) {
                    this.thePathBox.pathType = guibutton.field_146126_j;
                    ModSimukraft.sendChat("Path constructor set to " + guibutton.field_146126_j);
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                }

            }
        }
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
    }

    public void func_73864_a(int i, int j, int k) {
        super.func_73864_a(i, j, k);
    }
}

