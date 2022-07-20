package com.trhsy.sim.common.entity.ai;

import com.trhsy.sim.common.entity.EntityFolk;
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

    @Override
    public void func_75249_e() {
        if (!this.theFolk.field_70128_L) {
            EntityFolk actualFolk = (EntityFolk)this.theFolk;
            if (actualFolk != null && actualFolk.theData != null && !actualFolk.theData.stayPut) {
                try {
                    super.func_75249_e();
                } catch (Exception var3) {
                }
            }
        }

    }

    @Override
    public boolean func_75250_a() {
        if (!this.theFolk.field_70128_L) {
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
    public boolean func_75253_b() {
        if (!this.theFolk.field_70128_L) {
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
