package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskPatrol;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @ClassName JobSoldier
 * @Description todo 士兵
 * @Author TRHSY
 * @Date 2023/4/1517:34
 **/
public class JobSoldier extends Job{
    //工作阶段
    public int soldierStage = 0;
    public JobSoldier(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.tinSword);
        //士兵
        this.jobName = new TextComponentTranslation("container.sim.Vocation7",new Object[0]).getUnformattedText();

    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.stage = 0;
                    this.soldierStage=0;
                } else if (this.soldierStage == 0) {
                    this.soldierStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.soldierStage == 1) {
                    this.addJobTask(new JobTaskPatrol(this, -1L));
                    this.soldierStage = 2;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobSoldier-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
