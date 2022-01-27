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

    public int hashCode() {
        return (this.index != null ? this.index.hashCode() : 0) * 37 + (this.side != null ? this.side.ordinal() : 0) * 37 + this.localId;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound indexNBT = new NBTTagCompound();
        this.index.writeTo(indexNBT);
        nbt.func_74782_a("index", indexNBT);
        nbt.func_74774_a("side", (byte)this.side.ordinal());
        nbt.func_74768_a("localId", this.localId);
        nbt.func_74778_a("class", this.getClass().getCanonicalName());
    }

    protected void readFromNBT(NBTTagCompound nbt) {
        this.index = new BlockIndex(nbt.func_74775_l("index"));
        this.side = ForgeDirection.values()[nbt.func_74771_c("side")];
        this.localId = nbt.func_74762_e("localId");
    }

    public static ResourceId load(NBTTagCompound nbt) {
        try {
            Class clas = Class.forName(nbt.func_74779_i("class"));
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
