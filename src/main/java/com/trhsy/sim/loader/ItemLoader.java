package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.item.ItemBucketMilk;
import com.trhsy.sim.item.ItemWindmillBase;
import com.trhsy.sim.item.ItemWindmillSails;
import com.trhsy.sim.item.ItemWindmillVane;
import com.trhsy.sim.item.armor.ItemCopperArmor;
import com.trhsy.sim.item.armor.ItemTinArmor;
import com.trhsy.sim.item.food.ItemBurger;
import com.trhsy.sim.item.food.ItemCheese;
import com.trhsy.sim.item.food.ItemCheeseburger;
import com.trhsy.sim.item.food.ItemFries;
import com.trhsy.sim.item.granules.*;
import com.trhsy.sim.item.tool.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * 物品加载类
 * @author Trhsy
 */
public class ItemLoader {
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
    /**风车底座**/
    public static Item itemWindmillBase=new ItemWindmillBase();
    /**风车帆**/
    public static Item itemWindmillSails=new ItemWindmillSails();
    /**风车叶片**/
    public static Item itemWindmillVane=new ItemWindmillVane();

    /**汉堡**/
    public static ItemFood itemBurger=new ItemBurger();
    /**薯条**/
    public static ItemFood itemFries=new ItemFries();
    /**奶酪**/
    public static ItemFood itemCheese=new ItemCheese();
    /**奶酪汉堡**/
    public static ItemFood itemCheeseburger=new ItemCheeseburger();

    /**铜斧子**/
    public static Item copperAxe = new ItemCopperAxe();
    /**铜镐**/
    public static ItemPickaxe copperPickaxe = new ItemCopperPickaxe();
    /**铜锄头**/
    public static ItemHoe copperHoe = new ItemCopperHoe();
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
    public static Item tinAxe = new ItemTinAxe();
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
    public static ItemBucket itemBucketMilk= new ItemBucketMilk();
    /**
     * 加载物品
     *
     * @param event
     */
    public ItemLoader(FMLPreInitializationEvent event) {
        try {
            /**铜粒儿**/
            register(itemGranulesCopper,"item_granules_copper");
            /**铜锭**/
            register(itemCopperIngot,"item_copper_ingot");
            /**金粒**/
            register(itemGranulesGold,"item_granules_gold");
            /**铁粒**/
            register(itemGranulesIron,"item_granules_iron");
            /**锡粒**/
            register(itemGranulesTin,"item_granules_tin");
            /**锡锭**/
            register(itemTinIngot,"item_tin_ingot");
            /**风车底座**/
            register(itemWindmillBase,"item_windmill_base");
            /**风车帆**/
            register(itemWindmillSails,"item_windmill_sails");
            /**风车叶片**/
            register(itemWindmillVane,"item_windmill_vane");
            /**汉堡**/
            register(itemBurger,"food_burger");
            /**薯条**/
            register(itemFries,"food_fries");
            /**奶酪**/
            register(itemCheese,"food_cheese");
            /**奶酪汉堡**/
            register(itemCheeseburger,"food_cheese_burger");

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
            FluidContainerRegistry.registerFluidContainer(FluidLoader.fluidMilk, new ItemStack(itemBucketMilk), FluidContainerRegistry.EMPTY_BUCKET);
        }catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * 注册任务品
     *
     * @param item
     * @param name
     */
    private static void register(Item item, String name) {
        try {
            //GameRegistry.registerItem(item.setRegistryName(name));
            GameRegistry.register(item.setRegistryName(name));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-register出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
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
        try {
            /**铜粒儿**/
            registerRender(itemGranulesCopper);
            /**铜锭**/
            registerRender(itemCopperIngot);
            /**金粒儿**/
            registerRender(itemGranulesGold);
            /**铁粒儿**/
            registerRender(itemGranulesIron);
            /**锡粒儿**/
            registerRender(itemGranulesTin);
            /**锡锭**/
            registerRender(itemTinIngot);
            /**汉堡**/
            registerRender(itemBurger);
            /**薯条**/
            registerRender(itemFries);
            /**奶酪**/
            registerRender(itemCheese);
            /**奶酪汉堡**/
            registerRender(itemCheeseburger);

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

            /**风车底座**/
            registerRender(itemWindmillBase);
            /**风车帆**/
            for (int i = 0; i < 16; i++) {
                registerRender(itemWindmillSails,i,"item_windmill_sails"+i);
            }
            /**风车叶片**/
            for (int i = 0; i < 16; i++) {
                registerRender(itemWindmillVane,i,"item_windmill_vane"+i);
            }
            /**牛奶桶**/
            registerRender(itemBucketMilk);
        }catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        /**ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
         ModelLoader.setCustomModelResourceLocation(item, 0, model);**/
        try {
            ResourceLocation resourceLocation=item.getRegistryName();
            registerRender(item, 0, resourceLocation.getResourcePath());
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-registerRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item,int meta,String name) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(ModSim.MODID,name);
            ModelResourceLocation model = new ModelResourceLocation(resourcelocation, "inventory");
            ModelLoader.setCustomModelResourceLocation(item, meta, model);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-registerRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    protected static <T extends Item> T registerItem(T item, String name) {
        try {
                GameRegistry.registerItem(item, name);

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerItem出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return item;
    }
}
