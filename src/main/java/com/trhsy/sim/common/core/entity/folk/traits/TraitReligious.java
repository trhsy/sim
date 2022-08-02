package com.trhsy.sim.common.core.entity.folk.traits;

import net.minecraft.client.resources.I18n;

public class TraitReligious extends Trait {
    public TraitReligious() {
        super();
        //宗教信仰的
        setTraitName(I18n.format("container.sim.traits15"));
        //宗教人士喜欢每天去教堂祈祷。无法做到这一点会让他们不开心。确保在你的镇上建一座教堂。
        setTraitDescription(I18n.format("container.sim.traits16"));
        hasSpecialBuilding(I18n.format("container.sim.traits17"), I18n.format("container.sim.traits18"));
    }
}
