package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskChopTrees;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobLumberjack
 * @Description todo 伐木工
 * @Author TRHSY
 * @Date 2023/4/1517:33
 **/
public class JobLumberjack extends Job {
    List<Block> wood = new ArrayList();
    List<Block> toMine = new ArrayList();
    //工作阶段
    public int lumberjackStage = 0;
    public JobLumberjack(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            folk.holding=new ItemStack(ItemLoader.tinAxe);
            this.jobName = new TextComponentTranslation("container.sim.Vocation2",new Object[0]).getUnformattedText();
            this.wood.add(Blocks.LOG);
            this.wood.add(Blocks.LOG2);
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobLumberjack出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.lumberjackStage = 0;
                    this.stage = 0;
                } else if (this.lumberjackStage == 0) {
                    this.lumberjackStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.lumberjackStage == 1) {
                    this.addJobTask(new JobTaskChopTrees(this, -1L, this.workPlace, 60));
                    this.lumberjackStage = 2;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobLumberjack-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
