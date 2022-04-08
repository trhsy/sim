package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.functionality.TileEntityWindmill;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.*;

/**
 * ========================================
 *
 * @ClassName Job
 * @Description todo 工作类
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:32
 * ========================================
 **/
public abstract class Job {
    //返回游戏的singleton Minecraft实例
    Minecraft mc = Minecraft.getMinecraft();
    //步
    public int step = 1;
    ArrayList<V3> closestBlocks = new ArrayList();
    //职业
    public Vocation vocation = null;
    //职场
    public World jobWorld = null;
    //库存 箱子关闭
    private transient IInventory chestToClose = null;
    //箱子什么时候关闭
    private transient Long chestToCloseWhen = 0L;

    public Job() {
    }
    /**
     * @Author fan
     * @Description //TODO  他刚上班
     * @Date 10:59 2022/3/26
     * @Param
     * @return
     **/
    public abstract void onArrivedAtWork();
    /**
     * @Author fan
     * @Description //TODO 重新安排工作
     * @Date 11:00 2022/3/26
     * @Param []
     * @return void
     **/
    public abstract void resetJob();
    /**
     * @Author fan
     * @Description //TODO 更新
     * @Date 11:00 2022/3/26
     * @Param []
     * @return void
     **/
    public void onUpdate() {
        //当 关闭箱子不为空   并且 当前时间毫秒 大约关闭箱子时
        if (this.chestToClose != null && System.currentTimeMillis() > this.chestToCloseWhen) {
            //关上箱子
            this.chestToClose.closeChest();
            //设置为空
            this.chestToClose = null;
        }

    }
    /**
     * @Author fan
     * @Description //TODO     //去上班
     * @Date 11:02 2022/3/26
     * @Param [theFolk] 实体人数据
     * @return void
     **/
    public void onUpdateGoingToWork(FolkData theFolk) {
        //如果当前职场为空 则设置职场
        if (this.jobWorld == null) {
            try {
                this.jobWorld = MinecraftServer.getServer().worldServerForDimension(theFolk.employedAt.theDimension);
            } catch (Exception var7) {
                System.out.println(var7.getMessage());
                return;
            }
        }
        //如果不是在怀孕期间
        if (!(theFolk.pregnancyStage > 0.0F)) {
            //在上班的路上
            if (theFolk.action == FolkAction.ONWAYTOWORK) {
                //int dist = false;
                //被解雇
                if (theFolk.gotoMethod == GotoMethod.WALK) {
                    //不再留在原地
                    theFolk.stayPut = false;
                    //更新实体人位置
                    theFolk.updateLocationFromEntity();
                }
                //获得距离
                int dist = theFolk.location.getDistanceTo(theFolk.employedAt);
                //如果距离小于等于1 工作中
                if (dist <= 1) {
                    //工作中
                    theFolk.action = FolkAction.ATWORK;
                    //他刚上班
                    this.onArrivedAtWork();
                    //如果大于1小于3
                } else if (dist > 1 && dist < 3) {
                    //复制当前数据
                    V3 work = theFolk.employedAt.clone();
                    //work.y = work.y + 1.0D;
                    work=new V3(work.x,work.y+1.0D,work.z,work.theDimension);
                    //去位置
                    theFolk.gotoXYZ(work, GotoMethod.SHIFT);
                    theFolk.location = work;
                }
            }
            //  是否白天               活动的              去工作路上                             活动中                工作中
            if (ModSimReloaded.isDayTime() && theFolk.action != FolkAction.ONWAYTOWORK && theFolk.action != FolkAction.ATWORK) {
                //活动设置为去工作路上
                theFolk.action = FolkAction.ONWAYTOWORK;
                //设置原地不动为否
                theFolk.stayPut = false;
                //如果目的地为空
                if (theFolk.destination == null) {
                    //设置目的地
                    theFolk.gotoXYZ(theFolk.employedAt, (GotoMethod) null);
                }
            }

        }
    }
    /**
     * @Author fan
     * @Description //TODO 获得物品库存清单
     * @Date 11:23 2022/3/26
     * @Param [theFolk, item]
     * @return int
     **/
    public int getInventoryCount(FolkData theFolk, Item item) {
        //声明库存为0
        int ret = 0;
        //循环库存
        for(int i = 0; i < theFolk.inventory.size(); ++i) {
            //当前方块的数量
            ItemStack is = (ItemStack)theFolk.inventory.get(i);
            //如果物品 对上
            if (is.getItem() == item) {
                //赋值物品数量
                ret += is.stackSize;
            }
        }

        return ret;
    }

    /**
     *  获得方块的库存
     * @param theFolk
     * @param item
     * @return
     */
    public int getInventoryCount(FolkData theFolk, Block item) {
        //声明库存为0
        int ret = 0;
        //循环库存
        for(int i = 0; i < theFolk.inventory.size(); ++i) {
            //当前方块的数量
            ItemStack is = (ItemStack)theFolk.inventory.get(i);
            //如果物品 对上
            if (Block.getBlockFromName(is.getDisplayName()) == item) {
                //赋值物品数量
                ret += is.stackSize;
            }
        }

        return ret;
    }

    /**
     * 找到熔炉
     * @param v
     * @return
     */
    public TileEntityFurnace findFurnace(V3 v) {
        //声明熔炉实体
        TileEntityFurnace ret = null;
        //查找最近的块类型
        V3 vRet = findClosestBlockType(v, Blocks.furnace, 5, false);
        //如果等于空重新赋值
        if (vRet == null) {
            vRet = findClosestBlockType(v, Blocks.furnace, 5, false);
        }
        //如果不为空
        if (vRet != null) {
            //世界服务器的维度                                                       维度
            World theWorld = MinecraftServer.getServer().worldServerForDimension(vRet.theDimension);
            //设置熔炉位置 转换为int
            ret = (TileEntityFurnace)theWorld.getTileEntity(vRet.x.intValue(), vRet.y.intValue(), vRet.z.intValue());
        }

        return ret;
    }

    /**
     * 存货卖出价
     * @param chest
     * @param inStack
     * @return
     */
    private static boolean inventoryPut(IInventory chest, ItemStack inStack) {
        Boolean placedOK = false;
        //如果物品为空
        if (inStack == null) {
            return true;
        } else {
            //
            for(int q = 1; q <= inStack.stackSize; ++q) {
                for(int i = 0; i < chest.getSizeInventory(); ++i) {
                    ItemStack is = chest.getStackInSlot(i);
                    if (is == null) {
                        is = inStack.copy();
                        is.stackSize = 1;
                        //将给定的物品堆栈设置为库存中的指定位置（可以是工艺或装甲部分）。
                        chest.setInventorySlotContents(i, is);
                        ItemStack isTest = chest.getStackInSlot(i);
                        if (isTest != null) {
                            placedOK = true;
                            break;
                        }

                        ModSimReloaded.log.warning("Job: placeIntoInventory() 无法将 " + is.getDisplayName() + " 放入空槽 " + i);
                        placedOK = false;
                    } else if (is.getItem() == inStack.getItem() && is.getMetadata() == inStack.getMetadata() && is.stackSize < is.getMaxStackSize()) {
                        int isBefore = chest.getStackInSlot(i).stackSize;
                        ++is.stackSize;
                        //将给定的物品堆栈设置为库存中的指定位置（可以是工艺或装甲部分）。
                        chest.setInventorySlotContents(i, is);
                        int isAfter = chest.getStackInSlot(i).stackSize;
                        if (isAfter > isBefore) {
                            placedOK = true;
                            break;
                        }

                        ModSimReloaded.log.warning("Job: placeIntoInventory() 无法更改大小 " + is.getDisplayName() + " in slot " + i);
                        placedOK = false;
                    }
                }
            }

            return placedOK;
        }
    }

    /**
     *  得到库存
     * @param chests
     * @param whatItem
     * @param getRandomItem
     * @param compareMeta
     * @param ignoreId
     * @return
     */
    public static ItemStack inventoriesGet(ArrayList<IInventory> chests, ItemStack whatItem, boolean getRandomItem, boolean compareMeta, ItemStack ignoreId) {
        ItemStack retStack = null;

        for(int c = 0; c < chests.size(); ++c) {
            IInventory chest = (IInventory)chests.get(c);
            retStack = inventoryGet(chest, whatItem, getRandomItem, compareMeta);
            if (retStack != null) {
                return retStack;
            }
        }

        return null;
    }

    /**
     *
     * @param chests
     * @param whatItem
     * @param getRandomItem
     * @param compareMeta
     * @return
     */
    public static ItemStack inventoriesGet(ArrayList<IInventory> chests, ItemStack whatItem, boolean getRandomItem, boolean compareMeta) {
        ItemStack retStack = null;

        for(int c = 0; c < chests.size(); ++c) {
            IInventory chest = (IInventory)chests.get(c);
            retStack = inventoryGet(chest, whatItem, getRandomItem, compareMeta);
            if (retStack != null) {
                return retStack;
            }
        }

        return null;
    }

    private static ItemStack inventoryGet(IInventory chest, ItemStack whatItem, boolean getRandomItem, boolean compareMeta, ItemStack ignoreId) {
        ItemStack returnStack;
        int g;
        ItemStack chestStack;
        if (whatItem == null) {
            if (getRandomItem) {
                returnStack = null;
                ArrayList<Integer> slots = new ArrayList();

                for(int i = 0; i < chest.getSizeInventory(); ++i) {
                    chestStack = chest.getStackInSlot(i);
                    if (chestStack != null) {
                        slots.add(i);
                    }
                }

                if (slots.size() == 0) {
                    return null;
                } else {
                    returnStack = chest.getStackInSlot((new Random()).nextInt(slots.size()));
                    return returnStack;
                }
            } else {
                returnStack = null;

                for(g = 0; g < chest.getSizeInventory(); ++g) {
                    ItemStack chestStackStack = chest.getStackInSlot(g);
                    if (chestStackStack != null) {
                        returnStack = chestStackStack.copy();
                        chest.setInventorySlotContents(g, (ItemStack)null);
                        return returnStack;
                    }
                }

                return returnStack;
            }
        } else {
            returnStack = whatItem.copy();
            returnStack.stackSize = 0;

            for(g = 0; g < chest.getSizeInventory(); ++g) {
                boolean ignore = false;
                chestStack = chest.getStackInSlot(g);
                if (ignoreId != null && chestStack == ignoreId) {
                    ignore = true;
                }

                if (chestStack != null && !ignore) {
                    if (!compareMeta) {
                        chestStack.setMetadata(whatItem.getMetadata());
                    }

                    if (chestStack.isItemEqual(whatItem)) {
                        while(chestStack.stackSize >= 1) {
                            ++returnStack.stackSize;
                            --chestStack.stackSize;
                            if (chestStack.stackSize <= 0) {
                                chest.setInventorySlotContents(g, (ItemStack)null);
                            }

                            if (returnStack.stackSize == whatItem.stackSize) {
                                return returnStack;
                            }
                        }
                    }
                }
            }

            if (returnStack.stackSize > 0) {
                return returnStack;
            } else {
                return null;
            }
        }
    }

    private static ItemStack inventoryGet(IInventory chest, ItemStack whatItem, boolean getRandomItem, boolean compareMeta) {
        ItemStack returnStack;
        int g;
        ItemStack chestStack;
        if (whatItem == null) {
            if (getRandomItem) {
                returnStack = null;
                ArrayList<Integer> slots = new ArrayList();

                for(int i = 0; i < chest.getSizeInventory(); ++i) {
                    chestStack = chest.getStackInSlot(i);
                    if (chestStack != null) {
                        slots.add(i);
                    }
                }

                if (slots.size() == 0) {
                    return null;
                } else {
                    returnStack = chest.getStackInSlot((new Random()).nextInt(slots.size()));
                    return returnStack;
                }
            } else {
                returnStack = null;

                for(g = 0; g < chest.getSizeInventory(); ++g) {
                    ItemStack chestStackStack = chest.getStackInSlot(g);
                    if (chestStackStack != null) {
                        returnStack = chestStackStack.copy();
                        chest.setInventorySlotContents(g, (ItemStack)null);
                        return returnStack;
                    }
                }

                return returnStack;
            }
        } else {
            returnStack = whatItem.copy();
            returnStack.stackSize = 0;

            for(g = 0; g < chest.getSizeInventory(); ++g) {
                boolean ignore = false;
                chestStack = chest.getStackInSlot(g);
                if (chestStack != null && !ignore) {
                    if (!compareMeta) {
                        chestStack.setMetadata(whatItem.getMetadata());
                    }

                    if (chestStack.isItemEqual(whatItem)) {
                        while(chestStack.stackSize >= 1) {
                            ++returnStack.stackSize;
                            --chestStack.stackSize;
                            if (chestStack.stackSize <= 0) {
                                chest.setInventorySlotContents(g, (ItemStack)null);
                            }

                            if (returnStack.stackSize == whatItem.stackSize) {
                                return returnStack;
                            }
                        }
                    }
                }
            }

            if (returnStack.stackSize > 0) {
                return returnStack;
            } else {
                return null;
            }
        }
    }

    public boolean inventoriesPut(ArrayList<IInventory> chests, ItemStack inStack, boolean doOpenClose) {
        boolean placedOK = false;

        for(int i = 0; i < chests.size(); ++i) {
            IInventory chest = (IInventory)chests.get(i);
            if (doOpenClose) {
                this.openCloseChest(chest, 2000);
            }

            placedOK = inventoryPut(chest, inStack);
            if (placedOK) {
                break;
            }
        }

        return placedOK;
    }

    public static boolean inventoriesPut(ArrayList<IInventory> chests, ItemStack inStack) {
        boolean placedOK = false;

        for(int i = 0; i < chests.size(); ++i) {
            IInventory chest = (IInventory)chests.get(i);
            placedOK = inventoryPut(chest, inStack);
            if (placedOK) {
                break;
            }
        }

        return placedOK;
    }

    /**
     * 存货从民间转移
     * @param folkInventory
     * @param toChests
     * @param specificItems
     * @return
     */
    public boolean inventoriesTransferFromFolk(ArrayList<ItemStack> folkInventory, ArrayList<IInventory> toChests, ItemStack specificItems) {
        boolean placed = false;
        boolean okToPlace = false;

        for(int i = 0; i < folkInventory.size(); ++i) {
            try {
                ItemStack folkStack = (ItemStack)folkInventory.get(i);
                if (specificItems != null && specificItems.getItem() == folkStack.getItem()) {
                    okToPlace = true;
                } else if (specificItems == null) {
                    okToPlace = true;
                } else {
                    okToPlace = false;
                }

                if (okToPlace) {
                    placed = this.inventoriesPut(toChests, folkStack, true);
                    if (!placed) {
                        ModSimReloaded.log.warning("Job: 无法放置一堆 " + folkStack.getDisplayName() + " in chest");
                        return false;
                    }
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        folkInventory.clear();
        return true;
    }

    /**
     * 转移到民间
     * @param folkInventory
     * @param fromChests
     * @param whatItems
     * @param ignoreId
     * @return
     */
    public boolean inventoriesTransferToFolk(ArrayList<ItemStack> folkInventory, ArrayList<IInventory> fromChests, ItemStack whatItems, Block ignoreId) {
        boolean ret = false;
        int limit = 0;
        ItemStack got = null;

        for(int c = 0; c < fromChests.size(); ++c) {
            IInventory chest = (IInventory)fromChests.get(c);
            this.openCloseChest(chest, 2000);
        }

        do {
            got = inventoriesGet(fromChests, whatItems, false, false);
            if (got != null) {
                folkInventory.add(got);
                ret = true;
            }

            ++limit;
        } while(got != null && limit < 27);

        return ret;
    }

    /**
     * 转让仅限于民间
     * @param folkInventory
     * @param fromChests
     * @param whatItems
     * @param getQty
     * @param doCompareMeta
     * @return
     */
    public int inventoriesTransferLimitedToFolk(ArrayList<ItemStack> folkInventory, ArrayList<IInventory> fromChests, ItemStack whatItems, int getQty, boolean doCompareMeta) {
        int gotSoFar = 0;
        Iterator i$ = fromChests.iterator();

        while(i$.hasNext()) {
            IInventory chest = (IInventory)i$.next();

            for(int g = 0; g < chest.getSizeInventory(); ++g) {
                boolean gotMatch = false;
                ItemStack chestStack = chest.getStackInSlot(g);
                if (chestStack != null && chestStack == whatItems) {
                    if (doCompareMeta) {
                        if (chestStack.getMetadata() == whatItems.getMetadata()) {
                            gotMatch = true;
                        }
                    } else {
                        gotMatch = true;
                    }
                }

                if (gotMatch) {
                    while(gotSoFar < getQty && chestStack.stackSize > 0) {
                        ++gotSoFar;
                        --chestStack.stackSize;
                        folkInventory.add(new ItemStack(Block.getBlockFromItem(chestStack.getItem()), 1, chestStack.getMetadata()));
                    }

                    if (chestStack.stackSize > 0) {
                        chest.setInventorySlotContents(g, chestStack);
                    } else {
                        chest.setInventorySlotContents(g, (ItemStack)null);
                    }
                }

                if (gotSoFar == getQty) {
                    break;
                }
            }
        }

        return gotSoFar;
    }

    /**
     * 把物品放在箱子里
     * @param chests
     * @param is
     * @param doCompareMeta
     * @return
     */
    public int getItemCountInChests(ArrayList<IInventory> chests, ItemStack is, boolean doCompareMeta) {
        int ret = 0;
        Iterator i$ = chests.iterator();

        while(i$.hasNext()) {
            IInventory chest = (IInventory)i$.next();

            for(int g = 0; g < chest.getSizeInventory(); ++g) {
                ItemStack chestStack = chest.getStackInSlot(g);
                if (chestStack != null && chestStack == is) {
                    if (!doCompareMeta) {
                        ret += chestStack.stackSize;
                    } else if (chestStack.getMetadata() == is.getMetadata()) {
                        ret += chestStack.stackSize;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 开采时平移块体
     * @param world
     * @param location
     * @return
     */
    public ArrayList<ItemStack> translateBlockWhenMined(World world, V3 location) {
        int i = location.x.intValue();
        int j = location.y.intValue();
        int k = location.z.intValue();
        Block block = Blocks.air;
        if (block == null) {
            return null;
        } else {
            int meta = world.getBlockMetadata(i, j, k);
            return block.getDrops(world, i, j, k, meta, 0);
        }
    }

    /**
     * 开关箱子
     * @param chest
     * @param msDelay
     */
    public void openCloseChest(IInventory chest, int msDelay) {
        chest.openChest();
        this.chestToClose = chest;
        this.chestToCloseWhen = System.currentTimeMillis() + (long)msDelay;
    }

    /**
     * 设置最接近的类型块
     * @param startXYZ
     * @param blockIDs
     * @param distanceLimit
     * @param needsToSeeSky
     * @param scanDownwards
     * @param oneLayerOnly
     */
    public void setClosestBlocksOfType(final V3 startXYZ, final ArrayList<Block> blockIDs, final int distanceLimit, final boolean needsToSeeSky, final boolean scanDownwards, final boolean oneLayerOnly) {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
                HashMap hm = new HashMap();
                boolean skip = false;
                int YdistanceLimit = distanceLimit;
                if (oneLayerOnly) {
                    YdistanceLimit = 0;
                }

                for(int yo = 0; yo <= YdistanceLimit; ++yo) {
                    for(int d = 1; d < distanceLimit; ++d) {
                        for(int xo = -d; xo <= d; ++xo) {
                            for(int zo = -d; zo <= d; ++zo) {
                                int sx = startXYZ.x.intValue() + xo;
                                int sy;
                                if (scanDownwards) {
                                    sy = startXYZ.y.intValue() - yo;
                                } else {
                                    sy = startXYZ.y.intValue() + yo;
                                }

                                int sz = startXYZ.z.intValue() + zo;
                                skip = false;

                                for(int b = 0; b < blockIDs.size(); ++b) {
                                    Block blockID = (Block)blockIDs.get(b);
                                    if (theWorld == null) {
                                        return;
                                    }

                                    Block blockInWorld = theWorld.getBlock(sx, sy, sz);
                                    if (blockInWorld == blockID) {
                                        if (needsToSeeSky) {
                                            boolean canSeeSky;
                                            if (theWorld.getBlock(sx, sy + 1, sz) == null) {
                                                canSeeSky = true;
                                            } else {
                                                canSeeSky = false;
                                            }

                                            if (canSeeSky) {
                                                skip = false;
                                            } else {
                                                skip = true;
                                            }
                                        }

                                        if (!skip) {
                                            V3 v = new V3((double)sx, (double)sy, (double)sz, startXYZ.theDimension);
                                            if (!hm.containsKey(v.toString())) {
                                                hm.put(v.toString(), v);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Job.this.closestBlocks = new ArrayList(hm.values());
                Job.this.step = 3;
            }
        });
        t.start();
    }

    /**
     * 库存最接近
     * @param startXYZ
     * @param searchDistance
     * @return
     */
    public static ArrayList<IInventory> inventoriesFindClosest(V3 startXYZ, int searchDistance) {
        ArrayList ret = new ArrayList();

        try {
            World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
            TileEntity te = theWorld.getTileEntity(startXYZ.x.intValue(), startXYZ.y.intValue(), startXYZ.z.intValue());
            if (te != null && te instanceof IInventory && !(te instanceof TileEntityFurnace) && !(te instanceof TileEntityWindmill)) {
                ret.add((IInventory)te);
            }

            for(int d = 1; d < searchDistance; ++d) {
                for(int yo = -d; yo <= d; ++yo) {
                    for(int xo = -d; xo <= d; ++xo) {
                        for(int zo = -d; zo <= d; ++zo) {
                            int sx = startXYZ.x.intValue() + xo;
                            int sy = startXYZ.y.intValue() + yo;
                            int sz = startXYZ.z.intValue() + zo;
                            te = theWorld.getTileEntity(sx, sy, sz);
                            if (te != null && te instanceof IInventory && !(te instanceof TileEntityWindmill) && !alreadyGotChest(ret, (IInventory)te)) {
                                ret.add((IInventory)te);
                            }
                        }
                    }
                }
            }

            return ret;
        } catch (Exception var12) {
            return ret;
        }
    }

    /**
     * 已经有箱子了
     * @param chests
     * @param chest
     * @return
     */
    private static boolean alreadyGotChest(ArrayList<IInventory> chests, IInventory chest) {
        boolean ret = false;
        Iterator i$ = chests.iterator();

        while(i$.hasNext()) {
            IInventory ch = (IInventory)i$.next();
            if (ch.toString().contentEquals(chest.toString())) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    /**
     * 寻找相邻空间
     * @param startXYZ
     * @param world
     * @return
     */
    public static V3 findAdjacentSpace(V3 startXYZ, World world) {
        World theWorld = world;
        if (world == null) {
            theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
        }

        V3 test = startXYZ.clone();
        //Double var5 = test.x;
        //Double var6 = test.x = test.x + 1.0D;
        test=new V3(test.x+1.0D,test.y,test.z,test.theDimension);
        if (((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue())) {
            return test;
        } else {
            test = startXYZ.clone();

            test=new V3(test.x- 1.0D,test.y,test.z,test.theDimension);
            if (((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue())) {
                return test;
            } else {
                test = startXYZ.clone();
                test=new V3(test.x+1.0D,test.y,test.z,test.theDimension);
                if (((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue())) {
                    return test;
                } else {
                    test = startXYZ.clone();

                    test=new V3(test.x,test.y,test.z- 1.0D,test.theDimension);
                    return ((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue()) ? test : startXYZ;
                }
            }
        }
    }

    /**
     * 查找最近的块类型
     * @param startXYZ
     * @param block
     * @param searchDistance
     * @param mustSeeSky
     * @return
     */
    public static V3 findClosestBlockType(V3 startXYZ, Block block, int searchDistance, boolean mustSeeSky) {
        World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
        if (theWorld.getBlock(startXYZ.x.intValue(), startXYZ.y.intValue(), startXYZ.z.intValue()) == block) {
            return startXYZ;
        } else {
            for(int d = 1; d < searchDistance; ++d) {
                for(int yo = -searchDistance; yo <= searchDistance; ++yo) {
                    for(int xo = -d; xo <= d; ++xo) {
                        for(int zo = -d; zo <= d; ++zo) {
                            int sx = startXYZ.x.intValue() + xo;
                            int sy = startXYZ.y.intValue() + yo;
                            int sz = startXYZ.z.intValue() + zo;
                            if (theWorld.getBlock(sx, sy, sz) == block) {
                                V3 ret = new V3((double)sx, (double)sy, (double)sz, startXYZ.theDimension);
                                return ret;
                            }
                        }
                    }
                }
            }

            return null;
        }
    }

    /**
     * 查找最近的块类型
     * @param startXYZ
     * @param block
     * @param searchDistance
     * @return
     */
    public static V3 findClosestBlockType(V3 startXYZ, Block block, int searchDistance) {
        World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
        if (theWorld.getBlock(startXYZ.x.intValue(), startXYZ.y.intValue(), startXYZ.z.intValue()) == block) {
            return startXYZ;
        } else {
            for(int d = 1; d < searchDistance; ++d) {
                for(int xo = -d; xo <= d; ++xo) {
                    for(int zo = -d; zo <= d; ++zo) {
                        int sx = startXYZ.x.intValue() + xo;
                        int sy = startXYZ.y.intValue();
                        int sz = startXYZ.z.intValue() + zo;
                        if (theWorld.getBlock(sx, sy, sz) == block) {
                            V3 ret = new V3((double)sx, (double)sy, (double)sz, startXYZ.theDimension);
                            return ret;
                        }
                    }
                }
            }

            return null;
        }
    }

    /**
     * 找到最近的街区
     * @param startXYZ
     * @param block
     * @param distanceLimit
     * @return
     */
    public static ArrayList<V3> findClosestBlocks(V3 startXYZ, Block block, int distanceLimit) {
        ArrayList<V3> blocksFound = new ArrayList();
        int count = 0;
        World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);

        int ci;
        int sx;
        int i;
        for(ci = -distanceLimit; ci <= distanceLimit; ++ci) {
            for(int xo = -distanceLimit; xo <= distanceLimit; ++xo) {
                for(int zo = -distanceLimit; zo <= distanceLimit; ++zo) {
                    try {
                        sx = startXYZ.x.intValue() + xo;
                        i = startXYZ.y.intValue() + ci;
                        int sz = startXYZ.z.intValue() + zo;
                        ++count;
                        if (theWorld.getBlock(sx, i, sz) == block) {
                            V3 v = new V3((double)sx, (double)i, (double)sz, startXYZ.theDimension);
                            if (!blocksFound.contains(v)) {
                                blocksFound.add(v);
                            }
                        }
                    } catch (Exception var13) {
                        var13.printStackTrace();
                    }
                }
            }
        }

        ci = 0;
        double cd = 999.0D;

        for(sx = 0; sx < blocksFound.size(); ++sx) {
            V3 v = (V3)blocksFound.get(sx);
            double distance = Math.sqrt((v.x - startXYZ.x) * (v.x - startXYZ.x) + (v.z - startXYZ.z) * (v.z - startXYZ.z));
            if (distance < cd) {
                cd = distance;
                ci = sx;
            }
        }

        ArrayList<V3> retblocksFound = new ArrayList();
        if (blocksFound.size() > 0) {
            retblocksFound.add(blocksFound.get(ci));

            for(i = 0; i < blocksFound.size(); ++i) {
                if (i != ci) {
                    retblocksFound.add(blocksFound.get(i));
                }
            }
        }

        return retblocksFound;
    }

    /**
     * 把矿块放进箱子里
     * @param chests
     * @param blockXYZ
     * @return
     */
    public boolean mineBlockIntoChests(ArrayList<IInventory> chests, V3 blockXYZ) {
        boolean ret = false;
        ArrayList<ItemStack> minedStacks = this.translateBlockWhenMined(this.jobWorld, blockXYZ);
        if (minedStacks != null) {
            for(int s = 0; s < minedStacks.size(); ++s) {
                ItemStack stack = (ItemStack)minedStacks.get(s);
                if (stack != null) {
                    this.inventoriesPut(chests, stack, false);
                }
            }

            ret = true;
        }

        return ret;
    }

    /**
     * 把动物数记在笔里
     * @param controlBox
     * @param animal
     * @return
     */
    public int getAnimalCountInPen(V3 controlBox, Class animal) {
        List list = this.jobWorld.getEntitiesWithinAABB(animal, AxisAlignedBB.getBoundingBox(controlBox.x, controlBox.y, controlBox.z, controlBox.x + 1.0D, controlBox.y + 1.0D, controlBox.z + 1.0D).expand(3.0D, 2.0D, 3.0D));
        return list == null ? 0 : list.size();
    }

    /**
     * 去最近的民宅
     * @param searchWord
     * @param folk
     * @return
     */
    public static V3 getNearestBuildingForFolk(String searchWord, FolkData folk) {
        new ArrayList();
        Building shortestDist = null;

        for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
            Building b = (Building) ModSimReloaded.theBuildings.get(x);
            if (b.displayName.toLowerCase().contains(searchWord.toLowerCase())) {
                if (shortestDist.primaryXYZ == null) {
                    shortestDist = b;
                }

                if (folk.location.getDistanceTo(shortestDist.primaryXYZ) < b.primaryXYZ.getDistanceTo(folk.location)) {
                    shortestDist = b;
                }
            }
        }

        return shortestDist.primaryXYZ;
    }

}
