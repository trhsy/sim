package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskSearchForBlock;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobGlassMaker
 * @Description todo 玻璃制造商
 * @Author TRHSY
 * @Date 2023/6/416:29
 **/
public class JobGlassMaker extends Job{
    public V3 v3;
    public JobGlassMaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //手持玻璃
        folk.holding = new ItemStack(Blocks.SAND);
        //玻璃制造商
        this.jobName = I18n.format("container.sim.Vocation17");
        this.v3=new V3(pos.getX(),pos.getY(),pos.getZ());
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
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
                colItems.add(Blocks.SAND);
                //去寻找 沙子
                this.folk.setStatus(I18n.format("container.sim.GOTOCLAYBLOCK"));
                this.addJobTask(new JobTaskSearchForBlock(this, 120000, colItems,this.v3,30,false));
                this.stage = 2;
            }else{
                if (this.jobTasks.size() > 0&&this.currentTask==null) {
                    this.currentTask = (JobTask) this.jobTasks.get(0);
                    this.currentTask.begin();
                }
            }
        }
    }
    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation17");
    }
}
