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

    public EntityAIWanderSUK(EntityCreature folk, double par2) {
        super(folk, par2);
        this.theFolk = folk;
    }

    /**
     * 开始执行
     */
    @Override
    public void startExecuting() {
        try {
            EntityFolk actualFolk = (EntityFolk) this.theFolk;
            //如果人们站在原地不动
            if (actualFolk != null && actualFolk.theData != null && !actualFolk.theData.stayPut) {
                //开始移动
                super.startExecuting();
            }
        } catch (Exception var3) {
            ModSimReloaded.log.error("NPC智能AI移动发生错误：" + var3.getMessage());
        }
    }

    /**
     * 应该执行
     *
     * @return
     */
    @Override
    public boolean shouldExecute() {
        Boolean flay=false;
        try {
            EntityFolk actualFolk = (EntityFolk) this.theFolk;
            if (actualFolk != null && actualFolk.theData != null) {
                if (actualFolk.theData.stayPut) {
                    flay=false;
                }
                flay=true;
            }
        }catch (Exception e){
            ModSimReloaded.log.error("NPC智能AI移动发生错误：" + e.getMessage());
        }
        return flay;
    }

    /**
     * 继续执行
     *
     * @return
     */
    @Override
    public boolean continueExecuting() {
        Boolean flay=false;
        try {
            EntityFolk actualFolk = (EntityFolk) this.theFolk;
            if (actualFolk != null && actualFolk.theData != null) {
                if (actualFolk.theData.stayPut) {
                    flay=false;
                }
                flay=true;
            }
        }catch (Exception e){
            ModSimReloaded.log.error("NPC智能AI移动发生错误：" + e.getMessage());
        }
        return flay;

    }
}
