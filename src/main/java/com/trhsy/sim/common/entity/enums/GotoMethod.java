package com.trhsy.sim.common.entity.enums;

/**
 * @author trhsy
 * @date 2022/1/25 0025
 * @apiNote
 */
public enum GotoMethod {
    WALK,
    BEAM,
    SHIFT;

    private GotoMethod() {
    }

    public String toString() {
        if (this == BEAM) {
            return "Beaming";
        } else if (this == SHIFT) {
            return "Shifting";
        } else {
            return this == WALK ? "Walking" : "";
        }
    }
}
