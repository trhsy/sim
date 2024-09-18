package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskPlaceInChest;
import com.trhsy.sim.npcCode.task.JobTaskSpawnLivestock;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import scala.util.Random;

/**
 * @ClassName JobEggFarmer
 * @Description todo 鸡蛋农
 * @Author TRHSY
 * @Date 2023/4/89:01
 **/
public class JobEggFarmer extends Job{
    //工作阶段
    public int eggFarmerStage = 0;
    public JobEggFarmer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(Items.EGG);
            //鸡蛋农
            this.jobName = new TextComponentTranslation("container.sim.Vocation3",new Object[0]).getUnformattedText();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobEggFarmer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.eggFarmerStage = 0;
                    this.stage = 0;
                } else if (this.eggFarmerStage == 0) {
                    this.eggFarmerStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.eggFarmerStage == 1) {
                    //生成鸡
                    this.addJobTask(new JobTaskSpawnLivestock(this, new TextComponentTranslation("container.sim.job_Livestock_chicken",new Object[0]).getUnformattedText(), EntityChicken.class, 5000L));
                    this.eggFarmerStage = 2;
                } else if (this.eggFarmerStage == 2) {
                    //照料鸡
                    this.addJobTask(new JobTaskIdle(this, 180000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_chicken",new Object[0]).getUnformattedText()));
                    this.eggFarmerStage = 3;
                } else if (this.eggFarmerStage == 3) {
                    //往工作项放东西
                    this.addJobTask(new JobTaskPlaceInChest(this, 10000L, new ItemStack(Items.EGG, new Random().nextInt(5) + 1)));
                    this.eggFarmerStage = 4;
                }else if (this.eggFarmerStage == 4) {
                    //照料鸡
                    this.addJobTask(new JobTaskIdle(this, -1L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_chicken",new Object[0]).getUnformattedText()));
                    this.eggFarmerStage = 5;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobEggFarmer-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        return new TextComponentTranslation("container.sim.Vocation3",new Object[0]).getUnformattedText();
    }
}
