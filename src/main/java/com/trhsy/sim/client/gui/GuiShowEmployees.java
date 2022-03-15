package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

/**
 * ========================================
 *
 * @ClassName GuiShowEmployees
 * @Description todo 展示员工
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:36
 * ========================================
 **/
public class GuiShowEmployees extends GuiScreen {
    public boolean running = true;
    private int mouseCount = 0;

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    public GuiShowEmployees() {
        // TODO document why this constructor is empty
    }

    @Override
    public void initGui() {
        ModSim.log.info("初始化GUI");
        this.buttonList.add(new GuiButton(0, this.width / 2 - 75, 40, I18n.format("container.sim.ShowEmployees1")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 75, 90, I18n.format("container.sim.ShowEmployees2")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 75, 140, I18n.format("container.sim.ShowEmployees3")));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 75, 190, I18n.format("container.sim.ShowEmployees4")));
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees5"), this.width / 2, 20, 16777215);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees6"), this.width / 2, 60, 16776960);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees7"), this.width / 2, 110, 16776960);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees8"), this.width / 2, 160, 16776960);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.ShowEmployees9"), this.width / 2, 210, 16776960);
        } catch (Exception var5) {
            ModSim.log.error("在绘制字符串/屏幕时捕获异常：" + var5.getMessage());
        }

        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 0) {
            ModSim.states.gameModeNumber = 10;
            ModSim.log.info("关闭重新加载的模拟城市");
        } else if (guibutton.id == 1) {
            ModSim.states.gameModeNumber = 0;
            ModSim.log.info("在正常模式下重新加载模拟城市");
            FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        } else if (guibutton.id == 2) {
            ModSim.states.gameModeNumber = 1;
        } else if (guibutton.id == 3) {
            ModSim.states.gameModeNumber = 2;
        }

        ModSim.states.saveStates();
        this.running = false;
        this.mc.currentScreen = null;
        this.mc.setIngameFocus();
    }
}

