package com.trhsy.sim.common.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;

public class SimMenuWarning extends GuiMainMenu {
    public SimMenuWarning() {
    }

    @Override
    public void initGui() {
        this.drawString(this.mc.fontRendererObj, I18n.format("container.sim.SimMenuWarning"), this.width / 2, this.height / 2, 16711680);
    }
}
