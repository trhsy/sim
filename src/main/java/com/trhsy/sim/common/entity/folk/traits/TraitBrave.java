package com.trhsy.sim.common.entity.folk.traits;

public class TraitBrave extends Trait {
    public TraitBrave() {
        super();
        //勇敢的
        setTraitName("Brave");
        //勇敢的人可以造就更好的士兵。 他们在杀死一个暴徒之间等待了一半的时间，而那个级别的普通士兵会这样做。 他们的士兵技能也以两倍的速度升级。
        setTraitDescription("Brave folk make much better soldiers. They wait half the "
                + "time between killing a mob that an ordinary soldier of that level"
                + "would. Their soldier skill also levels up twice as fast.");
    }
}
