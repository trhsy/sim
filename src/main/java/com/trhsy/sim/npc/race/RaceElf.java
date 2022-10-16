package com.trhsy.sim.npc.race;

import net.minecraft.client.resources.I18n;

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
        setRaceName(I18n.format("container.sim.race_elf"));
        //描述
        setDesc(I18n.format("container.sim.race_elf_Desc"));
        //寿命
        setLifespan(250);
        // 成年期
        setMaturity(26);
    }
}
