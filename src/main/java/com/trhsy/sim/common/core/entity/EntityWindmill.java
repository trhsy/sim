package com.trhsy.sim.common.core.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

/**
 * ========================================
 *
 * @ClassName EntityWindmill
 * @Description todo 风车实体
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:55
 * ========================================
 **/
public class EntityWindmill extends Entity {

    public float sailRotation = 0.0F;
    private float sailSpeed = 0.0F;
    private float sailSpeedModifer = 0.0F;

    public EntityWindmill(World par1World) {
        super(par1World);
        this.setSize(5.0F, 8.0F);
        this.ignoreFrustumCheck = true;
        this.noClip = true;
        this.sailSpeedModifer = (new Random()).nextFloat() / 100.0F;
    }

    @Override
    public void setDead() {
        try {
            super.setDead();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setDead出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        try {
            if (this.worldObj.isRaining()) {
                if (this.sailSpeed < 0.1F) {
                    this.sailSpeed += 0.001F;
                } else if (this.sailSpeed > 0.1F) {
                    this.sailSpeed -= 0.001F;
                }
            } else if (this.sailSpeed < 0.02F) {
                this.sailSpeed += 1.0E-4F;
            } else if (this.sailSpeed > 0.02F) {
                this.sailSpeed -= 0.001F;
            }

            this.sailRotation += this.sailSpeed + this.sailSpeedModifer;
            super.onUpdate();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("EntityWindmill-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onEntityUpdate() {
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,boolean p_180426_10_) {
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity) {
        return par1Entity.getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isDead;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }
}
