package com.trhsy.sim.block;

import com.trhsy.sim.block.BlockBase;
import com.trhsy.sim.item.ItemLightBlock;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * @ClassName BlockLightBox
 * @Description todo
 * @Author Tian
 * @Date 2022/9/1812:02
 **/
public class BlockLightBox extends BlockBase {
    public static final PropertyInteger COLOR = PropertyInteger.create("color", 0, 7);
    public BlockLightBox(Material materialIn){
        super(materialIn,"lightBox");
        this.setLightLevel(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setStepSound(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, 0));
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLOR);
    }
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            for (int i = 0; i < 8; i++) {
                list.add(new ItemStack(this, 1, i));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("灯箱getSubBlocks出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COLOR, meta);
    }
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(COLOR,meta);
    }

    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this,new IProperty[]{COLOR});
    }
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(COLOR);
    }
    public static enum EnumLightColour {
        white,
        red,
        orange,
        yellow,
        green,
        blue,
        purple,
        rainbow;

        private EnumLightColour() {
        }
    }

}
