package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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

    @Override
    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
        if (this.meta == 0) {
            requirements.add(this.fluidItem);
        }

    }

    @Override
    public void storeRequirements(IBuilderContext context, int x, int y, int z) {
    }

    @Override
    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        if (this.meta != 0) {
            return this.block == context.world().getBlock(x, y, z);
        } else {
            return this.block == context.world().getBlock(x, y, z) && context.world().getBlockMetadata(x, y, z) == 0;
        }
    }

    @Override
    public void rotateLeft(IBuilderContext context) {
    }

    @Override
    public boolean doNotBuild() {
        return this.meta != 0;
    }

    @Override
    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        if (this.meta == 0) {
            context.world().setBlock(x, y, z, this.block, 0, 3);
        }

    }

    @Override
    public void postProcessing(IBuilderContext context, int x, int y, int z) {
        if (this.meta != 0) {
            context.world().setBlock(x, y, z, this.block, this.meta, 3);
        }

    }

    @Override
    public LinkedList<ItemStack> getStacksToDisplay(LinkedList<ItemStack> stackConsumed) {
        LinkedList<ItemStack> result = new LinkedList();
        result.add(this.fluidItem);
        return result;
    }

    @Override
    public int getEnergyRequirement(LinkedList<ItemStack> stacksUsed) {
        return 240;
    }
}
