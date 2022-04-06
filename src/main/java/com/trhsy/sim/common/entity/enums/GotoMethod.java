package com.trhsy.sim.common.entity.enums;

import net.minecraft.client.resources.I18n;

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

    @Override
    public String toString() {
        if (this == BEAM) {
            return I18n.format("container.sim.GotoMethod1") ;
        } else if (this == SHIFT) {
            return I18n.format("container.sim.GotoMethod2");
        } else {
            return this == WALK ? I18n.format("container.sim.Fire") : "";
        }
    }
}
