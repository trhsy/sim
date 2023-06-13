package com.trhsy.sim.npc.task;

import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;

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
            //住宅
            boolean isRes = this.building.buildingType.toLowerCase().contentEquals(I18n.format("container.sim.sim_gui_BC_Residential"));
            if (!this.folk.isAtBuilding(this.building, isRes ? 2.0F : 4.0F)) {
                //住宅
                if (this.building.buildingType.toLowerCase().contentEquals(I18n.format("container.sim.sim_gui_BC_Residential"))) {
                    if(!this.folk.forceMoveToXYZ(this.building.livingXYZ)){
                        this.folk.forceMoveToXYZNoWarp(this.building.livingXYZ);
                    }
                } else {
                    if(!this.folk.forceMoveToXYZ(this.building.controlXYZ)){
                        this.folk.forceMoveToXYZNoWarp(this.building.controlXYZ);
                    }
                }
            }
        }
    }

    @Override
    public void onTaskComplete() {
        this.folk.stayPut = false;
    }
}
