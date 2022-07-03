package com.trhsy.sim.common.entity.folk.traits;

import net.minecraft.client.resources.I18n;

public class TraitFriendly extends Trait
{
	public TraitFriendly() 
	{
		super();
		//友好的;
		setTraitName(I18n.format("container.sim.traits5"));
		//友好的人总是出去交朋友，他们很少与其他人有不好的关系，当他们没有朋友时会变得不开心。
		setTraitDescription(I18n.format("container.sim.traits6"));
	}
}
