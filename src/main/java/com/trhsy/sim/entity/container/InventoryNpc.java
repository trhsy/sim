package com.trhsy.sim.entity.container;

import com.trhsy.sim.entity.EntityNpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import java.util.Arrays;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.container
 * @ClassName: InventoryNpc
 * @Description:
 * @date 2024/3/21 15:42
 */
public class InventoryNpc implements IInventory {

    /** An array of 36 item stacks indicating the main player inventory (including the visible bar). */
    public final NonNullList<ItemStack> mainInventory = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);
    /** An array of 4 item stacks containing the currently worn armor pieces. */
    public final NonNullList<ItemStack> armorInventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    public final NonNullList<ItemStack> offHandInventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private final List<NonNullList<ItemStack>> allInventories;
    /** The index of the currently held item (0-8). */
    public int currentItem;
    /** The player whose inventory this is. */
    public EntityNpc player;
    /** The stack currently held by the mouse cursor */
    private ItemStack itemStack;
    private int timesChanged;

    public InventoryNpc(EntityNpc playerIn){
        this.allInventories = Arrays.<NonNullList<ItemStack>>asList(this.mainInventory, this.armorInventory, this.offHandInventory);
        this.itemStack = ItemStack.EMPTY;
        this.player = playerIn;
    }
    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
