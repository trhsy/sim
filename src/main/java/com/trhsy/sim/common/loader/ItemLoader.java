package com.trhsy.sim.common.loader;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 物品加载类
 */
public class ItemLoader {
    /**
     * 加载物品
     * @param event
     */
    public ItemLoader(FMLPreInitializationEvent event) {
        //register(goldenEgg, "golden_egg");
    }

    /**
     * 注册任务品
     * @param item
     * @param name
     */
    private static void register(Item item, String name) {
        GameRegistry.registerItem(item.setRegistryName(name));
    }
}
