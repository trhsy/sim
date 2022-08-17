package com.trhsy.sim.common.core.entity.enums;

import net.minecraft.client.resources.I18n;

/**
 * @author trhsy NPC 行动
 * @date 2022/1/25 0025
 * @apiNote
 */
public enum FolkAction {
    //上班途中
    ONWAYTOWORK,
    //在工作中
    ATWORK,
    //闲逛
    WANDER,
    //在家
    ATHOME,
    //回家
    GOINGHOME,
    //待在家里
    STAYINGHOME,
    //生孩子
    HAVINGBABY;

    private FolkAction() {
    }

    @Override
    public String toString() {
        String ret = I18n.format("container.sim.FolkAction1");
        if (this == WANDER) {
            ret = I18n.format("container.sim.FolkAction2");
        } else if (this == ATWORK) {
            ret = I18n.format("container.sim.FolkAction3");
        } else if (this == ONWAYTOWORK) {
            ret = I18n.format("container.sim.FolkAction4");
        } else if (this == ATHOME) {
            ret = I18n.format("container.sim.FolkAction5");
        } else if (this == GOINGHOME) {
            ret = I18n.format("container.sim.FolkAction6");
        } else if (this == STAYINGHOME) {
            ret = I18n.format("container.sim.FolkAction7");
        } else if (this == HAVINGBABY) {
            ret = I18n.format("container.sim.FolkAction8");
        }

        return ret;
    }
}
