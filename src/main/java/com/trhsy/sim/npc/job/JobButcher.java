package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskCollectItems;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskShopkeep;
import com.trhsy.sim.npc.task.JobTaskUnloadItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobButcher
 * @Description todo 屠夫
 * @Author TRHSY
 * @Date 2023/4/916:16
 **/
public class JobButcher extends Job{
    public JobButcher(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);

        this.jobName = I18n.format("container.sim.Vocation15");
        //要收集的物品
        List<ItemStack> colItems = new ArrayList();
        //牛肉
        colItems.add(new ItemStack(Items.BEEF, 16));
        //鸡肉
        colItems.add(new ItemStack(Items.CHICKEN, 16));
        //羊肉
        colItems.add(new ItemStack(Items.MUTTON, 16));
        //猪排
        colItems.add(new ItemStack(Items.PORKCHOP, 16));
        //兔肉
        colItems.add(new ItemStack(Items.RABBIT, 16));
        //去上班
        this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
        //收集
        this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
        //卸货
        this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
        /**
         * @Author fan
         * @Description //TODO 卖肉
         * @Date 16:44 2023/4/9
         * @Param [folk, pos, world]
         * @return
         **/
        this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.SELLINGMEAT")));
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation15");
    }
}
