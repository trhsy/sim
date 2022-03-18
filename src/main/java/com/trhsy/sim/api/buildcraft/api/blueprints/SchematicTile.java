package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.api.buildcraft.api.core.JavaTools;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * ========================================
 *
 * @ClassName SchematicTile
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:37
 * ========================================
 **/
public class SchematicTile extends SchematicBlock {
    public NBTTagCompound tileNBT = new NBTTagCompound();

    public SchematicTile() {
    }

    @Override
    public void idsToBlueprint(MappingRegistry registry) {
        registry.scanAndTranslateStacksToRegistry(this.tileNBT);
    }

    @Override
    public void idsToWorld(MappingRegistry registry) {
        try {
            registry.scanAndTranslateStacksToWorld(this.tileNBT);
        } catch (MappingNotFoundException var3) {
            this.tileNBT = new NBTTagCompound();
        }

    }

    public void onNBTLoaded() {
    }

    @Override
    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        super.placeInWorld(context, x, y, z, stacks);
        if (this.block.hasTileEntity(this.meta)) {
            this.tileNBT.setInteger("x", x);
            this.tileNBT.setInteger("y", y);
            this.tileNBT.setInteger("z", z);
            context.world().setTileEntity(x, y, z, TileEntity.createAndLoadEntity(this.tileNBT));
        }

    }

    @Override
    public void initializeFromObjectAt(IBuilderContext context, int x, int y, int z) {
        super.initializeFromObjectAt(context, x, y, z);
        if (this.block.hasTileEntity(this.meta)) {
            TileEntity tile = context.world().getTileEntity(x, y, z);
            if (tile != null) {
                tile.writeToNBT(this.tileNBT);
            }

            this.tileNBT = (NBTTagCompound)this.tileNBT.copy();
            this.onNBTLoaded();
        }

    }

    @Override
    public void storeRequirements(IBuilderContext context, int x, int y, int z) {
        super.storeRequirements(context, x, y, z);
        if (this.block.hasTileEntity(this.meta)) {
            TileEntity tile = context.world().getTileEntity(x, y, z);
            if (tile instanceof IInventory) {
                IInventory inv = (IInventory)tile;
                ArrayList<ItemStack> rqs = new ArrayList();

                for(int i = 0; i < inv.getSizeInventory(); ++i) {
                    if (inv.getStackInSlot(i) != null) {
                        rqs.add(inv.getStackInSlot(i));
                    }
                }

                this.storedRequirements = (ItemStack[]) JavaTools.concat(this.storedRequirements, rqs.toArray(new ItemStack[rqs.size()]));
            }
        }

    }

    @Override
    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.writeSchematicToNBT(nbt, registry);
        nbt.setTag("blockCpt", this.tileNBT);
    }

    @Override
    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.readSchematicFromNBT(nbt, registry);
        this.tileNBT = nbt.getCompoundTag("blockCpt");
        this.onNBTLoaded();
    }

    @Override
    public int buildTime() {
        return 5;
    }
}

