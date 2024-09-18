package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskCourier;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobCourier
 * @Description: 仓库快递员
 * @date 2023/08/04 上午 9:59
 */
public class JobCourier extends Job{
    //工作阶段
    public int courierStage = 0;
    public JobCourier(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            //快递员
            this.jobName = new TextComponentTranslation("container.sim.Vocation10",new Object[0]).getUnformattedText();
            this.stage = -1;
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobDairyFarmer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.courierStage = 0;
                    this.stage = 0;
                } else if (this.stage == 0) {
                    this.stage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                }else if (this.stage == 1) {
                    //去提货
                    this.stage = 2;
                    this.addJobTask(new JobTaskCourier(this, -1L, new TextComponentTranslation("container.sim.GOINGTOPICKUP",new Object[0]).getUnformattedText()));
                }/*else if (this.stage == 2) {
                    //去卸货
                    this.stage = 3;
                    this.addJobTask(new JobTaskCourierDroppingOff(this, -1L, new TextComponentTranslation("container.sim.DROPPINGOFF",new Object[0]).getUnformattedText()));
                }*/else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobCourier-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
