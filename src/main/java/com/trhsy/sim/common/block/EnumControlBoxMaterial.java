package com.trhsy.sim.common.block;

import net.minecraft.util.IStringSerializable;
/**
 * @Author fan
 * @Description //TODO 控制箱枚举，用于多方块
 * @Date 21:52 2022/4/19
 * @Param 
 * @return 
 **/
public enum EnumControlBoxMaterial implements IStringSerializable, EnumBlock.IEnumMeta {
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
    @Override
    public int getMeta() {
        return this.meta;
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
