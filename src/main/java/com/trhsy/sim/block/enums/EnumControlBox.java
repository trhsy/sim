package com.trhsy.sim.block.enums;

import com.trhsy.sim.util.EnumBlock;
import net.minecraft.util.IStringSerializable;

/**
 * 控制箱枚举，用于多方块
 *
 * @author Trhsy
 */
public enum EnumControlBox implements IStringSerializable, EnumBlock.IEnumMeta{
    TOP(0,"top","top"),
    ATM(1,"atm","atm"),
    OTHER(2,"other","other");
    private final int meta;
    private final String name;
    private final String unlocalizedName;
    private static final EnumControlBox[] META_LOOKUP = new EnumControlBox[values().length];
    private EnumControlBox(int meta, String name, String unlocalizedName){
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    public static EnumControlBox fromMeta(int meta) {
        if (meta < 0 || meta >= values().length) {
            meta = 0;
        }

        return values()[meta];
    }
    @Override
    public String toString()
    {
        return this.name;
    }
    public int getMetadata() {
        return this.meta;
    }
    public static EnumControlBox byMetadata(int meta)
    {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }
    @Override
    public String getName() {
        return this.name;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }
    static
    {
        for (EnumControlBox blocksandstone$enumtype : values())
        {
            META_LOOKUP[blocksandstone$enumtype.getMetadata()] = blocksandstone$enumtype;
        }
    }

    @Override
    public int getMeta() {
        return this.meta;
    }
}
