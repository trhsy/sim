package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.entity.functionality.FarmingBox;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
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
 * @Description todo 面包师的工作
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:35
 * ========================================
 **/
public class JobBaker extends Job implements Serializable {

    private static final long serialVersionUID = -1177112153304279141L;
    /*
    职业
     */
    public Vocation vocation = null;
    /*

     */
    public Stage theStage;
    /*
        模拟NPC
     */
    public FolkData theFolk;
    /*
    默认运行延迟 1m
     */
    public transient int runDelay = 1000;
    /*
    上次运动后的时间
     */
    public transient long timeSinceLastRun = 0L;
    /*
    支付/付款
     */
    private transient float pay = 0.0F;
    /*
    面包店/烘焙箱
     */
    private transient ArrayList<IInventory> bakeryChests = null;
    /*
    农场箱子
     */
    private transient ArrayList<IInventory> farmChests = new ArrayList();
    /*
    当前农场数量
     */
    private transient int currentFarmNum = 0;
    /*
    养殖箱
     */
    private transient FarmingBox farm = null;

    /**
     * 初始化面包师工作
     */
    public JobBaker() {
    }

    public JobBaker(FolkData folk) {
        try {
            //设置面包师的人选
            this.theFolk = folk;
            //如果状态为空
            if (this.theStage == null) {
                //设置为闲置的
                this.theStage = Stage.IDLE;
            }
            //如果不为空
            if (this.theFolk != null) {
                //目的地为空
                if (this.theFolk.destination == null) {
                    //设置目的地为雇佣地点
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
                }

            }
        } catch (Exception e) {
            ModSimReloaded.log.error("JobBaker出错了：" + e.getMessage());
        }

    }

    /**
     * 重新安排工作
     */
    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    /**
     * 更新
     */
    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            //闲置
            if (!ModSimReloaded.isDayTime()) {
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }
            //继续工作
            super.onUpdateGoingToWork(this.theFolk);
            //到达商店
            if (this.theStage == Stage.ARRIVEDATSHOP) {
                this.runDelay = 10000;
            }
            //收集小麦
            if (this.theStage == Stage.COLLECTINGWHEAT) {
                this.runDelay = 1000;
            }
            //卖面包
            if (this.theStage == Stage.SELLINGBREAD) {
                this.runDelay = 10000;
            }
            //做面包
            if (this.theStage == Stage.MAKEBREAD) {
                this.runDelay = 10000;
            }
            //当前时间毫秒    减去              上次跑步后的时间       大于等于     运行延迟
            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                //上次跑步后的时间
                this.timeSinceLastRun = System.currentTimeMillis();
                //状态不是闲置并且 是黑夜
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    //到达商店
                    if (this.theStage == Stage.ARRIVEDATSHOP) {
                        //去农场
                        this.theStage = Stage.GOINGTOWHEATFARM;
                        this.step = 1;
                        //去农场
                    } else if (this.theStage == Stage.GOINGTOWHEATFARM) {
                        this.stageGoingToWheatFarm();
                        //收集小麦
                    } else if (this.theStage == Stage.COLLECTINGWHEAT) {
                        this.stageCollectingWheat();
                        //回到面包店
                    } else if (this.theStage == Stage.GOBACKTOBAKERY) {
                        this.stageGoBackToBakery();
                        //做面包
                    } else if (this.theStage == Stage.MAKEBREAD) {
                        this.stageMakeBread();
                        //卖面包
                    } else if (this.theStage == Stage.SELLINGBREAD) {
                        this.stageSellingBread();
                    }
                }

            }
        } catch (Exception e) {
            ModSimReloaded.log.error("onUpdate出错了：" + e.getMessage());
        }
    }

    /**
     * 去麦田
     */
    private void stageGoingToWheatFarm() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.Baker_Fetching");
            if (this.theFolk.destination == null && this.step == 1) {
                this.farm = this.getCurrentFarm();
                if (this.farm == null) {
                    this.theStage = Stage.GOBACKTOBAKERY;
                    this.step = 1;
                } else {
                    this.theFolk.gotoXYZ(this.farm.getLocation(), (GotoMethod) null);
                    this.runDelay = 1000;
                    this.step = 2;
                }
            }

            if (this.step == 2) {
                double dist = 0;
                if (this.farm != null) {
                    this.runDelay = 1000;
                    if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                        this.theFolk.updateLocationFromEntity();
                    }

                    dist = (double)this.theFolk.location.getDistanceTo(this.farm.getLocation());
                    if (dist <= 1) {
                        this.theStage = Stage.COLLECTINGWHEAT;
                        this.step = 1;
                        this.theFolk.stayPut = true;
                        if (this.theFolk.theEntity != null) {
                            this.theFolk.theEntity.motionX = 0;
                            this.theFolk.theEntity.motionZ = 0;
                        }

                        this.runDelay = 1000;
                        return;
                    }
                } else {
                    this.theStage = Stage.GOBACKTOBAKERY;
                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("stageGoingToWheatFarm出错了：" + e.getMessage());
        }


    }

    /**
     * 分期收集小麦
     */
    private void stageCollectingWheat() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.Baker_Collecting");
            this.runDelay = 1000;
            if (this.step == 1) {
                this.farmChests = inventoriesFindClosest(this.farm.getLocation(), 5);
                if (this.farmChests.size() > 0) {
                    ((IInventory)this.farmChests.get(0)).openInventory(mc.thePlayer);
                    this.step = 2;
                }
            } else if (this.step == 2) {
                this.farmChests = inventoriesFindClosest(this.farm.getLocation(), 5);
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.farmChests, new ItemStack(Items.wheat, 640), Blocks.air);
                this.step = 3;
            } else if (this.step == 3) {
                ((IInventory)this.farmChests.get(0)).closeInventory(mc.thePlayer);
                this.theStage = Stage.GOINGTOWHEATFARM;
                this.step = 1;
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("stageCollectingWheat出错了：" + e.getMessage());
        }
    }

    /**
     * 回到面包店
     */
    private void stageGoBackToBakery() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.Baker_Taking");
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
        } catch (Exception e) {
            ModSimReloaded.log.error("stageGoBackToBakery出错了：" + e.getMessage());
        }
    }

    /**
     * 做面包
     */
    private void stageMakeBread() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.Baker_Baking_bread");
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
                    wheat = this.getInventoryCount(this.theFolk, Items.wheat);
                    int bread = (int)Math.floor((double)(wheat / 3));
                    this.pay = (float)((double)bread * 0.2D);
                    this.bakeryChests = inventoriesFindClosest(this.theFolk.employedAt, 4);

                    try {
                        ((IInventory)this.bakeryChests.get(0)).openInventory(mc.thePlayer);
                    } catch (Exception var4) {
                    }

                    this.inventoriesPut(this.bakeryChests, new ItemStack(Items.bread, bread), true);
                    this.theFolk.getVillagerInventory().clear();
                    this.step = 3;
                } else if (this.step == 3) {
                    ((IInventory) this.bakeryChests.get(0)).closeInventory(mc.thePlayer);
                    this.theFolk.statusText = I18n.format("container.sim.job.Baker_Selling");
                    this.theFolk.stayPut = true;
                    if (this.theFolk.theEntity != null) {
                        if (this.theFolk.gender == 0) {
                            this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":bakerm", 1.0F, 1.0F, false);
                        } else {
                            this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":bakerf", 1.0F, 1.0F, false);
                        }
                    }

                    this.theStage = Stage.SELLINGBREAD;
                    this.step = 1;
                }

            } else {
                this.theFolk.statusText = I18n.format("container.sim.job.Baker_Who");
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("stageMakeBread出错了：" + e.getMessage());
        }

    }

    /**
     *卖面包
     */
    private void stageSellingBread() {
        try {
            if (this.step == 1) {
                if (this.pay > 0.0F) {
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= this.pay;
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.Baker_paid") + ModSimReloaded.displayMoney(this.pay) + I18n.format("container.sim.job.credits"));
                    this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                }

                this.step = 2;
            } else if (this.step == 2) {
                if (MinecraftServer.getServer().worldServers[0].getWorldTime() % 24000L > 11600L) {
                    this.step = 3;
                }
            } else if (this.step == 3) {
                this.theFolk.statusText = I18n.format("container.sim.job.Baker_Closing");
                //int sell = false;
                ItemStack breadStack = null;
                if (ModSimReloaded.theFolks.size() > 1) {
                    int sell = ModSimReloaded.theFolks.size() + 1 + (new Random()).nextInt(ModSimReloaded.theFolks.size());
                    this.bakeryChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
                    breadStack = inventoriesGet(this.bakeryChests, new ItemStack(Items.bread, sell), false, false);
                }

                if (breadStack == null) {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.Baker_today"));
                } else {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.has_sold") + breadStack.stackSize + I18n.format("container.sim.job.folks_today"));

                    for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                        FolkData folk = (FolkData) ModSimReloaded.theFolks.get(f);
                        if (breadStack.stackSize > 0) {
                            folk.levelFood = 10;
                            --breadStack.stackSize;
                        }
                    }
                }

                this.step = 4;
            } else if (this.step == 4) {
            }

        } catch (Exception e) {
            ModSimReloaded.log.error("stageSellingBread出错了：" + e.getMessage());
        }
    }

    /**
     * 获取当前农场
     * @return
     */
    private FarmingBox getCurrentFarm() {
        boolean found = false;
        try {
            while(!found) {
                try {
                    FarmingBox farm = (FarmingBox) ModSimReloaded.theFarmingBoxes.get(this.currentFarmNum);
                    if (farm.farmType == FarmType.WHEAT) {
                        found = true;
                        ++this.currentFarmNum;
                        return farm;
                    }

                    ++this.currentFarmNum;
                    if (this.currentFarmNum > ModSimReloaded.theFarmingBoxes.size() - 1) {
                        return null;
                    }
                } catch (Exception var4) {
                    return null;
                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
        return null;
    }

    /**
     * 他刚上班
     */
    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.Baker_Arrived");
                this.theStage = Stage.ARRIVEDATSHOP;
                this.currentFarmNum = 0;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }

    }

}

