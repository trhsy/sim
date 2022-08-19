package com.trhsy.sim.common.core.entity.ai;

import com.trhsy.sim.common.core.entity.EntityFolk;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * npc 的智能AI 闲逛
 */
public class EntityAIWanderSUK extends EntityAIBase {
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;

    public EntityAIWanderSUK(EntityCreature folk, double speedIn) {
        this(folk, speedIn, 120);
    }

    public EntityAIWanderSUK(EntityCreature folk, double speedIn, int chance) {
        this.entity = folk;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 返回EntityAIBase是否应开始执行。
     * @Date 16:36 2022/7/31
     * @Param []
     **/
    @Override
    public boolean shouldExecute() {
        EntityFolk actualFolk = (EntityFolk) this.entity;
        if (this.entity.isWithinHomeDistanceCurrentPosition()) {
            return false;
        }else{
            if (!this.mustUpdate) {
                if (actualFolk.theData != null) {
                    if (actualFolk.theData.stayPut) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            V3 v = actualFolk.theData.destination;
            if (v == null) {
                return false;
            } else {
                this.xPosition = v.xCoord;
                this.yPosition = v.yCoord;
                this.zPosition = v.zCoord;
                this.mustUpdate = false;
                return true;
            }
        }
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 返回正在进行的EntityAIBase是否应继续执行
     * @Date 10:36 2022/8/13
     * @Param []
     **/
    @Override
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * 开始执行
     */
    @Override
    public void startExecuting() {
        try {
            this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("NPC智能AI移动发生错误：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 使任务绕过机会
     */
    public void makeUpdate() {
        this.mustUpdate = true;
    }

    /**
     * 更改任务执行的随机可能性
     */
    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}
