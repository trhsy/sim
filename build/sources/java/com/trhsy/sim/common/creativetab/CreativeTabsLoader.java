package com.trhsy.sim.common.creativetab;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;

/**
 * @ClassName CreativeTabsLoader
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2921:03
 **/
public class CreativeTabsLoader {

    public static CreativeTabs tabSimU;

    public CreativeTabsLoader(FMLPreInitializationEvent event) {
        tabSimU = new CreativeTabsSimU();
    }
}
