package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.api.buildcraft.api.core.BlockIndex;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.*;

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
    public static final BlockIndex[] RELATIVE_INDEXES = new BlockIndex[]{new BlockIndex(0, -1, 0), new BlockIndex(0, 1, 0), new BlockIndex(0, 0, -1), new BlockIndex(0, 0, 1), new BlockIndex(-1, 0, 0), new BlockIndex(1, 0, 0)};
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

    @Override
    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
        if (this.block != null) {
            if (this.storedRequirements.length != 0) {
                Collections.addAll(requirements, this.storedRequirements);
            } else {
                requirements.add(new ItemStack(this.block, 1, this.meta));
            }
        }

    }
    @Override
    public boolean isAlreadyBuilt(IBuilderContext context, int x, int y, int z) {
        return this.block == context.world().getBlock(x, y, z) && this.meta == context.world().getBlockMetadata(x, y, z);
    }
    @Override
    public void placeInWorld(IBuilderContext context, int x, int y, int z, LinkedList<ItemStack> stacks) {
        super.placeInWorld(context, x, y, z, stacks);
        this.setBlockInWorld(context, x, y, z);
    }
    @Override
    public void storeRequirements(IBuilderContext context, int x, int y, int z) {
        super.storeRequirements(context, x, y, z);
        if (this.block != null) {
            ArrayList<ItemStack> req = this.block.getDrops(context.world(), x, y, z, context.world().getBlockMetadata(x, y, z), 0);
            if (req != null) {
                this.storedRequirements = new ItemStack[req.size()];
                req.toArray(this.storedRequirements);
            }
        }

    }
    @Override
    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.writeSchematicToNBT(nbt, registry);
        this.writeBlockToNBT(nbt, registry);
        this.writeRequirementsToNBT(nbt, registry);
    }
    @Override
    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.readSchematicFromNBT(nbt, registry);
        this.readBlockFromNBT(nbt, registry);
        if (!this.doNotUse()) {
            this.readRequirementsFromNBT(nbt, registry);
        }

    }

    public Set<BlockIndex> getPrerequisiteBlocks(IBuilderContext context) {
        Set<BlockIndex> indexes = new HashSet<BlockIndex>();
        if (this.block instanceof BlockFalling) {
            indexes.add(RELATIVE_INDEXES[ForgeDirection.DOWN.ordinal()]);
        }

        return indexes;
    }
    @Override
    public BuildingStage getBuildStage() {
        return !(this.block instanceof BlockFluidBase) && !(this.block instanceof BlockLiquid) ? BuildingStage.STANDALONE : BuildingStage.EXPANDING;
    }
    @Override
    public BuildingPermission getBuildingPermission() {
        return this.defaultPermission;
    }

    protected void setBlockInWorld(IBuilderContext context, int x, int y, int z) {
        context.world().setBlock(x, y, z, this.block, this.meta, 3);
        context.world().setBlockMetadataWithNotify(x, y, z, this.meta, 3);
    }
    @Override
    public boolean doNotUse() {
        return this.doNotUse;
    }

    protected void readBlockFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        try {
            this.block = registry.getBlockForId(nbt.getInteger("blockId"));
            this.meta = nbt.getInteger("blockMeta");
        } catch (MappingNotFoundException var4) {
            this.doNotUse = true;
        }

    }

    protected void readRequirementsFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        if (nbt.hasKey("rq")) {
            NBTTagList rq = nbt.getTagList("rq", 10);
            ArrayList<ItemStack> rqs = new ArrayList();

            for(int i = 0; i < rq.tagCount(); ++i) {
                try {
                    NBTTagCompound sub = rq.getCompoundTagAt(i);
                    if (sub.getInteger("id") >= 0) {
                        registry.stackToWorld(sub);
                        rqs.add(ItemStack.loadItemStackFromNBT(sub));
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
        nbt.setInteger("blockId", registry.getIdForBlock(this.block));
        nbt.setInteger("blockMeta", this.meta);
    }

    protected void writeRequirementsToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        if (this.storedRequirements.length > 0) {
            NBTTagList rq = new NBTTagList();
            ItemStack[] var4 = this.storedRequirements;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                ItemStack stack = var4[var6];
                NBTTagCompound sub = new NBTTagCompound();
                stack.writeToNBT(sub);
                registry.stackToRegistry(sub);
                rq.appendTag(sub);
            }

            nbt.setTag("rq", rq);
        }

    }
}

