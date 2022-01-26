package com.trhsy.sim.common.entity.enums;

/**
 * @author trhsy
 * @date 2022/1/25 0025
 * @apiNote
 */
public enum FarmType {
    WHEAT,
    MELON,
    PUMPKIN,
    POTATO,
    CARROT,
    CUSTOM,
    SUGAR,
    CACTUS;

    private FarmType() {
    }

    public String toString() {
        if (this == CARROT) {
            return "Carrot";
        } else if (this == MELON) {
            return "Melon";
        } else if (this == POTATO) {
            return "Potato";
        } else if (this == PUMPKIN) {
            return "Pumpkin";
        } else if (this == WHEAT) {
            return "Wheat";
        } else if (this == CUSTOM) {
            return "Custom";
        } else if (this == SUGAR) {
            return "Sugar cane";
        } else {
            return this == CACTUS ? "Cactus" : "Unknown";
        }
    }
}
