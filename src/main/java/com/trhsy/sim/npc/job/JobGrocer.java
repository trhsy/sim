package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskCollectItems;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskShopkeep;
import com.trhsy.sim.npc.task.JobTaskUnloadItems;
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
    public JobGrocer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        this.jobName = I18n.format("container.sim.Vocation26");
        List<ItemStack> colItems = new ArrayList();
        //马铃薯
        colItems.add(new ItemStack(Items.POTATO, 32));
        //胡萝卜
        colItems.add(new ItemStack(Items.CARROT, 32));
        //甜菜根
        colItems.add(new ItemStack(Items.BEETROOT, 32));
        //苹果
        colItems.add(new ItemStack(Items.APPLE, 32));
        //南瓜
        colItems.add(new ItemStack(Blocks.PUMPKIN, 8));
        //西瓜
        colItems.add(new ItemStack(Items.MELON, 32));
        //去上班
        this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
        //装卸货
        this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
        this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
        //售卖
        this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.job.Grocer1")));
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation26");
    }
}
