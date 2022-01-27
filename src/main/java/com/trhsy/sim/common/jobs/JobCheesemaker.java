package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.block.Block;
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
 * @Description todo
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
            ModSimukraft.sendChat("There was a problem with the Cheese factory, try re-starting Minecraft");
        } else {
            if (!ModSimukraft.isDayTime()) {
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
                if (this.theStage != Stage.IDLE || !ModSimukraft.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATFACTORY) {
                        this.stageArrivedAtFactory();
                    } else if (this.theStage == Stage.GOINGTODAIRYFARM) {
                        this.stageGoingToDairyFarm();
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

                if (!ModSimukraft.isDayTime()) {
                    this.theStage = Stage.IDLE;
                }

                this.timeSinceLastRun = System.currentTimeMillis();
            }
        }
    }

    private void stageArrivedAtFactory() {
        try {
            ArrayList<V3> cheesechest = this.theCheeseFactory.getSpecialBlocks(5);
            ArrayList<IInventory> chests = inventoriesFindClosest((V3)cheesechest.get(0), 4);
            this.inventoriesTransferToFolk(this.theFolk.inventory, chests, new ItemStack(Items.field_151117_aB, 64), (Block)null);
        } catch (Exception var3) {
        }

        this.theStage = Stage.GOINGTODAIRYFARM;
        this.currentFarmNum = -1;
    }

    private void stageGoingToDairyFarm() {
        this.theFolk.statusText = "Going to collect milk";
        ++this.currentFarmNum;
        ArrayList<Building> dairyFarms = Building.getBuildingBySearch("Dairy Farm", true);
        if (!dairyFarms.isEmpty() && dairyFarms.size() - 1 <= this.currentFarmNum) {
            this.farm = (Building)dairyFarms.get(this.currentFarmNum);
            this.theFolk.gotoXYZ(this.farm.primaryXYZ, GotoMethod.BEAM);
            this.theStage = Stage.COLLECTINGMILK;
            this.step = 1;
        } else if (dairyFarms.isEmpty()) {
            ModSimukraft.sendChat(this.theFolk.name + " has retired, as there are no dairy farms");
            this.theFolk.selfFire();
        } else {
            this.theStage = Stage.GOINGTOTANK;
        }
    }

    private void stageCollectingMilk() {
        this.theFolk.statusText = "Collecting milk";
        if (this.step == 1) {
            if (this.theFolk.destination == null && this.theFolk.location.getDistanceTo(this.farm.primaryXYZ) < 5) {
                this.step = 2;
                this.theFolk.isWorking = true;
            } else {
                ModSimukraft.log.info("JobCheeseMaker: not arrived at farm yet");
            }
        } else if (this.step == 2) {
            this.chestsAtDairy = Job.inventoriesFindClosest(this.farm.primaryXYZ, 5);
            if (this.chestsAtDairy.isEmpty()) {
                ModSimukraft.sendChat(this.theFolk.name + ": Can't find any chests at the dairy farm, I quit!");
                this.theFolk.selfFire();
                return;
            }

            this.inventoriesTransferToFolk(this.theFolk.inventory, this.chestsAtDairy, new ItemStack(Items.field_151117_aB, 1), (Block)null);
            if (this.theFolk.inventory == null || this.theFolk.inventory.isEmpty()) {
                ModSimukraft.sendChat(this.theFolk.name + " hasn't found any milk at the dairy today.");
                this.theStage = Stage.SLICECHEESE;
                this.step = 1;
                this.theFolk.isWorking = false;
                this.theFolk.statusText = "No Milk to process, gonna be an easy day today!";
                return;
            }

            ArrayList<V3> tanktop = this.theCheeseFactory.getSpecialBlocks(3);
            if (!tanktop.isEmpty()) {
                this.theFolk.gotoXYZ((V3)tanktop.get(0), GotoMethod.BEAM);
                this.theStage = Stage.GOINGTOTANK;
                this.step = 1;
                this.theFolk.isWorking = false;
            } else {
                ModSimukraft.log.warning("JobCheesemaker: no tank top point");
                this.theFolk.selfFire();
            }
        }

    }

    private void stageGoingToTank() {
        if (this.step == 1) {
            if (this.theFolk.destination == null) {
                this.step = 2;
                this.theFolk.statusText = "Preparing to fill the tank";
            } else {
                ModSimukraft.log.info("JobCheeseMaker: not arrived at back yet");
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
                    this.theFolk.statusText = "Emptying " + this.theFolk.inventory.size() + " buckets of milk";
                } else {
                    this.theFolk.statusText = "Emptied all the milk";
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

                    milkBlock = (V3)i$.next();
                    id = this.jobWorld.getBlock(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue());
                    meta = this.jobWorld.func_72805_g(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue());
                } while(id != null && (id != ModSimukraft.blockFluidMilk || meta != 1));

                this.jobWorld.setBlock(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue(), ModSimukraft.blockFluidMilk, 0, 3);

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
            this.theFolk.statusText = "Checking milk viscosity";
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
                ModSimukraft.sendChat("There's a problem with the cheese factory, place a building constructor down and re-build it");
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
                    say = "Stirring the milk";
                    break;
                case 1:
                    say = "Adding top secret ingredient";
                    break;
                case 2:
                    say = "Adding bacterial culture";
                    break;
                case 3:
                    say = "Removing unwanted spores";
                    break;
                case 4:
                    say = "Checking fermentation progress";
                    break;
                case 5:
                    say = "Adding Rennet";
                    break;
                case 6:
                    say = "Reticulating Cheese splines";
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

            if (MinecraftServer.getServer().worldServers[0].func_72820_D() % 24000L > 9900L) {
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
                dist = this.jobWorld.func_72805_g(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue());
                if (id == ModSimukraft.blockFluidMilk && dist == 0) {
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
                    cheese = (V3)i$.next();
                    id = this.jobWorld.getBlock(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue());
                    dist = cheese.getDistanceTo(currentStirPos);
                    if (id != ModSimukraft.blockCheese && dist < 5) {
                        this.jobWorld.setBlock(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue(), ModSimukraft.blockCheese, 0, 3);
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
            ModSimukraft.sendChat("There was a problem with the Cheese factory, try re-building it - no milk blocks");
        }
    }

    private void stageHarvestCheese() {
        ArrayList<V3> cheeseBlocks = this.theCheeseFactory.getSpecialBlocks(1);
        ArrayList<V3> stirPositions = this.theCheeseFactory.getSpecialBlocks(4);
        this.theFolk.statusText = "Extracting Cheese blocks";
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
                    if (((V3)stirPositions.get(0)).getDistanceTo(block) < 5 && id == ModSimukraft.blockCheese) {
                        gotBlock = true;
                        this.theFolk.inventory.add(new ItemStack(ModSimukraft.blockCheese));
                        this.jobWorld.setBlock(block.x.intValue(), block.y.intValue(), block.z.intValue(), id, 0, 3);
                        this.theFolk.isWorking = true;
                        GameStates var10000 = ModSimukraft.states;
                        var10000.credits = (float)((double)var10000.credits - 0.45D);
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
                    if (((V3)stirPositions.get(1)).getDistanceTo(block) < 5 && id == ModSimukraft.blockCheese) {
                        gotBlock = true;
                        this.theFolk.inventory.add(new ItemStack(ModSimukraft.blockCheese));
                        this.jobWorld.setBlock(block.x.intValue(), block.y.intValue(), block.z.intValue(), id, 0, 3);
                        break;
                    }
                }

                if (!gotBlock) {
                    this.step = 1;
                    this.theStage = Stage.SLICECHEESE;
                    this.theFolk.statusText = "Counting cheese blocks";
                }
            }
        }

    }

    private void stageSliceCheese() {
        ArrayList<V3> slicewaypoint = this.theCheeseFactory.getSpecialBlocks(5);
        if (slicewaypoint.isEmpty()) {
            this.theFolk.selfFire();
            ModSimukraft.sendChat("There was a problem with the Cheese factory, try re-building it - waypoint issue");
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
                        ModSimukraft.sendChat(this.theFolk.name + ": Someone has stolen the chest in the cheese factory, I quit!");
                        this.theFolk.selfFire();
                    }

                    this.inventoriesTransferFromFolk(this.theFolk.inventory, chests, (ItemStack)null);
                    this.step = 4;
                } else if (this.step == 4) {
                    chests = Job.inventoriesFindClosest((V3)slicewaypoint.get(0), 4);
                    this.theFolk.statusText = "Slicing cheese";
                    ItemStack cheese = inventoriesGet(chests, new ItemStack(ModSimukraft.blockCheese, 1), false, false);
                    if (cheese != null) {
                        boolean placedOK = this.inventoriesPut(chests, new ItemStack(ModSimukraft.itemFood, 9, 0), true);
                        if (!placedOK) {
                            ModSimukraft.sendChat(this.theFolk.name + "'s chest at the cheese factory is full of cheese!");
                            this.theFolk.selfFire();
                        }
                    } else {
                        this.step = 5;
                    }
                } else if (this.step == 5) {
                    this.theFolk.statusText = "I love cheese!";
                }
            }

        }
    }

    @Override
    public void onArrivedAtWork() {
        int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Arrived at the factory";
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

