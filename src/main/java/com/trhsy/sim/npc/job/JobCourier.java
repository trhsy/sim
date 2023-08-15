package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskCourier;
import com.trhsy.sim.npc.task.JobTaskCourierDroppingOff;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobCourier
 * @Description: 仓库快递员
 * @date 2023/08/04 上午 9:59
 */
public class JobCourier extends Job{
    public JobCourier(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            //快递员
            this.jobName = I18n.format("container.sim.Vocation10");
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
                    this.stage = 0;
                } else if (this.stage == 0) {
                    this.stage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
                }else if (this.stage == 1) {
                    //去提货
                    this.stage = 2;
                    this.addJobTask(new JobTaskCourier(this, -1L, I18n.format("container.sim.GOINGTOPICKUP")));
                }else if (this.stage == 2) {
                    //去卸货
                    this.stage = 3;
                    this.addJobTask(new JobTaskCourierDroppingOff(this, -1L, I18n.format("container.sim.DROPPINGOFF")));
                }else{
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
