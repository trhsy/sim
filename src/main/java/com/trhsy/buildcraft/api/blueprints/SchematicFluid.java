package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;

import java.util.LinkedList;

/**
 * ========================================
 *
 * @ClassName SchematicFluid
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:36
 * ========================================
 **/
public class SchematicFluid extends SchematicBlock {
    private final ItemStack fluidItem;

    public SchematicFluid(FluidStack fluidStack) {
        this.fluidItem = new ItemStack(fluidStack.getFluid().getBlock(), 1);
    }

    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
        if (this.meta == 0) {
            requirements.add(this.fluidItem);
        }

    }

    public void storeRequirements(IBuilderContext context, int x, int y, int z) {
    }

    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        if (this.meta != 0) {
            return this.block == context.world().getBlock(x, y, z);
        } else {
            return this.block == context.world().getBlock(x, y, z) && context.world().func_72805_g(x, y, z) == 0;
        }
    }

    public void rotateLeft(IBuilderContext context) {
    }

    public boolean doNotBuild() {
        return this.meta != 0;
    }

    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        if (this.meta == 0) {
            context.world().setBlock(x, y, z, this.block, 0, 3);
        }

    }

    public void postProcessing(IBuilderContext context, int x, int y, int z) {
        if (this.meta != 0) {
            context.world().setBlock(x, y, z, this.block, this.meta, 3);
        }

    }

    public LinkedList<ItemStack> getStacksToDisplay(LinkedList<ItemStack> stackConsumed) {
        LinkedList<ItemStack> result = new LinkedList();
        result.add(this.fluidItem);
        return result;
    }

    public int getEnergyRequirement(LinkedList<ItemStack> stacksUsed) {
        return 240;
    }
}
