package com.trhsy.sim.common.entity.ai;

import com.trhsy.sim.common.entity.EntityFolk;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;

/**
 * npc 的智能AI 闲逛
 */
public class EntityAIWanderSUK extends EntityAIWander {
    private EntityCreature theFolk;

    public EntityAIWanderSUK(EntityCreature folk, float par2) {
        super(folk, (double)par2);
        this.theFolk = folk;
    }

    /**
     * 开始执行
     */
    @Override
    public void startExecuting() {
        if (!this.theFolk.isDead) {
            EntityFolk actualFolk = (EntityFolk)this.theFolk;
            //如果人们站在原地不动
            if (actualFolk != null && actualFolk.theData != null && !actualFolk.theData.stayPut) {
                try {
                    //开始移动
                    super.startExecuting();
                } catch (Exception var3) {
                    ModSimReloaded.log.error("NPC智能AI移动发生错误："+var3.getMessage());
                }
            }
        }

    }

    /**
     * 应该执行
     * @return
     */
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

    /**
     * 继续执行
     * @return
     */
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
