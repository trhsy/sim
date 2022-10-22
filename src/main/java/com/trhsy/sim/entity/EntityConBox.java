package com.trhsy.sim.entity;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.job.JobBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @ClassName EntityConBox
 * @Description todo
 * @Author TRHSY
 * @Date 2022/10/2117:54
 **/
public class EntityConBox extends Entity {
    public float boxYaw = 0.0F;
    public int textColor = 11534255;
    public JobBuilder job;
    public NpcData folk;
    private Long lastCheck = 0L;

    public EntityConBox(World world) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
    }

    public EntityConBox(World world, JobBuilder jobBuilder) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
        this.job = jobBuilder;
        this.folk = this.job.folk;
    }

    public void onUpdate() {
        if (System.currentTimeMillis() - this.lastCheck > 10000L) {
            if (!this.worldObj.isRemote) {
                if (this.folk == null) {
                    this.spawnExplosionParticle(this);
                    this.setDead();
                } else if (this.folk.job != this.job) {
                    this.spawnExplosionParticle(this);
                    this.setDead();
                }
            }

            this.lastCheck = System.currentTimeMillis();
        }

        ++this.boxYaw;
        super.onUpdate();
    }

    private void spawnExplosionParticle(Entity ent) {
        Random rand = new Random();

        for(int var1 = 0; var1 < 20; ++var1) {
            double var2 = rand.nextGaussian() * 0.02D;
            double var4 = rand.nextGaussian() * 0.02D;
            double var6 = rand.nextGaussian() * 0.02D;
            double var8 = 10.0D;

            try {
                ent.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, ent.posX + (double)(rand.nextFloat() * 1.0F * 2.0F) - 1.0D - var2 * var8, ent.posY + (double)(rand.nextFloat() * 1.0F) - var4 * var8, ent.posZ + (double)(rand.nextFloat() * 1.0F * 2.0F) - 1.0D - var6 * var8, var2, var4, var6, new int[0]);
            } catch (Exception var13) {
            }
        }

    }

    public AxisAlignedBB getCollisionBox(Entity par1Entity) {
        return null;
    }

    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    public boolean canBePushed() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    protected void entityInit() {
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
    }
}
