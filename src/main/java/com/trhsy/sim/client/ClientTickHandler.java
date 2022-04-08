package com.trhsy.sim.client;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.functionality.FarmingBox;
import com.trhsy.sim.common.block.functionality.MiningBox;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.util.GameMode;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.Random;

/**
 * ========================================
 *
 * @ClassName ClientTickHandler
 * @Description todo 客户报价处理程序
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:09
 * ========================================
 **/
public class ClientTickHandler {
    Minecraft mc = Minecraft.getMinecraft();
    GuiScreen hud = new GuiScreen();
    Long timeSinceLastSave = 0L;
    public static int beamingStage = 1;
    public static long beamingStartedAt = 0L;
    public static V3 beamingTo = null;
    public static EntityPlayer beamingPlayer = null;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void tick(WorldTickEvent event) {
        this.onTickInGame();
    }

    @SubscribeEvent
    public void tick(ClientTickEvent event) {
    }

    @SubscribeEvent
    public void tick(RenderTickEvent event) {
        this.onGui();
    }

    public void onTickInGame() {
        if (beamingTo != null) {
            this.beamingPlayer();
        }

        try {
            if (ModSimReloaded.states.gameModeNumber <= 0) {
                return;
            }
        } catch (Exception var3) {
        }

        if (this.mc.currentScreen != null && this.mc.currentScreen.toString().toLowerCase().contains("ingamemenu") && System.currentTimeMillis() - this.timeSinceLastSave > 10000L) {
            ConfigLoader.config.save();
            ModSimReloaded.states.saveStates();
            Building.saveAllBuildings();
            CourierTask.saveCourierTasksAndPoints();
            MiningBox.saveMiningBoxes();
            FarmingBox.saveFarmingBoxes();

            for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                FolkData folk = (FolkData) ModSimReloaded.theFolks.get(f);
                folk.updateLocationFromEntity();
                folk.saveThisFolk();
            }

            this.timeSinceLastSave = System.currentTimeMillis();
        }

    }

    public void onGui() {
        if (this.mc.currentScreen == null) {
            String worldname = "unknown";

            try {
                if (ModSimReloaded.states.gameModeNumber == 10) {
                    return;
                }

                worldname = this.mc.getIntegratedServer().getFolderName();
                worldname = MinecraftServer.getServer().getFolderName();
            } catch (Exception var4) {
                this.hud.drawString(this.mc.fontRendererObj, I18n.format("container.sim.trhsy2"), this.hud.width / 2, 2, 16777215);
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
                        this.hud.drawString(this.mc.fontRendererObj, worldname + " (" + ModSimReloaded.getDayOfWeek() + ") - " + I18n.format("container.sim.trhsy3") + ": " + ModSimReloaded.theFolks.size(), this.hud.width / 2, 2 + HUDoffset, 16777215);
                    } else {
                        this.hud.drawString(this.mc.fontRendererObj, worldname + " (" + ModSimReloaded.getDayOfWeek() + ") - " + I18n.format("container.sim.trhsy3") + ": " + ModSimReloaded.theFolks.size() + "   " + I18n.format("container.sim.trhsy4") + ": " + ModSimReloaded.displayMoney(ModSimReloaded.states.credits), this.hud.width / 2, 2 + HUDoffset, 16777215);
                    }
                } else {
                    this.hud.drawString(this.mc.fontRendererObj, I18n.format("container.sim.trhsy5"), this.hud.width / 2, 2, 16777215);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    private void beamingPlayer() {
        Minecraft mc = Minecraft.getMinecraft();
        Random random = new Random();
        beamingPlayer.motionX = 0.0D;
        beamingPlayer.motionY = 0.0D;
        beamingPlayer.motionZ = 0.0D;
        Double d4 = ((double)random.nextFloat() - 2.0D) * 2.0D;

        for(int p = 0; p < 20; ++p) {
            try {
                mc.theWorld.spawnParticle("portal", beamingPlayer.posX + random.nextDouble() - 0.5D, beamingPlayer.posY - 1.0D, beamingPlayer.posZ + random.nextDouble() - 0.5D, 0.0D, -d4, 0.0D);
            } catch (Exception var7) {
            }

            try {
                mc.theWorld.spawnParticle("portal", beamingTo.x + random.nextDouble() - 0.5D, beamingTo.y - 1.0D, beamingTo.z + random.nextDouble() - 0.5D, 0.0D, -d4, 0.0D);
            } catch (Exception var6) {
            }
        }

        if (beamingStage == 1) {
            if (System.currentTimeMillis() - beamingStartedAt > 6000L) {
                beamingStage = 2;
                beamingPlayer.setPositionAndUpdate(beamingTo.x, beamingTo.y, beamingTo.z);
            }
        } else if (beamingStage == 2) {
            beamingStage = 3;
        } else if (beamingStage == 3 && (System.currentTimeMillis() - beamingStartedAt > 10000L || beamingTo == null)) {
            beamingTo = null;
            beamingPlayer = null;
            return;
        }

    }

    public ClientTickHandler() {
    }

    public String getLabel() {
        return "ClientTickHandler";
    }
}
