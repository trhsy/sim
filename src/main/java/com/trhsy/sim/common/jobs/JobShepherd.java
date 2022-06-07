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
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private transient ArrayList<IInventory> farmChests = new ArrayList();
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
            this.theStage = Stage.IDLE;
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
        this.theFolk.statusText = I18n.format("container.sim.job.shepherd.farmer.Sharpening");
        this.theFolk.stayPut = false;
        List list = this.jobWorld.getEntitiesWithinAABB(EntitySheep.class, new AxisAlignedBB(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.x + 1, this.theFolk.employedAt.y + 1, this.theFolk.employedAt.z + 1).expand(3, 2, 3));
        Double playerdist = this.mc.thePlayer.getDistance(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z);
        int s;
        if (playerdist > 60) {
            try {
                this.sheepToShear = (EntitySheep)list.get(0);
                this.sheepToShear.setSheared(false);
            } catch (Exception var6) {
                this.placeWoolIntoAChest(1, 1);
            }
        } else if (list.size() > 0) {
            try {
                s = rand.nextInt(list.size() - 1);
                this.sheepToShear = (EntitySheep)list.get(s);
                this.sheepToShear.setSheared(false);
            } catch (Exception var5) {
            }
        }

        for(s = 0; s < list.size(); ++s) {
            this.sheepToShear = (EntitySheep)list.get(s);
            if (!this.sheepToShear.getSheared()) {
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
                this.theFolk.gotoXYZ(new V3(this.sheepToShear.posX, this.sheepToShear.posY, this.sheepToShear.posZ, this.theFolk.employedAt.theDimension), GotoMethod.WALK);
            }

            this.step = 2;
        } else if (this.step == 2) {
            this.step = 3;
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.faceEntity(this.sheepToShear, 1.0F, 1.0F);
            }
        } else if (this.step == 3) {
            this.theFolk.statusText = I18n.format("container.sim.job.shepherd.farmer.Shearing") + FolkData.generateName(0, true, "") + I18n.format("container.sim.job.shepherd.farmer.sheep");
            this.sheepToShear.setSheared(true);
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.faceEntity(this.sheepToShear, 1.0F, 1.0F);
                this.theFolk.isWorking = true;
                this.mc.theWorld.playSound(this.theFolk.theEntity.posX, this.theFolk.theEntity.posY, this.theFolk.theEntity.posZ, ModSim.MODID + ":shears", 1.0F, 1.0F, false);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isShearing = true;

                        for(int d = 0; d < 12; ++d) {
                            spawnExplosionParticle(sheepToShear);

                            try {
                                Thread.sleep(150L);
                            } catch (Exception var3) {
                            }
                        }

                        isShearing = false;
                        theFolk.statusText = I18n.format("container.sim.job.shepherd.farmer.Watching");
                    }
                });
                t.start();
                this.theFolk.theEntity.faceEntity(this.sheepToShear, 1.0F, 1.0F);
                this.step = 4;
            }
        } else if (this.step == 4) {
            Random ra = new Random();
            int count = ra.nextInt(3) + 1;
            this.placeWoolIntoAChest(this.sheepToShear.getFleeceColor(), count);
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
                ModSim.proxy.getClientWorld().spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, ent.posX + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1 - var2 * var8, ent.posY + (double) (rand.nextFloat() * 1.0F) - var4 * var8, ent.posZ + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1 - var6 * var8, var2, var4, var6);
            } catch (Exception var13) {
            }
        }

    }

    private void stageCantWork() {
        this.theFolk.statusText = I18n.format("container.sim.job.shepherd.farmer.chests");
        this.theFolk.isWorking = false;
    }

    private boolean placeWoolIntoAChest(int metaColor, int amount) {
        GameStates var10000 = ModSimReloaded.states;
        var10000.credits -= 0.02F * (float)amount;
        this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
        this.inventoriesPut(this.farmChests, new ItemStack(Blocks.wool, amount, metaColor), true);
        return true;
    }

    public void spawnSheepIfNeeded(V3 controlBox) {
        List list = this.jobWorld.getEntitiesWithinAABB(EntitySheep.class, new AxisAlignedBB(controlBox.x, controlBox.y, controlBox.z, controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z + 1.0).expand(3.0, 2.0, 3.0));
        Random ra = new Random();
        EntitySheep sheep;
        if (list.size() > 0 && list.size() < 6) {
            for(int fuck = 0; fuck < 6 - list.size(); ++fuck) {
                sheep = new EntitySheep(this.jobWorld);
                sheep.setLocationAndAngles(controlBox.x, controlBox.y + 1.0, controlBox.z, 0.0F, 0.0F);
                sheep.setFleeceColor(ra.nextInt(12) + 1);
                this.jobWorld.spawnEntityInWorld(sheep);
            }
        } else if (list.size() == 0) {
            sheep = new EntitySheep(this.jobWorld);
            sheep.setLocationAndAngles(controlBox.x - 1.0, controlBox.y + 1.0, controlBox.z - 1.0, 0.0F, 0.0F);
            sheep.setFleeceColor(ra.nextInt(12) + 1);
            this.jobWorld.spawnEntityInWorld(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.setLocationAndAngles(controlBox.x, controlBox.y + 1.0, controlBox.z - 1.0, 0.0F, 0.0F);
            sheep.setFleeceColor(ra.nextInt(12) + 1);
            this.jobWorld.spawnEntityInWorld(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.setLocationAndAngles(controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z - 1.0, 0.0F, 0.0F);
            sheep.setFleeceColor(ra.nextInt(12) + 1);
            this.jobWorld.spawnEntityInWorld(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.setLocationAndAngles(controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z, 0.0F, 0.0F);
            sheep.setFleeceColor(ra.nextInt(12) + 1);
            this.jobWorld.spawnEntityInWorld(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.setLocationAndAngles(controlBox.x + 1.0, controlBox.y + 1.0, controlBox.z + 1.0, 0.0F, 0.0F);
            sheep.setFleeceColor(ra.nextInt(12) + 1);
            this.jobWorld.spawnEntityInWorld(sheep);
            sheep = new EntitySheep(this.jobWorld);
            sheep.setLocationAndAngles(controlBox.x + 2.0, controlBox.y + 1.0, controlBox.z + 2.0, 0.0F, 0.0F);
            this.jobWorld.spawnEntityInWorld(sheep);
        }

    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.format("container.sim.job.shepherd.farmer.Arrived");
            this.theStage = Stage.ARRIVEDATFARM;
            this.spawnSheepIfNeeded(this.theFolk.employedAt);
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.BEAM);
        }

    }


}

