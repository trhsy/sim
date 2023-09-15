package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobBurgers
 * @Description: 汉堡店经理
 * @date 2023/07/28 下午 3:44
 */
public class JobBurgers extends Job{
    //奶酪 牛肉 面包 土豆
    private int itemCheese, beef, bread, potato;
    public JobBurgers(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.tinSpade);
        //汉堡店
        this.jobName = I18n.format("container.sim.Vocation36");
        this.stage = -1;
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.stage = 0;
                }else if (this.stage == 0) {
                    this.stage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
                }else if (this.stage == 1) {
                    //打开烘焙工具
                    this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job_baker1")));
                    this.stage = 2;
                } else if (this.stage == 2) {
                    List<ItemStack> colItems = new ArrayList();
                    //面包
                    colItems.add(new ItemStack(Items.BREAD, 16));
                    //牛肉
                    colItems.add(new ItemStack(Items.BEEF, 16));
                    //土豆
                    colItems.add(new ItemStack(Items.POTATO, 16));
                    //奶酪
                    colItems.add(new ItemStack(ItemLoader.itemCheese, 16));
                    //收集
                    this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
                    this.stage = 3;
                } else if (this.stage == 3) {
                    List<ItemStack> colItems = new ArrayList();
                    //面包
                    colItems.add(new ItemStack(Items.BREAD, 16));
                    //牛肉
                    colItems.add(new ItemStack(Items.BEEF, 16));
                    //土豆
                    colItems.add(new ItemStack(Items.POTATO, 16));
                    //奶酪
                    colItems.add(new ItemStack(ItemLoader.itemCheese, 16));
                    this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
                    this.stage = 4;
                } else if (this.stage == 4) {

                    if (this.itemCheese >= 1 && this.beef >= 1 && this.bread >= 2) {
                        //蛋糕需要的食材
                        List<ItemStack> cakes = new CopyOnWriteArrayList<ItemStack>();
                        //奶酪
                        cakes.add(new ItemStack(ItemLoader.itemCheese, 1));
                        //牛肉
                        cakes.add(new ItemStack(Items.BEEF, 1));
                        //面包
                        cakes.add(new ItemStack(Items.BREAD, 2));
                        //奶酪汉堡
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, ItemLoader.itemCheeseburger, cakes, I18n.format("container.sim.job.Baker_Baking")));
                    }
                    if (this.beef >= 1 && this.bread >= 2) {
                        //需要的食材
                        List<ItemStack> pumpkinPies = new CopyOnWriteArrayList<ItemStack>();
                        //牛肉
                        pumpkinPies.add(new ItemStack(Items.BEEF, 1));
                        //面包
                        pumpkinPies.add(new ItemStack(Items.BREAD, 2));
                        //汉堡
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, ItemLoader.itemBurger, pumpkinPies, I18n.format("container.sim.job.Baker_Baking")));
                    }
                    if (this.potato > 1) {
                        //烘烤 薯条
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, ItemLoader.itemFries, new ItemStack(Items.WHEAT, 3), I18n.format("container.sim.job.Baker_Baking")));
                    }
                    this.stage = 5;
                } else if (this.stage == 5) {
                    //售卖/关店
                    this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.job.Grocer1")));
                    this.stage = 6;
                } else if (this.stage == 6) {
                    //在去工作途中，并且已经到了工作位置则更新状态
                    if (this.atWork && this.folk.isAtLocation(this.workPlace) && this.currentTask == null && this.jobTasks.size() > 0) {
                        if (this.jobTasks.size() > 0) {
                            this.currentTask = (JobTask) this.jobTasks.get(0);
                            this.currentTask.begin();
                        }
                    } else if (((this.potato > 2) ||(this.beef > 1 && this.bread > 2)||(this.beef > 1 && this.bread > 2&&this.itemCheese > 2)) && this.folk.getStatusText().contains(I18n.format("container.sim.job_task_Selling"))) {
                        this.stage = 4;
                        this.currentTask.completeTask();
                        this.jobTasks.clear();
                    }
                }else{
                    this.stage = 0;
                    this.jobTasks.clear();
                }
                this.bread = 0;
                this.beef = 0;
                this.potato = 0;
                this.itemCheese = 0;
                List<IInventory> iterator = this.findJobChests(5);
                for (IInventory inv : iterator) {
                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                        ItemStack slot = inv.getStackInSlot(i);
                        if (slot != null) {
                            if (slot.isItemEqual(new ItemStack(Items.BREAD))) {
                                this.bread += slot.getCount();
                            } else if (slot.isItemEqual(new ItemStack(Items.BEEF))) {
                                this.beef += slot.getCount();
                            } else if (slot.isItemEqual(new ItemStack(Items.POTATO))) {
                                this.potato += slot.getCount();
                            } else if (slot.isItemEqual(new ItemStack(ItemLoader.itemCheese))) {
                                this.itemCheese += slot.getCount();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobBurgers-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
