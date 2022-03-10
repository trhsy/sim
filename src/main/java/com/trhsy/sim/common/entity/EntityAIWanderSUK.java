package com.trhsy.sim.common.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;

/**
 * ========================================
 *
 * @ClassName EntityAIWanderSUK
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:50
 * ========================================
 **/
public class EntityAIWanderSUK extends EntityAIWander {
    private EntityCreature theFolk;

    public EntityAIWanderSUK(EntityCreature folk, float par2) {
        super(folk, (double)par2);
        this.theFolk = folk;
    }

    @Override
    public void startExecuting() {
        if (!this.theFolk.isDead) {
            EntityFolk actualFolk = (EntityFolk)this.theFolk;
            if (actualFolk != null && actualFolk.theData != null && !actualFolk.theData.stayPut) {
                try {
                    super.startExecuting();
                } catch (Exception var3) {
                }
            }
        }

    }

    @Override
    public boolean shouldExecute() {
        if (!this.theFolk.isDead) {
            EntityFolk actualFolk = (EntityFolk)this.theFolk;
            if (actualFolk != null && actualFolk.theData != null) {
                if (actualFolk.theData.stayPut) {
                    return false;
                }

                return true;
            }
        }

        return true;
    }

    @Override
    public boolean continueExecuting() {
        if (!this.theFolk.isDead) {
            EntityFolk actualFolk = (EntityFolk)this.theFolk;
            if (actualFolk != null && actualFolk.theData != null) {
                if (actualFolk.theData.stayPut) {
                    return false;
                }

                return true;
            }
        }

        return true;
    }
}
