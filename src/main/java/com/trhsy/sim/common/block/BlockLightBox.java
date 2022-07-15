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
    public static final PropertyEnum<EnumBlockLightBox> TYPE = PropertyEnum.create("type", EnumBlockLightBox.class);
    public BlockLightBox() {
        super(Material.wood,TYPE,EnumBlockLightBox.class);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBlockLightBox.WHITE));
        this.setLightLevel(1.0F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setTickRandomly(true);
        //this.setTextureName(ModSim.MODID + ":" + "light_block_White");
        this.setUnlocalizedName("lightBox");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            EnumBlockLightBox[] enumBlockLightBoxes=EnumBlockLightBox.values();
            for (int i = 0; i < enumBlockLightBoxes.length; i++) {
                EnumBlockLightBox type = enumBlockLightBoxes[i];
                list.add(new ItemStack(this, 1, type.meta));
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出差了：" + e.getMessage());
        }

    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumBlockLightBox)state.getValue(TYPE)).meta;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{TYPE});
    }
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumBlockLightBox.fromMeta(meta));
    }

}
