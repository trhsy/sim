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

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobMcDonald
 * @Description: 麦当劳
 * @date 2023/07/31 下午 3:10
 */
public class JobMcDonald extends Job{
    //熟猪排 熟羊肉 熟兔肉 牛排 熟鸡肉 熟鲑鱼 熟鱼
    private int porkchop, mutton, rabbit, steak,chicken,salmon,fish;
    public JobMcDonald(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.tinSpade);
        //麦当劳
        this.jobName = I18n.format("container.sim.Vocation35");
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
                    //熟猪排
                    colItems.add(new ItemStack(Items.PORKCHOP, 16));
                    //熟羊肉
                    colItems.add(new ItemStack(Items.MUTTON, 16));
                    //熟兔肉
                    colItems.add(new ItemStack(Items.RABBIT, 16));
                    //牛排
                    colItems.add(new ItemStack(Items.BEEF, 16));
                    //牛排
                    colItems.add(new ItemStack(Items.CHICKEN, 16));
                    //鲑鱼
                    colItems.add(new ItemStack(Items.FISH, 16,1));
                    //生鱼
                    colItems.add(new ItemStack(Items.FISH, 16));
                    //收集
                    this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
                    this.stage = 3;
                } else if (this.stage == 3) {
                    List<ItemStack> colItems = new ArrayList();
                    //猪排
                    colItems.add(new ItemStack(Items.PORKCHOP, 16));
                    //羊肉
                    colItems.add(new ItemStack(Items.MUTTON, 16));
                    //兔肉
                    colItems.add(new ItemStack(Items.RABBIT, 16));
                    //牛排
                    colItems.add(new ItemStack(Items.BEEF, 16));
                    //鸡肉
                    colItems.add(new ItemStack(Items.CHICKEN, 16));
                    //鲑鱼
                    colItems.add(new ItemStack(Items.FISH, 16,1));
                    //生鱼
                    colItems.add(new ItemStack(Items.FISH, 16));
                    this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
                    this.stage = 4;
                } else if (this.stage == 4) {

                    if (this.steak >= 1) {
                        //牛肉
                        //熟牛排
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKED_BEEF, new ItemStack(Items.BEEF, 1), I18n.format("container.sim.job.Baker_Baking")));
                    }
                    if (this.porkchop >= 1) {
                        //需要的食材
                        //猪排
                        //熟猪排
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKED_PORKCHOP, new ItemStack(Items.PORKCHOP, 1), I18n.format("container.sim.job.Baker_Baking")));
                    }
                    if (this.mutton > 1) {
                        //烘烤 羊排
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKED_MUTTON, new ItemStack(Items.MUTTON, 1), I18n.format("container.sim.job.Baker_Baking")));
                    }

                    if (this.rabbit > 1) {
                        //烘烤 熟兔肉
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKED_RABBIT, new ItemStack(Items.RABBIT, 1), I18n.format("container.sim.job.Baker_Baking")));
                    }

                    if (this.chicken > 1) {
                        //烘烤 鸡肉
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKED_CHICKEN, new ItemStack(Items.CHICKEN, 1), I18n.format("container.sim.job.Baker_Baking")));
                    }

                    if (this.salmon > 1) {
                        //烘烤 鲑鱼
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, new ItemStack(Items.COOKED_FISH, 1,1).getItem(), new ItemStack(Items.FISH, 16,1), I18n.format("container.sim.job.Baker_Baking")));
                    }

                    if (this.fish > 1) {
                        //烘烤 鲑鱼
                        this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.COOKED_FISH, new ItemStack(Items.FISH, 16), I18n.format("container.sim.job.Baker_Baking")));
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
                    } else if ((this.steak > 1 ||this.porkchop > 1 ||this.mutton > 1||this.rabbit > 1||this.chicken > 1||this.salmon > 1||this.fish > 1) && this.folk.getStatusText().contains(I18n.format("container.sim.job_task_Selling"))) {
                        this.stage = 4;
                        this.currentTask.completeTask();
                        this.jobTasks.clear();
                    }
                }else{
                    this.stage = 0;
                    this.jobTasks.clear();
                }
                this.steak = 0;
                this.porkchop = 0;
                this.mutton = 0;
                this.rabbit = 0;
                this.chicken = 0;
                this.salmon = 0;
                this.fish = 0;
                List<IInventory> iterator = this.findJobChests(5);
                for (IInventory inv : iterator) {
                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                        ItemStack slot = inv.getStackInSlot(i);
                        if (slot != null) {
                            if (slot.isItemEqual(new ItemStack(Items.BEEF))) {
                                this.steak += slot.getCount();
                            } else if (slot.isItemEqual(new ItemStack(Items.PORKCHOP))) {
                                this.porkchop += slot.getCount();
                            } else if (slot.isItemEqual(new ItemStack(Items.MUTTON))) {
                                this.mutton += slot.getCount();
                            } else if (slot.isItemEqual(new ItemStack(Items.RABBIT))) {
                                this.rabbit += slot.getCount();
                            }else if (slot.isItemEqual(new ItemStack(Items.CHICKEN))) {
                                this.chicken += slot.getCount();
                            }else if (slot.isItemEqual(new ItemStack(Items.COOKED_FISH,1,1))) {
                                this.salmon += slot.getCount();
                            }else if (slot.isItemEqual(new ItemStack(Items.COOKED_FISH))) {
                                this.fish += slot.getCount();
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