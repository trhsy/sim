package com.trhsy.sim.common.loader;

import com.trhsy.sim.ModSim;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * 合成表
 */
public class CraftingLoader {

    public CraftingLoader() {
        registerRecipe();
        registerSmelting();
        registerFuel();
    }

    /**
     * 注册合成表
     */
    private static void registerRecipe() {
        //有序合成表
        //for(int x = 0; x < 16; ++x) {
        //    GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.itemWindmillSails, 1, x));
        //}
        /*GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.constructorBox), new Object[]{
                "##", "##", '#', Blocks.vine
        });
        //无需合成
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.vine, 4), BlockLoader.constructorBox);*/

        /*控制箱
        *木板 木板 木板
        *圆石 工作台 圆石
        *圆石 圆石 圆石
        * */
        GameRegistry.addRecipe(new ItemStack(BlockLoader.constructorBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Blocks.crafting_table});
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
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockCheeseBlock, 1), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(ItemLoader.itemFoods, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemFoods, 9, 0), new Object[]{new ItemStack(BlockLoader.blockCheeseBlock)});
        /* 复合砖
        * 硬化黏土 石头 硬化黏土
        * 石头 栅栏 石头
        * 硬化黏土 石头 硬化黏土
        * */
        GameRegistry.addRecipe(new ItemStack(BlockLoader.blockCompositeBrick, 1), new Object[]{"CSC", "SIS", "CSC", 'C', Blocks.hardened_clay, 'S', Blocks.stone, 'I', Blocks.fence});
        /* 风车底座
         *       复合砖
         * 复合砖 复合砖 复合砖
         * 复合砖 复合砖 复合砖
         */
        GameRegistry.addRecipe(new ItemStack(ItemLoader.itemWindmillBase), new Object[]{" C ", "CCC", "CCC", 'C', BlockLoader.blockCompositeBrick});

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
    }

    /**
     * 注册提炼
     */
    private static void registerSmelting() {
        //待烧炼的物品  烧炼后的物品  烧炼后玩家可以得到的经验
        GameRegistry.addSmelting(ItemLoader.itemGranulesGold, new ItemStack(Items.gold_ingot), 0.1F);
        GameRegistry.addSmelting(ItemLoader.itemGranulesIron, new ItemStack(Items.iron_ingot), 0.1F);
        //带创建
        //GameRegistry.addSmelting(ItemLoader.itemGranulesTin, new ItemStack(Items.tin_ingot), 0.1F);
        //GameRegistry.addSmelting(ItemLoader.itemGranulesCopper, new ItemStack(Items.copper_ingot), 0.1F);
    }

    /**
     * 燃料
     */
    private static void registerFuel() {
        //
        /*GameRegistry.registerFuelHandler(new IFuelHandler() {
            @Override
            public int getBurnTime(ItemStack fuel) {
                return Items.diamond != fuel.getItem() ? 0 : 12800;
            }
        });*/
    }
}
