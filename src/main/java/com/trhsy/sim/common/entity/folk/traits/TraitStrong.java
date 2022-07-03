package com.trhsy.sim.common.entity.folk.traits;

import net.minecraft.client.resources.I18n;

public class TraitStrong extends Trait
{
	public TraitStrong() 
	{
		super();
		//强健的
		setTraitName(I18n.format("container.sim.traits19"));
		//强壮的人可以更快地完成体力劳动。无论是采矿、伐木、建筑还是挖沙。
		setTraitDescription(I18n.format("container.sim.traits20"));
	}
}
