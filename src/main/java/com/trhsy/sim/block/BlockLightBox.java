package com.trhsy.sim.block;

import com.trhsy.sim.block.enums.EnumLightColour;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.util.EnumBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
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
 * @Description todo 灯箱
 * @Author Tian
 * @Date 2022/9/1812:02
 **/
public class BlockLightBox extends EnumBlock<EnumLightColour> {
    public static final PropertyEnum<EnumLightColour> COLOR = PropertyEnum.create("color", EnumLightColour.class);
    public BlockLightBox(){
        super(Material.WOOD,COLOR,EnumLightColour.class);
        this.setLightLevel(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setUnlocalizedName("lightBox");
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumLightColour.WHITE));
    }
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(COLOR).getMeta();
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLOR).getMeta();
    }
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            for (EnumLightColour enumLightColour: EnumLightColour.values()) {
                list.add(new ItemStack(this, 1, enumLightColour.getMeta()));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("灯箱getSubBlocks出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COLOR, EnumLightColour.fromMeta(meta));
    }
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(COLOR,EnumLightColour.fromMeta(meta));
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this,new IProperty[]{COLOR});
    }


}
