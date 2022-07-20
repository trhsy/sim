package com.trhsy.sim.common.block;

import net.minecraft.util.IStringSerializable;

public enum EnumBlockLightBox implements IStringSerializable, EnumBlock.IEnumMeta {
    WHITE, RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, RAINBOW;
    public final int meta = this.ordinal();
    private EnumBlockLightBox() {

    }



    public static EnumBlockLightBox fromMeta(int meta) {
        if (meta < 0 || meta >= values().length) {
            meta = 0;
        }
        return values()[meta];
    }

    @Override
    public int getMeta() {
        return this.meta;
    }

    @Override
    public String getName() {
        return this.toString();
    }

}
