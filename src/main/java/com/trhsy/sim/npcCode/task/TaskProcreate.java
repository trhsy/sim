//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Random;

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
        this.folk.setStatus(new TextComponentTranslation("container.sim.folk_data_Trying_baby", new Object[0]).getUnformattedText());

        if (this.folk.gender == 0) {
            Random random = this.folk.entity.getRNG();

            for (int i = 0; i < 7; ++i)
            {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double)this.folk.entity.width * 2.0D - (double)this.folk.entity.width;
                double d4 = 0.5D + random.nextDouble() * (double)this.folk.entity.height;
                double d5 = random.nextDouble() * (double)this.folk.entity.width * 2.0D - (double)this.folk.entity.width;
                this.folk.entity.world.spawnParticle(EnumParticleTypes.HEART, this.folk.entity.posX + d3, this.folk.entity.posY + d4, this.folk.entity.posZ + d5, d0, d1, d2);
            }
          /*  Random random = new Random();
            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextDouble() * 0.5D;
                double d1 = random.nextDouble() * 0.5D;
                double d2 = random.nextDouble() * 0.5D;
                double d3 = random.nextDouble() * (double) this.folk.entity.width * 2.0D - (double) this.folk.entity.width;
                double d4 = 0.5D + random.nextDouble() * (double) this.folk.entity.height;
                double d5 = random.nextDouble() * (double) this.folk.entity.width * 2.0D - (double) this.folk.entity.width;
                this.folk.entity.world.spawnParticle(EnumParticleTypes.HEART, this.folk.pos.x + d3, this.folk.pos.y + d4, this.folk.pos.z + d5, d0, d1, d2);
            }*/
            //爱心
//            this.folk.entity.world.spawnParticle(EnumParticleTypes.HEART, this.folk.pos.x, this.folk.pos.y, this.folk.pos.z, d0, d1, d2, new int[0]);
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
            //好消息！都 凌曦和苏 荣昕正在怀孩子！
            ModSimLoader.sendChat(new TextComponentTranslation("container.sim.folk_data_Good_news", new Object[0]).getUnformattedText() + this.spouse.getName() + new TextComponentTranslation("container.sim.folk_data_and", new Object[0]).getUnformattedText() + this.folk.getName() + new TextComponentTranslation("container.sim.folk_data_expecting_a_baby", new Object[0]).getUnformattedText());
        }

    }
}
