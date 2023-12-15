package com.trhsy.sim.npcCode.enums;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

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
                String s9=new TextComponentTranslation("container.sim.relation_ship_Best_friends",new Object[0]).getUnformattedText();
                return s9;
            case GOODFRIEND:
                String s8=new TextComponentTranslation("container.sim.relation_ship_Good_friends",new Object[0]).getUnformattedText();
                return s8;
            case FRIEND:
                String s7=new TextComponentTranslation("container.sim.relation_ship_Friends_",new Object[0]).getUnformattedText();
                return s7;
            case AQUAINTANCE:
                String s6=new TextComponentTranslation("container.sim.relation_ship_level_Aquaintance",new Object[0]).getUnformattedText();
                return s6;
            case DISLIKE:
                String s5=new TextComponentTranslation("container.sim.relation_ship_Dislike",new Object[0]).getUnformattedText();
                return s5;
            case HATE:
                String s4=new TextComponentTranslation("container.sim.relation_ship_Hate",new Object[0]).getUnformattedText();
                return s4;
            case DESPISE:
                String s3=new TextComponentTranslation("container.sim.relation_ship_Despise",new Object[0]).getUnformattedText();
                return s3;
            case ENEMY:
                String s2=new TextComponentTranslation("container.sim.relation_ship_Enemy",new Object[0]).getUnformattedText();
                return s2;
            default:
                String s1=new TextComponentTranslation("container.sim.gui_Folk_Unknown",new Object[0]).getUnformattedText();
                return s1;
        }
    }
}
