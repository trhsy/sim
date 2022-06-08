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
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, I18n.format("container.sim.sim_gui_BC_Done")));
        if (this.theMiningBox != null) {
            if (this.theWorkers != null && this.theWorkers.size() != 0) {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Fire") + ((FolkData) this.theWorkers.get(0)).name));
            } else {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Hire21")));
            }

            String i = "";
            String j = "";
            if (this.theMiningBox.discards == 0) {
                i = I18n.format("container.sim.Mining3");
            } else if (this.theMiningBox.discards == 1) {
                i = I18n.format("container.sim.Mining4");
            } else if (this.theMiningBox.discards == 2) {
                i = I18n.format("container.sim.Mining5");
            } else if (this.theMiningBox.discards == 3) {
                i = I18n.format("container.sim.Mining6");
            } else if (this.theMiningBox.discards == 4) {
                i = I18n.format("container.sim.Mining7");
            }

            if (this.theMiningBox.addGlassCover) {
                j = I18n.format("container.sim.Mining8");
            } else {
                j = I18n.format("container.sim.Mining9");
            }

            GuiButton gb = null;
            if (GameMode.gameMode != GameMode.GAMEMODES.HARDCORE) {
                this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 120, i));
                this.buttonList.add(gb = new GuiButton(3, this.width / 2 - 100, 160, j));
            }

            if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                this.tfSize = new GuiTextField(this.fontRendererObj, this.width / 2 - 25, this.height - 50, 50, 15);
                this.tfSize.setText(this.theMiningBox.size + "");
                this.tfSize.setFocused(true);
                this.tfSize.setMaxStringLength(3);
                if (gb != null) {
                    gb.enabled = false;
                }
            }

        }
    }
    private void extraButtons() {
        if (GameMode.gameMode != GameMode.GAMEMODES.HARDCORE) {
            String i = "";
            String j = "";
            if (this.theMiningBox.discards == 0) {
                i = I18n.format("container.sim.Mining3");
            } else if (this.theMiningBox.discards == 1) {
                i = I18n.format("container.sim.Mining4");
            } else if (this.theMiningBox.discards == 2) {
                i = I18n.format("container.sim.Mining5");
            } else if (this.theMiningBox.discards == 3) {
                i = I18n.format("container.sim.Mining6");
            } else if (this.theMiningBox.discards == 4) {
                i = I18n.format("container.sim.Mining7");
            }

            if (this.theMiningBox.addGlassCover) {
                j = I18n.format("container.sim.Mining8");
            } else {
                j = I18n.format("container.sim.Mining9");
            }

            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 120, i));
            this.buttonList.add(new GuiButton(3, this.width / 2 - 100, 140, j));
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
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Mining10"), this.width / 2, 17, 16777215);

            try {
                if (this.theMiningBox.marker1XYZ == null) {
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Mining11"), this.width / 2, 27, 16711680);
                }
            } catch (Exception var7) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Mining12"), this.width / 2, 27, 16711680);
            }

            if (this.theWorkers != null && this.theWorkers.size() > 0) {
                try {
                    String others = "";
                    if (this.theWorkers.size() > 1) {
                        others = I18n.format("container.sim.Mining13") + (this.theWorkers.size() - 1) + I18n.format("container.sim.Mining14");
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            if (this.theMiningBox != null) {
                try {
                    if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                        this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Mining14"), this.width / 2, this.height - 60, 16777130);
                        this.tfSize.drawTextBox();
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }

            super.drawScreen(i, j, f);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                if (guibutton.displayString.contentEquals(I18n.format("container.sim.Hire21"))) {
                    GuiEmployFolk ui = new GuiEmployFolk(this.theMiningBox, Vocation.MINER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.startsWith(I18n.format("container.sim.Fire"))) {
                    for (int i = 0; i < this.theWorkers.size(); ++i) {
                        FolkData folk = (FolkData) this.theWorkers.get(i);
                        folk.selfFire();
                    }

                    guibutton.enabled = false;
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    String i;
                    if (guibutton.id == 2) {
                        ++this.theMiningBox.discards;
                        if (this.theMiningBox.discards > 4) {
                            this.theMiningBox.discards = 0;
                        }

                        i = "";
                        if (this.theMiningBox.discards == 0) {
                            i = I18n.format("container.sim.Mining3");
                        } else if (this.theMiningBox.discards == 1) {
                            i = I18n.format("container.sim.Mining4");
                        } else if (this.theMiningBox.discards == 2) {
                            i = I18n.format("container.sim.Mining5");
                        } else if (this.theMiningBox.discards == 3) {
                            i = I18n.format("container.sim.Mining6");
                        } else if (this.theMiningBox.discards == 4) {
                            i = I18n.format("container.sim.Mining7");
                        }

                        guibutton.displayString = i;
                    } else if (guibutton.id == 3) {
                        this.theMiningBox.addGlassCover = !this.theMiningBox.addGlassCover;
                        i = "";
                        if (this.theMiningBox.addGlassCover) {
                            i = I18n.format("container.sim.Mining8");
                        } else {
                            i = I18n.format("container.sim.Mining9");
                        }

                        guibutton.displayString = i;
                    }
                }

            }
        }
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        } else {
            if (this.tfSize != null) {
                this.tfSize.textboxKeyTyped(c, i);
                int s = 3;

                try {
                    s = Integer.parseInt(this.tfSize.getText());
                } catch (Exception var5) {
                }

                this.theMiningBox.size = s;
            }

        }
    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        if (this.tfSize != null) {
            this.tfSize.mouseClicked(i, j, k);
        }

        super.mouseClicked(i, j, k);
    }
}
