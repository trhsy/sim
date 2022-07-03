package com.trhsy.sim.common.entity.folk.traits;

import net.minecraft.client.resources.I18n;

public class TraitWorkaholic extends Trait
{
	public TraitWorkaholic() 
	{
		super();
		//工作狂
		setTraitName(I18n.format("container.sim.traits20"));
		//工作狂喜欢工作，技能的发展速度是普通人的两倍。失业使这些人不快乐。保持就业率上升！
		setTraitDescription(I18n.format("container.sim.traits21"));
	}
}
