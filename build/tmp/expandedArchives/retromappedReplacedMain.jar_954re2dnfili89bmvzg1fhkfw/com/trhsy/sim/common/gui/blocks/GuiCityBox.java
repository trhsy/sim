package com.trhsy.sim.common.gui.blocks;

import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.gui.other.GuiBeamPlayerTo;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @ClassName GuiCityBox
 * @Description todo
 * @Author Tian
 * @Date 2022/4/414:13
 **/
public class GuiCityBox extends GuiScreen {
    private int mouseCount = 0;
    public V3 location;
    public Building theBuilding = null;
    public FolkData theFolk = null;
    private EntityPlayer playerWhoClickedIt = null;
    GuiCityBoxTaxes taxesGui = null;

    public GuiCityBox(V3 location, EntityPlayer thePlayer) {
        try {
            this.location = location.clone();
            Building.loadAllBuildings();
            this.theBuilding = Building.getBuilding(location);
            this.playerWhoClickedIt = thePlayer;
        } catch (Exception e) {
            ModSimReloaded.log.error("GuiCityBox出错了：" + e.getMessage());
        }
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }
    @Override
    public void func_73866_w_() {
        try {
            this.field_146292_n.clear();
            this.field_146292_n.add(new GuiButton(0, 5, 5, 50, 20, "完成"));
            this.field_146292_n.add(new GuiButton(30, this.field_146294_l - 110, this.field_146295_m - 30, 100, 20, "把我传送到..."));
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, this.field_146295_m / 2 - 70, 100, 20, "税和租金"));
            this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 50, this.field_146295_m / 2 - 30, 100, 20, "工作时间"));
            this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 50, this.field_146295_m / 2 + 10, 100, 20, "信息"));
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
            this.func_73732_a(this.field_146289_q, "城市控制面板", this.field_146294_l / 2, 17, 16777215);
            super.func_73863_a(i, j, f);
        } catch (Exception var5) {
            //var5.printStackTrace();
        }

    }

    @Override
    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            } else {
                if (guibutton.field_146127_k == 30) {
                    GuiScreen ui = new GuiBeamPlayerTo(this.playerWhoClickedIt);
                    this.field_146297_k.func_147108_a((GuiScreen)null);
                    this.field_146297_k.func_147108_a(ui);
                } else if (guibutton.field_146126_j.contentEquals("税和租金")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.field_146297_k.func_147108_a(this.taxesGui);
                } else if (guibutton.field_146126_j.contentEquals("租金")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.field_146297_k.func_147108_a(this.taxesGui);
                } else if (guibutton.field_146126_j.contentEquals("工作时间")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.field_146297_k.func_147108_a(this.taxesGui);
                } else if (guibutton.field_146126_j.contentEquals("信息")) {
                    this.taxesGui = new GuiCityBoxTaxes(this.location, this.playerWhoClickedIt, this);
                    this.field_146297_k.func_147108_a(this.taxesGui);
                }

            }
        }
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.field_146297_k.func_71381_h();
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
    }
}
