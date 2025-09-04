package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.group.ModGroup;
import com.trhsy.sim.group.SimGroup;
import com.trhsy.sim.items.ItemSimULoader;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import javax.swing.*;
// 关键：标记为事件订阅者，bus = Bus.MOD 表示监听 MOD 生命周期事件
@Mod.EventBusSubscriber(modid = ModSim.MODID, bus = Bus.MOD) // 替换 MOD_ID 为你的模组ID
public class BlocksLoader {
    // 1. 定义方块实例（静态常量，全局唯一）
    // 命名规范：小写+下划线，如 "constructor_box"
    public static final Block CONSTRUCTOR_BOX = new BlockConstructorBox()
            .setRegistryName(ModSim.MODID, "constructor_box"); // 必须设置注册名（模组ID:方块名）

    // 2. 方块注册事件：监听 Forge 的 Block 注册事件
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry(); // 获取方块注册表

        // 将自定义方块注册到注册表
        registry.register(CONSTRUCTOR_BOX);

        // （可选）注册日志，确认注册成功
        ModSim.log.info("已注册方块：{}", CONSTRUCTOR_BOX.getRegistryName());
    }

    // 3. 方块物品（BlockItem）注册事件：监听 Forge 的 Item 注册事件
    // 注意：1.13.2 中，方块必须关联 BlockItem 才能显示在创造栏/被玩家手持
    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry(); // 获取物品注册表

        // 为 BlockConstructorBox 创建对应的 BlockItem
        ItemSimULoader itemSimULoader = new ItemSimULoader(
                new Item.Properties() // 配置 BlockItem 属性（如创造栏分组）
                        .group(ModGroup.itemGroup) // 加入"建筑方块"创造栏（可替换为自定义栏）
        );

        // BlockItem 的注册名必须与方块一致（否则会出现模型异常）
        itemSimULoader.setRegistryName("simReel");

        // 将 BlockItem 注册到注册表
        registry.register(itemSimULoader);

        // （可选）注册日志，确认 BlockItem 成功
        ModSim.log.info("已注册方块物品：{}", itemSimULoader.getRegistryName());
    }
}
