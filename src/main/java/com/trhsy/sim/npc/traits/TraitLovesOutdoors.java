package com.trhsy.sim.npc.traits;

import net.minecraft.client.resources.I18n;

public class TraitLovesOutdoors extends Trait
{
	public TraitLovesOutdoors() 
	{
		super();
		//热爱户外运动
		setTraitName(I18n.format("container.sim.traits11"));
		//喜欢户外活动的人喜欢悠闲地散步。他们喜欢开放的空间，在户外工作将使他们的一天。
		setTraitDescription(I18n.format("container.sim.traits12"));
		setTraitOpposite(Traits.traitHatesOutdoors);
	}
}
