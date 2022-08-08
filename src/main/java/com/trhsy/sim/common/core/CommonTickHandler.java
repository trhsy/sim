package com.trhsy.sim.common.core;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.Building;
import com.trhsy.sim.common.core.entity.CourierTask;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.Relationship;
import com.trhsy.sim.common.core.entity.functionality.FarmingBox;
import com.trhsy.sim.common.core.entity.functionality.MiningBox;
import com.trhsy.sim.common.gui.GuiRunMod;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * 通用勾号处理程序
 */
public class CommonTickHandler {
    private World serverWorld;
    /*最后勾选*/
    Long lastSecondTickAt = 0L;
    /*最后勾选*/
    Long lastMinuteTickAt = 0L;
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
//        System.out.println("ServerWorldTickEvent");

    }

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
//        System.out.println("ServerTickEvent");
        try {
            if (this.ticks == 200) {
                this.onTickInGame();
            } else {
                this.ticks++;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("tick出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public void onTickInGame() {
        try {
            if (this.mc.currentScreen != null && this.mc.currentScreen.toString().toLowerCase().contains("guimainmenu")) {
                //CommTH:在Gui主菜单中
                ModSimReloaded.log.info("CommTH: 在Gui主菜单中");
            }
            if (FMLCommonHandler.instance().getSide().isClient()) {
                if (ModSimReloaded.states.gameModeNumber == 10) {
                    ModSim.proxy.ranStartup = true;
//                    return;
                }
            }
            Long now = System.currentTimeMillis();
            //每秒一次
            if (now - this.lastSecondTickAt > 1000L) {
                if (this.serverWorld != null) {
                    ModSimReloaded.log.info("触发更新");
                    //触发NPC所有更新
                    FolkData.triggerAllUpdates();
//                    ModSimReloaded.log.info("触发NPC更新");
                    //处理昼夜转换
                    ModSimReloaded.dayTransitionHandler();
//                    ModSimReloaded.log.info("触发昼夜转换更新");
                    //如果要升级的农场不为空则升级农场
                    if (ModSimReloaded.farmToUpgrade != null) {
                        ModSimReloaded.upgradeFarm();
                    }
//                    ModSimReloaded.log.info("触发要升级的农场更新");
                    //需要拆除的方块不为空则
                    if (ModSimReloaded.demolishBlocks.size() > 0) {
                        ModSimReloaded.demolishBlocks();
                    }
//                    ModSimReloaded.log.info("触发需要拆除的方块更新");
                }
                if (ModSim.proxy.ranStartup==false) {
                    //还没有启动——现在就这么做
                    //ModSimReloaded.log.info("Haven't run startup - doing that now");
                    ModSimReloaded.log.info("还没有启动——现在就这么做");

                    this.serverWorld = MinecraftServer.getServer().getEntityWorld();
                    this.currentWorld = ModSimReloaded.getSavesDataFolder();

                    //CommTH: Startup - set serverWorld/currentWorld
//                        ModSimReloaded.log.info("CommTH: 启动 - 设置 serverWorld/currentWorld");
                    ModSimReloaded.log.info("运行重置世界功能");
                    //ModSimReloaded.log.info("Running Reset World Function");
                    ModSimReloaded.resetAndLoadNewWorld();
                    this.lastReset=now;
                    //数据包已经有了一个解决方案——它们需要被修复。
                } else {
                    //用于检测世界变化-这仍然是一个bug，当玩家通过主菜单切换时不会卸载世界
                    //已经在启动中运行
                    if (!this.currentWorld.contentEquals(ModSimReloaded.getSavesDataFolder())){
                        if( now- this.lastReset > 30000L) {
                                this.serverWorld = MinecraftServer.getServer().getEntityWorld();
                                this.currentWorld = ModSimReloaded.getSavesDataFolder();
                                ModSimReloaded.log.info("创建时间=" + this.currentWorld + " 保存在=" + ModSimReloaded.getSavesDataFolder());
                                ModSim.proxy.ranStartup = false;
                                ModSimReloaded.resetAndLoadNewWorld();

                        }
                    }
                    //停止下雨MOD-在我的世界里一直下雨的时候实现了这个！
                    if(this.serverWorld!=null){
                        if (this.serverWorld.isRaining() && this.serverWorld.getWorldInfo().getRainTime() > 1 && ConfigLoader.configStopRain) {
                            this.serverWorld.getWorldInfo().setRaining(false);
                        }
                    }
                }
                now = System.currentTimeMillis();
                this.lastSecondTickAt = now;
            }
            now=System.currentTimeMillis();
            //每分钟一次
            if (this.serverWorld != null && now - this.lastMinuteTickAt > 60000L) {
                if (this.lastMinuteTickAt > 0L) {
                    Long start = System.currentTimeMillis();
                    //生成新的npc
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

                this.lastMinuteTickAt = System.currentTimeMillis();
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("onTickInGame出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
