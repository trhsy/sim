package com.trhsy.sim.entity;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
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
    private IInventory tileFurnace;
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
            //将插槽添加到容器里
            this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 30));
            //燃料 风车不用
            //this.addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53));
            this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 1, 110, 30));
            //三排 一排九个
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 74 + i * 18));
                }
            }
            //物品栏一排 九个
            for (int k = 2; k < 11; ++k) {
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
    public void addListener(ICrafting listener) {
        super.addListener(listener);
        try {
            listener.sendAllWindowProperties(this, this.tileFurnace);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("ContainerWindmill-addListener出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                ICrafting icrafting = (ICrafting) this.listeners.get(i);
                if (this.cookTime != this.tileFurnace.getField(2)) {
                    icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.getField(2));
                }

                if (this.furnaceBurnTime != this.tileFurnace.getField(0)) {
                    icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
                }

                if (this.currentItemBurnTime != this.tileFurnace.getField(1)) {
                    icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
                }

                if (this.totalCookTime != this.tileFurnace.getField(3)) {
                    icrafting.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
                }
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
     *
     * @param playerIn
     * @return
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileFurnace.isUseableByPlayer(playerIn);
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

        ItemStack itemstack = null;
        try {
            //从箱子里取出
            Slot slot = (Slot) this.inventorySlots.get(index);
            //不为空则继续
            if (slot != null && slot.getHasStack()) {
                //获取物品详情
                ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();
                //第二个插槽
                if (index == 1) {
                    if (!this.mergeItemStack(itemstack1, 1, 38, true)) {
                        return null;
                    }

                    slot.onSlotChange(itemstack1, itemstack);
                } else if ( index != 0) {
                    //熔炉配方
                    if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null) {
                        if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                            return null;
                        }
                        //背包
                    } else if (index >= 3 && index < 30) {
                        if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                            return null;
                        }
                        //物品栏
                    } else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
                    return null;
                }
                if (itemstack1.stackSize == 0) {
                    slot.putStack((ItemStack) null);
                } else {
                    slot.onSlotChanged();//插槽已更改
                }

//                if (itemstack1.stackSize == itemstack.stackSize) {
//                    return null;
//                }

                slot.onPickupFromSlot(playerIn, itemstack1);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("ContainerWindmill-transferStackInSlot出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return itemstack;
    }
}