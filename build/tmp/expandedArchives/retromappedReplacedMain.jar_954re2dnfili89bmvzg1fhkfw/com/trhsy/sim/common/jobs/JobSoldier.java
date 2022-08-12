package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
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
    public FolkData theFolk =new FolkData();
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
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
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
                        this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                    }
                } else if (this.theStage == Stage.ONPATROL) {
                    this.stageOnPatrol();
                } else if (this.theStage == Stage.ATTACKING) {
                    this.stageAttacking();
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobSoldier-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageOnPatrol() {
        try {
        this.theFolk.isWorking = false;
        this.theFolk.stayPut = false;
        this.theFolk.action = FolkAction.ATWORK;
        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.soldier.farmer.Patroling");
        this.runDelay = 10000;
        if (this.jobWorld == null) {
            this.jobWorld = MinecraftServer.func_71276_C().func_71218_a(this.theFolk.employedAt.theDimension);
        }

        if (System.currentTimeMillis() - this.timeSinceLastBTB > 120000L) {
            if (this.theFolk.isSpawned()) {
                EntityPlayer player = this.jobWorld.func_72977_a(this.theFolk.theEntity.field_70165_t, this.theFolk.theEntity.field_70163_u, this.theFolk.theEntity.field_70161_v, 50);
                if (player != null) {
                    this.theFolk.gotoXYZ(new V3(player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_71093_bK), null);
                }
            }

            this.timeSinceLastBTB = System.currentTimeMillis();
            this.runDelay = 60000;
        } else {
            int xo = this.rand.nextInt(60) - 30;
            int zo = this.rand.nextInt(60) - 30;

            V3 wanderTo = new V3(this.theFolk.location.field_72450_a + (double)xo, this.theFolk.location.field_72448_b - 1, this.theFolk.location.field_72449_c + (double)zo, this.theFolk.location.theDimension);
            if( this.jobWorld.func_180495_p(new BlockPos(wanderTo.field_72450_a, wanderTo.field_72448_b, wanderTo.field_72449_c)).func_177230_c() != null && wanderTo.field_72448_b < 255) {
                wanderTo=new V3(wanderTo.field_72450_a,wanderTo.field_72448_b,wanderTo.field_72449_c);
            }
            this.theFolk.gotoXYZ(wanderTo, null);
        }

        List list = this.jobWorld.func_72839_b(this.mc.field_71439_g, new AxisAlignedBB(this.theFolk.employedAt.field_72450_a, this.theFolk.employedAt.field_72448_b, this.theFolk.employedAt.field_72449_c, this.theFolk.employedAt.field_72450_a + 1.0, this.theFolk.employedAt.field_72448_b + 1.0, this.theFolk.employedAt.field_72449_c + 1.0).func_72314_b(100, 5.0, 100));


            this.badGuy = this.findClosestHostileMob(list);
            if (this.badGuy != null) {
                this.runDelay = 1000;
                this.theStage = Stage.ATTACKING;
                this.count = 100;
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.soldier.farmer.Going") + this.badGuy.getEntityData();
                if (this.theFolk.isSpawned()) {
                    this.theFolk.gotoXYZ(new V3(this.badGuy.field_70165_t, this.badGuy.field_70163_u, this.badGuy.field_70161_v, this.theFolk.theEntity.field_71093_bK), null);
                } else {
                    this.theFolk.gotoXYZ(new V3(this.badGuy.field_70165_t, this.badGuy.field_70163_u, this.badGuy.field_70161_v, this.theFolk.theEntity.field_71093_bK), GotoMethod.SHIFT);
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
                int distance = (int)this.theFolk.theEntity.func_70032_d(this.badGuy);
                if (this.theFolk.destination == null) {
                    if (this.theFolk.isSpawned()) {
                        this.theFolk.gotoXYZ(new V3(this.badGuy.field_70165_t, this.badGuy.field_70163_u, this.badGuy.field_70161_v, this.theFolk.theEntity.field_71093_bK), null);
                    } else {
                        this.theFolk.gotoXYZ(new V3(this.badGuy.field_70165_t, this.badGuy.field_70163_u, this.badGuy.field_70161_v, this.theFolk.theEntity.field_71093_bK), GotoMethod.SHIFT);
                    }
                }

                distance = (int)this.theFolk.theEntity.func_70032_d(this.badGuy);
                --this.count;
                if (this.count <= 0) {
                    this.theStage = Stage.ONPATROL;
                }

                if (distance <= 5) {
                    distance = (int)this.theFolk.theEntity.func_70032_d(this.badGuy);
                    if (distance < 4) {
                        this.theFolk.isWorking = true;
                        this.runDelay = 50;
                        this.badGuy.func_70097_a(DamageSource.field_76377_j, 3.0F);
                    }

                    if (this.badGuy.field_70128_L) {
                        this.theStage = Stage.ONPATROL;
                        this.runDelay = (int)((11 - this.theFolk.levelSoldier) * 500.0F * (11 - this.theFolk.levelSoldier));
                        ++this.kills;
                        if (this.theFolk.levelSoldier < 10.0F) {
                            FolkData var10000 = this.theFolk;
                            var10000.levelSoldier += 0.03F;
                            if (this.theFolk.levelSoldier > 10.0F) {
                                this.theFolk.levelSoldier = 10.0F;
                            }
                        }

                        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.soldier.farmer.Killed") + this.badGuy.getEntityData();
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
                        Boolean flag=this.theFolk.theEntity.func_70661_as().func_75492_a(entity1.field_70165_t, entity1.field_70163_u, entity1.field_70161_v,0.3);
                        if (flag) {
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
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.soldier.farmer.Reporting");
                this.theStage = Stage.ONPATROL;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }


}

