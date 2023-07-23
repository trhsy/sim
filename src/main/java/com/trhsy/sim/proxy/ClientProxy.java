package com.trhsy.sim.proxy;

import com.trhsy.sim.gui.GuiHud;
import com.trhsy.sim.loader.EntityLoader;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenHudGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
        //丨 模拟城市 丨 官方Q群: 749090174  丨 由TRHSY重制 丨 微信公众号：dasha500
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
//        if(mc.thePlayer!=null){
//            NetWorkLoader.net.sendToAllAround(new PacketOpenHudGui(),new NetworkRegistry.TargetPoint(mc.thePlayer.dimension,mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ,99999));
//        }
        try {
            GuiScreen hud = new GuiHud();
//            ModSimLoader.log.info("初始化GUI");
            if (mc.currentScreen == null) {
                String worldname = "unknown";
                try {
                    if (ModSimClientLoader.gamemode== 999) {
                        return;
                    }
                    worldname = mc.getIntegratedServer().getFolderName();
                } catch (Exception var7) {
                    worldname = "Server";
                }
                try {
                    if (ModSimClientLoader.gamemode != 999) {
                        if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                            int HUDoffset = 0;
                            if (mc.thePlayer.dimension == 1) {
                                HUDoffset = 20;
                            }

                            if (ModSimClientLoader.gamemode == 1) {
                                //世界名 人口
                                hud.drawString(mc.fontRendererObj, worldname + " (" + ModSimClientLoader.dayOfWeek + ") - "+ I18n.format("container.sim.trhsy3") +": " + ModSimClientLoader.tempHireableNpcNames.size(), hud.width / 2, 2 + HUDoffset, 16777215);
                            } else {
                                //世界名  人口  资金
                                hud.drawString(mc.fontRendererObj, worldname + " (" + ModSimClientLoader.dayOfWeek + ") - "+I18n.format("container.sim.trhsy3") +": " + ModSimClientLoader.tempHireableNpcNames.size() + "   "+ I18n.format("container.sim.trhsy4") +": " + ModSimLoader.displayMoney(ModSimLoader.money), hud.width / 2, 2 + HUDoffset, 16777215);
                            }
                        }
                    } else if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                        //正在加载模拟城市...
                        hud.drawString(mc.fontRendererObj, I18n.format("container.sim.trhsy5"), hud.width / 2, 2, 16777215);
                    }
                } catch (Exception e) {
                    StackTraceElement element = e.getStackTrace()[0];
                    ModSimLoader.log.error("renderTick出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiHud-initGui出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
}
