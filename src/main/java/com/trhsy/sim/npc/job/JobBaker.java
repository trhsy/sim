package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
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
public class JobBaker extends Job {
    //小麦，鸡蛋，南瓜，牛奶,糖,可可豆
    private int wheat, egg, pumpkin, milk_bucket, sugar, dye;

    public int theStage = -1;
    private int stage = -1;

    public JobBaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.tinSpade);
        //面包师
        this.jobName = I18n.format("container.sim.Vocation6");
        this.theStage = -1;
        this.stage = -1;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
        if (this.atWork) {
            if (this.theStage == -1) {
                //打开烘焙工具
                this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job_baker1")));
                this.theStage = 0;
            } else if (this.theStage == 0) {
                List<ItemStack> colItems = new ArrayList();
                //小麦
                colItems.add(new ItemStack(Items.WHEAT, 16));
                //鸡蛋
                colItems.add(new ItemStack(Items.EGG, 16));
                //南瓜
                colItems.add(new ItemStack(Blocks.PUMPKIN, 16));
                //牛奶
                colItems.add(new ItemStack(Items.MILK_BUCKET, 3));
                //糖
                colItems.add(new ItemStack(Items.SUGAR, 16));
                //可可豆
                colItems.add(new ItemStack(Items.DYE, 16));
                //收集
                this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
                this.theStage = 1;
            } else if (this.theStage == 1) {
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
                this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
                this.theStage = 2;
            } else if (this.theStage == 2) {

                if (this.stage == -1) {
                    if (this.milk_bucket >= 3 && this.sugar >= 2 && this.egg >= 1 && this.wheat > 3) {
                        //蛋糕需要的食材
                        List<ItemStack> cakes = new CopyOnWriteArrayList<>();
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
                    }

                    this.stage = 0;
                } else if (this.stage == 0) {
                    if (this.pumpkin > 1 && this.sugar > 1 && this.egg > 1) {
                        //需要的食材
                        List<ItemStack> pumpkinPies = new CopyOnWriteArrayList<>();
                        //南瓜
                        pumpkinPies.add(new ItemStack(Blocks.PUMPKIN, 1));
                        //糖
                        pumpkinPies.add(new ItemStack(Items.SUGAR, 1));
                        //鸡蛋
                        pumpkinPies.add(new ItemStack(Items.EGG, 1));
                        //南瓜派
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.PUMPKIN_PIE, pumpkinPies, I18n.format("container.sim.job.Baker_Baking")));
                    }
                    this.stage = 1;
                } else if (this.stage == 1) {
                    if (this.dye >= 1 && this.wheat >= 2) {
                        //曲奇饼需要的食材
                        List<ItemStack> cookies = new CopyOnWriteArrayList<>();
                        //可可豆
                        cookies.add(new ItemStack(Items.DYE, 1));
                        //小麦
                        cookies.add(new ItemStack(Items.WHEAT, 2));
                        //曲奇
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKIE, cookies, I18n.format("container.sim.job.Baker_Baking")));
                    }
                    this.stage = 2;
                } else if (this.stage == 2) {
                    if (this.wheat > 3) {
                        //烘烤 面包 食材 小麦三个
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.BREAD, new ItemStack(Items.WHEAT, 3), I18n.format("container.sim.job.Baker_Baking")));
                    }
                    this.stage = 3;
                } else {
                    this.theStage = 3;
                }
            } else if (this.theStage == 3) {
                //售卖/关店
                this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.job.Baker_bread")));
                this.theStage = 4;
            } else if (this.theStage == 4) {
                //在去工作途中，并且已经到了工作位置则更新状态
                if (this.atWork && this.folk.isAtLocation(this.workPlace) && this.currentTask == null && this.jobTasks.size() > 0) {
                    if (this.jobTasks.size() > 0) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                } else if ((this.wheat > 32 ||(this.pumpkin > 1 && this.sugar > 1 && this.egg > 1)||(this.dye > 1 && this.wheat > 2)||(this.milk_bucket > 3 && this.sugar > 2 && this.egg > 1 && this.wheat > 3)) && this.folk.getStatusText().contains(I18n.format("container.sim.job_task_Selling"))) {
                    this.theStage = 2;
                    this.stage = -1;
                    this.currentTask.completeTask();
                }
            }
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
                    if (slot != null) {
                        if (slot.isItemEqual(new ItemStack(Items.WHEAT))) {
                            this.wheat += slot.stackSize;
                        } else if (slot.isItemEqual(new ItemStack(Items.EGG))) {
                            this.egg += slot.stackSize;
                        } else if (slot.isItemEqual(new ItemStack(Blocks.PUMPKIN))) {
                            this.pumpkin += slot.stackSize;
                        } else if (slot.isItemEqual(new ItemStack(Items.MILK_BUCKET))) {
                            this.milk_bucket += slot.stackSize;
                        } else if (slot.isItemEqual(new ItemStack(Items.SUGAR))) {
                            this.sugar += slot.stackSize;
                        } else if (slot.isItemEqual(new ItemStack(Items.DYE))) {
                            this.dye += slot.stackSize;
                        }
                    }
                }
            }
        }
        }catch (Exception e){

        }
    }

    @Override
    public void onArrive() {
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation6");
    }
}
