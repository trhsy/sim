package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Iterator;
import java.util.UUID;

/**
 * @ClassName EventLoader
 * @Description todo 事件交互
 * @Author Tian
 * @Date 2022/9/2921:05
 **/
public class EventLoader {
    /**已加载世界**/
    public static boolean hasLoadedWorld;
    /**
     * 自定义的事件在这里被注册
     **/
    public static final EventBus EVENT_BUS = new EventBus();
    public EventLoader() {
        try {
            MinecraftForge.EVENT_BUS.register(this);
            EventLoader.EVENT_BUS.register(this);
            FMLCommonHandler.instance().bus().register(this);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("EventLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @Author fan
     * @Description //TODO 当玩家加入的时候
     * @Date 19:19 2022/10/7
     * @Param [event]
     * @return void
     **/
    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Thread skinThread = new Thread(() -> {
            try{
                if (event.player != null) {
                    System.out.println("*********************玩家加入*****************");
                /*Iterator var1 = WorldData.folks.iterator();

                while(var1.hasNext()) {
                    FolkData fd = (FolkData)var1.next();

                    while(fd.entity == null && FMLCommonHandler.instance() != null) {
                        try {
                            fd.entity = (EntityFolk)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(fd.ID));
                        } catch (Exception var4) {
                        }
                    }

                    fd.sendSkinPathToClient();
                }*/
                }
            }catch (Exception e){
                e.getMessage();
            }


        });
        skinThread.start();
    }

    /**
     * 世界保存
     * @param event
     */
    @SubscribeEvent
    public void worldSave(WorldEvent.Save event) {

    }

    /**
     * 世界加载
     * @param event
     */
    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        ModSimLoader.log.info("检查是否应该加载人员");
        if (event.getWorld().isRemote) {
            ModSimLoader.log.info("世界遥远，正在取消");
        } else if (hasLoadedWorld) {
            ModSimLoader.log.info("世界尚未加载，正在取消");
        } else {
            ModSimLoader.log.info("清除旧的世界数据");

            ModSimLoader.log.info("加载世界...");

            ModSimLoader.log.info("装载农场");
            ModSimLoader.log.info("装载矿场");
            ModSimLoader.log.info("获得保存的NPC");
            ModSimLoader.log.info("加载建筑物");

            hasLoadedWorld = true;
        }
    }

    /**
     * 钩子
     * @param event
     */
    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {

    }

    /**
     * 渲染钩子
     * @param e
     */
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent e) {
        ModSim.proxy.renderTick(e);
    }

    /**
     * 当实体加入世界
     * @param event
     */
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        World worldObj = event.getWorld();
    }
    /**
     * 客户端断开连接
     * @param event
     */
    @SubscribeEvent
    public void clientDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        hasLoadedWorld = false;
    }
}
