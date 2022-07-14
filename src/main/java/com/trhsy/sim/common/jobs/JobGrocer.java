package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.functionality.FarmingBox;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobGrocer
 * @Description todo 杂货商
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:50
 * ========================================
 **/
public class JobGrocer extends Job implements Serializable {
    private static final long serialVersionUID = -1177119265904279141L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient float pay = 0.0F;
    private transient ArrayList<IInventory> grocerChests = new ArrayList();
    private transient ArrayList<IInventory> farmChests = new ArrayList();
    private transient int currentFarmNum = 0;
    private transient FarmingBox farm = null;
    private transient boolean onRoute = false;

    public JobGrocer() {
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    public JobGrocer(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
                }

            }
        }catch (Exception e){

        }

    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            if (!ModSimReloaded.isDayTime()) {
                this.theStage = Stage.IDLE;
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.ARRIVEDATSHOP) {
                this.runDelay = 10000;
                this.theFolk.action = FolkAction.ATWORK;
            }

            if (this.theStage == Stage.COLLECTINGFOOD) {
                this.runDelay = 4000;
            }

            if (this.theStage == Stage.SELLINGFOOD) {
                this.runDelay = 10000;
                this.theFolk.action = FolkAction.ATWORK;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATSHOP) {
                        this.stageArrived();
                    } else if (this.theStage == Stage.GOINGTOFOODFARM) {
                        this.stageGoingToFoodFarm();
                    } else if (this.theStage == Stage.COLLECTINGFOOD) {
                        this.stageCollectingFood();
                    } else if (this.theStage == Stage.GOBACKTOSTORE) {
                        this.stageGoBackToStore();
                    } else if (this.theStage == Stage.SELLINGFOOD) {
                        this.stageSellingFood();
                    }
                }

            }
        }catch (Exception e){

        }

    }

    private void stageArrived() {
        try {
            this.currentFarmNum = 0;
            this.theFolk.stayPut = true;
            this.theStage = Stage.GOINGTOFOODFARM;
        }catch (Exception e){

        }

    }

    private void stageGoingToFoodFarm() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Fetching");
            if (!this.onRoute) {
                this.farm = this.getCurrentFarm();
                if (this.farm == null) {
                    this.theStage = Stage.GOBACKTOSTORE;
                } else {
                    this.onRoute = true;
                    this.theFolk.gotoXYZ(this.farm.getLocation(), (GotoMethod)null);
                }
            } else {
                double dist = 0;

                try {
                    if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                        this.theFolk.updateLocationFromEntity();
                    }

                    dist = (double)this.theFolk.location.getDistanceTo(this.farm.location);
                } catch (Exception var4) {
                    this.theStage = Stage.GOBACKTOSTORE;
                }

                if (dist < 3) {
                    if (this.theFolk.theEntity != null) {
                        this.theFolk.theEntity.motionX = 0;
                        this.theFolk.theEntity.motionZ = 0;
                    }

                    this.onRoute = false;
                    this.theStage = Stage.COLLECTINGFOOD;
                    this.step = 1;
                    this.theFolk.stayPut = true;
                    return;
                }

                if (this.theFolk.destination == null) {
                    this.onRoute = false;
                }
            }
        }catch (Exception e){

        }


    }

    private void stageCollectingFood() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Collecting");
            if (this.step == 1) {
                if (this.farm == null) {
                    this.theStage = Stage.GOINGTOFOODFARM;
                    return;
                }

                this.farmChests = inventoriesFindClosest(this.farm.getLocation(), 5);
                if (this.farmChests.size() > 0) {
                    this.theFolk.stayPut = true;
                    ((IInventory)this.farmChests.get(0)).openChest();
                    this.step = 2;
                }
            } else if (this.step == 2) {
                this.inventoriesTransferToFolk(this.theFolk.inventory, this.farmChests, new ItemStack(Items.melon, 640), Blocks.dirt);
                this.inventoriesTransferToFolk(this.theFolk.inventory, this.farmChests, new ItemStack(Blocks.pumpkin, 640), (Block)null);
                this.inventoriesTransferToFolk(this.theFolk.inventory, this.farmChests, new ItemStack(Items.carrot, 640), (Block)null);
                this.inventoriesTransferToFolk(this.theFolk.inventory, this.farmChests, new ItemStack(Items.potato, 640), (Block)null);
                this.step = 3;
            } else if (this.step == 3) {
                ((IInventory)this.farmChests.get(0)).closeChest();
                this.theStage = Stage.GOINGTOFOODFARM;
            }
        }catch (Exception e){

        }


    }

    private void stageGoBackToStore() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Taking");
            if (!this.onRoute) {
                this.onRoute = true;
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            } else {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist <= 1) {
                    if (this.theFolk.theEntity != null) {
                        this.theFolk.theEntity.motionX = 0;
                        this.theFolk.theEntity.motionZ = 0;
                    }

                    this.onRoute = false;
                    this.theStage = Stage.SELLINGFOOD;
                    this.step = 1;
                    this.theFolk.stayPut = true;
                    return;
                }

                if (this.theFolk.destination == null) {
                    this.onRoute = false;
                }
            }
        }catch (Exception e){

        }


    }

    private void stageSellingFood() {
        try {
            this.grocerChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            int sell;
            int f;
            int c;
            if (this.step == 1) {
                this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Unloading");
                sell = this.getInventoryCount(this.theFolk, Blocks.pumpkin);
                int melons = this.getInventoryCount(this.theFolk, Items.melon);
                f = this.getInventoryCount(this.theFolk, Items.carrot);
                c = this.getInventoryCount(this.theFolk, Items.potato);
                this.pay = (float)((double)sell * 0.2D);
                this.pay += (float)((double)melons * 0.05D);
                this.pay += (float)((double)f * 0.05D);
                this.pay += (float)((double)c * 0.05D);
                this.inventoriesTransferFromFolk(this.theFolk.inventory, this.grocerChests, (ItemStack)null);
                GameStates var10000 = ModSimReloaded.states;
                var10000.credits -= this.pay;
                this.step = 2;
            } else if (this.step == 2) {
                this.theFolk.updateLocationFromEntity();
                sell = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (sell > 2 && this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
                }

                this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Selling");
                if (MinecraftServer.getServer().worldServers[0].getWorldTime() % 24000L > 11600L) {
                    this.step = 3;
                }
            } else if (this.step == 3) {
                this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Closing");
                sell = 0;
                ItemStack piece = null;

                label58:
                for (f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                    for (c = 0; c < this.grocerChests.size(); ++c) {
                        IInventory chest = (IInventory) this.grocerChests.get(c);
                        int g = 0;

                        while (g < chest.getSizeInventory()) {
                            ItemStack chestStack = chest.getStackInSlot(g);

                            try {
                                int count = (new Random()).nextInt(3) + 1;
                                ItemFood food = (ItemFood) chestStack.getItem();
                                piece = inventoriesGet(this.grocerChests, new ItemStack(chestStack.getItem(), count), false, false);
                                FolkData folk = (FolkData) ModSimReloaded.theFolks.get(f);
                                folk.levelFood = 10;
                                sell += count;
                                continue label58;
                            } catch (Exception var11) {
                                ++g;
                            }
                        }
                    }
                }

                if (sell > 0) {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.grocer.farmer.grocer") + sell + I18n.format("container.sim.job.grocer.farmer.folks"));
                } else {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.grocer.farmer.today"));
                }

                this.step = 4;
            } else if (this.step == 4) {
            }
        }catch (Exception e){

        }


    }

    private FarmingBox getCurrentFarm() {
        boolean found = false;
        FarmingBox farm = null;
        try {
            while(true) {
                if (!found) {
                    try {
                        farm = (FarmingBox) ModSimReloaded.theFarmingBoxes.get(this.currentFarmNum);
                    } catch (Exception var4) {
                        return null;
                    }

                    if (farm == null) {
                        return null;
                    }

                    if (farm.farmType != FarmType.MELON && farm.farmType != FarmType.PUMPKIN && farm.farmType != FarmType.CARROT && farm.farmType != FarmType.POTATO) {
                        ++this.currentFarmNum;
                        if (this.currentFarmNum > ModSimReloaded.theFarmingBoxes.size() - 1) {
                            return null;
                        }
                        continue;
                    }

                    found = true;
                    ++this.currentFarmNum;
                    return farm;
                }

                return null;
            }
        }catch (Exception e){

        }
        return null;
    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.Arrived_at_the_store");
                this.theStage = Stage.ARRIVEDATSHOP;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }
        }catch (Exception e){

        }


    }


}

