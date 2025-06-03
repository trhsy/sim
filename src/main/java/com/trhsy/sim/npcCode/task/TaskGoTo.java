package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.Building;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @ClassName TaskGoTo
 * @Description todo 去哪里的任务
 * @Author TRHSY
 * @Date 2022/10/1615:52
 **/
public class TaskGoTo extends Task{
    Building building;
    long second;
    public TaskGoTo(NpcData folk, long ms, Building b, String status) {
        super(folk, ms);
        this.building = b;
        this.statusText = status;
    }
    @Override
    public void onTaskBegin() {
        this.folk.stayPut = false;
        this.folk.setStatus(this.statusText);
    }

    @Override
    public void onUpdate() {
        if (System.currentTimeMillis() - this.second > 1000L) {
            this.second = System.currentTimeMillis();
            //住宅
            boolean isRes = this.building.buildingType.toLowerCase().contentEquals(new TextComponentTranslation("container.sim.sim_gui_BC_Residential",new Object[0]).getUnformattedText());
            if (!this.folk.isAtBuilding(this.building, 2)) {
                //住宅
                if (this.building.buildingType.toLowerCase().contentEquals(new TextComponentTranslation("container.sim.sim_gui_BC_Residential",new Object[0]).getUnformattedText())) {
                    V3 v3=new V3(this.building.livingXYZ.x+0.5,this.building.livingXYZ.y,this.building.livingXYZ.z+0.5);
                    this.folk.forceMoveToXYZ(v3);
                } else {
                    V3 v3=null;
                    if(this.building.livingXYZ!=null){
                        v3=new V3(this.building.controlXYZ.x+0.5,this.building.controlXYZ.y,this.building.controlXYZ.z+0.5);
                    }else{
                        v3=new V3(this.building.controlXYZ.x+0.5,this.building.controlXYZ.y,this.building.controlXYZ.z+0.5);
                    }
                    this.folk.forceMoveToXYZ(v3);
                }
            }else{
                this.onTaskComplete();
            }
        }
    }

    @Override
    public void onTaskComplete() {
        this.folk.stayPut = false;
        this.completed=true;
    }

}
