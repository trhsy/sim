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
    Minecraft mc = Minecraft.func_71410_x();
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
            if (this.mc.field_71462_r != null) {
                if (this.mc.field_71462_r.toString().toLowerCase().contains("guimainmenu")) {
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
            if (mc.field_71462_r != null) {
                if (mc.field_71462_r.toString().toLowerCase().contains("ingamemenu")) {
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
        if (this.mc.field_71462_r == null) {
            String worldname = "unknown";

            try {
                if (ModSimReloaded.states.gameModeNumber == 10) {
                    return;
                }

//                worldname = this.mc.getIntegratedServer().getFolderName();
                worldname = MinecraftServer.func_71276_C().func_71270_I();
            } catch (Exception var4) {
                this.func_73731_b(this.mc.field_71466_p, I18n.func_135052_a("container.sim.trhsy2"), this.field_146294_l / 2, 2, 16777215);
                return;
            }

            try {
                if (ModSim.proxy.ranStartup) {
                    int HUDoffset = 0;
                    if (this.mc.field_71439_g.field_71093_bK == 1) {
                        HUDoffset = 20;
                    }

                    HUDoffset = HUDoffset + ConfigLoader.configHUDoffset;
                    if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                        this.func_73731_b(this.mc.field_71466_p, worldname + " (" + ModSimReloaded.getDayOfWeek() + ") - " + I18n.func_135052_a("container.sim.trhsy3") + ": " + ModSimReloaded.theFolks.size(), this.field_146294_l / 2, 2 + HUDoffset, 16777215);
                    } else {
                        this.func_73731_b(this.mc.field_71466_p, worldname + " (" + ModSimReloaded.getDayOfWeek() + ") - " + I18n.func_135052_a("container.sim.trhsy3") + ": " + ModSimReloaded.theFolks.size() + "   " + I18n.func_135052_a("container.sim.trhsy4") + ": " + ModSimReloaded.displayMoney(ModSimReloaded.states.credits), this.field_146294_l / 2, 2 + HUDoffset, 16777215);
                    }
                } else {
                    this.func_73731_b(this.mc.field_71466_p, I18n.func_135052_a("container.sim.trhsy5"), this.field_146294_l / 2, 2, 16777215);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }
    private void beamingPlayer() {
        try {
            Minecraft mc = Minecraft.func_71410_x();
            Random random = new Random();
            beamingPlayer.field_70159_w = 0;
            beamingPlayer.field_70181_x = 0;
            beamingPlayer.field_70179_y = 0;
            Double d4 = ((double) random.nextFloat() - 2) * 2;

            for (int p = 0; p < 20; ++p) {
                try {
                    mc.field_71441_e.func_175688_a(EnumParticleTypes.PORTAL, beamingPlayer.field_70165_t + random.nextDouble() - 0.5D, beamingPlayer.field_70163_u - 1, beamingPlayer.field_70161_v + random.nextDouble() - 0.5D, 0, -d4, 0);
                } catch (Exception e) {
                }

                try {
                    mc.field_71441_e.func_175688_a(EnumParticleTypes.PORTAL, beamingTo.field_72450_a + random.nextDouble() - 0.5D, beamingTo.field_72448_b - 1, beamingTo.field_72449_c + random.nextDouble() - 0.5D, 0, -d4, 0);
                } catch (Exception e) {
                }
            }

            if (beamingStage == 1) {
                if (System.currentTimeMillis() - beamingStartedAt > 6000L) {
                    beamingStage = 2;
                    beamingPlayer.func_70634_a(beamingTo.field_72450_a, beamingTo.field_72448_b, beamingTo.field_72449_c);
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
