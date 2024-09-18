package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskButcherAnimal;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskSpawnLivestock;
import net.minecraft.entity.passive.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobLivestockFarmer
 * @Description:
 * @date 2023/3/21 14:04
 */
public class JobLivestockFarmer extends Job{
    /**
     * 工作名称牲畜名称
     */
    public String livestockName;
    /**
     * 牲畜等级
     */
    public Class livestockClass;
    //工作阶段
    public int livestockFarmerStage = 0;
    public JobLivestockFarmer(NpcData folk, BlockPos pos, String livestock, World world) {
        super(folk,pos,world);
        try {
//手持斧子
            folk.holding = new ItemStack(ItemLoader.tinAxe);
            this.livestockName = livestock;
            //猪
            if (this.livestockName.equals(new TextComponentTranslation("container.sim.job_Livestock_pig",new Object[0]).getUnformattedText())) {
                this.livestockClass = EntityPig.class;
                //养猪户
                this.jobName = new TextComponentTranslation("container.sim.Vocation13",new Object[0]).getUnformattedText();
                //牛
            } else if (this.livestockName.equals(new TextComponentTranslation("container.sim.job_Livestock_cow",new Object[0]).getUnformattedText())) {
                this.livestockClass = EntityCow.class;
                //养牛户
                this.jobName =new TextComponentTranslation("container.sim.Vocation12",new Object[0]).getUnformattedText();
                //羊
            } else if (this.livestockName.equals(new TextComponentTranslation("container.sim.job_Livestock_sheep",new Object[0]).getUnformattedText())) {
                this.livestockClass = EntitySheep.class;
                //养羊户
                this.jobName = new TextComponentTranslation("container.sim.Vocation28",new Object[0]).getUnformattedText();
                //鸡
            } else if (this.livestockName.equals(new TextComponentTranslation("container.sim.job_Livestock_chicken",new Object[0]).getUnformattedText())) {
                this.livestockClass = EntityChicken.class;
                //养鸡户
                this.jobName = new TextComponentTranslation("container.sim.Vocation14",new Object[0]).getUnformattedText();
                //兔
            }else if (this.livestockName.equals(new TextComponentTranslation("container.sim.job_Livestock_rabbit",new Object[0]).getUnformattedText())) {
                this.livestockClass = EntityRabbit.class;
                //养兔户
                this.jobName =  new TextComponentTranslation("container.sim.Vocation29",new Object[0]).getUnformattedText();
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobLivestockFarmer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.livestockFarmerStage = 0;
                    this.stage = 0;
                } else if (this.livestockFarmerStage == 0) {
                    this.livestockFarmerStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.livestockFarmerStage == 1) {
                    //生成
                    this.addJobTask(new JobTaskSpawnLivestock(this, this.livestockName, this.livestockClass, 5000L));
                    this.livestockFarmerStage = 2;
                }else if (this.livestockFarmerStage == 2) {
                    //照料
                    this.addJobTask(new JobTaskIdle(this, 5000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+" " + this.livestockName));
                    this.livestockFarmerStage = 3;
                }else if (this.livestockFarmerStage == 3) {
                    //屠戮畜生
                    this.addJobTask(new JobTaskButcherAnimal(this, 5000L));
                    this.livestockFarmerStage = 4;
                }else if (this.livestockFarmerStage == 4) {
                    //照料
                    this.addJobTask(new JobTaskIdle(this, 5000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+" " + this.livestockName ));
                    this.livestockFarmerStage = 5;
                }else if (this.livestockFarmerStage == 5) {
                    //屠戮畜生
                    this.addJobTask(new JobTaskButcherAnimal(this, 5000L));
                    this.livestockFarmerStage = 6;
                }else if (this.livestockFarmerStage == 6) {
                    //照料
                    this.addJobTask(new JobTaskIdle(this, 5000L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+" " + this.livestockName ));
                    this.livestockFarmerStage =7;
                }else if (this.livestockFarmerStage == 7) {
                    //屠戮畜生
                    this.addJobTask(new JobTaskButcherAnimal(this, 5000L));
                    this.livestockFarmerStage = 8;
                }else if (this.livestockFarmerStage == 9) {
                    //照料
                    this.addJobTask(new JobTaskIdle(this, -1L, new TextComponentTranslation("container.sim.job.crop.farmer.Tending1",new Object[0]).getUnformattedText()+" " + this.livestockName ));
                    this.livestockFarmerStage = 10;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobLivestockFarmer-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        //农民
        return this.jobName;
    }
}
