package com.trhsy.sim;

import com.trhsy.sim.client.ClientProxy;
import com.trhsy.sim.common.Commodity;
import com.trhsy.sim.common.CommonProxy;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.jobs.JobSoldier;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.client.Handler;
import com.trhsy.sim.packets.client.UpdateFolkPositionMessage;
import com.trhsy.sim.packets.server.LoadBuildingMessage;
import com.trhsy.sim.util.GameMode;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * @ClassName ModSimukraft
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2319:40
 **/
@Mod(modid = ModSim.MODID, name = ModSim.NAME, version = ModSim.VERSION, useMetadata = true, dependencies = "required-after:Forge@[9.10,)")
public class ModSim {
    public static final String MODID = "sim";
    public static final String NAME = "Simulated town";
    public static final String VERSION = "1.0.2 Beta";
    /**
     * 将生成该mod的实例注册到对应mod的id里面，也可以访问其他mod的，要注意这里的id和此mod的id相同
     */
    @Instance(ModSim.MODID)
    public static ModSim instance = new ModSim();

    @SidedProxy(
            clientSide = "com.trhsy.sim.client.ClientProxy",
            serverSide = "com.trhsy.sim.common.CommonProxy"
    )
    public static CommonProxy proxy;
    public static ClientProxy clientProxy;
    public static Logger log;


    /*
    工作关系
     */
    public static SimpleNetworkWrapper network;


    public ModSim() {

    }





    /**
     * 在所有mod初始化之前调用此函数，这里应该加载配置文件，实例化方块和物品，并注册它们
     *
     * @param event
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        log = event.getModLog();
        //新的网络包装器
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        //注册客户端消息系统
        network.registerMessage(Handler.class, UpdateFolkPositionMessage.class, 1, Side.CLIENT);
        //注册服务端消息系统
        network.registerMessage(com.trhsy.sim.packets.server.Handler.class, LoadBuildingMessage.class, 0, Side.SERVER);





        /*
*/
        EntityRegistry.registerGlobalEntityID(EntityAlignBeam.class, "AlignBeam", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityAlignBeam.class, "AlignBeam", 0, this, 250, 10, false);
        EntityRegistry.registerGlobalEntityID(EntityFolk.class, "Folk", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityFolk.class, "Folk", 1, this, 250, 2, true);
        EntityRegistry.registerGlobalEntityID(EntityConBox.class, "ConBox", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityConBox.class, "ConBox", 2, this, 250, 2, true);
        EntityRegistry.registerGlobalEntityID(EntityWindmill.class, "SUKWindmill", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityWindmill.class, "SUKWindmill", 3, this, 250, 1, false);
        proxy.registerRenderInfo();
        proxy.registerMisc();
        //updateCheck();
    }


    @EventHandler
    public void init(FMLInitializationEvent event) {
        //在此mod初始化时调用此函数，这里应该注册合成表和烧练系统，并向其他mod发送交互信息，注意不要在这里注册方块和物品等等操作，forge支持在preInit函数执行
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //在所有mod初始化后调用此函数，这里应该接收其他mod发送的交互信息，并完成设置mod
        proxy.postInit(event);
    }




}
