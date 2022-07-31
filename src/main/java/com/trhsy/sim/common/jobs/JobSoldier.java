package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobSoldier
 * @Description todo 士兵
 * @Author Administratr
 * @Date 2022/1/27 0027下午 3:54
 * ========================================
 **/
public class JobSoldier extends Job implements Serializable {
    //职业
    public Vocation vocation = null;
    //实体人
    public FolkData theFolk = null;
    //阶段
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient Entity badGuy;
    private transient int count = 60;
    private transient long timeSinceLastBTB = 0L;
    public transient int kills = 0;
    Random rand = new Random();

    public JobSoldier() {
    }

    public JobSoldier(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }
            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobSoldier出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void resetJob() {
        try {
            this.theStage = Stage.IDLE;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("resetJob出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage == Stage.IDLE) {
                    this.theStage = Stage.ONPATROL;
                    if (this.theFolk.destination == null) {
                        this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
                    }
                } else if (this.theStage == Stage.ONPATROL) {
                    this.stageOnPatrol();
                } else if (this.theStage == Stage.ATTACKING) {
                    this.stageAttacking();
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageOnPatrol() {
        try {
        this.theFolk.isWorking = false;
        this.theFolk.stayPut = false;
        this.theFolk.action = FolkAction.ATWORK;
        this.theFolk.statusText = I18n.format("container.sim.job.soldier.farmer.Patroling");
        this.runDelay = 10000;
        if (this.jobWorld == null) {
            this.jobWorld = MinecraftServer.getServer().worldServerForDimension(this.theFolk.employedAt.theDimension);
        }

        if (System.currentTimeMillis() - this.timeSinceLastBTB > 120000L) {
            if (this.theFolk.isSpawned()) {
                EntityPlayer player = this.jobWorld.getClosestPlayer(this.theFolk.theEntity.posX, this.theFolk.theEntity.posY, this.theFolk.theEntity.posZ, 50);
                if (player != null) {
                    this.theFolk.gotoXYZ(new V3(player.posX, player.posY, player.posZ, player.dimension), GotoMethod.WALK);
                }
            }

            this.timeSinceLastBTB = System.currentTimeMillis();
            this.runDelay = 60000;
        } else {
            int xo = this.rand.nextInt(60) - 30;
            int zo = this.rand.nextInt(60) - 30;

            V3 wanderTo;
            Double var6;

            for(wanderTo = new V3(this.theFolk.location.x + (double)xo, this.theFolk.location.y - 1, this.theFolk.location.z + (double)zo, this.theFolk.location.theDimension); this.jobWorld.getBlockState(new BlockPos(wanderTo.x.intValue(), wanderTo.y.intValue(), wanderTo.z.intValue())).getBlock() != null && wanderTo.y < 255; var6 = wanderTo.y = wanderTo.y + 1) {
                Double var5 = wanderTo.y;
            }

            this.theFolk.gotoXYZ(wanderTo, GotoMethod.WALK);
        }

        List list = this.jobWorld.getEntitiesWithinAABBExcludingEntity(this.mc.thePlayer, new AxisAlignedBB(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.x + 1.0, this.theFolk.employedAt.y + 1.0, this.theFolk.employedAt.z + 1.0).expand(100, 5.0, 100));


            this.badGuy = this.findClosestHostileMob(list);
            if (this.badGuy != null) {
                this.runDelay = 1000;
                this.theStage = Stage.ATTACKING;
                this.count = 100;
                this.theFolk.statusText = I18n.format("container.sim.job.soldier.farmer.Going") + this.badGuy.getEntityData();
                if (this.theFolk.isSpawned()) {
                    this.theFolk.gotoXYZ(new V3(this.badGuy.posX, this.badGuy.posY, this.badGuy.posZ, this.theFolk.theEntity.dimension), GotoMethod.WALK);
                } else {
                    this.theFolk.gotoXYZ(new V3(this.badGuy.posX, this.badGuy.posY, this.badGuy.posZ, this.theFolk.theEntity.dimension), GotoMethod.SHIFT);
                }

                return;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageOnPatrol出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 阶段性进攻
     */
    private void stageAttacking() {
        try {
            this.runDelay = 200;
            if (this.theFolk.theEntity != null) {
                this.theFolk.stayPut = false;
                int distance = (int)this.theFolk.theEntity.getDistanceToEntity(this.badGuy);
                if (this.theFolk.destination == null) {
                    if (this.theFolk.isSpawned()) {
                        this.theFolk.gotoXYZ(new V3(this.badGuy.posX, this.badGuy.posY, this.badGuy.posZ, this.theFolk.theEntity.dimension), GotoMethod.WALK);
                    } else {
                        this.theFolk.gotoXYZ(new V3(this.badGuy.posX, this.badGuy.posY, this.badGuy.posZ, this.theFolk.theEntity.dimension), GotoMethod.SHIFT);
                    }
                }

                distance = (int)this.theFolk.theEntity.getDistanceToEntity(this.badGuy);
                --this.count;
                if (this.count <= 0) {
                    this.theStage = Stage.ONPATROL;
                }

                if (distance <= 5) {
                    distance = (int)this.theFolk.theEntity.getDistanceToEntity(this.badGuy);
                    if (distance < 4) {
                        this.theFolk.isWorking = true;
                        this.runDelay = 50;
                        this.badGuy.attackEntityFrom(DamageSource.generic, 3.0F);
                    }

                    if (this.badGuy.isDead) {
                        this.theStage = Stage.ONPATROL;
                        this.runDelay = (int)((11.0F - this.theFolk.levelSoldier) * 500.0F * (11.0F - this.theFolk.levelSoldier));
                        ++this.kills;
                        if (this.theFolk.levelSoldier < 10.0F) {
                            FolkData var10000 = this.theFolk;
                            var10000.levelSoldier += 0.03F;
                            if (this.theFolk.levelSoldier > 10.0F) {
                                this.theFolk.levelSoldier = 10.0F;
                            }
                        }

                        this.theFolk.statusText = I18n.format("container.sim.job.soldier.farmer.Killed") + this.badGuy.getEntityData();
                        this.theFolk.isWorking = false;
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageAttacking出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private Entity findClosestHostileMob(List<Entity> mobs) {
        Entity closestBadGuy = null;
        try {
            if (!this.theFolk.isSpawned()) {
                return null;
            } else {
                for(int j = 0; j < mobs.size(); ++j) {
                    Entity entity1 = (Entity)mobs.get(j);
                    if (entity1 instanceof EntityMob || entity1 instanceof IMob) {

                        PathEntity path =this.theFolk.theEntity.getNavigator().getPathToXYZ(entity1.posX, entity1.posY, entity1.posZ);
                        //PathEntity path = this.jobWorld.getEntityPathToXYZ(this.theFolk.theEntity, (int)entity1.posX, (int)entity1.posY, (int)entity1.posZ, 40.0F, true, true, true, true);
                        if (path != null) {
                            closestBadGuy = entity1;
                            break;
                        }
                    }
                }

                return closestBadGuy;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("findClosestHostileMob出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return closestBadGuy;
    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.soldier.farmer.Reporting");
                this.theStage = Stage.ONPATROL;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }


}

