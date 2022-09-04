package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.Building;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.TileEntityWindmill;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

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
    List<V3> closestBlocks = new CopyOnWriteArrayList();
    //职业
    public Vocation vocation = null;
    //职场
    public World jobWorld = null;
    //库存 箱子关闭 箱子已经打开了，这是为了以后再关上箱子
    private transient IInventory chestToClose = null;
    //箱子什么时候关闭
    private transient Long chestToCloseWhen = 0L;

    public Job() {
    }

    /**
     * @return
     * @Author fan
     * @Description //TODO  他刚上班 被子类覆盖，在上班途中发生火灾，但尚未到达
     * @Date 10:59 2022/3/26
     * @Param
     **/
    public abstract void onArrivedAtWork();

    /**
     * @return void
     * @Author fan
     * @Description //TODO 重新安排工作 重置他们的阶段，因为它与NPC保存
     * @Date 11:00 2022/3/26
     * @Param []
     **/
    public abstract void resetJob();

    /**
     * @return void
     * @Author fan
     * @Description //TODO 更新 在整个工作期间重复调用，在工作的子类中被覆盖
     * @Date 11:00 2022/3/26
     * @Param []
     **/
    public void onUpdate() {
        try {
            //当 关闭箱子不为空   并且 当前时间毫秒 大约关闭箱子时
            if (this.chestToClose != null && System.currentTimeMillis() > this.chestToCloseWhen) {
                //关上箱子
                this.chestToClose.closeInventory(this.mc.thePlayer);
                //设置为空
                this.chestToClose = null;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC安排工作出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO     //去上班 子类调用的逻辑让人们工作，处理步行正常工作
     * @Date 11:02 2022/3/26
     * @Param [theFolk] 实体人数据
     **/
    public void onUpdateGoingToWork(FolkData theFolk) {
        try {
            //如果当前职场为空 则设置职场
            if (this.jobWorld == null) {
                try {
                    this.jobWorld = MinecraftServer.getServer().worldServerForDimension(theFolk.employedAt.theDimension);
                } catch (Exception e) {
                    StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("设置职场出错：" + e.getMessage()+"行数："+element.getLineNumber());
                    return;
                }
            }
            //如果不是在怀孕期间
            if (!(theFolk.pregnancyStage > 0.0F)) {
                //在上班的路上
                if (theFolk.action == FolkAction.ONWAYTOWORK) {
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
                        theFolk.stayPut=true;
                        //他刚上班
                        this.onArrivedAtWork();
                        //如果大于1小于3
                    } else if (dist > 1 && dist < 3) {
                        //复制当前数据
                        V3 work = theFolk.employedAt.clone();
                        work=new V3(work.xCoord,work.yCoord+1,work.zCoord);
                        //去位置
                        theFolk.gotoXYZ(work, GotoMethod.SHIFT);
                        theFolk.location = work;
                    }
                }

                //  是否白天               活动的              去工作路上                             活动中                工作中
                if (ModSimReloaded.isDayTime()|| theFolk.isNightOwl()) {
                    if (theFolk.action != FolkAction.ONWAYTOWORK && theFolk.action != FolkAction.ATWORK) {
                        //活动设置为去工作路上
                        theFolk.action = FolkAction.ONWAYTOWORK;
                        //设置原地不动为否
                        theFolk.stayPut = false;
                        //如果目的地为空
                        if (theFolk.destination == null) {
                            //设置目的地
                            V3 v=new V3(theFolk.employedAt.xCoord,theFolk.employedAt.yCoord+1,theFolk.employedAt.zCoord);
                            //去位置
                            theFolk.gotoXYZ(v, GotoMethod.SHIFT);
                            theFolk.gotoXYZ(v, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC去上班出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * @return int
     * @Author fan
     * @Description //TODO 获得物品库存清单 返回人们库存中有多少特定项目/块的计数
     * @Date 11:23 2022/3/26
     * @Param [theFolk, item]
     **/
    public int getInventoryCount(FolkData theFolk, Item item) {
        //声明库存为0
        int ret = 0;
        try {
            String lang= FMLCommonHandler.instance().getCurrentLanguage();
            InventoryBasic inventoryBasic = theFolk.getVillagerInventory();
            if (inventoryBasic != null) {
                //循环库存
                for (int i = 0; i < inventoryBasic.getSizeInventory(); i++) {
                    //当前方块的数量
                    ItemStack is = inventoryBasic.getStackInSlot(i);
                    if (is != null) {
                        String name=is.getDisplayName();
                        String names=new ItemStack(item).getDisplayName();
                        if("en_US".equals(lang)){
                            names=names.substring(names.indexOf(" "),names.length());
                        }else{
                            names=names.substring(names.length()-1,names.length());
                        }
                        //如果物品 对上
                        if (name.contains(names)) {
                            //赋值物品数量
                            ret += is.stackSize;
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("获取物品清单个数出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 获得方块的库存
     *
     * @param theFolk
     * @param item
     * @return
     */
    public int getInventoryCount(FolkData theFolk, Block item) {
        //声明库存为0
        int ret = 0;
        try {
            String lang= FMLCommonHandler.instance().getCurrentLanguage();
            //循环库存
            for (int i = 0; i < theFolk.getVillagerInventory().getSizeInventory(); i++) {
                //当前方块的数量
                ItemStack is = (ItemStack) theFolk.getVillagerInventory().getStackInSlot(i);
                if (is != null) {//如果物品 对上
                    String name=is.getDisplayName();
                    String names=new ItemStack(item).getDisplayName();
                    if("en_US".equals(lang)){
                        names=names.substring(names.indexOf(" "),names.length());
                    }else{
                        names=names.substring(names.length()-1,names.length());
                    }
                    if (name.contains(names)) {
                        //赋值物品数量
                        ret += is.stackSize;
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("获得方块的库存出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return ret;
        }
        return ret;
    }

    /**
     * 找到熔炉
     * 找到离某个位置最近的熔炉并返回它，如果没有熔炉则返回 NULL
     *
     * @param v
     * @return
     */
    public TileEntityFurnace findFurnace(V3 v) {
        //声明熔炉实体
        TileEntityFurnace ret = null;
        try {
            //查找最近的块类型
            V3 vRet = findClosestBlockType(v, Blocks.furnace, 5, false);
            //如果等于空重新赋值
            if (vRet == null) {
                vRet = findClosestBlockType(v, Blocks.lit_furnace, 5, false);
            }
            //如果不为空
            if (vRet != null) {
                //世界服务器的维度                                                       维度
                World theWorld = MinecraftServer.getServer().worldServerForDimension(vRet.theDimension);
                BlockPos blockPos = new BlockPos(vRet.xCoord, vRet.yCoord, vRet.zCoord);
                //设置熔炉位置 转换为int
                ret = (TileEntityFurnace) theWorld.getTileEntity(blockPos);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC找熔炉出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 库存输出 由公共函数调用
     * 由公共函数调用
     *
     * @param chest
     * @param inStack
     * @return
     */
    private static boolean inventoryPut(IInventory chest, ItemStack inStack) {
        //放好了
        Boolean placedOK = false;
        try {
            //如果物品为空
            if (inStack == null) {
                return true;
            }
            //
            for (int q = 1; q <= inStack.stackSize; q++) {
                for (int i = 0; i < chest.getSizeInventory(); i++) {
                    ItemStack is = chest.getStackInSlot(i);
                    if (is == null) {
                        is = inStack.copy();
                        is.stackSize = 1;
                        //将给定的物品堆栈设置为库存中的指定位置（可以是工艺或装甲部分）。
                        chest.setInventorySlotContents(i, is);
                        ItemStack isTest = chest.getStackInSlot(i);
                        if (isTest != null) {
                            placedOK = true;
                            break;//重新进入数量循环
                        } else {
                            ModSimReloaded.log.warn("Job: placeIntoInventory() 无法将 " + is.getDisplayName() + " 放入空槽 " + i);
                            placedOK = false;
                        }
                    } else if (is.getItem() == inStack.getItem() && is.stackSize < is.getMaxStackSize()) {
                        int isBefore = chest.getStackInSlot(i).stackSize;
                        is.stackSize++;
                        //将给定的物品堆栈设置为库存中的指定位置（可以是工艺或装甲部分）。
                        chest.setInventorySlotContents(i, is);
                        int isAfter = chest.getStackInSlot(i).stackSize;
                        if (isAfter > isBefore) {
                            placedOK = true;
                            break;
                        } else {
                            ModSimReloaded.log.warn("Job: placeIntoInventory() 无法更改大小 " + is.getDisplayName() + " in slot " + i);
                            placedOK = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC从箱子里拿东西出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return placedOK;

    }

    /**
     * 得到库存
     * 从箱子/库存中取出一些东西并归还
     *
     * @param chests        箱子或库存
     * @param whatItem      获取任何项目所需的项目类型/数量等或 NULL
     * @param getRandomItem 如果 whatItem 为 NULL，则使用 - 如果为 false，则获取第一个可用项目，如果为 true，则获取随机项目
     * @param compareMeta
     * @param ignoreId
     * @return 取出物品的 Itemstack，如果无法获取物品，则为 NULL
     */
    public static ItemStack inventoriesGet(List<IInventory> chests, ItemStack whatItem, boolean getRandomItem, boolean compareMeta, ItemStack ignoreId) {
        ItemStack retStack = null;
        try {
            for (int c = 0; c < chests.size(); c++) {
                IInventory chest = (IInventory) chests.get(c);
                retStack = inventoryGet(chest, whatItem, getRandomItem, compareMeta);
                if (retStack != null) {
                    return retStack;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC从箱子/库存中取出一些东西并归还出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    /**
     * @param chests
     * @param whatItem
     * @param getRandomItem
     * @param compareMeta
     * @return
     */
    public static ItemStack inventoriesGet(List<IInventory> chests, ItemStack whatItem, boolean getRandomItem, boolean compareMeta) {
        ItemStack retStack = null;
        try {
            for (int c = 0; c < chests.size(); ++c) {
                IInventory chest = chests.get(c);
                //库存获取
                retStack = inventoryGet(chest, whatItem, getRandomItem, compareMeta);
                if (retStack != null) {
                    return retStack;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC从箱子/库存中取出一些东西并归还出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    /**
     * 由公共函数调用
     *
     * @param chest
     * @param whatItem
     * @param getRandomItem
     * @param compareMeta
     * @param ignoreId
     * @return
     */
    private static ItemStack inventoryGet(IInventory chest, ItemStack whatItem, boolean getRandomItem, boolean compareMeta, ItemStack ignoreId) {
        ItemStack returnStack = null;
        try {
            if (whatItem == null) {
                returnStack = whatItem.copy();
                returnStack.stackSize = 0;
                for (int i = 0; i < chest.getSizeInventory(); i++) {
                    boolean ignore = false;
                    ItemStack chestStack = chest.getStackInSlot(i);
                    if (ignoreId != null) {
                        if (chestStack == ignoreId) {
                            ignore = true;
                        }
                    }
                    if (chestStack != null && ignore == false) {
                        if (!compareMeta) {
                            chestStack.setItemDamage(whatItem.getItemDamage());
                        }
                        if (chestStack.isItemEqual(whatItem)) {
                            while (chestStack.stackSize >= 1) {
                                returnStack.stackSize++;
                                chestStack.stackSize--;

                                if (chestStack.stackSize <= 0) {
                                    chest.setInventorySlotContents(i, null);
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
            } else {
                if (getRandomItem) {
                    returnStack = null;
                    List<Integer> slots = new CopyOnWriteArrayList<Integer>();

                    for (int g = 0; g < chest.getSizeInventory(); g++) {
                        ItemStack chestStack = chest.getStackInSlot(g);

                        if (chestStack != null) {
                            slots.add(g);
                        }
                    }

                    if (slots.size() == 0) {
                        return null;
                    }

                    returnStack = chest.getStackInSlot(new Random().nextInt(slots.size()));
                    return returnStack;
                } else {
                    returnStack = null;

                    for (int g = 0; g < chest.getSizeInventory(); g++) {
                        ItemStack chestStack = chest.getStackInSlot(g);

                        if (chestStack != null) {
                            returnStack = chestStack.copy();
                            chest.setInventorySlotContents(g, null);
                            return returnStack;
                        }
                    }

                    return returnStack;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC从箱子/库存中取出一些东西并归还出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return returnStack;
    }

    /**
     * @param chest
     * @param whatItem
     * @param getRandomItem
     * @param compareMeta
     * @return
     */
    private static ItemStack inventoryGet(IInventory chest, ItemStack whatItem, boolean getRandomItem, boolean compareMeta) {
        ItemStack returnStack = null;
        try {
            if (whatItem != null) {
                returnStack = whatItem.copy();
                returnStack.stackSize = 0;

                for (int g = 0; g < chest.getSizeInventory(); g++) {
                    boolean ignore = false;
                    ItemStack chestStack = chest.getStackInSlot(g);

                    if (chestStack != null && ignore == false) {
                        if (!compareMeta) {
                            chestStack.setItemDamage(whatItem.getItemDamage());
                        }
                        if (chestStack.isItemEqual(whatItem)) {
                            while (chestStack.stackSize >= 1) {
                                returnStack.stackSize++;
                                chestStack.stackSize--;

                                if (chestStack.stackSize <= 0) {
                                    chest.setInventorySlotContents(g, null);
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
            } else {
                if (getRandomItem) {
                    returnStack = null;
                    List<Integer> slots = new CopyOnWriteArrayList<Integer>();

                    for (int g = 0; g < chest.getSizeInventory(); g++) {
                        ItemStack chestStack = chest.getStackInSlot(g);

                        if (chestStack != null) {
                            slots.add(g);
                        }
                    }

                    if (slots.size() == 0) {
                        return null;
                    }

                    returnStack = chest.getStackInSlot(new Random().nextInt(slots.size()));
                    return returnStack;
                } else {


                    for (int g = 0; g < chest.getSizeInventory(); g++) {
                        ItemStack chestStack = chest.getStackInSlot(g);

                        if (chestStack != null) {
                            returnStack = chestStack.copy();
                            chest.setInventorySlotContents(g, null);
                            return returnStack;
                        }
                    }

                    return returnStack;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC从箱子/库存中取出一些东西并归还出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return returnStack;
    }

    /**
     * 用于将itemStack放入一组箱子或其他库存中，如果需要，将全部使用，如果由于已满而无法放入指定的任何箱子中，则返回false
     * 用于将 itemStack 放入一组箱子或其他库存中，如果需要，将使用全部，如果由于已满而无法放入指定的任何箱子中，则返回 false
     *
     * @param chests      库存
     * @param inStack     堆叠
     * @param doOpenClose 是否打开关闭
     * @return
     */
    public boolean inventoriesPut(List<IInventory> chests, ItemStack inStack, boolean doOpenClose) {
        //是否放好
        boolean placedOK = false;
        try {
            for (int i = 0; i < chests.size(); i++) {
                IInventory chest = (IInventory) chests.get(i);
                if (doOpenClose) {
                    //打开或者关闭箱子
                    this.openCloseChest(chest, 2000);
                }

                placedOK = inventoryPut(chest, inStack);
                if (placedOK) {
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC将物品放入箱子出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return placedOK;
    }

    public static boolean inventoriesPut(List<IInventory> chests, ItemStack inStack) {
        boolean placedOK = false;
        try {
            for (int i = 0; i < chests.size(); i++) {
                IInventory chest = (IInventory) chests.get(i);
                placedOK = inventoryPut(chest, inStack);
                if (placedOK) {
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NPC将物品放入箱子出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return placedOK;
    }

    /**
     * 将物品从NPC转移到箱子 将员工库存转移到一组箱子/库存中
     *
     * @param folkInventory 要转移的人员库存
     * @param toChests      要转移到的箱子集
     * @param specificItems 如果有任何/所有项，则为NULL；如果只应放置，则指定itemStack
     * @return 如果成功，如果箱子都满了，则为false
     */
    public boolean inventoriesTransferFromFolk(InventoryBasic folkInventory, List<IInventory> toChests, ItemStack specificItems) {
        boolean placed = false;
        boolean okToPlace = false;
        try {
            for (int i = 0; i < folkInventory.getSizeInventory(); i++) {
                try {
                    ItemStack folkStack = folkInventory.getStackInSlot(i);
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
                            ModSimReloaded.log.warn("Job: 无法放置一堆 " + folkStack.getDisplayName() + " in chest");
                            return false;
                        }
                    }
                } catch (Exception e) {
                    //var8.printStackTrace();
                }
            }

            folkInventory.clear();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("将物品从NPC转移到箱子 将员工库存转移到一组箱子/库存中出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return true;
    }

    /**
     * 转移到民间
     * 将一些物品/任何物品从一组箱子中转移到人们的库存中
     *
     * @param folkInventory 要转移到的人们库存
     * @param fromChests    箱子在哪里得到它们
     * @param whatItems     他们想要的任何项目或项目堆栈（包括数量）为 NULL
     * @param ignoreId      传入 -1 以不忽略任何块或要留在箱子的东西的块 ID
     * @return 成功获得至少一个堆栈为真，如果没有得到则为假
     */
    public boolean inventoriesTransferToFolk(InventoryBasic folkInventory, List<IInventory> fromChests, ItemStack whatItems, Block ignoreId) {
        boolean ret = false;
        int limit = 0;
        ItemStack got = null;
        try {
            for (int c = 0; c < fromChests.size(); ++c) {
                IInventory chest = fromChests.get(c);
                this.openCloseChest(chest, 2000);
            }

            do {
                got = inventoriesGet(fromChests, whatItems, false, false);
                if (got != null) {
                    folkInventory.setInventorySlotContents(limit, got);
                    ret = true;
                }
                limit++;
            } while (got != null && limit < 64);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("将一些物品/任何物品从一组箱子中转移到人们的库存中出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 将有限数量的特定物品从一组箱子（库存）转移到NPC的库存中
     * 将有限数量的特定物品从一组箱子 (IInventory) 转移到人们的库存中
     *
     * @param folkInventory
     * @param fromChests
     * @param whatItems
     * @param getQty
     * @param doCompareMeta
     * @return
     */
    public int inventoriesTransferLimitedToFolk(InventoryBasic folkInventory, List<IInventory> fromChests, ItemStack whatItems, int getQty, boolean doCompareMeta) {
        int gotSoFar = 0;
        try {
            for (IInventory chest : fromChests) {
                for (int g = 0; g < chest.getSizeInventory(); g++) {
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
                        while (gotSoFar < getQty && chestStack.stackSize > 0) {
                            gotSoFar++;
                            chestStack.stackSize--;
                            folkInventory.setInventorySlotContents(0, new ItemStack(Block.getBlockFromItem(chestStack.getItem()), 1, chestStack.getMetadata()));
                        }

                        if (chestStack.stackSize > 0) {
                            chest.setInventorySlotContents(g, chestStack);
                        } else {
                            chest.setInventorySlotContents(g, (ItemStack) null);
                        }
                    }

                    if (gotSoFar == getQty) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("将有限数量的特定物品从一组箱子（库存）转移到NPC的库存中出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return gotSoFar;
    }

    /**
     * 把物品放在箱子里
     * 返回一个 int Count，表示箱子中有多少传入的物品（仅计数，不取出）
     *
     * @param chests
     * @param is
     * @param doCompareMeta
     * @return
     */
    public int getItemCountInChests(List<IInventory> chests, ItemStack is, boolean doCompareMeta) {
        int ret = 0;
        try {
            for (IInventory chest : chests) {

                for (int g = 0; g < chest.getSizeInventory(); g++) {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("把物品放在箱子里出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 开采时平移块体
     * 将开采的块翻译成物品，例如将煤炭矿石块翻译成煤炭物品
     *
     * @param world
     * @param location
     * @return
     */
    public List<ItemStack> translateBlockWhenMined(World world, V3 location) {
        List<ItemStack> itemStacks = new CopyOnWriteArrayList<ItemStack>();
        try {
            int i = (int) location.xCoord;
            int j = (int) location.yCoord;
            int k = (int) location.zCoord;
            BlockPos blockPos = new BlockPos(i, j, k);
            Block block = world.getBlockState(blockPos).getBlock();
            if (block == null) {
                return null;
            }

            int ma = block.getMetaFromState(world.getBlockState(blockPos));
            itemStacks = block.getDrops(world, blockPos, block.getStateFromMeta(ma), 0);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("开采时平移块体出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return itemStacks;

    }

    /**
     * 开关箱子
     * 打开和关闭一个箱子以Npc们将东西放入或取出
     *
     * @param chest
     * @param msDelay
     */
    public void openCloseChest(IInventory chest, int msDelay) {
        try {
            chest.openInventory(this.mc.thePlayer);
            this.chestToClose = chest;
            this.chestToCloseWhen = System.currentTimeMillis() + (long) msDelay;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("开关箱子出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 设置最接近的类型块
     * 在世界中找到传入的块类型的 V3 位置设置最接近的块数组列表
     *
     * @param startXYZ
     * @param blockIDs
     * @param distanceLimit
     * @param needsToSeeSky
     * @param scanDownwards
     * @param oneLayerOnly
     */
    public void setClosestBlocksOfType(final V3 startXYZ, final List<Block> blockIDs, final int distanceLimit, final boolean needsToSeeSky, final boolean scanDownwards, final boolean oneLayerOnly) {

        ThreadPoolExecutor threadPoolExecutor = ModSimReloaded.threadPoolExecutor;
        threadPoolExecutor.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                    World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
                    HashMap hm = new HashMap();
                    boolean skip = false;
                    int YdistanceLimit = distanceLimit;
                    if (oneLayerOnly) {
                        YdistanceLimit = 0;
                    }

                    for (int yo = 0; yo <= YdistanceLimit; yo++) {
                        for (int d = 1; d < distanceLimit; d++) {
                            for (int xo = -d; xo <= d; xo++) {
                                for (int zo = -d; zo <= d; zo++) {
                                    int sx = (int) (startXYZ.xCoord + xo);
                                    int sy;
                                    if (scanDownwards) {
                                        sy = (int) (startXYZ.yCoord - yo);
                                    } else {
                                        sy = (int) (startXYZ.yCoord + yo);
                                    }

                                    int sz = (int) (startXYZ.zCoord + zo);
                                    skip = false;

                                    for (int b = 0; b < blockIDs.size(); b++) {
                                        Block blockID = (Block) blockIDs.get(b);
                                        if (theWorld == null) {
                                            return;
                                        }

                                        Block blockInWorld = theWorld.getBlockState(new BlockPos(sx, sy, sz)).getBlock();
                                        if (blockInWorld == blockID) {
                                            if (needsToSeeSky) {
                                                boolean canSeeSky;
                                                if (theWorld.getBlockState(new BlockPos(sx, sy + 1, sz)).getBlock() == null) {
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
                                                V3 v = new V3((double) sx, (double) sy, (double) sz, startXYZ.theDimension);
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

                    Job.this.closestBlocks = new CopyOnWriteArrayList(hm.values());
                    Job.this.step = 3;
                    } catch (Exception e) {
                        StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("设置最接近的类型块出错了：" + e.getMessage()+"行数："+element.getLineNumber());
                    }
                }

            });
        //threadPoolExecutor.shutdown();

    }

    /**
     * 库存最接近
     * 在搜索区域内找到所有最近的库存/箱子并返回一个箱子类型的东西的数组列表
     *
     * @param startXYZ
     * @param searchDistance
     * @return
     */
    public static List<IInventory> inventoriesFindClosest(V3 startXYZ, int searchDistance) {
        List<IInventory> ret = new CopyOnWriteArrayList<IInventory>();

        try {
            World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
            BlockPos blockPos = new BlockPos(startXYZ.xCoord, startXYZ.yCoord, startXYZ.zCoord);
            TileEntity te = theWorld.getTileEntity(blockPos);
            if (te != null) {
                if (te instanceof IInventory && !(te instanceof TileEntityFurnace) && !(te instanceof TileEntityWindmill)) {
                    ret.add((IInventory) te);
                }
            }

            for (int d = 1; d < searchDistance; d++) {
                for (int yo = -d; yo <= d; yo++) {
                    for (int xo = -d; xo <= d; xo++) {
                        for (int zo = -d; zo <= d; zo++) {
                            int sx = (int) (startXYZ.xCoord + xo);
                            int sy = (int) (startXYZ.yCoord + yo);
                            int sz = (int) (startXYZ.zCoord + zo);
                            blockPos = new BlockPos(sx, sy, sz);
                            te = theWorld.getTileEntity(blockPos);
                            if (te != null) {
                                if (te instanceof IInventory && !(te instanceof TileEntityWindmill) && !alreadyGotChest(ret, (IInventory) te)) {
                                    ret.add((IInventory) te);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return ret;
        }
        return ret;
    }

    /**
     * 已经有箱子了
     *
     * @param chests
     * @param chest
     * @return
     */
    private static boolean alreadyGotChest(List<IInventory> chests, IInventory chest) {
        boolean ret = false;
        try {
            for (IInventory ch : chests) {
                if (ch.toString().contentEquals(chest.toString())) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("已经有箱子了出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 寻找相邻空间
     * 搜索 4 个相邻的方块，看它们是否是空气
     *
     * @param startXYZ
     * @param world
     * @return
     */
    public static V3 findAdjacentSpace(V3 startXYZ, World world) {
        World theWorld = world;
        try {
            if (world == null) {
                theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
            }

            V3 test = startXYZ.clone();
            BlockPos blockPos = new BlockPos(test.xCoord+1, test.yCoord, test.zCoord);
            if (((World) theWorld).isAirBlock(blockPos)) {
                return test;
            }
            test = startXYZ.clone();
            blockPos = new BlockPos(test.xCoord-1, test.yCoord, test.zCoord);
            if (((World) theWorld).isAirBlock(blockPos)) {
                return test;
            }
            test = startXYZ.clone();
            blockPos = new BlockPos(test.xCoord, test.yCoord, test.zCoord+1);
            if (((World) theWorld).isAirBlock(blockPos)) {
                return test;
            }
            test = startXYZ.clone();
            blockPos = new BlockPos(test.xCoord, test.yCoord, test.zCoord-1);
            if (((World) theWorld).isAirBlock(blockPos)) {
                return test;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("寻找相邻空间出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return startXYZ;
    }

    /**
     * 查找最近的块类型
     * 如果在该区域中没有找到，则在该位置下方最多搜索 10 并且对于某种类型的块最多搜索 80 的距离将返回 null
     *
     * @param startXYZ
     * @param block
     * @param searchDistance
     * @param mustSeeSky
     * @return
     */
    public static V3 findClosestBlockType(V3 startXYZ, Block block, int searchDistance, boolean mustSeeSky) {
        V3 ret = null;
        try {
            World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
            if (theWorld.getBlockState(new BlockPos(startXYZ.xCoord, startXYZ.yCoord, startXYZ.zCoord)).getBlock() == block) {
                return startXYZ;
            } else {
                for (int d = 1; d < searchDistance; d++) {
                    for (int yo = -searchDistance; yo <= searchDistance; yo++) {
                        for (int xo = -d; xo <= d; xo++) {
                            for (int zo = -d; zo <= d; zo++) {
                                int sx = (int) (startXYZ.xCoord + xo);
                                int sy = (int) (startXYZ.yCoord + yo);
                                int sz = (int) (startXYZ.zCoord + zo);
                                if (theWorld.getBlockState(new BlockPos(sx, sy, sz)).getBlock() == block) {
                                    ret = new V3((double) sx, (double) sy, (double) sz, startXYZ.theDimension);
                                    return ret;
                                }
                            }
                        }
                    }
                }
                return ret;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("查找最近的块类型出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 查找最近的块类型
     * 仅在 searchDistance 内搜索相同 Y 级别的块类型并返回 V3 或 null
     *
     * @param startXYZ
     * @param block
     * @param searchDistance
     * @return
     */
    public static V3 findClosestBlockType(V3 startXYZ, Block block, int searchDistance) {
        V3 ret = null;
        try {
            World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);
            BlockPos blockpos = new BlockPos(startXYZ.xCoord, startXYZ.yCoord, startXYZ.zCoord);
            if (theWorld.getBlockState(blockpos).getBlock() == block) {
                return startXYZ;
            } else {
                for (int d = 1; d < searchDistance; d++) {
                    for (int xo = -d; xo <= d; xo++) {
                        for (int zo = -d; zo <= d; zo++) {
                            int sx = (int) (startXYZ.xCoord + xo);
                            int sy = (int) startXYZ.yCoord;
                            int sz = (int) (startXYZ.zCoord + zo);
                            if (theWorld.getBlockState(new BlockPos(sx, sy, sz)).getBlock() == block) {
                                ret = new V3((double) sx, (double) sy, (double) sz, startXYZ.theDimension);
                                return ret;
                            }
                        }
                    }
                }
                return ret;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("查找最近的块类型出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 找到最近的街区
     * 查找找到指定块的位置的数组列表，排序为最接近的第一
     *
     * @param startXYZ
     * @param block
     * @param distanceLimit
     * @return
     */
    public static List<V3> findClosestBlocks(V3 startXYZ, Block block, int distanceLimit) {
        List<V3> blocksFound = new CopyOnWriteArrayList();
        int count = 0;
        List<V3> retblocksFound = new CopyOnWriteArrayList();
        try {
            World theWorld = MinecraftServer.getServer().worldServerForDimension(startXYZ.theDimension);

            for (int yo = -distanceLimit; yo <= distanceLimit; yo++) {
                for (int xo = -distanceLimit; xo <= distanceLimit; xo++) {
                    for (int zo = -distanceLimit; zo <= distanceLimit; zo++) {
                        try {
                            int sx = (int) (startXYZ.xCoord + xo);
                            int sy = (int) (startXYZ.yCoord + yo);
                            int sz = (int) (startXYZ.zCoord + zo);
                            count++;
                            if (theWorld.getBlockState(new BlockPos(sx, sy, sz)).getBlock() == block) {
                                V3 v = new V3((double) sx, (double) sy, (double) sz, startXYZ.theDimension);
                                if (!blocksFound.contains(v)) {
                                    blocksFound.add(v);
                                }
                            }
                        } catch (Exception e) {
                            //var13.printStackTrace();
                        }
                    }
                }
            }

            int ci = 0;
            double cd = 999;

            for (int i = 0; i < blocksFound.size(); i++) {
                V3 v = (V3) blocksFound.get(i);
                double distance = Math.sqrt((v.xCoord - startXYZ.xCoord) * (v.xCoord - startXYZ.xCoord) + (v.zCoord - startXYZ.zCoord) * (v.zCoord - startXYZ.zCoord));
                if (distance < cd) {
                    cd = distance;
                    ci = i;
                }
            }


            if (blocksFound.size() > 0) {
                retblocksFound.add(blocksFound.get(ci));

                for (int i = 0; i < blocksFound.size(); i++) {
                    if (i != ci) {
                        retblocksFound.add(blocksFound.get(i));
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("找到最近的街区出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return retblocksFound;
    }

    /**
     * 把矿块放进箱子里
     * 尝试在 xyz 开采一个块并将开采的东西放入箱子中，进行翻译（煤炭 > 煤炭项目）如果有东西被开采则返回 true
     *
     * @param chests
     * @param blockXYZ
     * @return
     */
    public boolean mineBlockIntoChests(List<IInventory> chests, V3 blockXYZ) {
        boolean ret = false;
        try {
            List<ItemStack> minedStacks = this.translateBlockWhenMined(this.jobWorld, blockXYZ);
            if (minedStacks != null) {
                for (int s = 0; s < minedStacks.size(); s++) {
                    ItemStack stack = (ItemStack) minedStacks.get(s);
                    if (stack != null) {
                        this.inventoriesPut(chests, stack, false);
                    }
                }

                ret = true;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("把矿块放进箱子里出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 把动物数记在笔里
     * 扫描笔的 3x3 区域以计算其中有多少动物（传入动物类别）
     *
     * @param controlBox
     * @param animal
     * @return
     */
    public int getAnimalCountInPen(V3 controlBox, Class animal) {
        int size = 0;
        try {
            List list = this.jobWorld.getEntitiesWithinAABB(animal, new AxisAlignedBB(controlBox.xCoord, controlBox.yCoord, controlBox.zCoord, controlBox.xCoord + 1, controlBox.yCoord + 1, controlBox.zCoord + 1).expand(3.0, 2.0, 3.0));
            if (list == null) {
                return size;
            } else {
                size = list.size();
                return size;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("把动物数记在笔里出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return size;
    }

    /**
     * 去最近的民宅
     *
     * @param searchWord
     * @param folk
     * @return
     */
    public static V3 getNearestBuildingForFolk(String searchWord, FolkData folk) {
        List<Building> ret = new CopyOnWriteArrayList<Building>();
        Building shortestDist = null;
        try {
            for (int x = 0; x < ModSimReloaded.theBuildings.size(); x++) {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("把动物数记在笔里出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return shortestDist.primaryXYZ;
    }

}
