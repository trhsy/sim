package com.trhsy.sim.proxy;

import com.trhsy.sim.key.KeyboardManager;
import com.trhsy.sim.loader.*;
import com.trhsy.sim.loader.render.ItemRenderLoader;
import com.trhsy.sim.npcCode.race.Race;
import com.trhsy.sim.npcCode.traits.Traits;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.proxy
 * @ClassName: CommonProxy
 * @Description:
 * @date 2023/10/19 下午 2:09
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModSimLoader.log = event.getModLog();
        /**配置**/
        ConfigLoader.load(event);
        /**创造模式物品栏**/
        new CreativeTabsLoader(event);
        /**流体加载注册**/
        new FluidLoader(event);
        /**物品加载注册**/
//        new ItemLoader(event);
        /**方块加载注册**/
//        new BlockLoader(event);
        /**方块对应物品的渲染**/
        new ItemRenderLoader();
        /**启动通讯**/
        new NetWorkLoader(event);

        /**矿物生成**/
        new WorldGeneratorLoader();
        /**实体渲染**/
        EntityLoader.registerEntitys();
        /**事件加载**/
        new EventLoader();
        /**特征**/
        Traits.loadTraits();
        /**种族*/
        Race.loadRaces();
        KeyboardManager.init();
    }

    public void init(FMLInitializationEvent event) {
        /**     合成表**/
        new CraftingLoader();
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
