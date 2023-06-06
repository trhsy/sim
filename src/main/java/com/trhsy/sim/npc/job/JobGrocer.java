package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobGrocer
 * @Description todo 食品店
 * @Author TRHSY
 * @Date 2023/4/921:05
 **/
public class JobGrocer extends Job{
    public List<ItemStack> colItems = new ArrayList();
    public JobGrocer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        this.jobName = I18n.format("container.sim.Vocation26");

        //马铃薯
        this.colItems.add(new ItemStack(Items.POTATO, 32));
        //胡萝卜
        this.colItems.add(new ItemStack(Items.CARROT, 32));
        //甜菜根
        this.colItems.add(new ItemStack(Items.BEETROOT, 32));
        //苹果
        this.colItems.add(new ItemStack(Items.APPLE, 32));
        //南瓜
        this.colItems.add(new ItemStack(Blocks.PUMPKIN, 8));
        //西瓜
        this.colItems.add(new ItemStack(Items.MELON, 32));



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
                //装卸货
                this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
                this.stage = 2;
            }else if (this.stage == 2) {
                //装卸货
                this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
                this.stage = 3;
            }else if (this.stage == 3) {
                //售卖
                this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.job.Grocer1")));
                this.stage = 4;
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
        return I18n.format("container.sim.Vocation26");
    }
}
