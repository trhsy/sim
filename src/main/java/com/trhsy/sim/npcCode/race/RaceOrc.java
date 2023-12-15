package com.trhsy.sim.npcCode.race;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @ClassName RaceOrc
 * @Description todo 兽人族
 * @Author TRHSY
 * @Date 2022/10/1516:40
 **/
public class RaceOrc extends Race{
    public RaceOrc() {
        super();
        //兽人族族
        this.raceName=new TextComponentTranslation("container.sim.race_Orc",new Object[0]).getUnformattedText();
        //描述
        this.desc=new TextComponentTranslation("container.sim.race_Orc_Desc",new Object[0]).getUnformattedText();
        //寿命
        this.lifespan=110;
        // 成年期
        this.maturity=18;
    }
}
