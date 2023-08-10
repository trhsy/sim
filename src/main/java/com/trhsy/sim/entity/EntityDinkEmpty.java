package com.trhsy.sim.entity;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity
 * @ClassName: EntityDinkEmpty
 * @Description:
 * @date 2023/08/04 上午 9:38
 */
public class EntityDinkEmpty extends EntityThrowable {
    public EntityDinkEmpty(World worldIn) {
        super(worldIn);
    }

    public EntityDinkEmpty(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityDinkEmpty(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     * 获取每次勾选时要应用于抛出实体的重力量。
     */
    @Override
    protected float getGravityVelocity()
    {
        return 0.07F;
    }
    /**
     * Called when this EntityThrowable hits a block or entity.
     * 当此EntityThrowable命中块或实体时调用。
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            int i = 0;

            if (result.entityHit instanceof EntityBlaze) {
                i = new Random().nextInt(99999)+50;
                ModSimLoader.log.info("啤酒瓶随机伤害为："+i);
            }
            DamageSource damageSource=DamageSource.causeThrownDamage(this, this.getThrower());
            damageSource.setProjectile();
            result.entityHit.attackEntityFrom(damageSource, i);
        }
        /**特效**/
        for (int j = 0; j < 8; ++j) {
            this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}
