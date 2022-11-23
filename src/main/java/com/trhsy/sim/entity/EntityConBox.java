package com.trhsy.sim.entity;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.job.JobBuilder;
import com.trhsy.sim.npc.job.JobTerrainFormer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @ClassName EntityConBox
 * @Description todo 悬浮的控制箱
 * @Author TRHSY
 * @Date 2022/10/2117:54
 **/
public class EntityConBox extends Entity {
    /**箱子偏移量**/
    public float boxYaw = 0.0F;
    /**字体颜色**/
    public int textColor = 11534255;
    /**建筑工作**/
    public JobBuilder builderJob;
    /**规划工作**/
    public JobTerrainFormer terrainFormerJob;
    /**NPC数据*/
    public NpcData folk;
    /**上传刷新**/
    private Long lastCheck = 0L;
    /**
     * @Author fan
     * @Description //TODO 初始化
     * @Date 16:29 2022/11/1
     * @Param [world]
     * @return
     **/
    public EntityConBox(World world) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
    }
    /**
     * @Author fan
     * @Description //TODO 建筑师初始化
     * @Date 16:29 2022/11/1
     * @Param [world, jobBuilder]
     * @return
     **/
    public EntityConBox(World world, JobBuilder jobBuilder) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
        this.builderJob = jobBuilder;
        this.folk = this.builderJob.folk;
    }
    /**
     * @Author fan
     * @Description //TODO 规划师初始化
     * @Date 16:29 2022/11/1
     * @Param [world, jobBuilder]
     * @return
     **/
    public EntityConBox(World world, JobTerrainFormer terrainFormerJob) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
        this.terrainFormerJob = terrainFormerJob;
        this.folk = this.terrainFormerJob.folk;
    }
    /**
     * @Author fan
     * @Description //TODO 实时更新
     * @Date 16:29 2022/11/1
     * @Param []
     * @return void
     **/
    public void onUpdate() {
        if (System.currentTimeMillis() - this.lastCheck > 10000L) {
            //不是客户端
            if (!this.worldObj.isRemote) {
                if (this.folk == null) {
                    //生成爆炸粒子
                    this.spawnExplosionParticle(this);
                    this.setDead();
                } else if (this.folk.job != this.builderJob) {
                    this.spawnExplosionParticle(this);
                    this.setDead();
                }else if (this.folk.job != this.terrainFormerJob) {
                    this.spawnExplosionParticle(this);
                    this.setDead();
                }
            }

            this.lastCheck = System.currentTimeMillis();
        }

        ++this.boxYaw;
        super.onUpdate();
    }
    /**
     * @Author fan
     * @Description //TODO 生成爆炸粒子
     * @Date 16:30 2022/11/1
     * @Param [ent]
     * @return void
     **/
    private void spawnExplosionParticle(Entity ent) {
        Random rand = new Random();

        for(int var1 = 0; var1 < 20; ++var1) {
            double var2 = rand.nextGaussian() * 0.02D;
            double var4 = rand.nextGaussian() * 0.02D;
            double var6 = rand.nextGaussian() * 0.02D;
            double var8 = 10.0D;

            try {
                //默认爆炸
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
