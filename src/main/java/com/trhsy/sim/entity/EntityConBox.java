package com.trhsy.sim.entity;

import com.trhsy.sim.loader.ModSimLoader;
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
    @Override
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
            double d0 = (double)((float)ent.posX + (5.0F + rand.nextFloat() * 6.0F) / 16.0F);
            double d1 = (double)((float)ent.posY + 0.8125F);
            double d2 = (double)((float)ent.posZ + (5.0F + rand.nextFloat() * 6.0F) / 16.0F);
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            try {
                ent.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
            } catch (Exception var13) {
                ModSimLoader.log.error("悬浮构建箱生成爆炸粒子出错了");
            }
        }

    }
    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity) {
        return null;
    }

    public AxisAlignedBB getBoundingBox() {
        return null;
    }
    @Override
    public boolean canBePushed() {
        return false;
    }
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    @Override
    protected void entityInit() {
    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }
}
