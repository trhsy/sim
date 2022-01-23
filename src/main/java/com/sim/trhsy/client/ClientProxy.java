package com.sim.trhsy.client;

import com.sim.trhsy.common.CommonProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @ClassName ClientProxy
 * @Description todo 客户端
 * @Author Tian
 * @Date 2022/1/2223:15
 **/
public class ClientProxy  extends CommonProxy {
    /**
     * @Author fan
     * @Description //TODO 声明该方法覆盖了父类方法，如果（拼写错误等）没有覆写，编译器会报错
     * @Date 23:16 2022/1/22
     * @Param [event]
     * @return void
     **/
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        //调用父类的方法
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
}
