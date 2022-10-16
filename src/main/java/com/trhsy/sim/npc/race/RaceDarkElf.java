package com.trhsy.sim.npc.race;

import net.minecraft.client.resources.I18n;

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
        setRaceName(I18n.format("container.sim.race_dark_elf"));
        //描述
        setDesc(I18n.format("container.sim.race_dark_elf_Desc"));
        //寿命
        setLifespan(250);
        // 成年期
        setMaturity(26);
    }
}
