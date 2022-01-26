package com.trhsy.sim.common.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

/**
 * ========================================
 *
 * @ClassName EntityWindmill
 * @Description todo
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

    public void func_70106_y() {
        ModSimukraft.log.info("EntityWindmill: setDead() called");
        super.func_70106_y();
    }

    protected void func_70088_a() {
    }

    public void func_70071_h_() {
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
    }

    public void func_70030_z() {
    }

    public boolean func_70094_T() {
        return false;
    }

    public void func_70056_a(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

    public AxisAlignedBB func_70114_g(Entity par1Entity) {
        return par1Entity.field_70121_D;
    }

    public AxisAlignedBB func_70046_E() {
        return this.field_70121_D;
    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    public boolean func_70104_M() {
        return false;
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
    }
}
