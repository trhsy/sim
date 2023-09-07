package com.trhsy.sim.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public class EnumBlock <E extends Enum<E> & EnumBlock.IEnumMeta & IStringSerializable> extends Block{
    public final PropertyEnum<E> prop;
    private final E[] values;
    private static PropertyEnum<?> tmp;
    public EnumBlock(Material blockMaterialIn, PropertyEnum<E> prop, Class<E> clazz) {
        super(preInit(blockMaterialIn, prop));
        this.prop = prop;
        this.values = (E[]) clazz.getEnumConstants();
    }
    private static Material preInit(Material material, PropertyEnum<?> property) {
        tmp = property;
        return material;
    }
    public interface IEnumMeta {
        int getMeta();
    }
}
