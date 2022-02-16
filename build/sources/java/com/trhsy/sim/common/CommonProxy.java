package com.trhsy.sim.common;

import com.trhsy.sim.client.ClientTickHandler;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.loader.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * @ClassName CommonProxy
 * @Description todo 服务端
 * @Author Tian
 * @Date 2022/1/2223:15
 **/
public class CommonProxy {

    public boolean ranStartup = false;
    public CommonProxy() {
    }
    public void registerRenderInfo(){
    }
    /**
     * @Author fan
     * @Description //TODO 初始化之前加载
     * @Date 11:46 2022/1/23
     * @Param [event]
     * @return void
     **/
    public void preInit(FMLPreInitializationEvent event) {
        //加载配置
        new ConfigLoader(event);
        //加载物品栏
        new CreativeTabsLoader(event);
        //加载所以物品
        new ItemLoader(event);
        //加载所以方块
        new BlockLoader(event);

    }
    /**
     * @Author fan
     * @Description //TODO 在此mod初始化时调用此函数
     * @Date 11:46 2022/1/23
     * @Param [event]
     * @return void
     **/
    public void init(FMLInitializationEvent event) {
        //加载合成表，燃烧规则
        new CraftingLoader();
        //事件
        new EventLoader();
    }
    /**
     * @Author fan
     * @Description //TODO 在所有mod初始化后调用此函数
     * @Date 11:46 2022/1/23
     * @Param [event]
     * @return void
     **/
    public void postInit(FMLPostInitializationEvent event) {

    }
    /**
     * @Author fan
     * @Description //TODO 保存对象
     * @Date 11:44 2022/1/23
     * @Param [filename, o]
     * @return void
     **/
    public void saveObject(String filename, Object o) {
    }
    /**
     * @Author fan
     * @Description //TODO 加载对象
     * @Date 11:44 2022/1/23
     * @Param [filename]
     * @return java.lang.Object
     **/
    public Object loadObject(String filename) {
        Object o = null;

        try {
            FileInputStream fis2 = new FileInputStream(filename);
            ObjectInputStream in2 = new ObjectInputStream(fis2);
            o = in2.readObject();
            in2.close();
        } catch (Exception var5) {
            ModSim.log.info("旧加载程序-无法加载对象 " + var5.getMessage());
        }

        return o;
    }
    /**
     * @Author fan
     * @Description //TODO 注册杂项
     * @Date 12:14 2022/1/23
     * @Param []
     * @return void
     **/
    public void registerMisc() {
        FMLCommonHandler.instance().bus().register(new CommonTickHandler());
        FMLCommonHandler.instance().bus().register(new ClientTickHandler());
    }
    /**
     * @Author fan
     * @Description //TODO 获取客户世界
     * @Date 12:14 2022/1/23
     * @Param []
     * @return net.minecraft.world.World
     **/
    public World getClientWorld() {
        return null;
    }
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

}
