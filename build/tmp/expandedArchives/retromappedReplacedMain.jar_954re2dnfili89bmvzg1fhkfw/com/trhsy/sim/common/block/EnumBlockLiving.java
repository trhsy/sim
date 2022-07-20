package com.trhsy.sim.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IStringSerializable;

public enum EnumBlockLiving implements IStringSerializable, EnumBlock.IEnumMeta {
    WHITE(0, 15, "white", "white", MapColor.field_151666_j, EnumChatFormatting.WHITE),
    ORANGE(1, 14, "orange", "orange", MapColor.field_151676_q, EnumChatFormatting.GOLD),
    MAGENTA(2, 13, "magenta", "magenta", MapColor.field_151675_r, EnumChatFormatting.AQUA),
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", MapColor.field_151674_s, EnumChatFormatting.BLUE),
    YELLOW(4, 11, "yellow", "yellow", MapColor.field_151673_t, EnumChatFormatting.YELLOW),
    LIME(5, 10, "lime", "lime", MapColor.field_151672_u, EnumChatFormatting.GREEN),
    PINK(6, 9, "pink", "pink", MapColor.field_151671_v, EnumChatFormatting.LIGHT_PURPLE),
    GRAY(7, 8, "gray", "gray", MapColor.field_151670_w, EnumChatFormatting.DARK_GRAY),
    SILVER(8, 7, "silver", "silver", MapColor.field_151680_x, EnumChatFormatting.GRAY),
    CYAN(9, 6, "cyan", "cyan", MapColor.field_151679_y, EnumChatFormatting.DARK_AQUA),
    PURPLE(10, 5, "purple", "purple", MapColor.field_151678_z, EnumChatFormatting.DARK_PURPLE),
    BLUE(11, 4, "blue", "blue", MapColor.field_151649_A, EnumChatFormatting.DARK_BLUE),
    BROWN(12, 3, "brown", "brown", MapColor.field_151650_B, EnumChatFormatting.GOLD),
    GREEN(13, 2, "green", "green", MapColor.field_151651_C, EnumChatFormatting.DARK_GREEN),
    RED(14, 1, "red", "red", MapColor.field_151645_D, EnumChatFormatting.DARK_RED),
    BLACK(15, 0, "black", "black", MapColor.field_151646_E, EnumChatFormatting.BLACK);
    private static final EnumBlockLiving[] META_LOOKUP = new EnumBlockLiving[values().length];
    private static final EnumBlockLiving[] DYE_DMG_LOOKUP = new EnumBlockLiving[values().length];
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String unlocalizedName;
    private final MapColor mapColor;
    private final EnumChatFormatting chatColor;
    private EnumBlockLiving(int meta, int dyeDamage, String name, String unlocalizedName, MapColor mapColorIn, EnumChatFormatting chatColor) {
        this.meta = meta;
        this.dyeDamage = dyeDamage;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.mapColor = mapColorIn;
        this.chatColor = chatColor;
    }



    public static EnumBlockLiving fromMeta(int meta) {
        if (meta < 0 || meta >= values().length) {
            meta = 0;
        }
        return values()[meta];
    }
    public static EnumBlockLiving byDyeDamage(int damage)
    {
        if (damage < 0 || damage >= DYE_DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return DYE_DMG_LOOKUP[damage];
    }
    @Override
    public int getMeta() {
        return this.meta;
    }

    public int getMetadata()
    {
        return this.meta;
    }

    public int getDyeDamage()
    {
        return this.dyeDamage;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public MapColor getMapColor()
    {
        return this.mapColor;
    }

    public static EnumBlockLiving byMetadata(int meta)
    {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    @Override
    public String toString()
    {
        return this.unlocalizedName;
    }

    @Override
    public String func_176610_l()
    {
        return this.name;
    }

    static
    {
        for (EnumBlockLiving enumdyecolor : values())
        {
            META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
        }
    }
}
