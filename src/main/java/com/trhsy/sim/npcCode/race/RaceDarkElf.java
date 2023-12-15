package com.trhsy.sim.npcCode.race;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @Author fan
 * @Description //TODO 
 * @Date 16:20 2022/10/15
 * @Param  黑暗精灵
 * @return 
 **/
public class RaceDarkElf extends Race {
    public RaceDarkElf() {
        super();
        //名字 黑暗精灵
        this.raceName=new TextComponentTranslation("container.sim.race_dark_elf",new Object[0]).getUnformattedText();
        //描述
        this.desc=new TextComponentTranslation("container.sim.race_dark_elf_Desc",new Object[0]).getUnformattedText();
        //寿命
        this.lifespan=250;
        // 成年期
        this.maturity=26;
    }
}
