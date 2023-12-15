package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitNightOwl extends Trait {
    public TraitNightOwl() {
        super();
        //夜猫子
        setTraitName(new TextComponentTranslation("container.sim.traits13",new Object[0]).getUnformattedText());
        //夜幕降临时，夜猫子们不会在室内呆太多时间。他们会在夜间漫步。夜猫子们将继续整夜的地形改造和建筑工作。
        setTraitDescription(new TextComponentTranslation("container.sim.traits14",new Object[0]).getUnformattedText());
    }
}
