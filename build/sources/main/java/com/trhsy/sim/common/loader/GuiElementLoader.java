package com.trhsy.sim.common.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.gui.blocks.GuiControlBox;
import com.trhsy.sim.common.core.entity.V3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 *
 */
public class GuiElementLoader implements IGuiHandler {
    public static final int GUI_CONTROL_SID = 1;

    /**
     * 注册GUI
     */
    public GuiElementLoader() {
        try {
            // 模组实例，IGuiHandler本身
            NetworkRegistry.INSTANCE.registerGuiHandler(ModSim.instance, this);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiElementLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 服务器端 GUI
     *
     * @param ID 用于判断打开那个GUI的编码
     * @param thePlayer
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer thePlayer, World world, int x, int y, int z) {
        try {
            switch (ID) {
                case GUI_CONTROL_SID:
                    return new GuiControlBox(new V3(x,y,z, thePlayer.dimension), thePlayer);
                default:
                    return null;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getServerGuiElement出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    /**
     * 客户端 GUI
     *
     * @param ID 用于判断打开那个GUI的编码
     * @param thePlayer
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer thePlayer, World world, int x, int y, int z) {
        try {
            switch (ID) {
                case GUI_CONTROL_SID:
                    return new GuiControlBox(new V3(x,y,z, thePlayer.dimension), thePlayer);
                default:
                    return null;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getClientGuiElement出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }
}
