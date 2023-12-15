package com.trhsy.sim.npcCode.traits;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TraitDwarvenHeritage extends Trait {
    public TraitDwarvenHeritage() {
        super();
        //矮人遗产
        setTraitName(new TextComponentTranslation("container.sim.traits3",new Object[0]).getUnformattedText());
        //有矮人血统的人在diggy diggy hole跑得更快。给一个鹤嘴锄，他们的挖掘速度将是普通矿工的两倍。虽然他们更喜欢采矿，但他们仍然是优秀、快速的玻璃制造商。
        setTraitDescription(new TextComponentTranslation("container.sim.traits4",new Object[0]).getUnformattedText());
    }
}
