package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
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
public class JobBaker extends Job {
    //小麦，鸡蛋，南瓜，牛奶,糖,可可豆
    private int wheat, egg, pumpkin, milk_bucket, sugar, dye;
    //工作阶段
    public int baker_stage = 0;
    // 上次统计物品的时间戳（单位：ticks，1 tick = 1/20秒）
    private long lastItemStatsTime = 0;
    // 统计间隔（五分钟 = 5 * 60秒 = 300秒 = 300 * 20 ticks = 6000 ticks）
    private static final long STATS_INTERVAL = 6000;
    public JobBaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.tinSpade);
        //面包师
        this.jobName = new TextComponentTranslation("container.sim.Vocation6",new Object[0]).getUnformattedText();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
        if (this.atWork) {
            if (this.stage == -1) {
                this.baker_stage = 0;
                this.stage = 0;
            }else if (this.baker_stage == 0) {
                this.baker_stage = 1;
                //去上班
                this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
            }else if (this.baker_stage == 1) {
                //打开烘焙工具
                this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job_baker1",new Object[0]).getUnformattedText()));
                this.baker_stage = 2;
            } else if (this.baker_stage == 2) {
                List<ItemStack> colItems = new ArrayList();
                //小麦
                colItems.add(new ItemStack(Items.WHEAT, 16));
                //鸡蛋
                colItems.add(new ItemStack(Items.EGG, 16));
                //南瓜
                colItems.add(new ItemStack(Blocks.PUMPKIN, 16));
                //牛奶
                colItems.add(new ItemStack(ItemLoader.itemBucketMilk, 1));
                //糖
                colItems.add(new ItemStack(Items.SUGAR, 16));
                //可可豆
                colItems.add(new ItemStack(Items.DYE, 16));
                //收集
                this.addJobTask(new JobTaskCollectItems(this, -1L, colItems));
                this.baker_stage = 3;
            } else if (this.baker_stage == 3) {
                List<ItemStack> colItems = new ArrayList();
                //小麦
                colItems.add(new ItemStack(Items.WHEAT, 24));
                //鸡蛋
                colItems.add(new ItemStack(Items.EGG, 24));
                //南瓜
                colItems.add(new ItemStack(Blocks.PUMPKIN, 24));
                //牛奶
                colItems.add(new ItemStack(ItemLoader.itemBucketMilk, 24));
                //糖
                colItems.add(new ItemStack(Items.SUGAR, 24));
                //可可豆
                colItems.add(new ItemStack(Items.DYE, 24));
                this.addJobTask(new JobTaskUnloadItems(this, -1L, colItems));
                this.baker_stage = 4;
            } else if (this.baker_stage == 4) {

                    if (this.milk_bucket >= 3 && this.sugar >= 2 && this.egg >= 1 && this.wheat > 3) {
                        //蛋糕需要的食材
                        List<ItemStack> cakes = new CopyOnWriteArrayList<ItemStack>();
                        //牛奶
                        cakes.add(new ItemStack(ItemLoader.itemBucketMilk, 3));
                        //糖
                        cakes.add(new ItemStack(Items.SUGAR, 2));
                        //鸡蛋
                        cakes.add(new ItemStack(Items.EGG, 1));
                        //小麦
                        cakes.add(new ItemStack(Items.WHEAT, 3));
                        //蛋糕
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.CAKE, cakes, new TextComponentTranslation("container.sim.job.Baker_Baking",new Object[0]).getUnformattedText()));
                    }
                    if (this.pumpkin > 1 && this.sugar > 1 && this.egg > 1) {
                        //南瓜饼需要的食材
                        List<ItemStack> pumpkinPies = new CopyOnWriteArrayList<ItemStack>();
                        //南瓜
                        pumpkinPies.add(new ItemStack(Blocks.PUMPKIN, 1));
                        //糖
                        pumpkinPies.add(new ItemStack(Items.SUGAR, 1));
                        //鸡蛋
                        pumpkinPies.add(new ItemStack(Items.EGG, 1));
                        //南瓜派
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.PUMPKIN_PIE, pumpkinPies, new TextComponentTranslation("container.sim.job.Baker_Baking",new Object[0]).getUnformattedText()));
                    }
                    if (this.dye >= 1 && this.wheat >= 2) {
                        //曲奇饼需要的食材
                        List<ItemStack> cookies = new CopyOnWriteArrayList<ItemStack>();
                        //可可豆
                        cookies.add(new ItemStack(Items.DYE, 1));
                        //小麦
                        cookies.add(new ItemStack(Items.WHEAT, 2));
                        //曲奇
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKIE, cookies, new TextComponentTranslation("container.sim.job.Baker_Baking",new Object[0]).getUnformattedText()));
                    }
                    if (this.wheat > 3) {
                        //烘烤 面包 食材 小麦三个
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.BREAD, new ItemStack(Items.WHEAT, 3), new TextComponentTranslation("container.sim.job.Baker_Baking",new Object[0]).getUnformattedText()));
                    }
                    this.baker_stage = 5;
            } else if (this.baker_stage == 5) {
                //售卖/关店
                this.addJobTask(new JobTaskShopkeep(this, -1L, new TextComponentTranslation("container.sim.job.Baker_bread",new Object[0]).getUnformattedText(),true));
                this.baker_stage = 6;
            } else if (this.baker_stage == 6) {
                //在去工作途中，并且已经到了工作位置则更新状态
                if (this.atWork && this.folk.isAtLocation(this.workPlace) && this.currentTask == null && this.jobTasks.size() > 0) {
                    if (this.jobTasks.size() > 0) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                } else if (((this.wheat > 3) || (this.pumpkin > 1 && this.sugar > 1 && this.egg > 1) || (this.dye > 1 && this.wheat > 2) || (this.milk_bucket > 3 && this.sugar > 2 && this.egg > 1 && this.wheat > 3)) && this.folk.getStatusText().contains(new TextComponentTranslation("container.sim.job_task_Selling",new Object[0]).getUnformattedText())) {
                    this.baker_stage = 4;
                    if(this.currentTask!=null){
                        this.currentTask.completeTask();
                        this.jobTasks.clear();
                    }
                }
            }else{
                this.baker_stage = 0;
                this.jobTasks.clear();
            }
            // ---------------------- 物品统计优化 ----------------------
            long currentTime = this.jobWorld.getWorldTime(); // 获取当前游戏时间（ticks）
            // 检查是否达到统计间隔（五分钟）
            if (currentTime - lastItemStatsTime >= STATS_INTERVAL) {
                this.wheat = 0;
                this.egg = 0;
                this.pumpkin = 0;
                this.milk_bucket = 0;
                this.sugar = 0;
                this.dye = 0;
                List<IInventory> iterator = this.findJobChests(5);
                for (IInventory inv : iterator) {
                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                        ItemStack slot = inv.getStackInSlot(i);
                        ItemStack itemWheat = new ItemStack(Items.WHEAT);
                        ItemStack itemEgg = new ItemStack(Items.EGG);
                        ItemStack itemPumpkin = new ItemStack(Blocks.PUMPKIN);
                        ItemStack itemBucketMilk = new ItemStack(ItemLoader.itemBucketMilk);
                        ItemStack itemSugar = new ItemStack(Items.SUGAR);
                        ItemStack itemDye = new ItemStack(Items.DYE);
                        if (slot != null) {
                            if (slot.isItemEqual(itemWheat)) {
                                this.wheat += slot.getCount();
                            } else if (slot.isItemEqual(itemEgg)) {
                                this.egg += slot.getCount();
                            } else if (slot.isItemEqual(itemPumpkin)) {
                                this.pumpkin += slot.getCount();
                            } else if (slot.isItemEqual(itemBucketMilk)) {
                                this.milk_bucket += slot.getCount();
                            } else if (slot.isItemEqual(itemSugar)) {
                                this.sugar += slot.getCount();
                            } else if (slot.getItem().equals(itemDye.getItem())) {
                                this.dye += slot.getCount();
                            }
                        }
                    }
                }
                // 更新上次统计时间
                this.lastItemStatsTime = currentTime;
            }

        }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("jobMaker-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    public void onArrive() {
    }

    @Override
    public String toString() {
        return new TextComponentTranslation("container.sim.Vocation6",new Object[0]).getUnformattedText();
    }
}
