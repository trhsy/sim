package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.functionality.Marker;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityAlignBeam extends Entity {
    public float yaw = 0.0F;
    public String caption = "x";
    private Marker theMarker = null;

    public EntityAlignBeam(World par1World) {
        super(par1World);
        this.ignoreFrustumCheck = true;
        //标记棒是否已放置
        if (!BlockMarker.hasPlaced) {
            //放置后摧毁
            this.setDead();
        }

        this.setSize(0.1F, 100.0F);
    }


    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void moveEntity(double par1, double par3, double par5) {
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
    }

    @Override
    public void onEntityUpdate() {
    }

    @Override
    public void setVelocity(double par1, double par3, double par5) {
        try {
            super.setVelocity(0, 0, 0);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setVelocity出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void onUpdate() {
        try {
            if (this.caption.contentEquals("x")) {
                this.theMarker = BlockMarker.getMarker(new V3(this.posX, this.posY, this.posZ, this.dimension));
                if (this.theMarker != null) {
                    this.caption = this.theMarker.caption;
                }
            }

            if (this.theMarker != null) {
                this.posY = (double)this.theMarker.y;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void entityInit() {
        this.noClip = true;
    }

    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
    }
}
