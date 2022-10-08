package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

import java.io.File;
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
    /**上次可以户连接的时间**/
    public static long timeSinceLastClientUpdate = 0L;
    /**分钟计时器**/
    long minuteTimer = System.currentTimeMillis();
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
        try{
            String baseURL = "https://trhsy.github.io/sim/1.9/version.txt";
            String ver = ModSimLoader.downloadFile(baseURL, ModSimLoader.getSimukraftFolder() + File.separator + "version.txt");
            if (ver != null) {
                ver = ver.trim();
                if (!ver.contentEquals("")&&!"1.0.0 Beta".contentEquals(ver)) {
                    if (!ModSim.VERSION.contentEquals(ver)) {
                        ModSimLoader.sendChat(I18n.format("container.sim.update_checker1") + ver + I18n.format("container.sim.update_checker2") );
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
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
        if (!event.world.isRemote && System.currentTimeMillis() - this.timeSinceLastClientUpdate > 2000L) {
            this.timeSinceLastClientUpdate = System.currentTimeMillis();
            /*NetWorkLoader.net.sendToAll(new PacketReturnHireableFolks());
            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());*/


            if (ModSimLoader.states.gameModeNumber != -1 && !event.world.isRemote) {
                if(ModSimLoader.isDayTime(event.world)){
                    if (System.currentTimeMillis() - this.minuteTimer > 60000L) {

                    }
                }
            }
            //实时更新人的状态
            if (!event.world.isRemote) {

            }
        }
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
     * 当实体加入世界赋予玩家手里第一个物品栏里一个模拟城市任命卷轴
     * @param event
     */
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        World worldObj = event.getWorld();
        if (!event.getWorld().isRemote && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            boolean shouldGive = ItemLoader.itemSimULoader != null && ModSimLoader.states.gameModeNumber == -1;
            if (shouldGive) {
                ItemStack starter = new ItemStack(ItemLoader.itemSimULoader);
                if (!player.inventory.addItemStackToInventory(starter)) {
                    float f = 0.7F;
                    float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    EntityItem entityitem = new EntityItem(worldObj, player.posX + (double)d0, player.posY + (double)d1, player.posZ + (double)d2, new ItemStack(ItemLoader.itemSimULoader));
                    entityitem.setDefaultPickupDelay();
                    worldObj.spawnEntityInWorld(entityitem);
                }
            }
        }
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
