package com.trhsy.sim.client.gui;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;

public class SimMenuWarning extends GuiMainMenu {
    public SimMenuWarning() {
    }

    @Override
    public void initGui() {
        try {
            this.drawString(this.mc.fontRendererObj, I18n.format("container.sim.SimMenuWarning"), this.width / 2, this.height / 2, 16711680);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("SimMenuWarning-initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
