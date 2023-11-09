package com.trhsy.sim.block.enums;

import net.minecraft.util.IStringSerializable;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block.enums
 * @ClassName: EnumControlBox
 * @Description: 控制箱 枚举，用于多方块
 * @date 2023/11/06 下午 4:45
 */
public enum EnumControlBox implements IStringSerializable, EnumBlock.IEnumMeta {
    TOP(0,"top","top"),

    ATM(1,"atm","atm"),

    OTHER(2,"other","other");

    private final int meta;
    private final String name;
    private final String unlocalizedName;
    private static final EnumControlBox[] META_LOOKUP = new EnumControlBox[values().length];

    private EnumControlBox(int meta, String name, String unlocalizedName) {
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getMetadata() {
        return this.meta;
    }

    public static EnumControlBox byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    static {
        for (EnumControlBox enumControlBox : values()) {
            META_LOOKUP[enumControlBox.getMetadata()] = enumControlBox;
        }
    }

    @Override
    public int getMeta() {
        return this.meta;
    }
}
