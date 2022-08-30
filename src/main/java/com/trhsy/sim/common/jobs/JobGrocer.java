package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FarmType;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.core.entity.functionality.FarmingBox;
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
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobGrocer
 * @Description todo 食品商
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:50
 * ========================================
 **/
public class JobGrocer extends Job implements Serializable {
    private static final long serialVersionUID = -1177119265904279141L;
    public Vocation vocation = null;
    public FolkData theFolk =new FolkData();
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient float pay = 0.0F;
    /**食品铺子的箱子**/
    private transient List<IInventory> grocerChests = new CopyOnWriteArrayList();
    /**农场的箱子**/
    private transient List<IInventory> farmChests = new CopyOnWriteArrayList();
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
                    V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+1,this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
                    //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobGrocer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

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
            //到达商店
            if (this.theStage == Stage.ARRIVEDATSHOP) {
                this.runDelay = 10000;
                //在工作
                this.theFolk.action = FolkAction.ATWORK;
            }
            //收集食物
            if (this.theStage == Stage.COLLECTINGFOOD) {
                this.runDelay = 4000;
            }
            //卖食物
            if (this.theStage == Stage.SELLINGFOOD) {
                this.runDelay = 10000;
                this.theFolk.action = FolkAction.ATWORK;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    //到达商店
                    if (this.theStage == Stage.ARRIVEDATSHOP) {
                        this.stageArrived();
                        //去食品农场
                    } else if (this.theStage == Stage.GOINGTOFOODFARM) {
                        this.stageGoingToFoodFarm();
                        //收集食物
                    } else if (this.theStage == Stage.COLLECTINGFOOD) {
                        this.stageCollectingFood();
                        //返回店里
                    } else if (this.theStage == Stage.GOBACKTOSTORE) {
                        this.stageGoBackToStore();
                        //卖食物
                    } else if (this.theStage == Stage.SELLINGFOOD) {
                        this.stageSellingFood();
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobGrocer-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageArrived() {
        try {
            this.currentFarmNum = 0;
            this.theFolk.stayPut = true;
            this.theStage = Stage.GOINGTOFOODFARM;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageArrived出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @Author fan
     * @Description //TODO 去农场
     * @Date 20:42 2022/8/28
     * @Param []
     * @return void
     **/
    private void stageGoingToFoodFarm() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Fetching");
            if (!this.onRoute) {
                this.farm = this.getCurrentFarm();
                if (this.farm == null) {
                    this.theStage = Stage.GOBACKTOSTORE;
                } else {
                    this.onRoute = true;
                    this.theFolk.gotoXYZ(this.farm.getLocation(), null);
                }
            } else {
                double dist = 0;

                try {
                    if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                        this.theFolk.updateLocationFromEntity();
                    }

                    dist = (double)this.theFolk.location.getDistanceTo(this.farm.location);
                } catch (Exception e) {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageGoingToFoodFarm出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageCollectingFood() {
        try {
            //收集新鲜食物
            this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Collecting");
            if (this.step == 1) {
                if (this.farm == null) {
                    //去食品农场
                    this.theStage = Stage.GOINGTOFOODFARM;
                    return;
                }

                this.farmChests = inventoriesFindClosest(this.farm.getLocation(), 5);
                if (this.farmChests.size() > 0) {
                    this.theFolk.stayPut = true;
                    (this.farmChests.get(0)).openInventory(mc.thePlayer);
                    this.step = 2;
                }
            } else if (this.step == 2) {
                //西瓜
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.farmChests, new ItemStack(Items.melon, 640), Blocks.dirt);
                //南瓜
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.farmChests, new ItemStack(Blocks.pumpkin, 640), (Block)null);
                //胡萝卜
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.farmChests, new ItemStack(Items.carrot, 640), (Block)null);
                //马铃薯
                this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.farmChests, new ItemStack(Items.potato, 640), (Block)null);
                this.step = 3;
            } else if (this.step == 3) {
                (this.farmChests.get(0)).closeInventory(mc.thePlayer);
                this.theStage = Stage.GOINGTOFOODFARM;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageCollectingFood出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageGoBackToStore() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Taking");
            if (!this.onRoute) {
                this.onRoute = true;
                V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+1,this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
                //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageGoBackToStore出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 销售食品
     */
    private void stageSellingFood() {
        try {
            this.grocerChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.step == 1) {
                //卸载新鲜食物
                this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Unloading");
                //南瓜
                int pumpkins = this.getInventoryCount(this.theFolk, Blocks.pumpkin);
                //西瓜
                int melons = this.getInventoryCount(this.theFolk, Items.melon);
                //胡萝卜
                int carrots = this.getInventoryCount(this.theFolk, Items.carrot);
                //马铃薯
                int potatos = this.getInventoryCount(this.theFolk, Items.potato);
                //设置价格
                this.pay = (float)((double)pumpkins * 0.2D);
                this.pay += (float)((double)melons * 0.05D);
                this.pay += (float)((double)carrots * 0.05D);
                this.pay += (float)((double)potatos * 0.05D);

                this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), this.grocerChests, null);
                GameStates var10000 = ModSimReloaded.states;
                //进货花掉多少钱
                ModSimReloaded.log.info("采购新鲜食品花掉："+this.pay+"钱");
                var10000.credits -= this.pay;
                this.step = 2;
            } else if (this.step == 2) {
                this.theFolk.updateLocationFromEntity();
                int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist > 2 && this.theFolk.destination == null) {
                    V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+1,this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
                    //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }
                //卖生鲜
                this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Selling");
                if (MinecraftServer.getServer().worldServers[0].getWorldTime() % 24000L > 11600L) {
                    this.step = 3;
                }
            } else if (this.step == 3) {
                //关闭商店
                this.theFolk.statusText = I18n.format("container.sim.job.grocer.farmer.Closing");
                int sell = 0;
                ItemStack breadStack = null;
                if(ModSimReloaded.theFolks.size()>1){
                    int count = (new Random()).nextInt(4) + 1;
                    Boolean falg=true;
                    while (falg){
                        switch (count){
                            case 1:
                                sell=ModSimReloaded.theFolks.size()+1+(new Random()).nextInt(ModSimReloaded.theFolks.size());
                                breadStack=inventoriesGet(this.grocerChests, new ItemStack(Blocks.pumpkin, count), false, false);
                                if (breadStack != null) {falg=false;}
                                break;
                            case 2:
                                sell=ModSimReloaded.theFolks.size()+1+(new Random()).nextInt(ModSimReloaded.theFolks.size());
                                breadStack=inventoriesGet(this.grocerChests, new ItemStack(Items.melon, count), false, false);
                                if (breadStack != null) {falg=false;}
                                break;
                            case 3:
                                sell=ModSimReloaded.theFolks.size()+1+(new Random()).nextInt(ModSimReloaded.theFolks.size());
                                breadStack=inventoriesGet(this.grocerChests, new ItemStack(Items.carrot, count), false, false);
                                if (breadStack != null) {falg=false;}
                                break;
                            case 4:
                                sell=ModSimReloaded.theFolks.size()+1+(new Random()).nextInt(ModSimReloaded.theFolks.size());
                                breadStack=inventoriesGet(this.grocerChests, new ItemStack(Items.potato, count), false, false);
                                if (breadStack != null) {falg=false;}
                                break;
                            default:
                                break;
                        }
                    }
                }
                if (breadStack == null) {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.Baker_today"));
                }else{
                    if (sell > 0) {
                        //(食品商) 已经卖了 1 个食物给人们的。
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.grocer.farmer.grocer") + sell + I18n.format("container.sim.job.grocer.farmer.folks"));
                    } else {
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.grocer.farmer.today"));
                    }
                }
                for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                    FolkData folk = ModSimReloaded.theFolks.get(f);
                    if (breadStack.stackSize > 0) {
                        folk.levelFood = 10;
                        --breadStack.stackSize;
                    }
//                    for (int c = 0; c < this.grocerChests.size(); ++c) {
//                        IInventory chest = this.grocerChests.get(c);
//                        int g = 0;
//                        while (g < chest.getSizeInventory()) {
//                            ItemStack chestStack = chest.getStackInSlot(g);
//                            try {
//                                int count = (new Random()).nextInt(3) + 1;
//                                ItemFood food = (ItemFood) chestStack.getItem();
//                                ItemStack breadStack = inventoriesGet(this.grocerChests, new ItemStack(chestStack.getItem(), count), false, false);
//                                FolkData folk =  ModSimReloaded.theFolks.get(f);
//                                folk.levelFood = 10;
//                                sell += count;
//                            } catch (Exception e) {
//                                ++g;
//                            }
//                        }
//                    }
                }
                this.step = 4;
            } else if (this.step == 4) {
                this.theFolk.action=FolkAction.WANDER;
                this.theFolk.stayPut=false;
                this.theFolk.isWorking=false;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageSellingFood出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    private FarmingBox getCurrentFarm() {
        boolean found = false;
        FarmingBox farm = null;
        try {
            while(true) {
                if (!found) {
                    try {
                        farm = ModSimReloaded.theFarmingBoxes.get(this.currentFarmNum);
                    } catch (Exception e) {
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
                this.theFolk.statusText = I18n.format("container.sim.job.Arrived_at_the_store");
                this.theStage = Stage.ARRIVEDATSHOP;
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

