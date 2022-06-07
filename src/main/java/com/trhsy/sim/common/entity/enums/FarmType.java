package com.trhsy.sim.common.entity.enums;

import net.minecraft.client.resources.I18n;

/**
 * @author trhsy 农场类型
 * @date 2022/1/25 0025
 * @apiNote
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
    //萝卜
    CARROT,
    //自定义
    CUSTOM,
    //甘蔗
    SUGAR,
    //仙人掌
    CACTUS;

    private FarmType() {
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
        } else if (this == CUSTOM) {
            return I18n.format("container.sim.FarmType6");
        } else if (this == SUGAR) {
            return I18n.format("container.sim.FarmType7");
        } else {
            return this == CACTUS ? I18n.format("container.sim.FarmType8") : I18n.format("container.sim.FarmType9");
        }
    }
}
