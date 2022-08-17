package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.creativetab.CreativeTabsFMLTutor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * 物品栏加载类
 */
public class CreativeTabsLoader {
    public static CreativeTabs tabSimU;

    public CreativeTabsLoader(FMLPreInitializationEvent event) {
        try {
            tabSimU = new CreativeTabsFMLTutor();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("CreativeTabsLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
