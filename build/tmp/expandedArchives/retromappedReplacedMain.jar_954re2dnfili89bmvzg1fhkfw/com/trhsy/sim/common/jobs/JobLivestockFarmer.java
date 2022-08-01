package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobLivestockFarmer
 * @Description todo 畜牧业者
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:50
 * ========================================
 **/
public class JobLivestockFarmer extends Job implements Serializable {
    private static final long serialVersionUID = -1177112209988279141L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private CopyOnWriteArrayList<IInventory> farmChests = new CopyOnWriteArrayList();
    EntityAnimal redShirt = null;

    public JobLivestockFarmer() {
    }

    public JobLivestockFarmer(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.BEAM);
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobLivestockFarmer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            if (!ModSimReloaded.isDayTime()) {
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.WAITINGFORMATUREANIMAL) {
                this.runDelay = 20000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATFARM) {
                        this.stageArrived();
                    } else if (this.theStage == Stage.WAITINGFORMATUREANIMAL) {
                        this.stageWaiting();
                    } else if (this.theStage == Stage.SLAUGHTERING) {
                        this.stageSlaughtering();
                    } else if (this.theStage == Stage.CANTWORK) {
                        this.stageCantWork();
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageArrived() {
        try {
            this.vocation = this.theFolk.vocation;
            this.theStage = Stage.WAITINGFORMATUREANIMAL;
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.Starting");
            //int count = false;
            int count;
            if (this.vocation == Vocation.CATTLEFARMER) {
                count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityCow.class);
                if (count < 2) {
                    this.spawnAnimals(this.theFolk.employedAt, "Cow", 6 - count);
                }
            } else if (this.vocation == Vocation.CHICKENFARMER) {
                count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityChicken.class);
                if (count < 2) {
                    this.spawnAnimals(this.theFolk.employedAt, "Chicken", 6 - count);
                }
            } else if (this.vocation == Vocation.PIGFARMER) {
                count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityPig.class);
                if (count < 2) {
                    this.spawnAnimals(this.theFolk.employedAt, "Pig", 6 - count);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageArrived出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageWaiting() {
        try {
            this.vocation = this.theFolk.vocation;
            this.theFolk.updateLocationFromEntity();
            double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist > 10) {
                this.theFolk.beamMeTo(this.theFolk.employedAt);
            }

            List list = null;
            if (this.vocation == Vocation.CATTLEFARMER) {
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.Feeding");
                list = this.jobWorld.func_72872_a(EntityCow.class,new AxisAlignedBB(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.x + 1, this.theFolk.employedAt.y + 1, this.theFolk.employedAt.z + 1).func_72314_b(4, 2, 4));
            } else if (this.vocation == Vocation.CHICKENFARMER) {
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.chickens");
                list = this.jobWorld.func_72872_a(EntityChicken.class, new AxisAlignedBB(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.x + 1, this.theFolk.employedAt.y + 1, this.theFolk.employedAt.z + 1).func_72314_b(4.0, 2.0, 4.0));
            } else if (this.vocation == Vocation.PIGFARMER) {
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.pigs");
                list = this.jobWorld.func_72872_a(EntityPig.class, new AxisAlignedBB(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.x + 1.0, this.theFolk.employedAt.y + 1.0, this.theFolk.employedAt.z + 1.0).func_72314_b(4.0, 2.0, 4.0));
            }

            int adultCount = 0;
            EntityAnimal animal = null;
            if (list != null) {
                for(int i = 0; i < list.size(); i++) {
                    animal = (EntityAnimal)list.get(i);
                    if (!animal.func_70631_g_()) {
                        ++adultCount;
                        this.redShirt = animal;
                    }
                }

                if (adultCount > 2) {
                    this.theStage = Stage.SLAUGHTERING;
                } else if (adultCount <= 2 && list.size() < 10) {
                    EntityAnimal a1 = null;
                    EntityAnimal a2 = null;

                    for(int i = 0; i < list.size(); i++) {
                        animal = (EntityAnimal)list.get(i);
                        if (!animal.func_70631_g_()) {
                            if (a1 == null) {
                                a1 = animal;
                            } else if (a2 == null) {
                                a2 = animal;
                            }
                        }
                    }

                    if (a1 != null && a2 != null) {
                        a2.func_70661_as().func_75484_a(a2.func_70661_as().func_75505_d(),20);
                        //a2.setPathToEntity(this.jobWorld.getPathEntityToEntity(a2, a1, 20.0F, true, true, true, true));
                        this.procreate(a1, new V3(a1.field_70165_t, a1.field_70163_u, a1.field_70161_v, this.theFolk.location.theDimension));
                    }

                    this.theStage = Stage.WAITINGFORMATUREANIMAL;
                    this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.Raking");
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageWaiting出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageSlaughtering() {
        try {

            Random rand = new Random();
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.Off");
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.func_70625_a(this.redShirt, 1.0F, 1.0F);
            }

            this.redShirt.func_70606_j(0.0F);
            this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
            int quant = 0;
            this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            boolean ok = true;
            if (this.vocation == Vocation.CATTLEFARMER) {
                quant = rand.nextInt(2) + 1;
                ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.field_151082_bd, quant), true);
                this.inventoriesPut(this.farmChests, new ItemStack(Items.field_151116_aA, 1), false);
            } else if (this.vocation == Vocation.PIGFARMER) {
                quant = rand.nextInt(2) + 1;
                ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.field_151147_al, quant), true);
            } else if (this.vocation == Vocation.CHICKENFARMER) {
                quant = rand.nextInt(2) + 1;
                ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.field_151076_bf, quant), true);
                this.inventoriesPut(this.farmChests, new ItemStack(Items.field_151008_G, 1), false);
            }

            if (!ok) {
                this.theStage = Stage.CANTWORK;
                ModSimReloaded.sendChat(this.theFolk.name + I18n.func_135052_a("container.sim.job.livestock.farmer.chests"));
            } else {
                GameStates var10000 = ModSimReloaded.states;
                var10000.credits -= 0.02F * (float)quant;
                this.theStage = Stage.WAITINGFORMATUREANIMAL;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageSlaughtering出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageCantWork() {
        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.meat");
    }

    private void procreate(EntityAnimal parentAnimal, V3 pos) {
        try {
            EntityAgeable babyAnimal = parentAnimal.func_90011_a(parentAnimal);
            Random rand = new Random();
            if (babyAnimal != null) {
                parentAnimal.func_90011_a(babyAnimal);
                babyAnimal.func_70873_a(-3000);
                babyAnimal.func_70012_b(parentAnimal.field_70165_t, parentAnimal.field_70163_u, parentAnimal.field_70161_v, parentAnimal.field_70177_z, parentAnimal.field_70125_A);

                for(int var3 = 0; var3 < 7; ++var3) {
                    double d = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                    this.mc.field_71441_e.func_175688_a(EnumParticleTypes.HEART, pos.x + (double)(rand.nextFloat() * 1.0F * 2.0F) - 1.0, pos.y + 0.5D + (double)(rand.nextFloat() * 1.0F), pos.z + (double)(rand.nextFloat() * 1.0F * 2.0F) - 1.0, d, d1, d2);
                }

                parentAnimal.field_70170_p.func_72838_d(babyAnimal);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("procreate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void spawnAnimals(V3 controlBox, String animal, int count) {
        try {
            EntityAnimal newAnimal = null;

            for(int c = 1; c <= count; ++c) {
                if (animal.contentEquals("Pig")) {
                    newAnimal = new EntityPig(this.jobWorld);
                } else if (animal.contentEquals("Cow")) {
                    newAnimal = new EntityCow(this.jobWorld);
                } else if (animal.contentEquals("Chicken")) {
                    newAnimal = new EntityChicken(this.jobWorld);
                }

                ((EntityAnimal)newAnimal).func_70012_b(controlBox.x, controlBox.y + 1.0, controlBox.z, 0.0F, 0.0F);
                if (!this.jobWorld.field_72995_K) {
                    this.jobWorld.func_72838_d((Entity)newAnimal);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("spawnAnimals出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.livestock.farmer.Arrived");
                this.theStage = Stage.ARRIVEDATFARM;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}

