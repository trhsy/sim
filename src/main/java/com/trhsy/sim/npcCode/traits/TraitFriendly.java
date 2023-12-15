package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitFriendly extends Trait {
    public TraitFriendly() {
        super();
        //友好的;
        setTraitName(new TextComponentTranslation("container.sim.traits5",new Object[0]).getUnformattedText());
        //友好的人总是出去交朋友，他们很少与其他人有不好的关系，当他们没有朋友时会变得不开心。
        setTraitDescription(new TextComponentTranslation("container.sim.traits6",new Object[0]).getUnformattedText());
    }
}
