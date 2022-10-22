package com.trhsy.sim.npc.task;

import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.NpcData;

/**
 * @ClassName TaskGoTo
 * @Description todo 去哪里的任务
 * @Author TRHSY
 * @Date 2022/10/1615:52
 **/
public class TaskGoTo extends Task{
    Building building;
    String statusText;
    long second;
    public TaskGoTo(NpcData folk, long ms, Building b, String status) {
        super(folk, ms);
        this.building = b;
        this.statusText = status;
    }
    @Override
    public void onTaskBegin() {
        this.folk.stayPut = true;
        this.folk.setStatus(this.statusText);
    }

    @Override
    public void onUpdate() {
        if (System.currentTimeMillis() - this.second > 1000L) {
            this.second = System.currentTimeMillis();
            boolean isRes = this.building.buildingType.toLowerCase().contentEquals("residential");
            if (!this.folk.isAtBuilding(this.building, isRes ? 2.0F : 4.0F)) {
                if (this.building.buildingType.toLowerCase().contentEquals("residential")) {
                    this.folk.forceMoveToXYZ(this.building.livingXYZ);
                } else {
                    this.folk.forceMoveToXYZ(this.building.controlXYZ);
                }
            }
        }
    }

    @Override
    public void onTaskComplete() {
        this.folk.stayPut = false;
    }
}
