package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * 复合砖
 */
public class BlockCompositeBrick extends Block {
    public BlockCompositeBrick() {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(8.0F,7.0F));
//        this.setHardness(8.0F);
//        this.setResistance(7.0F);
        //设置挖掘工具、挖掘等级
//        this.setHarvestLevel("pickaxe",0);
//        this.setUnlocalizedName("composite_brick");
//        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag advanced) {
        // 从 lang 文件读取翻译（格式：block.modid.composite_brick.tooltip）
        String fs_1 = new TextComponentTranslation("block.sim.composite_brick.tooltip", new Object[0]).getUnformattedComponentText();
        tooltip.add(fs_1);
    }
}
