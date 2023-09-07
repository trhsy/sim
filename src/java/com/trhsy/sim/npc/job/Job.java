package com.trhsy.sim.npc.job;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.task.JobTask;
import com.trhsy.sim.util.PricesForBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
    public List<Item> collectionItems = new CopyOnWriteArrayList<Item>();
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
//        folk.respawn(world, pos);
        this.workPlace = new V3(pos);
        this.jobWorld = world;
    }

    public Job(NpcData folk, V3 pos, World world) {
        this.folk = folk;
//        folk.respawn(world, pos.toBlockPos());
        this.workPlace = pos;
        this.jobWorld = world;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 下一个任务
     * @Date 10:33 2022/10/21
     * @Param []
     **/
    public void nextTask() {
        try {
//        ModSimLoader.log.info("移动到下一个任务");
            int curTask = 0;

            for (int i = 0; i < this.jobTasks.size(); ++i) {
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
                ModSimLoader.log.info("将任务[" + this.jobName + "]更改为 " + this.jobTasks.get(curTask + 1) + "(" + curTask + 1 + ")");
                this.stage = curTask + 1;
                this.currentTask = (JobTask) this.jobTasks.get(curTask + 1);
                this.currentTask.begin();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("Job-nextTask出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 工作更新回调
     * @Date 10:42 2022/10/21
     * @Param []
     **/
    public void onUpdate() {
        try {
            //NPC数据不为空
            if (this.folk != null) {
                //实体不为空
                if (this.folk.entity != null) {
                    //是否应该工作
                    if (this.folk.shouldWork()) {
                        //固定不动
                        this.folk.stayPut = true;
                    }
                    //不应该工作，但在工作
                    if (!this.folk.shouldWork() && this.atWork) {
                        //清楚状态
                        this.folk.clearStatus();
                        //可以行动
                        this.folk.stayPut = false;
                        //清除工作状态
                        this.atWork = false;
                        //在工作途中
                        this.onWayToWork = false;
                        //当前任务为空
                        this.currentTask = null;
                    } else {
                        //如果当前任务不为空，则更新任务
                        if (this.currentTask != null) {
                            this.currentTask.update();
                        }
                        //应该工作，但是没有在工作，并且不是服务器端
                        if (this.folk.shouldWork() && !this.atWork && !this.folk.entity.worldObj.isRemote) {
                            //设置去工作途中
                            this.onWayToWork = true;
                            //获取距离，并且小于20
                            if (this.folk.entity.getDistance(this.workPlace.x, this.workPlace.y, this.workPlace.z) < 20.0D) {
                                //去工作
                                this.folk.setStatus(I18n.format("container.sim.folk_data_Going_work"));
                                //强制瞬移过去
                                if (!this.folk.forceMoveToXYZ(this.workPlace)) {
                                    this.folk.forceMoveToXYZNoWarp(this.workPlace);
                                }
                            } else {
                                //去工作 走过去
                                this.folk.setStatus(I18n.format("container.sim.folk_data_Going_work"));
                                this.folk.entity.setPositionAndUpdate(this.workPlace.x + 0.5D, this.workPlace.y + 1.0D, this.workPlace.z + 0.5D);
                                this.folk.entity.getNavigator().clearPathEntity();
                            }
                            //设置固定不动
                            this.folk.stayPut = true;
                        }
                        //在去工作途中，并且已经到了工作位置则更新状态
                        if (this.onWayToWork && this.folk.isAtLocation(this.workPlace)) {
                            //工作中
                            this.atWork = true;
                            //没有在工作途中
                            this.onWayToWork = false;
                            //到达指定地址
                            this.onArrive();
                        }
                        //从建筑中抓取项目
                        if (this.grabItemsFromBuilding(this.collectionBuilding)) {
                            if (this.itemGrabTimer == 0L) {
                                this.itemGrabTimer = System.currentTimeMillis();
                            } else if (System.currentTimeMillis() - this.itemGrabTimer > 5000L) {
                                this.itemGrabTimer = System.currentTimeMillis();
                                List<IInventory> chests = this.inventoriesFindClosest(this.folk.getV3(), 5);

                                for (IInventory inv : chests) {
                                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                        for (int j = 0; j < this.collectionItems.size(); j++) {
                                            Item item = this.collectionItems.get(j);
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
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 查找最近的箱子
     *
     * @param startXYZ
     * @param searchDistance
     * @return
     */
    public List<IInventory> inventoriesFindClosest(V3 startXYZ, int searchDistance) {
        List ret = new CopyOnWriteArrayList();

        try {
            World world = this.folk.entity.worldObj;
            TileEntity te = world.getTileEntity(startXYZ.toBlockPos());
            if (te != null && te instanceof IInventory && !(te instanceof TileEntityFurnace)) {
                ret.add((IInventory) te);
            }

            for (int d = 1; d < searchDistance; ++d) {
                for (int yo = -d; yo <= d; ++yo) {
                    for (int xo = -d; xo <= d; ++xo) {
                        for (int zo = -d; zo <= d; ++zo) {
                            int sx = (int) (Math.round(startXYZ.x) + (long) xo);
                            int sy = (int) (Math.round(startXYZ.y) + (long) yo);
                            int sz = (int) (Math.round(startXYZ.z) + (long) zo);
                            te = world.getTileEntity(new BlockPos(sx, sy, sz));
                            if (te != null && te instanceof IInventory && !this.alreadyGotChest(ret, (IInventory) te)) {
                                ret.add((IInventory) te);
                            }
                        }
                    }
                }
            }

            return ret;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-inventoriesFindClosest出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return ret;
        }
    }

    /**
     * 已经获得箱子
     *
     * @param chests
     * @param chest
     * @return
     */
    private boolean alreadyGotChest(List<IInventory> chests, IInventory chest) {
        boolean ret = false;
        Iterator var4 = chests.iterator();
        try {
            while (var4.hasNext()) {
                IInventory ch = (IInventory) var4.next();
                if (ch.toString().contentEquals(chest.toString())) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-alreadyGotChest出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return ret;
    }

    /**
     * 每秒
     */
    public void onSecond() {
        try {
            if (this.currentTask != null) {
                this.currentTask.onSecond();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-onSecond出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

    /**
     * 每分钟
     */
    public void onMinute() {
        try {

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-onMinute出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return java.util.List<net.minecraft.inventory.IInventory>
     * @Author fan
     * @Description //TODO 查找工作箱子
     * @Date 21:21 2022/12/8
     * @Param [radius]
     **/
    public List<IInventory> findJobChests(int radius) {
        try {
            this.jobChests.clear();
            List<IInventory> inventoriesFindClosest = this.inventoriesFindClosest(this.workPlace, radius);
            for (IInventory i : inventoriesFindClosest) {
                jobChests.add(i);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-findJobChests出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return this.jobChests;
    }

    /**
     * 将物品放置到箱子里
     *
     * @param item
     * @return
     */
    public boolean placeInJobChest(ItemStack item) {
        try {
            if (this.jobChests.size() > 0) {
                for (IInventory chest : this.jobChests) {
                    if (this.placeInJobChest(chest, item)) {
                        return true;
                    }
                }
            } else {
                List<IInventory> inventoriesFindClosest = this.inventoriesFindClosest(this.workPlace, 5);
                for (IInventory i : inventoriesFindClosest) {
                    if (this.placeInJobChest(i, item)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("placeInJobChest出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return false;
    }

    /**
     * 将物品放置到箱子里
     *
     * @param chest
     * @param item
     * @return
     */
    public boolean placeInJobChest(IInventory chest, ItemStack item) {
        boolean placedOK = false;
        try {
            if (item == null) {
                ModSimLoader.log.info("试图放置空项");
                return true;
            } else {
                for (int itemNumber = 1; itemNumber <= item.stackSize; ++itemNumber) {
                    for (int chestSlot = 0; chestSlot < chest.getSizeInventory(); ++chestSlot) {
                        ItemStack is = chest.getStackInSlot(chestSlot);
                        if (is == null || is.getDisplayName().contentEquals("Air")) {
                            is = item.copy();
                            is.stackSize = 1;
                            chest.setInventorySlotContents(chestSlot, is);
                            ItemStack isTest = chest.getStackInSlot(chestSlot);
                            if (isTest != null) {
                                placedOK = true;
                                break;
                            }

                            placedOK = false;
                        } else if (is.getItem().getUnlocalizedName().contentEquals(item.getItem().getUnlocalizedName()) && is.stackSize < is.getMaxStackSize()) {
                            int isBefore = chest.getStackInSlot(chestSlot).stackSize;
                            is.stackSize = is.stackSize + 1;
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
        } catch (Exception e) {
            placedOK = false;
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-placeInJobChest出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return placedOK;
        }
    }

    /**
     *将一些物品/任何物品从一组箱子中转移到npc的库存中
     * @param fromChests 箱子在哪里得到它们
     * @return
     */
    public boolean inventoriesTransferToFolk(List<IInventory> fromChests) {
        boolean ret = false;
        try {
            int limit = 0;
            ItemStack got = null;
            do {
                for (int c = 0; c < fromChests.size(); ++c) {
                    IInventory chest = fromChests.get(c);
                    for (int g = 0; g < chest.getSizeInventory(); g++) {
                        ItemStack chestStack = chest.getStackInSlot(g);
                        this.folk.inventory.add(chestStack);
                        chest.removeStackFromSlot(g);
                    }
                }
            } while (got != null && limit < 64);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("将一些物品/任何物品从一组箱子中转移到人们的库存中出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * @return int
     * @Author fan
     * @Description //TODO 喂养npc
     * @Date 17:44 2022/12/10
     * @Param []
     **/
    public int feedFolks() {
        int fedFolks = 0;
        try {
            Iterator var2 = ModSimLoader.folks.iterator();
            while (true) {
                label35:
                while (true) {
                    NpcData fd;
                    do {
                        if (!var2.hasNext()) {
                            return fedFolks;
                        }

                        fd = (NpcData) var2.next();
                    } while (fd.hunger >= 10);
                    Iterator var4 = this.inventoriesFindClosest(this.workPlace, 5).iterator();
                    while (var4.hasNext()) {
                        IInventory chest = (IInventory) var4.next();
                        for (int i = 0; i < chest.getSizeInventory(); ++i) {
                            ItemStack is = chest.getStackInSlot(i);
                            if (is != null) {
                                Item item = is.getItem();
                                if (item != null) {
                                    if (item instanceof ItemFood) {
                                        ItemFood itemFood = (ItemFood) item;
                                        int healAmount = itemFood.getHealAmount(is);
                                        ModSimLoader.log.info("食物：" + itemFood.getUnlocalizedName() + ",增加饱和度：" + healAmount);
                                        ++fedFolks;
                                        fd.hunger += healAmount;
                                        chest.decrStackSize(i, 1);
                                        continue label35;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-feedFolks出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return fedFolks;
        }
    }

    public boolean isNearBlock(Block block) {
        return false;
    }

    /**
     * 来自建筑的集合
     *
     * @param buildingName
     * @param is
     */
    public void collectFromBuilding(String buildingName, ItemStack is) {
        List<Building> potentialBuildings = ModSimLoader.getClosestBuilding(buildingName, this.workPlace);
        this.folk.forceMoveToXYZ(((Building) potentialBuildings.get(0)).controlXYZ);
    }

    /**
     * 来自建筑的集合
     *
     * @param col
     */
    public void collectFromBuilding(LinkedHashMap<Building, ItemStack> col) {
        if (this.collectionBuilding == null) {
            this.colBs = col;
            this.collectionBuilding = (Building) col.keySet().toArray()[this.colCount];
        }
    }

    /**
     * 来自建筑的集合
     */
    public void collectFromBuilding() {
        try {
            if (this.collectionBuilding == null) {
                if (this.colCount >= this.colBs.size() - 1) {
                    this.colCount = 0;
                    this.colBs = null;
                    this.collectionBuilding = null;
                    this.hasCollected = true;
                } else {
                    this.collectionBuilding = (Building) this.colBs.keySet().toArray()[this.colCount];
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-collectFromBuilding出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 来自建筑的集合
     *
     * @param building
     * @param ms
     */
    public void collectFromBuilding(Building building, float ms) {
        this.folk.forceMoveToXYZ(building.controlXYZ);
        this.collectionBuilding = building;
        this.hasCollected = false;
    }

    /**
     * 从建筑中抓取项目
     *
     * @param building
     * @return
     */
    public boolean grabItemsFromBuilding(Building building) {
        try {
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
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-grabItemsFromBuilding出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return false;
        }
    }

    public void addCollectionPoint(String buildingName, ItemStack is) {
        try {
            List<Building> potentialBuildings = ModSimLoader.getClosestBuilding(buildingName, this.workPlace);
            if (potentialBuildings.size() > 0) {
                this.collectionPoints.put(potentialBuildings.get(0), is);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-addCollectionPoint出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public void onCollectItem() {
        try {
            ++this.colCount;
            this.collectionBuilding = null;
            this.collectFromBuilding();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-onCollectItem出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    public void addJobTask(JobTask jtask) {
        this.jobTasks.add(jtask);
    }

    /**
     * 将选定的玩家库存出售给商家
     */
    public void sellStuff() {
        try {
            ItemStack stack = null;
            int quant = 0;
            Block block = null;
            boolean ok = false;
            Float stackPrice = 0.0F;
            int stackCount = 0;
            List<IInventory> chests = inventoriesFindClosest(this.workPlace, 5);
            if (chests == null | chests.size() == 0) {
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant13"));//商人：请在这里放一个箱子,然后在里面放64个物品。
                return;
            }
            float total = 0.0F;
            IInventory iInventory = chests.get(0);
            for (int g = 0; g < iInventory.getSizeInventory(); g++) {

                ItemStack is = iInventory.getStackInSlot(g);
                if (is != null) {
                    if (is.stackSize == 64) {
                        stackPrice = PricesForBlocks.getPrice(Block.getBlockFromItem(is.getItem()), false);
                        if (stackPrice > 0.0F) {
                            //64 * 基本价格
                            ModSimLoader.money += stackPrice;
                            PricesForBlocks.adjustPrice((Block) block, false);
                            total += stackPrice;
                            (chests.get(0)).setInventorySlotContents(g, (ItemStack) null);
                        }
                    }
                }
            }
            if (total == 0.0F) {
                //箱子里没有我想从你那里买的有效堆栈？
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant14"));
            } else {
                SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                Minecraft mc = Minecraft.getMinecraft();
                for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                    mc.theWorld.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                }
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant15") + ModSimLoader.displayMoney(total));
            }


        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-sellStuff出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 买东西
     * 购买当前显示在购买页面上的东西
     */
    public void buyStuff(List<Integer> quantities) {
        try {
            ModSimLoader.log.info("准备买东西");
            ItemStack stack = null;
            int quant = 0;
            Block block = null;
            boolean ok = false;
            Float stackPrice = 0.0F;
            //找到箱子
            List<IInventory> chests = inventoriesFindClosest(this.workPlace, 5);
            if (chests != null && chests.size() != 0) {
                for (int i = 0; i < 9; i++) {
                    quant = quantities.get(i);
                    //ModSimLoader.log.info(String.valueOf(quant));
                    if (quant > 0) {
                        if (i == 0) {
                            block = Blocks.PLANKS;
                        } else if (i == 1) {
                            block = Blocks.LOG;
                        } else if (i == 2) {
                            block = Blocks.COBBLESTONE;
                        } else if (i == 3) {
                            block = Blocks.STONE;
                        } else if (i == 4) {
                            block = Blocks.GLASS;
                        } else if (i == 5) {
                            block = Blocks.WOOL;
                        } else if (i == 6) {
                            block = Blocks.BRICK_BLOCK;
                        } else if (i == 7) {
                            block = Blocks.STONEBRICK;
                        } else if (i == 8) {
                            block = Blocks.OAK_FENCE;
                        }

                        for (int c = 1; c <= quant; c++) {
                            stack = new ItemStack(block, 64);
//                            this.placeIntoChest(chests.get(0), stack, stack.getMetadata(), 64);
                            this.placeInJobChest(stack);
                            stackPrice = PricesForBlocks.getPrice(block, true);
                            //64 * 基本价格 + 25% 加价
                            ModSimLoader.money -= stackPrice;
                        }

                        PricesForBlocks.adjustPrice(block, true);
                    }
                }

                SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                Minecraft mc = Minecraft.getMinecraft();
                for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                    mc.theWorld.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                }
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                }
                SoundEvent soundEvent1 = new SoundEvent(new ResourceLocation(ModSim.MODID + ":merchm"));
                for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                    mc.theWorld.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent1, SoundCategory.AMBIENT, 1.0F, 1.0F);
                }
                //threadPoolExecutor.shutdown();
            } else {
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant12"));
                return;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-buyStuff出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 到底工作地点时
     */
    public void onArrive() {
        try {
            this.folk.stayPut = true;
            if (this.stage != 0) {
                this.stage = 0;
            }

            if (this.jobTasks.size() > 0) {
                this.currentTask = this.jobTasks.get(this.stage);
                this.currentTask.begin();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("job-onArrive出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

    @Override
    public abstract String toString();
}
