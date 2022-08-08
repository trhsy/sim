package com.trhsy.sim.common.gui;

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Mouse;

public class GuiRunMod extends GuiScreen {
    //运行
    public boolean running = true;
    //鼠标计数
    private int mouseCount = 0;

    @Override
    public boolean func_73868_f() {
        return true;
    }

    public GuiRunMod() {
    }

    /**
     * 初始化
     */
    @Override
    public void func_73866_w_() {
        try {
            ModSimReloaded.log.info("初始化GUI");
            //不运行模拟城镇
            String not_run = I18n.func_135052_a("container.sim.not_run");
            //正常模式
            String normal = I18n.func_135052_a("container.sim.normal");
            //创造模式
            String creative = I18n.func_135052_a("container.sim.creative");
            //专家模式
            String hardcore = I18n.func_135052_a("container.sim.hardcore");
            this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 75, 40, not_run));
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 75, 90, normal));
            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 75, 140, creative));
            this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 75, 190, hardcore));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiRunMod-initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 绘制屏幕
     *
     * @param i
     * @param j
     * @param f
     */
    @Override
    public void func_73863_a(int i, int j, float f) {
        try {

            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }
            //默认背景
            this.func_146276_q_();
            //请选择模拟城镇的游戏模式
            String sim_gui_game_mode = I18n.func_135052_a("container.sim.sim_gui_game_mode");
            //这个模式会关闭这个世界的模拟城市
            String sim_gui_switches = I18n.func_135052_a("container.sim.sim_gui_switches");
            //非常适合初学者和专家。不太有挑战性。
            String sim_gui_beginners = I18n.func_135052_a("container.sim.sim_gui_beginners");
            //不需要钱,一切免费,不需要方块,要有创意！
            String sim_gui_everything = I18n.func_135052_a("container.sim.sim_gui_everything");
            //建设者需要所有的方块,更难玩游戏
            String sim_gui_Builders = I18n.func_135052_a("container.sim.sim_gui_Builders");
            this.func_73732_a(this.field_146289_q, sim_gui_game_mode, this.field_146294_l / 2, 20, 16777215);
            this.func_73732_a(this.field_146289_q, sim_gui_switches, this.field_146294_l / 2, 60, 16776960);
            this.func_73732_a(this.field_146289_q, sim_gui_beginners, this.field_146294_l / 2, 110, 16776960);
            this.func_73732_a(this.field_146289_q, sim_gui_everything, this.field_146294_l / 2, 160, 16776960);
            this.func_73732_a(this.field_146289_q, sim_gui_Builders, this.field_146294_l / 2, 210, 16776960);
            super.func_73863_a(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimReloaded.log.warn("在绘制字符串/屏幕时捕获异常" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 已执行的操作
     *
     * @param guibutton
     */
    @Override
    protected void func_146284_a(GuiButton guibutton) {
        try {
            switch (guibutton.field_146127_k) {
                case 0:
                    //不运行模拟城镇 按超过10次
                    ModSimReloaded.states.gameModeNumber = 10;
                    ModSimReloaded.log.info("关闭重新加载的模拟城市");
                    break;
                case 1:
                    ModSimReloaded.states.gameModeNumber = 0;
                    ModSimReloaded.log.info("在正常模式下重新加载模拟城市");
                    FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().func_130014_f_());
                    break;
                case 2:
                    ModSimReloaded.states.gameModeNumber = 1;
                    break;
                case 3:
                    ModSimReloaded.states.gameModeNumber = 2;
                    break;
                default:
                    ModSimReloaded.states.gameModeNumber = 0;
                    ModSimReloaded.log.info("在正常模式下重新加载模拟城市");
                    //生成一个新的NPC
                    FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().func_130014_f_());
                    break;
            }
            ModSimReloaded.states.saveStates();
            this.running = false;
            //当前屏幕为空
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiRunMod-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
