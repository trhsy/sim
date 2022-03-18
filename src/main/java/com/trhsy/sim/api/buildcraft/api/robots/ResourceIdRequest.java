package com.trhsy.sim.api.buildcraft.api.robots;


import com.trhsy.sim.api.buildcraft.api.core.BlockIndex;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ResourceIdRequest extends ResourceId {
    private BlockIndex index;
    private ForgeDirection side;
    private int slot;

    public ResourceIdRequest() {
    }

    public ResourceIdRequest(DockingStation station, int slot) {
        this.index = station.index();
        this.side = station.side();
        this.slot = slot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            ResourceIdRequest compareId = ( ResourceIdRequest)obj;
            return this.index.equals(compareId.index) && this.side.equals(compareId.side) && this.slot == compareId.slot;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (new HashCodeBuilder()).append(this.index.hashCode()).append(this.side.hashCode()).append(this.slot).build();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound indexNBT = new NBTTagCompound();
        this.index.writeTo(indexNBT);
        nbt.setTag("index", indexNBT);
        nbt.setByte("side", (byte)this.side.ordinal());
        nbt.setInteger("localId", this.slot);
    }

    @Override
    protected void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.index = new BlockIndex(nbt.getCompoundTag("index"));
        this.side = ForgeDirection.getOrientation(nbt.getByte("side"));
        this.slot = nbt.getInteger("localId");
    }
}
