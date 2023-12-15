//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;

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
        this.folk.setStatus(new TextComponentTranslation("container.sim.folk_data_Trying_baby",new Object[0]).getUnformattedText());
        double d0 = this.rand.nextDouble() * 0.5D;
        double d1 = this.rand.nextDouble() * 0.5D;
        double d2 = this.rand.nextDouble() * 0.5D;
        if (this.folk.gender == 0) {
            //爱心
            this.folk.entity.world.spawnParticle(EnumParticleTypes.HEART, this.folk.pos.x, this.folk.pos.y, this.folk.pos.z, d0, d1, d2, new int[0]);
        }

    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onTaskComplete() {
        //男性 随机 6 次机会      最大年龄限制++++27
        if (this.folk.gender == 0 && this.rand.nextInt(4) == 3 && this.spouse.age < this.spouse.race.maturity + 27) {
            NpcData var10000 = this.spouse;
            var10000.pregnancyStage += 0.1F;
            //好消息！都 凌曦和苏 荣昕要生宝宝了！
            ModSimLoader.sendChat(new TextComponentTranslation("container.sim.folk_data_Good_news",new Object[0]).getUnformattedText() + this.spouse.getName() + new TextComponentTranslation("container.sim.folk_data_and",new Object[0]).getUnformattedText() + this.folk.getName() + new TextComponentTranslation("container.sim.folk_data_expecting_a_baby",new Object[0]).getUnformattedText());
        }

    }
}
