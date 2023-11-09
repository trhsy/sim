package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.item.*;
import com.trhsy.sim.item.ItemBucketMilk;
import com.trhsy.sim.item.armor.ItemCopperArmor;
import com.trhsy.sim.item.armor.ItemTinArmor;
import com.trhsy.sim.item.food.*;
import com.trhsy.sim.item.granules.*;
import com.trhsy.sim.item.tool.ItemCopperTool;
import com.trhsy.sim.item.tool.ItemTinTool;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: ItemLoader
 * @Description: 物品加载注册类
 * @date 2023/10/31 上午 10:13
 */
@Mod.EventBusSubscriber
public class ItemLoader {
    /**
     * 桶装牛奶
     */
    public static Item itemBucketMilk = new ItemBucketMilk();
    /**
     * 啤酒瓶
     **/
    public static Item itemDrinkEmpty = new ItemDrinkEmpty();
    /**
     * 模拟城市启动卷轴
     **/
    public static Item itemSimULoader = new ItemSimULoader();
    /**
     * 风车底座
     **/
    public static Item itemWindmillBase = new ItemWindmillBase();
    /**
     * 风车帆
     **/
    public static Item itemWindmillSails = new ItemWindmillSails();
    /**
     * 风车叶片
     **/
    public static Item itemWindmillVane = new ItemWindmillVane();

    /**
     * 锡镐
     **/
    public static ItemPickaxe tinPickaxe = new ItemTinTool.Pickaxe();
    /**
     * 锡锄头
     **/
    public static ItemHoe tinHoe = new ItemTinTool.Hoe();
    /**
     * 锡斧子
     **/
    public static ItemAxe tinAxe = new ItemTinTool.Axe();
    /**
     * 锡锹
     **/
    public static ItemSpade tinSpade = new ItemTinTool.Spade();
    /**
     * 锡剑
     **/
    public static ItemSword tinSword = new ItemTinTool.Sword();

    /**
     * 铜斧子
     **/
    public static ItemAxe copperAxe = new ItemCopperTool.Axe();
    /**
     * 铜镐
     **/
    public static ItemPickaxe copperPickaxe = new ItemCopperTool.Pickaxe();
    /**
     * 铜锄头
     **/
    public static ItemHoe copperHoe = new ItemCopperTool.Hoe();
    /**
     * 铜锹
     **/
    public static ItemSpade copperSpade = new ItemCopperTool.Spade();
    /**
     * 铜剑
     **/
    public static ItemSword copperSword = new ItemCopperTool.Sword();

    /**
     * 铜头盔
     **/
    public static ItemArmor copperHelmet = new ItemCopperArmor.Helmet();
    /**
     * 铜甲
     **/
    public static ItemArmor copperChestplate = new ItemCopperArmor.Chestplate();
    /**
     * 铜护腿
     **/
    public static ItemArmor copperLeggings = new ItemCopperArmor.Leggings();
    /**
     * 铜鞋
     **/
    public static ItemArmor copperBoots = new ItemCopperArmor.Boots();
    /**
     * 锡头盔
     **/
    public static ItemArmor tinHelmet = new ItemTinArmor.Helmet();
    /**
     * 锡甲
     **/
    public static ItemArmor tinChestplate = new ItemTinArmor.Chestplate();
    /**
     * 锡护腿
     **/
    public static ItemArmor tinLeggings = new ItemTinArmor.Leggings();
    /**
     * 锡鞋
     **/
    public static ItemArmor tinBoots = new ItemTinArmor.Boots();

    /**汉堡**/
    public static ItemFood itemBurger=new ItemBurger();
    /**薯条**/
    public static ItemFood itemFries=new ItemFries();
    /**奶酪**/
    public static ItemFood itemCheese=new ItemCheese();
    /**奶酪汉堡**/
    public static ItemFood itemCheeseburger=new ItemCheeseburger();

    /**
     * 啤酒
     */
    public static Item itemDrink=new ItemDrink();
    /**铜粒儿**/
    public static Item itemGranulesCopper=new ItemGranulesCopper();
    /**铜锭**/
    public static Item itemCopperIngot=new ItemCopperIngot();
    /**金粒**/
    public static Item itemGranulesGold=new ItemGranulesGold();
    /**铁粒**/
    public static Item itemGranulesIron=new ItemGranulesIron();
    /**锡粒**/
    public static Item itemGranulesTin=new ItemGranulesTin();
    /**锡锭**/
    public static Item itemTinIngot=new ItemTinIngot();
    /**
     * 注册物品
     *
     * @param event
     */
    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        //桶装牛奶
        event.getRegistry().register(itemBucketMilk.setRegistryName(ModSim.MODID + ":item_bucket_milk"));
        //啤酒瓶
        event.getRegistry().register(itemDrinkEmpty.setRegistryName(ModSim.MODID + ":beer_bottle"));
        //模拟城市启动卷轴
        event.getRegistry().register(itemSimULoader.setRegistryName(ModSim.MODID + ":item_sim_u_loader"));
        //风车底座
        event.getRegistry().register(itemWindmillBase.setRegistryName(ModSim.MODID + ":item_windmill_base"));
        //风车帆
        event.getRegistry().register(itemWindmillSails.setRegistryName(ModSim.MODID + ":item_windmill_sails"));
        //风车叶片
        event.getRegistry().register(itemWindmillVane.setRegistryName(ModSim.MODID + ":item_windmill_vane"));
        //铜头盔
        event.getRegistry().register(copperHelmet.setRegistryName(ModSim.MODID + ":item_copper_helmet"));
        //铜胸甲
        event.getRegistry().register(copperChestplate.setRegistryName(ModSim.MODID + ":item_copper_chestplate"));
        //铜护腿
        event.getRegistry().register(copperLeggings.setRegistryName(ModSim.MODID + ":item_copper_leggings"));
        //铜鞋
        event.getRegistry().register(copperBoots.setRegistryName(ModSim.MODID + ":item_copper_boots"));

        //铜斧子
        event.getRegistry().register(copperAxe.setRegistryName(ModSim.MODID + ":item_copper_axe"));
        //铜镐
        event.getRegistry().register(copperPickaxe.setRegistryName(ModSim.MODID + ":item_copper_pickaxe"));
        //铜锄头
        event.getRegistry().register(copperHoe.setRegistryName(ModSim.MODID + ":item_copper_hoe"));
        //铜锹
        event.getRegistry().register(copperSpade.setRegistryName(ModSim.MODID + ":item_copper_spade"));
        //铜剑
        event.getRegistry().register(copperSword.setRegistryName(ModSim.MODID + ":item_copper_sword"));

        //锡头盔
        event.getRegistry().register(tinHelmet.setRegistryName(ModSim.MODID + ":item_tin_helmet"));
        //锡胸甲
        event.getRegistry().register(tinChestplate.setRegistryName(ModSim.MODID + ":item_tin_chestplate"));
        //锡护腿
        event.getRegistry().register(tinLeggings.setRegistryName(ModSim.MODID + ":item_tin_leggings"));
        //锡鞋
        event.getRegistry().register(tinBoots.setRegistryName(ModSim.MODID + ":item_tin_boots"));

        //锡斧子
        event.getRegistry().register(tinAxe.setRegistryName(ModSim.MODID + ":item_tin_axe"));
        //锡镐
        event.getRegistry().register(tinPickaxe.setRegistryName(ModSim.MODID + ":item_tin_pickaxe"));
        //锡锄头
        event.getRegistry().register(tinHoe.setRegistryName(ModSim.MODID + ":item_tin_hoe"));
        //锡锹
        event.getRegistry().register(tinSpade.setRegistryName(ModSim.MODID + ":item_tin_spade"));
        //锡剑
        event.getRegistry().register(tinSword.setRegistryName(ModSim.MODID + ":item_tin_sword"));

        //铜粒儿
        event.getRegistry().register(itemGranulesCopper.setRegistryName(ModSim.MODID + ":item_granules_copper"));
        //铜锭
        event.getRegistry().register(itemCopperIngot.setRegistryName(ModSim.MODID + ":item_copper_ingot"));
        //金粒
        event.getRegistry().register(itemGranulesGold.setRegistryName(ModSim.MODID + ":item_granules_gold"));
        //铁粒
        event.getRegistry().register(itemGranulesIron.setRegistryName(ModSim.MODID + ":item_granules_iron"));
        //锡粒
        event.getRegistry().register(itemGranulesTin.setRegistryName(ModSim.MODID + ":item_granules_tin"));
        //锡锭
        event.getRegistry().register(itemTinIngot.setRegistryName(ModSim.MODID + ":item_tin_ingot"));

        //汉堡
        event.getRegistry().register(itemBurger.setRegistryName(ModSim.MODID + ":food_burger"));
        //薯条
        event.getRegistry().register(itemFries.setRegistryName(ModSim.MODID + ":food_fries"));
        //奶酪
        event.getRegistry().register(itemCheese.setRegistryName(ModSim.MODID + ":food_cheese"));
        //奶酪汉堡
        event.getRegistry().register(itemCheeseburger.setRegistryName(ModSim.MODID + ":food_cheese_burger"));
        //啤酒
        event.getRegistry().register(itemDrink.setRegistryName(ModSim.MODID + ":drinks"));

    }

    /**
     * 给物品加上模型材质
     *
     * @param event
     */
    @SubscribeEvent
    public static void registerItemModel(ModelRegistryEvent event) {
        //桶装牛奶
        ModelLoader.setCustomModelResourceLocation(itemBucketMilk, 0, new ModelResourceLocation(itemBucketMilk.getRegistryName(), "inventory"));
        //啤酒瓶
        ModelLoader.setCustomModelResourceLocation(itemDrinkEmpty, 0, new ModelResourceLocation(itemDrinkEmpty.getRegistryName(), "inventory"));
        //模拟城市启动卷轴
        ModelLoader.setCustomModelResourceLocation(itemSimULoader, 0, new ModelResourceLocation(itemSimULoader.getRegistryName(), "inventory"));
        //风车底座
        ModelLoader.setCustomModelResourceLocation(itemWindmillBase, 0, new ModelResourceLocation(itemWindmillBase.getRegistryName(), "inventory"));
        //风车帆
        for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomModelResourceLocation(itemWindmillSails, i, new ModelResourceLocation(itemWindmillSails.getRegistryName() + "" + i, "inventory"));
        }
        //风车叶片
        for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomModelResourceLocation(itemWindmillVane, i, new ModelResourceLocation(itemWindmillVane.getRegistryName() + "" + i, "inventory"));
        }
        //铜头盔
        ModelLoader.setCustomModelResourceLocation(copperHelmet, 0, new ModelResourceLocation(copperHelmet.getRegistryName(), "inventory"));
        //铜胸甲
        ModelLoader.setCustomModelResourceLocation(copperChestplate, 0, new ModelResourceLocation(copperChestplate.getRegistryName(), "inventory"));
        //铜护腿
        ModelLoader.setCustomModelResourceLocation(copperLeggings, 0, new ModelResourceLocation(copperLeggings.getRegistryName(), "inventory"));
        //铜鞋
        ModelLoader.setCustomModelResourceLocation(copperBoots, 0, new ModelResourceLocation(copperBoots.getRegistryName(), "inventory"));
        //锡头盔
        ModelLoader.setCustomModelResourceLocation(tinHelmet, 0, new ModelResourceLocation(tinHelmet.getRegistryName(), "inventory"));
        //锡胸甲
        ModelLoader.setCustomModelResourceLocation(tinChestplate, 0, new ModelResourceLocation(tinChestplate.getRegistryName(), "inventory"));
        //锡护腿
        ModelLoader.setCustomModelResourceLocation(tinLeggings, 0, new ModelResourceLocation(tinLeggings.getRegistryName(), "inventory"));
        //锡鞋
        ModelLoader.setCustomModelResourceLocation(tinBoots, 0, new ModelResourceLocation(tinBoots.getRegistryName(), "inventory"));

        //铜斧子
        ModelLoader.setCustomModelResourceLocation(copperAxe, 0, new ModelResourceLocation(copperAxe.getRegistryName(), "inventory"));
        //铜镐
        ModelLoader.setCustomModelResourceLocation(copperPickaxe, 0, new ModelResourceLocation(copperPickaxe.getRegistryName(), "inventory"));
        //铜锄头
        ModelLoader.setCustomModelResourceLocation(copperHoe, 0, new ModelResourceLocation(copperHoe.getRegistryName(), "inventory"));
        //铜锹
        ModelLoader.setCustomModelResourceLocation(copperSpade, 0, new ModelResourceLocation(copperSpade.getRegistryName(), "inventory"));
        //铜剑
        ModelLoader.setCustomModelResourceLocation(copperSword, 0, new ModelResourceLocation(copperSword.getRegistryName(), "inventory"));

        //锡斧子
        ModelLoader.setCustomModelResourceLocation(tinAxe, 0, new ModelResourceLocation(tinAxe.getRegistryName(), "inventory"));
        //锡镐
        ModelLoader.setCustomModelResourceLocation(tinPickaxe, 0, new ModelResourceLocation(tinPickaxe.getRegistryName(), "inventory"));
        //锡锄头
        ModelLoader.setCustomModelResourceLocation(tinHoe, 0, new ModelResourceLocation(tinHoe.getRegistryName(), "inventory"));
        //锡锹
        ModelLoader.setCustomModelResourceLocation(tinSpade, 0, new ModelResourceLocation(tinSpade.getRegistryName(), "inventory"));
        //锡剑
        ModelLoader.setCustomModelResourceLocation(tinSword, 0, new ModelResourceLocation(tinSword.getRegistryName(), "inventory"));


        //铜粒儿
        ModelLoader.setCustomModelResourceLocation(itemGranulesCopper, 0, new ModelResourceLocation(itemGranulesCopper.getRegistryName(), "inventory"));
        //铜锭
        ModelLoader.setCustomModelResourceLocation(itemCopperIngot, 0, new ModelResourceLocation(itemCopperIngot.getRegistryName(), "inventory"));
        //金粒
        ModelLoader.setCustomModelResourceLocation(itemGranulesGold, 0, new ModelResourceLocation(itemGranulesGold.getRegistryName(), "inventory"));
        //铁粒
        ModelLoader.setCustomModelResourceLocation(itemGranulesIron, 0, new ModelResourceLocation(itemGranulesIron.getRegistryName(), "inventory"));
        //锡粒
        ModelLoader.setCustomModelResourceLocation(itemGranulesTin, 0, new ModelResourceLocation(itemGranulesTin.getRegistryName(), "inventory"));

        //锡锭
        ModelLoader.setCustomModelResourceLocation(itemTinIngot, 0, new ModelResourceLocation(itemTinIngot.getRegistryName(), "inventory"));
        //汉堡
        ModelLoader.setCustomModelResourceLocation(itemBurger, 0, new ModelResourceLocation(itemBurger.getRegistryName(), "inventory"));
        //薯条
        ModelLoader.setCustomModelResourceLocation(itemFries, 0, new ModelResourceLocation(itemFries.getRegistryName(), "inventory"));
        //奶酪
        ModelLoader.setCustomModelResourceLocation(itemCheese, 0, new ModelResourceLocation(itemCheese.getRegistryName(), "inventory"));
        //奶酪汉堡
        ModelLoader.setCustomModelResourceLocation(itemCheeseburger, 0, new ModelResourceLocation(itemCheeseburger.getRegistryName(), "inventory"));
        //啤酒
        ModelLoader.setCustomModelResourceLocation(itemDrink, 0, new ModelResourceLocation(itemDrink.getRegistryName(), "inventory"));


    }
}
