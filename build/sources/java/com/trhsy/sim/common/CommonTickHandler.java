package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.client.gui.GuiRunMod;
import com.trhsy.sim.common.entity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName CommonTickHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:48
 * ========================================
 **/
public class CommonTickHandler {private World serverWorld = null;
    Long lastSecondTickAt = 0L;
    Long lastMinuteTickAt = 0L;
    GuiRunMod runModui = null;
    String currentWorld = "";
    Minecraft mc = Minecraft.getMinecraft();
    long lastReset = 0L;
    boolean haveRunStartup = false;
    int ticks = 0;
    public static final SimpleNetworkWrapper INSTANCE;

    @SubscribeEvent
    public void tick(WorldTickEvent event) {
    }

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
        if (this.ticks == 100) {
            this.onTickInGame();
        } else {
            ++this.ticks;
        }

    }

    public void onTickInGame() {
        if (this.mc.currentScreen != null && this.mc.currentScreen.toString().toLowerCase().contains("guimainmenu")) {
            ModSim.log.info("CommTH: in Gui Main menu");
        }

        if (ModSim.states.gameModeNumber == 10) {
            ModSim.proxy.ranStartup = true;
        } else {
            Long now = System.currentTimeMillis();
            if (this.serverWorld != null) {
                FolkData.triggerAllUpdates();
                ModSim.dayTransitionHandler();
                if (ModSim.farmToUpgrade != null) {
                    ModSim.upgradeFarm();
                }

                if (ModSim.demolishBlocks.size() > 0) {
                    ModSim.demolishBlocks();
                }
            }

            if (now - this.lastSecondTickAt > 1000L) {
                if (!ModSim.proxy.ranStartup) {
                    System.out.println("Haven't run startup - doing that now");
                    this.serverWorld = MinecraftServer.getServer().getEntityWorld();
                    this.currentWorld = ModSim.getSavesDataFolder();
                    ModSim.log.info("CommTH: Startup - set serverWorld/currentWorld");
                    System.out.println("Running Reset World Function");
                    ModSim.resetAndLoadNewWorld();
                } else {
                    if (!this.currentWorld.contentEquals(ModSim.getSavesDataFolder()) && now - this.lastReset > 30000L) {
                        ModSim.log.info("currentWorld=" + this.currentWorld + "     getSaves=" + ModSim.getSavesDataFolder());
                        this.currentWorld = ModSim.getSavesDataFolder();
                        ModSim.proxy.ranStartup = false;
                        ModSim.resetAndLoadNewWorld();
                    }

                    if (this.serverWorld.isRaining() && this.serverWorld.getWorldInfo().getRainTime() > 1 && ModSim.configStopRain) {
                        this.serverWorld.getWorldInfo().setRainTime(2);
                    }
                }

                this.lastSecondTickAt = now;
            }

            if (this.serverWorld != null && System.currentTimeMillis() - this.lastMinuteTickAt > 60000L) {
                if (this.lastMinuteTickAt > 0L) {
                    Long start = System.currentTimeMillis();
                    FolkData.generateNewFolk(this.serverWorld);
                    ModSim.states.saveStates();
                    Building.checkTennants();
                    Building.saveAllBuildings();
                    CourierTask.saveCourierTasksAndPoints();
                    MiningBox.saveMiningBoxes();
                    FarmingBox.saveFarmingBoxes();
                    Relationship.saveRelationships();
                    ModSim.log.info("CTH: Saved game data in " + (System.currentTimeMillis() - start) + " ms");
                }

                this.lastMinuteTickAt = now;
            }

        }
    }

    public void resetSimUKraft() {
        if (System.currentTimeMillis() - this.lastReset > 30000L) {
            this.lastReset = System.currentTimeMillis();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            ModSim.log.info(side.toString() + "-side CommTH: resetSimUKraft()");
        }

    }

    private void startingWorld() {
        if (!ModSim.proxy.ranStartup) {
        }

    }

    public String getLabel() {
        return "CommonTickHandler";
    }

    public CommonTickHandler() {
    }

    static {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SUKMain");
    }
}