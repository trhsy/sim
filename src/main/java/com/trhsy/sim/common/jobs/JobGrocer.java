package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FarmingBox;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.block.Block;
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
 * @Description todo
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
    public void onUpdate() {
        super.onUpdate();
        if (!ModSimukraft.isDayTime()) {
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
            if (this.theStage != Stage.IDLE || !ModSimukraft.isDayTime()) {
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
    }

    private void stageArrived() {
        this.currentFarmNum = 0;
        this.theFolk.stayPut = true;
        this.theStage = Stage.GOINGTOFOODFARM;
    }

    private void stageGoingToFoodFarm() {
        this.theFolk.statusText = "Fetching fresh food from farms";
        if (!this.onRoute) {
            this.farm = this.getCurrentFarm();
            if (this.farm == null) {
                this.theStage = Stage.GOBACKTOSTORE;
            } else {
                this.onRoute = true;
                this.theFolk.gotoXYZ(this.farm.getLocation(), (GotoMethod)null);
            }
        } else {
            double dist = 0.0D;

            try {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                dist = (double)this.theFolk.location.getDistanceTo(this.farm.location);
            } catch (Exception var4) {
                this.theStage = Stage.GOBACKTOSTORE;
            }

            if (dist < 3.0D) {
                if (this.theFolk.theEntity != null) {
                    this.theFolk.theEntity.motionX = 0.0D;
                    this.theFolk.theEntity.motionZ = 0.0D;
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

    }

    private void stageCollectingFood() {
        this.theFolk.statusText = "Collecting Fresh Food";
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

    }

    private void stageGoBackToStore() {
        this.theFolk.statusText = "Taking fresh food back to store";
        if (!this.onRoute) {
            this.onRoute = true;
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        } else {
            if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                this.theFolk.updateLocationFromEntity();
            }

            double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1.0D) {
                if (this.theFolk.theEntity != null) {
                    this.theFolk.theEntity.motionX = 0.0D;
                    this.theFolk.theEntity.motionZ = 0.0D;
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

    }

    private void stageSellingFood() {
        this.grocerChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        int sell;
        int f;
        int c;
        if (this.step == 1) {
            this.theFolk.statusText = "Unloading fresh food";
            sell = this.getInventoryCount(this.theFolk, Blocks.pumpkin);
            int melons = this.getInventoryCount(this.theFolk, Items.melon);
            f = this.getInventoryCount(this.theFolk, Items.carrot);
            c = this.getInventoryCount(this.theFolk, Items.potato);
            this.pay = (float)((double)sell * 0.2D);
            this.pay += (float)((double)melons * 0.05D);
            this.pay += (float)((double)f * 0.05D);
            this.pay += (float)((double)c * 0.05D);
            this.inventoriesTransferFromFolk(this.theFolk.inventory, this.grocerChests, (ItemStack)null);
            GameStates var10000 = ModSimukraft.states;
            var10000.credits -= this.pay;
            this.step = 2;
        } else if (this.step == 2) {
            this.theFolk.updateLocationFromEntity();
            sell = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (sell > 2 && this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }

            this.theFolk.statusText = "Selling fresh food";
            if (MinecraftServer.getServer().worldServers[0].getWorldTime() % 24000L > 11600L) {
                this.step = 3;
            }
        } else if (this.step == 3) {
            this.theFolk.statusText = "Closing the shop";
            sell = 0;
            ItemStack piece = null;

            label58:
            for(f = 0; f < ModSimukraft.theFolks.size(); ++f) {
                for(c = 0; c < this.grocerChests.size(); ++c) {
                    IInventory chest = (IInventory)this.grocerChests.get(c);
                    int g = 0;

                    while(g < chest.getSizeInventory()) {
                        ItemStack chestStack = chest.getStackInSlot(g);

                        try {
                            int count = (new Random()).nextInt(3) + 1;
                            ItemFood food = (ItemFood)chestStack.getItem();
                            piece = inventoriesGet(this.grocerChests, new ItemStack(chestStack.getItem(), count), false, false);
                            FolkData folk = (FolkData)ModSimukraft.theFolks.get(f);
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
                ModSimukraft.sendChat(this.theFolk.name + "(grocer) has sold " + sell + " items of food to folks today.");
            } else {
                ModSimukraft.sendChat(this.theFolk.name + " has no produce to sell to folks today.");
            }

            this.step = 4;
        } else if (this.step == 4) {
        }

    }

    private FarmingBox getCurrentFarm() {
        boolean found = false;
        FarmingBox farm = null;

        while(true) {
            if (!found) {
                try {
                    farm = (FarmingBox)ModSimukraft.theFarmingBoxes.get(this.currentFarmNum);
                } catch (Exception var4) {
                    return null;
                }

                if (farm == null) {
                    return null;
                }

                if (farm.farmType != FarmType.MELON && farm.farmType != FarmType.PUMPKIN && farm.farmType != FarmType.CARROT && farm.farmType != FarmType.POTATO) {
                    ++this.currentFarmNum;
                    if (this.currentFarmNum > ModSimukraft.theFarmingBoxes.size() - 1) {
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
    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Arrived at the store";
            this.theStage = Stage.ARRIVEDATSHOP;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }


}

