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
import java.util.concurrent.CopyOnWriteArrayList;

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
        //糖
        colItems.add(new ItemStack(Items.SUGAR, 24));
        //可可豆
        colItems.add(new ItemStack(Items.DYE, 24));
        //打开烘焙工具
        this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job_baker1")));
        //收集
        this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
        this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
        //烘烤 面包 食材 小麦三个
        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.BREAD, new ItemStack(Items.WHEAT, 3), I18n.format("container.sim.job.Baker_Baking")));
        //需要的食材
        List<ItemStack> pumpkinPies=new CopyOnWriteArrayList<>();
        //南瓜
        pumpkinPies.add(new ItemStack(Blocks.PUMPKIN, 1));
        //糖
        pumpkinPies.add(new ItemStack(Items.SUGAR, 1));
        //鸡蛋
        pumpkinPies.add(new ItemStack(Items.EGG, 1));
        //南瓜派
        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.PUMPKIN_PIE, pumpkinPies, I18n.format("container.sim.job.Baker_Baking")));
        //曲奇饼需要的食材
        List<ItemStack> cookies=new CopyOnWriteArrayList<>();
        //可可豆
        cookies.add(new ItemStack(Items.DYE, 1));
        //小麦
        cookies.add(new ItemStack(Items.WHEAT, 2));
        //曲奇
        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKIE, cookies, I18n.format("container.sim.job.Baker_Baking")));
        //蛋糕需要的食材
        List<ItemStack> cakes=new CopyOnWriteArrayList<>();
        //牛奶
        cakes.add(new ItemStack(Items.MILK_BUCKET, 3));
        //糖
        cakes.add(new ItemStack(Items.SUGAR, 2));
        //鸡蛋
        cakes.add(new ItemStack(Items.EGG, 1));
        //小麦
        cakes.add(new ItemStack(Items.WHEAT, 3));
        //蛋糕
        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.CAKE, cakes, I18n.format("container.sim.job.Baker_Baking")));
        this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.job.Baker_bread")));
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation6");
    }
}
