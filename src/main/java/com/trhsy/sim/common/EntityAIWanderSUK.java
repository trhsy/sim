package com.trhsy.sim.common;/**
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
