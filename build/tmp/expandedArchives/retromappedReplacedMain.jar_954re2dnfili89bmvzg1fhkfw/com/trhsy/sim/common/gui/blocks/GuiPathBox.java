package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.functionality.PathBox;
import com.trhsy.sim.common.gui.folk.GuiEmployFolk;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiPathBox
 * @Description todo PathBox
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
        try {
            this.thePathBox = pathBlock;
            this.theWorkers = folks;
        } catch (Exception e) {
            ModSimReloaded.log.error("GuiPathBox出错了：" + e.getMessage());
        }

    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73876_c() {
        try {
            if (this.tfSize != null) {
                this.tfSize.func_146178_a();
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("updateScreen出错了：" + e.getMessage());
        }
    }

    @Override
    public void func_73866_w_() {
        try {
            this.field_146292_n.clear();
            this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, I18n.func_135052_a("container.sim.sim_gui_BC_Done")));
            if (this.thePathBox != null) {
                if (this.thePathBox.marker1XYZ != null) {
                    if (this.page == 0) {
                        if (this.theWorkers != null && this.theWorkers.size() != 0) {
                            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, I18n.func_135052_a("container.sim.Fire") + ((FolkData) this.theWorkers.get(0)).name));
                            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 60, I18n.func_135052_a("container.sim.PathBox1")));
                        } else {
                            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, I18n.func_135052_a("container.sim.Hire24")));
                        }
                    } else if (this.page == 1) {
                        this.field_146292_n.add(new GuiButton(1, 10, 20, I18n.func_135052_a("container.sim.PathBox3")));
                    }

                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("initGui出错了：" + e.getMessage());
        }
    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.PathBox4"), this.field_146294_l / 2, 17, 16777215);

            try {
                if (this.thePathBox.marker1XYZ == null) {
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.PathBox5"), this.field_146294_l / 2, 27, 16711680);
                }
            } catch (Exception var5) {
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.PathBox6"), this.field_146294_l / 2, 27, 16711680);
            }

            super.func_73863_a(i, j, f);
        } catch (Exception e) {
            ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage());
            //var6.printStackTrace();
        }

    }

    @Override
    public void func_146284_a(GuiButton guibutton) {
        try {
            if (guibutton.field_146124_l) {
                if (guibutton.field_146127_k == 0) {
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                } else {
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire24"))) {
                        GuiEmployFolk ui = new GuiEmployFolk(this.thePathBox, Vocation.PATHBUILDER);
                        this.field_146297_k.func_147108_a(ui);
                    } else if (guibutton.field_146126_j.startsWith(I18n.func_135052_a("container.sim.Fire"))) {
                        for (int i = 0; i < this.theWorkers.size(); i++) {
                            FolkData folk = (FolkData) this.theWorkers.get(i);
                            folk.selfFire();
                        }

                        guibutton.field_146124_l = false;
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                    } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.PathBox8"))) {
                        this.page = 1;
                        this.func_73866_w_();
                    } else if (this.page == 1) {
                        this.thePathBox.pathType = guibutton.field_146126_j;
                        ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.PathBox9") + guibutton.field_146126_j);
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                    }

                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("actionPerformed出错了：" + e.getMessage());
        }

    }

    @Override
    public void func_73869_a(char c, int i) {
        try {if (i == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
        } catch (Exception e) {
            ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage());
        }

    }

    @Override
    public void func_73864_a(int i, int j, int k) {
        try {
            super.func_73864_a(i, j, k);
        } catch (IOException e) {
            ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage());
            //e.printStackTrace();
        }
    }
}

