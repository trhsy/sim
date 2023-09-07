package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
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
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobSugar
 * @Description: 制糖师工作
 * @date 2023/5/9 11:47
 */
public class JobSugar extends Job{
    private int sugarcane;

    public JobSugar(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.tinSpade);
        //制糖师
        this.jobName = I18n.format("container.sim.Vocation30");
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
                }else if (this.stage == 1) {
                    //打开烘焙工具
                    this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job_baker1")));
                    this.stage = 2;
                } else if (this.stage == 2) {
                    List<ItemStack> colItems = new ArrayList();
                    //甘蔗
                    colItems.add(new ItemStack(Items.REEDS, 64));
                    //收集
                    this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
                    this.stage = 3;
                } else if (this.stage == 3) {
                    List<ItemStack> colItems = new ArrayList();
                    //甘蔗
                    colItems.add(new ItemStack(Items.REEDS, 64));
                    this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
                    this.stage = 4;
                } else if (this.stage == 4) {

                    if (this.sugarcane >= 1 ) {
                        //蛋糕需要的食材
                        List<ItemStack> cakes = new CopyOnWriteArrayList<ItemStack>();
                        //甘蔗
                        cakes.add(new ItemStack(Items.REEDS, 1));
                        //糖
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.SUGAR, cakes, I18n.format("container.sim.job.Baker_Baking_Sugar")));
                    }
                    this.stage = 5;
                } else if (this.stage == 5) {
                    //售卖/关店
                    this.addJobTask(new JobTaskShopkeep(this, -1L, ""));
                    this.stage = 6;
                } else if (this.stage == 6) {
                    //在去工作途中，并且已经到了工作位置则更新状态
                    if (this.atWork && this.folk.isAtLocation(this.workPlace) && this.currentTask == null && this.jobTasks.size() > 0) {
                        if (this.jobTasks.size() > 0) {
                            this.currentTask = (JobTask) this.jobTasks.get(0);
                            this.currentTask.begin();
                        }
                    } else if ((this.sugarcane > 1)  && this.folk.getStatusText().contains(I18n.format("container.sim.job_task_Selling"))) {
                        this.stage = 4;
                        this.currentTask.completeTask();
                        this.jobTasks.clear();
                    }
                }else{
                    this.stage = 0;
                    this.jobTasks.clear();
                }
                this.sugarcane = 0;
                List<IInventory> iterator = this.findJobChests(5);
                for (IInventory inv : iterator) {
                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                        ItemStack slot = inv.getStackInSlot(i);
                        if (slot != null) {
                            if (slot.isItemEqual(new ItemStack(Items.REEDS))) {
                                this.sugarcane += slot.stackSize;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobSugar-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
