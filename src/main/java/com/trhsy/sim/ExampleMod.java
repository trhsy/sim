package com.trhsy.sim;

import com.trhsy.sim.common.CommonProxy;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = ExampleMod.MODID,name = ExampleMod.NAME, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MODID = "sim";
    public static final String NAME = "sim";
    public static final String VERSION = "1.0.0 Beta";

    @SidedProxy(clientSide = "com.trhsy.sim.client.ClientProxy",
            serverSide = "com.trhsy.sim.common.CommonProxy")
    public static CommonProxy proxy;
    /**
     * @Author fan
     * @Description //TODO 前面有加@的是注解,这个作用是将生成该mod的实例注册到对应mod的id里面，也可以访问其他mod的，要注意这里的id和此mod的id相同
     * @Date 23:10 2022/1/22
     * @Param
     * @return
     **/
    @Mod.Instance
    public static ExampleMod instance = new ExampleMod();
    /**
     * @Author fan
     * @Description //TODO 在Forge找到主类后，会检查主类含有这个注解的函数，并通过函数的参数类型判断应该何时调用这些函数
     *  在所有mod初始化之前调用此函数，这里应该加载配置文件，实例化方块和物品，并注册它们
     * @Date 23:11 2022/1/22
     * @Param [event]
     * @return void
     **/
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        proxy.preInit(event);
    }
    /**
     * @Author fan
     * @Description //TODO 在此mod初始化时调用此函数，这里应该注册合成表和烧练系统，并向其他mod发送交互信息，注意不要在这里注册方块和物品等等操作，forge支持在preInit函数执行
     * @Date 23:11 2022/1/22
     * @Param [event]
     * @return void
     **/
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
		// 示例代码
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    /**
     * @Author fan
     * @Description //TODO 在所有mod初始化后调用此函数，这里应该接收其他mod发送的交互信息，并完成设置mod
     * @Date 23:11 2022/1/22
     * @Param [event]
     * @return void
     **/
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
}
