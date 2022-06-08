package com.trhsy.sim.common.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiShowEmployees
 * @Description todo 展示员工
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:36
 * ========================================
 **/
public class GuiShowEmployees extends GuiScreen {
    ArrayList folks;
    //鼠标计数
    private int mouseCount = 0;
    //前叉偏移
    private int folkOffset = 0;
    //人们在一个页面上
    private int folksOnAPage = 0;

    public GuiShowEmployees() {
        // TODO document why this constructor is empty
    }

    @Override
    public void initGui() {
        ModSimReloaded.log.info("初始化GUI");
        this.folks = FolkData.getFolkUnemployed(true);
        this.showPage();
        super.initGui();
        /*this.buttonList.add(new GuiButton(0, this.width / 2 - 75, 40, I18n.format("container.sim.ShowEmployees1")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 75, 90, I18n.format("container.sim.ShowEmployees2")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 75, 140, I18n.format("container.sim.ShowEmployees3")));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 75, 190, I18n.format("container.sim.ShowEmployees4")));*/
    }
    private void showPage() {
        try {
            this.buttonList.clear();
            int y = 30;
            boolean more = false;
            int count = 0;
            if (this.folkOffset < 0) {
                this.folkOffset = 0;
            }

            for(int f = this.folkOffset; f < this.folks.size(); ++f) {
                this.buttonList.add(new GuiButton(f, this.width - 55, y, 50, 20, I18n.format("container.sim.Fire")));
                y += 20;
                if (y + 20 > this.height - 50) {
                    more = true;
                    break;
                }

                ++count;
            }

            if (this.folksOnAPage == 0) {
                this.folksOnAPage = count + 1;
            }

            if (this.folkOffset > 0) {
                this.buttonList.add(new GuiButton(1000, 0, 0, 50, 20, "<"));
            }

            if (more) {
                this.buttonList.add(new GuiButton(1001, this.width - 50, 0, 50, 20, ">"));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
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
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Employees"), this.width / 2, 17, 16777215);
            int y = 35;
            if (this.folkOffset < 0) {
                this.folkOffset = 0;
            }

            for(int ff = this.folkOffset; ff < this.folks.size(); ++ff) {
                FolkData folk = (FolkData)this.folks.get(ff);
                this.drawString(this.fontRendererObj, folk.name, 2, y, 10551295);
                String status;
                if (folk.employedAt == null) {
                    this.drawString(this.fontRendererObj, I18n.format("container.sim.gui_Folk_unemployed"), 110, y, 16715792);
                } else {
                    status = "";
                    if (folk.employedAt.theDimension == 0) {
                        status = I18n.format("container.sim.Overworld");
                    } else if (folk.employedAt.theDimension == 1) {
                        status = I18n.format("container.sim.end");
                    } else if (folk.employedAt.theDimension == -1) {
                        status = I18n.format("container.sim.hell");
                    } else {
                        status = I18n.format("container.sim.dim") + folk.employedAt.theDimension;
                    }

                    String voc = folk.vocation.toString() + " (" + status + ")";
                    this.drawString(this.fontRendererObj, voc, 110, y, 10551295);
                }

                status = "";

                try {
                    status = folk.action.toString() + ", " + folk.statusText;
                } catch (Exception var9) {
                }

                if (status.contains(I18n.format("container.sim.employees"))) {
                    status = I18n.format("container.sim.folk_data_Staying_home");
                }

                this.drawString(this.fontRendererObj, status, 250, y, 10551295);
                y += 20;
                if (y + 20 > this.height - 50) {
                    break;
                }
            }
            /*this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees5"), this.width / 2, 20, 16777215);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees6"), this.width / 2, 60, 16776960);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees7"), this.width / 2, 110, 16776960);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees8"), this.width / 2, 160, 16776960);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees9"), this.width / 2, 210, 16776960);*/
        } catch (Exception var5) {
            ModSim.log.warn("在绘制字符串/屏幕时捕获异常：" + var5.getMessage());
        }

        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        /*if (guibutton.id == 0) {
            ModSimReloaded.states.gameModeNumber = 10;
            ModSimReloaded.log.info("关闭重新加载的模拟城市");
        } else if (guibutton.id == 1) {
            ModSimReloaded.states.gameModeNumber = 0;
            ModSimReloaded.log.info("在正常模式下重新加载模拟城市");
            FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        } else if (guibutton.id == 2) {
            ModSimReloaded.states.gameModeNumber = 1;
        } else if (guibutton.id == 3) {
            ModSimReloaded.states.gameModeNumber = 2;
        }

        ModSimReloaded.states.saveStates();
        this.running = false;
        this.mc.currentScreen = null;
        this.mc.setIngameFocus();*/
        if (guibutton.id == 1000) {
            this.folkOffset -= this.folksOnAPage;
            this.showPage();
        } else if (guibutton.id == 1001) {
            this.folkOffset += this.folksOnAPage;
            this.showPage();
        } else {
            FolkData folk = (FolkData)this.folks.get(guibutton.id);
            folk.selfFire();
            guibutton.enabled = false;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
    @Override
    public void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }
}

