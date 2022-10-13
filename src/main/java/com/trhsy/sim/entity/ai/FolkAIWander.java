package com.trhsy.sim.entity.ai;

import com.trhsy.sim.entity.EntityFolk;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.ai
 * @ClassName: FolkAIWander
 * @Description:
 * @date 2022/10/13 10:59
 */
public class FolkAIWander extends EntityAIWander{
    EntityFolk folk;
    private EntityCreature entity;
    private boolean mustUpdate;
    private int executionChance;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    public FolkAIWander(EntityFolk entity, double speedIn) {
        super(entity, speedIn);
        this.folk = entity;
        this.speed=speedIn;
    }
    @Nullable
    protected Vec3d getPosition() {
        return this.folk.theData.stayPut ? this.folk.getPositionVector() : RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
    }

    @Override
    public boolean shouldExecute(){
        if (this.folk.theData == null) {
            return false;
        } else if (!this.folk.theData.stayPut) {
            if (!this.mustUpdate) {
               /* if (this.entity.getIdleTime() >= 100) {
                    return false;
                }*/

                if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                    return false;
                }
            }

            Vec3d vec3d = this.getPosition();
            if (vec3d == null) {
                return false;
            } else {
                this.xPosition = vec3d.xCoord;
                this.yPosition = vec3d.yCoord;
                this.zPosition = vec3d.zCoord;
                this.mustUpdate = false;
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
}
