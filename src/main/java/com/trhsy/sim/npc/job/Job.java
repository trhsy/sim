package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: Job
 * @Description: 工作
 * @date 2022/10/13 17:38
 */
public abstract class Job {
    //工作名称
    public String jobName = "null";
    //NPC数据
    public NpcData folk = null;
    //工作地址
    public V3 workPlace = null;
    //工作世界
    public World jobWorld = null;
    //工作阶段
    public int stage = -1;
    //物品
    public ItemStack stuckItem = null;
    //箱子是否已满
    public boolean chestsFull = false;
    //工作任务
    public List<JobTask> jobTasks = new CopyOnWriteArrayList();
    //工作箱子
    public List<IInventory> jobChests = new CopyOnWriteArrayList();
    //最近任务
    public JobTask currentTask;
    //是否在上班途中
    public boolean onWayToWork;
    //在工作
    public boolean atWork;
    //收集资源
    public List<Item> collectionItems = new CopyOnWriteArrayList<>();
    //建筑
    Building collectionBuilding;
    //已收集
    boolean hasCollected;
    //收集点
    LinkedHashMap<Building, ItemStack> collectionPoints = new LinkedHashMap();
    //支付
    public float pay;
    //物品抓取计时
    transient long itemGrabTimer = 0L;
    //列
    private int colCount = 0;

    private LinkedHashMap<Building, ItemStack> colBs;

    public Job(NpcData folk, BlockPos pos, World world) {
        this.folk = folk;
        folk.respawn(world, pos);
        this.workPlace = V3.fromBlockPos(pos);
        this.jobWorld = world;
    }

    public Job(NpcData folk, V3 pos, World world) {
        this.folk = folk;
        folk.respawn(world, pos.toBlockPos());
        this.workPlace = pos;
        this.jobWorld = world;
    }
    /**
     * @Author fan
     * @Description //TODO 下一个任务
     * @Date 10:33 2022/10/21
     * @Param []
     * @return void
     **/
    public void nextTask() {
        ModSimLoader.log.info("移动到下一个任务");
        int curTask = 0;

        for(int i = 0; i < this.jobTasks.size(); ++i) {
            if (this.jobTasks.get(i) == this.currentTask) {
                curTask = i;
                ModSimLoader.log.info("当前任务索引为 " + i);
            }
        }

        if (curTask >= this.jobTasks.size() - 1) {
            this.currentTask = null;
            this.stage = -1;
            ModSimLoader.log.info("没有剩余任务");
        } else {
            ModSimLoader.log.info("将任务更改为 " + this.jobTasks.get(curTask + 1) + "(" + curTask + 1 + ")");
            this.stage = curTask + 1;
            this.currentTask = (JobTask)this.jobTasks.get(curTask + 1);
            this.currentTask.begin();
        }

    }
    /**
     * @Author fan
     * @Description //TODO 工作更新回调
     * @Date 10:42 2022/10/21
     * @Param []
     * @return void
     **/
    public void onUpdate() {
        if (this.folk != null) {
            if (this.folk.entity != null) {
                if (this.folk.shouldWork()) {
                    this.folk.stayPut = true;
                }

                if (!this.folk.shouldWork() && this.atWork) {
                    this.folk.clearStatus();
                    this.folk.stayPut = false;
                    this.atWork = false;
                    this.onWayToWork = false;
                    this.currentTask = null;
                } else {
                    if (this.currentTask != null) {
                        this.currentTask.update();
                    }

                    if (this.folk.shouldWork() && !this.atWork && !this.folk.entity.worldObj.isRemote) {
                        this.onWayToWork = true;
                        if (this.folk.entity.getDistance(this.workPlace.x, this.workPlace.y, this.workPlace.z) < 20.0D) {
                            this.folk.setStatus(I18n.format("container.sim.folk_data_Going_work"));
                            this.folk.forceMoveToXYZ(this.workPlace);
                        } else {
                            this.folk.setStatus(I18n.format("container.sim.folk_data_Going_work"));
                            this.folk.entity.setPositionAndUpdate(this.workPlace.x + 0.5D, this.workPlace.y + 1.0D, this.workPlace.z + 0.5D);
                            this.folk.entity.getNavigator().clearPathEntity();
                        }

                        this.folk.stayPut = true;
                    }

                    if (this.onWayToWork && this.folk.isAtLocation(this.workPlace)) {
                        this.atWork = true;
                        this.onWayToWork = false;
                        this.onArrive();
                    }

                    if (this.grabItemsFromBuilding(this.collectionBuilding)) {
                        if (this.itemGrabTimer == 0L) {
                            this.itemGrabTimer = System.currentTimeMillis();
                        } else if (System.currentTimeMillis() - this.itemGrabTimer > 5000L) {
                            this.itemGrabTimer = System.currentTimeMillis();
                            List<IInventory> chests = this.inventoriesFindClosest(this.folk.getV3(), 5);

                            for (IInventory inv:chests){
                                for(int i = 0; i < inv.getSizeInventory(); ++i) {
                                    for (int j = 0; j < this.collectionItems.size(); j++) {
                                        Item item=this.collectionItems.get(j);
                                        if (inv.getStackInSlot(i).getItem().getUnlocalizedName().contentEquals(item.getUnlocalizedName())) {
                                        }
                                        this.folk.inventory.add(inv.getStackInSlot(i));
                                        inv.removeStackFromSlot(i);
                                    }
                                }
                            }

                            this.onCollectItem();
                        }
                    }

                }
            }
        }
    }

    public List<IInventory> inventoriesFindClosest(V3 startXYZ, int searchDistance) {
        List ret = new CopyOnWriteArrayList();

        try {
            World world = this.folk.entity.worldObj;
            TileEntity te = world.getTileEntity(startXYZ.toBlockPos());
            if (te != null && te instanceof IInventory && !(te instanceof TileEntityFurnace)) {
                ret.add((IInventory)te);
            }

            for(int d = 1; d < searchDistance; ++d) {
                for(int yo = -d; yo <= d; ++yo) {
                    for(int xo = -d; xo <= d; ++xo) {
                        for(int zo = -d; zo <= d; ++zo) {
                            int sx = (int)(Math.round(startXYZ.x) + (long)xo);
                            int sy = (int)(Math.round(startXYZ.y) + (long)yo);
                            int sz = (int)(Math.round(startXYZ.z) + (long)zo);
                            te = world.getTileEntity(new BlockPos(sx, sy, sz));
                            if (te != null && te instanceof IInventory && !this.alreadyGotChest(ret, (IInventory)te)) {
                                ret.add((IInventory)te);
                            }
                        }
                    }
                }
            }

            return ret;
        } catch (Exception var13) {
            return ret;
        }
    }

    private boolean alreadyGotChest(List<IInventory> chests, IInventory chest) {
        boolean ret = false;
        Iterator var4 = chests.iterator();

        while(var4.hasNext()) {
            IInventory ch = (IInventory)var4.next();
            if (ch.toString().contentEquals(chest.toString())) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public void onSecond() {
        if (this.currentTask != null) {
            this.currentTask.onSecond();
        }

    }

    public void onMinute() {
    }

    public List<IInventory> findJobChests(int radius) {
        this.jobChests.clear();
        List<IInventory> inventoriesFindClosest= this.inventoriesFindClosest(this.workPlace, radius);
        for (IInventory i:inventoriesFindClosest){
            jobChests.add(i);
        }
        return this.jobChests;
    }

    public boolean placeInJobChest(ItemStack item) {
        if (this.jobChests.size() > 0) {
            for (IInventory chest:this.jobChests){
                if (this.placeInJobChest(chest, item)) {
                    return true;
                }
            }
        } else {
            List<IInventory> inventoriesFindClosest= this.inventoriesFindClosest(this.workPlace, 5);
            for (IInventory i:inventoriesFindClosest){
                if (this.placeInJobChest(i, item)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean placeInJobChest(IInventory chest, ItemStack item) {
        boolean placedOK = false;
        if (item == null) {
            ModSimLoader.log.info("试图放置空项");
            return true;
        } else {
            for(int itemNumber = 1; itemNumber <= item.stackSize; ++itemNumber) {
                for(int chestSlot = 0; chestSlot < chest.getSizeInventory(); ++chestSlot) {
                    ItemStack is = chest.getStackInSlot(chestSlot);
                    if (is==null||is.getDisplayName().contentEquals("Air")) {
                        is = item.copy();
                        is.stackSize=1;
                        chest.setInventorySlotContents(chestSlot, is);
                        ItemStack isTest = chest.getStackInSlot(chestSlot);
                        if (isTest != null) {
                            placedOK = true;
                            break;
                        }

                        placedOK = false;
                    } else if (is.getItem().getUnlocalizedName().contentEquals(item.getItem().getUnlocalizedName()) && is.stackSize < is.getMaxStackSize()) {
                        int isBefore = chest.getStackInSlot(chestSlot).stackSize;
                        is.stackSize=is.stackSize+1;
//                        is.setCount(is.getCount() + 1);
                        chest.setInventorySlotContents(chestSlot, is);
                        int isAfter = chest.getStackInSlot(chestSlot).stackSize;
                        if (isAfter > isBefore) {
                            placedOK = true;
                            break;
                        }

                        placedOK = false;
                    }
                }
            }

            if (!placedOK) {
                this.stuckItem = item.copy();
            }

            return placedOK;
        }
    }

    public int feedFolks() {
        int fedFolks = 0;
        Iterator var2 = ModSimLoader.folks.iterator();

        while(true) {
            label35:
            while(true) {
                NpcData fd;
                do {
                    if (!var2.hasNext()) {
                        return fedFolks;
                    }

                    fd = (NpcData)var2.next();
                } while(fd.hunger >= 10);

                Iterator var4 = this.inventoriesFindClosest(this.workPlace, 5).iterator();

                while(var4.hasNext()) {
                    IInventory chest = (IInventory)var4.next();

                    for(int i = 0; i < chest.getSizeInventory(); ++i) {
                        ItemStack is = chest.getStackInSlot(i);
                        if (is.getItem() instanceof ItemFood) {
                            ++fedFolks;
                            fd.hunger = 10;
                            chest.decrStackSize(i, 1);
                            continue label35;
                        }
                    }
                }
            }
        }
    }

    public boolean isNearBlock(Block block) {
        return false;
    }

    public void collectFromBuilding(String buildingName, ItemStack is) {
        List<Building> potentialBuildings = ModSimLoader.getClosestBuilding(buildingName, this.workPlace);
        this.folk.moveToXYZ(((Building)potentialBuildings.get(0)).controlXYZ);
    }

    public void collectFromBuilding(LinkedHashMap<Building, ItemStack> col) {
        if (this.collectionBuilding == null) {
            this.colBs = col;
            this.collectionBuilding = (Building)col.keySet().toArray()[this.colCount];
        }
    }

    public void collectFromBuilding() {
        if (this.collectionBuilding == null) {
            if (this.colCount >= this.colBs.size() - 1) {
                this.colCount = 0;
                this.colBs = null;
                this.collectionBuilding = null;
                this.hasCollected = true;
            } else {
                this.collectionBuilding = (Building)this.colBs.keySet().toArray()[this.colCount];
            }
        }
    }

    public void collectFromBuilding(Building building, float ms) {
        this.folk.moveToXYZ(building.controlXYZ);
        this.collectionBuilding = building;
        this.hasCollected = false;
    }

    boolean grabItemsFromBuilding(Building building) {
        if (building == null) {
            return false;
        } else {
            int dist = this.folk.getV3().getDistanceTo(building.controlXYZ);
            if (dist < 3 && this.folk.entity != null) {
                this.folk.entity.motionX = 0.0D;
                this.folk.entity.motionZ = 0.0D;
                return true;
            } else {
                return false;
            }
        }
    }

    public void addCollectionPoint(String buildingName, ItemStack is) {
        List<Building> potentialBuildings = ModSimLoader.getClosestBuilding(buildingName, this.workPlace);
        if (potentialBuildings.size() > 0) {
            this.collectionPoints.put(potentialBuildings.get(0), is);
        }

    }

    public void onCollectItem() {
        ++this.colCount;
        this.collectionBuilding = null;
        this.collectFromBuilding();
    }

    public void addJobTask(JobTask jtask) {
        this.jobTasks.add(jtask);
    }



    public void onArrive() {
        this.folk.stayPut = true;
        if (this.stage == -1) {
            this.stage = 0;
        }

        if (this.jobTasks.size() > 0) {
            this.currentTask = (JobTask)this.jobTasks.get(this.stage);
            this.currentTask.begin();
        }

    }

    public abstract String toString();
}
