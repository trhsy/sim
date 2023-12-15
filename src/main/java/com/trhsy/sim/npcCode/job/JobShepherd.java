package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskHarvestAnimal;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskSpawnLivestock;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @ClassName JobShepherd
 * @Description todo 羊毛农
 * @Author TRHSY
 * @Date 2023/4/521:04
 **/
public class JobShepherd extends Job{
    //工作阶段
    public int shepherdStage = -1;
    public JobShepherd(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //手持剪刀
        folk.holding = new ItemStack(Items.SHEARS);
        //牧羊人
        this.jobName = new TextComponentTranslation("container.sim.Vocation8",new Object[0]).getUnformattedText();
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.shepherdStage = 0;
                    this.stage = 0;
                } else if (this.shepherdStage == 0) {
                    this.shepherdStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.shepherdStage == 1) {
                    //生成羊
                    this.addJobTask(new JobTaskSpawnLivestock(this, new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText(), EntitySheep.class, 5000L));
                    this.shepherdStage = 2;
                }else if (this.shepherdStage == 2) {
                    //照料羊
                    this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText()));
                    this.shepherdStage = 3;
                }else if (this.shepherdStage == 3) {
                    //剪羊毛
                    this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText(), new ItemStack(Blocks.WOOL, new Random().nextInt(6)+1), true, new TextComponentTranslation("container.sim.SHEARING",new Object[0]).getUnformattedText()));
                    this.shepherdStage = 4;
                }else if (this.shepherdStage == 4) {
                    //照料羊
                    this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText()));
                    this.shepherdStage = 5;
                }else if (this.shepherdStage == 5) {
                    //剪羊毛
                    this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText(), new ItemStack(Blocks.WOOL, new Random().nextInt(6)+1), true, new TextComponentTranslation("container.sim.SHEARING",new Object[0]).getUnformattedText()));
                    this.shepherdStage = 6;
                }else if (this.shepherdStage == 6) {
                    //照料羊
                    this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText()));
                    this.shepherdStage = 7;
                }else if (this.shepherdStage == 7) {
                    //剪羊毛
                    this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText(), new ItemStack(Blocks.WOOL, new Random().nextInt(6)+1), true, new TextComponentTranslation("container.sim.SHEARING",new Object[0]).getUnformattedText()));
                    this.shepherdStage = 8;
                }else if (this.shepherdStage == 8) {
                    //生成羊
                    this.addJobTask(new JobTaskIdle(this, -1L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText()));
                    this.shepherdStage = 9;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobShepherd-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
