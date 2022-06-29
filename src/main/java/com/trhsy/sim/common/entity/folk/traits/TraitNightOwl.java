package com.trhsy.sim.common.entity.folk.traits;

public class TraitNightOwl extends Trait
{
	public TraitNightOwl() 
	{
		super();
		//夜猫子
		setTraitName("Night Owl");
		//夜幕降临时，夜猫子们不会在室内呆太多时间。他们会在夜间漫步。夜猫子们将继续整夜的地形改造和建筑工作。
		setTraitDescription("Night Owl folk won't spend much time indoors upon nightfall."
				+ " They will wander during the night. Night Owl folk will continue terraforming "
				+ "and building work throughout the night.");
	}
}
