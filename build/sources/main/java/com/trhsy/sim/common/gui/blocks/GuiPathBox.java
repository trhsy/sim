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
import java.util.concurrent.CopyOnWriteArrayList;

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
    CopyOnWriteArrayList<FolkData> theWorkers = new CopyOnWriteArrayList();
    PathBox thePathBox = null;
    private GuiTextField tfSize;
    private int mouseCount = 0;
    private int page = 0;

    public GuiPathBox(PathBox pathBlock, CopyOnWriteArrayList<FolkData> folks) {
        try {
            this.thePathBox = pathBlock;
            this.theWorkers = folks;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiPathBox出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            if (this.thePathBox != null) {
                if (this.thePathBox.marker1XYZ != null) {
                    if (this.page == 0) {
                        if (this.theWorkers != null && this.theWorkers.size() != 0) {
                            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Fire") + ((FolkData) this.theWorkers.get(0)).name));
                            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 60, I18n.format("container.sim.PathBox1")));
                        } else {
                            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Hire24")));
                        }
                    } else if (this.page == 1) {
                        this.buttonList.add(new GuiButton(1, 10, 20, I18n.format("container.sim.PathBox3")));
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox4"), this.width / 2, 17, 16777215);

            try {
                if (this.thePathBox.marker1XYZ == null) {
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox5"), this.width / 2, 27, 16711680);
                }
            } catch (Exception e) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox6"), this.width / 2, 27, 16711680);
            }

            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var6.printStackTrace();
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
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.Hire24"))) {
                        GuiEmployFolk ui = new GuiEmployFolk(this.thePathBox, Vocation.PATHBUILDER);
                        this.mc.displayGuiScreen(ui);
                    } else if (guibutton.displayString.startsWith(I18n.format("container.sim.Fire"))) {
                        for (int i = 0; i < this.theWorkers.size(); i++) {
                            FolkData folk = (FolkData) this.theWorkers.get(i);
                            folk.selfFire();
                        }

                        guibutton.enabled = false;
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                    } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.PathBox8"))) {
                        this.page = 1;
                        this.initGui();
                    } else if (this.page == 1) {
                        this.thePathBox.pathType = guibutton.displayString;
                        ModSimReloaded.sendChat(I18n.format("container.sim.PathBox9") + guibutton.displayString);
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIPATHBOX-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void keyTyped(char c, int i) {
        try {if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        try {
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //e.printStackTrace();
        }
    }
}

