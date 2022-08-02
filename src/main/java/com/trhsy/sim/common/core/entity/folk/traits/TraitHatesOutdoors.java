package com.trhsy.sim.common.core.entity.folk.traits;

import net.minecraft.client.resources.I18n;

public class TraitHatesOutdoors extends Trait
{
	public TraitHatesOutdoors() 
	{
		super();
		//讨厌户外活动
		setTraitName(I18n.format("container.sim.traits9"));
		//讨厌户外活动的人倾向于更频繁地弯曲。他们不喜欢开放的空间，也讨厌在农场工作。
		setTraitDescription(I18n.format("container.sim.traits10"));
		setTraitOpposite(Traits.traitLovesOutdoors);
	}
}
