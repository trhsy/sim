package com.trhsy.sim.loader;

import com.trhsy.sim.gui.GuiRunMod;
import com.trhsy.sim.network.GameStates;
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
    /*
       包含他们正在玩的这个关卡的所有游戏状态和设置
        */
    public static GameStates states = new GameStates();
    /**
     * 运行模组
     */
    public static void openSetupGui() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiRunMod());
    }
}
