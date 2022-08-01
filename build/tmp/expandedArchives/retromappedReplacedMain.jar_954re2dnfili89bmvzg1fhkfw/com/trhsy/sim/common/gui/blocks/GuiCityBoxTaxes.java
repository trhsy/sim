package com.trhsy.sim.common.gui.blocks;

import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @ClassName GuiCityBoxTaxes
 * @Description todo 城市箱税
 * @Author Tian
 * @Date 2022/4/414:14
 **/
public class GuiCityBoxTaxes extends GuiScreen {
    private int mouseCount = 0;
    private EntityPlayer playerWhoClickedIt = null;
    private GuiCityBox cityBoxGui;
    private int guiID = 0;
    int taxPercentage;

    public GuiCityBoxTaxes(V3 location, EntityPlayer thePlayer, GuiCityBox gui) {
        try {
            this.playerWhoClickedIt = thePlayer;
            this.cityBoxGui = gui;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiCityBoxTaxes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, 5, 5, 50, 20, "返回"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2, this.field_146295_m / 2 - 40, 60, 20, String.valueOf(this.taxPercentage)));
    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, "税", this.field_146294_l / 2, 17, 16777215);
            super.func_73863_a(i, j, f);
        } catch (Exception e) {
            //var5.printStackTrace();
        }

    }

    @Override
    public void func_146284_a(GuiButton guibutton) {
        try {
            if (guibutton.field_146124_l) {
                if (guibutton.field_146127_k == 0) {
                    this.field_146297_k.field_71462_r = this.cityBoxGui;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUICITYBOXTAXES-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.field_146297_k.func_71381_h();
    }

    @Override
    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
    }
}

