package com.trhsy.sim.common.entity.folk.traits;

public class TraitHatesOutdoors extends Trait
{
	public TraitHatesOutdoors() 
	{
		super();
		//讨厌户外活动
		setTraitName("Hates the Outdoors");
		//讨厌户外活动的人倾向于更频繁地弯曲。他们不喜欢开放的空间，也讨厌在农场工作。
		setTraitDescription("Folk that hate the outdoors tend to warp more frequently. "
				+ "They dislike open spaces, and hate working on farms.");
		setTraitOpposite(Traits.traitLovesOutdoors);
	}
}
