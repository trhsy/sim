package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

import java.util.LinkedList;

/**
 * ========================================
 *
 * @ClassName SchematicMask
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:37
 * ========================================
 **/
public class SchematicMask extends SchematicBlockBase {
    public boolean isConcrete = true;

    public SchematicMask() {
    }

    public SchematicMask(boolean isConcrete) {
        this.isConcrete = isConcrete;
    }

    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        if (this.isConcrete) {
            if (stacks.size() == 0 || !BuildCraftAPI.isSoftBlock(context.world(), x, y, z)) {
                return;
            }

            ItemStack stack = (ItemStack)stacks.getFirst();
            context.world().setBlock(x, y, z, Blocks.field_150350_a, 0, 3);
            stack.func_77943_a((EntityPlayer)BuildCraftAPI.proxy.getBuildCraftPlayer((WorldServer)context.world()).get(), context.world(), x, y, z, 1, 0.0F, 0.0F, 0.0F);
        } else {
            context.world().setBlock(x, y, z, Blocks.field_150350_a, 0, 3);
        }

    }

    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        if (this.isConcrete) {
            return !BuildCraftAPI.isSoftBlock(context.world(), x, y, z);
        } else {
            return BuildCraftAPI.isSoftBlock(context.world(), x, y, z);
        }
    }

    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        nbt.func_74757_a("isConcrete", this.isConcrete);
    }

    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        this.isConcrete = nbt.func_74767_n("isConcrete");
    }
}

