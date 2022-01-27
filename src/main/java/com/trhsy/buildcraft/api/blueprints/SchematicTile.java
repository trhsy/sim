package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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

    public void idsToBlueprint(MappingRegistry registry) {
        registry.scanAndTranslateStacksToRegistry(this.tileNBT);
    }

    public void idsToWorld(MappingRegistry registry) {
        try {
            registry.scanAndTranslateStacksToWorld(this.tileNBT);
        } catch (MappingNotFoundException var3) {
            this.tileNBT = new NBTTagCompound();
        }

    }

    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        super.placeInWorld(context, x, y, z, stacks);
        if (this.block.hasTileEntity(this.meta)) {
            TileEntity tile = context.world().getTileEntity(x, y, z);
            this.tileNBT.func_74768_a("x", x);
            this.tileNBT.func_74768_a("y", y);
            this.tileNBT.func_74768_a("z", z);
            if (tile != null) {
                tile.func_145839_a(this.tileNBT);
            }
        }

    }

    public void initializeFromObjectAt(IBuilderContext context, int x, int y, int z) {
        super.initializeFromObjectAt(context, x, y, z);
        if (this.block.hasTileEntity(this.meta)) {
            TileEntity tile = context.world().getTileEntity(x, y, z);
            if (tile != null) {
                tile.func_145841_b(this.tileNBT);
            }
        }

    }

    public void storeRequirements(IBuilderContext context, int x, int y, int z) {
        super.storeRequirements(context, x, y, z);
        if (this.block.hasTileEntity(this.meta)) {
            TileEntity tile = context.world().getTileEntity(x, y, z);
            if (tile instanceof IInventory) {
                IInventory inv = (IInventory)tile;
                ArrayList<ItemStack> rqs = new ArrayList();

                for(int i = 0; i < inv.func_70302_i_(); ++i) {
                    if (inv.func_70301_a(i) != null) {
                        rqs.add(inv.func_70301_a(i));
                    }
                }

                this.storedRequirements = (ItemStack[])JavaTools.concat(this.storedRequirements, rqs.toArray(new ItemStack[rqs.size()]));
            }
        }

    }

    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.writeSchematicToNBT(nbt, registry);
        nbt.func_74782_a("blockCpt", this.tileNBT);
    }

    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.readSchematicFromNBT(nbt, registry);
        this.tileNBT = nbt.func_74775_l("blockCpt");
    }

    public int buildTime() {
        return 5;
    }
}

