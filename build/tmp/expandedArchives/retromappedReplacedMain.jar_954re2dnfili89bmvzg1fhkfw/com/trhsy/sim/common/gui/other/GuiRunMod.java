package com.trhsy.sim.common.gui.other;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Mouse;

/**
 * ========================================
 *
 * @ClassName GuiRunMod
 * @Description todo Gui运行模式
 * @Author Administrator
 * @Date 2022/1/26 0026下午 3:37
 * ========================================
 **/
public class GuiRunMod extends GuiScreen {

    public boolean running = true;
    private int mouseCount = 0;

    @Override
    public boolean func_73868_f() {
        return true;
    }

    public GuiRunMod() {
    }

    @Override
    public void func_73866_w_() {
        try {
            ModSimReloaded.log.info("初始化GUI");
            String not_run = I18n.func_135052_a("container.sim.not_run");
            String normal = I18n.func_135052_a("container.sim.normal");
            String creative = I18n.func_135052_a("container.sim.creative");
            String hardcore = I18n.func_135052_a("container.sim.hardcore");
            this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 75, 40, not_run));
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 75, 90, normal));
            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 75, 140, creative));
            this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 75, 190, hardcore));
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
            String sim_gui_game_mode = I18n.func_135052_a("container.sim.sim_gui_game_mode");
            String sim_gui_switches = I18n.func_135052_a("container.sim.sim_gui_switches");
            String sim_gui_beginners = I18n.func_135052_a("container.sim.sim_gui_beginners");
            String sim_gui_everything = I18n.func_135052_a("container.sim.sim_gui_everything");
            String sim_gui_Builders = I18n.func_135052_a("container.sim.sim_gui_Builders");
            this.func_73732_a(this.field_146289_q, sim_gui_game_mode, this.field_146294_l / 2, 20, 16777215);
            this.func_73732_a(this.field_146289_q, sim_gui_switches, this.field_146294_l / 2, 60, 16776960);
            this.func_73732_a(this.field_146289_q, sim_gui_beginners, this.field_146294_l / 2, 110, 16776960);
            this.func_73732_a(this.field_146289_q, sim_gui_everything, this.field_146294_l / 2, 160, 16776960);
            this.func_73732_a(this.field_146289_q, sim_gui_Builders, this.field_146294_l / 2, 210, 16776960);
            super.func_73863_a(i, j, f);
        } catch (Exception var5) {
            //ModSimReloaded.log.info("Caught Exception while drawing strings/screen");
            ModSimReloaded.log.warn("在绘制字符串/屏幕时捕获异常"+var5.getMessage());
        }
    }

    @Override
    protected void func_146284_a(GuiButton guibutton) {
        try {
            if (guibutton.field_146127_k == 0) {
                ModSimReloaded.states.gameModeNumber = 10;
                //ModSimReloaded.log.info("Turning off SimCity Reloaded");
                ModSimReloaded.log.info("关闭重新加载的模拟城市");
            } else if (guibutton.field_146127_k == 1) {
                ModSimReloaded.states.gameModeNumber = 0;
                //ModSimReloaded.log.info("Playing SimCity Reloaded in normal mode");
                ModSimReloaded.log.info("在正常模式下重新加载模拟城市");
                FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().func_130014_f_());
            } else if (guibutton.field_146127_k == 2) {
                ModSimReloaded.states.gameModeNumber = 1;
            } else if (guibutton.field_146127_k == 3) {
                ModSimReloaded.states.gameModeNumber = 2;
            }

            ModSimReloaded.states.saveStates();
            this.running = false;
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } catch (Exception e) {
            ModSimReloaded.log.error("actionPerformed出错了：" + e.getMessage());
        }

    }
}
