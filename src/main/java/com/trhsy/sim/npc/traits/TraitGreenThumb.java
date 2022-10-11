package com.trhsy.sim.npc.traits;

import net.minecraft.client.resources.I18n;

public class TraitGreenThumb extends Trait
{
	public TraitGreenThumb() 
	{
		super();
		//擅长园艺
		setTraitName(I18n.format("container.sim.traits7"));
		//有园艺经验的人是更好的种植庄稼的农民。他们可以以两倍的速度种植、耕作和收获，这意味着你的产量是原来的两倍。
		setTraitDescription(I18n.format("container.sim.traits8"));
	}
}
