//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npc.task;

import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;

public class TaskWander extends Task {
	public TaskWander(NpcData folk, long ms) {
		super(folk, ms);
	}

	@Override
    public void onTaskBegin() {
		this.folk.setStatus(I18n.format("container.sim.folk_data.Wandering"));
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onTaskComplete() {
	}
}
