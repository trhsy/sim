package com.trhsy.sim.gui;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.gui
 * @ClassName: GuiHud
 * @Description:
 * @date 2023/11/20 下午 3:03
 */
public class GuiHud extends GuiScreen {
    public GuiHud() {
    }
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    /**
     * 初始化
     */
    @Override
    public void initGui() {
        try {
            GuiScreen hud = new GuiHud();
            if (this.mc.currentScreen == null) {
                return;
            }
            String worldname = "unknown";
            try {
                if (ModSimClientLoader.gamemode== 999) {
                    return;
                }
                worldname = this.mc.getIntegratedServer().getFolderName();
            } catch (Exception var7) {
                worldname = "Server";
            }
            try {
                if(ModSimClientLoader.sim_is_running){
                    if (ModSimClientLoader.gamemode != 999) {
                        if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                            int HUDoffset = 0;
                            if (this.mc.player!=null&&mc.player.dimension == 1) {
                                HUDoffset = 20;
                            }

                            if (ModSimClientLoader.gamemode == 1) {
                                //世界名 人口
                                hud.drawString(this.mc.fontRenderer, worldname + " (" + ModSimClientLoader.dayOfWeek + ") - "+ new TextComponentTranslation("container.sim.trhsy3",new Object[0]).getUnformattedText() +": " + ModSimClientLoader.tempHireableNpcNames.size(), hud.width / 2, 2 + HUDoffset, 16777215);
                            } else {
                                //世界名  人口  资金
                                hud.drawString(this.mc.fontRenderer, worldname + " (" + ModSimClientLoader.dayOfWeek + ") - "+new TextComponentTranslation("container.sim.trhsy3",new Object[0]).getUnformattedText() +": " + ModSimClientLoader.tempHireableNpcNames.size() + "   "+ new TextComponentTranslation("container.sim.trhsy4",new Object[0]).getUnformattedText() +": " + ModSimLoader.displayMoney(ModSimClientLoader.money), hud.width / 2, 2 + HUDoffset, 16777215);
                            }
                        }
                    } else if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                        //正在加载模拟城市...
                        hud.drawString(this.mc.fontRenderer, new TextComponentTranslation("container.sim.trhsy5",new Object[0]).getUnformattedText(), hud.width / 2, 2, 16777215);
                    }
                }else{
                    //正在加载模拟城市...
                    hud.drawString(this.mc.fontRenderer, new TextComponentTranslation("container.sim.trhsy5",new Object[0]).getUnformattedText(), hud.width / 2, 2, 16777215);
                }

            } catch (Exception e) {
                StackTraceElement element = e.getStackTrace()[0];
                ModSimLoader.log.error("renderTick出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiHud-initGui出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
}
