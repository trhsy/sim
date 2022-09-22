package com.trhsy.sim.loader;

import com.trhsy.sim.gui.GuiRunMod;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

/**
 * sim 加载信息
 * @author Administrator
 */
public class ModSimLoader {
    /**
     * 全局日志调用
     */
    public static Logger log;

    /**
     * 运行模组
     */
    public static void openSetupGui() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiRunMod());
    }
}
