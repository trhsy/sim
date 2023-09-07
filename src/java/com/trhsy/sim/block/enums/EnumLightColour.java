package com.trhsy.sim.block.enums;

import com.trhsy.sim.util.EnumBlock;
import net.minecraft.util.IStringSerializable;

/**
 * @author Trhsy
 */

public enum EnumLightColour implements IStringSerializable, EnumBlock.IEnumMeta{

    WHITE(0,"white"),
    RED(1,"red"),
    ORANGE(2,"orange"),
    YELLOW(3,"yellow"),
    GREEN(4,"green"),
    BLUE(5,"blue"),
    PURPLE(6,"purple"),
    RAINBOW(7,"rainbow");
    private final int meta;
    private final String name;
    private EnumLightColour(int meta, String name) {
        this.meta = meta;
        this.name = name;
    }
    public static EnumLightColour fromMeta(int meta) {
        if (meta < 0 || meta >= values().length) {
            meta = 0;
        }
        return values()[meta];
    }

    @Override
    public int getMeta() {
        return meta;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
