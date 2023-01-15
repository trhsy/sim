package com.trhsy.sim.util;

import net.minecraft.client.resources.I18n;

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
    CACTUS;

    private FarmType() {
    }

    public static FarmType byName(String readUTF8String) {
        if (readUTF8String.equals(I18n.format("container.sim.FarmType1"))) {
            return CARROT;
        } else if (readUTF8String.equals(I18n.format("container.sim.FarmType2"))) {
            return MELON;
        } else if (readUTF8String.equals(I18n.format("container.sim.FarmType3"))) {
            return POTATO;
        } else if (readUTF8String.equals(I18n.format("container.sim.FarmType4"))) {
            return PUMPKIN;
        } else if (readUTF8String.equals(I18n.format("container.sim.FarmType5"))) {
            return WHEAT;
        } else if (readUTF8String.equals(I18n.format("container.sim.FarmType6"))) {
            return BEETROOTS;
        } else if (readUTF8String.equals(I18n.format("container.sim.FarmType7"))) {
            return SUGAR;
        } else if (readUTF8String.equals(I18n.format("container.sim.FarmType8"))) {
            return CACTUS;
        }
        return WHEAT;
    }

    @Override
    public String toString() {
        if (this == CARROT) {
            return I18n.format("container.sim.FarmType1");
        } else if (this == MELON) {
            return I18n.format("container.sim.FarmType2");
        } else if (this == POTATO) {
            return I18n.format("container.sim.FarmType3");
        } else if (this == PUMPKIN) {
            return I18n.format("container.sim.FarmType4");
        } else if (this == WHEAT) {
            return I18n.format("container.sim.FarmType5");
        } else if (this == BEETROOTS) {
            return I18n.format("container.sim.FarmType6");
        } else if (this == SUGAR) {
            return I18n.format("container.sim.FarmType7");
        } else {
            return this == CACTUS ? I18n.format("container.sim.FarmType8") : I18n.format("container.sim.FarmType9");
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
                return WHEAT;
            default:
                return WHEAT;
        }
    }
}
