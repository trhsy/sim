package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskHarvestAnimal;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskSpawnLivestock;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import scala.util.Random;

/**
 * @ClassName JobDairyFarmer
 * @Description todo 牛奶农的工作
 * @Author TRHSY
 * @Date 2023/4/517:40
 **/
public class JobDairyFarmer extends Job{
    //工作阶段
    public int dairyFarmerStage = -1;
    public JobDairyFarmer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(Items.MILK_BUCKET);
            //牛奶农
            this.jobName = new TextComponentTranslation("container.sim.Vocation20",new Object[0]).getUnformattedText();
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
                    this.dairyFarmerStage = 0;
                    this.stage = 0;
                } else if (this.dairyFarmerStage == 0) {
                    this.dairyFarmerStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.dairyFarmerStage == 1) {
                    //生成 牛
                    this.addJobTask(new JobTaskSpawnLivestock(this, new TextComponentTranslation("container.sim.job_Livestock_cow",new Object[0]).getUnformattedText(), EntityCow.class, 5000L));
                    this.dairyFarmerStage = 2;
                } else if (this.dairyFarmerStage == 2) {
                    //照料牛
                    this.addJobTask(new JobTaskIdle(this, 180000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_cow",new Object[0]).getUnformattedText()));
                    this.dairyFarmerStage = 3;
                } else if (this.dairyFarmerStage == 3) {
                    // 挤牛奶
                    this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, new TextComponentTranslation("container.sim.job_Livestock_cow",new Object[0]).getUnformattedText(), new ItemStack(ItemLoader.itemBucketMilk, new Random().nextInt(5) + 1), false, new TextComponentTranslation("container.sim.MILKING",new Object[0]).getUnformattedText()));
                    //往工作项放东西
//                this.addJobTask(new JobTaskPlaceInChest(this, 10000L, new ItemStack(ItemLoader.itemBucketMilk, new Random().nextInt(5) + 1)));
                    this.dairyFarmerStage = 4;
                }else if (this.dairyFarmerStage == 4) {
                    //照料牛
                    this.addJobTask(new JobTaskIdle(this, -1L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_cow",new Object[0]).getUnformattedText()));
                    this.dairyFarmerStage = 5;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobDairyFarmer-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public String toString() {
        //牛奶农
        return this.jobName;
    }
}
