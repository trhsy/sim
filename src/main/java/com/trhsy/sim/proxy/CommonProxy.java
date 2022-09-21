package com.trhsy.sim.proxy;

import com.trhsy.sim.loader.*;
import com.trhsy.sim.loader.render.ItemRenderLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 *公共代理
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModSimLoader.log = event.getModLog();
        /**创造模式物品栏**/
        new CreativeTabsLoader(event);
        /**物品加载注册**/
        new ItemLoader(event);
        /**方块加载注册**/
        new BlockLoader(event);
        /**流体加载注册**/
        new FluidLoader(event);

        /**方块对应物品的渲染**/
        new ItemRenderLoader();
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}
