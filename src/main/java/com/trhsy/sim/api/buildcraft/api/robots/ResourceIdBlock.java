package com.trhsy.sim.api.buildcraft.api.robots;


import com.trhsy.sim.api.buildcraft.api.core.BlockIndex;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ResourceIdBlock extends ResourceId {
    public BlockIndex index = new BlockIndex();
    public ForgeDirection side;

    public ResourceIdBlock() {
        this.side = ForgeDirection.UNKNOWN;
    }

    public ResourceIdBlock(int x, int y, int z) {
        this.side = ForgeDirection.UNKNOWN;
        this.index = new BlockIndex(x, y, z);
    }

    public ResourceIdBlock(BlockIndex iIndex) {
        this.side = ForgeDirection.UNKNOWN;
        this.index = iIndex;
    }

    public ResourceIdBlock(TileEntity tile) {
        this.side = ForgeDirection.UNKNOWN;
        this.index = new BlockIndex(tile);
    }

    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            ResourceIdBlock compareId = ( ResourceIdBlock)obj;
            return this.index.equals(compareId.index) && this.side == compareId.side;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (new HashCodeBuilder()).append(this.index.hashCode()).append(this.side != null ? this.side.ordinal() : 0).build();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound indexNBT = new NBTTagCompound();
        this.index.writeTo(indexNBT);
        nbt.setTag("index", indexNBT);
        nbt.setByte("side", (byte)this.side.ordinal());
    }

    @Override
    protected void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.index = new BlockIndex(nbt.getCompoundTag("index"));
        this.side = ForgeDirection.values()[nbt.getByte("side")];
    }
}
