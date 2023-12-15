package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitReligious extends Trait {
    public TraitReligious() {
        super();
        //宗教信仰的
        setTraitName(new TextComponentTranslation("container.sim.traits15",new Object[0]).getUnformattedText());
        //宗教人士喜欢每天去教堂祈祷。无法做到这一点会让他们不开心。确保在你的镇上建一座教堂。
        setTraitDescription(new TextComponentTranslation("container.sim.traits16",new Object[0]).getUnformattedText());
        hasSpecialBuilding(new TextComponentTranslation("container.sim.traits17",new Object[0]).getUnformattedText(), new TextComponentTranslation("container.sim.traits18",new Object[0]).getUnformattedText());
    }
}
