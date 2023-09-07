package com.trhsy.sim.loader;

import com.trhsy.sim.creative.CreativeTabsFMLTutor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * 物品栏加载
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
