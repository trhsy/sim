package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * @ClassName BlockLightBox
 * @Description todo 灯箱
 * @Author Tian
 * @Date 2022/5/49:58
 **/
public class BlockLightBox extends EnumBlock<EnumBlockLightBox> {
    public static final PropertyEnum<EnumBlockLightBox> TYPE = PropertyEnum.func_177709_a("type", EnumBlockLightBox.class);
    public BlockLightBox() {
        super(Material.field_151575_d,TYPE,EnumBlockLightBox.class);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBlockLightBox.WHITE));
        this.func_149715_a(1.0F);
        this.func_149647_a(CreativeTabsLoader.tabSimU);
        this.func_149672_a(Block.field_149766_f);
        this.func_149711_c(2.0F);
        this.func_149752_b(1.0F);
        this.func_149675_a(true);
        //this.setTextureName(ModSim.MODID + ":" + "light_block_White");
        this.func_149663_c("lightBox");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void func_149666_a(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            EnumBlockLightBox[] enumBlockLightBoxes=EnumBlockLightBox.values();
            for (int i = 0; i < enumBlockLightBoxes.length; i++) {
                EnumBlockLightBox type = enumBlockLightBoxes[i];
                list.add(new ItemStack(this, 1, type.meta));
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("灯箱getSubBlocks出错了：" + e.getMessage());
        }

    }
    @Override
    public int func_176201_c(IBlockState state) {
        return ((EnumBlockLightBox)state.func_177229_b(TYPE)).meta;
    }

    @Override
    public int func_180651_a(IBlockState state) {
        return this.func_176201_c(state);
    }
    @Override
    protected BlockState func_180661_e() {
        return new BlockState(this, new IProperty[]{TYPE});
    }
    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P().func_177226_a(TYPE, EnumBlockLightBox.fromMeta(meta));
    }

}
