package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.BuildCraftAPI;
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

    @Override
    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        if (this.isConcrete) {
            if (stacks.size() == 0 || !BuildCraftAPI.isSoftBlock(context.world(), x, y, z)) {
                return;
            }

            ItemStack stack = (ItemStack)stacks.getFirst();
            context.world().setBlock(x, y, z, Blocks.air, 0, 3);
            stack.tryPlaceItemIntoWorld((EntityPlayer) BuildCraftAPI.proxy.getBuildCraftPlayer((WorldServer)context.world()).get(), context.world(), x, y, z, 1, 0.0F, 0.0F, 0.0F);
        } else {
            context.world().setBlock(x, y, z, Blocks.air, 0, 3);
        }

    }

    @Override
    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        if (this.isConcrete) {
            return !BuildCraftAPI.isSoftBlock(context.world(), x, y, z);
        } else {
            return BuildCraftAPI.isSoftBlock(context.world(), x, y, z);
        }
    }

    @Override
    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        nbt.setBoolean("isConcrete", this.isConcrete);
    }

    @Override
    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        this.isConcrete = nbt.getBoolean("isConcrete");
    }
}

