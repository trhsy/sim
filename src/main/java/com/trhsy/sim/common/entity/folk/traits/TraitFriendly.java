package com.trhsy.sim.common.entity.folk.traits;

public class TraitFriendly extends Trait
{
	public TraitFriendly() 
	{
		super();
		//友好的;
		setTraitName("Friendly");
		//友好的人总是出去交朋友，他们很少与其他人有不好的关系，当他们没有朋友时会变得不开心。
		setTraitDescription("Friendly folk are always out to make friends. "
				+ "They will rarely have poor relationships with other folk "
				+ "and become unhappy when they have no friends.");
	}
}
