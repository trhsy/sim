package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.job.Job;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobTaskProduceItem
 * @Description todo 制作任务
 * @Author TRHSY
 * @Date 2022/11/1521:15
 **/
public class JobTaskProduceItem extends JobTask {
    Item produce;
    List<ItemStack> requirements;
    transient long timeToPlace = 0L;
    String status;
    /**
     * @Author fan
     * @Description //TODO
     * @Date 21:20 2022/11/16
     * @Param [j, ms, prod, req, stage, status]工作，耗时，成品，消耗品，状态字段
     * @return
     **/
    public JobTaskProduceItem(Job j, long ms, Item prod, List<ItemStack> req, String status) {
        super(j, ms);
        this.produce = prod;
        this.requirements = req;
        this.status = status;
    }

    public JobTaskProduceItem(Job j, long ms, Item prod, ItemStack req, String status) {
        super(j, ms);
        this.produce = prod;
        this.requirements = new ArrayList();
        this.requirements.add(req);
        this.status = status;
    }
    @Override
    public void onTaskBegin() {
        //设置固定不动
        this.folk.stayPut = true;
    }
    @Override
    public void onUpdate() {
        //在工作期间
        if (this.job.folk.isAtLocation(this.job.workPlace)) {
            //设置状态
            this.folk.setStatus(this.status);

            if (this.timeToPlace == 0L) {
                this.timeToPlace = System.currentTimeMillis();
                return;
            }

            if (System.currentTimeMillis() - this.timeToPlace < 10000L) {
                return;
            }
            //箱子
            List<IInventory> invs = this.job.inventoriesFindClosest(this.job.workPlace, 5);
            //最大产量
            int maxProd = -1;;
            int i;
            int j;
            boolean falg=false;
            for (int k = 0; k < invs.size(); k++) {
                //获取箱子
                IInventory iInventory=invs.get(k);
                for(i = 0; i < iInventory.getSizeInventory(); ++i) {
                    //当前格子的物品
                    ItemStack itemStack= iInventory.getStackInSlot(i);

                    for(j = 0; j < this.requirements.size(); ++j) {
                        //需求的物品
                        ItemStack itemStacks=this.requirements.get(j);
                        if (itemStack!=null && itemStack.isItemEqual(itemStacks)) {
                            if(itemStack.getCount()>=itemStacks.getCount()){
                                ItemStack newItemStacks=itemStack;
                                iInventory.removeStackFromSlot(i);
                                //NPC拿走所需物品
                                this.folk.addToInventory(newItemStacks);
                                falg=true;
                                break;
                            }
                        }
                    }
                    if(falg){
                        break;
                    }
                }
                if(falg){
                    break;
                }
            }
            maxProd = -1;

            //循环NPC的物品
            for(i = 0; i < this.folk.inventory.size(); ++i) {
                //当前NPC的物品
                ItemStack itemStack=this.folk.inventory.get(i);
                //需求物品循环
                for(j = 0; j < this.requirements.size(); ++j) {
                    //需求物品
                    ItemStack requirementsStack=this.requirements.get(j);
                    if (itemStack.isItemEqual(requirementsStack)) {

                        int div = Math.floorDiv(itemStack.getCount(), requirementsStack.getCount());
                        if (maxProd == -1 || maxProd > div) {
                            maxProd = div;
                        }
                        this.folk.inventory.remove(i);
                    }
                }
            }
            ModSimLoader.log.info("制作 " + maxProd + " " + this.produce.getUnlocalizedName());
            if (maxProd > 0) {
                //箱子里放入制作的物品
                this.job.placeInJobChest(new ItemStack(this.produce, maxProd));
                ModSimLoader.addMoney(-0.2F * (float)maxProd);
            }
            //完成任务
            this.completeTask();
        } else {
            //返回工作岗位
            this.folk.setStatus(new TextComponentTranslation("container.sim.job_task_Returning_to_work",new Object[0]).getUnformattedText());
            if (!this.job.folk.entity.isMoving() && this.job.folk.entity.getNavigator().getPath() == null) {
                if(!this.folk.forceMoveToXYZ(this.job.workPlace)){
                    this.folk.forceMoveToXYZNoWarp(this.job.workPlace);
                }
            }
        }

    }
    @Override
    public void onTaskComplete() {
    }
}