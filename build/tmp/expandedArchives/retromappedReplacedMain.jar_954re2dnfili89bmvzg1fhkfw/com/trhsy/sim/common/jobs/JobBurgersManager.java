package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ItemLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName JobBurgersManager
 * @Description todo 漢堡店经理
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:43
 * ========================================
 **/
public class JobBurgersManager extends Job {

    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public int runDelay = 1000;
    private long timeSinceLastRun = 0L;
    private Building theStore = null;
    private int currentPickup = 0;
    private ArrayList<Building> pickupBuildings = new ArrayList();

    public JobBurgersManager(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod) null);
            }

        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.theStore == null) {
            this.theStore = Building.getBuilding(this.theFolk.employedAt);
        }

        if (this.theStore != null) {
            if (!ModSimReloaded.isDayTime()) {
                this.theStage = Stage.IDLE;
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.ARRIVEDATSTORE) {
                this.theFolk.action = FolkAction.ATWORK;
                this.runDelay = 11000;
            } else if (this.theStage == Stage.HANGINGOUT) {
                this.runDelay = 30000;
            } else {
                this.runDelay = 5000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long) this.runDelay) {
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATSTORE) {
                        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.manager.Checking");
                        this.theStage = Stage.PICKUPBAKERY;
                        this.step = 1;
                    } else if (this.theStage == Stage.PICKUPBAKERY) {
                        this.stagePickupBakery();
                    } else if (this.theStage == Stage.PICKUPGROCERY) {
                        this.stagePickupGrocery();
                    } else if (this.theStage == Stage.PICKUPCHEESE) {
                        this.stagePickupCheese();
                    } else if (this.theStage == Stage.PICKUPBUTCHERS) {
                        this.stagePickupButchers();
                    } else if (this.theStage == Stage.DROPOFF) {
                        this.stageDropoff();
                    } else if (this.theStage == Stage.HANGINGOUT) {
                        this.stageHangingOut();
                    }
                }

                if (!ModSimReloaded.isDayTime()) {
                    this.theStage = Stage.IDLE;
                }

                this.timeSinceLastRun = System.currentTimeMillis();
            }
        }
    }

    private void doPickup(String buildingSearch, Block pickUpItem, boolean doCompareMeta) {
        if (this.step == 1) {
            this.currentPickup = 0;
            this.pickupBuildings.clear();
            this.pickupBuildings = Building.getBuildingBySearch(buildingSearch, true);
            if (this.pickupBuildings.size() == 0) {
                this.step = 4;
                return;
            }

            this.theFolk.gotoXYZ(((Building) this.pickupBuildings.get(this.currentPickup)).primaryXYZ, (GotoMethod) null);
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.manager.On_my") + ((Building) this.pickupBuildings.get(this.currentPickup)).displayName;
            this.step = 2;
        } else if (this.step == 2) {
            if (this.theFolk.destination == null) {
                this.step = 3;
            }
        } else if (this.step == 3) {
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.manager.Buying") + ((Building) this.pickupBuildings.get(this.currentPickup)).displayName;
            ArrayList<IInventory> chests = inventoriesFindClosest(((Building) this.pickupBuildings.get(this.currentPickup)).primaryXYZ, 5);
            if (!chests.isEmpty()) {
                int count = this.getItemCountInChests(chests, new ItemStack(pickUpItem, 1), doCompareMeta);
                int buy = count / 4;
                if (buy > 0) {
                    ModSimReloaded.log.info("JobBurgersManager: 购买 " + count + " 件商品中购买 " + buy + " 件");
                    this.inventoriesTransferLimitedToFolk(this.theFolk.inventory, chests, new ItemStack(pickUpItem, 1, pickUpItem.func_180651_a(pickUpItem.func_176223_P())), buy, doCompareMeta);
                }
            }

            ++this.currentPickup;
            if (this.currentPickup <= this.pickupBuildings.size() - 1) {
                this.theFolk.gotoXYZ(((Building) this.pickupBuildings.get(this.currentPickup)).primaryXYZ, (GotoMethod) null);
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.manager.On_my") + ((Building) this.pickupBuildings.get(this.currentPickup)).displayName;
                this.step = 2;
            } else {
                this.step = 4;
            }
        }

    }

    private void doPickup(String buildingSearch, Item pickUpItem, boolean doCompareMeta) {
        if (this.step == 1) {
            this.currentPickup = 0;
            this.pickupBuildings.clear();
            this.pickupBuildings = Building.getBuildingBySearch(buildingSearch, true);
            if (this.pickupBuildings.size() == 0) {
                this.step = 4;
                return;
            }

            this.theFolk.gotoXYZ(((Building) this.pickupBuildings.get(this.currentPickup)).primaryXYZ, (GotoMethod) null);
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.manager.On_my") + ((Building) this.pickupBuildings.get(this.currentPickup)).displayName;
            this.step = 2;
        } else if (this.step == 2) {
            if (this.theFolk.destination == null) {
                this.step = 3;
            }
        } else if (this.step == 3) {
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.manager.Buying_items") + ((Building) this.pickupBuildings.get(this.currentPickup)).displayName;
            ArrayList<IInventory> chests = inventoriesFindClosest(((Building) this.pickupBuildings.get(this.currentPickup)).primaryXYZ, 5);
            if (!chests.isEmpty()) {
                int count = this.getItemCountInChests(chests, new ItemStack(pickUpItem, 1), doCompareMeta);
                int buy = count / 4;
                if (buy > 0) {
                    ModSimReloaded.log.info("JobBurgersManager: 购买 " + buy + " out of " + count + " 物品");
                    this.inventoriesTransferLimitedToFolk(this.theFolk.inventory, chests, new ItemStack(pickUpItem, 1), buy, doCompareMeta);
                }
            }

            ++this.currentPickup;
            if (this.currentPickup <= this.pickupBuildings.size() - 1) {
                this.theFolk.gotoXYZ(((Building) this.pickupBuildings.get(this.currentPickup)).primaryXYZ, (GotoMethod) null);
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.manager.On_my") + ((Building) this.pickupBuildings.get(this.currentPickup)).displayName;
                this.step = 2;
            } else {
                this.step = 4;
            }
        }

    }

    private void stagePickupBakery() {
        if (this.step < 4) {
            this.doPickup("bakery", (new ItemStack(Items.field_151025_P)).func_77973_b(), false);
        } else {
            this.theStage = Stage.PICKUPGROCERY;
            this.step = 1;
        }

    }

    private void stagePickupGrocery() {
        if (this.step < 4) {
            this.doPickup("grocery", (new ItemStack(Items.field_151174_bG)).func_77973_b(), false);
        } else {
            this.theStage = Stage.PICKUPCHEESE;
            this.step = 1;
        }

    }

    private void stagePickupCheese() {
        if (this.step < 4) {
            this.doPickup("cheese factory", (new ItemStack(ItemLoader.itemCheese, 1, 0)).func_77973_b(), true);
        } else {
            this.theStage = Stage.PICKUPBUTCHERS;
            this.step = 1;
        }

    }

    private void stagePickupButchers() {
        if (this.step < 4) {
            this.doPickup("butchers", (new ItemStack(Items.field_151082_bd, 1)).func_77973_b(), false);
        } else {
            this.theStage = Stage.DROPOFF;
            this.step = 1;
        }

    }

    private void stageDropoff() {
        ArrayList back;
        if (this.step == 1) {
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.dropoff.On_my");
            back = this.theStore.getSpecialBlocks(0);
            if (!back.isEmpty()) {
                this.theFolk.gotoXYZ((V3) back.get(0), (GotoMethod) null);
                this.step = 2;
            }
        } else if (this.step == 2) {
            if (this.theFolk.destination == null) {
                this.step = 3;
            }
        } else if (this.step == 3) {
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.dropoff.Unloading");
            back = this.theStore.getSpecialBlocks(0);
            ArrayList<IInventory> backstoreChests = inventoriesFindClosest((V3) back.get(0), 3);
            boolean ok = this.inventoriesTransferFromFolk(this.theFolk.inventory, backstoreChests, (ItemStack) null);
            if (!ok) {
                ModSimReloaded.sendChat(this.theFolk.name + I18n.func_135052_a("container.sim.job.dropoff.chest"));
            }

            this.theStage = Stage.HANGINGOUT;
            this.step = 0;
            GameStates var10000 = ModSimReloaded.states;
            var10000.credits = (float) ((double) var10000.credits - 2.45D);
        }

    }

    private void stageHangingOut() {
        if (this.step % 2 == 0) {
            this.theFolk.gotoXYZ(this.theStore.primaryXYZ, GotoMethod.WALK);
        } else {
            this.theFolk.gotoXYZ((V3) this.theStore.getSpecialBlocks(0).get(0), GotoMethod.WALK);
        }

        String say = "";
        switch (this.step) {
            case 0:
                say = I18n.func_135052_a("container.sim.job.dropoff.Counting");
                break;
            case 1:
                say = I18n.func_135052_a("container.sim.job.dropoff.Cancelling");
                break;
            case 2:
                say = I18n.func_135052_a("container.sim.job.dropoff.Being");
                break;
            case 3:
                say = I18n.func_135052_a("container.sim.job.dropoff.Doing");
                break;
            case 4:
                say = I18n.func_135052_a("container.sim.job.dropoff.Disciplining");
                break;
            case 5:
                say = I18n.func_135052_a("container.sim.job.dropoff.Reducing");
                break;
            case 6:
                say = I18n.func_135052_a("container.sim.job.dropoff.Adjusting");
        }

        ++this.step;
        if (this.step > 6) {
            this.step = 0;
        }

        this.theFolk.statusText = say;
    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.Arrived_at_the_store");
            this.theStage = Stage.ARRIVEDATSTORE;
            ArrayList<V3> back = this.theStore.getSpecialBlocks(0);
            if (!back.isEmpty()) {
                this.theFolk.gotoXYZ((V3) back.get(0), (GotoMethod) null);
            }
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod) null);
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }


}
