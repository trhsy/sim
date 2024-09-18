//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.build.Building;
import net.minecraft.util.text.TextComponentTranslation;

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

	@Override
    public void onTaskBegin() {
		this.folk.stayPut = false;
		//
		this.folk.setStatus(new TextComponentTranslation("container.sim.folk_data_Hanging",new Object[0]).getUnformattedText() + this.other.forename);
		if (this.other == null) {
			//other was null
			this.failTask(new TextComponentTranslation("container.sim.folk_other_was_null",new Object[0]).getUnformattedText());
		}

	}

	@Override
	public void onUpdate() {
		if (!this.folk.isAtBuilding(this.building)) {
			if(!this.folk.forceMoveToXYZ(this.folk.home.livingXYZ)){
				this.folk.forceMoveToXYZNoWarp(this.folk.home.livingXYZ);
			}
		} else if (System.currentTimeMillis() - this.socialUpdate > 10000L && this.host) {
			this.socialUpdate = System.currentTimeMillis();
			this.folk.adjustRelationship(this.other, 1);
		}

	}

	@Override
	public void onTaskComplete() {
		this.folk.stayPut = false;
	}
}
