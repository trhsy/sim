package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.block.FarmBox;
import com.trhsy.sim.npcCode.task.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @ClassName JobFarmer
 * @Description todo 农民
 * @Author TRHSY
 * @Date 2022/12/622:48
 **/
public class JobFarmer extends Job {
    //农田箱
    public FarmBox farm;
    //工作阶段
    public int farmer_stage = 0;
    public JobFarmer(NpcData folk, BlockPos pos, World world, FarmBox fb) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(ItemLoader.tinHoe);
            this.jobName = new TextComponentTranslation("container.sim.Vocation5",new Object[0]).getUnformattedText();
            this.farm = fb;
            if(this.farm!=null){
                this.farm.employee = folk;
            }
           /* //去上班
            this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
            //开始在农场工作
            this.addJobTask(new JobTaskFarmer(this, -1L, new TextComponentTranslation("container.sim.job.livestock.farmer.Starting",new Object[0]).getUnformattedText(),this.farm));*/
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.farmer_stage = 0;
                    this.stage = 0;
                } else if (this.farmer_stage == 0) {
                    this.farmer_stage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.farmer_stage == 1) {
                    this.farmer_stage = 2;
                    //锄地
                    this.addJobTask(new JobTaskFarmer(this, -1L, new TextComponentTranslation("container.sim.job.livestock.farmer.Starting",new Object[0]).getUnformattedText(),this.farm));
                } else if (this.farmer_stage == 2) {
                    this.farmer_stage = 3;
                    //种植
                    this.addJobTask(new JobTaskFarmerPlant(this, -1L, new TextComponentTranslation("container.sim.job.livestock.farmer.Starting",new Object[0]).getUnformattedText(),this.farm));
                } else if (this.farmer_stage == 3) {
                    this.farmer_stage = 4;
                    //等待
                    this.addJobTask(new JobTaskFarmerGrow(this, -1L, new TextComponentTranslation("container.sim.job.livestock.farmer.Starting",new Object[0]).getUnformattedText(),this.farm));
                } else if (this.farmer_stage == 4) {
                    this.farmer_stage = 5;
                    //收获
                    this.addJobTask(new JobTaskFarmerHarvest(this, -1L, new TextComponentTranslation("container.sim.job.livestock.farmer.Starting",new Object[0]).getUnformattedText(),this.farm));
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        //农民
        return new TextComponentTranslation("container.sim.Vocation5",new Object[0]).getUnformattedText();
    }
}
