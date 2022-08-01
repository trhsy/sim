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
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
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
 * @ClassName JobShepherd
 * @Description todo 牧羊人
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:53
 * ========================================
 **/
public class JobShepherd extends Job implements Serializable {
    private static final long serialVersionUID = -1177112207904191941L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient CopyOnWriteArrayList<IInventory> farmChests = new CopyOnWriteArrayList();
    private transient EntitySheep sheepToShear = null;
    private transient boolean isShearing = false;

    public JobShepherd() {
    }

    public JobShepherd(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.BEAM);
            }

        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
        this.theFolk.isWorking = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!ModSimReloaded.isDayTime()) {
            if (!theFolk.isNightOwl()) {
                //闲置
                this.theStage = Stage.IDLE;
                return;
            }
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.WAITINGFORWOOL) {
            this.runDelay = 15000;
        } else {
            this.runDelay = 2000;
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                if (this.theStage == Stage.ARRIVEDATFARM) {
                    this.theStage = Stage.WAITINGFORWOOL;
                } else if (this.theStage == Stage.WAITINGFORWOOL) {
                    this.stageWaiting();
                } else if (this.theStage == Stage.SHEARING) {
                    this.stageShearing();
                } else if (this.theStage == Stage.CANTWORK) {
                    this.stageCantWork();
                }
            }

        }
    }

    private void stageWaiting() {
        Random rand = new Random();
        this.theFolk.isWorking = false;
        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.shepherd.farmer.Sharpening");
        this.theFolk.stayPut = false;
        List list = this.jobWorld.func_72872_a(EntitySheep.class, new AxisAlignedBB(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.x + 1, this.theFolk.employedAt.y + 1, this.theFolk.employedAt.z + 1).func_72314_b(3, 2, 3));
        Double playerdist = this.mc.field_71439_g.func_70011_f(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z);
        int s;
        if (playerdist > 60) {
            try {
                this.sheepToShear = (EntitySheep)list.get(0);
                this.sheepToShear.func_70893_e(false);
            } catch (Exception e) {
                this.placeWoolIntoAChest(1, 1);
            }
        } else if (list.size() > 0) {
            try {
                s = rand.nextInt(list.size() - 1);
                this.sheepToShear = (EntitySheep)list.get(s);
                this.sheepToShear.func_70893_e(false);
            } catch (Exception e) {
            }
        }

        for(s = 0; s < list.size(); ++s) {
            this.sheepToShear = (EntitySheep)list.get(s);
            if (!this.sheepToShear.func_70892_o()) {
                this.theStage = Stage.SHEARING;
                this.step = 1;
                break;
            }
        }

    }

    private void stageShearing() {
        this.theFolk.stayPut = false;
        if (this.step == 1) {
            if (this.theFolk.getDistanceToPlayer() < 50) {
                this.theFolk.gotoXYZ(new V3(this.sheepToShear.field_70165_t, this.sheepToShear.field_70163_u, this.sheepToShear.field_70161_v, this.theFolk.employedAt.theDimension), GotoMethod.WALK);
            }

            this.step = 2;
        } else if (this.step == 2) {
            this.step = 3;
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.func_70625_a(this.sheepToShear, 1.0F, 1.0F);
            }
        } else if (this.step == 3) {
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.shepherd.farmer.Shearing") + FolkData.generateName(0, true, "") + I18n.func_135052_a("container.sim.job.shepherd.farmer.sheep");
            this.sheepToShear.func_70893_e(true);
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.func_70625_a(this.sheepToShear, 1.0F, 1.0F);
                this.theFolk.isWorking = true;
                this.mc.field_71441_e.func_72980_b(this.theFolk.theEntity.field_70165_t, this.theFolk.theEntity.field_70163_u, this.theFolk.theEntity.field_70161_v, ModSim.MODID + ":shears", 1.0F, 1.0F, false);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isShearing = true;

                        for(int d = 0; d < 12; ++d) {
                            spawnExplosionParticle(sheepToShear);

                            try {
                                Thread.sleep(150L);
                            } catch (Exception e) {
                            }
                        }

                        isShearing = false;
                        theFolk.statusText = I18n.func_135052_a("container.sim.job.shepherd.farmer.Watching");
                    }
                });
                t.start();
                this.theFolk.theEntity.func_70625_a(this.sheepToShear, 1.0F, 1.0F);
                this.step = 4;
            }
        } else if (this.step == 4) {
            Random ra = new Random();
            int count = ra.nextInt(3) + 1;
            this.placeWoolIntoAChest(this.sheepToShear.func_175509_cj().func_176765_a(), count);
            this.theStage = Stage.WAITINGFORWOOL;
            this.theFolk.isWorking = false;
            this.step = 1;
        }

    }

    public void spawnExplosionParticle(Entity ent) {
        Random rand = new Random();

        for(int var1 = 0; var1 < 20; ++var1) {
            double var2 = rand.nextGaussian() * 0.02D;
            double var4 = rand.nextGaussian() * 0.02D;
            double var6 = rand.nextGaussian() * 0.02D;
            double var8 = 10;

            try {
                ModSim.proxy.getClientWorld().func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, ent.field_70165_t + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1 - var2 * var8, ent.field_70163_u + (double) (rand.nextFloat() * 1.0F) - var4 * var8, ent.field_70161_v + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1 - var6 * var8, var2, var4, var6);
            } catch (Exception e) {
            }
        }

    }

    private void stageCantWork() {
        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.shepherd.farmer.chests");
        this.theFolk.isWorking = false;
    }

    private boolean placeWoolIntoAChest(int metaColor, int amount) {
        GameStates var10000 = ModSimReloaded.states;
        var10000.credits -= 0.02F * (float)amount;
        this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
        this.inventoriesPut(this.farmChests, new ItemStack(Blocks.field_150325_L, amount, metaColor), true);
        return true;
    }

    public void spawnSheepIfNeeded(V3 controlBox) {
        List list = this.jobWorld.func_72872_a(EntitySheep.class, new AxisAlignedBB(controlBox.x, controlBox.y, controlBox.z, controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z + 1.0).func_72314_b(3.0, 2.0, 3.0));
        Random ra = new Random();
        EntitySheep sheep;
        if (list.size() > 0 && list.size() < 6) {
            for(int fuck = 0; fuck < 6 - list.size(); ++fuck) {
                sheep = new EntitySheep(this.jobWorld);
                sheep.func_70012_b(controlBox.x, controlBox.y + 1.0, controlBox.z, 0.0F, 0.0F);
                sheep.func_175512_b(EnumDyeColor.func_176764_b(ra.nextInt(12) + 1));
                this.jobWorld.func_72838_d(sheep);
            }
        } else if (list.size() == 0) {
            sheep = new EntitySheep(this.jobWorld);
            sheep.func_70012_b(controlBox.x - 1.0, controlBox.y + 1.0, controlBox.z - 1.0, 0.0F, 0.0F);
            sheep.func_175512_b(EnumDyeColor.func_176764_b(ra.nextInt(12) + 1));
            this.jobWorld.func_72838_d(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.func_70012_b(controlBox.x, controlBox.y + 1.0, controlBox.z - 1.0, 0.0F, 0.0F);
            sheep.func_175512_b(EnumDyeColor.func_176764_b(ra.nextInt(12) + 1));
            this.jobWorld.func_72838_d(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.func_70012_b(controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z - 1.0, 0.0F, 0.0F);
            sheep.func_175512_b(EnumDyeColor.func_176764_b(ra.nextInt(12) + 1));
            this.jobWorld.func_72838_d(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.func_70012_b(controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z, 0.0F, 0.0F);
            sheep.func_175512_b(EnumDyeColor.func_176764_b(ra.nextInt(12) + 1));
            this.jobWorld.func_72838_d(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.func_70012_b(controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z + 1.0, 0.0F, 0.0F);
            sheep.func_175512_b(EnumDyeColor.func_176764_b(ra.nextInt(12) + 1));
            this.jobWorld.func_72838_d(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.func_70012_b(controlBox.x + 2.0, controlBox.y + 1.0, controlBox.z + 2.0, 0.0F, 0.0F);
            this.jobWorld.func_72838_d(sheep);
        }

    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.shepherd.farmer.Arrived");
            this.theStage = Stage.ARRIVEDATFARM;
            this.spawnSheepIfNeeded(this.theFolk.employedAt);
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.BEAM);
        }

    }


}

