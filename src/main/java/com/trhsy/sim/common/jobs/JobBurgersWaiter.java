package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.Building;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.loader.ItemLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobBurgersWaiter
 * @Description todo 服务员
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:44
 * ========================================
 **/
public class JobBurgersWaiter extends Job {

    public Vocation vocation = null;
    public FolkData theFolk =new FolkData();
    public Stage theStage;
    public int runDelay = 1000;
    private long timeSinceLastRun = 0L;
    private Building theStore = null;

    public JobBurgersWaiter(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+1,this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBurgersWaiter出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            if (this.theStore == null) {
                this.theStore = Building.getBuilding(this.theFolk.employedAt);
            }

            if (this.theStore != null) {
                if (!ModSimReloaded.isDayTime()) {
                    if (!theFolk.isNightOwl()) {
                        //闲置
                        this.theStage = Stage.IDLE;
                        return;
                    }
                }

                super.onUpdateGoingToWork(this.theFolk);
                if (this.theStage == Stage.ARRIVEDATSTORE) {
                    this.theFolk.action = FolkAction.ATWORK;
                    this.runDelay = 11000;
                } else if (this.theStage == Stage.SERVING) {
                    this.runDelay = 45000;
                } else {
                    this.runDelay = 5000;
                }

                if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                    if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                        if (this.theStage == Stage.ARRIVEDATSTORE) {
                            this.theStage = Stage.SERVING;
                            this.theFolk.statusText = I18n.format("container.sim.job.serving_customers");
                        } else if (this.theStage == Stage.SERVING) {
                            this.stageServing();
                        }
                    }

                    if (!ModSimReloaded.isDayTime()) {
                        if (!theFolk.isNightOwl()) {
                            //闲置
                            this.theStage = Stage.IDLE;
                            return;
                        }
                    }

                    this.timeSinceLastRun = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBurgersWaiter-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageServing() {
        try {
            List<V3> serve = this.theStore.getSpecialBlocks(2);
            if (!serve.isEmpty()) {
                List<IInventory> theChests = inventoriesFindClosest((V3)serve.get(0), 3);
                if (!theChests.isEmpty()) {
                    this.theFolk.gotoXYZ((V3)serve.get(0), null);

                    try {
                        this.theFolk.destination.destinationAcc = 0.3D;
                    } catch (Exception e) {
                    }

                    ItemStack is = inventoriesGet(theChests, (ItemStack)null, true, false);
                    if (is == null) {
                        this.theFolk.statusText = I18n.format("container.sim.job.serving.Wishing");
                    } else {
                        if (is.getItem() == ItemLoader.itemCheese||is.getItem() == ItemLoader.itemCheeseburger||is.getItem() == ItemLoader.itemFries||is.getItem() == ItemLoader.itemBurger) {
                            is = new ItemStack(is.getItem(), 1, is.getMetadata());
                            inventoriesGet(theChests, is, false, true);
                            this.theFolk.statusText = I18n.format("container.sim.job.merchant.Just_sold") + is.getDisplayName();
                            int r = (new Random()).nextInt(ModSimReloaded.theFolks.size() - 1);
                            FolkData folk = (FolkData) ModSimReloaded.theFolks.get(r);
                            if (folk.levelFood < 10) {
                                ++folk.levelFood;
                            }

                            folk.saveThisFolk();
                            ModSimReloaded.log.info("JobBurgersWaiter: 刚吃过 " + folk.name);
                            GameStates var10000 = ModSimReloaded.states;
                            var10000.credits = (float) ((double) var10000.credits - 0.45D);
                        } else {
                            this.theFolk.statusText = I18n.format("container.sim.job.merchant.Who_put") + is.getDisplayName() + I18n.format("container.sim.job.merchant.my_chest");
                        }

                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageServing出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.Arrived_at_the_store");
                this.theStage = Stage.ARRIVEDATSTORE;
                List<V3> back = this.theStore.getSpecialBlocks(2);
                if (!back.isEmpty()) {
                    this.theFolk.gotoXYZ((V3)back.get(0), null);
                }
            } else {
                V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+1,this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

}

