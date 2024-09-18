package com.trhsy.sim.entity.container;

import com.trhsy.sim.entity.EntityNpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.container
 * @ClassName: SlotNpcCrafting
 * @Description:
 * @date 2024/3/21 16:17
 */
public class SlotNpcCrafting extends Slot {
    /** The player that is using the GUI where this slot resides. */
    private final EntityNpc player;
    /** The number of items that have been crafted so far. Gets passed to ItemStack.onCrafting before being reset. */
    private int amountCrafted;

    public SlotNpcCrafting(EntityNpc player, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    @Override
    public ItemStack decrStackSize(int amount)
    {
        if (this.getHasStack())
        {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    @Override
    protected void onCrafting(ItemStack stack, int amount)
    {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }
    @Override
    protected void onSwapCraft(int p_190900_1_)
    {
        this.amountCrafted += p_190900_1_;
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
    protected void onCrafting(ItemStack stack)
    {
        if (this.amountCrafted > 0)
        {
//            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
//            net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(this.player, stack, craftMatrix);
        }

        this.amountCrafted = 0;
        InventoryCraftResult inventorycraftresult = (InventoryCraftResult)this.inventory;
        IRecipe irecipe = inventorycraftresult.getRecipeUsed();

        if (irecipe != null && !irecipe.isDynamic())
        {
//            this.player.unlockRecipes(Lists.newArrayList(irecipe));
            inventorycraftresult.setRecipeUsed((IRecipe)null);
        }
    }
    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
//        NonNullList<ItemStack> nonnulllist = CraftingManager.getRemainingItems(this.craftMatrix, thePlayer.world);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
/*
        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack1 = nonnulllist.get(i);

            if (!itemstack.isEmpty())
            {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack = this.craftMatrix.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty())
            {
                if (itemstack.isEmpty())
                {
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                }
                else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1))
                {
                    itemstack1.grow(itemstack.getCount());
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                }
                else if (!this.player.inventory.addItemStackToInventory(itemstack1))
                {
                    this.player.dropItem(itemstack1, false);
                }
            }
        }
*/
        return stack;
    }
}
