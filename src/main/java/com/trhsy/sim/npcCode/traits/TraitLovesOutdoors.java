package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitLovesOutdoors extends Trait {
    public TraitLovesOutdoors() {
        super();
        //热爱户外运动
        setTraitName(new TextComponentTranslation("container.sim.traits11",new Object[0]).getUnformattedText());
        //喜欢户外活动的人喜欢悠闲地散步。他们喜欢开放的空间，在户外工作将使他们的一天。
        setTraitDescription(new TextComponentTranslation("container.sim.traits12",new Object[0]).getUnformattedText());
        setTraitOpposite(Traits.traitHatesOutdoors);
    }
}
