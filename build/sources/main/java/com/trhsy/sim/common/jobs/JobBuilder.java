package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobBuilder
 * @Description todo 建筑工人
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:37
 * ========================================
 **/
public class JobBuilder extends Job implements Serializable {
    private static final long serialVersionUID = -1177665807904279141L;
    //建筑阶段
    public Stage theStage;
    //实体人数据
    public FolkData theFolk = null;
    //职业
    public Vocation vocation = null;
    //运行延迟
    public int runDelay = 1000;
    //自上次运行以来的时间
    public long timeSinceLastRun = 0L;
    //建筑用的储物箱
    private transient CopyOnWriteArrayList<IInventory> constructorChests = new CopyOnWriteArrayList();
    //建筑物
    private transient Building theBuilding = null;
    //实体的建筑箱
    private transient EntityConBox theConBox = null;
    //最后通知的材料
    private transient long lastNotifiedOfMaterials = 0L;
    //上次播放的声音
    private transient long soundLastPlayed = 0L;
    int l = 0;
    int ftb = 0;
    int ltr = 0;
    int xo = 0;
    int zo = 0;
    int acount = 0;
    int cx;
    int cy;
    int cz;
    int ex;
    int ey;
    int ez;
    int bx = 0;
    int by = 0;
    int bz = 0;

    public JobBuilder() {
    }

    /**
     * 初始化
     *
     * @param folk
     */
    public JobBuilder(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
                }

                this.theBuilding = this.theFolk.theBuilding;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBuilder出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 重置工作
     */
    @Override
    public void resetJob() {
        try {
            this.theStage = Stage.IDLE;
        }catch (Exception e){
            this.theStage = Stage.IDLE;
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("重新安排工作出错了:"+e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onUpdate() {
        try {
            if (this.theFolk != null) {
                super.onUpdate();
                //如果是晚上 设置闲置
                if (!ModSimReloaded.isDayTime()) {
                    if (!theFolk.isNightOwl()) {
                        //闲置
                        this.theStage = Stage.IDLE;
                        return;
                    }
                }
                //去上班
                super.onUpdateGoingToWork(this.theFolk);
                //建筑工正在检查建筑物的资源
                if (this.theStage == Stage.WAITINGFORRESOURCES) {
                    //延迟3秒
                    this.runDelay = 3000;
                    //if (this.theBuilding != null) {
                    //}
                }
                //建筑工正忙着建筑 并且 步=1
                if (this.theStage == Stage.INPROGRESS && this.step == 1) {
                    //建筑速度
                    this.runDelay = (int) (2000.0F / this.theFolk.levelBuilder);
                }
                //当前毫秒-上次运行>=延迟
                if (System.currentTimeMillis() - this.timeSinceLastRun >= (long) this.runDelay) {
                    //当前时间
                    this.timeSinceLastRun = System.currentTimeMillis();
                    //当前建筑工不为空并且 职业不是建筑工
                    if (this.theFolk.theirJob != null && this.theFolk.vocation != Vocation.BUILDER) {
                        //解雇
                        this.theFolk.selfFire();
                    } else {
                        //更新实体位置
                        this.theFolk.updateLocationFromEntity();
                        //获得NPC与雇佣点的距离
                        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                        //如果距离小于等于3 并且阶段为分配工人
                        if (dist <= 3 && this.theStage == Stage.WORKERASSIGNED) {
                            //NPC设置为去上班
                            this.theFolk.action = FolkAction.ATWORK;
                            this.theFolk.statusText = I18n.format("container.sim.job.builder_Arrived");
                            //阶段为获取蓝图
                            this.theStage = Stage.BLUEPRINT;
                        }
                        //如果距离小于10 并且阶段为分配工人 并且npc目的地为空
                        if (dist < 10 && this.theStage == Stage.WORKERASSIGNED && this.theFolk.destination == null) {
                            //NPC设置为去上班
                            this.theFolk.action = FolkAction.ATWORK;
                            this.theFolk.statusText = I18n.format("container.sim.job.builder_Arrived");
                            //阶段为获取蓝图
                            this.theStage = Stage.BLUEPRINT;
                        }
                        //（如果阶段为闲置  或者 为分配工人）为白天
                        if ((this.theStage == Stage.IDLE || this.theStage == Stage.WORKERASSIGNED) && ModSimReloaded.isDayTime()) {
                            //如果npc 不是工作途中
                            if (this.theFolk.action != FolkAction.ONWAYTOWORK) {
                                //阶段为分配员工
                                this.theStage = Stage.WORKERASSIGNED;
                            }
                            //如果不为分配员工
                        } else if (this.theStage != Stage.WORKERASSIGNED) {
                            //阶段为蓝图
                            if (this.theStage == Stage.BLUEPRINT) {
                                this.stageBlueprint();
                                //阶段等待资源
                            } else if (this.theStage == Stage.WAITINGFORRESOURCES) {
                                this.stageWaitingForResources();
                                //正在进行
                            } else if (this.theStage == Stage.INPROGRESS) {
                                this.stageInProgress();
                                //完成
                            } else if (this.theStage == Stage.COMPLETE) {
                                this.stageComplete();
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 阶段为蓝图
     */
    private void stageBlueprint() {
        try {
            this.theBuilding = this.theFolk.theBuilding;
            if (this.theBuilding == null) {
                //请您选择要我建造的建筑
                this.theFolk.statusText = I18n.format("container.sim.job.builder_building");
            } else {
                //翻翻蓝图......
                this.theFolk.statusText = I18n.format("container.sim.job.builder_blueprints");
                //更新实体位置
                this.theFolk.updateLocationFromEntity();
                //获取实体到雇佣点的距离
                double dist = (double) this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                //距离小于4，留在原地
                if (dist < 4) {
                    this.theFolk.stayPut = true;
                }
                //如果允许 NPC 说话
                if (ConfigLoader.configFolkTalking) {
                    //判断性别，发出不一样的声音
                    if (this.theFolk.gender == 0) {
                        this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readym", 1.0F, 1.0F, false);
                    } else {
                        this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readyf", 1.0F, 1.0F, false);
                    }
                }
                //等待资源
                this.theStage = Stage.WAITINGFORRESOURCES;
                this.step = 1;
                //实体的建筑箱
                if (this.theConBox == null) {
                    World world = MinecraftServer.getServer().worldServerForDimension(this.theFolk.location.theDimension);
                    this.theConBox = new EntityConBox(world);
                    this.theConBox.theFolk = this.theFolk;
                    this.theConBox.setLocationAndAngles(this.theFolk.employedAt.x + 2, this.theFolk.employedAt.y, this.theFolk.employedAt.z, 0.0F, 0.0F);
                    if (!world.isRemote) {
                        world.spawnEntityInWorld(this.theConBox);
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageBlueprint出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    /**
     * 等待资源的阶段
     */
    private void stageWaitingForResources() {
        try {
            //停止工作
            this.theFolk.isWorking = false;
            int dist;
            //步1
            if (this.step == 1) {
                //检查建设资源...
                this.theFolk.statusText = I18n.format("container.sim.job.builder_Checking");
                this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                if (this.constructorChests.size() == 0) {
                    //至少附近有一个箱子/存储方块。
                    this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_block");
                } else {
                    try {
                        //打开箱子
                        ((IInventory) this.constructorChests.get(0)).openInventory(mc.thePlayer);
                    } catch (Exception e) {
                        ModSimReloaded.log.warn("JobBuilder:JobBuilder's 的箱子是空的");
                    }

                    this.step = 2;
                }
                //获得NPC到目的地的距离
                dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                //小于5原地
                if (dist < 5) {
                    this.theFolk.stayPut = true;
                }
                //步2
            } else if (this.step == 2) {
                //关闭
                ((IInventory) this.constructorChests.get(0)).closeInventory(mc.thePlayer);
                //正在进行中
                this.theStage = Stage.INPROGRESS;
                //重置为步1
                this.step = 1;
                //如果步3
            } else if (this.step == 3) {
                //NPC职业不是建筑师
                if (this.theFolk.vocation != Vocation.BUILDER) {
                    //咨询解雇
                    this.theFolk.selfFire();
                    return;
                }
                //设置步2
                this.step = 2;
                //正在进行中
                this.theStage = Stage.INPROGRESS;
                //如果NPC已经生产
                if (this.theFolk.isSpawned()) {
                    //更新实体人数据
                    this.theFolk.updateLocationFromEntity();
                }
                //获得NPC到目的地的距离
                dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                //小于5原地
                if (dist < 5) {
                    this.theFolk.stayPut = true;
                } else {
                    //否则传输到目的地
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageWaitingForResources出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    /**
     * 接到为正在进行
     */
    private void stageInProgress() {
        try {
            //块id
            Block blockId = null;
            //以放置就绪
            boolean alreadyPlaced = false;
            //更新实体位置
            this.theFolk.updateLocationFromEntity();
            //获取实体与雇佣点的距离
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            //距离大于5并且NPC目的地为空
            if (dist > 5 && this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
                return;
            } else {
                //如果步骤1
                if (this.step == 1) {
                    this.cx = this.theFolk.employedAt.x.intValue();
                    this.cy = this.theFolk.employedAt.y.intValue();
                    this.cz = this.theFolk.employedAt.z.intValue();
                    this.ex = this.theFolk.employedAt.x.intValue();
                    this.ey = this.theFolk.employedAt.y.intValue();
                    this.ez = this.theFolk.employedAt.z.intValue();
                    this.bx = this.ex;
                    this.by = this.ey;
                    this.bz = this.ez;
                    if (this.theBuilding.buildDirection.contentEquals("-x")) {
                        this.bx = this.cx + 1;
                    } else if (this.theBuilding.buildDirection.contentEquals("+x")) {
                        this.bx = this.cx - 1;
                    } else if (this.theBuilding.buildDirection.contentEquals("-z")) {
                        this.bz = this.cz + 1;
                    } else if (this.theBuilding.buildDirection.contentEquals("+z")) {
                        this.bz = cz - 1;
                    } else {
                        if (!this.theBuilding.buildDirection.contentEquals("+z")) {
                            //不能确定建造的方向，当你右键点击它时请站在构造的四边之一
                            ModSimReloaded.sendChat(I18n.format("container.sim.job.builder_constructor_direction"));
                            this.theFolk.selfFire();
                            return;
                        }

                        this.bz = this.cz - 1;
                    }
                    //开始建造
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_started_building") + this.theBuilding.displayNameWithoutPK);
                    //建造中
                    this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_started_Building") + this.theBuilding.displayNameWithoutPK;
                    if (this.theBuilding == null || this.theBuilding.layerCount == 0) {
                        //建筑图纸错误，删除中，请尝试其他建筑
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_started_misplaced"));
                        return;
                    }
                    //原地不动
                    this.theFolk.stayPut = true;
                    //建筑物为空 辞职
                    if (this.theBuilding == null) {
                        this.theFolk.selfFire();
                        return;
                    }

                    this.l = 0;
                    this.ftb = 0;
                    this.ltr = 0;
                    this.acount = 0;
                    this.step = 2;
                    this.theBuilding.blockLocations.clear();
                } else if (this.step == 2) {
                    do {
                        //已经开始建筑一个
                        this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_started_Building") + this.theBuilding.displayNameWithoutPK;
                        if (this.theBuilding.buildDirection.contentEquals("+z")) {
                            this.xo = this.ltr;
                            this.zo = -this.ftb;
                        } else if (this.theBuilding.buildDirection.contentEquals("-z")) {
                            this.xo = -this.ltr;
                            this.zo = this.ftb;
                        } else if (this.theBuilding.buildDirection.contentEquals("+x")) {
                            this.xo = -this.ftb;
                            this.zo = -this.ltr;
                        } else if (this.theBuilding.buildDirection.contentEquals("-x")) {
                            this.xo = this.ftb;
                            this.zo = this.ltr;
                        }
                        //建筑物为空 辞职
                        if (this.theBuilding == null) {
                            this.theFolk.selfFire();
                            return;
                        }

                        String[] bl = null;
                        int st = 0;
                        try {
                            //获取结构体
                            bl = this.theBuilding.structure[this.acount].split(":");
                        } catch (Exception e) {
                            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBuilder: 建筑中的空块,改用空气");
                            bl = "0:0".split(":");
                        }
                        //获取块id
                        blockId = Block.getBlockFromName(bl[0]);
                        //ModSimReloaded.log.info("***************blockId:" + blockId);
                        //转为int
                        int subtype = Integer.parseInt(bl[1]);
                        //草方块改为泥土
                        if (blockId == Blocks.grass) {
                            blockId = Blocks.dirt;
                        }
                        //类型为其他
                        if (this.theBuilding.type.contentEquals("other") && this.acount == 0) {
                            blockId = BlockLoader.blockControlBox;
                            subtype = 2;//控制箱其他
                        }
                        //获得控制箱id
                        if (blockId == BlockLoader.blockControlBox) {
                            try {
                                //主体的坐标
                                this.theBuilding.primaryXYZ = new V3((double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), this.theFolk.employedAt.theDimension);
                                //保存建筑
                                this.theBuilding.saveThisBuilding();
                            } catch (Exception e) {
                                StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.warn("JobBuilder:构建为空" + e.getMessage()+"行数："+element.getLineNumber());
                            }
                        }

                        V3 v3;
                        //地毯 并且建筑为住宅
                        if (blockId == BlockLoader.blockLiving && this.theBuilding.type == "residential") {
                            //生活区
                            this.theBuilding.livingXYZ = new V3((double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), this.theFolk.employedAt.theDimension);
                            blockId = null;
                            subtype = 0;
                            //如果方块为特除 并且 为住宅
                        } else if (blockId == BlockLoader.blockSpecial && this.theBuilding.type != "residential") {
                            v3 = new V3((double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), this.theFolk.employedAt.theDimension);
                            v3.meta = subtype;
                            this.theBuilding.blockSpecial.add(v3);
                            blockId = null;
                            subtype = 0;
                        }
                        Block currBlockId;
                        try {
                            currBlockId = this.jobWorld.getBlockState(new BlockPos(this.bx + this.xo, this.by + this.l, this.bz + this.zo)).getBlock();
                            //
                            if (blockId != currBlockId && (blockId != Blocks.dirt || currBlockId != Blocks.grass)) {
                                alreadyPlaced = false;
                            } else {
                                alreadyPlaced = true;
                            }
                        } catch (Exception e) {
                            this.theFolk.selfFire();
                            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("******************建筑错误，NPC辞职:" + e.getMessage()+"行数："+element.getLineNumber());
                            return;
                        }

                        String want = "???";
                        //获取
                        ItemStack wantIS = new ItemStack(blockId, 1, 0);
                        if (wantIS != null) {
                            try {
                                //获取物品名称
                                want = wantIS.getDisplayName();
                                if (blockId != null) {
                                    this.theBuilding.blockLocations.add(new V3(this.bx + this.xo, this.by + this.l, this.bz + this.zo, this.theFolk.location.theDimension));
                                }
                            } catch (Exception e) {
                                want = "?";
                                //StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBuilder:wantItemStack 为空, wantIS 为空, blockID=" + blockId);
                            }
                        } else {
                            want = "???";
                        }
                        //放置未完成
                        if (!alreadyPlaced && currBlockId != null) {

                            V3 blockToRemove = new V3(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                            //找到最近的箱子
                            this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                            //将矿块开采到箱子中
                            this.mineBlockIntoChests(this.constructorChests, blockToRemove);

                            BlockPos blockPos = new BlockPos(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                            this.jobWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
                            //设置正在工作
                            this.theFolk.isWorking = true;
                        }

                        if (!alreadyPlaced) {
                            boolean gotBlock = false;
                            boolean requiredBlocks = blockId == Blocks.planks || blockId == Blocks.cobblestone || blockId == Blocks.glass || blockId == Blocks.wool || blockId == Blocks.brick_block || blockId == Blocks.dirt || blockId == Blocks.stonebrick || blockId == Blocks.oak_fence || blockId == Blocks.stone || blockId == Blocks.log;
                            ItemStack got;
                            if (GameMode.gameMode == GameMode.GAMEMODES.NORMAL) {
                                if (requiredBlocks) {
                                    this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                                    got = inventoriesGet(this.constructorChests, new ItemStack(blockId, 1, 0), false, false);
                                    if (got != null) {
                                        gotBlock = true;
                                    } else {
                                        gotBlock = false;
                                    }
                                } else {
                                    gotBlock = true;
                                }
                            } else if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                                gotBlock = true;
                            } else if (GameMode.gameMode == GameMode.GAMEMODES.HARDCORE) {
                                if (blockId != null) {
                                    if (blockId != Blocks.grass && blockId != Blocks.water && blockId != Blocks.water && blockId != Blocks.lava && blockId != Blocks.lava && blockId != Blocks.wall_sign && blockId != Blocks.cake && blockId != Blocks.stone_slab && blockId != Blocks.wooden_slab && blockId != Blocks.double_wooden_slab && blockId != Blocks.double_stone_slab && blockId != Blocks.farmland && blockId != Blocks.oak_door && blockId != Blocks.iron_door && blockId != Blocks.bed) {
                                        this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                                        got = inventoriesGet(this.constructorChests, new ItemStack(blockId, 1, 0), false, false);
                                        if (got != null) {
                                            gotBlock = true;
                                        } else {
                                            gotBlock = false;
                                        }

                                        if (blockId == BlockLoader.blockControlBox) {
                                            gotBlock = true;
                                        }
                                    } else {
                                        gotBlock = true;
                                    }
                                } else {
                                    gotBlock = true;
                                }
                            }

                            if (!gotBlock) {
                                this.theStage = Stage.WAITINGFORRESOURCES;
                                //木板
                                if (want.toLowerCase().contentEquals(I18n.format("container.sim.sim_gui_BC11"))) {
                                    want = I18n.format("container.sim.sim_gui_BC12");
                                }
//原木                        //橡木
                                if (want.toLowerCase().contentEquals(I18n.format("container.sim.sim_gui_BC9"))) {
                                    want = I18n.format("container.sim.sim_gui_BC10");
                                }
                                //等待
                                this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_started_Waiting") + want;
                                if (System.currentTimeMillis() - this.lastNotifiedOfMaterials > (long) (ConfigLoader.configMaterialReminderInterval * 60 * 1000)) {
                                    this.lastNotifiedOfMaterials = System.currentTimeMillis();
                                    ModSimReloaded.sendChat(this.theFolk.name + " ( " + I18n.format("container.sim.job.builder_constructor_started_who's") + this.theFolk.theBuilding.displayNameWithoutPK + ")" + I18n.format("container.sim.job.builder_constructor_started_more") + want);
                                }

                                this.step = 3;
                                return;
                            }

                            try {
                                if (!alreadyPlaced) {
                                    try {
                                        if (blockId == BlockLoader.blockLiving) {
                                            alreadyPlaced = true;
                                        }

                                        if (blockId == BlockLoader.blockControlBox && this.theBuilding.displayNameWithoutPK.toLowerCase().contentEquals(I18n.format("container.sim.ATMs"))) {
                                            subtype = 1;
                                        }
                                        //把积木放好
                                        if (!alreadyPlaced) {
                                            this.theFolk.stayPut = true;
                                            BlockPos blockPos = new BlockPos(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                                            this.jobWorld.setBlockState(blockPos, blockId.getDefaultState(), 3);
                                            this.jobWorld.markBlockForUpdate(blockPos);
                                        }

                                        int b4 = (int) Math.floor((double) this.theFolk.levelBuilder);
                                        if (this.theFolk.levelBuilder < 10.0F) {
                                            FolkData var10000 = this.theFolk;
                                            var10000.levelBuilder = (float) ((double) var10000.levelBuilder + 0.001 / (double) b4);
                                        }

                                        int aft = (int) Math.floor((double) this.theFolk.levelBuilder);
                                        if (b4 != aft) {
                                            //刚刚升级到建造者等级
                                            ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_levelled") + aft);
                                        }
                                        //每2秒播放一次音效
                                        if (System.currentTimeMillis() - this.soundLastPlayed >= 2000L) {
                                            this.mc.theWorld.playSound((double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), ModSim.MODID + ":construction", 1.0F, 1.0F, false);
                                            this.soundLastPlayed = System.currentTimeMillis();
                                        }

                                        if (this.mc.theWorld.isRemote) {
                                            this.mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), 0, 0.3f, 0);
                                            this.mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), 0, 0.2f, 0);
                                            this.mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), 0, 0.1f, 0);
                                        }

                                        if (blockId != null && GameMode.gameMode != GameMode.GAMEMODES.CREATIVE && blockId != BlockLoader.blockLiving) {
                                            GameStates var25 = ModSimReloaded.states;
                                            var25.credits -= 0.02F;
                                        }
                                    } catch (Exception e) {
                                        ModSimReloaded.log.warn("JobBuilder: 可能不存在的方块（来自其他模组）ID=" + blockId);
                                            BlockPos blockPos = new BlockPos(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                                            this.jobWorld.setBlockState(blockPos, blockId.getDefaultState(), 3);
                                    }
                                }
                            } catch (Exception e) {
                                StackTraceElement element=e.getStackTrace()[0];
                                ModSimReloaded.log.warn("错误：" + e.getMessage()+"行数："+element.getLineNumber());
                            }
                        }

                        this.acount++;
                        this.ltr++;
                        if (this.ltr == this.theBuilding.ltrCount) {
                            this.ltr = 0;
                            this.ftb++;
                            if (this.ftb == this.theBuilding.ftbCount) {
                                this.ftb = 0;
                                this.l++;
                                if (this.l == this.theBuilding.layerCount) {
                                    //完成
                                    this.theStage = Stage.COMPLETE;
                                    this.stageComplete();
                                    return;
                                }
                            }
                        }

                        if (blockId != null && !alreadyPlaced) {
                            if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                                this.runDelay = 0;
                            } else {
                                this.runDelay = (int) (2000.0F / this.theFolk.levelBuilder);
                            }
                        } else {
                            this.runDelay = 0;
                        }

                        if (this.theFolk.theEntity != null) {
                            this.theFolk.theEntity.swingItem();
                        }
                    } while (blockId == null || alreadyPlaced);
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageInProgress出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 阶段完成
     */
    private void stageComplete() {
        try {
            this.theFolk.isWorking = false;
            if (this.theBuilding != null) {
                if (this.theBuilding.buildingComplete) {
                }

                if (this.theBuilding != null) {
                    this.theBuilding.buildingComplete = true;
                    //已完成建设
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_completed") + this.theBuilding.displayNameWithoutPK);
                    ModSim.proxy.getClientWorld().playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                    this.theBuilding.saveThisBuilding();
                    this.theFolk.theBuilding = null;
                } else {
                    //错误：无法设置该建筑物 正在建设“完成”,尝试立即重建（免费）再试一次
                    ModSimReloaded.sendChat(I18n.format("container.sim.job.builder_constructor_Error") + this.theFolk.name + I18n.format("container.sim.job.builder_constructor_was_building"));
                }
            }

            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.setSneaking(false);
            }

            this.theFolk.stayPut = false;
            this.theFolk.selfFire();
            this.theStage = Stage.IDLE;
            boolean activeBuilders = false;

            int b;
            for (b = 0; b < ModSimReloaded.theFolks.size(); ++b) {
                FolkData fd = (FolkData) ModSimReloaded.theFolks.get(b);
                if (fd.vocation == Vocation.BUILDER) {
                    activeBuilders = true;
                }
            }

            if (!activeBuilders) {
                for (b = 0; b < ModSimReloaded.theBuildings.size(); ++b) {
                    Building building = (Building) ModSimReloaded.theBuildings.get(b);
                    building.buildingComplete = true;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageComplete出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    /**
     * 到达上班地点
     */
    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                //到达建筑工地
                this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_site");
                this.theStage = Stage.BLUEPRINT;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.WALK);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}

