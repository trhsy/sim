package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @ClassName EnumBlock
 * @Description todo
 * @Author Tian
 * @Date 2022/5/117:32
 **/
public class EnumBlock <E extends Enum<E> & EnumBlock.IEnumMeta & IStringSerializable> extends Block {
    public final PropertyEnum<E> prop;
    private final E[] values;
    private static PropertyEnum<?> tmp;
    public EnumBlock(Material material, PropertyEnum<E> prop, Class<E> clazz) {
        super(preInit(material, prop));
        this.prop = prop;
        this.values = (E[]) clazz.getEnumConstants();
    }

    private static Material preInit(Material material, PropertyEnum<?> property) {
        tmp = property;
        return material;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_149666_a(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            Enum[] var4 = this.values;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                E type = (E) var4[var6];
                list.add(new ItemStack(this, 1, ((EnumBlock.IEnumMeta)type).getMeta()));
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage());
        }


    }

    @Override
    protected BlockState func_180661_e() {
        return this.prop == null ? new BlockState(this, new IProperty[]{tmp}) : new BlockState(this, new IProperty[]{this.prop});
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P().func_177226_a(this.prop, this.fromMeta(meta));
    }

    public int func_176201_c(IBlockState state) {
        return ((EnumBlock.IEnumMeta)((Enum)state.func_177229_b(this.prop))).getMeta();
    }

    public int func_180651_a(IBlockState state) {
        return this.func_176201_c(state);
    }

    protected E fromMeta(int meta) {
        if (meta < 0 || meta >= this.values.length) {
            meta = 0;
        }

        return this.values[meta];
    }

    public interface IEnumMeta {
        int getMeta();
    }
}
