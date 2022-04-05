package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.item.*;
import com.trhsy.sim.common.item.food.*;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

import javax.annotation.CheckForNull;

/**
 * @ClassName ItemLoader
 * @Description todo 物品注册加载
 * @Author Tian
 * @Date 2022/1/2920:49
 **/
public class ItemLoader {
    /**
     * 铜粒儿
     */
    public static Item itemGranulesCopper = new ItemGranulesCopper();
    /**
     * 金粒
     */
    public static Item itemGranulesGold = new ItemGranulesGold();
    /**
     * 铁粒
     */
    public static Item itemGranulesIron = new ItemGranulesIron();
    /**
     * 锡粒儿
     */
    public static Item itemGranulesTin = new ItemGranulesTin();
    /**
     * 风车底座
     */
    public static Item itemWindmillBase = new ItemWindmillBase();
    /**
     * 风车帆
     */
    public static Item itemWindmillSails = new ItemWindmillSails();
    /**
     * 风车叶片
     */
    public static Item itemWindmillVane = new ItemWindmillVane();
    /*
    奶酪
     */
    public static Item itemFoods = new ItemFoods();
    public static Item itemDrinks = new ItemDrink();
    /*风车*/
    //public static Item itemBlockWindmill = new ItemBlockWindmill();
    /*灯箱*/
    //public static Item itemBlockLightBox = new ItemBlockLightBox();


    public ItemLoader(FMLPreInitializationEvent event) {
        //调用注册物品方法（函数）
        register(itemWindmillBase, "windmill_base");
        register(itemGranulesGold, "granules_gold");
        register(itemGranulesCopper, "granules_copper");
        register(itemGranulesIron, "granules_iron");
        register(itemGranulesTin, "granules_tin");

        register(itemWindmillSails, "windmill_sails");
        register(itemWindmillVane, "windmill_vane");
        register(itemFoods, "foods");
        register(itemDrinks, "drinks");
        //register(itemBlockWindmill, "block_windmill");
        //register(itemBlockLightBox, "light_box");

    }

    private static void register(Item item, String name) {
        //注册物品,GameRegistry是Forge提供的一个用来注册物品、方块、合成表、烧炼规则等各种常见内容的类，比如下面的用于注册的方法我们在后面都会遇到并加以讲解。
        GameRegistry.registerItem(item, name);
    }
    public static void nameItems() {
        LanguageRegistry.addName(new ItemStack(itemFoods, 1, 0), I18n.format("item.foodCheese.name"));
        LanguageRegistry.addName(new ItemStack(itemFoods, 1, 1), I18n.format("item.foodBurger.name"));
        LanguageRegistry.addName(new ItemStack(itemFoods, 1, 2), I18n.format("item.foodFries.name"));
        LanguageRegistry.addName(new ItemStack(itemFoods, 1, 3), I18n.format("item.foodCheeseburger.name"));

        LanguageRegistry.addName(new ItemStack(itemDrinks, 1, 0), I18n.format("item.drinkBeerEmpty.name"));
        LanguageRegistry.addName(new ItemStack(itemDrinks, 1, 1), I18n.format("item.drinkBeer.name"));

    }

}
