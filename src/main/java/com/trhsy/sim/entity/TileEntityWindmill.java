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
 * @Description: 风车方块的TileEntity类，处理风车的库存、熔炼等逻辑
 * @date 2023/08/21 上午 9:22
 */
public class TileEntityWindmill extends TileEntityLockable implements ITickable, ISidedInventory {
    //自定义名称
    private String windmillCustomName;
    /**
     * 存放风车中当前使用的物品的ItemStack 列表，大小为2
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
     * 插槽左 插槽左，对应输入物品的插槽
     */
    private static final int[] SLOTS_LEFT = new int[]{0};
    /**
     * 插槽右 插槽右，对应输出物品的插槽
     */
    private static final int[] SLOTS_RIGHT = new int[]{1};

    /**
     * 返回资源清册中的插槽数。
     *
     * @return 插槽数
     */
    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.size();
    }

    /**
     * 判断库存是否为空
     *
     * @return 如果库存为空返回true，否则返回false
     */
    @Override
    public boolean isEmpty() {
        // 遍历库存中的每个物品栈
        for (ItemStack itemstack : this.furnaceItemStacks) {
            // 如果有一个物品栈不为空，则库存不为空
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 返回给定插槽中的堆栈
     *
     * @param index 插槽索引
     * @return 对应插槽的物品栈
     */
    @Override
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks.get(index);
    }

    /**
     * 从库存槽中最多删除指定数量的项目，并将它们返回到新堆栈中
     *
     * @param index 插槽索引
     * @param count 要删除的物品数量
     * @return 被删除的物品栈
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }

    /**
     * 从给定插槽中删除堆栈并返回它
     *
     * @param index 插槽索引
     * @return 被删除的物品栈
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }

    /**
     * 将给定的物品堆叠设置为库存中的指定插槽（可以是手工制作或盔甲部分）
     *
     * @param index 插槽索引
     * @param stack 要设置的物品栈
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        try {
            // 获取当前插槽的物品栈
            ItemStack itemstack = (ItemStack) this.furnaceItemStacks.get(index);
            // 判断新物品栈和原物品栈是否相同
            boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
            // 设置新的物品栈
            this.furnaceItemStacks.set(index, stack);
            // 如果物品栈数量超过最大限制，调整为最大限制
            if (stack != null && stack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }
            // 如果是第一个插槽且物品栈不同，重置制作时间
            if (index == 0 && !flag) {
                this.totalCookTime = this.getCookTime(stack);
                this.cookTime = 0;
                // 标记方块状态已更改
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
     * @return 如果有自定义名称返回true，否则返回false
     */
    @Override
    public String getName() {
        return this.hasCustomName() ? this.windmillCustomName : "tile.windmill.name";
    }

    /**
     * 判断是否有自定义名称
     *
     * @return 如果有自定义名称返回true，否则返回false
     */
    @Override
    public boolean hasCustomName() {
        return this.windmillCustomName != null && !this.windmillCustomName.isEmpty();
    }

    /**
     * 设置自定义库存名称
     *
     * @param customName 自定义名称
     */
    public void setCustomInventoryName(String customName) {
        this.windmillCustomName = customName;
    }

    /**
     * 注册数据修复器，用于处理方块实体的数据修复
     *
     * @param fixer 数据修复器
     */
    public static void registerFixesFurnace(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityWindmill.class, new String[]{"Items"}));
    }

    /**
     * 从NBT标签中读取数据，恢复方块实体的状态
     *
     * @param compound NBT标签
     */
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        // 初始化物品栈列表
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        // 从NBT标签中加载物品栈
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        // 读取燃烧时间
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        // 读取制作时间
        this.cookTime = compound.getInteger("CookTime");
        // 读取总计制作时间
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        // 设置当前物品燃烧时间
        this.currentItemBurnTime = 200;
// 如果NBT标签中包含自定义名称，读取并设置
        if (compound.hasKey("CustomName", 8)) {
            this.windmillCustomName = compound.getString("CustomName");
        }
    }

    /**
     * 将方块实体的状态写入NBT标签，用于保存数据
     *
     * @param compound NBT标签
     * @return 写入数据后的NBT标签
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        // 写入燃烧时间
        compound.setInteger("BurnTime", (short) this.furnaceBurnTime);
        // 写入制作时间
        compound.setInteger("CookTime", (short) this.cookTime);
        // 写入总计制作时间
        compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
        // 将物品栈保存到NBT标签中
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);

        // 如果有自定义名称，写入NBT标签
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

    /**
     * 客户端判断风车是否在运行
     *
     * @param inventory 库存对象
     * @return 如果在运行返回true，否则返回false
     */
    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory) {
        return inventory.getField(0) > 0;
    }

    /**
     * 与旧的updateEntity（）类似，只是更通用。 用于更新方块实体的状态
     */
    @Override
    public void update() {
        try {
            // 记录当前是否在运行
            boolean flag = this.isBurning();
            // 标记是否有状态改变
            boolean flag1 = false;
            // 如果在运行，减少燃烧时间
            if (this.isBurning()) {
                --this.furnaceBurnTime;
            }
            //服务端逻辑
            if (!this.world.isRemote) {
                //如果在运行或者输出插槽有物品
                if (this.isBurning() || this.furnaceItemStacks.get(1) != null) {
                    // 如果不在运行且可以熔炼
                    if (!this.isBurning() && this.canSmelt()) {
                        // 设置燃烧时间
                        this.currentItemBurnTime = this.furnaceBurnTime = 200;
                        // 如果开始运行，标记状态改变
                        if (this.isBurning()) {
                            flag1 = true;
                        }
                    }
                    // 如果在运行且可以熔炼
                    if (this.isBurning() && this.canSmelt()) {
                        // 增加制作时间
                        ++this.cookTime;
                        // 如果制作时间达到总计制作时间
                        if (this.cookTime == this.totalCookTime) {
                            // 重置制作时间
                            this.cookTime = 0;
                            // 重新计算总计制作时间
                            this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
                            // 进行熔炼操作
                            this.smeltItem();
                            // 标记状态改变
                            flag1 = true;
                        }
                    } else {
                        // 否则重置制作时间
                        this.cookTime = 0;
                    }
                } else if (!this.isBurning() && this.cookTime > 0) {
                    // 如果不在运行且制作时间大于0，减少制作时间
                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
                }
                // 如果运行状态改变
                if (flag != this.isBurning()) {
                    // 标记状态改变
                    flag1 = true;
                    // 更新风车方块的状态
                    BlockWindmill.setState(this.isBurning(), this.world, this.pos);
                }
            }
            // 如果状态有改变
            if (flag1) {
                // 标记方块状态已更改
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
     * 判断熔炉是否可以熔炼物品，即有来源物品、目标堆栈未满等
     *
     * @return 如果可以熔炼返回true，否则返回false
     */
    private boolean canSmelt() {
        ItemStack inputStack = this.furnaceItemStacks.get(0);
        // 如果输入插槽没有物品，不能熔炼
        if (inputStack.isEmpty()) {
            return false;
        }
        // 获取熔炼结果
        ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(inputStack);
        // 如果没有熔炼结果，不能熔炼
        if (smeltingResult.isEmpty()) {
            return false;
        }
        // 获取输出插槽的物品栈
        ItemStack outputStack = this.furnaceItemStacks.get(1);
        // 如果输出插槽为空，可以熔炼
        if (outputStack.isEmpty()) {
            return true;
        }
            if (!outputStack.isItemEqual(smeltingResult)) {
            // 如果输出插槽的物品和熔炼结果不同，不能熔炼
            return false;
        }
        return outputStack.getCount() + smeltingResult.getCount() <= this.getInventoryStackLimit()
                && outputStack.getCount() + smeltingResult.getCount() <= outputStack.getMaxStackSize();



    }

    /**
     * 将炉源堆栈中的一个项目转换为炉结果堆栈中的适当冶炼项目
     * 冶炼
     */
    public void smeltItem() {
        try {
            // 检查是否可以熔炼
            if (this.canSmelt()) {
                //第一个是 获取输入槽的物品栈 金铁铜锡
                ItemStack inputStack = this.furnaceItemStacks.get(0);
                // 获取熔炼结果
                ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(inputStack);

                if (!smeltingResult.isEmpty()) {
                    // 获取输出槽的物品栈
                    ItemStack outputStack = this.furnaceItemStacks.get(1);

                    if (outputStack.isEmpty()) {
                        // 如果输出槽为空，直接放入熔炼结果
                        this.furnaceItemStacks.set(1, smeltingResult.copy());
                    } else if (outputStack.isItemEqual(smeltingResult)) {
                        // 如果输出槽已有相同物品，增加数量
                        outputStack.grow(smeltingResult.getCount());
                    }
                }
                // 处理特殊矿石情况
                Item inputItem = inputStack.getItem();
                ItemStack outputStack = this.furnaceItemStacks.get(1);
                int additionalCount = 9;
                //铜矿
                if (inputItem == Item.getItemFromBlock(BlockLoader.blockCopperOre)) {
                    updateOutputStack(outputStack, ItemLoader.itemGranulesCopper, additionalCount);
                } else if (inputItem == Item.getItemFromBlock(BlockLoader.blockTinOre)) {
                    updateOutputStack(outputStack, ItemLoader.itemGranulesTin, additionalCount);  //锡矿
                } else if (inputItem == Item.getItemFromBlock(Blocks.GOLD_ORE)) {
                    updateOutputStack(outputStack, ItemLoader.itemGranulesGold, additionalCount);//金矿
                } else if (inputItem == Item.getItemFromBlock(Blocks.IRON_ORE)) {
                    updateOutputStack(outputStack, ItemLoader.itemGranulesIron, additionalCount);//铁矿
                }

                // 减少输入槽物品数量
                inputStack.shrink(1);

                /*
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
                itemstack.shrink(1);*/


            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("smeltItem-setInventorySlotContents出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 更新输出槽的物品栈
     *
     * @param outputStack     输出槽的物品栈
     * @param item            要添加的物品
     * @param additionalCount 要增加的数量
     */
    private void updateOutputStack(ItemStack outputStack, Item item, int additionalCount) {
        if (!outputStack.isEmpty() && outputStack.getItem() == item) {
            outputStack.grow(additionalCount);
        } else {
            ItemStack newStack = new ItemStack(item);
            newStack.setCount(additionalCount - 1);
            this.furnaceItemStacks.set(1, newStack);
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
