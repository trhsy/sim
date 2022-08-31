package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.Building;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobButcher
 * @Description todo 屠夫
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:45
 * ========================================
 **/
public class JobButcher extends Job implements Serializable {
    private static final long serialVersionUID = -1177112207904271422L;
    public Vocation vocation = null;
    public FolkData theFolk =new FolkData();
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient float pay = 0.0F;
    private transient List<IInventory> chestsAtFarm = new CopyOnWriteArrayList();
    private transient List<IInventory> chestsAtShop = new CopyOnWriteArrayList();
    private transient int currentFarmNum = 0;
    private transient Building farm = null;
    private transient boolean onRoute = false;

    public JobButcher() {
    }

    public JobButcher(FolkData folk) {
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobButcher出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            if (this.theStage == Stage.ARRIVEDATSHOP) {
                this.theFolk.action = FolkAction.ATWORK;
                this.runDelay = 11000;
            } else {
                this.runDelay = 3000;
            }

            if (this.theStage == Stage.SELLINGMEAT) {
                this.runDelay = 10000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATSHOP) {
                        this.theStage = Stage.GOINGTOMEATFARM;
                    } else if (this.theStage == Stage.GOINGTOMEATFARM) {
                        this.stageGoingToFarm();
                    } else if (this.theStage == Stage.COLLECTINGMEAT) {
                        this.stageCollectingMeat();
                    } else if (this.theStage == Stage.GOBACKTOSTORE) {
                        this.stageGoBackToStore();
                    } else if (this.theStage == Stage.SELLINGMEAT) {
                        this.stageSellingMeat();
                    }
                }

                if (!ModSimReloaded.isDayTime()) {
                    if (!theFolk.isNightOwl()) {
                        //闲置
                        this.theStage = Stage.IDLE;
                        return;
                    }
                }

                if (this.theStage == Stage.ARRIVEDATSHOP) {
                    this.theFolk.action = FolkAction.ATWORK;
                    this.runDelay = 11000;
                } else {
                    this.runDelay = 3000;
                }

                if (this.theStage == Stage.SELLINGMEAT) {
                    this.runDelay = 10000;
                }

                if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                    this.timeSinceLastRun = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobButcher-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageGoingToFarm() {
        try {
            //从农场获取新鲜食物
            this.theFolk.statusText = I18n.format("container.sim.job.butcher.Fetching");
            this.theFolk.action = FolkAction.ATWORK;
            if (!this.onRoute) {
                this.farm = this.getCurrentFarm();

                try {
                    if (this.farm != null && this.farm.primaryXYZ != null) {
                        this.onRoute = true;
                        this.theFolk.gotoXYZ(this.farm.primaryXYZ, null);
                    } else {
                        this.theStage = Stage.GOBACKTOSTORE;
                    }
                } catch (Exception e) {
                    //var2.printStackTrace();
                    this.theStage = Stage.GOBACKTOSTORE;
                }
            } else {
                int dist = this.theFolk.location.getDistanceTo(this.farm.primaryXYZ);
                if (dist < 3) {
                    if (this.theFolk.theEntity != null) {
                        this.theFolk.theEntity.motionX = 0;
                        this.theFolk.theEntity.motionZ = 0;
                    }

                    this.onRoute = false;
                    this.theStage = Stage.COLLECTINGMEAT;
                    this.step = 1;
                    this.theFolk.stayPut = true;
                    return;
                }

                if (this.theFolk.destination == null) {
                    this.onRoute = false;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageGoingToFarm出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageCollectingMeat() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.butcher.Collecting");
            this.theFolk.action = FolkAction.ATWORK;
            if (this.step == 1) {
                this.chestsAtFarm.clear();
                this.chestsAtFarm = inventoriesFindClosest(this.farm.primaryXYZ, 5);
                if (this.chestsAtFarm.size() > 0) {
                    this.step = 2;
                }
            } else if (this.step == 2) {
                //chicken 鸡肉
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.chestsAtFarm, new ItemStack(Items.chicken, 1, 640), (Block)null);
                //porkchop 猪排
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.chestsAtFarm, new ItemStack(Items.porkchop, 1, 640), (Block)null);
                //牛肉
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.chestsAtFarm, new ItemStack(Items.beef, 1, 640), (Block)null);
                this.step = 3;
            } else if (this.step == 3) {
                this.theStage = Stage.GOINGTOMEATFARM;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageCollectingMeat出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    private void stageGoBackToStore() {
        try {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.statusText = I18n.format("container.sim.job.butcher.Taking");
            if (!this.onRoute) {
                this.onRoute = true;
                V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+1,this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
                //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            } else {
                double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist < 2) {
                    this.onRoute = false;
                    if (this.theFolk.theEntity != null) {
                        this.theFolk.theEntity.motionX = 0;
                        this.theFolk.theEntity.motionZ = 0;
                    }

                    this.theFolk.stayPut = true;
                    this.theFolk.statusText = I18n.format("container.sim.job.butcher.Unloading");
                    int meat1 = this.getInventoryCount(this.theFolk, Items.porkchop);
                    int meat2 = this.getInventoryCount(this.theFolk, Items.chicken);
                    int meat3 = this.getInventoryCount(this.theFolk, Items.beef);
                    this.pay = (float)((double)(meat1 + meat2 + meat3) * 0.03D);
                    this.chestsAtShop = inventoriesFindClosest(this.theFolk.employedAt, 3);
                    this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), this.chestsAtShop, (ItemStack)null);
                    this.theStage = Stage.SELLINGMEAT;
                    this.step = 1;
                    return;
                }

                if (this.theFolk.destination == null) {
                    this.onRoute = false;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageGoBackToStore出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageSellingMeat() {
        try {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.statusText = I18n.format("container.sim.job.butcher.Selling");
            if (this.step == 1) {
                this.chestsAtShop = inventoriesFindClosest(this.theFolk.employedAt, 3);
                this.openCloseChest((IInventory)this.chestsAtShop.get(0), 2000);
                if (this.pay > 0.0F) {
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= this.pay;
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.butcher.collected") + ModSimReloaded.displayMoney(this.pay) + I18n.format("container.sim.job.credits"));
                    this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1, 1, false);
                }

                this.step = 2;
            } else if (this.step == 2) {
                if (this.mc.getIntegratedServer().worldServers[0].getWorldTime() % 24000L > 11600L) {
                    this.step = 3;
                }

                this.theFolk.updateLocationFromEntity();
                double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist > 8) {
                    this.theFolk.beamMeTo(this.theFolk.employedAt);
                }
            } else if (this.step == 3) {
                this.theFolk.statusText = I18n.format("container.sim.job.butcher.Closing");
                int sell = 0;
                boolean notEnough = false;
                ItemStack piece = null;
                this.chestsAtShop = inventoriesFindClosest(this.theFolk.employedAt, 3);

                for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                    piece = inventoriesGet(this.chestsAtShop, new ItemStack(Items.porkchop, 1), false, false);
                    if (piece == null) {
                        piece = inventoriesGet(this.chestsAtShop, new ItemStack(Items.chicken, 1), false, false);
                    }

                    if (piece == null) {
                        piece = inventoriesGet(this.chestsAtShop, new ItemStack(Items.beef, 1), false, false);
                    }

                    if (piece != null) {
                        FolkData folk = (FolkData) ModSimReloaded.theFolks.get(f);
                        folk.levelFood = 10;
                        ++sell;
                    }
                }

                if (sell > 0) {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.butcher.has_sold") + sell + I18n.format("container.sim.job.butcher.folks"));
                }

                this.step = 4;
            } else if (this.step == 4) {
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageSellingMeat出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 获取当前肉场
     * @return
     */
    private Building getCurrentFarm() {
        boolean found = false;
        try {
            while(!found) {
                try {
                    Building farm = (Building) ModSimReloaded.theBuildings.get(this.currentFarmNum);
                    //养牛场
                    String cattleFarm=I18n.format("container.sim.gui_contains_Cattle_Farm");
                    String pigFarm=I18n.format("container.sim.gui_contains_Pig_Farm");
                    String chickenFarm=I18n.format("container.sim.gui_contains_Chicken_Farm");

                    if (farm.displayNameWithoutPK.contains(cattleFarm) || farm.displayNameWithoutPK.contains(pigFarm) || farm.displayNameWithoutPK.contains(chickenFarm)) {
                        found = true;
                        ++this.currentFarmNum;
                        return farm;
                    }

                    ++this.currentFarmNum;
                    if (this.currentFarmNum > ModSimReloaded.theBuildings.size() - 1) {
                        ModSimReloaded.sendChat(I18n.format("container.sim.job.Butcher_today"));
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getCurrentFarm出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.butcher.Arrived");
                this.theStage = Stage.ARRIVEDATSHOP;
                this.currentFarmNum = 0;
            } else {
                V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+1,this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
                //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}

