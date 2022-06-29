package com.trhsy.sim.common.entity.folk.traits;

public class TraitDwarvenHeritage extends Trait 
{
	public TraitDwarvenHeritage() 
	{
		super();
		//矮人遗产
		setTraitName("Dwarven Heritage");
		//有矮人血统的人在diggy diggy hole跑得更快。给一个鹤嘴锄，他们的挖掘速度将是普通矿工的两倍。虽然他们更喜欢采矿，但他们仍然是优秀、快速的玻璃制造商。
		setTraitDescription("Folk with a dwarven heritage are faster at diggy diggy hole. Give "
				+ "one a pickaxe and they will excavate twice as fast as an ordinary miner. While "
				+ "they prefer mining, they still make good, fast glass makers.");
	}
}
