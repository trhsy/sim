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
    public void func_73866_w_() {
        ModSimReloaded.log.info("初始化GUI");
        this.folks = FolkData.getFolkUnemployed(true);
        this.showPage();
        super.func_73866_w_();
    }
    private void showPage() {
        try {
            this.field_146292_n.clear();
            int y = 30;
            boolean more = false;
            int count = 0;
            if (this.folkOffset < 0) {
                this.folkOffset = 0;
            }

            for(int f = this.folkOffset; f < this.folks.size(); ++f) {
                this.field_146292_n.add(new GuiButton(f, this.field_146294_l - 55, y, 50, 20, I18n.func_135052_a("container.sim.Fire")));
                y += 20;
                if (y + 20 > this.field_146295_m - 50) {
                    more = true;
                    break;
                }

                ++count;
            }

            if (this.folksOnAPage == 0) {
                this.folksOnAPage = count + 1;
            }

            if (this.folkOffset > 0) {
                this.field_146292_n.add(new GuiButton(1000, 0, 0, 50, 20, "<"));
            }

            if (more) {
                this.field_146292_n.add(new GuiButton(1001, this.field_146294_l - 50, 0, 50, 20, ">"));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
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
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Employees"), this.field_146294_l / 2, 17, 16777215);
            int y = 35;
            if (this.folkOffset < 0) {
                this.folkOffset = 0;
            }

            for(int ff = this.folkOffset; ff < this.folks.size(); ++ff) {
                FolkData folk = (FolkData)this.folks.get(ff);
                this.func_73731_b(this.field_146289_q, folk.name, 2, y, 10551295);
                String status;
                if (folk.employedAt == null) {
                    this.func_73731_b(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Folk_unemployed"), 110, y, 16715792);
                } else {
                    status = "";
                    if (folk.employedAt.theDimension == 0) {
                        status = I18n.func_135052_a("container.sim.Overworld");
                    } else if (folk.employedAt.theDimension == 1) {
                        status = I18n.func_135052_a("container.sim.end");
                    } else if (folk.employedAt.theDimension == -1) {
                        status = I18n.func_135052_a("container.sim.hell");
                    } else {
                        status = I18n.func_135052_a("container.sim.dim") + folk.employedAt.theDimension;
                    }

                    String voc = folk.vocation.toString() + " (" + status + ")";
                    this.func_73731_b(this.field_146289_q, voc, 110, y, 10551295);
                }

                status = "";

                try {
                    status = folk.action.toString() + ", " + folk.statusText;
                } catch (Exception var9) {
                }

                if (status.contains(I18n.func_135052_a("container.sim.employees"))) {
                    status = I18n.func_135052_a("container.sim.folk_data_Staying_home");
                }

                this.func_73731_b(this.field_146289_q, status, 250, y, 10551295);
                y += 20;
                if (y + 20 > this.field_146295_m - 50) {
                    break;
                }
            }
        } catch (Exception var5) {
            ModSimReloaded.log.warn("在绘制字符串/屏幕时捕获异常：" + var5.getMessage());
        }

        super.func_73863_a(i, j, f);
    }

    @Override
    protected void func_146284_a(GuiButton guibutton) {

        if (guibutton.field_146127_k == 1000) {
            this.folkOffset -= this.folksOnAPage;
            this.showPage();
        } else if (guibutton.field_146127_k == 1001) {
            this.folkOffset += this.folksOnAPage;
            this.showPage();
        } else {
            FolkData folk = (FolkData)this.folks.get(guibutton.field_146127_k);
            folk.selfFire();
            guibutton.field_146124_l = false;
        }
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }
    @Override
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }
    @Override
    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
    }
    @Override
    public void func_73864_a(int i, int j, int k) {
        try {
            super.func_73864_a(i, j, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

