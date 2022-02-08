package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.IInvSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * ========================================
 *
 * @ClassName Schematic
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:32
 * ========================================
 **/
public abstract class Schematic {
    public Schematic() {
    }

    public ItemStack useItem(IBuilderContext context, ItemStack req, IInvSlot slot) {
        ItemStack stack = slot.getStackInSlot();
        ItemStack result = stack.copy();
        if (stack.isItemStackDamageable()) {
            if (req.getMetadata() + stack.getMetadata() <= stack.getMaxDurability()) {
                stack.setMetadata(req.getMetadata() + stack.getMetadata());
                result.setMetadata(req.getMetadata());
                req.stackSize = 0;
            }

            if (stack.getMetadata() >= stack.getMaxDurability()) {
                slot.decreaseStackInSlot(1);
            }
        } else if (stack.stackSize >= req.stackSize) {
            result.stackSize = req.stackSize;
            stack.stackSize -= req.stackSize;
            req.stackSize = 0;
        } else {
            req.stackSize -= stack.stackSize;
            stack.stackSize = 0;
        }

        if (stack.stackSize == 0 && stack.getItem().getContainerItem() != null) {
            Item container = stack.getItem().getContainerItem();
            ItemStack newStack = new ItemStack(container);
            slot.setStackInSlot(newStack);
        } else if (stack.stackSize == 0) {
            slot.setStackInSlot((ItemStack)null);
        }

        return result;
    }

    public void rotateLeft(IBuilderContext context) {
    }

    public void translateToBlueprint(Translation transform) {
    }

    public void translateToWorld(Translation transform) {
    }

    public void idsToBlueprint(MappingRegistry registry) {
    }

    public void idsToWorld(MappingRegistry registry) {
    }

    public void initializeFromObjectAt(IBuilderContext context, int x, int y, int z) {
    }

    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
    }

    public void storeRequirements(IBuilderContext context, int x, int y, int z) {
    }

    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
    }

    public int getEnergyRequirement(LinkedList<ItemStack> stacksUsed) {
        int result = 0;
        ItemStack s;
        if (stacksUsed != null) {
            for(Iterator i$ = stacksUsed.iterator(); i$.hasNext(); result += s.stackSize * 240) {
                s = (ItemStack)i$.next();
            }
        }

        return result;
    }

    public LinkedList<ItemStack> getStacksToDisplay(LinkedList<ItemStack> stackConsumed) {
        return stackConsumed;
    }

    public BuildingStage getBuildStage() {
        return BuildingStage.STANDALONE;
    }

    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        return true;
    }

    public boolean doNotBuild() {
        return false;
    }

    public boolean doNotUse() {
        return false;
    }

    public BuildingPermission getBuildingPermission() {
        return BuildingPermission.ALL;
    }

    public void postProcessing(IBuilderContext context, int x, int y, int z) {
    }

    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
    }

    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
    }

    public int buildTime() {
        return 1;
    }
}
