package com.trhsy.sim.common.entity;/**
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
        this.func_70105_a(5.0F, 8.0F);
        this.field_70158_ak = true;
        this.field_70145_X = true;
        this.sailSpeedModifer = (new Random()).nextFloat() / 100.0F;
    }

    @Override
    public void func_70106_y() {
        try {
            super.func_70106_y();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setDead出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void func_70088_a() {
    }

    @Override
    public void func_70071_h_() {
        try {
            if (this.field_70170_p.func_72896_J()) {
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
            super.func_70071_h_();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void func_70030_z() {
    }

    @Override
    public boolean func_70094_T() {
        return false;
    }

    @Override
    public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,boolean p_180426_10_) {
    }

    @Override
    public AxisAlignedBB func_70114_g(Entity par1Entity) {
        return par1Entity.func_174813_aQ();
    }

    @Override
    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    @Override
    public boolean func_70104_M() {
        return false;
    }

    @Override
    protected void func_70037_a(NBTTagCompound nbttagcompound) {
    }

    @Override
    protected void func_70014_b(NBTTagCompound nbttagcompound) {
    }
}
