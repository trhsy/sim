package com.trhsy.sim.common.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.gui.GuiRunMod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
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

            for(int f = this.folkOffset; f < this.folks.size(); f++) {
                this.buttonList.add(new GuiButton(f, this.width - 55, y, 50, 20, I18n.format("container.sim.Fire")));
                y += 20;
                if (y + 20 > this.height - 50) {
                    more = true;
                    break;
                }

                count++;
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
            //var5.printStackTrace();
            ModSimReloaded.log.error("显示员工出差："+var5.getMessage());
        }

    }
    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                this.mouseCount++;
                Mouse.setGrabbed(false);
            }
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Employees"), this.width / 2, 17, 16777215);
            int y = 35;
            if (this.folkOffset < 0) {
                this.folkOffset = 0;
            }

            for(int ff = this.folkOffset; ff < this.folks.size(); ff++) {
                FolkData folk = (FolkData)this.folks.get(ff);
                this.drawString(this.fontRendererObj, folk.name, 2, y, 10551295);
                if (folk.employedAt == null) {
                    this.drawString(this.fontRendererObj, I18n.format("container.sim.gui_Folk_unemployed"), 110, y, 16715792);
                } else {
                    String dime = "";
                    if (folk.employedAt.theDimension == 0) {
                        dime = I18n.format("container.sim.Overworld");
                    } else if (folk.employedAt.theDimension == 1) {
                        dime = I18n.format("container.sim.end");
                    } else if (folk.employedAt.theDimension == -1) {
                        dime = I18n.format("container.sim.hell");
                    } else {
                        dime = I18n.format("container.sim.dim") + folk.employedAt.theDimension;
                    }

                    String voc = folk.vocation.toString() + " (" + dime + ")";
                    this.drawString(this.fontRendererObj, voc, 110, y, 10551295);
                }

                String status = "";

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
        } catch (Exception var5) {
            ModSimReloaded.log.warn("在绘制字符串/屏幕时捕获异常：" + var5.getMessage());
        }

        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {

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
        try {
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            ModSimReloaded.log.error("鼠标点击出问题了："+e.getMessage());
            //e.printStackTrace();
        }
    }
}

