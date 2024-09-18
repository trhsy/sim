package com.trhsy.sim.entity.ai;

import com.trhsy.sim.entity.EntityNpc;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.ai
 * @ClassName: FolkAIWander
 * @Description: 实体NPC的Ai
 * @date 2023/11/20 下午 4:23
 */
public class FolkAIWander extends EntityAIWander {

    protected EntityNpc folk;
    protected boolean mustUpdate;
    protected int executionChance;
    protected double x;
    protected double y;
    protected double z;
    protected double speed;

    public FolkAIWander(EntityNpc entity, double speedIn) {
        super(entity, speedIn);
        this.folk = entity;
        this.speed = speedIn;
        this.executionChance = 120;
    }

    @Override
    protected Vec3d getPosition() {
        if(this.folk.theData.stayPut){
           return this.folk.getPositionVector();
        }else{
            return RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
        }
    }

    @Override
    public boolean shouldExecute() {
        if (this.folk.theData == null) {
            return false;
        }else if(!this.folk.theData.stayPut){
            if (!this.mustUpdate) {
                //获得空闲时间
                if (this.entity.getIdleTime() >= 100) {
                    return false;
                }
                //下一个
                if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                    return false;
                }
            }

            Vec3d vec3d = this.getPosition();
            if (vec3d == null) {
                return false;
            } else {
                this.x = vec3d.x;
                this.y = vec3d.y;
                this.z = vec3d.z;
                this.mustUpdate = false;
                return true;
            }
        }else {
            return false;
        }

        /*else if (!this.folk.theData.stayPut && !(this.folk.theData.currentTask instanceof TaskSleep)) {
            if (!this.mustUpdate) {
                if (this.entity.getIdleTime() >= 100) {
                    return false;
                }

                if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                    return false;
                }
            }

            Vec3d vec3d = this.getPosition();
            if (vec3d == null) {
                return false;
            } else {
                this.x = vec3d.x;
                this.y = vec3d.y;
                this.z = vec3d.z;
                this.mustUpdate = false;
                return true;
            }
        }*/
    }
}
