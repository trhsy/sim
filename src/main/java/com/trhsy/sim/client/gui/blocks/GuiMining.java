package com.trhsy.sim.client.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameMode;
import com.trhsy.sim.common.core.entity.functionality.MiningBox;
import com.trhsy.sim.client.gui.folk.GuiEmployFolk;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName GuiMining
 * @Description todo 采矿
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:35
 * ========================================
 **/
@SideOnly(Side.CLIENT)
public class GuiMining extends GuiScreen {
    List<FolkData> theWorkers = new CopyOnWriteArrayList();
    MiningBox theMiningBox = null;
    private GuiTextField tfSize;
    private int mouseCount = 0;

    public GuiMining(MiningBox miningBlock, List<FolkData> folks) {
        try {
            this.theMiningBox = miningBlock;
            this.theWorkers = folks;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiMining出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        try {
            if (this.tfSize != null) {
                this.tfSize.updateCursorCounter();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("updateScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    @Override
    public void initGui() {
        try {
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
                    this.tfSize = new GuiTextField(0,this.fontRendererObj, this.width / 2 - 25, this.height - 50, 50, 15);
                    this.tfSize.setText(this.theMiningBox.size + "");
                    this.tfSize.setFocused(true);
                    this.tfSize.setMaxStringLength(3);
                    if (gb != null) {
                        gb.enabled = false;
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiMining-initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    private void extraButtons() {
        try {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("extraButtons出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            } catch (Exception e) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Mining12"), this.width / 2, 27, 16711680);
            }

            if (this.theWorkers != null && this.theWorkers.size() > 0) {
                    String others = "";
                    if (this.theWorkers.size() > 1) {
                        others = I18n.format("container.sim.Mining13") + (this.theWorkers.size() - 1) + I18n.format("container.sim.Mining14");
                    }
            }

            if (this.theMiningBox != null) {
                    if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                        this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Mining14"), this.width / 2, this.height - 60, 16777130);
                        this.tfSize.drawTextBox();
                    }
            }

            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiMining-drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var8.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.Hire21"))) {
                        GuiEmployFolk ui = new GuiEmployFolk(this.theMiningBox, Vocation.MINER);
                        this.mc.displayGuiScreen(ui);
                    } else if (guibutton.displayString.startsWith(I18n.format("container.sim.Fire"))) {
                        for (int i = 0; i < this.theWorkers.size(); i++) {
                            FolkData folk = this.theWorkers.get(i);
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIMININGactionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
            } else {
                if (this.tfSize != null) {
                    this.tfSize.textboxKeyTyped(c, i);
                    int s = 3;

                    try {
                        s = Integer.parseInt(this.tfSize.getText());
                    } catch (Exception e) {
                    }

                    this.theMiningBox.size = s;
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        try {if (this.tfSize != null) {
            this.tfSize.mouseClicked(i, j, k);
        }


            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //e.printStackTrace();
        }
    }
}
