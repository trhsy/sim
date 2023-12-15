package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitWorkaholic extends Trait {
    public TraitWorkaholic() {
        super();
        //工作狂
        setTraitName(new TextComponentTranslation("container.sim.traits21",new Object[0]).getUnformattedText());
        //工作狂喜欢工作，技能的发展速度是普通人的两倍。失业使这些人不快乐。保持就业率上升！
        setTraitDescription(new TextComponentTranslation("container.sim.traits22",new Object[0]).getUnformattedText());
    }
}
