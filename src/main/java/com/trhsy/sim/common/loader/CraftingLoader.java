package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ItemLoader;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @ClassName CraftingLoader
 * @Description todo 合成表
 * @Author Tian
 * @Date 2022/5/1122:15
 **/
public class CraftingLoader {
    public CraftingLoader() {
        registerRecipe();
        registerSmelting();
        registerFuel();
    }

    /***
     * @Author fan
     * @Description //TODO 注册菜谱
     * @Date 22:17 2022/5/11
     * @Param []
     * @return void
     **/
    private static void registerRecipe() {
        /*控制箱
         *木板 木板 木板
         *圆石 工作台 圆石
         *圆石 圆石 圆石
         * */
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockConstructorBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Blocks.crafting_table});
        /* 标记棒
         * 黄色燃料
         * 木棍
         * 木棍
         * */
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockMarker, 3), new Object[]{"G", "S", 'S', Items.stick, 'G', new ItemStack(Items.dye, 1, 11)});
        //采矿箱和养殖箱是否弃用最贵的钻石镐合成
        if (ConfigLoader.configUseExpensiveRecipies) {
            /** 采矿箱 养殖箱
             * 木板 木板 木板
             * 圆石 镐子/锄头 圆石
             * 圆石 圆石 圆石
             */
            GameRegistry.addRecipe(new ItemStack(BlockLoader.blockMiningBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.diamond_pickaxe});
            GameRegistry.addRecipe(new ItemStack(BlockLoader.blockFarmingBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.diamond_hoe});
        } else {
            GameRegistry.addRecipe(new ItemStack(BlockLoader.blockMiningBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.stone_pickaxe});
            GameRegistry.addRecipe(new ItemStack(BlockLoader.blockFarmingBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.stone_hoe});
        }
        //四个火把合成灯箱
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockLightBox, 2), new Object[]{"LL", "LL", 'L', Blocks.torch});
        GameRegistry.addShapelessRecipe(new ItemStack(BlockLoader.blockLightBox, 1, 1), new Object[]{BlockLoader.blockLightBox, new ItemStack(Items.dye, 1, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(BlockLoader.blockLightBox, 1, 2), new Object[]{BlockLoader.blockLightBox, new ItemStack(Items.dye, 1, 14)});
        GameRegistry.addShapelessRecipe(new ItemStack(BlockLoader.blockLightBox, 1, 3), new Object[]{BlockLoader.blockLightBox, new ItemStack(Items.dye, 1, 11)});
        GameRegistry.addShapelessRecipe(new ItemStack(BlockLoader.blockLightBox, 1, 4), new Object[]{BlockLoader.blockLightBox, new ItemStack(Items.dye, 1, 10)});
        GameRegistry.addShapelessRecipe(new ItemStack(BlockLoader.blockLightBox, 1, 5), new Object[]{BlockLoader.blockLightBox, new ItemStack(Items.dye, 1, 4)});
        GameRegistry.addShapelessRecipe(new ItemStack(BlockLoader.blockLightBox, 1, 6), new Object[]{BlockLoader.blockLightBox, new ItemStack(Items.dye, 1, 5)});
        GameRegistry.addShapelessRecipe(new ItemStack(BlockLoader.blockLightBox, 1, 7), new Object[]{BlockLoader.blockLightBox, new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 14), new ItemStack(Items.dye, 1, 11), new ItemStack(Items.dye, 1, 10), new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 5)});
        //九个奶酪片合成奶酪块
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockCheese, 1), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(ItemLoader.itemCheese, 1, 0)});
        //一个奶酪块分解九奶酪片
        GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemCheese, 9), BlockLoader.blockCheese);
        /* 复合砖
         * 硬化黏土 石头 硬化黏土
         * 石头 栅栏 石头
         * 硬化黏土 石头 硬化黏土
         * */
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockCompositeBrick, 1), new Object[]{"CSC", "SIS", "CSC", 'C', Blocks.hardened_clay, 'S', Blocks.stone, 'I', Blocks.oak_fence});
        /* 风车底座
         *       复合砖
         * 复合砖 复合砖 复合砖
         * 复合砖 复合砖 复合砖
         */
        GameRegistry.addRecipe(new ItemStack(ItemLoader.itemWindmillBase), new Object[]{" C ", "CCC", "CCC", 'C', BlockLoader.blockCompositeBrick});
        /*城市箱
        *木板 木板 木板
        *圆石 建筑箱 圆石
        *圆石 圆石 圆石
         */
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockControlBox, 1), new Object[]{
        "PPP", "CWC", "CCC",
        'C', Blocks.cobblestone,
        'P', Blocks.planks,
        'W', BlockLoader.blockConstructorBox
        });

        int c;
        for (c = 0; c < 16; ++c) {
            /** 风车叶片
             *木棍 木棍 木棍
             *羊毛 羊毛 羊毛
             */
            GameRegistry.addRecipe(new ItemStack(ItemLoader.itemWindmillVane, 1, c), new Object[]{"WWW", "SSS", 'S', Items.stick, 'W', new ItemStack(Blocks.wool, 1, c)});
        }

        for (c = 0; c < 16; ++c) {
            /** 风车帆
             *      叶片
             * 叶片 木板 叶片
             *     叶片
             */
            GameRegistry.addRecipe(new ItemStack(ItemLoader.itemWindmillSails, 1, c), new Object[]{" V ", "VPV", " V ", 'V', new ItemStack(ItemLoader.itemWindmillVane, 1, c), 'P', Blocks.planks});
        }

        for (c = 0; c < 16; ++c) {
            /** 风车
             * 风车帆
             * 风车底座
             *
             */
            GameRegistry.addRecipe(new ItemStack(BlockLoader.blockWindmill, 1, c), new Object[]{"S", "B", 'S', new ItemStack(ItemLoader.itemWindmillSails, 1, c), 'B', ItemLoader.itemWindmillBase});

        }
        /**铜块**/
        GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.blockCopper), new Object[]
                {
                        "###", "###", "###", '#', ItemLoader.itemCopperIngot
                });
        /**一个铜块等于九个铜锭**/
        GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemCopperIngot, 9), BlockLoader.blockCopper);
        /**铜镐
         * 铜锭 铜锭 铜锭
         *     木棍
         *     木棍
         * **/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperPickaxe), new Object[]
                {
                        "###", " * ", " * ", '#', ItemLoader.itemCopperIngot, '*', Items.stick
                });
        /**铜锄头**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperHoe), new Object[]
                {
                        "## ", " * ", " * ", '#', ItemLoader.itemCopperIngot, '*', Items.stick
                });
        /**铜斧子**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperAxe), new Object[]
                {
                        "## ", "#* ", " * ", '#', ItemLoader.itemCopperIngot, '*', Items.stick
                });
        /**铜锹**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperSpade), new Object[]
                {
                        " # ", " * ", " * ", '#', ItemLoader.itemCopperIngot, '*', Items.stick
                });
        /**铜剑**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperSword), new Object[]
                {
                        " # ", " # "," * ", '#', ItemLoader.itemCopperIngot, '*', Items.stick
                });
        /**铜头盔**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperHelmet), new Object[]
                {
                        "###", "# #", '#', ItemLoader.itemCopperIngot
                });
        /**铜甲**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperChestplate), new Object[]
                {
                        "# #", "###", "###", '#', ItemLoader.itemCopperIngot
                });
        /**铜护腿**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperLeggings), new Object[]
                {
                        "###", "# #", "# #", '#', ItemLoader.itemCopperIngot
                });
        /**铜鞋**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.copperBoots), new Object[]
                {
                        "# #", "# #", '#', ItemLoader.itemCopperIngot
                });

        /**锡块**/
        GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.blockTin), new Object[]
                {
                        "###", "###", "###", '#', ItemLoader.itemTinIngot
                });
        /**一个铜块等于九个铜锭**/
        GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemTinIngot, 9), BlockLoader.blockTin);
        /**锡镐
         * 锡锭 锡锭 锡锭
         *     木棍
         *     木棍
         * **/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinPickaxe), new Object[]
                {
                        "###", " * ", " * ", '#', ItemLoader.itemTinIngot, '*', Items.stick
                });
        /**锡锄头**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinHoe), new Object[]
                {
                        "## ", " * ", " * ", '#', ItemLoader.itemTinIngot, '*', Items.stick
                });
        /**锡斧子**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinAxe), new Object[]
                {
                        "## ", "#* ", " * ", '#', ItemLoader.itemTinIngot, '*', Items.stick
                });
        /**锡锹**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinSpade), new Object[]
                {
                        " # ", " * ", " * ", '#', ItemLoader.itemTinIngot, '*', Items.stick
                });
        /**锡剑**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinSword), new Object[]
                {
                        " # ", " # "," * ", '#', ItemLoader.itemTinIngot, '*', Items.stick
                });
        /**锡头盔**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinHelmet), new Object[]
                {
                        "###", "# #", '#', ItemLoader.itemTinIngot
                });
        /**锡甲**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinChestplate), new Object[]
                {
                        "# #", "###", "###", '#', ItemLoader.itemTinIngot
                });
        /**锡护腿**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinLeggings), new Object[]
                {
                        "###", "# #", "# #", '#', ItemLoader.itemTinIngot
                });
        /**锡鞋**/
        GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.tinBoots), new Object[]
                {
                        "# #", "# #", '#', ItemLoader.itemTinIngot
                });
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO  注册熔炼
     * @Date 22:17 2022/5/11
     * @Param []
     **/
    private static void registerSmelting() {
        //待烧炼的物品  烧炼后的物品  烧炼后玩家可以得到的经验
        //烧金粒得金锭
        GameRegistry.addSmelting(ItemLoader.itemGranulesGold, new ItemStack(Items.gold_ingot), 0.1F);
        //烧铁粒得铁锭
        GameRegistry.addSmelting(ItemLoader.itemGranulesIron, new ItemStack(Items.iron_ingot), 0.1F);
        //烧锡粒得锡锭
        GameRegistry.addSmelting(ItemLoader.itemGranulesTin, new ItemStack(ItemLoader.itemTinIngot), 0.1F);
        //烧铜粒得铜锭
        GameRegistry.addSmelting(ItemLoader.itemGranulesCopper, new ItemStack(ItemLoader.itemCopperIngot), 0.1F);
        //烧锡矿得锡锭
        GameRegistry.addSmelting(BlockLoader.blockTinOre, new ItemStack(ItemLoader.itemTinIngot), 0.1F);
        //烧铜矿得铜锭
        GameRegistry.addSmelting(BlockLoader.blockCopperOre, new ItemStack(ItemLoader.itemCopperIngot), 0.1F);
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 注册燃料
     * @Date 22:17 2022/5/11
     * @Param []
     **/
    private static void registerFuel() {

    }
}
