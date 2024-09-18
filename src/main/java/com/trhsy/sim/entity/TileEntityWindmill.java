package com.trhsy.sim.entity;

import com.trhsy.sim.block.BlockWindmill;
import com.trhsy.sim.entity.container.ContainerWindmill;
import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity
 * @ClassName: TileEntityWindmill
 * @Description:
 * @date 2023/08/21 上午 9:22
 */
public class TileEntityWindmill extends TileEntityLockable implements ITickable, ISidedInventory {
    //自定义名称
    private String windmillCustomName;
    /**
     * 存放风车中当前使用的物品的ItemStack
     */
    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    /**
     * 当前正在燃烧的物品的新副本将使熔炉持续燃烧的勾号数
     */
    private int currentItemBurnTime;
    /**
     * 制作时间
     */
    private int cookTime;
    /**
     * 总计制作时间
     */
    private int totalCookTime;
    /**
     * 炉子将继续燃烧的节拍数
     */
    private int furnaceBurnTime;
    /**
     * 插槽左
     */
    private static final int[] SLOTS_LEFT = new int[]{0};
    /**
     * 插槽右
     */
    private static final int[] SLOTS_RIGHT = new int[]{1};

    /**
     * 返回资源清册中的插槽数。
     *
     * @return
     */
    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.furnaceItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 返回给定插槽中的堆栈。
     *
     * @param index
     * @return
     */
    @Override
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks.get(index);
    }

    /**
     * 从库存槽中最多删除指定数量的项目，并将它们返回到新堆栈中。
     *
     * @param index
     * @param count
     * @return
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }

    /**
     * 从给定插槽中删除堆栈并返回它。
     *
     * @param index
     * @return
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }

    /**
     * 将给定的物品堆叠设置为库存中的指定插槽（可以是手工制作或盔甲部分）。
     *
     * @param index
     * @param stack
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        try {
            ItemStack itemstack = (ItemStack) this.furnaceItemStacks.get(index);
            boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
            this.furnaceItemStacks.set(index, stack);

            if (stack != null && stack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }
            //是零并且
            if (index == 0 && !flag) {
                this.totalCookTime = this.getCookTime(stack);
                this.cookTime = 0;
                this.markDirty();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("setInventorySlotContents出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 如果此事物已命名，则返回true
     *
     * @return
     */
    @Override
    public String getName() {
        return this.hasCustomName() ? this.windmillCustomName : "tile.windmill.name";
    }

    /**
     * 自定义名称
     *
     * @return
     */
    @Override
    public boolean hasCustomName() {
        return this.windmillCustomName != null && !this.windmillCustomName.isEmpty();
    }

    /**
     * 设置自定义库存名称
     *
     * @param customName
     */
    public void setCustomInventoryName(String customName) {
        this.windmillCustomName = customName;
    }

    public static void registerFixesFurnace(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityWindmill.class, new String[]{"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = 200;

        if (compound.hasKey("CustomName", 8)) {
            this.windmillCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short) this.furnaceBurnTime);
        compound.setInteger("CookTime", (short) this.cookTime);
        compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.windmillCustomName);
        }
        return compound;
    }

    /**
     * 返回库存插槽的最大堆栈大小。似乎总是64，可能会被延长。
     *
     * @return
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * 是否在运行
     *
     * @return
     */
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory) {
        return inventory.getField(0) > 0;
    }

    /**
     * 与旧的updateEntity（）类似，只是更通用。
     */
    @Override
    public void update() {
        try {
            //是否运行
            boolean flag = this.isBurning();
            boolean flag1 = false;
            //运行则减时间
            if (this.isBurning()) {
                --this.furnaceBurnTime;
            }
            //客户端
            if (!this.world.isRemote) {
                //是在运行 并且第一个框里有物品
                if (this.isBurning() || this.furnaceItemStacks.get(1) != null) {
                    //没有运行，
                    if (!this.isBurning() && this.canSmelt()) {
                        //燃烧时间
                        this.currentItemBurnTime = this.furnaceBurnTime = 200;
                        //是否在运行
                        if (this.isBurning()) {
                            flag1 = true;
                        }
                    }
                    //在运行
                    if (this.isBurning() && this.canSmelt()) {
                        ++this.cookTime;
                        //制作时间        总制作时间     重制时间
                        if (this.cookTime == this.totalCookTime) {
                            this.cookTime = 0;
                            this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
                            this.smeltItem();
                            flag1 = true;
                        }
                    } else {
                        this.cookTime = 0;
                    }
                } else if (!this.isBurning() && this.cookTime > 0) {
                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
                }
                //重制风车
                if (flag != this.isBurning()) {
                    flag1 = true;
                    BlockWindmill.setState(this.isBurning(), this.world, this.pos);
                }
            }
            if (flag1) {
                //对于tile实体，确保包含tile实体的区块稍后保存到磁盘上——游戏不会认为它没有更改并跳过它。
                //标记
                this.markDirty();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("update-setInventorySlotContents出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 总制作时间
     *
     * @param stack
     * @return
     */
    public int getCookTime(ItemStack stack) {
        return 200;
    }


    /**
     * 如果熔炉可以熔炼物品，即有来源物品、目标堆栈未满等，则返回true。
     *
     * @return
     */
    private boolean canSmelt() {
        if (this.furnaceItemStacks.get(0) == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks.get(0));
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.furnaceItemStacks.get(1);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())  // Forge fix: make furnace respect stack sizes in furnace recipes
                {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }

    /**
     * 将炉源堆栈中的一个项目转换为炉结果堆栈中的适当冶炼项目
     * 冶炼
     */
    public void smeltItem() {
        try {
            //有燃料
            if (this.canSmelt()) {
                //第一个是金铁铜锡
                ItemStack itemstack = this.furnaceItemStacks.get(0);
                ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
                ItemStack itemstack2 = this.furnaceItemStacks.get(1);
                if (itemstack2.isEmpty())
                {
                    this.furnaceItemStacks.set(1, itemstack1.copy());
                }
                else if (itemstack2.getItem() == itemstack1.getItem())
                {
                    itemstack2.grow(itemstack1.getCount());
                }

                //if (this.furnaceItemStacks.get(1) == null) {
                //    this.furnaceItemStacks.get(1) = itemstack.copy();
                //} else if (this.furnaceItemStacks.get(1).getItem() == itemstack.getItem()) {
                //    this.furnaceItemStacks.get(1).stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
                //}
                //铜矿
                if (itemstack.getItem() == Item.getItemFromBlock(BlockLoader.blockCopperOre)) {
                    if (this.furnaceItemStacks.get(1) != null) {
                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 9);
                    } else {
                        //铜粒儿
                        this.furnaceItemStacks.set(1, new ItemStack(ItemLoader.itemGranulesCopper));
                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 8);
                    }
                }
                //锡矿
                if (itemstack.getItem() == Item.getItemFromBlock(BlockLoader.blockTinOre)) {
                    if (this.furnaceItemStacks.get(1) != null) {
                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 9);
                    } else {
                        //锡粒儿
                        this.furnaceItemStacks.set(1, new ItemStack(ItemLoader.itemGranulesTin));
                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 8);
                    }
                }
                //金矿
                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.GOLD_ORE)) {
                    if (this.furnaceItemStacks.get(1) != null) {

                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 9);
                    } else {
                        //金粒儿
                        this.furnaceItemStacks.set(1, new ItemStack(ItemLoader.itemGranulesGold));
                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 8);
                    }

                }
                //铁矿
                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE)) {
                    if (this.furnaceItemStacks.get(1) != null) {
                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 9);
                    } else {
                        //铁粒儿
                        this.furnaceItemStacks.set(1, new ItemStack(ItemLoader.itemGranulesIron));
                        this.furnaceItemStacks.get(1).setCount(this.furnaceItemStacks.get(1).getCount() + 8);
                    }
                }

//库存减少
                itemstack.shrink(1);


            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("smeltItem-setInventorySlotContents出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 不要将此方法命名为canInteractiveWith，因为它与Container冲突
     *
     * @param player
     * @return
     */
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    /**
     * 如果允许自动化将给定堆栈（忽略堆栈大小）插入到给定插槽中，则返回true。
     *
     * @param index
     * @param stack
     * @return
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 1) {
            return false;
        } else if (index != 1) {
            return true;
        }
        return true;
    }

    /**
     * 获取正面的插槽
     *
     * @param side
     * @return
     */
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.UP ? SLOTS_LEFT : SLOTS_RIGHT;
    }

    /**
     * 如果自动化可以从给定的一侧将给定的项插入到给定的插槽中，则返回true。
     * 验证插槽有效与否
     *
     * @param index
     * @param itemStackIn
     * @param direction
     * @return
     */
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * 如果自动化可以从给定的一侧提取给定插槽中的给定项，则返回true。
     * 可以提取项目
     *
     * @param index
     * @param stack
     * @param direction
     * @return
     */
    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        //面向 1
        if (direction == EnumFacing.DOWN && index == 1) {
            //获取物品
            Item item = stack.getItem();
            //不是水桶或者桶
            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getGuiID() {
        return "sim:Windmill";
    }

    /**
     * 创建容器
     *
     * @param playerInventory
     * @param playerIn
     * @return
     */
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerWindmill(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        this.furnaceItemStacks.clear();
    }

    net.minecraftforge.items.IItemHandler handlerLeft = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.UP);
    //net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerRIGHT = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.WEST);

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, EnumFacing facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.UP) {
                return (T) handlerLeft;
            } else {
                return (T) handlerRIGHT;
            }

        }
        return super.getCapability(capability, facing);
    }
}
