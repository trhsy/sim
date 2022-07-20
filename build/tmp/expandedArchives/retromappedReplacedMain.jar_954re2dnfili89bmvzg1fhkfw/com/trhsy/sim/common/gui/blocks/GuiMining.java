package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameMode;
import com.trhsy.sim.common.entity.functionality.MiningBox;
import com.trhsy.sim.common.gui.folk.GuiEmployFolk;
import com.trhsy.sim.common.jobs.Vocation;
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
 * @ClassName GuiMining
 * @Description todo 采矿
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:35
 * ========================================
 **/
public class GuiMining extends GuiScreen {
    ArrayList<FolkData> theWorkers = new ArrayList();
    MiningBox theMiningBox = null;
    private GuiTextField tfSize;
    private int mouseCount = 0;

    public GuiMining(MiningBox miningBlock, ArrayList<FolkData> folks) {
        this.theMiningBox = miningBlock;
        this.theWorkers = folks;
    }
    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73876_c() {
        if (this.tfSize != null) {
            this.tfSize.func_146178_a();
        }

    }

    @Override
    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, I18n.func_135052_a("container.sim.sim_gui_BC_Done")));
        if (this.theMiningBox != null) {
            if (this.theWorkers != null && this.theWorkers.size() != 0) {
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, I18n.func_135052_a("container.sim.Fire") + ((FolkData) this.theWorkers.get(0)).name));
            } else {
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, I18n.func_135052_a("container.sim.Hire21")));
            }

            String i = "";
            String j = "";
            if (this.theMiningBox.discards == 0) {
                i = I18n.func_135052_a("container.sim.Mining3");
            } else if (this.theMiningBox.discards == 1) {
                i = I18n.func_135052_a("container.sim.Mining4");
            } else if (this.theMiningBox.discards == 2) {
                i = I18n.func_135052_a("container.sim.Mining5");
            } else if (this.theMiningBox.discards == 3) {
                i = I18n.func_135052_a("container.sim.Mining6");
            } else if (this.theMiningBox.discards == 4) {
                i = I18n.func_135052_a("container.sim.Mining7");
            }

            if (this.theMiningBox.addGlassCover) {
                j = I18n.func_135052_a("container.sim.Mining8");
            } else {
                j = I18n.func_135052_a("container.sim.Mining9");
            }

            GuiButton gb = null;
            if (GameMode.gameMode != GameMode.GAMEMODES.HARDCORE) {
                this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 120, i));
                this.field_146292_n.add(gb = new GuiButton(3, this.field_146294_l / 2 - 100, 160, j));
            }

            if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                this.tfSize = new GuiTextField(0,this.field_146289_q, this.field_146294_l / 2 - 25, this.field_146295_m - 50, 50, 15);
                this.tfSize.func_146180_a(this.theMiningBox.size + "");
                this.tfSize.func_146195_b(true);
                this.tfSize.func_146203_f(3);
                if (gb != null) {
                    gb.field_146124_l = false;
                }
            }

        }
    }
    private void extraButtons() {
        if (GameMode.gameMode != GameMode.GAMEMODES.HARDCORE) {
            String i = "";
            String j = "";
            if (this.theMiningBox.discards == 0) {
                i = I18n.func_135052_a("container.sim.Mining3");
            } else if (this.theMiningBox.discards == 1) {
                i = I18n.func_135052_a("container.sim.Mining4");
            } else if (this.theMiningBox.discards == 2) {
                i = I18n.func_135052_a("container.sim.Mining5");
            } else if (this.theMiningBox.discards == 3) {
                i = I18n.func_135052_a("container.sim.Mining6");
            } else if (this.theMiningBox.discards == 4) {
                i = I18n.func_135052_a("container.sim.Mining7");
            }

            if (this.theMiningBox.addGlassCover) {
                j = I18n.func_135052_a("container.sim.Mining8");
            } else {
                j = I18n.func_135052_a("container.sim.Mining9");
            }

            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 120, i));
            this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, 140, j));
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
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Mining10"), this.field_146294_l / 2, 17, 16777215);

            try {
                if (this.theMiningBox.marker1XYZ == null) {
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Mining11"), this.field_146294_l / 2, 27, 16711680);
                }
            } catch (Exception var7) {
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Mining12"), this.field_146294_l / 2, 27, 16711680);
            }

            if (this.theWorkers != null && this.theWorkers.size() > 0) {
                try {
                    String others = "";
                    if (this.theWorkers.size() > 1) {
                        others = I18n.func_135052_a("container.sim.Mining13") + (this.theWorkers.size() - 1) + I18n.func_135052_a("container.sim.Mining14");
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            if (this.theMiningBox != null) {
                try {
                    if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                        this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Mining14"), this.field_146294_l / 2, this.field_146295_m - 60, 16777130);
                        this.tfSize.func_146194_f();
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }

            super.func_73863_a(i, j, f);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    @Override
    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            } else {
                if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire21"))) {
                    GuiEmployFolk ui = new GuiEmployFolk(this.theMiningBox, Vocation.MINER);
                    this.field_146297_k.func_147108_a(ui);
                } else if (guibutton.field_146126_j.startsWith(I18n.func_135052_a("container.sim.Fire"))) {
                    for (int i = 0; i < this.theWorkers.size(); ++i) {
                        FolkData folk = (FolkData) this.theWorkers.get(i);
                        folk.selfFire();
                    }

                    guibutton.field_146124_l = false;
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                } else {
                    String i;
                    if (guibutton.field_146127_k == 2) {
                        ++this.theMiningBox.discards;
                        if (this.theMiningBox.discards > 4) {
                            this.theMiningBox.discards = 0;
                        }

                        i = "";
                        if (this.theMiningBox.discards == 0) {
                            i = I18n.func_135052_a("container.sim.Mining3");
                        } else if (this.theMiningBox.discards == 1) {
                            i = I18n.func_135052_a("container.sim.Mining4");
                        } else if (this.theMiningBox.discards == 2) {
                            i = I18n.func_135052_a("container.sim.Mining5");
                        } else if (this.theMiningBox.discards == 3) {
                            i = I18n.func_135052_a("container.sim.Mining6");
                        } else if (this.theMiningBox.discards == 4) {
                            i = I18n.func_135052_a("container.sim.Mining7");
                        }

                        guibutton.field_146126_j = i;
                    } else if (guibutton.field_146127_k == 3) {
                        this.theMiningBox.addGlassCover = !this.theMiningBox.addGlassCover;
                        i = "";
                        if (this.theMiningBox.addGlassCover) {
                            i = I18n.func_135052_a("container.sim.Mining8");
                        } else {
                            i = I18n.func_135052_a("container.sim.Mining9");
                        }

                        guibutton.field_146126_j = i;
                    }
                }

            }
        }
    }

    @Override
    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        } else {
            if (this.tfSize != null) {
                this.tfSize.func_146201_a(c, i);
                int s = 3;

                try {
                    s = Integer.parseInt(this.tfSize.func_146179_b());
                } catch (Exception var5) {
                }

                this.theMiningBox.size = s;
            }

        }
    }

    @Override
    public void func_73864_a(int i, int j, int k) {
        if (this.tfSize != null) {
            this.tfSize.func_146192_a(i, j, k);
        }

        try {
            super.func_73864_a(i, j, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
