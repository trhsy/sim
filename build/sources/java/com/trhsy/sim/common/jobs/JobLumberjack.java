package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ========================================
 *
 * @ClassName JobLumberjack
 * @Description todo 伐木工人
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:51
 * ========================================
 **/
public class JobLumberjack extends Job implements Serializable {
    private static final long serialVersionUID = -1177112207904887741L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient ArrayList<IInventory> millChests = new ArrayList();
    private transient V3 foundWoodAt = new V3();
    private transient Building lumbermill = null;
    private transient long startedGoing = 0L;
    public transient boolean isChopping = false;
    private transient float pay = 0.0F;
    private transient boolean onRoute = false;

    public JobLumberjack() {
    }

    public JobLumberjack(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
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
        if (!ModSim.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage == Stage.IDLE && ModSim.isDayTime()) {
                this.theStage = Stage.SCANFORTREE;
            } else if (this.theStage == Stage.ARRIVEDATMILL) {
                this.theStage = Stage.SCANFORTREE;
            } else if (this.theStage == Stage.SCANFORTREE) {
                this.stageScanForTree();
            } else if (this.theStage == Stage.GOTOTREE) {
                this.pickUpSaplings();
                this.stageGotoTree();
            } else if (this.theStage == Stage.CHOPPINGTREE) {
                this.stageChoppingTree();
                this.pickUpSaplings();
            } else if (this.theStage == Stage.RETURNWOOD) {
                this.stageReturnWood();
                this.pickUpSaplings();
            }

        }
    }

    private void stageScanForTree() {
        this.theFolk.action = FolkAction.ATWORK;
        this.theFolk.isWorking = false;
        V3 searchXYZ = null;
        this.lumbermill = Building.getBuilding(this.theFolk.employedAt);
        V3 ts = null;

        try {
            if (this.lumbermill.lumbermillMarker != null) {
                searchXYZ = this.lumbermill.lumbermillMarker;
            } else if (this.theFolk.employedAt != null) {
                searchXYZ = this.theFolk.employedAt.clone();
            } else {
                searchXYZ = this.theFolk.location.clone();
            }

            ts = searchXYZ.clone();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        V3 searchpos;
        if (ts != null) {
            searchpos = ts.clone();
        } else {
            searchpos = this.theFolk.location.clone();
        }

        try {
            this.foundWoodAt = findClosestBlockType(searchpos, Blocks.log, ModSim.configLumberArea, false);
            this.foundWoodAt.theDimension = this.jobWorld.provider.dimensionId;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        this.theStage = Stage.GOTOTREE;
        this.onRoute = false;
        if (this.foundWoodAt == null) {
            ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.lumberjack.farmer.wood"));
            this.theFolk.selfFire();
        }
    }

    private void stageGotoTree() {
        this.theFolk.isWorking = false;
        if (!this.onRoute) {
            this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Going");
            this.theFolk.gotoXYZ(this.foundWoodAt, (GotoMethod)null);
            this.startedGoing = System.currentTimeMillis();
            this.onRoute = true;
        } else {
            if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                this.theFolk.updateLocationFromEntity();
            }

            double dist = (double)this.theFolk.location.getDistanceTo(this.foundWoodAt);
            if (dist < 7.0D) {
                this.theStage = Stage.CHOPPINGTREE;
                this.theFolk.stayPut = true;
                this.step = 1;
            } else {
                if (this.theFolk.destination == null && this.theFolk.theEntity != null) {
                }

                if (System.currentTimeMillis() - this.startedGoing > 25000L) {
                    this.theStage = Stage.CHOPPINGTREE;
                    this.theFolk.stayPut = true;
                    this.theFolk.destination = null;
                    this.step = 1;
                }
            }
        }

    }

    private void stageChoppingTree() {
        int i;
        int l;
        if (this.step == 1) {
            this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Choppy");
            this.theFolk.isWorking = true;

            for(i = 0; i < 20; ++i) {
                l = this.foundWoodAt.x.intValue();
                int y = this.foundWoodAt.y.intValue() - 1;
                int z = this.foundWoodAt.z.intValue();
                if (this.jobWorld == null) {
                    this.theFolk.selfFire();
                    return;
                }

                if (this.jobWorld.getBlock(l, y, z) != Blocks.log) {
                    break;
                }

                this.foundWoodAt.y = (double)y;
            }

            this.step = 2;
        } else if (this.step == 2) {
            if (this.jobWorld.getBlock(this.foundWoodAt.x.intValue(), this.foundWoodAt.y.intValue(), this.foundWoodAt.z.intValue()) == Blocks.log) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isChopping = true;

                        for(int d = 0; d < 12; ++d) {
                            try {
                                mc.theWorld.playSound(theFolk.location.x, theFolk.location.y, theFolk.location.z, "step.wood", 1.0F, 1.0F, false);
                            } catch (Exception var5) {
                            }

                            if (theFolk.theEntity != null) {
                                theFolk.theEntity.swingProgress = 0.3F;

                                try {
                                    Thread.sleep(100L);
                                } catch (Exception var4) {
                                }

                                theFolk.theEntity.swingProgress = 0.7F;

                                try {
                                    Thread.sleep(100L);
                                } catch (Exception var3) {
                                }
                            }
                        }

                        isChopping = false;
                    }
                });
                t.start();
                this.step = 3;
            } else {
                this.step = 4;
            }
        } else {
            int count;
            if (this.step == 3) {
                if (this.isChopping) {
                    return;
                }

                ArrayList<ItemStack> log = this.translateBlockWhenMined(this.jobWorld, this.foundWoodAt);
                this.jobWorld.setBlock(this.foundWoodAt.x.intValue(), this.foundWoodAt.y.intValue(), this.foundWoodAt.z.intValue(), Blocks.air, 0, 3);
                if (log != null) {
                    for(l = 0; l < log.size(); ++l) {
                        ItemStack isl = (ItemStack)log.get(l);
                        this.theFolk.inventory.add(isl);
                    }
                }

                count = this.getInventoryCount(this.theFolk, Blocks.log);
                this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Got") + count + I18n.format("container.sim.job.lumberjack.farmer.logs_so_far");
                this.theFolk.stayPut = false;
                this.foundWoodAt.y = this.foundWoodAt.y + 1.0D;
                this.step = 2;
            } else if (this.step == 4) {
                if (this.theFolk.isSpawned()) {
                    count = this.getInventoryCount(this.theFolk, Blocks.sapling);
                    if (count > 0) {
                        for(i = 0; i < this.theFolk.inventory.size(); ++i) {
                            ItemStack fis = (ItemStack)this.theFolk.inventory.get(i);
                            if (fis != null && Block.getBlockFromItem(fis.getItem()) == Blocks.sapling) {
                                this.theFolk.inventory.remove(i);
                                this.plantSapling(Block.getBlockFromItem(fis.getItem()));
                                break;
                            }
                        }
                    }
                } else {
                    this.plantSapling(Blocks.sapling);
                }

                count = this.getInventoryCount(this.theFolk, Blocks.log);
                if (count < 12) {
                    this.theStage = Stage.SCANFORTREE;
                } else {
                    this.theStage = Stage.RETURNWOOD;
                    this.step = 1;
                }
            }
        }

    }

    private void stageReturnWood() {
        this.theFolk.isWorking = false;
        if (this.step == 1) {
            this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Delivering");
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            this.step = 2;
        } else {
            int dist;
            if (this.step == 2) {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist <= 1) {
                    this.step = 3;
                } else if (this.theFolk.destination == null && this.theFolk.theEntity != null) {
                }
            } else if (this.step == 3) {
                this.theFolk.stayPut = true;
                dist = this.getInventoryCount(this.theFolk, Blocks.log);
                this.millChests = inventoriesFindClosest(this.theFolk.employedAt, 6);
                this.inventoriesTransferFromFolk(this.theFolk.inventory, this.millChests, new ItemStack(Blocks.log));
                this.pay = (float)dist * 0.03F;
                GameStates var10000 = ModSim.states;
                var10000.credits -= this.pay;
                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.lumberjack.farmer.delivered") + dist + I18n.format("container.sim.job.lumberjack.farmer.lumbermill"));
                this.theStage = Stage.SCANFORTREE;
                this.step = 1;
            }
        }

    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.a_lumberjack");
            this.theStage = Stage.ARRIVEDATMILL;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    private void pickUpSaplings() {
        if (this.theFolk.isSpawned()) {
            List list1 = this.jobWorld.getEntitiesWithinAABBExcludingEntity(this.theFolk.theEntity, AxisAlignedBB.getBoundingBox(this.theFolk.theEntity.posX, this.theFolk.theEntity.posY, this.theFolk.theEntity.posZ, this.theFolk.theEntity.posX + 1.0D, this.theFolk.theEntity.posY + 1.0D, this.theFolk.theEntity.posZ + 1.0D).expand(3.0D, 4.0D, 3.0D));
            Iterator iterator1 = list1.iterator();
            if (!list1.isEmpty()) {
                while(iterator1.hasNext()) {
                    Entity entity1 = (Entity)iterator1.next();
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem)entity1;
                        ItemStack is = entityitem.getEntityItem();

                        try {
                            Item ID = is.getItem();
                            if (ID == Item.getItemFromBlock(Blocks.sapling)) {
                                this.theFolk.inventory.add(new ItemStack(Blocks.sapling, is.getMetadata(), 1));
                                entityitem.setDead();
                            }
                        } catch (Exception var7) {
                        }
                    }
                }
            }

        }
    }

    private void plantSapling(Block is) {
        if (this.theFolk.isSpawned()) {
            if (this.jobWorld.getBlock((int)this.theFolk.theEntity.posX, (int)this.theFolk.theEntity.posY, (int)this.theFolk.theEntity.posZ) == null) {
                this.jobWorld.setBlock((int)this.theFolk.theEntity.posX, (int)this.theFolk.theEntity.posY, (int)this.theFolk.theEntity.posZ, is);
            }
        } else {
            this.jobWorld.setBlock(this.theFolk.location.x.intValue(), this.theFolk.location.y.intValue(), this.theFolk.location.z.intValue(), Blocks.sapling, 0, 3);
        }

    }

}

