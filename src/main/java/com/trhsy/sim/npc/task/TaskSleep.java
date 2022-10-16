//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;

/**
 * @Author fan
 * @Description //TODO 回家
 * @Date 16:01 2022/10/16
 * @Param
 * @return
 **/
public class TaskSleep extends Task {
    String statusText;

    public TaskSleep(NpcData folk, long ms, String statusText) {
        super(folk, ms);
        this.statusText = statusText;
    }

    public void onTaskBegin() {
        if (this.folk.home != null) {
            this.folk.stayPut = true;
            if (!this.folk.isAtBuilding(this.folk.home)) {
                //回家
                this.folk.setStatus(I18n.format("container.sim.FolkAction6"));
                this.folk.forceMoveToXYZ(this.folk.home.livingXYZ);
            }
        } else {
            this.folk.stayPut = true;
            this.folk.setStatus(this.statusText);
        }

    }

    public void onUpdate() {
        if (this.folk.home != null) {
            if (!this.folk.isAtBuilding(this.folk.home)) {
                this.folk.forceMoveToXYZ(this.folk.home.livingXYZ);
            } else {
                this.folk.setStatus(this.statusText);
            }
        }

        if (ModSimLoader.isDayTime(this.folk.entity.worldObj)) {
            this.completeTask();
        }

    }

    public void onTaskComplete() {
        this.folk.stayPut = false;
    }
}
