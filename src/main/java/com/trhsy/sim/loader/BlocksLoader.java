package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.*;
import com.trhsy.sim.group.ModGroup;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import javax.swing.*;
// 关键：标记为事件订阅者，bus = Bus.MOD 表示监听 MOD 生命周期事件
@Mod.EventBusSubscriber(modid = ModSim.MODID, bus = Bus.MOD) // 替换 MOD_ID 为你的模组ID
public class BlocksLoader {
    // 1. 定义方块实例（静态常量，全局唯一）
    // 命名规范：小写+下划线，如 "constructor_box"
    //建筑箱
    public static final Block CONSTRUCTOR_BOX = new BlockConstructorBox().setRegistryName(ModSim.MODID, "constructor_box"); // 必须设置注册名（模组ID:方块名）
    //奶酪
    public static final Block BLOCK_CHEESE = new BlockCheese().setRegistryName(ModSim.MODID,"cheese_block");
    //复合砖
    public static final Block COMPOSITE_BRICK = new BlockCompositeBrick().setRegistryName(ModSim.MODID,"composite_brick");
    //控制箱
    public static final Block CONTROL_BOX = new BlockControlBox().setRegistryName(ModSim.MODID,"control_box");
//    public static RegistryObject<Block> CONTROL_BOX = BLOCKS.register("control_box", () -> {
//        return new BlockControlBox();
//    });
    //铜块
    public static final Block COPPER_BLOCK = new BlockCopper().setRegistryName(ModSim.MODID,"copper_block");
    //铜矿
    public static final Block COPPER_BLOCK_ORE = new BlockCopperOre().setRegistryName(ModSim.MODID,"copper_block_ore");
    //农田箱
    public static final Block FARMING_BOX = new BlockFarmingBox().setRegistryName(ModSim.MODID,"farming_box");
    //流体牛奶块
//    public static final Block FLUID_WING_MILK = new BlockFlowingMilk().setRegistryName(ModSim.MODID,"fluid_wing_milk");
    //灯箱
    public static final Block LIGHT_BOX = new BlockLightBox().setRegistryName(ModSim.MODID,"light_box");
    //地毯
//    public static final Block LIVING_BLOCK = new BlockLiving().setRegistryName(ModSim.MODID,"living_block");
    //标记棒
    public static final Block MARKER_BAR = new BlockMarker().setRegistryName(ModSim.MODID,"marker_bar");
    //静态牛奶块
//    public static final Block FLUID_MILK = new BlockMilk().setRegistryName(ModSim.MODID,"fluid_milk");
    //采矿箱
    public static final Block MINING_BOX = new BlockMiningBox().setRegistryName(ModSim.MODID,"mining_box");
    //路径箱
    public static final Block PATH_BOX = new BlockPathBox().setRegistryName(ModSim.MODID,"path_box");
    //特除空气方块
//    public static final Block SPECIAL_BLOCK = new BlockSpecial().setRegistryName(ModSim.MODID,"special_block");
    //锡块
    public static final Block TIN_BLOCK = new BlockTin().setRegistryName(ModSim.MODID,"tin_block");
    //锡矿
    public static final Block TIN_BLOCK_ORE = new BlockTinOre().setRegistryName(ModSim.MODID,"tin_block_ore");
    //风车
    public static final Block windmill = new BlockWindmill().setRegistryName(ModSim.MODID,"windmill");
    // 2. 方块注册事件：监听 Forge 的 Block 注册事件
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry(); // 获取方块注册表

        // 将自定义方块注册到注册表
        registry.register(CONSTRUCTOR_BOX);
        // （可选）注册日志，确认注册成功
        ModSim.log.info("已注册方块：{}", CONSTRUCTOR_BOX.getRegistryName());
        registry.register(BLOCK_CHEESE);
        ModSim.log.info("已注册方块：{}", BLOCK_CHEESE.getRegistryName());

        registry.register(COMPOSITE_BRICK);
        ModSim.log.info("已注册方块：{}", COMPOSITE_BRICK.getRegistryName());
        registry.register(CONTROL_BOX);
        ModSim.log.info("已注册方块：{}", CONTROL_BOX.getRegistryName());
        registry.register(COPPER_BLOCK);
        ModSim.log.info("已注册方块：{}", COPPER_BLOCK.getRegistryName());
        registry.register(COPPER_BLOCK_ORE);
        ModSim.log.info("已注册方块：{}", COPPER_BLOCK_ORE.getRegistryName());
        registry.register(FARMING_BOX);
        ModSim.log.info("已注册方块：{}", FARMING_BOX.getRegistryName());
//        registry.register(FLUID_WING_MILK);
//        ModSim.log.info("已注册方块：{}", FLUID_WING_MILK.getRegistryName());
        registry.register(LIGHT_BOX);
        ModSim.log.info("已注册方块：{}", LIGHT_BOX.getRegistryName());
//        registry.register(LIVING_BLOCK);
//        ModSim.log.info("已注册方块：{}", LIVING_BLOCK.getRegistryName());
        registry.register(MARKER_BAR);
        ModSim.log.info("已注册方块：{}", MARKER_BAR.getRegistryName());
//        registry.register(FLUID_MILK);
//        ModSim.log.info("已注册方块：{}", FLUID_MILK.getRegistryName());
        registry.register(MINING_BOX);
        ModSim.log.info("已注册方块：{}", MINING_BOX.getRegistryName());
        registry.register(PATH_BOX);
        ModSim.log.info("已注册方块：{}", PATH_BOX.getRegistryName());
//        registry.register(SPECIAL_BLOCK);
//        ModSim.log.info("已注册方块：{}", SPECIAL_BLOCK.getRegistryName());
        registry.register(TIN_BLOCK);
        ModSim.log.info("已注册方块：{}", TIN_BLOCK.getRegistryName());
        registry.register(TIN_BLOCK_ORE);
        ModSim.log.info("已注册方块：{}", TIN_BLOCK_ORE.getRegistryName());
        registry.register(windmill);
        ModSim.log.info("已注册方块：{}", windmill.getRegistryName());

    }

    // 3. 方块物品（BlockItem）注册事件：监听 Forge 的 Item 注册事件
    // 注意：1.13.2 中，方块必须关联 BlockItem 才能显示在创造栏/被玩家手持
    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry(); // 获取物品注册表

        // 为 BlockConstructorBox 创建对应的 BlockItem
        ItemBlock constructorBoxItem = new ItemBlock(
                CONSTRUCTOR_BOX, // 关联已注册的方块
                new Item.Properties().group(ModGroup.itemGroup) // 加入创造栏
        );
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        constructorBoxItem.setRegistryName(CONSTRUCTOR_BOX.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(constructorBoxItem);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", CONSTRUCTOR_BOX.getRegistryName());

        ItemBlock BlockCheeseItem = new ItemBlock(
                BLOCK_CHEESE, // 关联已注册的方块
                new Item.Properties().group(ModGroup.itemGroup) // 加入创造栏
        );
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        BlockCheeseItem.setRegistryName(BLOCK_CHEESE.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(BlockCheeseItem);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", BLOCK_CHEESE.getRegistryName());

        ItemBlock COMPOSITE_BRICK_ITEM = new ItemBlock(COMPOSITE_BRICK, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        COMPOSITE_BRICK_ITEM.setRegistryName(COMPOSITE_BRICK.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(COMPOSITE_BRICK_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", COMPOSITE_BRICK.getRegistryName());

        ItemBlock CONTROL_BOX_ITEM = new ItemBlock(CONTROL_BOX, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        CONTROL_BOX_ITEM.setRegistryName(CONTROL_BOX.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(CONTROL_BOX_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", CONTROL_BOX.getRegistryName());

        ItemBlock COPPER_BLOCK_ITEM = new ItemBlock(COPPER_BLOCK, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        COPPER_BLOCK_ITEM.setRegistryName(COPPER_BLOCK.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(COPPER_BLOCK_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", COPPER_BLOCK.getRegistryName());

        ItemBlock COPPER_BLOCK_ORE_ITEM = new ItemBlock(COPPER_BLOCK_ORE, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        COPPER_BLOCK_ORE_ITEM.setRegistryName(COPPER_BLOCK_ORE.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(COPPER_BLOCK_ORE_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", COPPER_BLOCK_ORE.getRegistryName());

        ItemBlock FARMING_BOX_ITEM = new ItemBlock(FARMING_BOX, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        FARMING_BOX_ITEM.setRegistryName(FARMING_BOX.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(FARMING_BOX_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", FARMING_BOX.getRegistryName());

       /* ItemBlock FLUID_WING_MILK_ITEM = new ItemBlock(FLUID_WING_MILK, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        FLUID_WING_MILK_ITEM.setRegistryName(FLUID_WING_MILK.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(FLUID_WING_MILK_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", FLUID_WING_MILK.getRegistryName());*/

        ItemBlock LIGHT_BOX_ITEM = new ItemBlock(LIGHT_BOX, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        LIGHT_BOX_ITEM.setRegistryName(LIGHT_BOX.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(LIGHT_BOX_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", LIGHT_BOX.getRegistryName());

        /*ItemBlock LIVING_BLOCK_ITEM = new ItemBlock(LIVING_BLOCK, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        LIVING_BLOCK_ITEM.setRegistryName(LIVING_BLOCK.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(LIVING_BLOCK_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", LIVING_BLOCK.getRegistryName());*/

        ItemBlock MARKER_BAR_ITEM = new ItemBlock(MARKER_BAR, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        MARKER_BAR_ITEM.setRegistryName(MARKER_BAR.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(MARKER_BAR_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", MARKER_BAR.getRegistryName());

        /*ItemBlock FLUID_MILK_ITEM = new ItemBlock(FLUID_MILK, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        FLUID_MILK_ITEM.setRegistryName(FLUID_MILK.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(FLUID_MILK_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", FLUID_MILK.getRegistryName());*/

        ItemBlock MINING_BOX_ITEM = new ItemBlock(MINING_BOX, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        MINING_BOX_ITEM.setRegistryName(MINING_BOX.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(MINING_BOX_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", MINING_BOX.getRegistryName());

        ItemBlock PATH_BOX_ITEM = new ItemBlock(PATH_BOX, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        PATH_BOX_ITEM.setRegistryName(PATH_BOX.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(PATH_BOX_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", PATH_BOX.getRegistryName());

        /*ItemBlock SPECIAL_BLOCK_ITEM = new ItemBlock(SPECIAL_BLOCK, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        SPECIAL_BLOCK_ITEM.setRegistryName(SPECIAL_BLOCK.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(SPECIAL_BLOCK_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", SPECIAL_BLOCK.getRegistryName());*/

        ItemBlock TIN_BLOCK_ITEM = new ItemBlock(TIN_BLOCK, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        TIN_BLOCK_ITEM.setRegistryName(TIN_BLOCK.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(TIN_BLOCK_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", TIN_BLOCK.getRegistryName());

        ItemBlock TIN_BLOCK_ORE_ITEM = new ItemBlock(TIN_BLOCK_ORE, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        TIN_BLOCK_ORE_ITEM.setRegistryName(TIN_BLOCK_ORE.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(TIN_BLOCK_ORE_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", TIN_BLOCK_ORE.getRegistryName());

        ItemBlock WINDMILL_ITEM = new ItemBlock(windmill, new Item.Properties().group(ModGroup.itemGroup));
        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        WINDMILL_ITEM.setRegistryName(windmill.getRegistryName());
        // 将 BlockItem 注册到注册表
        registry.register(WINDMILL_ITEM);
        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", windmill.getRegistryName());

    }
}
