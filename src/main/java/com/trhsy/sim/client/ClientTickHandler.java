package com.trhsy.sim.client;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.*;
import com.trhsy.sim.common.core.entity.functionality.FarmingBox;
import com.trhsy.sim.common.core.entity.functionality.MiningBox;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;


import java.util.Random;

/**
 * 客户端的
 */

public class ClientTickHandler extends GuiScreen{
    public ClientTickHandler() {
    }
    Minecraft mc = Minecraft.getMinecraft();
    Long timeSinceLastSave = 0L;
    public static int beamingStage = 1;
    public static long beamingStartedAt = 0L;
    public static V3 beamingTo = null;
    public static EntityPlayer beamingPlayer = null;


    @SubscribeEvent
    public void tick(WorldTickEvent event) {

    }

    @SubscribeEvent
    public void tick(ClientTickEvent event) {
        try {
            this.onTickInGame();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("客户端tick出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @SubscribeEvent
    public void tick(RenderTickEvent event) {
        this.onGui();
    }
    public void onTickInGame() {
        try {
            if (this.mc.currentScreen != null) {
                if (this.mc.currentScreen.toString().toLowerCase().contains("guimainmenu")) {
//                    ModSimReloaded.log.info("ClientTH: 在Gui主菜单中");
                }
            }
            if (beamingTo != null) {
                this.beamingPlayer();
            }
            if (ModSimReloaded.states.gameModeNumber <= 0) {
                ModSim.proxy.ranStartup = true;
//                return;
            }
            if (mc.currentScreen != null) {
                if (mc.currentScreen.toString().toLowerCase().contains("ingamemenu")) {
                    if (System.currentTimeMillis() - timeSinceLastSave > 10000) {
                        Long start = System.currentTimeMillis();
                        ModSimReloaded.states.saveStates();
                        Building.checkTenants();
                        Building.saveAllBuildings();
                        CourierTask.saveCourierTasksAndPoints();
                        MiningBox.saveMiningBoxes();
                        FarmingBox.saveFarmingBoxes();
                        Relationship.saveRelationships();
                        ModSimReloaded.log.info("CTH: 将游戏数据保存在 " + (System.currentTimeMillis() - start) + " ms");

                        this.timeSinceLastSave = System.currentTimeMillis();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("客户端onTickInGame出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }
    public void onGui() {
        if (this.mc.currentScreen == null) {
            String worldname = "unknown";

            try {
                if (ModSimReloaded.states.gameModeNumber == 10) {
                    return;
                }

//                worldname = this.mc.getIntegratedServer().getFolderName();
                worldname = MinecraftServer.getServer().getFolderName();
            } catch (Exception var4) {
                this.drawString(this.mc.fontRendererObj, I18n.format("container.sim.trhsy2"), this.width / 2, 2, 16777215);
                return;
            }

            try {
                if (ModSim.proxy.ranStartup) {
                    int HUDoffset = 0;
                    if (this.mc.thePlayer.dimension == 1) {
                        HUDoffset = 20;
                    }

                    HUDoffset = HUDoffset + ConfigLoader.configHUDoffset;
                    if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                        this.drawString(this.mc.fontRendererObj, worldname + " (" + ModSimReloaded.getDayOfWeek() + ") - " + I18n.format("container.sim.trhsy3") + ": " + ModSimReloaded.theFolks.size(), this.width / 2, 2 + HUDoffset, 16777215);
                    } else {
                        this.drawString(this.mc.fontRendererObj, worldname + " (" + ModSimReloaded.getDayOfWeek() + ") - " + I18n.format("container.sim.trhsy3") + ": " + ModSimReloaded.theFolks.size() + "   " + I18n.format("container.sim.trhsy4") + ": " + ModSimReloaded.displayMoney(ModSimReloaded.states.credits), this.width / 2, 2 + HUDoffset, 16777215);
                    }
                } else {
                    this.drawString(this.mc.fontRendererObj, I18n.format("container.sim.trhsy5"), this.width / 2, 2, 16777215);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }
    private void beamingPlayer() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            Random random = new Random();
            beamingPlayer.motionX = 0;
            beamingPlayer.motionY = 0;
            beamingPlayer.motionZ = 0;
            Double d4 = ((double) random.nextFloat() - 2) * 2;

            for (int p = 0; p < 20; ++p) {
                try {
                    mc.theWorld.spawnParticle(EnumParticleTypes.PORTAL, beamingPlayer.posX + random.nextDouble() - 0.5D, beamingPlayer.posY - 1, beamingPlayer.posZ + random.nextDouble() - 0.5D, 0, -d4, 0);
                } catch (Exception e) {
                }

                try {
                    mc.theWorld.spawnParticle(EnumParticleTypes.PORTAL, beamingTo.xCoord + random.nextDouble() - 0.5D, beamingTo.yCoord - 1, beamingTo.zCoord + random.nextDouble() - 0.5D, 0, -d4, 0);
                } catch (Exception e) {
                }
            }

            if (beamingStage == 1) {
                if (System.currentTimeMillis() - beamingStartedAt > 6000L) {
                    beamingStage = 2;
                    beamingPlayer.setPositionAndUpdate(beamingTo.xCoord, beamingTo.yCoord, beamingTo.zCoord);
                }
            } else if (beamingStage == 2) {
                beamingStage = 3;
            } else if (beamingStage == 3 && (System.currentTimeMillis() - beamingStartedAt > 10000L || beamingTo == null)) {
                beamingTo = null;
                beamingPlayer = null;
                return;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("客户端beamingPlayer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public String getLabel() {
        return "ClientTickHandler";
    }
}
