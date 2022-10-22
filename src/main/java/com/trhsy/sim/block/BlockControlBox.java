package com.trhsy.sim.block;

import com.trhsy.sim.block.enums.EnumControlBox;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.util.EnumBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Trhsy
 * 控制箱
 */
public class BlockControlBox extends EnumBlock<EnumControlBox> {
    public List<NpcData> employees = new ArrayList();
    public static final PropertyEnum<EnumControlBox> TYPE = PropertyEnum.create("type", EnumControlBox.class);

    public BlockControlBox() {
        super(Material.WOOD, TYPE, EnumControlBox.class);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(10.0F);
        this.setResistance(1);
        this.setUnlocalizedName("controlBox");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumControlBox.TOP));
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            for (EnumControlBox enumControlBox:EnumControlBox.values()) {
                list.add(new ItemStack(this, 1, enumControlBox.getMeta()));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("控制箱getSubBlocks出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return ( state.getValue(TYPE)).getMeta();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{TYPE});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumControlBox.fromMeta(meta));
    }
    /**
     * 销毁时要丢弃的项目数量
     * @param random
     * @return
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
