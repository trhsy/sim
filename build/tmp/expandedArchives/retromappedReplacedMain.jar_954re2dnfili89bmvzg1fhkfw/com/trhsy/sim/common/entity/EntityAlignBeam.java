package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.functionality.Marker;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityAlignBeam extends Entity {
    public float yaw = 0.0F;
    public String caption = "x";
    private Marker theMarker = null;

    public EntityAlignBeam(World par1World) {
        super(par1World);
        this.field_70158_ak = true;
        if (!BlockMarker.hasPlaced) {
            this.func_70106_y();
        }

        this.func_70105_a(0.1F, 100.0F);
    }


    @Override
    public boolean func_70067_L() {
        return false;
    }

    @Override
    public boolean func_70104_M() {
        return false;
    }

    @Override
    public void func_70091_d(double par1, double par3, double par5) {
        this.field_70159_w = 0;
        this.field_70181_x = 0;
        this.field_70179_y = 0;
    }

    @Override
    public void func_70030_z() {
    }

    @Override
    public void func_70016_h(double par1, double par3, double par5) {
        super.func_70016_h(0, 0, 0);
    }

    @Override
    public void func_70071_h_() {
        if (this.caption.contentEquals("x")) {
            this.theMarker = BlockMarker.getMarker(new V3(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_71093_bK));
            if (this.theMarker != null) {
                this.caption = this.theMarker.caption;
            }
        }

        if (this.theMarker != null) {
            this.field_70163_u = (double)this.theMarker.y;
        }

    }

    @Override
    protected void func_70088_a() {
        this.field_70145_X = true;
    }

    //@Override
    //public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    //}
    @Override
    protected void func_70037_a(NBTTagCompound var1) {
    }
    @Override
    protected void func_70014_b(NBTTagCompound var1) {
    }
}
