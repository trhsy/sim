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
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ========================================
 *
 * @ClassName JobLumberjack
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:51
 * ========================================
 **/
public class JobLumberjack extends Job implements Serializable {
    private static final long serialVersionUID = -1177112207904887741L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient ArrayList<IInventory> millChests = new ArrayList();
    private transient V3 foundWoodAt = new V3();
    private transient Building lumbermill = null;
    private transient long startedGoing = 0L;
    private transient boolean isChopping = false;
    private transient float pay = 0.0F;
    private transient boolean onRoute = false;

    public JobLumberjack() {
    }

    public JobLumberjack(FolkData folk) {
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
        this.theFolk.isWorking = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!ModSimukraft.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage == Stage.IDLE && ModSimukraft.isDayTime()) {
                this.theStage = Stage.SCANFORTREE;
            } else if (this.theStage == Stage.ARRIVEDATMILL) {
                this.theStage = Stage.SCANFORTREE;
            } else if (this.theStage == Stage.SCANFORTREE) {
                this.stageScanForTree();
            } else if (this.theStage == Stage.GOTOTREE) {
                this.pickUpSaplings();
                this.stageGotoTree();
            } else if (this.theStage == Stage.CHOPPINGTREE) {
                this.stageChoppingTree();
                this.pickUpSaplings();
            } else if (this.theStage == Stage.RETURNWOOD) {
                this.stageReturnWood();
                this.pickUpSaplings();
            }

        }
    }

    private void stageScanForTree() {
        this.theFolk.action = FolkAction.ATWORK;
        this.theFolk.isWorking = false;
        V3 searchXYZ = null;
        this.lumbermill = Building.getBuilding(this.theFolk.employedAt);
        V3 ts = null;

        try {
            if (this.lumbermill.lumbermillMarker != null) {
                searchXYZ = this.lumbermill.lumbermillMarker;
            } else if (this.theFolk.employedAt != null) {
                searchXYZ = this.theFolk.employedAt.clone();
            } else {
                searchXYZ = this.theFolk.location.clone();
            }

            ts = searchXYZ.clone();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        V3 searchpos;
        if (ts != null) {
            searchpos = ts.clone();
        } else {
            searchpos = this.theFolk.location.clone();
        }

        try {
            this.foundWoodAt = findClosestBlockType(searchpos, Blocks.field_150364_r, ModSimukraft.configLumberArea, false);
            this.foundWoodAt.theDimension = this.jobWorld.field_73011_w.field_76574_g;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        this.theStage = Stage.GOTOTREE;
        this.onRoute = false;
        if (this.foundWoodAt == null) {
            ModSimukraft.sendChat(this.theFolk.name + " could not find any wood in the area.");
            this.theFolk.selfFire();
        }
    }

    private void stageGotoTree() {
        this.theFolk.isWorking = false;
        if (!this.onRoute) {
            this.theFolk.statusText = "Going to tree...";
            this.theFolk.gotoXYZ(this.foundWoodAt, (GotoMethod)null);
            this.startedGoing = System.currentTimeMillis();
            this.onRoute = true;
        } else {
            if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                this.theFolk.updateLocationFromEntity();
            }

            double dist = (double)this.theFolk.location.getDistanceTo(this.foundWoodAt);
            if (dist < 7.0D) {
                this.theStage = Stage.CHOPPINGTREE;
                this.theFolk.stayPut = true;
                this.step = 1;
            } else {
                if (this.theFolk.destination == null && this.theFolk.theEntity != null) {
                }

                if (System.currentTimeMillis() - this.startedGoing > 25000L) {
                    this.theStage = Stage.CHOPPINGTREE;
                    this.theFolk.stayPut = true;
                    this.theFolk.destination = null;
                    this.step = 1;
                }
            }
        }

    }

    private void stageChoppingTree() {
        int i;
        int l;
        if (this.step == 1) {
            this.theFolk.statusText = "Choppy Choppy tree!";
            this.theFolk.isWorking = true;

            for(i = 0; i < 20; ++i) {
                l = this.foundWoodAt.x.intValue();
                int y = this.foundWoodAt.y.intValue() - 1;
                int z = this.foundWoodAt.z.intValue();
                if (this.jobWorld == null) {
                    this.theFolk.selfFire();
                    return;
                }

                if (this.jobWorld.getBlock(l, y, z) != Blocks.field_150364_r) {
                    break;
                }

                this.foundWoodAt.y = (double)y;
            }

            this.step = 2;
        } else if (this.step == 2) {
            if (this.jobWorld.getBlock(this.foundWoodAt.x.intValue(), this.foundWoodAt.y.intValue(), this.foundWoodAt.z.intValue()) == Blocks.field_150364_r) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        this.isChopping = true;

                        for(int d = 0; d < 12; ++d) {
                            try {
                                this.mc.field_71441_e.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, "step.wood", 1.0F, 1.0F, false);
                            } catch (Exception var5) {
                            }

                            if (this.theFolk.theEntity != null) {
                                this.theFolk.theEntity.field_70733_aJ = 0.3F;

                                try {
                                    Thread.sleep(100L);
                                } catch (Exception var4) {
                                }

                                this.theFolk.theEntity.field_70733_aJ = 0.7F;

                                try {
                                    Thread.sleep(100L);
                                } catch (Exception var3) {
                                }
                            }
                        }

                        this.isChopping = false;
                    }
                });
                t.start();
                this.step = 3;
            } else {
                this.step = 4;
            }
        } else {
            int count;
            if (this.step == 3) {
                if (this.isChopping) {
                    return;
                }

                ArrayList<ItemStack> log = this.translateBlockWhenMined(this.jobWorld, this.foundWoodAt);
                this.jobWorld.setBlock(this.foundWoodAt.x.intValue(), this.foundWoodAt.y.intValue(), this.foundWoodAt.z.intValue(), Blocks.field_150350_a, 0, 3);
                if (log != null) {
                    for(l = 0; l < log.size(); ++l) {
                        ItemStack isl = (ItemStack)log.get(l);
                        this.theFolk.inventory.add(isl);
                    }
                }

                count = this.getInventoryCount(this.theFolk, Blocks.field_150364_r);
                this.theFolk.statusText = "Got " + count + " logs so far";
                this.theFolk.stayPut = false;
                this.foundWoodAt.y = this.foundWoodAt.y + 1.0D;
                this.step = 2;
            } else if (this.step == 4) {
                if (this.theFolk.isSpawned()) {
                    count = this.getInventoryCount(this.theFolk, Blocks.field_150345_g);
                    if (count > 0) {
                        for(i = 0; i < this.theFolk.inventory.size(); ++i) {
                            ItemStack fis = (ItemStack)this.theFolk.inventory.get(i);
                            if (fis != null && Block.func_149634_a(fis.func_77973_b()) == Blocks.field_150345_g) {
                                this.theFolk.inventory.remove(i);
                                this.plantSapling(Block.func_149634_a(fis.func_77973_b()));
                                break;
                            }
                        }
                    }
                } else {
                    this.plantSapling(Blocks.field_150345_g);
                }

                count = this.getInventoryCount(this.theFolk, Blocks.field_150364_r);
                if (count < 12) {
                    this.theStage = Stage.SCANFORTREE;
                } else {
                    this.theStage = Stage.RETURNWOOD;
                    this.step = 1;
                }
            }
        }

    }

    private void stageReturnWood() {
        this.theFolk.isWorking = false;
        if (this.step == 1) {
            this.theFolk.statusText = "Delivering wood back to base";
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            this.step = 2;
        } else {
            int dist;
            if (this.step == 2) {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist <= 1) {
                    this.step = 3;
                } else if (this.theFolk.destination == null && this.theFolk.theEntity != null) {
                }
            } else if (this.step == 3) {
                this.theFolk.stayPut = true;
                dist = this.getInventoryCount(this.theFolk, Blocks.field_150364_r);
                this.millChests = inventoriesFindClosest(this.theFolk.employedAt, 6);
                this.inventoriesTransferFromFolk(this.theFolk.inventory, this.millChests, new ItemStack(Blocks.field_150364_r));
                this.pay = (float)dist * 0.03F;
                GameStates var10000 = ModSimukraft.states;
                var10000.credits -= this.pay;
                ModSimukraft.sendChat(this.theFolk.name + " has delivered " + dist + " logs at the lumbermill");
                this.theStage = Stage.SCANFORTREE;
                this.step = 1;
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
            this.theFolk.statusText = "I'm a lumberjack, and I'm ok";
            this.theStage = Stage.ARRIVEDATMILL;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    private void pickUpSaplings() {
        if (this.theFolk.isSpawned()) {
            List list1 = this.jobWorld.func_72839_b(this.theFolk.theEntity, AxisAlignedBB.func_72330_a(this.theFolk.theEntity.posX, this.theFolk.theEntity.posY, this.theFolk.theEntity.posZ, this.theFolk.theEntity.posX + 1.0D, this.theFolk.theEntity.posY + 1.0D, this.theFolk.theEntity.posZ + 1.0D).func_72314_b(3.0D, 4.0D, 3.0D));
            Iterator iterator1 = list1.iterator();
            if (!list1.isEmpty()) {
                while(iterator1.hasNext()) {
                    Entity entity1 = (Entity)iterator1.next();
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem)entity1;
                        ItemStack is = entityitem.func_92059_d();

                        try {
                            Item ID = is.func_77973_b();
                            if (ID == Item.func_150898_a(Blocks.field_150345_g)) {
                                this.theFolk.inventory.add(new ItemStack(Blocks.field_150345_g, is.func_77960_j(), 1));
                                entityitem.func_70106_y();
                            }
                        } catch (Exception var7) {
                        }
                    }
                }
            }

        }
    }

    private void plantSapling(Block is) {
        if (this.theFolk.isSpawned()) {
            if (this.jobWorld.getBlock((int)this.theFolk.theEntity.posX, (int)this.theFolk.theEntity.posY, (int)this.theFolk.theEntity.posZ) == null) {
                this.jobWorld.func_147449_b((int)this.theFolk.theEntity.posX, (int)this.theFolk.theEntity.posY, (int)this.theFolk.theEntity.posZ, is);
            }
        } else {
            this.jobWorld.setBlock(this.theFolk.location.x.intValue(), this.theFolk.location.y.intValue(), this.theFolk.location.z.intValue(), Blocks.field_150345_g, 0, 3);
        }

    }

}

