package com.trhsy.sim.npcCode.enums;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * 农场类型
 */
public enum FarmType {
    //小麦
    WHEAT,
    //西瓜
    MELON,
    //南瓜
    PUMPKIN,
    //土豆
    POTATO,
    //胡萝卜
    CARROT,
    //甜菜根
    BEETROOTS,
    //甘蔗
    SUGAR,
    //仙人掌
    CACTUS,
    //可可豆
    COCOA;

    private FarmType() {
    }

    public static FarmType byName(String readUTF8String) {
        if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType1",new Object[0]).getUnformattedText())) {
            return CARROT;
        } else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType2",new Object[0]).getUnformattedText())) {
            return MELON;
        } else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType3",new Object[0]).getUnformattedText())) {
            return POTATO;
        } else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType4",new Object[0]).getUnformattedText())) {
            return PUMPKIN;
        } else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType5",new Object[0]).getUnformattedText())) {
            return WHEAT;
        } else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType6",new Object[0]).getUnformattedText())) {
            return BEETROOTS;
        } else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType7",new Object[0]).getUnformattedText())) {
            return SUGAR;
        } else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType8",new Object[0]).getUnformattedText())) {
            return CACTUS;
        }else if (readUTF8String.equals(new TextComponentTranslation("container.sim.FarmType10",new Object[0]).getUnformattedText())) {
            return COCOA;
        }
        return WHEAT;
    }

    @Override
    public String toString() {
        if (this == CARROT) {
            return new TextComponentTranslation("container.sim.FarmType1",new Object[0]).getUnformattedText();
        } else if (this == MELON) {
            return new TextComponentTranslation("container.sim.FarmType2",new Object[0]).getUnformattedText();
        } else if (this == POTATO) {
            return new TextComponentTranslation("container.sim.FarmType3",new Object[0]).getUnformattedText();
        } else if (this == PUMPKIN) {
            return new TextComponentTranslation("container.sim.FarmType4",new Object[0]).getUnformattedText();
        } else if (this == WHEAT) {
            return new TextComponentTranslation("container.sim.FarmType5",new Object[0]).getUnformattedText();
        } else if (this == BEETROOTS) {
            return new TextComponentTranslation("container.sim.FarmType6",new Object[0]).getUnformattedText();
        } else if (this == SUGAR) {
            return new TextComponentTranslation("container.sim.FarmType7",new Object[0]).getUnformattedText();
        } else if (this == CACTUS){
            return new TextComponentTranslation("container.sim.FarmType8",new Object[0]).getUnformattedText();
        }else if(this == COCOA){
            return new TextComponentTranslation("container.sim.FarmType10",new Object[0]).getUnformattedText();
        }else{
            return new TextComponentTranslation("container.sim.FarmType9",new Object[0]).getUnformattedText();
        }
    }

    /**
     * 转
     * @return
     */
    public FarmType rotateY() {
        switch (this) {
            case WHEAT:
                return MELON;
            case MELON:
                return PUMPKIN;
            case PUMPKIN:
                return POTATO;
            case POTATO:
                return CARROT;
            case CARROT:
                return BEETROOTS;
            case BEETROOTS:
                return SUGAR;
            case SUGAR:
                return CACTUS;
            case CACTUS:
                return COCOA;
            case COCOA:
                return WHEAT;
            default:
                return WHEAT;
        }
    }
}
