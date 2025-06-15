package com.trhsy.sim.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.proxy
 * @ClassName: ServerProxy
 * @Description: 服务器端 服务器端代理类，目前没有额外的服务器端特定逻辑
 * @date 2023/10/19 下午 2:47
 */
public class ServerProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        super.serverStarting(event);
    }
}
