package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskShopkeep;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobBarefootDoctor
 * @Description: 赤脚大夫
 * @date 2023/07/26 下午 2:18
 */
public class JobBarefootDoctor extends Job{
    //工作阶段
    public int barefootDoctorStage = -1;
    public JobBarefootDoctor(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            //手持药水
            folk.holding = new ItemStack(Items.POTIONITEM);
            //赤脚大夫
            this.jobName = new TextComponentTranslation("container.sim.Vocation33",new Object[0]).getUnformattedText();
            /*//去上班
            this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
            //服务客户
            this.addJobTask(new JobTaskShopkeep(this, -1L, new TextComponentTranslation("container.sim.job.serving_customers",new Object[0]).getUnformattedText()));*/
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobBuildersMerchant出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.barefootDoctorStage = 0;
                    this.stage = 0;
                } else if (this.barefootDoctorStage == 0) {
                    this.barefootDoctorStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.barefootDoctorStage == 1) {
                    this.barefootDoctorStage = 2;
                    //服务客户
                    this.addJobTask(new JobTaskShopkeep(this, -1L, new TextComponentTranslation("container.sim.job.serving_customers",new Object[0]).getUnformattedText()));
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobBarefootDoctor-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        return this.jobName;
    }
}