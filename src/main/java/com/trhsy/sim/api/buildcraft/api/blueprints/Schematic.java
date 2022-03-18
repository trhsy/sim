package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.api.buildcraft.api.core.IInvSlot;
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

    public boolean isItemMatchingRequirement(ItemStack suppliedStack, ItemStack requiredStack) {
        return BuilderAPI.schematicHelper.isEqualItem(suppliedStack, requiredStack);
    }

    public ItemStack useItem(IBuilderContext context, ItemStack req, IInvSlot slot) {
        ItemStack stack = slot.getStackInSlot();
        ItemStack result = stack.copy();
        if (stack.stackSize >= req.stackSize) {
            result.stackSize = req.stackSize;
            stack.stackSize -= req.stackSize;
            req.stackSize = 0;
        } else {
            req.stackSize -= stack.stackSize;
            stack.stackSize = 0;
        }

        if (stack.stackSize == 0) {
            stack.stackSize = 1;
            if (stack.getItem().hasContainerItem(stack)) {
                ItemStack newStack = stack.getItem().getContainerItem(stack);
                slot.setStackInSlot(newStack);
            } else {
                slot.setStackInSlot((ItemStack)null);
            }
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
            for(Iterator var3 = stacksUsed.iterator(); var3.hasNext(); result += s.stackSize * 240) {
                s = (ItemStack)var3.next();
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
