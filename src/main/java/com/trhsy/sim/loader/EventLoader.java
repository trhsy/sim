package com.trhsy.sim.loader;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: EventLoader
 * @Description:
 * @date 2022/9/22 0022 下午 5:09
 */
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
}
