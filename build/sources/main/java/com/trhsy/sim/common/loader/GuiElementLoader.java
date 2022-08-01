package com.trhsy.sim.common.loader;

import com.trhsy.sim.ModSim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 *
 */
public class GuiElementLoader implements IGuiHandler {
    public static final int GUI_DEMO = 1;

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
     * @param entityPlayer
     * @param world
     * @param i1
     * @param i2
     * @param i3
     * @return
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        try {
            switch (ID) {
                case GUI_DEMO:
                    //return new ContainerDemo();
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
     * @param entityPlayer
     * @param world
     * @param i1
     * @param i2
     * @param i3
     * @return
     */
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        try {
            switch (ID) {
                case GUI_DEMO:
                    //return new ContainerDemo();
                default:
                    return null;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getClientGuiElement出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }
}
