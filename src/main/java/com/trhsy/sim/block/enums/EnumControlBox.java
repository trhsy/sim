package com.trhsy.sim.block.enums;

import com.trhsy.sim.util.EnumBlock;
import net.minecraft.util.IStringSerializable;

/**
 * 控制箱枚举，用于多方块
 *
 * @author Trhsy
 */
public enum EnumControlBox implements IStringSerializable, EnumBlock.IEnumMeta{
    TOP(0,"top"),
    ATM(1,"atm"),
    OTHER(2,"other");
    private final int meta;
    private final String name;
    private EnumControlBox(int meta, String name){
        this.meta = meta;
        this.name = name;
    }

    public static EnumControlBox fromMeta(int meta) {
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
        return this.name;
    }
}
