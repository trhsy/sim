package com.trhsy.sim.common.core.entity.ai;

import com.sun.javafx.geom.Vec3d;
import com.trhsy.sim.common.core.entity.EntityFolk;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

import java.util.Random;

/**
 * npc 的智能AI 闲逛
 */
public class EntityAIWanderSUK extends EntityAIWander {
    private EntityFolk entity;
    protected boolean mustUpdate;
    protected int executionChance;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    protected double speed;
    public EntityAIWanderSUK(EntityFolk folk, double speedIn) {
        super(folk, speedIn, 120);
        this.entity = folk;
        this.speed=speedIn;
        this.executionChance = 120;
    }

    /*public EntityAIWanderSUK(EntityCreature folk, double speedIn, int chance) {
        this.entity = folk;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }*/

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 返回EntityAIBase是否应开始执行。
     * @Date 16:36 2022/7/31
     * @Param []
     **/
    @Override
    public boolean shouldExecute() {
        if(this.entity.theData==null){
            return false;
        }else if(!this.entity.theData.stayPut){
            if (!this.mustUpdate) {
                Random random=this.entity.getRNG();
                if (random.nextInt(this.executionChance) != 0) {
                    return false;
                }
            }
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
            if(vec3 == null){
                return false;
            }else{
                this.xPosition = vec3.xCoord;
                this.yPosition = vec3.yCoord;
                this.zPosition = vec3.zCoord;
                this.mustUpdate = false;
                return true;
            }
        }else {
            return false;
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
            this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition,0.3);
            //ModSimReloaded.log.info("NPC智能AI移动到x：" + this.xPosition + ",y：" + this.yPosition + ",z:" + this.zPosition);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("NPC智能AI移动发生错误：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

}
