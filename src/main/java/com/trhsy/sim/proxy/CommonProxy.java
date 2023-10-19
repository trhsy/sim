package com.trhsy.sim.proxy;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.proxy
 * @ClassName: CommonProxy
 * @Description:
 * @date 2023/10/19 下午 2:09
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModSimLoader.log = event.getModLog();
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
        /**加载所以建筑蓝图**/
        //ModSimLoader.loadAllBuildings();
    }

    public void renderTick(TickEvent.RenderTickEvent e) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
        try {
            //new CommandLoader(event);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("serverStarting出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
}
