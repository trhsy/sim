package com.trhsy.sim.gui;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.server.PacketSetupMod;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
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
            if(page==0){
                this.buttonList.clear();
                //不接受
                String not_accept = I18n.format("container.sim.not_accept");
                //接受
                String accept = I18n.format("container.sim.accept");
                //接受
                this.buttonList.add(new GuiButton(4, 160, 140,50,20, accept));
                //不接受
                this.buttonList.add(new GuiButton(5, 240, 140,50,20, not_accept));
            }else{
                this.buttonList.clear();
                //不运行模拟城镇
                String not_run = I18n.format("container.sim.not_run");
                //正常模式
                String normal = I18n.format("container.sim.normal");
                //创造模式
                String creative = I18n.format("container.sim.creative");
                //专家模式
//                String hardcore = I18n.format("container.sim.hardcore");
                this.buttonList.add(new GuiButton(0, this.width / 2 - 75, 40, not_run));
                this.buttonList.add(new GuiButton(1, this.width / 2 - 75, 80, normal));
                this.buttonList.add(new GuiButton(2, this.width / 2 - 75, 120, creative));
//                this.buttonList.add(new GuiButton(3, this.width / 2 - 75, 190, hardcore));
            }

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
            if(page==0){
                //任命书
                String sim_gui_rms = I18n.format("container.sim.sim_gui_rms");
                this.drawCenteredString(this.fontRendererObj, sim_gui_rms, this.width / 2, 17, 16777215);
                //冒险家，当你厌倦了冒险后，你可以尝试接受这个委任书。
                //它可以帮你成为这个城市唯一的话语人，你可以雇佣NPC为你工作，
                //他们可以成为建筑师，规划师，面包师，农民，屠夫，牧羊人，养鸡农民，
                //养牛人，士兵，玻璃制造商，板砖工人等等很多职业。帮助你为你的城市添砖加瓦，
                //出一份力，最初你只有10点资金，想要更好的发展城市，剩下的只能你自己想办法了，加油冒险家！
                String sim_gui_zw0 = I18n.format("container.sim.sim_gui_zw0");
                String sim_gui_zw1 = I18n.format("container.sim.sim_gui_zw1");
                String sim_gui_zw2 = I18n.format("container.sim.sim_gui_zw2");
                String sim_gui_zw3 = I18n.format("container.sim.sim_gui_zw3");
                String sim_gui_zw4= I18n.format("container.sim.sim_gui_zw4");

                this.drawCenteredString(this.fontRendererObj, sim_gui_zw0, this.width / 2, 60, 16776960);
                this.drawCenteredString(this.fontRendererObj, sim_gui_zw1, this.width / 2, 70, 16776960);
                this.drawCenteredString(this.fontRendererObj, sim_gui_zw2, this.width / 2, 80, 16776960);
                this.drawCenteredString(this.fontRendererObj, sim_gui_zw3, this.width / 2, 90, 16776960);
                this.drawCenteredString(this.fontRendererObj, sim_gui_zw4, this.width / 2, 100, 16776960);
            }else{
                //请选择模拟城镇的游戏模式
                String sim_gui_game_mode = I18n.format("container.sim.sim_gui_game_mode");
                //这个模式会关闭这个世界的模拟城市
                String sim_gui_switches = I18n.format("container.sim.sim_gui_switches");
                //非常适合初学者和专家。不太有挑战性。
                String sim_gui_beginners = I18n.format("container.sim.sim_gui_beginners");
                //不需要钱,一切免费,不需要方块,要有创意！
                String sim_gui_everything = I18n.format("container.sim.sim_gui_everything");
                //建设者需要所有的方块,更难玩游戏
//                String sim_gui_Builders = I18n.format("container.sim.sim_gui_Builders");
                this.drawCenteredString(this.fontRendererObj, sim_gui_game_mode, this.width / 2, 20, 16777215);
                this.drawCenteredString(this.fontRendererObj, sim_gui_switches, this.width / 2, 60, 16776960);
                this.drawCenteredString(this.fontRendererObj, sim_gui_beginners, this.width / 2, 110, 16776960);
                this.drawCenteredString(this.fontRendererObj, sim_gui_everything, this.width / 2, 160, 16776960);
//                this.drawCenteredString(this.fontRendererObj, sim_gui_Builders, this.width / 2, 210, 16776960);
            }

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
            Minecraft fs_mc = Minecraft.getMinecraft();
            switch (guibutton.id) {
                case 0:
                    this.page=0;
                    //不运行模拟城镇 按超过10次
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();
                    ModSimLoader.log.info("关闭重新加载的模拟城市");
                    break;
                case 1:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(0));
                    this.page=0;
                    ModSimClientLoader.gamemode = 0;
                    ModSimLoader.log.info("在正常模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    //ModSimLoader.log.info("在创造模式下重新加载模拟城市，开始生成新的NPC");
                    //new NpcData(fs_mc.theWorld, true);
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();

                    break;
                case 2:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(1));
                    this.page=0;
                    ModSimClientLoader.gamemode = 1;
                    ModSimLoader.log.info("在创造模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    //ModSimLoader.log.info("在创造模式下重新加载模拟城市，开始生成新的NPC");
                    //new NpcData(fs_mc.theWorld, true);
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();
                    break;
                case 3:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(2));
                    this.page=0;
                    ModSimClientLoader.gamemode = 2;
                    ModSimLoader.log.info("在专业模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    //ModSimLoader.log.info("在专业模式下重新加载模拟城市，开始生成新的NPC");
                    //new NpcData(fs_mc.theWorld, true);
                    this.mc.currentScreen=null;
                    this.mc.setIngameFocus();
//                    this.updateScreen();
//                    this.initGui();
                    break;
                case 4:
                    ModSimLoader.log.info("接受任命书");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.page=1;
//                    this.mc.currentScreen=null;
//                    if(ModSimClientLoader.gamemode!=999){
//                        this.mc.setIngameFocus();
//                    }else{
                        this.updateScreen();
                        this.initGui();
                    //}
                    break;
                case 5:
                    ModSimLoader.log.info("不接受任命书");
                    this.page=0;
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.mc.currentScreen=null;
                    //ModSimLoader.log.info("所有人都有住宅，开始生成新的NPC");
                    //new NpcData(fs_mc.theWorld, true);
                    this.mc.setIngameFocus();
                    break;
                default:
                    NetWorkLoader.net.sendToServer(new PacketSetupMod(0));
                    this.page=0;
                    ModSimLoader.log.info("在正常模式下重新加载模拟城市");
                    this.buttonList.get(0).visible=false;
                    this.buttonList.get(1).visible=false;
                    this.buttonList.get(2).visible=false;
                    //ModSimLoader.log.info("在正常模式下重新加载模拟城市，开始生成新的NPC");
                    //new NpcData(fs_mc.theWorld, true);
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
