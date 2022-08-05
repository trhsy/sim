package com.trhsy.sim.client.gui.other;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
@SideOnly(Side.CLIENT)
public class GuiRunMod extends GuiScreen {

    public boolean running = true;
    private int mouseCount = 0;

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    public GuiRunMod() {
    }

    @Override
    public void initGui() {
        try {
            ModSimReloaded.log.info("初始化GUI");
            String not_run = I18n.format("container.sim.not_run");
            String normal = I18n.format("container.sim.normal");
            String creative = I18n.format("container.sim.creative");
            String hardcore = I18n.format("container.sim.hardcore");
            this.buttonList.add(new GuiButton(0, this.width / 2 - 75, 40, not_run));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 75, 90, normal));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 75, 140, creative));
            this.buttonList.add(new GuiButton(3, this.width / 2 - 75, 190, hardcore));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiRunMod-initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            String sim_gui_game_mode = I18n.format("container.sim.sim_gui_game_mode");
            String sim_gui_switches = I18n.format("container.sim.sim_gui_switches");
            String sim_gui_beginners = I18n.format("container.sim.sim_gui_beginners");
            String sim_gui_everything = I18n.format("container.sim.sim_gui_everything");
            String sim_gui_Builders = I18n.format("container.sim.sim_gui_Builders");
            this.drawCenteredString(this.fontRendererObj, sim_gui_game_mode, this.width / 2, 20, 16777215);
            this.drawCenteredString(this.fontRendererObj, sim_gui_switches, this.width / 2, 60, 16776960);
            this.drawCenteredString(this.fontRendererObj, sim_gui_beginners, this.width / 2, 110, 16776960);
            this.drawCenteredString(this.fontRendererObj, sim_gui_everything, this.width / 2, 160, 16776960);
            this.drawCenteredString(this.fontRendererObj, sim_gui_Builders, this.width / 2, 210, 16776960);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimReloaded.log.warn("在绘制字符串/屏幕时捕获异常"+e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.id == 0) {
                ModSimReloaded.states.gameModeNumber = 10;
                //ModSimReloaded.log.info("Turning off SimCity Reloaded");
                ModSimReloaded.log.info("关闭重新加载的模拟城市");
            } else if (guibutton.id == 1) {
                ModSimReloaded.states.gameModeNumber = 0;
                //ModSimReloaded.log.info("Playing SimCity Reloaded in normal mode");
                ModSimReloaded.log.info("在正常模式下重新加载模拟城市");
                FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
            } else if (guibutton.id == 2) {
                ModSimReloaded.states.gameModeNumber = 1;
            } else if (guibutton.id == 3) {
                ModSimReloaded.states.gameModeNumber = 2;
            }

            ModSimReloaded.states.saveStates();
            this.running = false;
            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIRUNMOD-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
