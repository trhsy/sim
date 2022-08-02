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
        if (!this.mustUpdate) {
            if (actualFolk.theData.age >= 100) {
                return false;
            }
            if(actualFolk.theData.stayPut){
                return false;
            }
        }
        V3 v = actualFolk.theData.destination;
        if (v == null) {
            return false;
        } else {
            this.xPosition = v.x;
            this.yPosition = v.y;
            this.zPosition = v.z;
            this.mustUpdate = false;
            return true;
        }
    }

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
            ModSimReloaded.log.error("NPC智能AI移动发生错误：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 使任务绕过机会
     */
    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    /**
     * 更改任务执行的随机可能性
     */
    public void setExecutionChance(int newchance)
    {
        this.executionChance = newchance;
    }
}
