package com.trhsy.sim.npcCode.race;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @Author fan
 * @Description //TODO
 * @Date 16:37 2022/10/15
 * @Param 人族
 * @return
 **/
public class RaceHuman extends Race {
    public RaceHuman() {
        super();
        //人族
        this.raceName=new TextComponentTranslation("container.sim.race_Human",new Object[0]).getUnformattedText();
        //描述
        this.desc=new TextComponentTranslation("container.sim.race_Human_Desc",new Object[0]).getUnformattedText();
        //寿命
        this.lifespan=110;
        // 成年期
        this.maturity=18;
    }
}
