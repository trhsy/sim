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
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

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
        //
        if (this.theCheeseFactory == null) {
            Building.loadAllBuildings();
            this.theCheeseFactory = Building.getBuilding(this.theFolk.employedAt);
        }

        if (this.theCheeseFactory == null) {
            Building.loadAllBuildings();
            this.theCheeseFactory = Building.getBuilding(this.theFolk.employedAt);
        }

        if (this.theCheeseFactory == null) {
            this.theFolk.selfFire();
            //奶酪工厂出了问题，试着重新启动Minecraft
            ModSimReloaded.sendChat(I18n.format("container.sim.job.cheese_maker.There"));
        } else {
            //是晚上，状态设置为闲置
            if (!ModSimReloaded.isDayTime()) {
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }
            //去工作
            super.onUpdateGoingToWork(this.theFolk);
            //到达工厂
            if (this.theStage == Stage.ARRIVEDATFACTORY) {
                //工作中
                this.theFolk.action = FolkAction.ATWORK;
                this.runDelay = 11000;
                //倒牛奶
            } else if (this.theStage == Stage.EMPTYINGMILK) {
                this.runDelay = 1000;
            } else {
                this.runDelay = 5000;
            }
            //当前时间-时间间隔>=停顿时间
            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                //状态不是闲置 或者 是夜晚
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    //到达工厂
                    if (this.theStage == Stage.ARRIVEDATFACTORY) {
                        this.stageArrivedAtFactory();
                        //去奶牛场
                    } else if (this.theStage == Stage.GOINGTODAIRYFARM) {
                        this.stageGoingToDairyFarm();
                        //收集牛奶
                    } else if (this.theStage == Stage.COLLECTINGMILK) {
                        this.stageCollectingMilk();
                        //去储水池
                    } else if (this.theStage == Stage.GOINGTOTANK) {
                        this.stageGoingToTank();
                        //倒牛奶
                    } else if (this.theStage == Stage.EMPTYINGMILK) {
                        this.stageEmptyingMilk();
                        //搅拌
                    } else if (this.theStage == Stage.STIRING) {
                        this.stageStiring();
                        //收获奶酪
                    } else if (this.theStage == Stage.HARVESTCHEESE) {
                        this.stageHarvestCheese();
                        //切片奶酪
                    } else if (this.theStage == Stage.SLICECHEESE) {
                        this.stageSliceCheese();
                    }
                }
                //是夜晚
                if (!ModSimReloaded.isDayTime()) {
                    //状态 闲置
                    if (!theFolk.isNightOwl()) {
                        //闲置
                        this.theStage = Stage.IDLE;
                        return;
                    }
                }
                // 时间间隔邓毅当前系统时间
                this.timeSinceLastRun = System.currentTimeMillis();
            }
        }
    }

    /**
     * 到达工厂
     */
    private void stageArrivedAtFactory() {
        try {
            //特殊方块 5
            ArrayList<V3> cheesechest = this.theCheeseFactory.getSpecialBlocks(5);
            ArrayList<IInventory> chests = inventoriesFindClosest((V3)cheesechest.get(0), 4);
            this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), chests, new ItemStack(Items.milk_bucket, 64), (Block)null);
        } catch (Exception var3) {
        }

        this.theStage = Stage.GOINGTODAIRYFARM;
        this.currentFarmNum = -1;
    }

    /**
     * 去奶牛场
     */
    private void stageGoingToDairyFarm() {
        //去收集牛奶
        this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Going");
        //当前农场数量
        ++this.currentFarmNum;
        //通过搜索获得建筑 奶牛场
        ArrayList<Building> dairyFarms = Building.getBuildingBySearch(I18n.format("container.sim.gui_contains_Dairy_Farm"), true);
        //如果奶牛场不为空，或者 奶牛场个数-1小于等于当前奶牛农场值
        if (!dairyFarms.isEmpty() && dairyFarms.size() - 1 <= this.currentFarmNum) {
            this.farm = (Building)dairyFarms.get(this.currentFarmNum);
            //去农场
            this.theFolk.gotoXYZ(this.farm.primaryXYZ, GotoMethod.BEAM);
            //切换状态为 收集牛奶
            this.theStage = Stage.COLLECTINGMILK;
            this.step = 1;
        } else if (dairyFarms.isEmpty()) { //如果等于空
            //否则 没有检测到奶牛场，员工自动退休
            ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.has_retired"));
            this.theFolk.selfFire();//自动辞职
        } else {
            //否则去蓄水池
            this.theStage = Stage.GOINGTOTANK;
        }
    }

    /**
     * 收集牛奶
     */
    private void stageCollectingMilk() {
        //收集牛奶
        this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Collecting");
        if (this.step == 1) {
            //如果npc 目的地为空 并且距离小于5
            if (this.theFolk.destination == null && this.theFolk.location.getDistanceTo(this.farm.primaryXYZ) < 5) {
                this.step = 2;
                //在工作中
                this.theFolk.isWorking = true;
            } else {
                ModSimReloaded.log.info("JobCheeseMaker: 还没到农场");
            }
        } else if (this.step == 2) {
            //库存最接近
            this.chestsAtDairy = Job.inventoriesFindClosest(this.farm.primaryXYZ, 5);
            //为空
            if (this.chestsAtDairy.isEmpty()) {
                //当前位置在奶牛场找不到箱子，我辞职了！
                ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.I_quit"));
                this.theFolk.selfFire();
                return;
            }
            //库存转移到民间 牛奶场的箱子                                                       牛奶桶
            this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.chestsAtDairy, new ItemStack(Items.milk_bucket, 1), (Block)null);
            //如果npc 库存为空
            if (this.theFolk.getVillagerInventory() == null) {
                //今天在牛奶场没找到牛奶。
                ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.dairy"));
                //设置为切奶酪
                this.theStage = Stage.SLICECHEESE;
                this.step = 1;
                this.theFolk.isWorking = false;
                //没有牛奶可加工，今天会很轻松！
                this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.process");
                return;
            }
            //奶酪厂 获取特除方块 3
            ArrayList<V3> tanktop = this.theCheeseFactory.getSpecialBlocks(3);
            if (!tanktop.isEmpty()) {
                //对齐光波
                this.theFolk.gotoXYZ((V3)tanktop.get(0), GotoMethod.BEAM);
                //去蓄水池
                this.theStage = Stage.GOINGTOTANK;
                this.step = 1;
                this.theFolk.isWorking = false;
            } else {
                ModSimReloaded.log.warn("JobCheesemaker: 没有蓄水池");
                this.theFolk.selfFire();
            }
        }

    }
    /**
     * @Author fan
     * @Description //TODO 去蓄水池
     * @Date 21:46 2022/4/7
     * @Param []
     * @return void
     **/
    private void stageGoingToTank() {
        if (this.step == 1) {
            if (this.theFolk.destination == null) {

                this.step = 2;
                //准备加满水槽
                this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Preparing");
            } else {
                ModSimReloaded.log.info("JobCheeseMaker: 还没到后面");
            }
        } else if (this.step == 2) {
            //倒牛奶
            this.theStage = Stage.EMPTYINGMILK;
            this.step = 1;
        }

    }
    /**
     * @Author fan
     * @Description //TODO 倒牛奶
     * @Date 21:47 2022/4/7
     * @Param []
     * @return void
     **/
    private void stageEmptyingMilk() {
        if (this.step == 1) {
            //如果NPC库存不等于空
            if (this.theFolk.getVillagerInventory() != null) {
                //库存大于1
                if (this.theFolk.getVillagerInventory().getSizeInventory() > 1) {
                    //倒了 N 桶牛奶
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Emptying") + this.theFolk.getVillagerInventory().getSizeInventory() + I18n.format("container.sim.job.cheese_maker.buckets");
                } else {
                    //倒了所有的牛奶
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
            int lightID = Block.getIdFromBlock(BlockLoader.blockLightBox);
            ModSimReloaded.log.info(Integer.toString(lightID));
            boolean filledOk = false;
            Iterator iterator = milkblocks.iterator();

            label61: {
                V3 milkBlock;
                Block id;
                int meta;
                do {
                    if (!iterator.hasNext()) {
                        break label61;
                    }
                    //牛奶
                    milkBlock = (V3) iterator.next();
                    BlockPos blockPos= new BlockPos(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue());
                    id = this.jobWorld.getBlockState(blockPos).getBlock();
                    meta = id.getMetaFromState(this.jobWorld.getBlockState(blockPos));
                } while (id != Blocks.air && (id != BlockLoader.blockFluidMilk || meta != 1));
                BlockPos blockPos=new BlockPos(milkBlock.x.intValue(), milkBlock.y.intValue(), milkBlock.z.intValue());
                this.jobWorld.setBlockState(blockPos,BlockLoader.blockFluidMilk.getDefaultState(),3);

                try {
                    this.theFolk.getVillagerInventory().removeStackFromSlot(0);
                } catch (Exception var8) {
                }

                filledOk = true;
            }

            if (filledOk) {
                //停止工作
                this.theFolk.isWorking = false;
                this.step = 1;
            } else {
                this.theStage = Stage.STIRING;
                this.step = 1;
                this.theFolk.isWorking = false;
                ArrayList<V3> cheesechest = this.theCheeseFactory.getSpecialBlocks(5);
                ArrayList<IInventory> chests = inventoriesFindClosest((V3)cheesechest.get(0), 4);
                this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), chests, (ItemStack)null);
            }
        } else if (this.step == 3) {
            this.theStage = Stage.STIRING;
            this.step = 1;
            this.theFolk.isWorking = false;
        }

    }
    /**
     * @Author fan
     * @Description //TODO 搅拌
     * @Date 21:59 2022/4/7
     * @Param []
     * @return void
     **/
    private void stageStiring() {
        ArrayList<V3> stirPositions = this.theCheeseFactory.getSpecialBlocks(4);
        if (this.step == 1) {
            //检查牛奶粘度
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
                //有的奶酪厂出了问题，把建筑构造下来，重新构建它
                ModSimReloaded.sendChat(I18n.format("container.sim.job.cheese_maker.constructor"));
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
                    //搅拌牛奶
                    say = I18n.format("container.sim.job.cheese_maker.Stirring");
                    break;
                case 1:
                    //加入绝密成分
                    say = I18n.format("container.sim.job.cheese_maker.ingredient");
                    break;
                case 2:
                    //添加细菌培养
                    say = I18n.format("container.sim.job.cheese_maker.bacterial");
                    break;
                case 3:
                    //除去不需要的孢子
                    say = I18n.format("container.sim.job.cheese_maker.unwanted");
                    break;
                case 4:
                    //检查发酵进度
                    say = I18n.format("container.sim.job.cheese_maker.fermentation");
                    break;
                case 5:
                    //加入凝乳酶
                    say = I18n.format("container.sim.job.cheese_maker.Adding");
                    break;
                case 6:
                    //网条状奶酪
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
    /**
     * @Author fan
     * @Description //TODO 将牛奶转化为奶酪
     * @Date 22:01 2022/4/7
     * @Param [currentStirPos]
     * @return void
     **/
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

                BlockPos blockPos= new BlockPos(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue());
                id = this.jobWorld.getBlockState(blockPos).getBlock();
                dist = id.getMetaFromState(this.jobWorld.getBlockState(blockPos));

                if (id == BlockLoader.blockFluidMilk && dist == 0) {
                    this.jobWorld.setBlockState(blockPos,id.getDefaultState(),3);
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
                    id = this.jobWorld.getBlockState(new BlockPos(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue())).getBlock();
                    dist = cheese.getDistanceTo(currentStirPos);
                    if (id != BlockLoader.blockCheese && dist < 5) {
                        BlockPos blockPos=new BlockPos(cheese.x.intValue(), cheese.y.intValue(), cheese.z.intValue());
                        this.jobWorld.setBlockState(blockPos,BlockLoader.blockCheese.getDefaultState(),3);
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
            //有一个与奶酪工厂问题，请尝试重新建立它 - 没有奶块
            ModSimReloaded.sendChat(I18n.format("container.sim.job.cheese_maker.Cheese_factory"));
        }
    }
    /**
     * @Author fan
     * @Description //TODO 收获奶酪
     * @Date 22:02 2022/4/7
     * @Param []
     * @return void
     **/
    private void stageHarvestCheese() {
        ArrayList<V3> cheeseBlocks = this.theCheeseFactory.getSpecialBlocks(1);
        ArrayList<V3> stirPositions = this.theCheeseFactory.getSpecialBlocks(4);
        //提取奶酪块
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
            Iterator iterator;
            V3 block;
            Block id;
            if (this.step == 3) {
                this.theFolk.isWorking = false;
                gotBlock = false;
                iterator = cheeseBlocks.iterator();

                while(iterator.hasNext()) {
                    block = (V3)iterator.next();

                    id = this.jobWorld.getBlockState(new BlockPos(block.x.intValue(), block.y.intValue(), block.z.intValue())).getBlock();
                    if (((V3) stirPositions.get(0)).getDistanceTo(block) < 5 && id == BlockLoader.blockCheese) {
                        gotBlock = true;
                        this.theFolk.getVillagerInventory().setInventorySlotContents(0,new ItemStack(BlockLoader.blockCheese));
                        BlockPos blockPos=new BlockPos(block.x.intValue(), block.y.intValue(), block.z.intValue());
                        this.jobWorld.setBlockState(blockPos,id.getDefaultState(),3);
                        this.theFolk.isWorking = true;
                        GameStates var10000 = ModSimReloaded.states;
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
                iterator = cheeseBlocks.iterator();

                while(iterator.hasNext()) {
                    block = (V3)iterator.next();

                    id = this.jobWorld.getBlockState(new BlockPos(block.x.intValue(), block.y.intValue(), block.z.intValue())).getBlock();
                    if (((V3) stirPositions.get(1)).getDistanceTo(block) < 5 && id == BlockLoader.blockCheese) {
                        gotBlock = true;
                        this.theFolk.getVillagerInventory().setInventorySlotContents(0,new ItemStack(BlockLoader.blockCheese));
                        BlockPos blockPos=new BlockPos(block.x.intValue(), block.y.intValue(), block.z.intValue());
                        this.jobWorld.setBlockState(blockPos,id.getDefaultState(),3);
                        break;
                    }
                }

                if (!gotBlock) {
                    this.step = 1;
                    this.theStage = Stage.SLICECHEESE;
                    //计算奶酪块
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
            //有一个与奶酪厂的问题，尝试重新建立它的航点问题
            ModSimReloaded.sendChat(I18n.format("container.sim.job.cheese_maker.problem"));
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
                        //有人在奶酪工厂箱子里偷奶酪，我不干了！
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.Someone"));
                        this.theFolk.selfFire();
                    }

                    this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), chests, (ItemStack)null);
                    this.step = 4;
                } else if (this.step == 4) {
                    chests = Job.inventoriesFindClosest((V3) slicewaypoint.get(0), 4);
                    //奶酪工厂的箱子装满了奶酪！
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.Slicing");
                    ItemStack cheese = inventoriesGet(chests, new ItemStack(BlockLoader.blockCheese, 1), false, false);
                    if (cheese != null) {
                        boolean placedOK = this.inventoriesPut(chests, new ItemStack(ItemLoader.itemCheese, 9, 0), true);
                        if (!placedOK) {
                            ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.cheese_maker.factory"));
                            this.theFolk.selfFire();
                        }
                    } else {
                        this.step = 5;
                    }
                } else if (this.step == 5) {
                    //我喜欢的奶酪！
                    this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.love_cheese");
                }
            }

        }
    }
    /**
     * @Author fan
     * @Description //TODO 到达工厂
     * @Date 22:03 2022/4/7
     * @Param []
     * @return void
     **/
    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            //到达工厂
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

