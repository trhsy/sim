package com.trhsy.sim.entity.container;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity
 * @ClassName: ContainerWindmill
 * @Description:
 * @date 2023/08/22 下午 2:46
 */
public class ContainerWindmill extends Container {
    private  IInventory tileFurnace;
    /**
     * 制作时间
     */
    private int cookTime;
    /**
     * 总计制作时间
     */
    private int totalCookTime;
    /**
     * 燃烧时间
     */
    private int furnaceBurnTime;
    /**
     * 当前项目烧录时间
     */
    private int currentItemBurnTime;

    /**
     * @param playerInventory
     * @param furnaceInventory
     */
    public ContainerWindmill(InventoryPlayer playerInventory, IInventory furnaceInventory) {
        try {
            this.tileFurnace = furnaceInventory;
            //将插槽添加到容器里 索引范围0
            this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 30));
            //燃料 风车不用
            //this.addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53));
            //输出栏 索引范围1
            this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 1, 110, 30));

            //三排 一排九个 索引范围是从 9 到 35 （玩家背包）
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    int fs_index=j + i * 9 + 9;
                    System.out.println("风车的id"+fs_index);
                    this.addSlotToContainer(new Slot(playerInventory,fs_index , 8 + j * 18, 74 + i * 18));
                }
            }
            //物品栏一排 九个 索引范围是从 0 到 8
            for (int k = 0; k < 9; ++k) {
                System.out.println("风车的id"+k);
                this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 132));
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("ContainerWindmill出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 添加监听
     *
     * @param listener
     */
    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        try {
            listener.sendAllWindowProperties(this, this.tileFurnace);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("ContainerWindmill-addListener出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    private void sendPropertyIfChanged(IContainerListener listener, int id, int currentValue, int newValue) {
        if (currentValue != newValue) {
            listener.sendWindowProperty(this, id, newValue);
        }
    }
    /**
     * 查找容器中所做的更改，并将其发送给每个侦听器。
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        try {
            for (int i = 0; i < this.listeners.size(); ++i) {
                IContainerListener icrafting = (IContainerListener) this.listeners.get(i);
                /*//制作时间
                if (this.cookTime != this.tileFurnace.getField(2)) {
                    icrafting.sendWindowProperty(this, 2, this.tileFurnace.getField(2));
                }
                //燃烧时间
                if (this.furnaceBurnTime != this.tileFurnace.getField(0)) {
                    icrafting.sendWindowProperty(this, 0, this.tileFurnace.getField(0));
                }
                //当前物品燃烧时间
                if (this.currentItemBurnTime != this.tileFurnace.getField(1)) {
                    icrafting.sendWindowProperty(this, 1, this.tileFurnace.getField(1));
                }
                //总计制作时间
                if (this.totalCookTime != this.tileFurnace.getField(3)) {
                    icrafting.sendWindowProperty(this, 3, this.tileFurnace.getField(3));
                }*/
                sendPropertyIfChanged(icrafting, 2, this.cookTime, this.tileFurnace.getField(2));
                sendPropertyIfChanged(icrafting, 0, this.furnaceBurnTime, this.tileFurnace.getField(0));
                sendPropertyIfChanged(icrafting, 1, this.currentItemBurnTime, this.tileFurnace.getField(1));
                sendPropertyIfChanged(icrafting, 3, this.totalCookTime, this.tileFurnace.getField(3));
            }
            this.cookTime = this.tileFurnace.getField(2);
            this.furnaceBurnTime = this.tileFurnace.getField(0);
            this.currentItemBurnTime = this.tileFurnace.getField(1);
            this.totalCookTime = this.tileFurnace.getField(3);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("ContainerWindmill-detectAndSendChanges出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        try {
            this.tileFurnace.setField(id, data);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("ContainerWindmill-updateProgressBar出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 交互
     * 确定提供的玩家是否可以使用此容器
     * @param playerIn
     * @return
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileFurnace.isUsableByPlayer(playerIn);
    }

    /**
     * 从指定的库存插槽中取出一个堆栈。
     *
     * @param playerIn
     * @param index
     * @return
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        try {
            if (index < 0 || index >= this.inventorySlots.size()) {
                return null;
            }
            //从箱子里取出
            Slot slot = (Slot) this.inventorySlots.get(index);
            //不为空则继续
            if (slot != null && slot.getHasStack()) {
                //获取物品详情
                ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();
                System.out.println("当前操作的物品槽索引: " + index); // 打印索引值
                //输出插槽
                if (index == 1) {
                    if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onSlotChange(itemstack1, itemstack);
                    //不是输入插槽
                } else if ( index != 0) {
                    //熔炉配方
                    if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null) {
                        if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                            return ItemStack.EMPTY;
                        }
                        //背包
                    } else if (index >= 2 && index < 29) {
                        if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                            return ItemStack.EMPTY;
                        }
                        //物品栏
                    } else if (index >= 29 && index < 38) {
                        if(!this.mergeItemStack(itemstack1, 2, 29, false)){
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();//插槽已更改
                }

                if (itemstack1.getCount() == itemstack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTake(playerIn, itemstack1);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("ContainerWindmill-transferStackInSlot出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return itemstack;
    }
}