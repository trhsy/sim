package com.trhsy.sim;

import com.trhsy.sim.client.ClientProxy;
import com.trhsy.sim.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

/**
 * 指的是Mod接受的Minecraft版本，当版本不对时，FML会优雅地抛出一个错误而不是继续加载这个Mod
 */
@Mod(modid = ModSim.MODID, name = ModSim.NAME, version = ModSim.VERSION, useMetadata = true, dependencies = "required-after:Forge@[9.10,)",acceptedMinecraftVersions = "1.8.9")
public class ModSim {
    /**模组id 指的就是该Mod的唯一标识符**/
    public static final String MODID = "sim";
    /**模组名称**/
    public static final String NAME = "Simulated town";
    /**模组版本 在Mod间的依赖关系时可能会用作识别**/
    public static final String VERSION = "1.0.0 Beta";
    /**
     * 将生成该mod的实例注册到对应mod的id里面，也可以访问其他mod的，要注意这里的id和此mod的id相同
     */
    @Mod.Instance(ModSim.MODID)
    public static ModSim instance = new ModSim();
    /**服务端代理**/
    @SidedProxy(
            clientSide = "com.trhsy.sim.client.ClientProxy",
            serverSide = "com.trhsy.sim.common.CommonProxy"
    )
    public static CommonProxy proxy;
    /**客户端代理**/
    public static ClientProxy clientProxy;
    /**日志**/
    public static Logger log;

    /**
     *
     */
    public ModSim() {

    }

    /**
     * 所有Mod初始化之前调用,这时候应该加载配置文件，实例化物品和方块，并注册它们。
     * @param event
     */
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    /**
     * 用于该Mod的初始化,这时候应该为Mod进行设置，如注册合成表和烧炼系统，并且向其他Mod发送交互信息。
     * @param event
     */
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    /**
     *在所有Mod都初始化之后调用,这时候应该接收其他Mod发送的交互信息，并完成对Mod的设置
     * @param event
     */
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
