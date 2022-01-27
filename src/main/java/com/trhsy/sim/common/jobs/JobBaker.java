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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobBaker
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:35
 * ========================================
 **/
public class JobBaker extends Job implements Serializable {
    private static final long serialVersionUID = -1177112153304279141L;
    public Vocation vocation = null;
    public Stage theStage;
    public FolkData theFolk;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient float pay = 0.0F;
    private transient ArrayList<IInventory> bakeryChests = null;
    private transient ArrayList<IInventory> farmChests = new ArrayList();
    private transient int currentFarmNum = 0;
    private transient FarmingBox farm = null;

    public JobBaker() {
    }

    public JobBaker(FolkData folk) {
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
        }

        if (this.theStage == Stage.COLLECTINGWHEAT) {
            this.runDelay = 1000;
        }

        if (this.theStage == Stage.SELLINGBREAD) {
            this.runDelay = 10000;
        }

        if (this.theStage == Stage.MAKEBREAD) {
            this.runDelay = 10000;
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage != Stage.IDLE || !ModSimukraft.isDayTime()) {
                if (this.theStage == Stage.ARRIVEDATSHOP) {
                    this.theStage = Stage.GOINGTOWHEATFARM;
                    this.step = 1;
                } else if (this.theStage == Stage.GOINGTOWHEATFARM) {
                    this.stageGoingToWheatFarm();
                } else if (this.theStage == Stage.COLLECTINGWHEAT) {
                    this.stageCollectingWheat();
                } else if (this.theStage == Stage.GOBACKTOBAKERY) {
                    this.stageGoBackToBakery();
                } else if (this.theStage == Stage.MAKEBREAD) {
                    this.stageMakeBread();
                } else if (this.theStage == Stage.SELLINGBREAD) {
                    this.stageSellingBread();
                }
            }

        }
    }

    private void stageGoingToWheatFarm() {
        this.theFolk.statusText = "Fetching wheat from farms";
        if (this.theFolk.destination == null && this.step == 1) {
            this.farm = this.getCurrentFarm();
            if (this.farm == null) {
                this.theStage = Stage.GOBACKTOBAKERY;
                this.step = 1;
            } else {
                this.theFolk.gotoXYZ(this.farm.getLocation(), (GotoMethod)null);
                this.runDelay = 1000;
                this.step = 2;
            }
        }

        if (this.step == 2) {
            double dist = 0.0D;
            if (this.farm != null) {
                this.runDelay = 1000;
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                dist = (double)this.theFolk.location.getDistanceTo(this.farm.getLocation());
                if (dist <= 1.0D) {
                    this.theStage = Stage.COLLECTINGWHEAT;
                    this.step = 1;
                    this.theFolk.stayPut = true;
                    if (this.theFolk.theEntity != null) {
                        this.theFolk.theEntity.field_70159_w = 0.0D;
                        this.theFolk.theEntity.field_70179_y = 0.0D;
                    }

                    this.runDelay = 1000;
                    return;
                }
            } else {
                this.theStage = Stage.GOBACKTOBAKERY;
            }
        }

    }

    private void stageCollectingWheat() {
        this.theFolk.statusText = "Collecting Wheat";
        this.runDelay = 1000;
        if (this.step == 1) {
            this.farmChests = inventoriesFindClosest(this.farm.getLocation(), 5);
            if (this.farmChests.size() > 0) {
                ((IInventory)this.farmChests.get(0)).func_70295_k_();
                this.step = 2;
            }
        } else if (this.step == 2) {
            this.farmChests = inventoriesFindClosest(this.farm.getLocation(), 5);
            this.inventoriesTransferToFolk(this.theFolk.inventory, this.farmChests, new ItemStack(Items.field_151015_O, 640), Blocks.field_150350_a);
            this.step = 3;
        } else if (this.step == 3) {
            ((IInventory)this.farmChests.get(0)).func_70305_f();
            this.theStage = Stage.GOINGTOWHEATFARM;
            this.step = 1;
        }

    }

    private void stageGoBackToBakery() {
        this.theFolk.statusText = "Taking wheat back to bakery";
        if (this.theFolk.destination == null && this.step == 1) {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            this.runDelay = 100;
            this.step = 2;
        }

        if (this.step == 2) {
            if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                this.theFolk.updateLocationFromEntity();
            }

            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theStage = Stage.MAKEBREAD;
                this.step = 1;
                this.theFolk.stayPut = true;
                return;
            }
        }

    }

    private void stageMakeBread() {
        this.theFolk.statusText = "Baking bread";
        this.bakeryChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
        if (this.bakeryChests != null && this.bakeryChests.size() != 0) {
            int wheat;
            if (this.step == 1) {
                this.theFolk.updateLocationFromEntity();
                wheat = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (wheat > 1) {
                    this.theFolk.beamMeTo(this.theFolk.employedAt);
                }

                this.step = 2;
            } else if (this.step == 2) {
                wheat = this.getInventoryCount(this.theFolk, Items.field_151015_O);
                int bread = (int)Math.floor((double)(wheat / 3));
                this.pay = (float)((double)bread * 0.2D);
                this.bakeryChests = inventoriesFindClosest(this.theFolk.employedAt, 4);

                try {
                    ((IInventory)this.bakeryChests.get(0)).func_70295_k_();
                } catch (Exception var4) {
                }

                this.inventoriesPut(this.bakeryChests, new ItemStack(Items.field_151025_P, bread), true);
                this.theFolk.inventory.clear();
                this.step = 3;
            } else if (this.step == 3) {
                ((IInventory)this.bakeryChests.get(0)).func_70305_f();
                this.theFolk.statusText = "Selling bread to customers";
                this.theFolk.stayPut = true;
                if (this.theFolk.theEntity != null) {
                    if (this.theFolk.gender == 0) {
                        this.mc.field_71441_e.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, "satscapesimukraft:bakerm", 1.0F, 1.0F, false);
                    } else {
                        this.mc.field_71441_e.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, "satscapesimukraft:bakerf", 1.0F, 1.0F, false);
                    }
                }

                this.theStage = Stage.SELLINGBREAD;
                this.step = 1;
            }

        } else {
            this.theFolk.statusText = "Who stole the chest from my bakery!!";
        }
    }

    private void stageSellingBread() {
        if (this.step == 1) {
            if (this.pay > 0.0F) {
                GameStates var10000 = ModSimukraft.states;
                var10000.credits -= this.pay;
                ModSimukraft.sendChat(this.theFolk.name + " has made some bread and has been paid " + ModSimukraft.displayMoney(this.pay) + " Sim-u-credits.");
                this.mc.field_71441_e.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, "satscapesimukraft:cash", 1.0F, 1.0F, false);
            }

            this.step = 2;
        } else if (this.step == 2) {
            if (MinecraftServer.getServer().worldServers[0].func_72820_D() % 24000L > 11600L) {
                this.step = 3;
            }
        } else if (this.step == 3) {
            this.theFolk.statusText = "Closing the shop";
            int sell = false;
            ItemStack breadStack = null;
            if (ModSimukraft.theFolks.size() > 1) {
                int sell = ModSimukraft.theFolks.size() + 1 + (new Random()).nextInt(ModSimukraft.theFolks.size());
                this.bakeryChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
                breadStack = inventoriesGet(this.bakeryChests, new ItemStack(Items.field_151025_P, sell), false, false);
            }

            if (breadStack == null) {
                ModSimukraft.sendChat(this.theFolk.name + " did not have any bread to sell today, do you have an active wheat farm?");
            } else {
                ModSimukraft.sendChat(this.theFolk.name + " has sold " + breadStack.field_77994_a + " loafs of bread to folks today.");

                for(int f = 0; f < ModSimukraft.theFolks.size(); ++f) {
                    FolkData folk = (FolkData)ModSimukraft.theFolks.get(f);
                    if (breadStack.field_77994_a > 0) {
                        folk.levelFood = 10;
                        --breadStack.field_77994_a;
                    }
                }
            }

            this.step = 4;
        } else if (this.step == 4) {
        }

    }

    private FarmingBox getCurrentFarm() {
        boolean found = false;

        while(!found) {
            try {
                FarmingBox farm = (FarmingBox)ModSimukraft.theFarmingBoxes.get(this.currentFarmNum);
                if (farm.farmType == FarmType.WHEAT) {
                    found = true;
                    ++this.currentFarmNum;
                    return farm;
                }

                ++this.currentFarmNum;
                if (this.currentFarmNum > ModSimukraft.theFarmingBoxes.size() - 1) {
                    return null;
                }
            } catch (Exception var4) {
                return null;
            }
        }

        return null;
    }

    @Override
    public void onArrivedAtWork() {
        int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Arrived at the Bakery";
            this.theStage = Stage.ARRIVEDATSHOP;
            this.currentFarmNum = 0;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

}

