//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.npcCode.NpcData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class TaskWander extends Task {
	public TaskWander(NpcData folk, long ms) {
		super(folk, ms);
	}

	@Override
    public void onTaskBegin() {
		this.folk.setStatus(new TextComponentTranslation("container.sim.folk_data.Wandering",new Object[0]).getUnformattedText());
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onTaskComplete() {
	}
}
