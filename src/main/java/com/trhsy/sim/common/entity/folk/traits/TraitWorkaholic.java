package com.trhsy.sim.common.entity.folk.traits;

public class TraitWorkaholic extends Trait
{
	public TraitWorkaholic() 
	{
		super();
		//工作狂
		setTraitName("Workaholic");
		//工作狂喜欢工作，技能的发展速度是普通人的两倍。失业使这些人不快乐。保持就业率上升！
		setTraitDescription("Workaholic folk enjoy working, and will develop skills "
				+ "twice as fast as an ordinary folk. Unemployment makes these folk "
				+ "unhappy. Keep those employment rates up!");
	}
}
