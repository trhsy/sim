package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.Building;
import com.trhsy.sim.npcCode.job.Job;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Iterator;

/**
 * @ClassName JobTaskPatrol
 * @Description todo 士兵任务
 * @Author TRHSY
 * @Date 2023/4/1517:42
 **/
public class JobTaskPatrol extends JobTask {
    /**
     * @Author fan
     * @Description //TODO 当前目标
     * @Date 17:46 2023/4/15
     * @Param
     * @return
     **/
    Entity currentTarget = null;
    /**
     * @Author fan
     * @Description //TODO 巡逻地点
     * @Date 17:46 2023/4/15
     * @Param
     * @return
     **/
    V3 patrolTo = null;

    double maxX;
    double minX;
    double maxZ;
    double minZ;
    /**
     * @Author fan
     * @Description //TODO 攻击延迟
     * @Date 17:46 2023/4/15
     * @Param
     * @return
     **/
    long attackDelay;

    public JobTaskPatrol(Job j, long ms) {
        super(j, ms);
        //工作空间
        this.maxX = this.job.workPlace.x + 20.0D;
        this.minX = this.job.workPlace.x - 20.0D;
        this.maxZ = this.job.workPlace.z + 20.0D;
        this.minZ = this.job.workPlace.z - 20.0D;
        //零攻击延迟
        this.attackDelay = 0L;

        Building b;
        for (Iterator var4 = ModSimLoader.buildings.iterator(); var4.hasNext(); this.minZ = b.controlXYZ.z < this.minZ ? b.controlXYZ.z : this.minZ) {
            b = (Building) var4.next();
            this.maxX = b.controlXYZ.x > this.maxX ? b.controlXYZ.x : this.maxX;
            this.minX = b.controlXYZ.x < this.minX ? b.controlXYZ.x : this.minX;
            this.maxZ = b.controlXYZ.z > this.maxZ ? b.controlXYZ.z : this.maxZ;
        }

        this.maxX += 20.0D;
        this.maxZ += 20.0D;
        this.minX -= 20.0D;
        this.minZ -= 20.0D;
    }

    @Override
    public void onTaskBegin() {
        this.patrolTo = this.getNewPosition();
    }

    @Override
    public void onUpdate() {
        BlockPos bp = this.job.folk.entity.getPosition();
        //如果当前目标为空则巡逻并找到目标
        if (this.currentTarget == null) {
            //巡逻
            this.folk.setStatus(new TextComponentTranslation("container.sim.ONPATROL",new Object[0]).getUnformattedText());
            double posX = this.folk.entity.posX;
            double posY = this.folk.entity.posY;
            double posZ = this.folk.entity.posZ;
            AxisAlignedBB bb = new AxisAlignedBB(posX - 5.0D, posY, posZ - 5.0D, posX + 5.0D, posY + 2.0D, posZ + 5.0D);
            //僵尸 小白 苦力怕
            EntityMob mob = (EntityMob) this.folk.entity.world.findNearestEntityWithinAABB(EntityMob.class, bb, this.folk.entity);
            //蜘蛛
            EntitySpider spider = (EntitySpider) this.folk.entity.world.findNearestEntityWithinAABB(EntitySpider.class, bb, this.folk.entity);
            //EntityGhast 恶魂
            EntityGhast ghast = (EntityGhast) this.folk.entity.world.findNearestEntityWithinAABB(EntityGhast.class, bb, this.folk.entity);
            //傀儡
//            EntityGolem golem=(EntityGolem)this.folk.entity.world.findNearestEntityWithinAABB(EntityGolem.class, bb, this.folk.entity);
            //史莱姆
            EntitySlime slime = (EntitySlime) this.folk.entity.world.findNearestEntityWithinAABB(EntitySlime.class, bb, this.folk.entity);

            if (mob != null) {
                this.currentTarget = mob;
            } else if (spider != null) {
                this.currentTarget = spider;
            } else if (ghast != null) {
                this.currentTarget = ghast;
            } else if (slime != null) {
                this.currentTarget = slime;
            }/*else if(golem!=null){
                this.currentTarget = golem;
            }*/
        }
        //不为空则开始攻击
        if (this.currentTarget != null) {
            //攻击谁
            this.folk.setStatus(new TextComponentTranslation("container.sim.ATTACKING",new Object[0]).getUnformattedText() + " " + this.currentTarget.getCommandSenderEntity().getName());
            if (System.currentTimeMillis() - this.attackDelay > 2L) {
                this.attackDelay = System.currentTimeMillis();
                V3 mobPos = V3.fromBlockPos(this.currentTarget.getPosition());
                this.job.folk.forceMoveToXYZNoWarp(mobPos);
                if (mobPos.getDistanceTo(this.job.folk.getV3()) < 2) {
                    if (this.currentTarget.isDead) {
                        this.currentTarget = null;
                    } else {
                        //攻击 夜行者
                        this.currentTarget.attackEntityFrom(DamageSource.GENERIC, 6.0F);
                    }
                }
            }
        }
        //到达巡逻地点 攻击目标不清空不走
        if (this.job.folk.isAtLocation(this.patrolTo)&&this.currentTarget != null) {
            //更换巡逻地点
            this.patrolTo = this.getNewPosition();
            if(this.patrolTo.x > this.maxX || this.patrolTo.x < this.minX || this.patrolTo.z > this.maxZ || this.patrolTo.z < this.minZ){
                this.patrolTo = this.getNewPosition();
                this.job.folk.forceMoveToXYZ(this.patrolTo,2);
            }
        }
    }

    @Override
    public void onTaskComplete() {

    }

    /**
     * 随机位置
     * @return
     */
    V3 getNewPosition() {
        Vec3d v3d = RandomPositionGenerator.findRandomTarget(this.job.folk.entity, 10, 7);
        return v3d != null ? V3.fromVec3d(v3d) : this.getNewPosition();
    }
}
