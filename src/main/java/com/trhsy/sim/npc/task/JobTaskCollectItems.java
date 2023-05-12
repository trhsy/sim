package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName JobTaskCollectItems
 * @Description todo 收集任务
 * @Author TRHSY
 * @Date 2022/11/1520:19
 **/
public class JobTaskCollectItems extends JobTask {
    /**
     * 要收集的物品
     **/
    public List<ItemStack> collectionItems = new ArrayList();
    /**
     * 目的地
     **/
    public List<V3> destinations = new ArrayList();
    /**
     * 当前目的地的
     **/
    V3 currentDestination = null;
    /**
     * 正在转到目的地
     */
    boolean isGoingToDestination = false;
    /**
     * 到达后的时间
     **/
    transient long timeSinceArrival;

    /**
     * 要收集的物品
     *
     * @param j
     * @param ms
     * @param collectionItems
     */
    public JobTaskCollectItems(Job j, int ms, List<ItemStack> collectionItems) {
        super(j, (long) ms);
        this.collectionItems = collectionItems;
    }

    /**
     * 开始任务，任务分配
     */
    @Override
    public void onTaskBegin() {
        //找不到任何可收集的建筑物
        if (this.collectionItems.size() < 1) {
            this.failTask(this.job.folk.getName() + " (" + this.job.jobName + ") " + I18n.format("container.sim.job_task_could"));
        } else {

            for (int i = 0; i < this.collectionItems.size(); ++i) {
                Item colItem = ((ItemStack) this.collectionItems.get(i)).getItem();
                //猪排
                if (colItem == Items.PORKCHOP) {
                    //养猪场
                    this.addDestination(I18n.format("container.sim.Vocation13"));
                    //兔肉
                } else if (colItem == Items.RABBIT) {
                    //养兔场
                    this.addDestination(I18n.format("container.sim.Vocation29"));
                    //牛肉
                } else if (colItem == Items.BEEF) {
                    //养牛场
                    this.addDestination(I18n.format("container.sim.Vocation12"));
                    //鸡肉
                } else if (colItem == Items.CHICKEN) {
                    //养鸡场
                    this.addDestination(I18n.format("container.sim.Vocation14"));
                    //羊肉
                } else if (colItem == Items.MUTTON) {
                    //养羊场
                    this.addDestination(I18n.format("container.sim.Vocation28"));
                    //鸡蛋
                } else if (colItem == Items.EGG) {
                    //鸡蛋农场
                    this.addDestination(I18n.format("container.sim.Vocation3"));
                    //牛奶
                } else if (colItem == Items.MILK_BUCKET) {
                    //奶牛场
                    this.addDestination(I18n.format("container.sim.Vocation20"));
                    //鱼
                } else if (colItem == Items.FISH) {
                    //渔场
                    this.addDestination(I18n.format("container.sim.Vocation18"));
                    //胡萝卜
                } else if (colItem == Items.CARROT) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType1"));
                    //马铃薯
                } else if (colItem == Items.POTATO) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType3"));
                    //甜菜根
                } else if (colItem == Items.BEETROOT) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType6"));
                    //可可豆
                } else if (colItem == Items.DYE) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType10"));
                    //西瓜
                } else if (colItem == Items.MELON) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType2"));
                    //小麦
                } else if (colItem == Items.WHEAT) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType5"));
                    //甘蔗
                } else if (colItem == Items.REEDS) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType7"));
                    //南瓜
                } else if (colItem == new ItemStack(Blocks.PUMPKIN).getItem()) {
                    this.addDestination("farmer:" + I18n.format("container.sim.FarmType4"));
                    //糖
                }else if (colItem == Items.SUGAR) {
                    this.addDestination( I18n.format("container.sim.Vocation30"));
                }

            }

            if (this.destinations.size() < 1) {
                //找不到任何可收集的建筑物
                this.failTask(this.job.folk.getName() + " (" + this.job.jobName + ") " + I18n.format("container.sim.job_task_could"));
            } else {
                this.currentDestination = this.destinations.get(0);
            }
        }
    }

    /**
     * 添加目的地
     *
     * @param jobt
     */
    private void addDestination(String jobt) {
        //如果是农场作物
        if (jobt.contains("farmer:")) {
            String fType = jobt.split(":")[1];
            //获取最近的农场
            ModSimLoader.getClosestFarm(this.job.workPlace, fType).forEach((f) -> {
                System.out.println("最近的农场：" + f.farmType);
                this.destinations.add(f.loc);

            });
        } else {
            List<Building> buildings = ModSimLoader.getClosestBuildingByJob(jobt, this.job.workPlace);
            if (buildings.size() > 0) {
                this.destinations.add((buildings.get(0)).controlXYZ);
            }

        }
    }

    @Override
    public void onUpdate() {
        //目的地不为空
        if (this.currentDestination != null) {
            //如果在建筑内
            if (!this.job.folk.isAtLocation(this.currentDestination)) {
                //收集材料
                this.folk.setStatus(I18n.format("container.sim.job_task_Collecting_materials"));
                if (!this.job.folk.entity.isMoving()) {
                    this.job.folk.forceMoveToXYZ(this.currentDestination);
                }

                this.isGoingToDestination = true;
            } else {
                //到达后的时间
                if (this.timeSinceArrival == 0L) {
                    this.timeSinceArrival = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - this.timeSinceArrival > 5000L) {
                    ModSimLoader.log.info("开始收集");
                    //寻找附近的箱子
                    List<IInventory> chests = this.job.inventoriesFindClosest(this.currentDestination, 5);
                    for (IInventory inv : chests) {
                        for (int i = 0; i < inv.getSizeInventory(); i++) {
                            ItemStack invItemStack = inv.getStackInSlot(i);

                            if (invItemStack != null && this.collectionItems.contains(invItemStack)) {
                                for (int j = 0; j <this.collectionItems.size() ; j++) {
                                    ItemStack collectionItemStack=this.collectionItems.get(j);
                                    if(invItemStack.isItemEqual(collectionItemStack)){
                                        if (invItemStack.stackSize < collectionItemStack.stackSize) {
                                            this.job.folk.inventory.add(invItemStack);
                                            inv.removeStackFromSlot(i);
                                        } else {
                                            this.job.folk.inventory.add(collectionItemStack);
                                            inv.decrStackSize(i, (this.collectionItems.get(j)).stackSize);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (this.destinations.size() > 1) {
                        ModSimLoader.log.info("找到下一个目的地");
                        int fsi = this.destinations.indexOf(this.currentDestination);
                        if (fsi < this.destinations.size()) {
                            this.currentDestination = (V3) this.destinations.get(+1);
                            this.destinations.remove(this.destinations.indexOf(this.currentDestination));
                        } else {
                            this.completeTask();
                        }
                    } else {
                        ModSimLoader.log.info("从所有目的地收集");
                        this.completeTask();
                    }
                }
            }
        } else {
            this.currentDestination = (V3) this.destinations.get(0);
        }

    }

    @Override
    public void onTaskComplete() {
        this.destinations.clear();
    }
}
