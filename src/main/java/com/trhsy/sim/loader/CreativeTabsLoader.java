package com.trhsy.sim.loader;

import com.trhsy.sim.creative.CreativeTabsFMLTutor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: CreativeTabsLoader
 * @Description: 模拟城市 创造模式 物品栏加载
 * @date 2023/10/31 上午 10:09
 */
public class CreativeTabsLoader {
    public static CreativeTabs tabSimU;
    public CreativeTabsLoader(FMLPreInitializationEvent event) {
        try {
            tabSimU = new CreativeTabsFMLTutor();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("CreativeTabsLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
