package com.trhsy.sim.common.entity.folk.traits;

public class TraitStrong extends Trait
{
	public TraitStrong() 
	{
		super();
		//强健的
		setTraitName("Strong");
		//强壮的人可以更快地完成体力劳动。无论是采矿、伐木、建筑还是挖沙。
		setTraitDescription("Strong folk can perform manual labour faster. Be it mining, cutting wood, "
				+ "building or digging sand.");
	}
}
