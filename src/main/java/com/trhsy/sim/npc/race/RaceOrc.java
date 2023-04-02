package com.trhsy.sim.npc.race;

import net.minecraft.client.resources.I18n;

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
        this.raceName=I18n.format("container.sim.race_Orc");
        //描述
        this.desc=I18n.format("container.sim.race_Orc_Desc");
        //寿命
        this.lifespan=110;
        // 成年期
        this.maturity=18;
    }
}
