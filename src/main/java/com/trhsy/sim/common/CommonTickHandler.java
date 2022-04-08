package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.gui.other.GuiRunMod;
import com.trhsy.sim.common.block.functionality.FarmingBox;
import com.trhsy.sim.common.block.functionality.MiningBox;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
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
public class CommonTickHandler {

    private World serverWorld = null;
    /*最后勾选*/
    Long lastSecondTickAt = 0L;
    /*最后勾选*/
    Long lastMinuteTickAt = 0L;
    /*运行mod ui*/
    GuiRunMod runModui = null;
    /*当前世界*/
    String currentWorld = "";

    Minecraft mc = Minecraft.getMinecraft();
    /*最后重置*/
    long lastReset = 0L;
    /*是否已经启动*/
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
            //CommTH:在Gui主菜单中
            ModSimReloaded.log.info("CommTH: 在Gui主菜单中");
        }

        if (ModSimReloaded.states.gameModeNumber == 10) {
            ModSim.proxy.ranStartup = true;
        } else {
            Long now = System.currentTimeMillis();
            if (this.serverWorld != null) {
                FolkData.triggerAllUpdates();
                ModSimReloaded.dayTransitionHandler();
                if (ModSimReloaded.farmToUpgrade != null) {
                    ModSimReloaded.upgradeFarm();
                }

                if (ModSimReloaded.demolishBlocks.size() > 0) {
                    ModSimReloaded.demolishBlocks();
                }
            }

            if (now - this.lastSecondTickAt > 1000L) {
                if (!ModSim.proxy.ranStartup) {
                    //还没有启动——现在就这么做
                    //ModSimReloaded.log.info("Haven't run startup - doing that now");
                    ModSimReloaded.log.info("还没有启动——现在就这么做");
                    this.serverWorld = MinecraftServer.getServer().getEntityWorld();
                    this.currentWorld = ModSimReloaded.getSavesDataFolder();
                    //CommTH: Startup - set serverWorld/currentWorld
                    ModSimReloaded.log.info("CommTH: 启动 - 设置 serverWorld/currentWorld");
                    ModSimReloaded.log.info("运行重置世界功能");
                    //ModSimReloaded.log.info("Running Reset World Function");
                    ModSimReloaded.resetAndLoadNewWorld();
                } else {
                    if (!this.currentWorld.contentEquals(ModSimReloaded.getSavesDataFolder()) && now - this.lastReset > 30000L) {
                        ModSimReloaded.log.info("currentWorld=" + this.currentWorld + "     getSaves=" + ModSimReloaded.getSavesDataFolder());
                        this.currentWorld = ModSimReloaded.getSavesDataFolder();
                        ModSim.proxy.ranStartup = false;
                        ModSimReloaded.resetAndLoadNewWorld();
                    }

                    if (this.serverWorld.isRaining() && this.serverWorld.getWorldInfo().getRainTime() > 1 && ConfigLoader.configStopRain) {
                        this.serverWorld.getWorldInfo().setRainTime(2);
                    }
                }

                this.lastSecondTickAt = now;
            }

            if (this.serverWorld != null && System.currentTimeMillis() - this.lastMinuteTickAt > 60000L) {
                if (this.lastMinuteTickAt > 0L) {
                    Long start = System.currentTimeMillis();
                    FolkData.generateNewFolk(this.serverWorld);
                    ModSimReloaded.states.saveStates();
                    Building.checkTenants();
                    Building.saveAllBuildings();
                    CourierTask.saveCourierTasksAndPoints();
                    MiningBox.saveMiningBoxes();
                    FarmingBox.saveFarmingBoxes();
                    Relationship.saveRelationships();
                    ModSimReloaded.log.info("CTH: 将游戏数据保存在 " + (System.currentTimeMillis() - start) + " ms");
                    //Saved game data in
                }

                this.lastMinuteTickAt = now;
            }

        }
    }

    public void resetSimUKraft() {
        if (System.currentTimeMillis() - this.lastReset > 30000L) {
            this.lastReset = System.currentTimeMillis();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            //side CommTH: resetSimUKraft()
            ModSimReloaded.log.info(side.toString() + "-side CommTH: 重置SimUKraft()");
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