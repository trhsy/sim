package com.trhsy.sim.common.entity.folk.traits;

import net.minecraft.client.resources.I18n;

public class TraitNightOwl extends Trait
{
	public TraitNightOwl() 
	{
		super();
		//夜猫子
		setTraitName(I18n.format("container.sim.traits13"));
		//夜幕降临时，夜猫子们不会在室内呆太多时间。他们会在夜间漫步。夜猫子们将继续整夜的地形改造和建筑工作。
		setTraitDescription(I18n.format("container.sim.traits14"));
	}
}
