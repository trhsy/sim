package com.trhsy.sim.proxy;

import com.trhsy.sim.loader.*;
import com.trhsy.sim.loader.render.ItemRenderLoader;
import com.trhsy.sim.npc.race.Race;
import com.trhsy.sim.npc.traits.Traits;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 *公共代理
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModSimLoader.log = event.getModLog();
        /**配置**/
        ConfigLoader.load(event);
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
        /**启动通讯**/
        new NetWorkLoader(event);
        /**事件加载**/
        new EventLoader();
        /**合成表**/
        new CraftingLoader();
        /**矿物生成**/
        new WorldGeneratorLoader();
        /**实体渲染**/
        EntityLoader.init();
        /**特征**/
        Traits.loadTraits();
        /**种族*/
        Race.loadRaces();

    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
        /**加载所以建筑蓝图**/
        ModSimLoader.loadAllBuildings();
    }

    public void renderTick(TickEvent.RenderTickEvent e) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
        try {
            new CommandLoader(event);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("serverStarting出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
}
