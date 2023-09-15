package com.trhsy.sim;

import com.trhsy.sim.loader.CommandLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.proxy.CommonProxy;
import com.trhsy.sim.util.SimConfigSync;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModSim.MODID,name = ModSim.NAME,useMetadata = true, version = ModSim.VERSION,acceptedMinecraftVersions = "1.11.2",guiFactory = "com.trhsy.sim.gui.ConfigGui$ConfigGuiFactory")
public class ModSim {
    /**
     * 模组id 指的就是该Mod的唯一标识符
     **/
    public static final String MODID = "sim";
    /**
     * 模组名称
     **/
    public static final String NAME = "Simulated town";
    /**
     * 模组版本 在Mod间的依赖关系时可能会用作识别
     **/
    public static final String VERSION = "1.11.2-1.0.0 Beta";
    /**
     * 将生成该mod的实例注册到对应mod的id里面，也可以访问其他mod的，要注意这里的id和此mod的id相同
     */
    @Mod.Instance(ModSim.MODID)
    public static ModSim instance;
    @SidedProxy(
            clientSide = "com.trhsy.sim.proxy.ClientProxy",
            serverSide = "com.trhsy.sim.proxy.ServerProxy"
    )
    public static CommonProxy proxy;
    /**
     * 所有Mod初始化之前调用,这时候应该加载配置文件，实例化物品和方块，并注册它们。
     *
     * @param event
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            proxy.preInit(event);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("ModSim-preInit出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    /**
     * 用于该Mod的初始化,这时候应该为Mod进行设置，如注册合成表和烧炼系统，并且向其他Mod发送交互信息。
     *
     * @param event
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            proxy.init(event);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ModSim-init出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * 在所有Mod都初始化之后调用,这时候应该接收其他Mod发送的交互信息，并完成对Mod的设置
     *
     * @param event
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            MinecraftForge.EVENT_BUS.register(new SimConfigSync());
            proxy.postInit(event);
        } catch (Exception e) {
            ModSimLoader.log.error("ModSim-postInit出错了：" + e.getMessage());
        }

    }
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        try {
            proxy.serverStarting(event);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("serverStarting出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}
