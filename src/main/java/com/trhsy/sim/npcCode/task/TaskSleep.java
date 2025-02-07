//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * @Author fan
 * @Description //TODO 回家
 * @Date 16:01 2022/10/16
 * @Param
 * @return
 **/
public class TaskSleep extends Task {
    long fs_t;
    public TaskSleep(NpcData folk, long ms, String statusText) {
        super(folk, ms);
        this.statusText = statusText;
        this.fs_t=0l;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 任务开始
     * @Date 10:11 2022/11/13
     * @Param []
     **/
    @Override
    public void onTaskBegin() {
        this.folk.stayPut = true;
        this.folk.holding = new ItemStack(Blocks.AIR);
        this.folk.setStatus(this.statusText);
        this.fs_t=0l;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 更新任务
     * @Date 10:16 2022/11/13
     * @Param []
     **/
    @Override
    public void onUpdate() {
        if (this.folk.home != null&&!this.folk.isAtLocation(this.folk.home.livingXYZ)) {
            this.folk.stayPut = false;
//            if(!this.folk.forceMoveToXYZ(this.folk.home.livingXYZ)){
            V3 v3=new V3(this.folk.home.livingXYZ.x,this.folk.home.livingXYZ.y,this.folk.home.livingXYZ.z);
                this.folk.forceMoveToXYZNoWarp(v3);
//            }
            this.fs_t=System.currentTimeMillis();
        }else{
            this.folk.stayPut = true;
            this.folk.setStatus(this.statusText);
        }
        //白天则完成任务
        if (ModSimLoader.isDayTime(this.folk.entity.world)) {
            this.completeTask();
        }

    }

    @Override
    public void onTaskComplete() {
        this.folk.stayPut = false;
    }
}
