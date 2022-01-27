package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.MiningBox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiMining
 * @Description todo
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
        if (this.theMiningBox != null) {
            if (this.theWorkers != null && this.theWorkers.size() != 0) {
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, "Fire " + ((FolkData)this.theWorkers.get(0)).name));
            } else {
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, "Hire Miner"));
            }

            String i = "";
            String j = "";
            if (this.theMiningBox.discards == 0) {
                i = "Keep all block types";
            } else if (this.theMiningBox.discards == 1) {
                i = "Discard Dirt blocks";
            } else if (this.theMiningBox.discards == 2) {
                i = "Discard Dirt and Stone";
            } else if (this.theMiningBox.discards == 3) {
                i = "Discard Dirt and Sand";
            } else if (this.theMiningBox.discards == 4) {
                i = "Discard Dirt, sand and Stone";
            }

            if (this.theMiningBox.addGlassCover) {
                j = "Cover with glass (put glass in chest)";
            } else {
                j = "Leave mine open";
            }

            GuiButton gb = null;
            if (ModSimukraft.gameMode != GameMode.HARDCORE) {
                this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 120, i));
                this.field_146292_n.add(gb = new GuiButton(3, this.field_146294_l / 2 - 100, 160, j));
            }

            if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                this.tfSize = new GuiTextField(this.field_146289_q, this.field_146294_l / 2 - 25, this.field_146295_m - 50, 50, 15);
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
        if (ModSimukraft.gameMode != GameMode.HARDCORE) {
            String i = "";
            String j = "";
            if (this.theMiningBox.discards == 0) {
                i = "Keep all block types";
            } else if (this.theMiningBox.discards == 1) {
                i = "Discard Dirt blocks";
            } else if (this.theMiningBox.discards == 2) {
                i = "Discard Dirt and Stone";
            } else if (this.theMiningBox.discards == 3) {
                i = "Discard Dirt and Sand";
            } else if (this.theMiningBox.discards == 4) {
                i = "Discard Dirt, sand and Stone";
            }

            if (this.theMiningBox.addGlassCover) {
                j = "Cover with glass (put glass in chest)";
            } else {
                j = "Leave mine open";
            }

            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 120, i));
            this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, 140, j));
        }
    }

    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, "Mining", this.field_146294_l / 2, 17, 16777215);

            try {
                if (this.theMiningBox.marker1XYZ == null) {
                    this.func_73732_a(this.field_146289_q, "Error: No markers placed - 3 markers are needed to mine vertically, 1 marker for horizontal.", this.field_146294_l / 2, 27, 16711680);
                }
            } catch (Exception var7) {
                this.func_73732_a(this.field_146289_q, "Error: Please place markers BEFORE the mining box", this.field_146294_l / 2, 27, 16711680);
            }

            if (this.theWorkers != null && this.theWorkers.size() > 0) {
                try {
                    String others = "";
                    if (this.theWorkers.size() > 1) {
                        others = " and " + (this.theWorkers.size() - 1) + " other folks";
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            if (this.theMiningBox != null) {
                try {
                    if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                        this.func_73732_a(this.field_146289_q, "Size of Horizontal mine", this.field_146294_l / 2, this.field_146295_m - 60, 16777130);
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

    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            } else {
                if (guibutton.field_146126_j.contentEquals("Hire Miner")) {
                    GuiEmployFolk ui = new GuiEmployFolk(this.theMiningBox, Vocation.MINER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.startsWith("Fire ")) {
                    for(int i = 0; i < this.theWorkers.size(); ++i) {
                        FolkData folk = (FolkData)this.theWorkers.get(i);
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
                            i = "Keep all block types";
                        } else if (this.theMiningBox.discards == 1) {
                            i = "Discard Dirt blocks";
                        } else if (this.theMiningBox.discards == 2) {
                            i = "Discard Dirt and Stone";
                        } else if (this.theMiningBox.discards == 3) {
                            i = "Discard Dirt and Sand";
                        } else if (this.theMiningBox.discards == 4) {
                            i = "Discard Dirt, sand and Stone";
                        }

                        guibutton.field_146126_j = i;
                    } else if (guibutton.field_146127_k == 3) {
                        this.theMiningBox.addGlassCover = !this.theMiningBox.addGlassCover;
                        i = "";
                        if (this.theMiningBox.addGlassCover) {
                            i = "Cover with glass (put glass in chest)";
                        } else {
                            i = "Leave mine open";
                        }

                        guibutton.field_146126_j = i;
                    }
                }

            }
        }
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
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

    public void func_73864_a(int i, int j, int k) {
        if (this.tfSize != null) {
            this.tfSize.func_146192_a(i, j, k);
        }

        super.func_73864_a(i, j, k);
    }
}
