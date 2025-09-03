package com.trhsy.sim;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim
 * @ClassName: ModSim
 * @Description: 此处的值应与META-INF/mods.toml文件中的条目匹配
 * @date 2025/9/3 22:54
 */
@Mod("sim")
public class ModSim {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ModSim() {
        // 注册modloading的设置方法
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // 注册用于模块加载的入队IMC方法
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // 注册processIMC方法进行模态加载
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // 注册doClientStuff方法进行modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // 注册我们感兴趣的服务器和其他游戏活动
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("欢迎来到模拟的大都市");
        LOGGER.info("泥土 BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // 做一些只能在客户端上完成的事情
        LOGGER.info("已获取游戏设置 {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // 将IMC分派到另一个模块的示例代码
        InterModComms.sendTo("sim", "helloworld", () -> { LOGGER.info("Hello world from the TRHSY"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // 一些示例代码，用于接收和处理来自其他模组的InterModComms
        LOGGER.info("获取 IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    /**
     *  您可以使用SubscribeEvent，让事件总线发现方法来调用
     * @param event
     */
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // 服务器启动时执行某些操作
        LOGGER.info("SIM 模拟大都市从服务器启动了");
    }


    /**
     * 您可以使用EventBusSubscriber自动订阅所包含类上的事件（这是订阅MOD事件总线以接收注册表事件）
     */
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // 在此处注册新块
            LOGGER.info("HELLO来自注册表块");
        }
    }
}
