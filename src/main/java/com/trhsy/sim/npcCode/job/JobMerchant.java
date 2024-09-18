package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskShopkeep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobMerchant 杂货商的工作
 * @Description:
 * @date 2023/07/24 下午 2:14
 */
public class JobMerchant extends Job{
    //工作阶段
    public int merchantStage = 0;
    public JobMerchant(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            //手持末影珍珠
            folk.holding = new ItemStack(Items.ENDER_PEARL);
            //行长
            this.jobName = new TextComponentTranslation("container.sim.Vocation9",new Object[0]).getUnformattedText();
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
                    this.merchantStage = 0;
                    this.stage = 0;
                } else if (this.merchantStage == 0) {
                    this.merchantStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.merchantStage == 1) {
                    this.merchantStage = 2;
                    //服务客户
                    this.addJobTask(new JobTaskShopkeep(this, -1L, new TextComponentTranslation("container.sim.job.serving_customers",new Object[0]).getUnformattedText(),false));
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFlower-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        return this.jobName;
    }
}