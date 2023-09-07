package com.trhsy.sim.util;

import net.minecraft.client.resources.I18n;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util
 * @ClassName: EnumLevel
 * @Description:
 * @date 2022/10/13 15:31
 */
public enum EnumLevel {
    //敌人
    ENEMY,
    //讨厌
    DESPISE,
    //恨
    HATE,
    //不喜欢
    DISLIKE,
    //熟人
    AQUAINTANCE,
    //朋友
    FRIEND,
    //好朋友
    GOODFRIEND,
    //最好的朋友
    BESTFRIENDS;

    private EnumLevel() {
    }

    public String getText() {
        switch(this) {
            case BESTFRIENDS:
                String s9=I18n.format("container.sim.relation_ship_Best_friends");
                return s9;
            case GOODFRIEND:
                String s8=I18n.format("container.sim.relation_ship_Good_friends");
                return s8;
            case FRIEND:
                String s7=I18n.format("container.sim.relation_ship_Friends_");
                return s7;
            case AQUAINTANCE:
                String s6=I18n.format("container.sim.relation_ship_level_Aquaintance");
                return s6;
            case DISLIKE:
                String s5=I18n.format("container.sim.relation_ship_Dislike");
                return s5;
            case HATE:
                String s4=I18n.format("container.sim.relation_ship_Hate");
                return s4;
            case DESPISE:
                String s3=I18n.format("container.sim.relation_ship_Despise");
                return s3;
            case ENEMY:
                String s2=I18n.format("container.sim.relation_ship_Enemy");
                return s2;
            default:
                String s1=I18n.format("container.sim.gui_Folk_Unknown");
                return s1;
        }
    }
}
