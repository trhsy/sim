package com.trhsy.sim.common.entity.enums;

/**
 * @author trhsy
 * @date 2022/1/25 0025
 * @apiNote
 */
public enum FolkAction {
    ONWAYTOWORK,
    ATWORK,
    WANDER,
    ATHOME,
    GOINGHOME,
    STAYINGHOME,
    HAVINGBABY;

    private FolkAction() {
    }

    public String toString() {
        String ret = "doing nothing";
        if (this == WANDER) {
            ret = "just wandering";
        } else if (this == ATWORK) {
            ret = "at work";
        } else if (this == ONWAYTOWORK) {
            ret = "on my way to work";
        } else if (this == ATHOME) {
            ret = "relaxing at home";
        } else if (this == GOINGHOME) {
            ret = "going home";
        } else if (this == STAYINGHOME) {
            ret = "staying at home";
        } else if (this == HAVINGBABY) {
            ret = "having a baby";
        }

        return ret;
    }
}
