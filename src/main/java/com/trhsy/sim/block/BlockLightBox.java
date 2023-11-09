package com.trhsy.sim.block;

import com.trhsy.sim.block.enums.EnumBlock;
import com.trhsy.sim.block.enums.EnumLightColour;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockLightBox
 * @Description: 灯箱 照明用的
 * @date 2023/11/07 下午 5:13
 */
public class BlockLightBox extends EnumBlock<EnumLightColour> {
    public static final PropertyEnum<EnumLightColour> COLOR = PropertyEnum.create("color", EnumLightColour.class);
    public BlockLightBox(){
        super(Material.WOOD,COLOR,EnumLightColour.class);
        this.setLightLevel(2F);
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
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
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
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this,new IProperty[]{COLOR});
    }

}
