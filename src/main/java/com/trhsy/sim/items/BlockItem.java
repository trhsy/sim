package com.trhsy.sim.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;

/**
 * BlockItem 类，用于将方块包装为可手持/放置的物品
 */
public class BlockItem extends ItemBlock {
    // 关联的方块实例
    private final Block block;

    /**
     * 构造函数：创建方块对应的物品
     * @param block 关联的方块
     * @param properties 物品属性（如创造栏分组、最大堆叠数等）
     */
    public BlockItem(Block block, Item.Properties properties) {
        super(block,properties);
        this.block = block;
    }

    /**
     * 获取关联的方块
     */
    @Override
    public Block getBlock() {
        return this.block;
    }



    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        // 自定义逻辑（如前置检查）
        // ...

        // 调用原版逻辑执行放置（关键！）
        return super.onItemUse(context);
    }

}
