package com.trhsy.sim.client;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.entity.functionality.FarmingBox;
import com.trhsy.sim.common.entity.functionality.MiningBox;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;

import java.util.Random;

/**
 * 客户端的
 */
public class ClientTickHandler extends GuiScreen {
    Minecraft mc = Minecraft.getMinecraft();
    Long timeSinceLastSave = 0L;
    public static int beamingStage = 1;
    public static long beamingStartedAt = 0L;
    public static V3 beamingTo = null;
    public static EntityPlayer beamingPlayer = null;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            FMLCommonHandler.instance().bus().register(this);
        } catch (Exception e) {
            ModSimReloaded.log.error("客户端init出错了：" + e.getMessage());
        }
    }

    @SubscribeEvent
    public void tick(WorldTickEvent event) {
        try {
            this.onTickInGame();
        } catch (Exception e) {
            ModSimReloaded.log.error("客户端tick出错了：" + e.getMessage());
        }

    }

    @SubscribeEvent
    public void tick(ClientTickEvent event) {
    }

    @SubscribeEvent
    public void tick(RenderTickEvent event) {
        try {
            this.onGui();
        } catch (Exception e) {
            ModSimReloaded.log.error("客户端tick出错了：" + e.getMessage());
        }
    }

    public void onTickInGame() {
        try {
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
                ConfigLoader.configFile.save();
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
        } catch (Exception e) {
            ModSimReloaded.log.error("客户端onTickInGame出错了：" + e.getMessage());
        }


    }

    public void onGui() {
        try {
            if (this.mc.currentScreen == null) {
                String worldname = "unknown";

                try {
                    if (ModSimReloaded.states.gameModeNumber == 10) {
                        return;
                    }

                    worldname = this.mc.getIntegratedServer().getFolderName();
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
                    //var3.printStackTrace();
                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("客户端onGui出错了：" + e.getMessage());
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
                } catch (Exception var7) {
                }

                try {
                    mc.theWorld.spawnParticle(EnumParticleTypes.PORTAL, beamingTo.x + random.nextDouble() - 0.5D, beamingTo.y - 1, beamingTo.z + random.nextDouble() - 0.5D, 0, -d4, 0);
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
        } catch (Exception e) {
            ModSimReloaded.log.error("客户端beamingPlayer出错了：" + e.getMessage());
        }
    }

    public String getLabel() {
        return "ClientTickHandler";
    }
}
