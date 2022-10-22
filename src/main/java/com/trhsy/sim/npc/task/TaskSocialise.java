//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npc.task;

import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;

/**
 * @Author fan
 * @Description //TODO 社交
 * @Date 16:01 2022/10/16
 * @Param
 * @return
 **/
public class TaskSocialise extends Task {
	public NpcData other;
	public Building building;
	public boolean host;
	long socialUpdate = 0L;

	public TaskSocialise(NpcData folk, long ms, NpcData other, Building b, boolean host) {
		super(folk, ms);
		this.other = other;
		this.building = b;
		this.host = host;
	}

	public void onTaskBegin() {
		this.folk.stayPut = true;
		//
		this.folk.setStatus(I18n.format("container.sim.folk_data_Hanging") + this.other.forename);
		if (this.other == null) {
			//other was null
			this.failTask(I18n.format("container.sim.folk_other_was_null"));
		}

	}

	public void onUpdate() {
		if (!this.folk.isAtBuilding(this.building)) {
			this.folk.forceMoveToXYZ(this.building.livingXYZ);
		} else if (System.currentTimeMillis() - this.socialUpdate > 10000L && this.host) {
			this.socialUpdate = System.currentTimeMillis();
			this.folk.adjustRelationship(this.other, 1);
		}

	}

	public void onTaskComplete() {
		this.folk.stayPut = false;
	}
}
