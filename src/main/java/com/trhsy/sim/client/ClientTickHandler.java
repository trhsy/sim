package com.trhsy.sim.client;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
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
            if (ModSimukraft.states.gameModeNumber <= 0) {
                return;
            }
        } catch (Exception var3) {
        }

        if (this.mc.field_71462_r != null && this.mc.field_71462_r.toString().toLowerCase().contains("ingamemenu") && System.currentTimeMillis() - this.timeSinceLastSave > 10000L) {
            ModSimukraft.config.save();
            ModSimukraft.states.saveStates();
            Building.saveAllBuildings();
            CourierTask.saveCourierTasksAndPoints();
            MiningBox.saveMiningBoxes();
            FarmingBox.saveFarmingBoxes();

            for(int f = 0; f < ModSimukraft.theFolks.size(); ++f) {
                FolkData folk = (FolkData)ModSimukraft.theFolks.get(f);
                folk.updateLocationFromEntity();
                folk.saveThisFolk();
            }

            this.timeSinceLastSave = System.currentTimeMillis();
        }

    }

    public void onGui() {
        if (this.mc.field_71462_r == null) {
            String worldname = "unknown";

            try {
                if (ModSimukraft.states.gameModeNumber == 10) {
                    return;
                }

                worldname = this.mc.func_71401_C().getFolderName();
                worldname = MinecraftServer.getServer().getFolderName();
            } catch (Exception var4) {
                this.hud.func_73731_b(this.mc.field_71466_p, "Sim-U-Kraft is not SMP", this.hud.field_146294_l / 2, 2, 16777215);
                return;
            }

            try {
                if (ModSimukraft.proxy.ranStartup) {
                    int HUDoffset = 0;
                    if (this.mc.thePlayer.field_71093_bK == 1) {
                        HUDoffset = 20;
                    }

                    int HUDoffset = HUDoffset + ModSimukraft.configHUDoffset;
                    if (ModSimukraft.gameMode == GameMode.CREATIVE) {
                        this.hud.func_73731_b(this.mc.field_71466_p, worldname + " (" + ModSimukraft.getDayOfWeek() + ") - Population: " + ModSimukraft.theFolks.size(), this.hud.field_146294_l / 2, 2 + HUDoffset, 16777215);
                    } else {
                        this.hud.func_73731_b(this.mc.field_71466_p, worldname + " (" + ModSimukraft.getDayOfWeek() + ") - Population: " + ModSimukraft.theFolks.size() + "   Sim-U-credits: " + ModSimukraft.displayMoney(ModSimukraft.states.credits), this.hud.field_146294_l / 2, 2 + HUDoffset, 16777215);
                    }
                } else {
                    this.hud.func_73731_b(this.mc.field_71466_p, "Loading Sim-U-Kraft...", this.hud.field_146294_l / 2, 2, 16777215);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    private void beamingPlayer() {
        Minecraft mc = Minecraft.getMinecraft();
        Random random = new Random();
        beamingPlayer.field_70159_w = 0.0D;
        beamingPlayer.field_70181_x = 0.0D;
        beamingPlayer.field_70179_y = 0.0D;
        Double d4 = ((double)random.nextFloat() - 2.0D) * 2.0D;

        for(int p = 0; p < 20; ++p) {
            try {
                mc.field_71441_e.func_72869_a("portal", beamingPlayer.posX + random.nextDouble() - 0.5D, beamingPlayer.posY - 1.0D, beamingPlayer.posZ + random.nextDouble() - 0.5D, 0.0D, -d4, 0.0D);
            } catch (Exception var7) {
            }

            try {
                mc.field_71441_e.func_72869_a("portal", beamingTo.x + random.nextDouble() - 0.5D, beamingTo.y - 1.0D, beamingTo.z + random.nextDouble() - 0.5D, 0.0D, -d4, 0.0D);
            } catch (Exception var6) {
            }
        }

        if (beamingStage == 1) {
            if (System.currentTimeMillis() - beamingStartedAt > 6000L) {
                beamingStage = 2;
                beamingPlayer.func_70634_a(beamingTo.x, beamingTo.y, beamingTo.z);
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
