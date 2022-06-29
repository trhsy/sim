package com.trhsy.sim.common.entity.folk.traits;

public class TraitLovesOutdoors extends Trait
{
	public TraitLovesOutdoors() 
	{
		super();
		//热爱户外运动
		setTraitName("Loves the Outdoors");
		//喜欢户外活动的人喜欢悠闲地散步。他们喜欢开放的空间，在户外工作将使他们的一天。
		setTraitDescription("Folk that love the outdoors love taking leisurely strolls. "
				+ "They love open spaces, and working outside will make their day.");
		setTraitOpposite(Traits.traitHatesOutdoors);
	}
}
