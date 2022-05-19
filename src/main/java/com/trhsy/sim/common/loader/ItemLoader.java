package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.item.*;
import com.trhsy.sim.common.item.ItemBucketMilk;
import com.trhsy.sim.common.item.armor.ItemCopperArmor;
import com.trhsy.sim.common.item.armor.ItemTinArmor;
import com.trhsy.sim.common.item.food.ItemBurger;
import com.trhsy.sim.common.item.food.ItemCheese;
import com.trhsy.sim.common.item.food.ItemCheeseburger;
import com.trhsy.sim.common.item.food.ItemFries;
import com.trhsy.sim.common.item.tool.*;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 物品加载类
 */
public class ItemLoader {
    /**铜粒儿**/
    public static Item itemGranulesCopper=new ItemGranulesCopper();
    /**锡粒儿**/
    public static Item itemGranulesTin=new ItemGranulesTin();
    /**铁粒儿**/
    public static Item itemGranulesIron=new ItemGranulesIron();
    /**金粒儿**/
    public static Item itemGranulesGold=new ItemGranulesGold();

    /**铜锭**/
    public static Item itemCopperIngot=new ItemCopperIngot();
    /**锡锭**/
    public static Item itemTinIngot=new ItemTinIngot();

    /**铜镐**/
    public static ItemPickaxe copperPickaxe = new ItemCopperPickaxe();
    /**铜锄头**/
    public static ItemHoe copperHoe = new ItemCopperHoe();
    /**铜斧子**/
    public static ItemAxe copperAxe = new ItemCopperAxe();
    /**铜锹**/
    public static ItemSpade copperSpade = new ItemCopperSpade();
    /**铜剑**/
    public static ItemSword copperSword = new ItemCopperSword();
    /**铜头盔**/
    public static ItemArmor copperHelmet = new ItemCopperArmor.Helmet();
    /**铜甲**/
    public static ItemArmor copperChestplate = new ItemCopperArmor.Chestplate();
    /**铜护腿**/
    public static ItemArmor copperLeggings = new ItemCopperArmor.Leggings();
    /**铜鞋**/
    public static ItemArmor copperBoots = new ItemCopperArmor.Boots();


    /**锡镐**/
    public static ItemPickaxe tinPickaxe = new ItemTinPickaxe();
    /**锡锄头**/
    public static ItemHoe tinHoe = new ItemTinHoe();
    /**锡斧子**/
    public static ItemAxe tinAxe = new ItemTinAxe();
    /**锡锹**/
    public static ItemSpade tinSpade = new ItemTinSpade();
    /**锡剑**/
    public static ItemSword tinSword = new ItemTinSword();
    /**锡头盔**/
    public static ItemArmor tinHelmet = new ItemTinArmor.Helmet();
    /**锡甲**/
    public static ItemArmor tinChestplate = new ItemTinArmor.Chestplate();
    /**锡护腿**/
    public static ItemArmor tinLeggings = new ItemTinArmor.Leggings();
    /**锡鞋**/
    public static ItemArmor tinBoots = new ItemTinArmor.Boots();

    /**牛奶桶**/
    public static Item itemBucketMilk=new ItemBucketMilk();
    /**汉堡**/
    public static Item itemBurger=new ItemBurger();
    /**薯条**/
    public static Item itemFries=new ItemFries();
    /**奶酪**/
    public static Item itemCheese=new ItemCheese();
    /**奶酪汉堡**/
    public static Item itemCheeseburger=new ItemCheeseburger();
    /**
     * 加载物品
     *
     * @param event
     */
    public ItemLoader(FMLPreInitializationEvent event) {

        /**铜粒儿**/
        register(itemGranulesCopper, "item_granules_copper");
        /**锡粒儿**/
        register(itemGranulesTin, "item_granules_tin");
        /**铁粒儿**/
        register(itemGranulesIron, "item_granules_iron");
        /**金粒儿**/
        register(itemGranulesGold, "item_granules_gold");

        /**铜锭**/
        register(itemCopperIngot, "item_copper_ingot");
        /**锡锭**/
        register(itemTinIngot, "item_tin_ingot");

        /**铜镐**/
        register(copperPickaxe, "item_copper_pickaxe");
        /**铜锄头**/
        register(copperHoe, "item_copper_hoe");
        /**铜斧**/
        register(copperAxe, "item_copper_axe");
        /**铜锹**/
        register(copperSpade, "item_copper_spade");
        /**铜剑**/
        register(copperSword, "item_copper_sword");
        /**铜头盔**/
        register(copperHelmet, "item_copper_helmet");
        /**铜胸甲**/
        register(copperChestplate, "item_copper_chestplate");
        /**铜护腿**/
        register(copperLeggings, "item_copper_leggings");
        /**铜鞋**/
        register(copperBoots, "item_copper_boots");

        /**锡镐**/
        register(tinPickaxe, "item_tin_pickaxe");
        /**锡锄头**/
        register(tinHoe, "item_tin_hoe");
        /**锡斧**/
        register(tinAxe, "item_tin_axe");
        /**锡锹**/
        register(tinSpade, "item_tin_spade");
        /**锡剑**/
        register(tinSword, "item_tin_sword");
        /**锡头盔**/
        register(tinHelmet, "item_tin_helmet");
        /**锡胸甲**/
        register(tinChestplate, "item_tin_chestplate");
        /**锡护腿**/
        register(tinLeggings, "item_tin_leggings");
        /**锡鞋**/
        register(tinBoots, "item_tin_boots");
        /**牛奶桶**/
        register(itemBucketMilk, "item_bucket_milk");
        /**汉堡**/
        register(itemBurger, "food_burger");
        /**薯条**/
        register(itemFries, "food_fries");
        /**奶酪**/
        register(itemCheese, "food_cheese");
        /**奶酪汉堡**/
        register(itemCheeseburger, "food_cheese_burger");
    }

    /**
     * 注册任务品
     *
     * @param item
     * @param name
     */
    private static void register(Item item, String name) {
        GameRegistry.registerItem(item.setRegistryName(name));
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 注册材质
     * @Date 20:45 2022/4/15
     * @Param []
     **/
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        /**铜粒儿**/
        registerRender(itemGranulesCopper);
        /**铜锭**/
        registerRender(itemCopperIngot);
        /**锡锭**/
        registerRender(itemTinIngot);

        /**铜镐**/
        registerRender(copperPickaxe);
        /**铜锄头**/
        registerRender(copperHoe);
        /**铜斧子**/
        registerRender(copperAxe);
        /**铜锹**/
        registerRender(copperSpade);
        /**铜剑**/
        registerRender(copperSword);
        /**铜头盔**/
        registerRender(copperHelmet);
        /**铜胸甲**/
        registerRender(copperChestplate);
        /**铜护腿**/
        registerRender(copperLeggings);
        /**铜鞋**/
        registerRender(copperBoots);


        /**锡镐**/
        registerRender(tinPickaxe);
        /**锡锄头**/
        registerRender(tinHoe);
        /**锡斧子**/
        registerRender(tinAxe);
        /**锡锹**/
        registerRender(tinSpade);
        /**锡剑**/
        registerRender(tinSword);
        /**锡头盔**/
        registerRender(tinHelmet);
        /**锡胸甲**/
        registerRender(tinChestplate);
        /**锡护腿**/
        registerRender(tinLeggings);
        /**锡鞋**/
        registerRender(tinBoots);
        /**牛奶桶**/
        registerRender(itemBucketMilk);

        /**汉堡**/
        registerRender(itemBurger);
        /**薯条**/
        registerRender(itemFries);
        /**奶酪**/
        registerRender(itemCheese);
        /**奶酪汉堡**/
        registerRender(itemCheeseburger);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, model);
    }
}
