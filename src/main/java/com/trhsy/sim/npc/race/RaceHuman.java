package com.trhsy.sim.npc.race;

import net.minecraft.client.resources.I18n;

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
        setRaceName(I18n.format("container.sim.race_Human"));
        //描述
        setDesc(I18n.format("container.sim.race_Human_Desc"));
        //寿命
        setLifespan(110);
        // 成年期
        setMaturity(18);
    }
}
