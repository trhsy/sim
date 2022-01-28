package com.trhsy.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.BlockIndex;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName ResourceId
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:48
 * ========================================
 **/
public abstract class ResourceId {
    public BlockIndex index = new BlockIndex();
    public ForgeDirection side;
    public int localId;

    protected ResourceId() {
        this.side = ForgeDirection.UNKNOWN;
        this.localId = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        } else {
            ResourceId compareId = (ResourceId)obj;
            return this.index.equals(compareId.index) && this.side == compareId.side && this.localId == compareId.localId;
        }
    }

    @Override
    public int hashCode() {
        return (this.index != null ? this.index.hashCode() : 0) * 37 + (this.side != null ? this.side.ordinal() : 0) * 37 + this.localId;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound indexNBT = new NBTTagCompound();
        this.index.writeTo(indexNBT);
        nbt.setTag("index", indexNBT);
        nbt.setByte("side", (byte)this.side.ordinal());
        nbt.setInteger("localId", this.localId);
        nbt.setString("class", this.getClass().getCanonicalName());
    }

    protected void readFromNBT(NBTTagCompound nbt) {
        this.index = new BlockIndex(nbt.getCompoundTag("index"));
        this.side = ForgeDirection.values()[nbt.getByte("side")];
        this.localId = nbt.getInteger("localId");
    }

    public static ResourceId load(NBTTagCompound nbt) {
        try {
            Class clas = Class.forName(nbt.getString("class"));
            ResourceId id = (ResourceId)clas.newInstance();
            id.readFromNBT(nbt);
            return id;
        } catch (Throwable var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public void taken(long robotId) {
    }

    public void released(long robotId) {
    }
}
