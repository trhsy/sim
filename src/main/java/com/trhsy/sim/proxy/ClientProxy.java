package com.trhsy.sim.proxy;

import com.trhsy.sim.gui.GuiHud;
import com.trhsy.sim.loader.*;
import com.trhsy.sim.loader.render.ItemRenderLoader;
import com.trhsy.sim.util.BuildingsExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.proxy
 * @ClassName: ClientProxy
 * @Description: 客户端
 * @date 2023/10/19 下午 2:42
 */
public class ClientProxy extends CommonProxy {


    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        new BuildingsExtractor();
        /**配置**/
        ConfigLoader.load(event);
        /**方块对应物品的渲染**/
        new ItemRenderLoader();
        //加载实体
        EntityLoader.initModels();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        //丨 模拟城市 丨 官方Q群: 749090174  丨 由TRHSY重制 丨 微信公众号：dasha500
        String title = new TextComponentTranslation("container.sim.title", new Object[0]).getUnformattedText();
        Display.setTitle(Display.getTitle() + title);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void renderTick(TickEvent.RenderTickEvent renderTickEvent) {
        Minecraft mc=Minecraft.getMinecraft();
        try {
            GuiScreen hud = new GuiHud();
            if (mc!= null && mc.world!=null&&mc.world.isRemote) {
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
                    if(ModSimClientLoader.sim_is_running){
                        if (ModSimClientLoader.gamemode != 999) {
                            if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                                int HUDoffset = 0;
                                if (mc.player!=null&&mc.player.dimension == 1) {
                                    HUDoffset = 20;
                                }

                                if (ModSimClientLoader.gamemode == 1) {
                                    //世界名 人口
                                    hud.drawString(mc.fontRenderer, worldname + new TextComponentTranslation("container.sim.simday_of_week",new Object[0]).getUnformattedText()+" (" + ModSimClientLoader.dayOfWeek + ") - "+ new TextComponentTranslation("container.sim.trhsy3",new Object[0]).getUnformattedText() +": " + ModSimClientLoader.tempHireableNpcNames.size(), hud.width / 2, 2 + HUDoffset, 16777215);
                                } else {
                                    //世界名  人口  资金
                                    hud.drawString(mc.fontRenderer, worldname +  new TextComponentTranslation("container.sim.simday_of_week",new Object[0]).getUnformattedText()+" (" + ModSimClientLoader.dayOfWeek + ") - "+new TextComponentTranslation("container.sim.trhsy3",new Object[0]).getUnformattedText() +": " + ModSimClientLoader.tempHireableNpcNames.size() + "   "+ new TextComponentTranslation("container.sim.trhsy4",new Object[0]).getUnformattedText() +": " + ModSimLoader.displayMoney(ModSimClientLoader.money), hud.width / 2, 2 + HUDoffset, 16777215);
                                }
                            }
                        } else if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                            //正在加载模拟城市...
                            hud.drawString(mc.fontRenderer, new TextComponentTranslation("container.sim.trhsy5",new Object[0]).getUnformattedText(), hud.width / 2, 2, 16777215);
//                            SimmodeStart.simModupdate();
                        }
                    }else{
                        //正在加载模拟城市...
                        hud.drawString(mc.fontRenderer, new TextComponentTranslation("container.sim.trhsy5",new Object[0]).getUnformattedText(), hud.width / 2, 2, 16777215);
//                        if (ModSimClientLoader.gamemode != 999) {
//                            SimmodeStart.simModupdate();
//                        }
                    }

                } catch (Exception e) {
                    StackTraceElement element = e.getStackTrace()[0];
                    ModSimLoader.log.error("renderTick出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
                }
            }else{
                return;
            }


        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiHud-initGui出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
}
