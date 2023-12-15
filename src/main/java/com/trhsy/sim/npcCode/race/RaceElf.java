package com.trhsy.sim.npcCode.race;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/***
 * @Author fan
 * @Description //TODO
 * @Date 16:34 2022/10/15
 * @Param 精灵族
 * @return
 **/
public class RaceElf extends Race {
    public RaceElf() {
        super();
        //名字 黑暗精灵
        this.raceName=new TextComponentTranslation("container.sim.race_elf",new Object[0]).getUnformattedText();
        //描述
        this.desc=new TextComponentTranslation("container.sim.race_elf_Desc",new Object[0]).getUnformattedText();
        //寿命
        this.lifespan=250;
        // 成年期
        this.maturity=26;
    }
}
