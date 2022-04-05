package com.trhsy.sim.api.buildcraft.api.facades;

/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */
public enum FacadeType {
    Basic,
    Phased;

    private FacadeType() {
    }

    public static FacadeType fromOrdinal(int ordinal) {
        return ordinal == 1 ? Phased : Basic;
    }
}
