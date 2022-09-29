package com.trhsy.sim.loader;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Iterator;
import java.util.UUID;

/**
 * @ClassName EventLoader
 * @Description todo 事件交互
 * @Author Tian
 * @Date 2022/9/2921:05
 **/
public class EventLoader {
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

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Thread skinThread = new Thread(() -> {
            try{
                if (event.player != null) {
                    System.out.println("1");
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
}
