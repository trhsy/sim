package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.TileEntityWindmill;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
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
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:32
 * ========================================
 **/
public abstract class Job {
    Minecraft mc = Minecraft.getMinecraft();
    public int step = 1;
    ArrayList<V3> closestBlocks = new ArrayList();
    public Vocation vocation = null;
    public World jobWorld = null;
    private transient IInventory chestToClose = null;
    private transient Long chestToCloseWhen = 0L;

    public Job() {
    }

    public abstract void onArrivedAtWork();

    public abstract void resetJob();

    public void onUpdate() {
        if (this.chestToClose != null && System.currentTimeMillis() > this.chestToCloseWhen) {
            this.chestToClose.closeChest();
            this.chestToClose = null;
        }

    }

    public void onUpdateGoingToWork(FolkData theFolk) {
        if (this.jobWorld == null) {
            try {
                this.jobWorld = MinecraftServer.getServer().worldServerForDimension(theFolk.employedAt.theDimension);
            } catch (Exception var7) {
                var7.printStackTrace();
                return;
            }
        }

        if (!(theFolk.pregnancyStage > 0.0F)) {
            if (theFolk.action == FolkAction.ONWAYTOWORK) {
                //int dist = false;
                if (theFolk.gotoMethod == GotoMethod.WALK) {
                    theFolk.stayPut = false;
                    theFolk.updateLocationFromEntity();
                }

                int dist = theFolk.location.getDistanceTo(theFolk.employedAt);
                if (dist <= 1) {
                    theFolk.action = FolkAction.ATWORK;
                    this.onArrivedAtWork();
                } else if (dist > 1 && dist < 3) {
                    V3 work = theFolk.employedAt.clone();
                    Double var5 = work.y;
                    Double var6 = work.y = work.y + 1.0D;
                    theFolk.gotoXYZ(work, GotoMethod.SHIFT);
                    theFolk.location = work;
                }
            }

            if (ModSim.isDayTime() && theFolk.action != FolkAction.ONWAYTOWORK && theFolk.action != FolkAction.ATWORK) {
                theFolk.action = FolkAction.ONWAYTOWORK;
                theFolk.stayPut = false;
                if (theFolk.destination == null) {
                    theFolk.gotoXYZ(theFolk.employedAt, (GotoMethod) null);
                }
            }

        }
    }

    public int getInventoryCount(FolkData theFolk, Item item) {
        int ret = 0;

        for(int i = 0; i < theFolk.inventory.size(); ++i) {
            ItemStack is = (ItemStack)theFolk.inventory.get(i);
            if (is.getItem() == item) {
                ret += is.stackSize;
            }
        }

        return ret;
    }

    public int getInventoryCount(FolkData theFolk, Block item) {
        int ret = 0;

        for(int i = 0; i < theFolk.inventory.size(); ++i) {
            ItemStack is = (ItemStack)theFolk.inventory.get(i);
            if (Block.getBlockFromName(is.getDisplayName()) == item) {
                ret += is.stackSize;
            }
        }

        return ret;
    }

    public TileEntityFurnace findFurnace(V3 v) {
        TileEntityFurnace ret = null;
        V3 vRet = findClosestBlockType(v, Blocks.furnace, 5, false);
        if (vRet == null) {
            vRet = findClosestBlockType(v, Blocks.furnace, 5, false);
        }

        if (vRet != null) {
            World theWorld = MinecraftServer.getServer().worldServerForDimension(vRet.theDimension);
            ret = (TileEntityFurnace)theWorld.getTileEntity(vRet.x.intValue(), vRet.y.intValue(), vRet.z.intValue());
        }

        return ret;
    }

    private static boolean inventoryPut(IInventory chest, ItemStack inStack) {
        Boolean placedOK = false;
        if (inStack == null) {
            return true;
        } else {
            for(int q = 1; q <= inStack.stackSize; ++q) {
                for(int g = 0; g < chest.getSizeInventory(); ++g) {
                    ItemStack is = chest.getStackInSlot(g);
                    if (is == null) {
                        is = inStack.copy();
                        is.stackSize = 1;
                        chest.setInventorySlotContents(g, is);
                        ItemStack isTest = chest.getStackInSlot(g);
                        if (isTest != null) {
                            placedOK = true;
                            break;
                        }

                        ModSim.log.warning("Job: placeIntoInventory() could not place " + is.getDisplayName() + " in null slot " + g);
                        placedOK = false;
                    } else if (is.getItem() == inStack.getItem() && is.getMetadata() == inStack.getMetadata() && is.stackSize < is.getMaxStackSize()) {
                        int isBefore = chest.getStackInSlot(g).stackSize;
                        ++is.stackSize;
                        chest.setInventorySlotContents(g, is);
                        int isAfter = chest.getStackInSlot(g).stackSize;
                        if (isAfter > isBefore) {
                            placedOK = true;
                            break;
                        }

                        ModSim.log.warning("Job: placeIntoInventory() could not inc Stacksize for " + is.getDisplayName() + " in slot " + g);
                        placedOK = false;
                    }
                }
            }

            return placedOK;
        }
    }

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
                        ModSim.log.warning("Job: Could not place stack of " + folkStack.getDisplayName() + " in chest");
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

    public void openCloseChest(IInventory chest, int msDelay) {
        chest.openChest();
        this.chestToClose = chest;
        this.chestToCloseWhen = System.currentTimeMillis() + (long)msDelay;
    }

    public void setClosestBlocksOfType(final V3 startXYZ, final ArrayList<Block> blockIDs, final int distanceLimit, final boolean needsToSeeSky, final boolean scanDownwards, final boolean oneLayerOnly) {
        Thread t = new Thread(new Runnable() {
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

    public static V3 findAdjacentSpace(V3 startXYZ, World world) {
        World theWorld = world;
        if (world == null) {
            theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
        }

        V3 test = startXYZ.clone();
        Double var5 = test.x;
        Double var6 = test.x = test.x + 1.0D;
        if (((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue())) {
            return test;
        } else {
            test = startXYZ.clone();
            var5 = test.x;
            var6 = test.x = test.x - 1.0D;
            if (((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue())) {
                return test;
            } else {
                test = startXYZ.clone();
                var5 = test.z;
                var6 = test.z = test.z + 1.0D;
                if (((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue())) {
                    return test;
                } else {
                    test = startXYZ.clone();
                    var5 = test.z;
                    var6 = test.z = test.z - 1.0D;
                    return ((World)theWorld).isAirBlock(test.x.intValue(), test.y.intValue(), test.z.intValue()) ? test : startXYZ;
                }
            }
        }
    }

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

    public int getAnimalCountInPen(V3 controlBox, Class animal) {
        List list = this.jobWorld.getEntitiesWithinAABB(animal, AxisAlignedBB.getBoundingBox(controlBox.x, controlBox.y, controlBox.z, controlBox.x + 1.0D, controlBox.y + 1.0D, controlBox.z + 1.0D).expand(3.0D, 2.0D, 3.0D));
        return list == null ? 0 : list.size();
    }

    public static V3 getNearestBuildingForFolk(String searchWord, FolkData folk) {
        new ArrayList();
        Building shortestDist = null;

        for (int x = 0; x < ModSim.theBuildings.size(); ++x) {
            Building b = (Building) ModSim.theBuildings.get(x);
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
