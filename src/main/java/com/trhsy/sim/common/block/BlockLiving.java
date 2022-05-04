package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;

/**
 * @ClassName BlockLivingBlock
 * @Description todo
 * @Author Tian
 * @Date 2022/5/414:09
 **/
public class BlockLiving extends BlockCarpet {
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    public BlockLiving() {
        //super(Material.wood,TYPE,EnumBlockLivingBlock.class);
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("livingBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
