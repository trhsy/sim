package com.trhsy.sim.gui;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenSetupGui;
import com.trhsy.sim.network.server.PacketSetupMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Mouse;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.gui
 * @ClassName: GuiRunMod
 * @Description: 启动
 * @date 2022/9/22 0022 下午 3:27
 */
public class GuiRunMod extends GuiScreen {
    //运行
    public boolean running = true;
    //鼠标计数
    private int mouseCount = 0;
    //当前页
    public int page = 0;
    public GuiRunMod() {
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    /**
     * 初始化
     */
    @Override
    public void initGui() {
        try {
            ModSimLoader.log.info("初始化GUI");
            //不运行模拟城镇
            String not_run = I18n.format("container.sim.not_run");
            //正常模式
            String normal = I18n.format("container.sim.normal");
            //创造模式
            String creative = I18n.format("container.sim.creative");
            //专家模式
            String hardcore = I18n.format("container.sim.hardcore");
            this.buttonList.add(new GuiButton(0, this.width / 2 - 75, 40, not_run));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 75, 90, normal));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 75, 140, creative));
            this.buttonList.add(new GuiButton(3, this.width / 2 - 75, 190, hardcore));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("GuiRunMod-initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
    public void drawScreen(int i, int j, float f) {
        try {

            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }
            //默认背景
            this.drawDefaultBackground();
            //请选择模拟城镇的游戏模式
            String sim_gui_game_mode = I18n.format("container.sim.sim_gui_game_mode");
            //这个模式会关闭这个世界的模拟城市
            String sim_gui_switches = I18n.format("container.sim.sim_gui_switches");
            //非常适合初学者和专家。不太有挑战性。
            String sim_gui_beginners = I18n.format("container.sim.sim_gui_beginners");
            //不需要钱,一切免费,不需要方块,要有创意！
            String sim_gui_everything = I18n.format("container.sim.sim_gui_everything");
            //建设者需要所有的方块,更难玩游戏
            String sim_gui_Builders = I18n.format("container.sim.sim_gui_Builders");
            this.drawCenteredString(this.fontRendererObj, sim_gui_game_mode, this.width / 2, 20, 16777215);
            this.drawCenteredString(this.fontRendererObj, sim_gui_switches, this.width / 2, 60, 16776960);
            this.drawCenteredString(this.fontRendererObj, sim_gui_beginners, this.width / 2, 110, 16776960);
            this.drawCenteredString(this.fontRendererObj, sim_gui_everything, this.width / 2, 160, 16776960);
            this.drawCenteredString(this.fontRendererObj, sim_gui_Builders, this.width / 2, 210, 16776960);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.warn("在绘制字符串/屏幕时捕获异常" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * 已执行的操作
     *
     * @param guibutton
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        try {
            switch (guibutton.id) {
                case 0:
                    //不运行模拟城镇 按超过10次
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();
                    ModSimLoader.log.info("关闭重新加载的模拟城市");
                    break;
                case 1:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(0));
                    ModSimLoader.log.info("在正常模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();

                    break;
                case 2:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(1));
                    ModSimLoader.log.info("在创造模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();
                    break;
                case 3:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(2));
                    ModSimLoader.log.info("在专业模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();
                    break;
                default:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(0));
                    ModSimLoader.log.info("在正常模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();
                    break;
            }
//            ModSimLoader.states.saveStates();
//            this.running = false;
            //当前屏幕为空
//            this.mc.currentScreen = null;
//            this.mc.setIngameFocus();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("GuiRunMod-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
