package com.trhsy.sim.client;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.Random;

/**
 * ========================================
 *
 * @ClassName ClientTickHandler
 * @Description todo
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
            if (ModSim.states.gameModeNumber <= 0) {
                return;
            }
        } catch (Exception var3) {
        }

        if (this.mc.currentScreen != null && this.mc.currentScreen.toString().toLowerCase().contains("ingamemenu") && System.currentTimeMillis() - this.timeSinceLastSave > 10000L) {
            ModSim.config.save();
            ModSim.states.saveStates();
            Building.saveAllBuildings();
            CourierTask.saveCourierTasksAndPoints();
            MiningBox.saveMiningBoxes();
            FarmingBox.saveFarmingBoxes();

            for (int f = 0; f < ModSim.theFolks.size(); ++f) {
                FolkData folk = (FolkData) ModSim.theFolks.get(f);
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
                if (ModSim.states.gameModeNumber == 10) {
                    return;
                }

                worldname = this.mc.getIntegratedServer().getFolderName();
                worldname = MinecraftServer.getServer().getFolderName();
            } catch (Exception var4) {
                this.hud.drawString(this.mc.fontRendererObj, "SimCity is not SMP", this.hud.width / 2, 2, 16777215);
                return;
            }

            try {
                if (ModSim.proxy.ranStartup) {
                    int HUDoffset = 0;
                    if (this.mc.thePlayer.dimension == 1) {
                        HUDoffset = 20;
                    }

                    HUDoffset = HUDoffset + ModSim.configHUDoffset;
                    if (ModSim.gameMode == GameMode.CREATIVE) {
                        this.hud.drawString(this.mc.fontRendererObj, worldname + " (" + ModSim.getDayOfWeek() + ") - Population: " + ModSim.theFolks.size(), this.hud.width / 2, 2 + HUDoffset, 16777215);
                    } else {
                        this.hud.drawString(this.mc.fontRendererObj, worldname + " (" + ModSim.getDayOfWeek() + ") - Population: " + ModSim.theFolks.size() + "   Sim-U-credits: " + ModSim.displayMoney(ModSim.states.credits), this.hud.width / 2, 2 + HUDoffset, 16777215);
                    }
                } else {
                    this.hud.drawString(this.mc.fontRendererObj, "Loading SimCity...", this.hud.width / 2, 2, 16777215);
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
