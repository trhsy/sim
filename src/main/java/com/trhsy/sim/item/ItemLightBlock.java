package com.trhsy.sim.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @ClassName ItemLightBlock
 * @Description todo
 * @Author Tian
 * @Date 2022/9/1817:53
 **/
public class ItemLightBlock extends ItemBlock {
    public ItemLightBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    public int getMetadata(int damage) {
        return damage;
    }
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + stack.getMetadata();
    }
}
