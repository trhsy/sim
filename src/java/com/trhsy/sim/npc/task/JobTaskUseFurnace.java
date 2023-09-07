package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName JobTaskUseFurnace
 * @Description todo 使用熔炉
 * @Author TRHSY
 * @Date 2023/6/2213:02
 **/
public class JobTaskUseFurnace extends JobTask {
    /**
     * @Author fan
     * @Description //TODO 要烧制的东西
     * @Date 13:05 2023/6/22
     * @Param 
     * @return 
     **/
    public ItemStack collectionItems = null;
    //熔炉
    private transient TileEntityFurnace factoryFurnace = null;
    //步
    public int step = 1;
    //上次检查后的时间
    long timeSinceLastCheck = 0L;
    public JobTaskUseFurnace(Job j, long ms, ItemStack collectionItems) {
        super(j, ms);
        this.collectionItems = collectionItems;
    }
    @Override
    public void onTaskBegin() {
        
    }

    @Override
    public void onUpdate() {
        //寻找熔炉
        this.factoryFurnace = this.findFurnace(this.job.workPlace);

        if (this.factoryFurnace == null) {
            if (System.currentTimeMillis() - this.timeSinceLastCheck > 1000L*60) {
                this.timeSinceLastCheck = System.currentTimeMillis();
                //我的炉子不见了
                ModSimLoader.sendChat(this.folk.getName() + I18n.format("container.sim.job.glass.farmer.Where"));
            }
        }else {
            ItemStack currentSand;
            //燃料
            ItemStack gotFuel;
            //寻找箱子
            List<IInventory> factoryChests= this.job.inventoriesFindClosest(this.job.workPlace, 5);
            if (this.step == 1) {
                //检查炉子燃料
                this.folk.status = I18n.format("container.sim.job.glass.farmer.Checking");
                //沙子
                currentSand = this.factoryFurnace.getStackInSlot(1);
                if (currentSand == null) {
                    //煤炭
                    gotFuel = inventoriesGet(factoryChests, new ItemStack(Items.COAL, 64), false, false, new ItemStack(Items.COAL, 64));
                    //熔岩桶
                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(factoryChests, new ItemStack(Items.LAVA_BUCKET, 1), false, false, new ItemStack(Items.LAVA_BUCKET, 1));
                    }
                    //木材
                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(factoryChests, new ItemStack(Blocks.LOG, 64), false, false, new ItemStack(Blocks.LOG, 64));
                    }
                    //木板
                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(factoryChests, new ItemStack(Blocks.PLANKS, 64), false, false, new ItemStack(Blocks.PLANKS, 1));
                    }
                    if (gotFuel == null) {
                        if (System.currentTimeMillis() - this.timeSinceLastCheck > 1000L*60) {
                            this.timeSinceLastCheck = System.currentTimeMillis();
                            // 我的的炉子没有任何燃料
                            ModSimLoader.sendChat(this.folk.getName() + "(" + this.job.jobName + ")" + I18n.format("container.sim.job.glass.farmer.furnace"));
                            this.step = 1;
                            return;
                        }
                    }
                    //将燃料放到熔炉
                    this.factoryFurnace.setInventorySlotContents(1, gotFuel);
                    this.step = 2;
                    return;
                }

                this.step = 2;
            } else if (this.step == 2) {
                //往炉子里加材料
                this.folk.status = I18n.format("container.sim.job.glass.farmer.Adding");
                if (this.factoryFurnace != null) {
                    currentSand = this.factoryFurnace.getStackInSlot(0);
                    if (currentSand == null) {
                        collectionItems.stackSize=64;
                        //取箱子的材料
                        gotFuel = inventoriesGet(factoryChests, collectionItems, false, false, collectionItems);
                        if (gotFuel != null) {
                            this.factoryFurnace.setInventorySlotContents(0, gotFuel);
                        }

                        this.step = 3;
                        return;
                    }
                    collectionItems.stackSize=64 - currentSand.stackSize;
                    gotFuel = inventoriesGet(factoryChests, collectionItems, false, false, collectionItems);
                    if (gotFuel != null) {
                        currentSand.stackSize += gotFuel.stackSize;
                        this.factoryFurnace.setInventorySlotContents(0, currentSand);
                    }

                    this.step = 3;
                    return;
                }
            } else if (this.step == 3) {
                //获得玻璃
                currentSand = this.factoryFurnace.getStackInSlot(2);
                if (currentSand != null) {
                    //将锻造物放入仓库
                    this.folk.status = I18n.format("container.sim.job.glass.farmer.Putting");
                    this.job.placeInJobChest(currentSand);
                    if(ModSimLoader.gamemode!=1){
                        ModSimLoader.addMoney(-0.02F * (float)currentSand.stackSize);
                    }
                    this.factoryFurnace.setInventorySlotContents(2, null);
                } else {
                    //没有制造物品
                    this.folk.status = I18n.format("container.sim.job.glass.farmer.glass");
                    this.step = 1;
                    return;
                }
            }
        }
    }
    //找到熔炉
    public TileEntityFurnace findFurnace(V3 v) {
        //声明熔炉实体
        TileEntityFurnace ret = null;
        try {
            //查找最近的块类型
            V3 vRet = findClosestBlockType(v, Blocks.FURNACE, 5, false);
            //如果等于空重新赋值
            if (vRet == null) {
                vRet = findClosestBlockType(v, Blocks.LIT_FURNACE, 5, false);
            }
            //如果不为空
            if (vRet != null) {
                BlockPos blockPos = new BlockPos(vRet.x, vRet.y, vRet.z);
                //设置熔炉位置 转换为int
                ret = (TileEntityFurnace) this.folk.entity.worldObj.getTileEntity(blockPos);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("NPC找熔炉出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
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
                IInventory chest = chests.get(c);
                retStack = inventoryGet(chest, whatItem, getRandomItem, compareMeta);
                if (retStack != null) {
                    return retStack;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("NPC从箱子/库存中取出一些东西并归还出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
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
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("NPC从箱子/库存中取出一些东西并归还出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return returnStack;
    }
    public V3 findClosestBlockType(V3 startXYZ, Block block, int searchDistance, boolean mustSeeSky) {
        V3 ret = null;
        try {
            Block block1=this.folk.entity.worldObj.getBlockState(new BlockPos(startXYZ.x, startXYZ.y, startXYZ.z)).getBlock();
            if ( block1 == block) {
                return startXYZ;
            } else {
                for (int d = 1; d < searchDistance; d++) {
                    for (int yo = -searchDistance; yo <= searchDistance; yo++) {
                        for (int xo = -d; xo <= d; xo++) {
                            for (int zo = -d; zo <= d; zo++) {
                                int sx = (int) (startXYZ.x + xo);
                                int sy = (int) (startXYZ.y + yo);
                                int sz = (int) (startXYZ.z + zo);
                                if (this.folk.entity.worldObj.getBlockState(new BlockPos(sx, sy, sz)).getBlock().getBlockState() == block.getBlockState()) {
                                    ret = new V3(sx,sy,sz);
                                    return ret;
                                }
                            }
                        }
                    }
                }
                return ret;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("查找最近的块类型出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }
    @Override
    public void onTaskComplete() {

    }
}
