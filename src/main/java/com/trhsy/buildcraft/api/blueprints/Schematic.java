package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

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
        ItemStack result = stack.func_77946_l();
        if (stack.func_77984_f()) {
            if (req.func_77960_j() + stack.func_77960_j() <= stack.func_77958_k()) {
                stack.func_77964_b(req.func_77960_j() + stack.func_77960_j());
                result.func_77964_b(req.func_77960_j());
                req.field_77994_a = 0;
            }

            if (stack.func_77960_j() >= stack.func_77958_k()) {
                slot.decreaseStackInSlot(1);
            }
        } else if (stack.field_77994_a >= req.field_77994_a) {
            result.field_77994_a = req.field_77994_a;
            stack.field_77994_a -= req.field_77994_a;
            req.field_77994_a = 0;
        } else {
            req.field_77994_a -= stack.field_77994_a;
            stack.field_77994_a = 0;
        }

        if (stack.field_77994_a == 0 && stack.func_77973_b().func_77668_q() != null) {
            Item container = stack.func_77973_b().func_77668_q();
            ItemStack newStack = new ItemStack(container);
            slot.setStackInSlot(newStack);
        } else if (stack.field_77994_a == 0) {
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
            for(Iterator i$ = stacksUsed.iterator(); i$.hasNext(); result += s.field_77994_a * 240) {
                s = (ItemStack)i$.next();
            }
        }

        return result;
    }

    public LinkedList<ItemStack> getStacksToDisplay(LinkedList<ItemStack> stackConsumed) {
        return stackConsumed;
    }

    public Schematic.BuildingStage getBuildStage() {
        return Schematic.BuildingStage.STANDALONE;
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
