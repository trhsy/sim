package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobBaker
 * @Description todo 面包师的工作
 * @Author TRHSY
 * @Date 2022/11/1422:27
 **/
public class JobBaker extends Job{
    public JobBaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //面包师
        this.jobName = I18n.format("container.sim.Vocation6");
        List<ItemStack> colItems = new ArrayList();
        //小麦
        colItems.add(new ItemStack(Items.WHEAT, 24));
        //鸡蛋
        colItems.add(new ItemStack(Items.EGG, 24));
        //南瓜
        colItems.add(new ItemStack(Blocks.PUMPKIN, 24));
        //牛奶
        colItems.add(new ItemStack(Items.MILK_BUCKET, 24));
        //打开烘焙工具
        this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job_baker1")));
        //收集
        this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
        this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
        //烘烤 面包 食材 小麦
        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.BREAD, new ItemStack(Items.WHEAT, 3), I18n.format("container.sim.job.Baker_Baking")));
        this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.job.Baker_bread")));
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation6");
    }
}
