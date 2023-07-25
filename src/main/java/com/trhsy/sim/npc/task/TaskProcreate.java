//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumParticleTypes;
/**
 * @Author fan
 * @Description //TODO 生育任务
 * @Date 18:44 2022/10/16
 * @Param 
 * @return 
 **/
public class TaskProcreate extends Task {
    NpcData spouse;

    public TaskProcreate(NpcData folk, long ms, NpcData spouse) {
        super(folk, ms);
        this.spouse = spouse;
        this.interruptSleep = true;
    }

    @Override
    public void onTaskBegin() {
        //想要个宝宝
        this.folk.setStatus(I18n.format("container.sim.folk_data_Trying_baby"));
        double d0 = this.rand.nextDouble() * 0.5D;
        double d1 = this.rand.nextDouble() * 0.5D;
        double d2 = this.rand.nextDouble() * 0.5D;
        if (this.folk.gender == 0) {
            //爱心
            this.folk.entity.worldObj.spawnParticle(EnumParticleTypes.HEART, this.folk.pos.x, this.folk.pos.y, this.folk.pos.z, d0, d1, d2, new int[0]);
        }

    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onTaskComplete() {
        if (this.folk.gender == 0 && this.rand.nextInt(7) == 6 && this.spouse.age < this.spouse.race.maturity + 27) {
            NpcData var10000 = this.spouse;
            var10000.pregnancyStage += 0.1F;
            //好消息！都 凌曦和苏 荣昕要生宝宝了！
            ModSimLoader.sendChat(I18n.format("container.sim.folk_data_Good_news") + this.spouse.getName() + I18n.format("container.sim.folk_data_and") + this.folk.getName() + I18n.format("container.sim.folk_data_expecting_a_baby"));
        }

    }
}
