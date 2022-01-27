package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.ClientTickHandler;
import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.CourierTask;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

/**
 * ========================================
 *
 * @ClassName GuiBeamPlayerTo
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:26
 * ========================================
 **/
public class GuiBeamPlayerTo extends GuiScreen {
    private EntityPlayer thePlayer = null;

    public GuiBeamPlayerTo(EntityPlayer thePlayer) {
        this.thePlayer = thePlayer;
    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_73876_c() {
    }

    public void func_73866_w_() {
        this.initscreen();
    }

    private void initscreen() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, 5, 5, 50, 20, "Cancel"));
        int x = 10;
        int y = 40;
        int idx = 2;

        for(int f = 0; f < ModSimukraft.theCourierPoints.size(); ++f) {
            V3 cpoint = (V3)ModSimukraft.theCourierPoints.get(f);
            this.field_146292_n.add(new GuiButton(idx, x, y, 110, 20, cpoint.name));
            ++idx;
            x += 110;
            if (x + 110 > this.field_146294_l) {
                x = 10;
                y += 20;
            }

            if (y + 20 > this.field_146295_m - 50) {
                break;
            }
        }

    }

    public void func_73863_a(int i, int j, float f) {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, "Choose a point to beam to...", this.field_146294_l / 2, 17, 16777215);
        if (ModSimukraft.theCourierPoints.size() == 0) {
            this.func_73732_a(this.field_146289_q, "You don't have any courier/beaming points!", this.field_146294_l / 2, 37, 16752800);
            this.func_73732_a(this.field_146289_q, "Place a single Sim-U-Marker down and right click it", this.field_146294_l / 2, 57, 16752800);
            this.func_73732_a(this.field_146289_q, "to make one. You can then beam there using ANY control box.", this.field_146294_l / 2, 77, 16752800);
        }

        super.func_73863_a(i, j, f);
    }

    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
            } else {
                String name = guibutton.field_146126_j.trim();
                V3 v = CourierTask.getCourierPoint(name);
                V3 safePoint = v.clone();
                Double var6 = safePoint.y;
                Double var7 = safePoint.y = safePoint.y + 1.0D;
                ModSimukraft.sendChat("Beaming you to " + name);
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                ClientTickHandler.beamingPlayer = this.thePlayer;
                ClientTickHandler.beamingStage = 1;
                ClientTickHandler.beamingStartedAt = System.currentTimeMillis();
                ClientTickHandler.beamingTo = safePoint.clone();
            }
        }
    }

    public GuiButton getButtonWithId(int id) {
        for(int x = 0; x < this.field_146292_n.size(); ++x) {
            GuiButton retbut = (GuiButton)this.field_146292_n.get(x);
            if (retbut.field_146127_k == id) {
                return retbut;
            }
        }

        return null;
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.field_146297_k.func_71381_h();
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
        }
    }
}