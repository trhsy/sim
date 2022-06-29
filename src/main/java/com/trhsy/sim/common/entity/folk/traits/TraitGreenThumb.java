package com.trhsy.sim.common.entity.folk.traits;

public class TraitGreenThumb extends Trait
{
	public TraitGreenThumb() 
	{
		super();
		//擅长园艺
		setTraitName("Green Thumb");
		//有园艺经验的人是更好的种植庄稼的农民。他们可以以两倍的速度种植、耕作和收获，这意味着你的产量是原来的两倍。
		setTraitDescription("Folk with a green thumb make much better crop farmers. They"
				+ " can plant, plough and harvest twice as fast, meaning you make twice"
				+ "the amount of produce.");
	}
}
