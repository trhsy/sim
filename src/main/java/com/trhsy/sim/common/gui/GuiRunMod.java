package com.trhsy.sim.common.gui;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
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
    public boolean doesGuiPauseGame() {
        return true;
    }

    public GuiRunMod() {
    }

    /**
     * 初始化
     */
    @Override
    public void initGui() {
        ModSimReloaded.log.info("初始化GUI");
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
        } catch (Exception var5) {
            ModSimReloaded.log.warn("在绘制字符串/屏幕时捕获异常" + var5.getMessage());
        }

        super.drawScreen(i, j, f);
    }

    /**
     * 已执行的操作
     *
     * @param guibutton
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        switch (guibutton.id) {
            case 0:
                //不运行模拟城镇 按超过10次
                ModSimReloaded.states.gameModeNumber = 10;
                ModSimReloaded.log.info("关闭重新加载的模拟城市");
                break;
            case 1:
                ModSimReloaded.states.gameModeNumber = 0;
                ModSimReloaded.log.info("在正常模式下重新加载模拟城市");
                FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
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
                FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
                break;
        }
        ModSimReloaded.states.saveStates();
        this.running = false;
        //当前屏幕为空
        this.mc.currentScreen = null;
        this.mc.setIngameFocus();
    }
}
