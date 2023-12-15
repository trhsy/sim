package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitStrong extends Trait {
    public TraitStrong() {
        super();
        //强健的
        setTraitName(new TextComponentTranslation("container.sim.traits19",new Object[0]).getUnformattedText());
        //强壮的人可以更快地完成体力劳动。无论是采矿、伐木、建筑还是挖沙。
        setTraitDescription(new TextComponentTranslation("container.sim.traits20",new Object[0]).getUnformattedText());
    }
}
