package com.trhsy.sim.common.entity.folk.traits;

import net.minecraft.client.resources.I18n;

public class TraitDwarvenHeritage extends Trait
{
	public TraitDwarvenHeritage() 
	{
		super();
		//矮人遗产
		setTraitName(I18n.format("container.sim.traits3"));
		//有矮人血统的人在diggy diggy hole跑得更快。给一个鹤嘴锄，他们的挖掘速度将是普通矿工的两倍。虽然他们更喜欢采矿，但他们仍然是优秀、快速的玻璃制造商。
		setTraitDescription(I18n.format("container.sim.traits4"));
	}
}
