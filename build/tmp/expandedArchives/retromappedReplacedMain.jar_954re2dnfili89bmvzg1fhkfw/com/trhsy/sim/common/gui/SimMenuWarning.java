package com.trhsy.sim.common.gui;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;

public class SimMenuWarning extends GuiMainMenu {
    public SimMenuWarning() {
    }

    @Override
    public void func_73866_w_() {
        try {
            this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("container.sim.SimMenuWarning"), this.field_146294_l / 2, this.field_146295_m / 2, 16711680);
        } catch (Exception e) {
            ModSimReloaded.log.error("initGui出错了：" + e.getMessage());
        }
    }
}
