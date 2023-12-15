package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitHatesOutdoors extends Trait {
    public TraitHatesOutdoors() {
        super();
        //讨厌户外活动
        setTraitName(new TextComponentTranslation("container.sim.traits9",new Object[0]).getUnformattedText());
        //讨厌户外活动的人倾向于更频繁地弯曲。他们不喜欢开放的空间，也讨厌在农场工作。
        setTraitDescription(new TextComponentTranslation("container.sim.traits10",new Object[0]).getUnformattedText());
        setTraitOpposite(Traits.traitLovesOutdoors);
    }
}
