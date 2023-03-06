package com.trhsy.sim.proxy;

import com.trhsy.sim.gui.GuiHud;
import com.trhsy.sim.loader.EntityLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

/**
 * 客户端代理
 */
public class ClientProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        EntityLoader.initModels();
    }
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        String title= I18n.format("container.sim.title");
        Display.setTitle(Display.getTitle() +title);
    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void renderTick(TickEvent.RenderTickEvent renderTickEvent){
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen hud = new GuiHud();
        if (mc.currentScreen == null) {
            String worldname = "unknown";
            try {
                if (ModSimLoader.states.gameModeNumber== 999) {
                    return;
                }
                worldname = mc.getIntegratedServer().getFolderName();
            } catch (Exception var7) {
                worldname = "Server";
            }
            try {
                if (ModSimLoader.states.gameModeNumber != 999) {
                    if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                        int HUDoffset = 0;
                        if (mc.thePlayer.dimension == 1) {
                            HUDoffset = 20;
                        }

                        if (ModSimLoader.states.gameModeNumber == 1) {
                            hud.drawString(mc.fontRendererObj, worldname + " (" + ModSimLoader.states.getDayOfWeek() + ") - "+I18n.format("container.sim.trhsy3") +": " + ModSimLoader.tempHireableNpcNames.size(), hud.width / 2, 2 + HUDoffset, 16777215);
                        } else {
                            hud.drawString(mc.fontRendererObj, worldname + " (" + ModSimLoader.states.getDayOfWeek() + ") - "+I18n.format("container.sim.trhsy3") +": " + ModSimLoader.tempHireableNpcNames.size() + "   "+ I18n.format("container.sim.trhsy4") +": " + ModSimLoader.displayMoney(ModSimLoader.states.credits), hud.width / 2, 2 + HUDoffset, 16777215);
                        }
                    }
                } else if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                    hud.drawString(mc.fontRendererObj, I18n.format("container.sim.trhsy5"), hud.width / 2, 2, 16777215);
                }
            } catch (Exception e) {
                StackTraceElement element = e.getStackTrace()[0];
                ModSimLoader.log.error("renderTick出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            }
        }
    }
}
