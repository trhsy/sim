package com.trhsy.sim.block.material;

import net.minecraft.util.IStringSerializable;

/**
 * @author Administrator
 */

public enum EnumControlBoxMaterial implements IStringSerializable{
    TOP,
    ATM,
    OTHER;
    private EnumControlBoxMaterial(){

    }
    public final int meta = this.ordinal();

    public static EnumControlBoxMaterial fromMeta(int meta) {
        if (meta < 0 || meta >= values().length) {
            meta = 0;
        }

        return values()[meta];
    }
    public int getMeta() {
        return this.meta;
    }
    @Override
    public String getName() {
        return this.toString();
    }
}
