package com.trhsy.sim.common.entity.folk.traits;

public class TraitReligious extends Trait {
    public TraitReligious() {
        super();
        //宗教信仰的
        setTraitName("Religious");
        //宗教人士喜欢每天去教堂祈祷。无法做到这一点会让他们不开心。确保在你的镇上建一座教堂。
        setTraitDescription("Religious folk like to visit church daily to pray. "
                + "Being unable to do so makes them unhappy. Make sure you build a church"
                + "in your town.");
        //hasSpecialBuilding("Church", "Attending Church");
    }
}
