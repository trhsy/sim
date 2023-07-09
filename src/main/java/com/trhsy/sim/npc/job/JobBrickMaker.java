package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ConfigLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.task.JobTaskChopTrees;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskSearchForBlock;
import com.trhsy.sim.npc.task.JobTaskUseFurnace;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobBrickMaker
 * @Description todo 板砖工匠
 * @Author TRHSY
 * @Date 2023/6/416:29
 **/
public class JobBrickMaker extends Job{
    public V3 v3;
    public JobBrickMaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(Blocks.CLAY);
            //板砖工匠
            this.jobName = I18n.format("container.sim.Vocation25");
            this.v3=new V3(pos.getX(),pos.getY(),pos.getZ());
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobBrickMaker出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                } else if (this.stage == 1) {
                    //要收集的物品
                    List<Block> colItems = new ArrayList();
                    //黏土
                    colItems.add(Blocks.CLAY);
                    //寻找黏土
                    //去寻找
                    this.folk.setStatus(I18n.format("container.sim.GOTOCLAYBLOCK"));
                    this.addJobTask(new JobTaskSearchForBlock(this, 120000, colItems,this.v3, ConfigLoader.configLumberArea,false));
                    this.stage = 2;
                }else if(this.stage == 2){
                    this.addJobTask(new JobTaskUseFurnace(this, 120000, new ItemStack(Blocks.CLAY)));
                    this.stage = 3;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobBrickMaker-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation25");
    }
}
