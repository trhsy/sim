package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * ========================================
 *
 * @ClassName SchematicBlock
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:34
 * ========================================
 **/
public class SchematicBlock extends SchematicBlockBase {
    public Block block = null;
    public int meta = 0;
    public BuildingPermission defaultPermission;
    public ItemStack[] storedRequirements;
    private boolean doNotUse;

    public SchematicBlock() {
        this.defaultPermission = BuildingPermission.ALL;
        this.storedRequirements = new ItemStack[0];
        this.doNotUse = false;
    }

    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
        if (this.block != null) {
            if (this.storedRequirements.length != 0) {
                Collections.addAll(requirements, this.storedRequirements);
            } else {
                requirements.add(new ItemStack(this.block, 1, this.meta));
            }
        }

    }

    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        return this.block == context.world().getBlock(x, y, z) && this.meta == context.world().func_72805_g(x, y, z);
    }

    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        super.placeInWorld(context, x, y, z, stacks);
        this.setBlockInWorld(context, x, y, z);
    }

    public void storeRequirements(IBuilderContext context, int x, int y, int z) {
        super.storeRequirements(context, x, y, z);
        if (this.block != null) {
            ArrayList<ItemStack> req = this.block.getDrops(context.world(), x, y, z, context.world().func_72805_g(x, y, z), 0);
            if (req != null) {
                this.storedRequirements = new ItemStack[req.size()];
                req.toArray(this.storedRequirements);
            }
        }

    }

    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.writeSchematicToNBT(nbt, registry);
        this.writeBlockToNBT(nbt, registry);
        this.writeRequirementsToNBT(nbt, registry);
    }

    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.readSchematicFromNBT(nbt, registry);
        this.readBlockFromNBT(nbt, registry);
        if (!this.doNotUse()) {
            this.readRequirementsFromNBT(nbt, registry);
        }

    }

    public BuildingStage getBuildStage() {
        if (this.block instanceof BlockFalling) {
            return BuildingStage.SUPPORTED;
        } else if (!(this.block instanceof BlockFluidBase) && !(this.block instanceof BlockLiquid)) {
            return this.block.func_149662_c() ? BuildingStage.STANDALONE : BuildingStage.SUPPORTED;
        } else {
            return BuildingStage.EXPANDING;
        }
    }

    public BuildingPermission getBuildingPermission() {
        return this.defaultPermission;
    }

    protected void setBlockInWorld(IBuilderContext context, int x, int y, int z) {
        context.world().setBlock(x, y, z, this.block, this.meta, 3);
        context.world().func_72921_c(x, y, z, this.meta, 3);
    }

    public boolean doNotUse() {
        return this.doNotUse;
    }

    protected void readBlockFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        try {
            this.block = registry.getBlockForId(nbt.func_74762_e("blockId"));
            this.meta = nbt.func_74762_e("blockMeta");
        } catch (MappingNotFoundException var4) {
            this.doNotUse = true;
        }

    }

    protected void readRequirementsFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        if (nbt.func_74764_b("rq")) {
            NBTTagList rq = nbt.func_150295_c("rq", 10);
            ArrayList<ItemStack> rqs = new ArrayList();

            for(int i = 0; i < rq.func_74745_c(); ++i) {
                try {
                    NBTTagCompound sub = rq.func_150305_b(i);
                    if (sub.func_74762_e("id") >= 0) {
                        registry.stackToWorld(sub);
                        rqs.add(ItemStack.func_77949_a(sub));
                    } else {
                        this.defaultPermission = BuildingPermission.CREATIVE_ONLY;
                    }
                } catch (MappingNotFoundException var7) {
                    this.defaultPermission = BuildingPermission.CREATIVE_ONLY;
                } catch (Throwable var8) {
                    var8.printStackTrace();
                    this.defaultPermission = BuildingPermission.CREATIVE_ONLY;
                }
            }

            this.storedRequirements = (ItemStack[])rqs.toArray(new ItemStack[rqs.size()]);
        } else {
            this.storedRequirements = new ItemStack[0];
        }

    }

    protected void writeBlockToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        nbt.func_74768_a("blockId", registry.getIdForBlock(this.block));
        nbt.func_74768_a("blockMeta", this.meta);
    }

    protected void writeRequirementsToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        if (this.storedRequirements.length > 0) {
            NBTTagList rq = new NBTTagList();
            ItemStack[] arr$ = this.storedRequirements;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                ItemStack stack = arr$[i$];
                NBTTagCompound sub = new NBTTagCompound();
                stack.func_77955_b(sub);
                registry.stackToRegistry(sub);
                rq.func_74742_a(sub);
            }

            nbt.func_74782_a("rq", rq);
        }

    }
}

