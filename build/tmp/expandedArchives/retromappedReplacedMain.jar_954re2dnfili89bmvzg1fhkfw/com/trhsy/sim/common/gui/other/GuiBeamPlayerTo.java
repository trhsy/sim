package com.trhsy.sim.common.gui.other;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.ClientTickHandler;
import com.trhsy.sim.common.entity.CourierTask;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

/**
 * ========================================
 *
 * @ClassName GuiBeamPlayerTo
 * @Description todo Beam 播放器
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:26
 * ========================================
 **/
public class GuiBeamPlayerTo extends GuiScreen {
    private EntityPlayer thePlayer = null;

    public GuiBeamPlayerTo(EntityPlayer thePlayer) {
        this.thePlayer = thePlayer;
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73876_c() {
    }

    @Override
    public void func_73866_w_() {
        this.initscreen();
    }

    private void initscreen() {
        try {
            this.field_146292_n.clear();
            this.field_146292_n.add(new GuiButton(0, 5, 5, 50, 20, I18n.func_135052_a("container.sim.sim_gui_player_to_Cancel")));
            int x = 10;
            int y = 40;
            int idx = 2;

            for (int f = 0; f < ModSimReloaded.theCourierPoints.size(); ++f) {
                V3 cpoint = (V3) ModSimReloaded.theCourierPoints.get(f);
                this.field_146292_n.add(new GuiButton(idx, x, y, 110, 20, cpoint.name));
                idx++;
                x += 110;
                if (x + 110 > this.field_146294_l) {
                    x = 10;
                    y += 20;
                }

                if (y + 20 > this.field_146295_m - 50) {
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initscreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        try {
            this.func_146276_q_();
            //选择要投射到的点...
            String sim_gui_BPT_Choose = I18n.func_135052_a("container.sim.sim_gui_BPT_Choose");
            this.func_73732_a(this.field_146289_q, sim_gui_BPT_Choose, this.field_146294_l / 2, 17, 16777215);
            if (ModSimReloaded.theCourierPoints.size() == 0) {
                //你没有任何信使/光束点！
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_player_to_You"), this.field_146294_l / 2, 37, 16752800);
                //向下放置一个标记棒并右键单击它
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_player_to_Place"), this.field_146294_l / 2, 57, 16752800);
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_player_to_to"), this.field_146294_l / 2, 77, 16752800);
            }

            super.func_73863_a(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void func_146284_a(GuiButton guibutton) {
        try {
            if (guibutton.field_146124_l) {
                if (guibutton.field_146127_k == 0) {
                    this.field_146297_k.func_147108_a((GuiScreen)null);
                } else {
                    String name = guibutton.field_146126_j.trim();
                    V3 v = CourierTask.getCourierPoint(name);
                    V3 safePoint = v.clone();
                    Double var6 = safePoint.y;
                    Double var7 = safePoint.y = safePoint.y + 1;
                    ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.sim_gui_Beaming") + name);
                    this.field_146297_k.func_147108_a((GuiScreen) null);
                    ClientTickHandler.beamingPlayer = this.thePlayer;
                    ClientTickHandler.beamingStage = 1;
                    ClientTickHandler.beamingStartedAt = System.currentTimeMillis();
                    ClientTickHandler.beamingTo = safePoint.clone();
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIBEAMPLAYERTO-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public GuiButton getButtonWithId(int id) {
        try {
            for(int x = 0; x < this.field_146292_n.size(); ++x) {
                GuiButton retbut = (GuiButton)this.field_146292_n.get(x);
                if (retbut.field_146127_k == id) {
                    return retbut;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getButtonWithId出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    @Override
    public void func_146281_b() {
        try {
            Keyboard.enableRepeatEvents(false);
            this.field_146297_k.func_71381_h();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onGuiClosed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void func_73869_a(char c, int i) {
        try {
            if (i == 1) {
                this.field_146297_k.func_147108_a((GuiScreen)null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}