package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.npc.job.JobLivestockFarmer;
import com.trhsy.sim.task.JobTask;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.task
 * @ClassName: JobTaskButcherAnimal
 * @Description: 屠夫动物
 * @date 2023/3/21 16:22
 */
public class JobTaskButcherAnimal extends JobTask {
    /**
     * 任务
     */
    public JobLivestockFarmer farmJob = null;
    /**
     * 屠戮目标
     */
    public Entity butcherTarget = null;
    public JobTaskButcherAnimal(Job j, long ms) {
        super(j, ms);
        this.farmJob = (JobLivestockFarmer)j;
    }

    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus("Butchering animal");
        this.selectTarget();
    }

    @Override
    public void onUpdate() {
        //如果屠戮目标为空则下一个
        if (this.butcherTarget == null) {
            this.selectTarget();
        } else {
            this.farmJob.folk.forceMoveToXYZ(V3.fromBlockPos(this.butcherTarget.getPosition()));
            if (V3.fromBlockPos(this.butcherTarget.getPosition()).getDistanceTo(this.farmJob.folk.getV3()) < 2) {
                this.killTarget();
            }

        }
    }
    public void selectTarget() {
        List<EntityAnimal> farmAnimals = this.getAnimalsInPen(this.farmJob.workPlace, (ForgeRegistries.BIOMES.getValue(new ResourceLocation(this.farmJob.livestockName))).getBiomeClass());
        List<EntityAnimal> grownAnimals = new ArrayList();
        if (farmAnimals.size() >= 1) {
            for(int i = 0; i < farmAnimals.size(); ++i) {
                if (!((EntityAnimal)farmAnimals.get(i)).isChild()) {
                    grownAnimals.add(farmAnimals.get(i));
                }
            }

            this.butcherTarget = (Entity)grownAnimals.get(this.rand.nextInt(farmAnimals.size()));
        }
    }

    /**
     * 选择实体动物
     * @param controlBox
     * @param animal
     * @return
     */
    public List<EntityAnimal> getAnimalsInPen(V3 controlBox, Class animal) {
        List<EntityAnimal> list = this.folk.entity.worldObj.getEntitiesWithinAABB(animal, (new AxisAlignedBB(controlBox.x, controlBox.y, controlBox.z, controlBox.x + 1.0D, controlBox.y + 1.0D, controlBox.z + 1.0D)).expand(3.0D, 2.0D, 3.0D));
        return list;
    }

    /**
     * 杀死目标
     */
    public void killTarget() {
        ModSimLoader.log.info("尝试杀死 " + this.butcherTarget.getName() + " " + this.butcherTarget.getEntityId());
        this.butcherTarget.setDropItemsWhenDead(false);
        this.butcherTarget.attackEntityFrom(DamageSource.generic, 500.0F);
        //随机数
        int quant = this.rand.nextInt(3) + 1;
        //牛
        if (this.butcherTarget instanceof EntityCow) {
            //牛肉
            this.farmJob.placeInJobChest(new ItemStack(Items.BEEF, quant));
            ModSimLoader.addMoney(-0.02F * (float)quant);
            //皮革
            if (this.rand.nextInt(5) > 2) {
                this.farmJob.placeInJobChest(new ItemStack(Items.LEATHER, 1));
            }
            //鸡
        } else if (this.butcherTarget instanceof EntityChicken) {
            //鸡肉
            this.farmJob.placeInJobChest(new ItemStack(Items.CHICKEN, 1));
            ModSimLoader.addMoney(-0.02F);
            if (this.rand.nextInt(5) > 2) {
                //羽毛
                this.farmJob.placeInJobChest(new ItemStack(Items.FEATHER, 1));
            }
            //猪
        } else if (this.butcherTarget instanceof EntityPig) {
            //猪排
            this.farmJob.placeInJobChest(new ItemStack(Items.PORKCHOP, quant));
            ModSimLoader.addMoney(-0.02F * (float)quant);
            //羊
        } else if (this.butcherTarget instanceof EntitySheep) {
            //羊肉
            this.farmJob.placeInJobChest(new ItemStack(Items.MUTTON, quant));
            ModSimLoader.addMoney(-0.02F * (float)quant);
            //兔子
        }else if (this.butcherTarget instanceof EntityRabbit) {
            //兔肉
            this.farmJob.placeInJobChest(new ItemStack(Items.RABBIT, quant));
            //兔脚
            this.farmJob.placeInJobChest(new ItemStack(Items.RABBIT_FOOT, 1));
            //兔皮
            this.farmJob.placeInJobChest(new ItemStack(Items.RABBIT_HIDE, 1));
            ModSimLoader.addMoney(-0.02F * (float)quant);
        }

        this.completeTask();
    }
    @Override
    public void onTaskComplete() {

    }
}
