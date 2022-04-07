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
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ItemLoader;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ========================================
 *
 * @ClassName JobCheesemaker
 * @Description todo 奶酪制造商
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:45
 * ========================================
 **/
public class JobCheesemaker extends Job {

    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public int runDelay = 1000;
    private ArrayList<IInventory> chestsAtDairy = new ArrayList();
    private int currentFarmNum = 0;
    private Building farm = null;
    private long timeSinceLastRun = 0L;
    private Building theCheeseFactory = null;
    private boolean tubToggle = true;
    private int stirCount = 0;
    private V3 currentStirPos;

    public JobCheesemaker(FolkData folk) {
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
        if (this.theCheeseFactory == null) {
            this.theCheeseFactory = Building.getBuilding(this.theFolk.employedAt);
        }

        if (this.theCheeseFactory == null) {
            Building.loadAllBuildings();
            this.theCheeseFactory = Building.getBuilding(this.theFolk.employedAt);
        }

        if (this.theCheeseFactory == null) {
            this.theFolk.selfFire();
            ModSim.sendChat(I18n.format("container.sim.job.cheese_maker.There"));
        } else {
            if (!ModSim.isDayTime()) {
                this.theStage = Stage.IDLE;
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.ARRIVEDATFACTORY) {
                this.theFolk.action = FolkAction.ATWORK;
                this.runDelay = 11000;
            } else if (this.theStage == Stage.EMPTYINGMILK) {
                this.runDelay = 1000;
            } else {
                this.runDelay = 5000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                if (this.theStage != Stage.IDLE || !ModSim.isDayTime()) {
                    //到达工厂
                    if (this.theStage == Stage.ARRIVEDATFACTORY) {
                        this.stageArrivedAtFactory();
                        //去奶牛场
                    } else if (this.theStage == Stage.GOINGTODAIRYFARM) {
                        this.stageGoingToDairyFarm();
                        //收集牛奶
                    } else if (this.theStage == Stage.COLLECTINGMILK) {
                        this.stageCollectingMilk();
                    } else if (this.theStage == Stage.GOINGTOTANK) {
                        this.stageGoingToTank();
                    } else if (this.theStage == Stage.EMPTYINGMILK) {
                        this.stageEmptyingMilk();
                    } else if (this.theStage == Stage.STIRING) {
                        this.stageStiring();
                    } else if (this.theStage == Stage.HARVESTCHEESE) {
                        this.stageHarvestCheese();
                    } else if (this.theStage == Stage.SLICECHEESE) {
                        this.stageSliceCheese();
                    }
                }

                if (!ModSim.isDayTime()) {
                    this.theStage = Stage.IDLE;
                }

                this.timeSinceLastRun = System.currentTimeMillis();
            }
        }
    }

    /**
     * 到达工厂
     */
    private void stageArrivedAtFactory() {
        try {
            ArrayList<V3> cheesechest = this.theCheeseFactory.getSpecialBlocks(5);
            ArrayList<IInventory> chests = inventoriesFindClosest((V3)cheesechest.get(0), 4);
            this.inventoriesTransferToFolk(this.theFolk.inventory, chests, new ItemStack(Items.milk_bucket, 64), (Block)null);
        } catch (Exception var3) {
        }

        this.theStage = Stage.GOINGTODAIRYFARM;
        this.currentFarmNum = -1;
    }

    /**
     * 去奶牛场
     */
    private void stageGoingToDairyFarm() {
        this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Going");
        ++this.currentFarmNum;
        ArrayList<Building> dairyFarms = Building.getBuildingBySearch(I18n.format("container.sim.gui_contains_Dairy_Farm"), true);
        if (!dairyFarms.isEmpty() && dairyFarms.size() - 1 <= this.currentFarmNum) {
            this.farm = (Building)dairyFarms.get(this.currentFarmNum);
            this.theFolk.gotoXYZ(this.farm.primaryXYZ, GotoMethod.BEAM);
            this.theStage = Stage.COLLECTINGMILK;
            this.step = 1;
        } else if (dairyFarms.isEmpty()) {
            ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.has_retired"));
            this.theFolk.selfFire();
        } else {
            this.theStage = Stage.GOINGTOTANK;
        }
    }

    /**
     * 收集牛奶
     */
    private void stageCollectingMilk() {
        this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Collecting");
        if (this.step == 1) {
            if (this.theFolk.destination == null && this.theFolk.location.getDistanceTo(this.farm.primaryXYZ) < 5) {
                this.step = 2;
                this.theFolk.isWorking = true;
            } else {
                ModSim.log.info("JobCheeseMaker: 还没到农场");
            }
        } else if (this.step == 2) {
            this.chestsAtDairy = Job.inventoriesFindClosest(this.farm.primaryXYZ, 5);
            if (this.chestsAtDairy.isEmpty()) {
                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.I_quit"));
                this.theFolk.selfFire();
                return;
            }

            this.inventoriesTransferToFolk(this.theFolk.inventory, this.chestsAtDairy, new ItemStack(Items.milk_bucket, 1), (Block)null);
            if (this.theFolk.inventory == null || this.theFolk.inventory.isEmpty()) {
                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.dairy"));
                this.theStage = Stage.SLICECHEESE;
                this.step = 1;
                this.theFolk.isWorking = false;
                this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.process");
                return;
            }

            ArrayList<V3> tanktop = this.theCheeseFactory.getSpecialBlocks(3);
            if (!tanktop.isEmpty()) {
                this.theFolk.gotoXYZ((V3)tanktop.get(0), GotoMethod.BEAM);
                this.theStage = Stage.GOINGTOTANK;
                this.step = 1;
                this.theFolk.isWorking = false;
            } else {
                ModSim.log.warn("JobCheesemaker: 没有蓄水池");
                this.theFolk.selfFire();
            }
        }

    }

    private void stageGoingToTank() {
        if (this.step == 1) {
            if (this.theFolk.destination == null) {
                this.step = 2;
                this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Preparing");
            } else {
                ModSim.log.info("JobCheeseMaker: 还没到后面");
            }
        } else if (this.step == 2) {
            this.theStage = Stage.EMPTYINGMILK;
            this.step = 1;
        }

    }

    private void stageEmptyingMilk() {
        if (this.step == 1) {
            if (this.theFolk.inventory != null && !this.theFolk.inventory.isEmpty()) {
                if (this.theFolk.inventory.size() > 1) {
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Emptying") + this.theFolk.inventory.size() + I18n.format("container.sim.job.cheese_maker.buckets");
                } else {
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Emptied");
                }

                this.theFolk.isWorking = true;
                this.theFolk.stayPut = true;
                this.step = 2;
            } else {
                this.step = 3;
            }
        } else if (this.step == 2) {
            ArrayList<V3> milkblocks = this.theCheeseFactory.getSpecialBlocks(0);
            boolean filledOk = false;
            Iterator i$ = milkblocks.iterator();

            label61: {
                V3 milkBlock;
                Block id;
                int meta;
                do {
                    if (!i$.hasNext()) {
                        break label61;
                    }

                    milkBlock = (V3) i$.next();
                    id = this.jobWorld.getBlock(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue());
                    meta = this.jobWorld.getBlockMetadata(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue());
                } while (id != null && (id != BlockLoader.blockFluidMilk || meta != 1));

                this.jobWorld.setBlock(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue(), BlockLoader.blockFluidMilk, 0, 3);

                try {
                    this.theFolk.inventory.remove(0);
                } catch (Exception var8) {
                }

                filledOk = true;
            }

            if (filledOk) {
                this.theFolk.isWorking = false;
                this.step = 1;
            } else {
                this.theStage = Stage.STIRING;
                this.step = 1;
                this.theFolk.isWorking = false;
                ArrayList<V3> cheesechest = this.theCheeseFactory.getSpecialBlocks(5);
                ArrayList<IInventory> chests = inventoriesFindClosest((V3)cheesechest.get(0), 4);
                this.inventoriesTransferFromFolk(this.theFolk.inventory, chests, (ItemStack)null);
            }
        } else if (this.step == 3) {
            this.theStage = Stage.STIRING;
            this.step = 1;
            this.theFolk.isWorking = false;
        }

    }

    private void stageStiring() {
        ArrayList<V3> stirPositions = this.theCheeseFactory.getSpecialBlocks(4);
        if (this.step == 1) {
            this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.viscosity");
            if (!stirPositions.isEmpty()) {
                if (this.tubToggle) {
                    this.theFolk.gotoXYZ(this.currentStirPos = (V3)stirPositions.get(0), (GotoMethod)null);
                } else {
                    this.theFolk.gotoXYZ(this.currentStirPos = (V3)stirPositions.get(1), (GotoMethod)null);
                }

                this.tubToggle = !this.tubToggle;
                this.stirCount = 0;
                this.step = 2;
            } else {
                ModSim.sendChat(I18n.format("container.sim.job.cheese_maker.constructor"));
                this.theFolk.selfFire();
            }
        } else if (this.step == 2) {
            if (this.theFolk.destination == null) {
                this.step = 3;
            }
        } else if (this.step == 3) {
            String say = "";
            switch(this.stirCount) {
                case 0:
                    say = I18n.format("container.sim.job.cheese_maker.Stirring");
                    break;
                case 1:
                    say = I18n.format("container.sim.job.cheese_maker.ingredient");
                    break;
                case 2:
                    say = I18n.format("container.sim.job.cheese_maker.bacterial");
                    break;
                case 3:
                    say = I18n.format("container.sim.job.cheese_maker.unwanted");
                    break;
                case 4:
                    say = I18n.format("container.sim.job.cheese_maker.fermentation");
                    break;
                case 5:
                    say = I18n.format("container.sim.job.cheese_maker.Adding");
                    break;
                case 6:
                    say = I18n.format("container.sim.job.cheese_maker.Reticulating");
            }

            this.theFolk.statusText = say;
            this.theFolk.isWorking = true;
            this.theFolk.stayPut = true;
            if (this.stirCount == 6) {
                this.transformMilkToCheese(this.currentStirPos);
            }

            ++this.stirCount;
            if (this.stirCount > 6) {
                this.theFolk.isWorking = false;
                this.step = 1;
            }

            if (MinecraftServer.getServer().worldServers[0].getWorldTime() % 24000L > 9900L) {
                this.step = 1;
                this.theFolk.isWorking = false;
                this.theStage = Stage.HARVESTCHEESE;
            }
        }

    }

    private void transformMilkToCheese(V3 currentStirPos) {
        ArrayList<V3> milkBlocks = this.theCheeseFactory.getSpecialBlocks(0);
        ArrayList<V3> cheeseBlocks = this.theCheeseFactory.getSpecialBlocks(1);
        if (!milkBlocks.isEmpty() && !cheeseBlocks.isEmpty()) {
            boolean placedCheese = false;
            int milkGotCount = 0;

            V3 cheese;
            Block id;
            int dist;
            for(int m = milkBlocks.size() - 1; m > 0; --m) {
                cheese = (V3)milkBlocks.get(m);
                id = this.jobWorld.getBlock(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue());
                dist = this.jobWorld.getBlockMetadata(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue());
                if (id == BlockLoader.blockFluidMilk && dist == 0) {
                    this.jobWorld.setBlock(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue(), id, 0, 3);
                    ++milkGotCount;
                    if (milkGotCount > 1) {
                        break;
                    }
                }
            }

            if (milkGotCount > 0) {
                Iterator i$ = cheeseBlocks.iterator();

                while(i$.hasNext()) {
                    cheese = (V3) i$.next();
                    id = this.jobWorld.getBlock(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue());
                    dist = cheese.getDistanceTo(currentStirPos);
                    if (id != BlockLoader.blockCheeseBlock && dist < 5) {
                        this.jobWorld.setBlock(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue(), BlockLoader.blockCheeseBlock, 0, 3);
                        placedCheese = true;
                        break;
                    }
                }
            }

            if (milkGotCount == 0 || !placedCheese) {
                this.step = 1;
                this.theFolk.isWorking = false;
                this.theStage = Stage.HARVESTCHEESE;
            }

        } else {
            this.theFolk.selfFire();
            ModSim.sendChat(I18n.format("container.sim.job.cheese_maker.Cheese_factory"));
        }
    }

    private void stageHarvestCheese() {
        ArrayList<V3> cheeseBlocks = this.theCheeseFactory.getSpecialBlocks(1);
        ArrayList<V3> stirPositions = this.theCheeseFactory.getSpecialBlocks(4);
        this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Extracting");
        if (this.step == 1) {
            this.theFolk.gotoXYZ((V3)stirPositions.get(0), (GotoMethod)null);
            this.step = 2;
        } else if (this.step == 2) {
            if (this.theFolk.destination == null) {
                this.step = 3;
                this.theFolk.isWorking = true;
            }
        } else {
            boolean gotBlock;
            Iterator i$;
            V3 block;
            Block id;
            if (this.step == 3) {
                this.theFolk.isWorking = false;
                gotBlock = false;
                i$ = cheeseBlocks.iterator();

                while(i$.hasNext()) {
                    block = (V3)i$.next();
                    id = this.jobWorld.getBlock(block.x.intValue(), block.y.intValue(), block.z.intValue());
                    if (((V3) stirPositions.get(0)).getDistanceTo(block) < 5 && id == BlockLoader.blockCheeseBlock) {
                        gotBlock = true;
                        this.theFolk.inventory.add(new ItemStack(BlockLoader.blockCheeseBlock));
                        this.jobWorld.setBlock(block.x.intValue(), block.y.intValue(), block.z.intValue(), id, 0, 3);
                        this.theFolk.isWorking = true;
                        GameStates var10000 = ModSim.states;
                        var10000.credits = (float) ((double) var10000.credits - 0.45D);
                        break;
                    }
                }

                if (!gotBlock) {
                    this.theFolk.isWorking = false;
                    this.step = 4;
                    this.theFolk.gotoXYZ((V3)stirPositions.get(1), (GotoMethod)null);
                }
            } else if (this.step == 4) {
                if (this.theFolk.destination == null) {
                    this.step = 5;
                    this.theFolk.isWorking = true;
                }
            } else if (this.step == 5) {
                this.theFolk.isWorking = false;
                gotBlock = false;
                i$ = cheeseBlocks.iterator();

                while(i$.hasNext()) {
                    block = (V3)i$.next();
                    id = this.jobWorld.getBlock(block.x.intValue(), block.y.intValue(), block.z.intValue());
                    if (((V3) stirPositions.get(1)).getDistanceTo(block) < 5 && id == BlockLoader.blockCheeseBlock) {
                        gotBlock = true;
                        this.theFolk.inventory.add(new ItemStack(BlockLoader.blockCheeseBlock));
                        this.jobWorld.setBlock(block.x.intValue(), block.y.intValue(), block.z.intValue(), id, 0, 3);
                        break;
                    }
                }

                if (!gotBlock) {
                    this.step = 1;
                    this.theStage = Stage.SLICECHEESE;
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Counting");
                }
            }
        }

    }

    /**
     * 切片奶酪
     */
    private void stageSliceCheese() {
        ArrayList<V3> slicewaypoint = this.theCheeseFactory.getSpecialBlocks(5);
        if (slicewaypoint.isEmpty()) {
            this.theFolk.selfFire();
            ModSim.sendChat(I18n.format("container.sim.job.cheese_maker.problem"));
        } else {
            if (this.step == 1) {
                this.theFolk.gotoXYZ((V3)slicewaypoint.get(0), (GotoMethod)null);
                this.step = 2;
            } else if (this.step == 2) {
                if (this.theFolk.destination == null) {
                    this.step = 3;
                    this.theFolk.stayPut = true;
                }
            } else {
                ArrayList chests;
                if (this.step == 3) {
                    chests = Job.inventoriesFindClosest((V3)slicewaypoint.get(0), 4);
                    if (chests.isEmpty()) {
                        ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.Someone"));
                        this.theFolk.selfFire();
                    }

                    this.inventoriesTransferFromFolk(this.theFolk.inventory, chests, (ItemStack)null);
                    this.step = 4;
                } else if (this.step == 4) {
                    chests = Job.inventoriesFindClosest((V3) slicewaypoint.get(0), 4);
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Slicing");
                    ItemStack cheese = inventoriesGet(chests, new ItemStack(BlockLoader.blockCheeseBlock, 1), false, false);
                    if (cheese != null) {
                        boolean placedOK = this.inventoriesPut(chests, new ItemStack(ItemLoader.itemFoods, 9, 0), true);
                        if (!placedOK) {
                            ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.factory"));
                            this.theFolk.selfFire();
                        }
                    } else {
                        this.step = 5;
                    }
                } else if (this.step == 5) {
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.love_cheese");
                }
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
            this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.the_factory");
            this.theStage = Stage.ARRIVEDATFACTORY;
            this.currentFarmNum = 0;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

}

