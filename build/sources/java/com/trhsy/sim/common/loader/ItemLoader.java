package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.item.ItemGranulesCopper;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * @ClassName ItemLoader
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2920:49
 **/
public class ItemLoader {
    public static ItemGranulesCopper ds=new ItemGranulesCopper();
    public ItemLoader(FMLPreInitializationEvent event) {
        register(ds, "diamondStick");//调用注册物品方法（函数）
    }

    private static void register(Item item, String name) {
        GameRegistry.registerItem(item, name);//注册物品,GameRegistry是Forge提供的一个用来注册物品、方块、合成表、烧炼规则等各种常见内容的类，比如下面的用于注册的方法我们在后面都会遇到并加以讲解。
    }
}
