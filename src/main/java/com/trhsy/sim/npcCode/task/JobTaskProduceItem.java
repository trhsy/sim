package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.V3;
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

            boolean flag =false;
            for (IInventory inventory:invs){
                // 遍历箱子中的每个物品槽位
                for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
                    ItemStack stackInSlot = inventory.getStackInSlot(slot);

                    // 更安全的空检查
                    if (stackInSlot.isEmpty()) {
                        continue;
                    }

                    // 遍历NPC的需求列表
                    for (ItemStack requiredStack : this.requirements) {
                        // 更全面的物品匹配逻辑（考虑元数据和NBT）
                        if (ItemStack.areItemsEqual(stackInSlot, requiredStack) &&
                                ItemStack.areItemStackTagsEqual(stackInSlot, requiredStack)) {

                            // 检查数量是否足够
                            if (stackInSlot.getCount() >= requiredStack.getCount()) {
                                // 创建物品副本，避免修改原始需求
                                ItemStack takenStack = stackInSlot.copy();
                                takenStack.setCount(requiredStack.getCount());

                                // 从箱子中移除相应数量的物品
                                stackInSlot.shrink(requiredStack.getCount());

                                // 如果箱子中物品被取空，清空该槽位
                                if (stackInSlot.isEmpty()) {
                                    inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
                                }

                                // 将物品添加到NPC库存
                                this.folk.addToInventory(takenStack);
                                flag = true;
                                break; // 跳出需求列表循环
                            }
                        }
                    }

                    if (flag) {
                        break; // 跳出箱子槽位循环
                    }
                }

                if (flag) {
                    break; // 跳出箱子列表循环
                }
            }
//            for (int k = 0; k < invs.size(); k++) {
//                //获取箱子
//                IInventory iInventory=invs.get(k);
//                for(int i = 0; i < iInventory.getSizeInventory(); ++i) {
//                    //当前格子的物品
//                    ItemStack itemStack= iInventory.getStackInSlot(i);
//
//                    for(int j = 0; j < this.requirements.size(); ++j) {
//                        //需求的物品
//                        ItemStack itemStacks=this.requirements.get(j);
//                        if (!itemStack.isEmpty() && itemStack.getItem().equals(itemStacks.getItem())) {
//                            if(itemStack.getCount()>=itemStacks.getCount()){
//                                ItemStack newItemStacks=itemStack;
//                                iInventory.removeStackFromSlot(i);
//                                //NPC拿走所需物品
//                                this.folk.addToInventory(newItemStacks);
//                                flag =true;
//                                break;
//                            }
//                        }
//                    }
//                    if(flag ){
//                        break;
//                    }
//                }
//                if(flag){
//                    break;
//                }
//            }
            maxProd = -1;
// 创建一个列表记录需要移除的物品索引
            List<Integer> indicesToRemove = new ArrayList<>();
            // 遍历NPC的所有物品（使用安全的倒序遍历）
            for (int i = this.folk.inventory.size() - 1; i >= 0; i--) {
                ItemStack itemStack = this.folk.inventory.get(i);

                // 遍历需求列表
                for (ItemStack requiredStack : this.requirements) {
                    // 更全面的物品匹配（考虑NBT标签）
                    if (ItemStack.areItemsEqual(itemStack, requiredStack) &&
                            ItemStack.areItemStackTagsEqual(itemStack, requiredStack)) {

                        // 计算可生产次数
                        int div = Math.floorDiv(itemStack.getCount(), requiredStack.getCount());

                        // 更新最大生产次数
                        if (maxProd == -1 || maxProd > div) {
                            maxProd = div;
                        }

                        // 标记该物品需要移除（或消耗部分）
                        indicesToRemove.add(i);

                        // 找到匹配后跳出需求循环
                        break;
                    }
                }
            }
            // 安全地移除需要处理的物品
            for (int index : indicesToRemove) {
                this.folk.inventory.remove(index);
            }/*
            //循环NPC的物品
            for(int i = 0; i < this.folk.inventory.size(); ++i) {
                //当前NPC的物品
                ItemStack itemStack=this.folk.inventory.get(i);
                //需求物品循环
                for(int j = 0; j < this.requirements.size(); ++j) {
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
            }*/
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
                V3 v3=this.job.workPlace;

                this.folk.forceMoveToXYZ(v3);
            }
        }

    }
    @Override
    public void onTaskComplete() {
    }
}