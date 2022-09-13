package com.trhsy.sim.common.core.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.core.entity.folk.genetics.Race;
import com.trhsy.sim.common.core.entity.folk.genetics.Races;
import com.trhsy.sim.common.core.entity.folk.traits.Trait;
import com.trhsy.sim.common.core.entity.folk.traits.Traits;
import com.trhsy.sim.common.jobs.*;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.NetWorkLoader;
import com.trhsy.sim.packets.client.UpdateFolkPositionPacket;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @Description todo npc 逻辑和属性 即使EntityFolk破产，也会被勾选。
 * @Author Tian
 * @Date 2022/5/2120:43
 **/
public class FolkData implements Serializable {
    private static final long serialVersionUID = -2617939828256928361L;
    /**
     * @Author fan
     * @Description //TODO 员工的工作地点，如果他们失业，则为零
     * @Date 11:04 2022/3/26
     * @Param
     **/
    public V3 employedAt = null;
    //职业 他们的职业类型或null（如果失业）-用于创建他们的工作对象
    public Vocation vocation = null;
    /**
     * 对其工作（工作类别和子类别）的引用，如果失业，则为空-根据其职业领域在启动时创建
     **/
    protected ItemStack[] validTools = new ItemStack[0];
    //当前工作
    public transient Job theirJob = null;
    //姓名 以字符串形式显示其全名
    public String name = "";
    //年龄 0至17岁的人=儿童18+成人
    public int age = 18;
    //性别 0=男性1=女性
    public int gender = 0;
    //皮肤数
    public int skinnumber = 1;
    //npc 亲属
    public Race folkRace = null;
    //亲属名称
    public String folkRaceName = "";
    /**
     * 与他们合作的人的姓名
     */
    // public String partneredWith = "";
    //特点
    public String trait1 = "";
    public String trait2 = "";
    public String trait3 = "";
    public String trait4 = "";

    //食物等级 10=食物充足0=饥饿
    public int levelFood = 10;
    //快乐等级 10=爆炸0=应力
    public int levelFun = 10;
    //社会地位
    public int levelSocial = 10;
    //环境
    public int levelEnvironment = 10;
    //建筑等级 10=爆炸0=应力
    public float levelBuilder = 1;
    //采矿等级 10=爆炸0=应力
    public float levelMiner = 1;
    //士兵等级 10=爆炸0=应力
    public float levelSoldier = 1;
    /**
     * ODO 活动 行动 他们当前的行动（在家工作等）
     **/
    public FolkAction action = FolkAction.WANDER;
    //NULL或到达时应打开的折叠操作
    public FolkAction actionArrival = null;
    /**
     * 是否站在原地不动 默认是false
     */
    public Boolean stayPut = false;
    /**
     * 目的地 目标为V3，如果没有，则为null
     */
    public V3 destination = null;
    /**
     * 其当前位置，包括维度（在工作和模拟工作时更新）
     */
    public V3 location = null;
    //状态文本 出现在他们头上的状态文本
    public String statusText = I18n.format("container.sim.folk_data.Wandering");
    //状态1
    public String status1 = "";
    //状态1
    public String status2 = "";
    //状态1
    public String status3 = "";
    //状态1
    public String status4 = "";
    //乐趣状态
    public String funStatus = "";
    //社会地位

    public String socialStatus = "";
    //环境状况
    public String environmentStatus = "";
    //交配阶段
    public float shaggingStage = 0.0f;
    //妊娠期
    public float pregnancyStage = 0.0f;
    //正在工作
    public transient boolean isWorking = false;
    //今天是否打招呼
    public boolean greetedToday = false;
    //最后说话的时间
    public static transient long anyFolkLastSpoke = 0L;
    //无法存储在JOB类中的各种与工作相关的变量
    //建筑物 参考他们正在建造的建筑（如果他们是建筑商）-bodge，但没有其他地方可以保存它
    public Building theBuilding = null;
    //地形成型器类型
    public TerraformerType terraformerType = null;
    //地形形成器半径
    public int terraformerRadius = 1;

    // npc 自己的物品清单，用于在世界各地运送物品和寄送物品
    //设置库存
    private InventoryBasic villagerInventory;
    //实体人 对实体的引用，以便我们可以检查它的isDead（）并处理它等
    public transient EntityFolk theEntity;
    //开始去的时间 当他们去和走路/微笑时设置，如果他们不能在40秒内到达那里，则用于向他们微笑
    public transient Long timeStartedGotoing = 0L;
    public transient GotoMethod gotoMethod;
    //自上次保存以来的时间
    private transient long timeSinceLastSave = 0L;
    //自上次状态更新以来的时间
    private transient long timeSinceLastStatusUpdate = 0L;
    //自最后一分钟起的时间
    private transient long timeSinceLastMinute = 0L;
    //它们发送到的位置，如果不发送，则为空
    public transient V3 beamingTo;
    //挂起
    private transient FolkData hangingWith = null;
    //通话计数器
    private transient int talkCounter = 0;
    //交配阶段 -1今天没有0.0到0.9=有1.0=有
    protected transient float matingStage = -1;
    //实体id
    private transient int entityId;

    public FolkData() {
        try {
            //用于sk2文件加载
            this.action= FolkAction.WANDER;
            this.actionArrival = null;
            this.stayPut = false;
            this.destination = null;
            this.location = null;
            this.statusText = I18n.format("container.sim.folk_data.Wandering");
            this.status1 = "";
            this.status2 = "";
            this.status3 = "";
            this.status4 = "";
            this.shaggingStage = 0.0F;
            this.pregnancyStage = 0.0F;
            this.isWorking = false;
            this.greetedToday = false;
            this.theBuilding = null;
            this.terraformerType = null;
            this.terraformerRadius = 1;
            this.villagerInventory = new InventoryBasic("Items", false, 64);
            this.theEntity = null;
            this.timeStartedGotoing = 0L;
            this.gotoMethod = null;
            this.timeSinceLastSave = 0L;
            this.timeSinceLastStatusUpdate = 0L;
            this.timeSinceLastMinute = 0L;
            this.beamingTo = null;
            this.talkCounter = 0;
            this.matingStage = -1;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("FolkData出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 在反序列化这些人以激活他们、使他们重生并添加到arraylist后调用
     */
    public void hasLoaded() {
        try {
            //雇佣地点
            if (this.employedAt == null) {
                this.vocation = null;
            }
            //职业
            if (this.vocation == null) {
                this.employedAt = null;
            }

            if (this.levelMiner < 1) {
                this.levelMiner = 1;
            }

            if (this.levelBuilder < 1) {
                this.levelBuilder = 1;
            }

            if (this.levelSoldier < 1) {
                this.levelSoldier = 1;
            }

            //初始化NPC背包
            this.villagerInventory = new InventoryBasic("Items", false, 64);
            //安排他们的工作
            setTheirJob(this.vocation);
            ModSimReloaded.log.info(name + ",开始重生了");
            respawnEntity(MinecraftServer.getServer().worldServerForDimension(0));
            ModSimReloaded.theFolks.add(this);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("hasLoaded出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 创建一个具有随机名称、皮肤等的新人物，并加入arrayList
     *
     * @param theWorld
     */
    public FolkData(World theWorld) {
        try {
            Random rand = new Random();
            this.gender = rand.nextInt(2);
            this.name = generateName(this.gender, false, "");
            this.age = 18;
            //male 男性
            if (this.gender == 0) {
                //folkRace = Races.raceList.get(rand.nextInt(Races.raceList.size()));
                //folkRaceName = folkRace.getRaceName();
                this.skinnumber = rand.nextInt(64);
            } else {
                //folkRace = Races.raceList.get(rand.nextInt(Races.raceList.size()));
                //folkRaceName = folkRace.getRaceName();
                this.skinnumber = rand.nextInt(64);
            }
            int fs = Races.raceList.size();
            this.folkRace = Races.raceList.get(rand.nextInt(fs));
            this.folkRaceName = this.folkRace.getRaceName();
            //folkRaceName = Races.raceList.get(rand.nextInt(Races.raceList.size())).getRaceName();
            this.location = getLocationCloseToPlayer();
            if (this.location == null) {
                return;
            }
            //生成特征
            generateTraits();
            respawnEntity(theWorld);
            ModSimReloaded.theFolks.add(this);
            //刚刚进入该地区。
            String just = I18n.format("container.sim.folk_data_just");
            ModSimReloaded.sendChat(this.name + just);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("FolkData出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public FolkData(World theWorld, String theName) {
        try {
            Random rand = new Random();
            this.gender = rand.nextInt(2);
            this.name = theName;
            this.age = 18;
            if (this.gender == 0) {
                //folkRace = Races.raceList.get(rand.nextInt(Races.raceList.size()));
                //folkRaceName = folkRace.getRaceName();
                this.skinnumber = rand.nextInt(64); //1 to 63    male;
            } else {
                //folkRace = Races.raceList.get(rand.nextInt(Races.raceList.size()));
                //folkRaceName = folkRace.getRaceName();
                this.skinnumber = rand.nextInt(64); //1 to 58    female;
            }
            this.folkRace = Races.raceList.get(rand.nextInt(Races.raceList.size()));
            this.folkRaceName = this.folkRace.getRaceName();
            //folkRaceName = Races.raceList.get(rand.nextInt(Races.raceList.size())).getRaceName();


            this.location = getLocationCloseToPlayer();

            if (this.location == null) {
                return;
            }
            generateTraits();
            respawnEntity(theWorld);
            ModSimReloaded.theFolks.add(this);
            //刚刚进入该地区。
            String just = I18n.format("container.sim.folk_data_just");
            ModSimReloaded.sendChat(this.name + just);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("FolkData出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 将一个全新的孩子带到世界中，传回参考folkData
     *
     * @param theWorld
     * @param mother
     * @param father
     */
    public FolkData(World theWorld, FolkData mother, FolkData father) {
        try {
            Random rand = new Random();
            String surname = "Unknown";
            generateTraits();

            if (father != null) {
                surname = father.name.substring(father.name.indexOf(" ") + 1).trim();
            } else if (mother != null) {
                surname = mother.name.substring(mother.name.indexOf(" ") + 1).trim();
            }

            this.gender = rand.nextInt(2);

            this.name = generateName(this.gender, true, surname) + " " + surname;
            this.age = 0;
            if (this.gender == 0) {

                this.skinnumber = rand.nextInt(64);
            } else {
                this.skinnumber = rand.nextInt(64);
            }
            this.folkRace = Races.raceList.get(rand.nextInt(Races.raceList.size()));
            this.folkRaceName = this.folkRace.getRaceName();

            if (mother.getHome() != null) {
                mother.getHome().tenants.add(this.name);
            }

            mother.updateLocationFromEntity();
            World mworld = null;
            if (mother.isSpawned()) {
                mworld = mother.theEntity.worldObj;
            }

            this.location = Job.findAdjacentSpace(mother.location, mworld);
            respawnEntity(theWorld);
            ModSimReloaded.theFolks.add(this);
            //刚刚诞生！
            String born = I18n.format("container.sim.folk_data_born");
            ModSimReloaded.sendChat(this.name + born);
            World world = ModSim.proxy.getClientWorld();
            if (world != null) {
                EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                if (p != null) {
                    ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":birth", 1, 1, false);
                }
            }

            Relationship.setupBloodRelationships(this, father, mother);
            //从父母那里继承技能

            this.levelBuilder = (float) Math.floor((double) (father.levelBuilder / 2.0F)) + (float) Math.floor((double) (mother.levelBuilder / 2.0F));
            if (this.levelBuilder > 10.0F) {
                this.levelBuilder = 10.0F;
            }

            this.levelMiner = (float) Math.floor((double) (father.levelMiner / 2.0F)) + (float) Math.floor((double) (mother.levelMiner / 2.0F));
            if (this.levelMiner > 10.0F) {
                this.levelMiner = 10.0F;
            }

            this.levelSoldier = (float) Math.floor((double) (father.levelSoldier / 2.0F)) + (float) Math.floor((double) (mother.levelSoldier / 2.0F));
            if (this.levelSoldier > 10.0F) {
                this.levelSoldier = 10.0F;
            }
        } catch (Exception e) {
            //一位家长去世了
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("FolkData出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 更新实体位置
     * 从esc菜单调用，根据实体所在的位置（如果实体有）保存此人员位置
     */
    public void updateLocationFromEntity() {
        try {
            //theEntity.setPosition(location.xCoord, location.yCoord, location.zCoord);
            //theEntity.setLocationAndAngles(location.xCoord, location.yCoord, location.zCoord, 0.0F, 0.0F);
            if (isSpawned()) {
                this.location = new V3(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ, this.location.theDimension);
                //theEntity.setPosition(location.xCoord,location.yCoord,location.zCoord);
                //location = new V3(theEntity.lastTickPosX, theEntity.lastTickPosY, theEntity.lastTickPosZ, location.theDimension);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("updateLocationFromEntity出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 重生该实体，但仅当其距离玩家50格以内时
     *
     * @param world
     */
    public void respawnEntity(World world) {
        try {
            if (world == null) {
                return;
            }
            //不要在传送中间重新生成
            if (this.beamingTo != null) {
                return;
            }
            //已经繁殖了，所以不需要
            if (this.theEntity != null) {
                if (!this.theEntity.isDead) {
                    return;
                }
            }
            if (this.employedAt != null) {
                this.location = this.employedAt.clone();
            }
            if (getDistanceToPlayer() < 100) {
                this.theEntity = new EntityFolk(world);
                //设置实体在世界中的位置和偏航/俯仰
                V3 v=new V3(this.location.xCoord-0.5, this.location.yCoord+1, this.location.zCoord+0.5);
                this.theEntity.setLocationAndAngles(v.xCoord,v.yCoord,v.zCoord, 0.0F, 0.0F);
                //if(theEntity.getCanSpawnHere()){}
                //theEntity.setPosition(location.xCoord, location.yCoord, location.zCoord);
                if (!world.isRemote) {
                    if (this.theEntity.isDead || this.theEntity.theData == null) {
                        this.theEntity.theData = this;
                        world.spawnEntityInWorld(this.theEntity);
                        this.location=v.clone();
                        //theEntity.isDead = false;
                        ModSimReloaded.log.info("NPC【" + name + "】在当前位置已重生，x:" + v.xCoord + ",y:" + v.yCoord + ",z:" + v.zCoord + " 维度:" + location.theDimension + " 实体id:" + theEntity.getEntityId());
                    }
                }
                this.entityId = this.theEntity.getEntityId();

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("respawnEntity出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 触发所有更新
     * 这是从CommonTickHandler调用的，用于触发所有folkData onUpdate（）
     */
    public static void triggerAllUpdates() {
        try {
            for (int f = 0; f < ModSimReloaded.theFolks.size(); f++) {
                FolkData fd = ModSimReloaded.theFolks.get(f);
                fd.onUpdate();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("triggerAllUpdates出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 服务器到客户端位置更新
     * 运行客户端接收更新，而非实现？
     *
     * @param newLocation
     */
    public void serverToClientLocationUpdate(V3 newLocation) {
        try {
            this.location = newLocation.clone();
            if (this.theEntity != null) {
                newLocation = new V3(Math.floor(newLocation.xCoord) + 0.5, newLocation.yCoord, Math.floor(newLocation.zCoord) + 0.5);
                this.theEntity.posX = newLocation.xCoord;
                this.theEntity.posY = newLocation.yCoord;
                this.theEntity.posZ = newLocation.zCoord;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("serverToClientLocationUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 更新
     * 更新循环，从CommonTickHandler的inGameTick间接频繁调用
     */
    public void onUpdate() {
        try {
            //和朋友一起
            String hanging = I18n.format("container.sim.folk_data_Hanging");
            //参观
            String visiting = I18n.format("container.sim.folk_data_Visiting");
            //待在家里
            String staying = I18n.format("container.sim.folk_data_Staying_home");
            //在家放松
            String relaxing = I18n.format("container.sim.folk_data_Relaxing_home");
            //在商店购物
            String shopping = I18n.format("container.sim.folk_data_Shopping");
            Random rand = new Random();
            Long now = System.currentTimeMillis();

            //60秒
            if (now - this.timeSinceLastMinute > 60000L) {
                //如果 状态有 和朋友一起，在商店购物，参观，待在家，在家放松
                if (!this.statusText.contains(hanging) && !this.statusText.startsWith(shopping) && !this.statusText.contains(visiting) && !this.statusText.contains(staying) && !this.statusText.contains(relaxing) && this.levelFun > 1 && !this.isWorking) {
                    //乐趣--
                    this.levelFun -= 1;
                }
                //在工作中并且乐趣大于11
                if (this.isWorking == true && this.levelFun > 1) {
                    this.levelFun -= 1;
                }
                //没有家
                if (getHome() == null && this.timeSinceLastMinute > 0L) {
                    getHomeForHomeless();
                }
                //和朋友在一起，并且社交大于1
                if (!this.statusText.contains(hanging) && this.levelSocial > 1) {
                    this.levelSocial -= 1;
                }
                //如果怀孕并在第9天分娩！
                long t = MinecraftServer.getServer().worldServers[0].getWorldTime() % 24000L;
                if (t < 2000L && this.pregnancyStage >= 1) {
                    //循环所有建筑物
                    for (Building build : ModSimReloaded.theBuildings) {
                        //诊所
                        String clinic = I18n.format("container.sim.FolkData.Clinic");
                        //建筑不为空 主体坐标不为空 建筑名字包括诊所
                        if (build != null && build.primaryXYZ != null && build.displayName.contains(clinic)) {
                            //目的地等于空
                            if (this.destination == null) {
                                if (!build.blockSpecial.isEmpty()) {
                                    V3 bed = build.blockSpecial.get(0);
                                    bed=new V3(bed.xCoord,bed.yCoord+1,bed.zCoord);
                                    gotoXYZ(bed, null);
                                    //就要生孩子了,她正在去诊所的路上！
                                    String baby = I18n.format("container.sim.folk_data_baby");
                                    ModSimReloaded.sendChat(this.name + baby);
                                }
                            }
                        }
                    }
                    //生孩子
                    this.action= FolkAction.HAVINGBABY;
                } else if (t > 2000L && this.pregnancyStage >= 1) {
                    //刚生了个孩子
                    this.statusText = I18n.format("container.sim.folk_data_a_baby");
                    this.pregnancyStage = 0.0F;
                    //和父母在一起
                    FolkData male = Relationship.isFolkLivingWithSomeone(this, true);
                    new FolkData(MinecraftServer.getServer().worldServerForDimension(0), this, male);
                }
                //待在家里或在家放松时，确保他们不会走得太远
                if (this.action== FolkAction.ATHOME || this.action== FolkAction.STAYINGHOME) {
                    //更新实体位置
                    updateLocationFromEntity();
                    V3 liveAt = null;
                    if (getHome() != null) {
                        if (getHome().livingXYZ != null) {
                            liveAt = getHome().livingXYZ.clone();
                        }
                    }

                    if (liveAt == null) {
                        if (getHome() != null) {
                            if (getHome().primaryXYZ != null) {
                                liveAt = getHome().primaryXYZ.clone();
                            }
                        }
                    }
                    if (liveAt != null) {
                        if (this.location.getDistanceTo(liveAt) > 5 && this.destination == null || this.location.theDimension != getHome().primaryXYZ.theDimension) {
                            this.actionArrival = action;
                            gotoXYZ(new V3(liveAt.xCoord,liveAt.yCoord+1,liveAt.zCoord), null);
                        }
                    }
                }
                //如果他们在闲逛，就让他们逛商店和房子
                boolean gotWanderPoint = false;
                //闲逛 已经生成 没有被雇佣 年龄大于18 状态中没有宝宝
                if (this.action== FolkAction.WANDER && isSpawned() && this.employedAt == null && this.age >= 18 && !this.statusText.contains(I18n.format("container.sim.folk_data.baby"))) {
                    for (int xo = 0; xo < ModSimReloaded.theBuildings.size(); xo++) {
                        //随机去逛
                        Building b = ModSimReloaded.theBuildings.get(rand.nextInt(ModSimReloaded.theBuildings.size()));
                        //获得距离建筑的距离
                        double dist = this.location.getDistanceTo(b.primaryXYZ);
                        //农场
                        String farm = I18n.format("container.sim.gui_Farm");
                        if (dist < 100) {
                            //商业
                            if (b.type.contentEquals("commercial")) {
                                //是否有店主
                                boolean hasShopKeeper = false;
                                for (int f = 0; f < ModSimReloaded.theFolks.size(); f++) {
                                    //获得店主
                                    FolkData keeper = ModSimReloaded.theFolks.get(f);
                                    //店主被雇佣并且 店主在店里
                                    if (keeper.employedAt != null && keeper.employedAt.isSameCoordsAs(b.primaryXYZ, true, true)) {
                                        hasShopKeeper = true;
                                        break;
                                    }
                                }
                                //有店主
                                if (hasShopKeeper) {
                                    //ModSimReloaded.log.info("FolkData:onUpdate() " + name + " 距离 " + b.displayName + " " + dist + " 个距离之外。");
                                    //设置走过去
                                    V3 v=new V3(b.primaryXYZ.xCoord,b.primaryXYZ.yCoord+1,b.primaryXYZ.zCoord);
                                    gotoXYZ(v, null);
                                    this.destination.doNotTimeout = true;
                                    this.statusText = shopping + b.displayName;
                                    gotWanderPoint = true;
                                    if (this.hangingWith != null) {
                                        //闲逛
                                        this.hangingWith.statusText = I18n.format("container.sim.folk_data.Wandering");
                                        this.hangingWith.hangingWith = null;
                                        this.hangingWith = null;
                                    }
                                    break;
                                }
                                //工业
                            } else if (b.type.contentEquals("industrial") && !b.displayName.toLowerCase().contains(farm)) {
                                //ModSimReloaded.log.info("FolkData: onUpdate() " + name + "距离" + b.displayName + " " + dist + " 个街区之外。");
                                if(b.primaryXYZ!=null){
                                    V3 v=new V3(b.primaryXYZ.xCoord,b.primaryXYZ.yCoord+1,b.primaryXYZ.zCoord);
                                    gotoXYZ(v, null);
                                    this.destination.doNotTimeout = true;
                                    this.statusText = I18n.format("container.sim.folk_data_Visiting") + b.displayName;
                                    gotWanderPoint = true;

                                    if (this.hangingWith != null) {
                                        this.hangingWith.statusText = I18n.format("container.sim.folk_data.Wandering");
                                        this.hangingWith.hangingWith = null;
                                        this.hangingWith = null;
                                    }
                                }
                                break;
                            } else if (b.type.contentEquals("residential") && hangingWith == null) {
                                if (b.tenants != null && b.tenants.size() > 0) {
                                    //住宅
                                    FolkData resy = getFolkByName(b.tenants.get(0));
                                    if (!resy.name.contentEquals(name) && resy.hangingWith == null) {
                                        if (resy.action == FolkAction.WANDER || resy.action == FolkAction.STAYINGHOME) {
                                            ModSimReloaded.log.info("FolkData:onUpdate() " + this.name + " 距离 " + b.displayName + " " + dist + " 个街区之外。");
                                            V3 v=new V3(b.primaryXYZ.xCoord,b.primaryXYZ.yCoord+1,b.primaryXYZ.zCoord);
                                            gotoXYZ(v, null);
                                            gotWanderPoint = true;
                                            hanging = I18n.format("container.sim.folk_data_Hanging");
                                            this.statusText = hanging + resy.name;
                                            V3 v1=new V3(b.primaryXYZ.xCoord,b.primaryXYZ.yCoord+1,b.primaryXYZ.zCoord);
                                            resy.gotoXYZ(v1, null);
                                            if (this.destination != null) {
                                                this.destination.doNotTimeout = true;
                                            }

                                            resy.statusText = hanging + name;
                                            this.hangingWith = resy;
                                            resy.hangingWith = this;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!gotWanderPoint) {
                        //随机让去一个地方
                        V3 wanderTo = new V3(this.location.xCoord + 1, this.location.yCoord, this.location.zCoord + 1, this.location.theDimension);
                        ModSimReloaded.log.info(this.name + ":要随机去一个地方，x:" + wanderTo.xCoord + ",y:" + wanderTo.yCoord + ",z:" + wanderTo.zCoord);
                        //WorldServer world = MinecraftServer.getServer().worldServerForDimension(location.theDimension);
                        //while (world.getBlockState(new BlockPos(wanderTo.xCoord, wanderTo.yCoord, wanderTo.zCoord)).getBlock() != null && wanderTo.yCoord < 255.0) {
                        //    //wanderTo.yCoord = wanderTo.yCoord + 1;
                        //    wanderTo = new V3(wanderTo.xCoord, wanderTo.yCoord + 1, wanderTo.zCoord);
                        //}

                        //ModSimReloaded.log.info("FolkData:onUpdate() 漫游命令 " + name + " to " + wanderTo.toString());
                        gotoXYZ(wanderTo, GotoMethod.WALK);
                        if (this.destination != null) {
                            this.destination.doNotTimeout = true;
                        }

                        this.statusText = I18n.format("container.sim.folk_data.Wandering");
                        this.stayPut = false;
                    }
                    //宝宝闲逛
                } else if (this.action== FolkAction.WANDER && isSpawned() && age < 18) {
                    //跟着TA妈妈
                    FolkData male = Relationship.getMotherOf(this);
                    if (male != null) {
                        gotoXYZ(male.location, null);
                    }
                }

                if (this.hangingWith != null) {
                    //闲逛
                    if (!this.hangingWith.statusText.contains(this.name)) {
                        this.hangingWith = null;
                        this.statusText = I18n.format("container.sim.folk_data.Wandering");
                    }
                }
                //晚上，并且和某人在一起 交配欲望小于0
                if (!ModSimReloaded.isDayTime() && Relationship.isFolkLivingWithSomeone(this) && this.matingStage < 0.0F) {
                    goHome();
                    //尝试生宝宝
                    tryForBaby();
                }

                this.timeSinceLastMinute = now - (long) rand.nextInt(20000);
            }
            /**每秒钟一次的任务**/
            if (now - this.timeSinceLastStatusUpdate > 1000L) {
                //没有特征 生成特征
                if (this.trait1 == "" || this.trait2 == "" || this.trait3 == "" || this.trait4 == "") {
                    generateTraits();
                }
                //更新NPC状态
                updateStatusLines();

                //如果你雇用他们时，他们正在一所民房闲逛，他们一到就被困在那里
                if (this.statusText.contentEquals(I18n.format("container.sim.gui.button_Going"))) {
                    this.stayPut = false;
                }
                if (this.action== FolkAction.WANDER) {
                    this.stayPut = false;
                }
                //在玩家射程内复活 //如果次人没有复活
                if (!isSpawned()) {
                    int range = getDistanceToPlayer();
                    //System.out.println(name+",距离玩家："+range);
                    if (range < 100) {
                        //重生
                        respawnEntity(MinecraftServer.getServer().worldServerForDimension(this.location.theDimension));
                        //ModSimReloaded.log.info("NPC" + name + "离玩家 " + range + " 个街区远,位于x:" + location.xCoord + ",y:" + location.yCoord + ",z:" + location.zCoord + ",所以下一刻被重生");
                    }
                } else {
                    //如果它们是繁殖的，看看它们是否在射程之外，并迫使它们绝望
                    this.theEntity.dimension = this.location.theDimension;
                    updateLocationFromEntity();
                    int range = getDistanceToPlayer();
                    if (range >= 100) {
                        if (this.theEntity != null) {
                            ModSimReloaded.log.info("NPC" + this.name + "离玩家 " + range + " 个街区远,位于x:" + this.location.xCoord + ",y:" + this.location.yCoord + ",z:" + this.location.zCoord + ",所以下一刻被摧毁");
                            this.theEntity.setDead();
                        }
                    }
                }
                //如果白天他们有工作就去工作
                if (ModSimReloaded.isDayTime() || isNightOwl()) {
                    //要工作了
                    if (this.employedAt != null && this.action!= FolkAction.ONWAYTOWORK && this.action!= FolkAction.ATWORK && this.pregnancyStage == 0.0F) {

                        this.statusText = I18n.format("container.sim.folk_data_Going_work");
                        this.action= FolkAction.ONWAYTOWORK;
                        V3 temp = employedAt.clone();
                        temp = new V3(temp.xCoord, temp.yCoord+1, temp.zCoord);
                        gotoXYZ(temp, GotoMethod.SHIFT);
//                        gotoXYZ(temp, null);
                        //ModSimReloaded.log.info("FolkData: " + this.name + " 要工作了,地址是：x:" + temp.xCoord + ",y:" + temp.yCoord + ",z:" + temp.zCoord);
                        return;
                    }
                }

                if (this.pregnancyStage > 0.0F && this.employedAt != null && ModSimReloaded.isDayTime()) {
                    //产假
                    this.statusText = I18n.format("container.sim.folk_data_Maternity_leave");
                }
                //如果白天他们有工作就去工作
                if (ModSimReloaded.isDayTime() || isNightOwl()) {
                    //去工作的路上
                    if (this.employedAt != null && this.action!= FolkAction.ATWORK && this.destination != null &&this.pregnancyStage == 0.0F) {
                        this.statusText = I18n.format("container.sim.folk_data_Going_work");
                        this.action= FolkAction.ONWAYTOWORK;
                        //ModSimReloaded.log.warn("FolkData:onUpdate() " + name + " 还在工作");
                        updateLocationFromEntity();
                        V3 temp = employedAt.clone();
                        temp = new V3(temp.xCoord, temp.yCoord+1, temp.zCoord);
                        gotoXYZ(temp, GotoMethod.SHIFT);
//                        gotoXYZ(temp, null);
                        //ModSimReloaded.log.info("FolkData: " + this.name + " 要工作了,地址是：x:" + temp.xCoord + ",y:" + temp.yCoord + ",z:" + temp.zCoord);
                        return;
                    }
                }
                if (this.action == FolkAction.ONWAYTOWORK) {
                    //去工作
                    this.statusText = I18n.format("container.sim.folk_data_Going_work");
                    this.stayPut = false;
                    if (this.destination == null) {
                        V3 temp = employedAt.clone();
                        temp = new V3(temp.xCoord, temp.yCoord+1, temp.zCoord);
                        gotoXYZ(temp, GotoMethod.SHIFT);
//                        this.gotoXYZ(temp, null);
                    }
                }
                if (this.action== FolkAction.STAYINGHOME && this.hangingWith == null) {
                    //待在家里
                    this.statusText = I18n.format("container.sim.folk_data_Staying_home");
                    this.stayPut = true;
                }
                //要个宝宝
                if (ModSimReloaded.isDayTime() && this.statusText.contains(I18n.format("container.sim.folk_data.for_a_baby"))) {
                    //徘徊
                    this.statusText = I18n.format("container.sim.folk_data.Wandering");
                    this.action= FolkAction.WANDER;
                }

                if (this.action== FolkAction.HAVINGBABY) {
                    if (this.pregnancyStage < 1) {
                        //刚生了个孩子
                        this.statusText = I18n.format("container.sim.folk_data_a_baby");
                    } else {
                        //有了孩子！
                        this.statusText = I18n.format("container.sim.folk_data_Having_baby");
                    }
                }
                //失业，所以呆在家里或流浪
                if (ModSimReloaded.isDayTime() && this.employedAt == null && this.action== FolkAction.ATHOME) {
                    this.isWorking = false;
                    if (new Random().nextInt(4) == 1) {
                        //在家
                        this.statusText = I18n.format("container.sim.folk_data_Staying_home");
                        this.action= FolkAction.STAYINGHOME;
                    } else {
                        //徘徊
                        this.statusText = I18n.format("container.sim.folk_data.Wandering");
                        this.action= FolkAction.WANDER;
                    }
                }

                if (this.action== FolkAction.STAYINGHOME && this.employedAt != null) {
                    //如果他们待在家里，然后找份工作
                    this.action= FolkAction.WANDER;
                }
                //晚上漫步或回家
                boolean isSoldier = false;
                if (this.vocation != null) {
                    if (this.vocation == Vocation.SOLDIER) {
                        isSoldier = true;
                    }
                }

                if (!ModSimReloaded.isDayTime() && !isSoldier) {
                    goHome();
                } else {
                    this.stayPut = false;
                }
                if (this.action== FolkAction.WANDER) {
                    this.stayPut = false;
                }
                //每隔15秒左右（10到20秒）救一次这个人
                int about10 = rand.nextInt(10000) + 10000;
                if (System.currentTimeMillis() - this.timeSinceLastSave > (long) about10) {
                    Side side = FMLCommonHandler.instance().getEffectiveSide();
                    if (side == Side.SERVER) {
                        UpdateFolkPositionPacket updateFolkPositionPacket = new UpdateFolkPositionPacket();
                        updateFolkPositionPacket.nbt = new NBTTagCompound();
                        updateFolkPositionPacket.nbt.setString("NPCDaTa", location.toString() + ";" + name);
                        NetWorkLoader.net.sendToServer(updateFolkPositionPacket);
                        saveThisFolk();
                    }

                    if (this.statusText.contains(hanging) && this.levelSocial < 10) {
                        this.levelSocial += 1;
                    }

                    if (this.statusText.contains(visiting) || this.statusText.contains(hanging) || this.statusText.contains(staying) || this.statusText.contains(relaxing) || this.statusText.contains(shopping) && this.levelFun < 10) {
                        this.levelFun += 1;
                    }
                    this.timeSinceLastSave = System.currentTimeMillis();
                }
                //和朋友在一起  或者 在购物
                if (this.statusText.contains(hanging) || this.statusText.contains(shopping)) {
                    if (this.destination == null && this.theEntity != null) {
                        this.talkCounter++;
                        if (this.talkCounter == 12) {
                            if (ConfigLoader.configFolkTalking) {
                                int chance = rand.nextInt(26) + 97;
                                String letter = ModSim.MODID + ":blarg" + Character.toString((char) chance);
                                ModSim.proxy.getClientWorld().playSound(this.location.xCoord, this.location.yCoord, this.location.zCoord, letter, 1, 1, false);
                            }

                            this.talkCounter = 0;
                            if (this.hangingWith != null) {
                                Relationship.meddleWithRelationship(this, this.hangingWith);
                            }
                        }
                    }
                }
                //生孩子
                if (this.matingStage >= 0.0F && this.matingStage < 1 && this.gender == 1 && this.pregnancyStage == 0.0F) {
                    if (ModSimReloaded.isDayTime()) {
                        this.matingStage = -1;
                    } else {
                        FolkData male = Relationship.isFolkLivingWithSomeone(this, true);
                        if (male != null) {
                            this.matingStage += 0.02F;
                            if (isSpawned()) {
                                World theWorld = null;
                                if (!MinecraftServer.getServer().isDedicatedServer()) {
                                    theWorld = Minecraft.getMinecraft().theWorld;
                                } else {
                                    theWorld = MinecraftServer.getServer().getEntityWorld();
                                }
                                double d0 = rand.nextDouble() * 0.5D;
                                double d1 = rand.nextDouble() * 0.5D;
                                double d2 = rand.nextDouble() * 0.5D;
                                theWorld.spawnParticle(EnumParticleTypes.HEART, this.theEntity.posX, this.theEntity.posY + 2.1, this.theEntity.posZ, d0, d1, d2);
                                male.updateLocationFromEntity();
                                if ((double) this.matingStage < 0.15D) {
                                    //有时，它们在交配过程中会走失LOL：-）
                                    gotoXYZ(male.location, GotoMethod.SHIFT);
                                }

                                theWorld.spawnParticle(EnumParticleTypes.HEART, male.location.xCoord, male.location.yCoord + 2.1, male.location.zCoord, d0, d1, d2);
                                //想要个孩子
                                this. statusText = I18n.format("container.sim.folk_data_Trying_baby");
                                male.statusText = I18n.format("container.sim.folk_data_Trying_baby");
                                male.stayPut = true;
                            }
                        }
                    }
                    //已完成
                } else if (this.matingStage >= 1 && this.matingStage < 1.1F) {
                    this.matingStage = 1.1F;
                    int chance = rand.nextInt(5);
                    ModSimReloaded.log.info("FolkData: 完成了对宝宝的尝试 = 机会:" + chance);
                    FolkData male = Relationship.isFolkLivingWithSomeone(this, true);
                    //在家放松
                    this.statusText = I18n.format("container.sim.folk_data_Relaxing_home");
                    male.statusText = I18n.format("container.sim.folk_data_Relaxing_home");
                    //5分之一的怀孕机会，女性也需要少于45岁
                    if (chance == 1 && this.age < 45) {
                        this.pregnancyStage = 0.1F;
                        //好消息！
                        String news = I18n.format("container.sim.folk_data_Good_news");
                        //和
                        String and = I18n.format("container.sim.folk_data_and");
                        //要生宝宝了！
                        String expecting_a_baby = I18n.format("container.sim.folk_data_expecting_a_baby");
                        ModSimReloaded.sendChat(news + this.name + and + male.name + expecting_a_baby);
                        if (isSpawned()) {
                            this.theEntity.setJumping(true);
                        }

                        if (male.isSpawned()) {
                            male.theEntity.setJumping(true);
                        }

                        World world = ModSim.proxy.getClientWorld();
                        if (world != null) {
                            EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                            if (p != null) {
                                ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":pregnant", 1, 1, false);
                            }
                        }
                    }
                }

                this.timeSinceLastStatusUpdate = now;
            }
            //其余的将在每次更新/勾选时运行
            //如果他们正在微笑，则执行微笑进度
            if (this.beamingTo != null) {
                doBeaming();
            }
            //日夜不停地更新工作内容
            if (this.theirJob != null) {
                this.theirJob.onUpdate();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("FolkData-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            e.printStackTrace();
        }
    }

    /**
     * 回家
     */
    private void goHome(){
    if (!isNightOwl()||this.action!= FolkAction.ATWORK) {
        this.action= FolkAction.WANDER;
        this.isWorking = false;
        if (getHome() == null) {
            //【徘徊
            this.statusText = I18n.format("container.sim.folk_data.Wandering");
            this.stayPut = false;
        } else {
            if (this.gotoMethod == GotoMethod.WALK) {
                updateLocationFromEntity();
            }

            V3 liveAt = null;
            if (getHome().livingXYZ != null) {
                liveAt = getHome().livingXYZ.clone();
            }

            if (liveAt == null) {
                if (getHome().primaryXYZ != null) {
                    liveAt = getHome().primaryXYZ.clone();
                }
            }

            if (liveAt != null) {
                int chance = this.location.getDistanceTo(liveAt);
                if (chance > 1 && this.destination == null) {
                    this.stayPut = false;
                    V3 v3=new V3(liveAt.xCoord,liveAt.yCoord+1,liveAt.zCoord);
                    gotoXYZ(v3, GotoMethod.SHIFT);
//                                    gotoXYZ(liveAt, null);
                    this.action= FolkAction.GOINGHOME;
                    //回家
                    this.statusText = I18n.format("container.sim.folk_data_Going_home");
                    this.isWorking = false;
                }

                if (chance <= 1 && !statusText.contains(I18n.format("container.sim.folk_data.baby"))) {
                    this.stayPut = true;
                    this.action= FolkAction.ATHOME;
                    //在家放松
                    this.statusText = I18n.format("container.sim.folk_data_Relaxing_home");
                    this.isWorking = false;
                }
            }
        }
    }
}
    /**
     * 这是在“一分钟一次”上调用的，但仅在夜间调用一次，并且只有在夜间，而且他们有一个伴侣
     */
    private void tryForBaby() {
        try {
            ModSimReloaded.log.info(this.name+"尝试要个小孩儿！");
            //只有非怀孕女性才需要这样做
            if (this.gender == 1 && this.pregnancyStage == 0.0F) {
                FolkData malePartner = Relationship.isFolkLivingWithSomeone(this, true);
                if (malePartner != null && this.action== FolkAction.ATHOME && malePartner.action== FolkAction.ATHOME) {
                    //拥有一秒钟的任务可以处理其余的任务
                    this.matingStage = 0.0F;
                    if (malePartner.isSpawned()) {
                        gotoXYZ(new V3(malePartner.theEntity.posX, malePartner.theEntity.posY+1, malePartner.theEntity.posZ, malePartner.theEntity.dimension), GotoMethod.SHIFT);
                    }
                }
            } else {
                this.matingStage = -1;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("tryForBaby出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    //无家可归者之家

    /**
     * 只有在他们无家可归的情况下才每秒运行一次
     */
    private void getHomeForHomeless() {
        try {
            Building.loadAllBuildings();
            if (this.action== FolkAction.WANDER) {
                for (int b = 0; b < ModSimReloaded.theBuildings.size(); b++) {
                    Building building = ModSimReloaded.theBuildings.get(b);
                    if (building.tenants.size() == 0 && building.buildingComplete == true && building.type.contentEquals("residential")) {
                        building.tenants.add(name);
                        this.action= FolkAction.GOINGHOME;
                        this.actionArrival = FolkAction.STAYINGHOME;
                        if (building.livingXYZ != null) {
                            V3 v3 = new V3(building.livingXYZ.xCoord, building.livingXYZ.yCoord+1, building.livingXYZ.zCoord, building.livingXYZ.theDimension);
                            gotoXYZ(v3, GotoMethod.SHIFT);
//                            gotoXYZ(v3, null);
                        } else {
                            V3 v3 = new V3(building.primaryXYZ.xCoord, building.primaryXYZ.yCoord+1, building.primaryXYZ.zCoord, building.primaryXYZ.theDimension);
                            gotoXYZ(v3, GotoMethod.SHIFT);
//                            gotoXYZ(v3, null);
                        }

                        ModSimReloaded.states.saveStates();
                        String moving = I18n.format("container.sim.folk_data_moving");
                        String Moved = I18n.format("container.sim.folk_data_Moved");
                        ModSimReloaded.sendChat(name + moving + building.displayNameWithoutPK);
                        this.statusText = Moved + building.displayNameWithoutPK;
                        building.saveThisBuilding();
                        break;
                    }
                }
            } else if (this.action== FolkAction.ATWORK) {
                boolean falg = true;
                for (int b = 0; b < ModSimReloaded.theBuildings.size(); b++) {
                    Building building = ModSimReloaded.theBuildings.get(b);
                    if (building.tenants.size() > 0 && building.buildingComplete && building.type.contentEquals("residential")) {
                        if (building.tenants.toString().contains(name)) {
                            falg = true;
                            break;
                        } else {
                            falg = false;
                        }
                    }
                }
                if (!falg) {
                    String noHome = I18n.format("container.sim.folk_data_noHome");
                    ModSimReloaded.sendChat(name + noHome);
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getHomeForHomeless出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

    /**
     * 更新状态行
     */
    private void updateStatusLines() {
        try {
            if (this.vocation == null) {
                //失业的
                this.status1 = I18n.format("container.sim.folkData1");
            } else {
                if (this.vocation != null) {
                    this.status1 = this.vocation.toString();
                }
            }
            //Random rand = new Random();
            Building building = getHome();
            if (building != null) {
                //拥有自己的房子
                this.status2 = I18n.format("container.sim.folkData2");
            } else {
                //无家可归的
                this.status2 = I18n.format("container.sim.folkData3");
            }

            if (!Relationship.isFolkLivingWithSomeone(this)) {
                //单身狗
                this.status3 = I18n.format("container.sim.folkData4");
            } else {
                //和某人住在一起
                this.status3 = I18n.format("container.sim.folkData5");
            }

            if (this.levelFood == 10) {
                //吃饱的
                this.status4 = I18n.format("container.sim.folkData6");
            } else if (this.levelFood > 5) {
                //有点饿
                this.status4 = I18n.format("container.sim.folkData7");
            } else if (this.levelFood > 1) {
                //很饿
                this.status4 = I18n.format("container.sim.folkData8");
            } else {
                //非常饿！
                this.status4 = I18n.format("container.sim.folkData9");
            }

            if (this.levelFun == 10) {
                //玩得很开心
                this.funStatus = I18n.format("container.sim.folkData10");
            } else if (this.levelFun > 7) {
                //自得其乐
                this.funStatus = I18n.format("container.sim.folkData11");
            } else if (this.levelFun > 4) {
                //无聊的
                this.funStatus = I18n.format("container.sim.folkData12");
            } else {
                //焦虑不安
                this.funStatus = I18n.format("container.sim.folkData13");
            }

            if (this.levelSocial == 10) {
                //伟人
                this.socialStatus = I18n.format("container.sim.FolkData.Great_Banter");
            } else if (this.levelSocial > 7) {
                //名人
                this.socialStatus = I18n.format("container.sim.FolkData.Socially_Fulfilled");
            } else if (this.levelSocial > 4) {
                //孤独
                this.socialStatus = I18n.format("container.sim.FolkData.Lonely");
            } else if (this.levelSocial > 2) {
                //非常孤独
                this.socialStatus = I18n.format("container.sim.FolkData.Very_Lonely");
            } else {
                //发疯
                this.socialStatus = I18n.format("container.sim.FolkData.Going_Insane");
            }
            //生存环境
            if (this.levelEnvironment == 10) {
                this.environmentStatus = I18n.format("container.sim.FolkData.Beautiful_Surroundings");
            } else if (this.levelEnvironment > 7) {
                this.environmentStatus = I18n.format("container.sim.FolkData.Nice_Surroundings");
            } else if (this.levelEnvironment > 4) {
                this.environmentStatus = I18n.format("container.sim.FolkData.Poor_Surroundings");
            } else {
                this.environmentStatus = I18n.format("container.sim.FolkData.Horrific_Surroundings");
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("updateStatusLines出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 已生成
     * 如果实体当前已派生，则返回（使用entity==null或entity.IsRead为true）
     *
     * @return
     */
    public boolean isSpawned() {
        boolean falg = true;
        try {
            if (this.theEntity == null) {
                FolkData folkData = getFolkByName(this.name);
                if (folkData != null) {
                    this.theEntity = folkData.theEntity;
                }
            }
            if (this.theEntity == null) {
                falg = false;
                return falg;
            } else {
                //return theEntity.isEntityAlive();
//                this.theEntity.isDead = false;
                return !this.theEntity.isDead;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("isSpawned出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return falg;
    }

    /**
     * 是否是夜猫子
     *
     * @return
     */
    public boolean isNightOwl() {
        boolean falg = false;
        try {
            if ((this.trait1.equals(I18n.format("container.sim.traits13")) || trait2.equals(I18n.format("container.sim.traits13")) || trait3.equals(I18n.format("container.sim.traits13")) || trait4.equals(I18n.format("container.sim.traits13")))&&this.vocation!=null&&this.vocation==Vocation.BUILDER) {
                falg = true;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("isNightOwl出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return falg;
    }

    /**
     * 获取到玩家的距离
     * 计算此玩家与玩家之间的当前距离
     *
     * @return
     */
    public int getDistanceToPlayer() {
        int i = 0;
        try {
            EntityPlayer p = getClosestPlayer(this.location);
            if (p == null) {
                i = 9999;
                return i;
            }
            V3 pv = new V3(p.posX, p.posY, p.posZ, this.location.theDimension);
            i = this.location.getDistanceTo(pv);
            return i;

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getDistanceToPlayer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return i;
    }

    /**
     * 获取靠近玩家的位置
     * 找到一个靠近玩家的好地方（大约30个街区外），可以安全地放下一个人
     *
     * @return
     */
    public V3 getLocationCloseToPlayer() {
        V3 ret = null;
        try {
            EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
            try {
                ret = new V3(p.posX, p.posY, p.posZ, p.dimension);
            } catch (Exception e) {
                StackTraceElement element = e.getStackTrace()[0];
                ModSimReloaded.log.warn("getLocationCloseToPlayer: 玩家为空，返回空V3" + e.getMessage() + "行数：" + element.getLineNumber());
                return new V3(p.posX, p.posY, p.posZ, 0);
            }
            boolean found = false;
            Block bid = null;

            for (int go = 30; go > 1; go--) {
                ret = new V3(p.posX, p.posY, p.posZ + (double) go, p.dimension);

                while (!found) {
                    BlockPos blockPos = new BlockPos(ret.xCoord, ret.yCoord, ret.zCoord);
                    bid = p.worldObj.getBlockState(blockPos).getBlock();
                    if (p.worldObj.canSeeSky(blockPos) || p.dimension != 0) {
                        if (bid == Blocks.air && bid != Blocks.leaves && bid != null) {
                            found = true;
                        }
                    }

                    ret = new V3(ret.xCoord, ret.yCoord + 1, ret.zCoord);
                    if (ret.yCoord > 200) {
                        break;
                    }
                }

                if (found) {
                    break;
                }
            }
            if (!found) {
                return new V3(p.posX, p.posY, p.posZ, 0);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getLocationCloseToPlayer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

        return ret;
    }

    /**
     * 获取最近的玩家
     * 返回距离某个位置最近的玩家-确保也设置了维度
     *
     * @param location
     * @return
     */
    public static EntityPlayer getClosestPlayer(V3 location) {
        try {
            World world = MinecraftServer.getServer().worldServerForDimension(location.theDimension);
            EntityPlayer ret = world.getClosestPlayer(location.xCoord, location.yCoord, location.zCoord, 100);
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成名称
     * 随机姓名生成器-确保这个世界上不存在姓名
     *
     * @param gender
     * @param firstNameOnly
     * @param lastNameOptional
     * @return
     */
    public static String generateName(int gender, boolean firstNameOnly, String lastNameOptional) {
        Random randomGenerator = new Random();
        String firstName = "";
        String lastName = "";
        FolkData test = null;
        String name = "";
        try {
            int i;
            for (int go = 0; go < 200; ++go) {

                if (gender == 0) {
                    i = randomGenerator.nextInt(ConfigLoader.configMaleNames.length);
                    firstName = ConfigLoader.configMaleNames[i].trim();
                } else {
                    i = randomGenerator.nextInt(ConfigLoader.configFemaleNames.length);
                    firstName = ConfigLoader.configFemaleNames[i].trim();
                }

                i = randomGenerator.nextInt(ConfigLoader.configSurnames.length);
                if (lastName.contentEquals("")) {
                    lastName = ConfigLoader.configSurnames[i].trim();
                } else {
                    lastName = lastNameOptional;
                }

                String lang = FMLCommonHandler.instance().getCurrentLanguage();

                if ("en_US".equals(lang)) {
                    name = firstName + " " + lastName;
                } else {
                    name = lastName + firstName;
                }
                test = getFolkByName(name);
                if (test == null) {
                    break;
                } else {
                    name = name + " II";
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("generateName出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return name;
    }

    /**
     * 解雇
     */
    public void selfFire() {
        //ModSimReloaded.log.info("FolkData: selfFire() " + name);
        try {
            this.isWorking = false;
                if (this.villagerInventory != null&&this.villagerInventory.getSizeInventory() > 0) {
                    int count = 0;
                    for (int inv = 0; inv < this.villagerInventory.getSizeInventory(); inv++) {
                        ItemStack is = this.villagerInventory.getStackInSlot(inv);
                        if (is != null) {
                            if (this.theEntity != null) {
                                this.theEntity.entityDropItem(is, (float) is.stackSize);
                            } else {
                                getClosestPlayer(this.location).entityDropItem(is, (float) is.stackSize);
                            }

                            count += is.stackSize;
                        }
                    }

                    if (count > 0) {
                        //从他们的库存中取走了
                        String has_dropped = I18n.format("container.sim.folk_data_has_dropped");
                        //件物品
                        String inventory = I18n.format("container.sim.folk_data_inventory");

                        ModSimReloaded.sendChat(this.name + has_dropped + count + inventory);
                    }
                }
            if (this.villagerInventory != null){
                this.villagerInventory.clear();
            }else{
                this.villagerInventory= new InventoryBasic("Items", false, 64);
            }

                if (this.theEntity != null) {
                    this.theEntity.swingProgress = 0.0F;
                    this.theEntity.getNavigator().clearPathEntity();
                }

            this.employedAt = null;
                if (this.vocation == Vocation.BUILDER) {
                    this.theBuilding = null;
                }

            this.vocation = null;
            this.theirJob = null;
                this.action= FolkAction.WANDER;
            this.statusText = I18n.format("container.sim.folk_data.Wandering");
            this.stayPut = false;
                saveThisFolk();
        } catch (
                Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("selfFire出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 告诉人们去一个地方，方法将决定如何让他们去那里如果你给方法传递NULL，到达可以是NULL
     *
     * @param whereTo
     * @param methodOfTravel
     */
    public void gotoXYZ(V3 whereTo, GotoMethod methodOfTravel) {
        try {
            /*if(theEntity.worldObj.isRemote == true);
    	{
    		SimukraftReloaded.log.info("Is Remote");
    	}
    	if(theEntity.worldObj.isRemote == false)
    	{
    		SimukraftReloaded.log.info("Is NOT Remote");
    	}*/

            if (whereTo == null) {
                return;
            }
            this.stayPut = false;
            this.destination = whereTo.clone();
            if (this.destination == null) {
                return;
            }
            this.destination.doNotTimeout = false;
            int dist = location.getDistanceTo(whereTo);
            if (!isSpawned()) {
                methodOfTravel = null;
            }
            if (methodOfTravel == null) {
                V3 playpos = null;
                EntityPlayer pl = getClosestPlayer(this.location);
                if (pl == null || (this.location.theDimension != this.destination.theDimension)) {
                    dist = 999;
                } else {
                    playpos = new V3(pl.posX, pl.posY, pl.posZ, pl.dimension);
                }
                //小于40则走过去
                if (dist < 40) {
                    this.gotoMethod = GotoMethod.WALK;
                }
                if (!isSpawned() || dist >= 40) {
                    this.gotoMethod = GotoMethod.BEAM;
                }
                //如果玩家处于不同维度或超出范围，则为空
                if (playpos != null) {
                    if (location.getDistanceTo(playpos) >= 100 && whereTo.getDistanceTo(playpos) >= 100) {
                        this.gotoMethod = GotoMethod.SHIFT;
                    }
                    try {
                        if (this.location.theDimension != Minecraft.getMinecraft().thePlayer.dimension && this.destination.theDimension != Minecraft.getMinecraft().thePlayer.dimension) {
                            this.gotoMethod = GotoMethod.SHIFT;
                        }
                    } catch (Exception e) {
                        this.gotoMethod = GotoMethod.SHIFT;
                    }
                }

                if (methodOfTravel == null) {
                    if (this.gotoMethod == null) {
                        this.gotoMethod = GotoMethod.BEAM;
                    }
                }
            } else {
                this.gotoMethod = methodOfTravel;
            }

            if (this.destination == null) {
                return;
            }

            if (this.gotoMethod == GotoMethod.SHIFT) {
                //ModSimReloaded.log.info(name + ":选择SHIFT去x:" + whereTo.xCoord + ",y:" + whereTo.yCoord + ",z:" + whereTo.zCoord);
                this.destination = new V3(this.destination.xCoord, this.destination.yCoord+1, this.destination.zCoord);
                if (this.theEntity != null) {
                    if (this.destination != null) {
                        //theEntity.setPosition(destination.xCoord,destination.yCoord,destination.zCoord);
                        this.theEntity.setLocationAndAngles(this.destination.xCoord, this.destination.yCoord+1, this.destination.zCoord, 0.0F, 0.0F);
                    }
                    //如果维度不一样传送到维度
                    //修改为不管维度一样不一样都要传送
                    if (this.location.theDimension != this.destination.theDimension) {
                        this.theEntity.travelToDimension(this.destination.theDimension);
                        this.theEntity.dimension = this.destination.theDimension;
                        this.location.theDimension = this.destination.theDimension;
                    }
                }
                this.location = this.destination.clone();
                this.destination = null;
            } else if (this.gotoMethod == GotoMethod.BEAM) {
                //ModSimReloaded.log.info(name + ":选择传送去x:" + whereTo.xCoord + ",y:" + whereTo.yCoord + ",z:" + whereTo.zCoord);
                this.timeStartedGotoing = System.currentTimeMillis();
                beamMeTo(whereTo);
            } else if (this.gotoMethod == GotoMethod.WALK) {
                //ModSimReloaded.log.info(name + ":选择步行去x:" + whereTo.xCoord + ",y:" + whereTo.yCoord + ",z:" + whereTo.zCoord);
                this.stayPut = false;
                this.timeStartedGotoing = System.currentTimeMillis();
                if (this.theEntity != null) {
                    this.theEntity.gotPath = false;
                    //已经设置了它们的目的地，所以这就是它所需要的一切。只有当它们重生并在步行距离内时，它才会到达这里，所以实体的MoveEntity（）方法现在接管。

                    /*theEntity.gotPath = theEntity.getNavigator().tryMoveToXYZ(destination.xCoord, destination.yCoord, destination.zCoord, 0.3D);
                    if (theEntity.gotPath) {
                        PathEntity path = theEntity.getNavigator().getPathToXYZ(destination.xCoord, destination.yCoord, destination.zCoord);
                        if (path != null) {
                            ModSimReloaded.log.info("实体人:[ " + name + " ]即走过去☞x:" + destination.xCoord + ",y:" + destination.yCoord + ",z:" + destination.zCoord);
                            theEntity.getNavigator().setPath(path, 0.3D);
                            theEntity.gotPath = true;
                        }
                    }*/
//                                theEntity.moveEntity(whereTo.xCoord, whereTo.yCoord, whereTo.zCoord);
                }
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("gotoXYZ出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 将NPC传递到指定位置
     *
     * @param whereToIn
     */
    public void beamMeTo(V3 whereToIn) {
        try {
            this.stayPut = true;
            //仅当它们当前已繁殖时才执行此操作
            updateLocationFromEntity();
            if (this.beamingTo != null) {
                //ModSimReloaded.log.warn("FolkData:beamMeTo() 已经传送 " + name);
                return;
            }
            if (whereToIn == null) {
                //ModSimReloaded.log.warn("FolkData: beamMeTo() 传送地址为空，取消传送 ");
                return;
            }

            this.timeStartedGotoing = System.currentTimeMillis();
            V3 whereTo = whereToIn.clone();
            World destWorld = MinecraftServer.getServer().worldServerForDimension(whereTo.theDimension);
            for (int i = 0; i < 200; i++) {
                Block id1 = destWorld.getBlockState(new BlockPos(whereTo.xCoord, whereTo.yCoord, whereTo.zCoord)).getBlock();
                Block id2 = destWorld.getBlockState(new BlockPos(whereTo.xCoord, whereTo.yCoord + 1, whereTo.zCoord)).getBlock();
                if (id1 == Blocks.air && id2 == Blocks.air) {
                    break;
                }
                whereTo = new V3(whereTo.xCoord, whereTo.yCoord + 1, whereTo.zCoord);
                //whereTo.yCoord = whereTo.yCoord + 1;
            }
            whereTo = new V3(whereTo.xCoord, whereTo.yCoord - 1, whereTo.zCoord);


            this.destination = whereTo.clone();
            //ModSimReloaded.log.info("FolkData: BeamMeTo() for " + name + " to " + whereTo.toString() + " Dim:" + whereTo.theDimension);
            this.stayPut = true;
            if (isSpawned()) {
                if (this.theEntity != null) {
                    this.theEntity.getNavigator().clearPathEntity();
                }
            }

            if (ModSim.proxy.getClientWorld() != null) {
                if (!ModSim.proxy.getClientWorld().isRemote) {
                    ModSim.proxy.getClientWorld().playSound(this.location.xCoord, this.location.yCoord, this.location.zCoord, ModSim.MODID + ":beamdown", 1, 1,true);
                    ModSim.proxy.getClientWorld().playSound(whereTo.xCoord, whereTo.yCoord, whereTo.zCoord, ModSim.MODID + ":beamdown", 1f, 1f,true);
                }
            }
            respawnEntity(MinecraftServer.getServer().worldServerForDimension(location.theDimension));
            this.beamingTo = whereTo.clone();
            this.location = beamingTo.clone();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("将民俗传递到指定位置出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            return;
        }
    }

    /**
     * 在发射过程中重复调用
     */
    private void doBeaming() {
        try {
            if (System.currentTimeMillis() - this.timeStartedGotoing > 4000L || this.beamingTo == null) {
                //通过传送到达
                if (this.theEntity != null) {
                    //设置实体的位置并更新“最后”变量
                    //theEntity.setPosition(beamingTo.xCoord, beamingTo.yCoord, beamingTo.zCoord);
                    this.theEntity.setPositionAndUpdate(this.beamingTo.xCoord, this.beamingTo.yCoord + 1, this.beamingTo.zCoord);
                    if (this.theEntity.dimension != this.beamingTo.theDimension) {
                        this.theEntity.travelToDimension(this.beamingTo.theDimension);
                        this.theEntity.dimension = this.beamingTo.theDimension;
                        this.location.theDimension = this.beamingTo.theDimension;
                    }
                }

                //ModSimReloaded.log.info("FolkData: doBeaming() 完成 " + name + " to " + beamingTo.toString() + " (dim " + beamingTo.theDimension + ")");
                this.location = this.beamingTo.clone();
                respawnEntity(MinecraftServer.getServer().worldServerForDimension(this.location.theDimension));
                this.destination = null;
                this.beamingTo = null;
                saveThisFolk();
                return;
            }
            Random random = new Random();
            Double d4 = ((double) random.nextFloat() - 2) * 2;
            this.stayPut = true;
            if (!MinecraftServer.getServer().isDedicatedServer()) {
                //仅需要粒子的客户端世界
                World theWorld = Minecraft.getMinecraft().theWorld;
                if (theWorld != null) {
                    for (int p = 0; p < 10; p++) {
                        //仅需要粒子的客户端世界
                        if (!ConfigLoader.configDisableBeamEffect) {
                            theWorld.spawnParticle(EnumParticleTypes.PORTAL, this.location.xCoord + random.nextDouble() - 0.5D, this.location.yCoord - 1.0, this.location.zCoord + random.nextDouble() - 0.5D, 0, -d4, 0);
                            theWorld.spawnParticle(EnumParticleTypes.PORTAL, this.beamingTo.xCoord + random.nextDouble() - 0.5D, this.beamingTo.yCoord - 1.0, this.beamingTo.zCoord + random.nextDouble() - 0.5D, 0, -d4, 0);
                        }
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("doBeaming出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            this.destination = null;
            this.beamingTo = null;
        }
    }

    /**
     * 在加载所有预先存在的peops数据时，FolkData类将决定是否应该将它们生成到world中
     */
    public static void loadAndSpawnFolks() {
        ThreadPoolExecutor threadPoolExecutor = ModSimReloaded.threadPoolExecutor;
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ModSimReloaded.log.info("***********************开始加载npc人物***************");
                    ModSimReloaded.theFolks.clear();
                    File folksFolder = new File(ModSimReloaded.getSavesDataFolder() + "folks" + File.separator);
                    if (!folksFolder.exists()) {
                        folksFolder.mkdirs();
                    }
                    for (File f : folksFolder.listFiles()) {
                        if (f.getName().endsWith(".sk2")) {
                            List<String> strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                            FolkData folkd = new FolkData();
                            for (String line : strings) {
                                if (line.contains("|")) {
                                    int m1 = line.indexOf("|");
                                    String name = line.substring(0, m1);
                                    String value = line.substring(m1 + 1);

                                    try {
                                        if (name.contentEquals("employedat")) {
                                            if (!value.contentEquals("null")) {
                                                String[] v = value.split(",");
                                                double x = Double.parseDouble(v[0]);
                                                double y = Double.parseDouble(v[1]);
                                                double z = Double.parseDouble(v[2]);
                                                folkd.employedAt = new V3(x, y, z);
                                            }
                                        } else if (name.contentEquals("vocation")) {
                                            if (!value.contentEquals("null")) {
                                                folkd.vocation = Vocation.valueOf(value);
                                            }
                                        } else if (name.contentEquals("name")) {
                                            folkd.name = value;
                                        } else if (name.contentEquals("age")) {
                                            folkd.age = Integer.parseInt(value);
                                        } else if (name.contentEquals("gender")) {
                                            folkd.gender = Integer.parseInt(value);
                                        } else if (name.contentEquals("skin")) {
                                            folkd.skinnumber = Integer.parseInt(value);
                                        } else if (name.contentEquals("levelfood")) {
                                            folkd.levelFood = Integer.parseInt(value);
                                        } else if (name.contentEquals("trait1")) {
                                            folkd.trait1 = value;
                                        } else if (name.contentEquals("trait2")) {
                                            folkd.trait2 = value;
                                        } else if (name.contentEquals("trait3")) {
                                            folkd.trait3 = value;
                                        } else if (name.contentEquals("trait4")) {
                                            folkd.trait4 = value;
                                        } else if (name.contentEquals("levelfun")) {
                                            folkd.levelFun = Integer.parseInt(value);
                                        } else if (name.contentEquals("levelbuilder")) {
                                            folkd.levelBuilder = Float.parseFloat(value);
                                        } else if (name.contentEquals("levelminer")) {
                                            folkd.levelMiner = Float.parseFloat(value);
                                        } else if (name.contentEquals("levelsoldier")) {
                                            folkd.levelSoldier = Float.parseFloat(value);
                                        } else if (name.contentEquals("stayput")) {
                                            folkd.stayPut = Boolean.parseBoolean(value);
                                        } else if (name.contentEquals("location")) {
                                            String[] v = value.split(",");
                                            double x = Double.parseDouble(v[0]);
                                            double y = Double.parseDouble(v[1]);
                                            double z = Double.parseDouble(v[2]);
                                            folkd.location = new V3(x, y, z);
                                        } else if (name.contentEquals("pregnancy")) {
                                            folkd.pregnancyStage = Float.parseFloat(value);
                                        } else if (name.contentEquals("building")) {
                                            if (!value.contentEquals("null")) {
                                                int m2 = value.indexOf("|");
                                                int m3 = value.indexOf("||");
                                                String fn = value.substring(0, m2);
                                                String type = value.substring(m2 + 1, m3);
                                                String dir = value.substring(m3 + 2);
                                                folkd.theBuilding = Building.getBuildingForFolk(fn, type);
                                                if (folkd.theBuilding != null) {
                                                    folkd.theBuilding.buildDirection = dir;
                                                }
                                            }
                                        } else if (name.contentEquals("terraformtype")) {
                                            if (!value.contentEquals("null")) {
                                                folkd.terraformerType = TerraformerType.valueOf(value);
                                            }
                                        } else if (name.contentEquals("terraformradius")) {
                                            folkd.terraformerRadius = Integer.parseInt(value);
                                        }
                                    } catch (Exception e) {
                                        StackTraceElement element = e.getStackTrace()[0];
                                        ModSimReloaded.log.error("加载NPC数据出错：" + e.getMessage() + "行数：" + element.getLineNumber());
                                    }
                                }
                            }

                            if (folkd != null) {
                                ModSimReloaded.log.info("FolkData: loadAndSpawnFolks() 加载 " + folkd.name + " 使用新的文件系统");
                                folkd.hasLoaded();
                            }
                        } else if (f.getName().endsWith(".suk")) {
                            FolkData folkd = (FolkData) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
                            if (folkd != null) {
                                folkd.hasLoaded();
                            } else {
                                f.delete();
                            }
                        }
                    }
                    ModSimReloaded.log.info("***********************加载npc人物完成***************");
                } catch (Exception e) {
                    StackTraceElement element = e.getStackTrace()[0];
                    ModSimReloaded.log.error("FolkData-loadAndSpawnFolks出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
                }
            }
        },"loadAndSpawnFolks_sim");
        //threadPoolExecutor.shutdown();
       /* Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }, "loadAndSpawnFolks_sim");
        thread.start();*/

    }

    /**
     * 仅保存此Folk（folkData），以便可以在下一个会话中加载它们
     */
    public void saveThisFolk() {
        try {
            String folder = ModSimReloaded.getSavesDataFolder() + "folks" + File.separator;
            File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }

            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.SERVER) {
                List<String> strings = new CopyOnWriteArrayList();
                if (this.employedAt == null) {
                    strings.add("employedat|null");
                } else {
                    strings.add("employedat|" + this.employedAt.toString());
                }

                if (this.vocation == null) {
                    strings.add("vocation|null");
                } else {
                    strings.add("vocation|" + this.vocation.name());
                }

                strings.add("name|" + this.name);
                strings.add("age|" + this.age);
                strings.add("gender|" + this.gender);
                strings.add("skin|" + this.skinnumber);
                strings.add("race|" + this.folkRaceName);
                strings.add("trait1|" + this.trait1);
                strings.add("trait2|" +this.trait2);
                strings.add("trait3|" + this.trait3);
                strings.add("trait4|" + this.trait4);
                strings.add("levelfood|" + this.levelFood);
                //strings.add("levelfun|" + levelFun);
                strings.add("levelbuilder|" + this.levelBuilder);
                strings.add("levelminer|" + this.levelMiner);
                strings.add("levelsoldier|" + this.levelSoldier);
                strings.add("stayput|" + this.stayPut.toString());
                strings.add("location|" + this.location.toString());
                strings.add("pregnancy|" + this.pregnancyStage);
                if (this.theBuilding == null) {
                    strings.add("building|null");
                } else {
                    strings.add("building|" + this.theBuilding.displayName + ".txt|" + this.theBuilding.type + "||" + this.theBuilding.buildDirection);
                }

                if (this.terraformerType == null) {
                    strings.add("terraformtype|null");
                } else {
                    strings.add("terraformtype|" + this.terraformerType.name());
                }

                strings.add("terraformradius|" + this.terraformerRadius);
                if (!name.contentEquals("")) {
                    ModSimReloaded.saveSK2(folder + this.name + ".sk2", strings);
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("saveThisFolk出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

    /**
     * 获取该居民居住的建筑/房屋，如果无家可归，则为空
     *
     * @return
     */
    public Building getHome() {
        Building home = null;
        try {
            for (int b = 0; b < ModSimReloaded.theBuildings.size(); b++) {
                home = ModSimReloaded.theBuildings.get(b);
                if("residential".equals(home.type)){
                    for (int t = 0; t < home.tenants.size(); ++t) {
                        String tennant = home.tenants.get(t);
                        if (tennant.contentEquals(name)) {
                            return home;
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getHome获取该居民居住的建筑/房屋出错了:"+element.getLineNumber());
        }
        return null;
    }

    /**
     * 生成一个新的NPC
     * 创建并可选生成一个全新的folk-每分钟都会被调用，但可能不会每分钟都生成
     *
     * @param world
     */
    public static void generateNewFolk(World world) {
        try {
            List<FolkData> fds = getFolkHomeless();
            if (fds.size() == 0 && ModSimReloaded.theFolks.size() < ConfigLoader.configPopulationLimit) {
                new FolkData(world);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("生成一个新的NPC出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public static void forceGenerateNewFolk(World world) {
        try {
            FolkData folk = new FolkData(world);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("forceGenerateNewFolk出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public static void forceGenerateNewFolk(World world, String nme) {
        try {
            FolkData folk = new FolkData(world, nme);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("forceGenerateNewFolk出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 根据姓名返回一个人
     *
     * @param name
     * @return
     */
    public static FolkData getFolkByName(String name) {
        try {
            for (int x = 0; x < ModSimReloaded.theFolks.size(); x++) {
                FolkData f = ModSimReloaded.theFolks.get(x);
                if (f.name.contentEquals(name)) {
                    //ModSimReloaded.log.info("依据姓名找到NPC:"+name);
                    return f;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getFolkByName出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return null;
    }

    /**
     * 根据其位置返回文件夹数据
     *
     * @param loc
     * @return
     */
    public static FolkData getFolkByLocation(V3 loc) {
        FolkData f = null;
        try {
            for (int x = 0; x < ModSimReloaded.theFolks.size(); x++) {
                f = ModSimReloaded.theFolks.get(x);
                if (f.location.isSameCoordsAs(loc, true, false)) {
                    return f;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getFolkByLocation出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return null;
    }

    /**
     * 找一个在特定xyz工作的人
     *
     * @param employedAt
     * @return
     */
    public static FolkData getFolkByEmployedAt(V3 employedAt) {
        FolkData f = null;
        try {
            for (int x = 0; x < ModSimReloaded.theFolks.size(); x++) {
                f = ModSimReloaded.theFolks.get(x);
                if (f.employedAt != null && f.employedAt.isSameCoordsAs(employedAt, true, false)) {
                    return f;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getFolkByEmployedAt出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return null;
    }

    /**
     * getFolkByEmployedAt的复数形式（返回找到的第一个）
     * 受雇佣的地方
     *
     * @param v
     * @return
     */
    public static List<FolkData> getFolksByEmployedAt(V3 v) {
        List<FolkData> ret = new CopyOnWriteArrayList();
        try {
            for (int x = 0; x < ModSimReloaded.theFolks.size(); x++) {
                FolkData f = (FolkData) ModSimReloaded.theFolks.get(x);
                if (f.employedAt != null && f.employedAt.isSameCoordsAs(v, true, false)) {
                    ret.add(f);
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getFolksByEmployedAt出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


        return ret;
    }

    /**
     * 如果showEmployeed==true，则显示所有就业人员false将返回失业人员
     *
     * @param showEmployed
     * @return
     */
    public static List getFolkUnemployed(boolean showEmployed) {
        List f = new CopyOnWriteArrayList();
        try {
            for (int x = 0; x < ModSimReloaded.theFolks.size(); x++) {
                FolkData folk = ModSimReloaded.theFolks.get(x);
                if (showEmployed) {
                    if (folk.employedAt != null) {
                        f.add(folk);
                    }
                } else {
                    if (folk.employedAt == null && folk.age > 17 && folk.pregnancyStage == 0.0F) {
                        f.add(folk);
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("CopyOnWriteArrayList出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return f;
    }

    /**
     * 返回无家可归者的arraylist
     *
     * @return
     */
    public static List getFolkHomeless() {
        List f = new CopyOnWriteArrayList();
        try {
            for (int x = 0; x < ModSimReloaded.theFolks.size(); x++) {
                FolkData folk = ModSimReloaded.theFolks.get(x);
                if (folk.getHome() == null) {
                    f.add(folk);
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getFolkHomeless出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return f;
    }

    /**
     * 用于生成，以便实体可以获取对数据的引用，如果找不到，则返回null
     *
     * @param id
     * @return
     */
    public static FolkData getFolkDataByEntityId(int id) {
        try {
            for (int i = 0; i < ModSimReloaded.theFolks.size(); i++) {
                FolkData fd = ModSimReloaded.theFolks.get(i);
                if (fd.theEntity != null && fd.theEntity.getEntityId() == id) {
                    return fd;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getFolkDataByEntityId出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return null;
    }

    /**
     * 当人们死于某事时，从实体中调用
     *
     * @param d
     */
    public void eventDied(DamageSource d) {
        try {
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            ModSimReloaded.log.info("在FolkData发生的事件中死亡 " + side.toString() + " side");
            String oldJob = "";
            if (this.vocation != null) {
                oldJob = " (" + this.vocation.toString() + ")";
            }

            this.employedAt = null;
            this.vocation = null;
            String deathBy = "";
            if (d == DamageSource.cactus) {
                //(被仙人掌扎死... 最糟糕的死法之一.)
                deathBy = I18n.format("container.sim.folk_data_death_cactus");
            }

            if (d == DamageSource.drown) {
                //(淹死)
                deathBy = I18n.format("container.sim.folk_data_death_drowned");
            }

            if (d == DamageSource.generic) {
                //(自然老死,寿终正寝)
                deathBy = I18n.format("container.sim.folk_data_death_old");
            }

            if (d == DamageSource.inFire) {
                //(自燃了)
                deathBy = I18n.format("container.sim.folk_data_death_combustion");
            }

            if (d == DamageSource.lava) {
                //(试图在岩浆中游泳)
                deathBy = I18n.format("container.sim.folk_data_death_lava");
            }
            if (d == DamageSource.onFire) {
                //(烧死)
                deathBy = I18n.format("container.sim.folk_data_death_Burned");
            }

            if (d == DamageSource.outOfWorld) {
                //(从世界上消失了！)
                deathBy = I18n.format("container.sim.folk_data_death_Fell");
            }

            if (d == DamageSource.starve) {
                //(饥饿,请建造农场,面包店和食品杂货店！)
                deathBy = I18n.format("container.sim.folk_data_death_starvation");
            }

            if (d == DamageSource.fall) {
                //(是从悬崖上掉下来的还是被推下去的？！)
                deathBy = I18n.format("container.sim.folk_data_death_cliff");
            }

            if (d == DamageSource.inWall) {
                //(活埋在砾石/沙子下)
                deathBy = I18n.format("container.sim.folk_data_death_under");
            }

            if (deathBy.contentEquals("")) {
                //Random r = new Random();
                //int i = r.nextInt(6);
                //if (i == 0) {
                //    //(在洗澡时触电身亡)
                //    deathBy = I18n.format("container.sim.folk_data_death_by_Electrocuted");
                //} else if (i == 1) {
                //    //(在楼梯上被溜冰鞋绊倒了)
                //    deathBy = I18n.format("container.sim.folk_data_death_by_Tripped");
                //} else if (i == 2) {
                //    //(被牛践踏)
                //    deathBy = I18n.format("container.sim.folk_data_death_by_Trampled");
                //} else if (i == 3) {
                //    //(被地雷车碾过)
                //    deathBy = I18n.format("container.sim.folk_data_death_by_Ran");
                //} else if (i == 4) {
                //    //(在香蕉皮上滑倒)
                //    deathBy = I18n.format("container.sim.folk_data_death_by_Slipped");
                //} else if (i == 5) {
                //(被砍死)
                deathBy = I18n.format("container.sim.folk_data_death_by_killed");
                //}
            }

            String only = "";
            if (this.age < 80) {
                //他们只有 岁。
                only = I18n.format("container.sim.folk_data_death_by_They") + this.age + I18n.format("container.sim.folk_data_death_by_years");
            } else {
                //他们当时 岁,哦,他们的寿命很长！
                only = I18n.format("container.sim.folk_data_death_by_were") + this.age + I18n.format("container.sim.folk_data_death_by_life");
            }
            //刚刚死了！
            ModSimReloaded.sendChat(this.name + oldJob + I18n.format("container.sim.folk_data_death_by_died") + deathBy + only);
            this.action= FolkAction.WANDER;
            int i = 0;
            //找到阵法中的人
            for (int q = 0; q < ModSimReloaded.theFolks.size(); q++) {
                FolkData fo = ModSimReloaded.theFolks.get(q);
                if (fo.name.contentEquals(this.name)) {
                    i = q;
                    break;
                }
            }

            evictThem();
            //如果是合作伙伴，则取消他们的合作伙伴关系
            try {
                //删除其所有关系
                for (int q = 0; q < ModSimReloaded.theRelationships.size(); q++) {
                    try {
                        Relationship rel = ModSimReloaded.theRelationships.get(q);
                        if (rel.folk1.name.contentEquals(name) || rel.folk2.name.contentEquals(this.name)) {
                            String fn = rel.folk1.name.replaceAll(" ", "") + rel.folk2.name.replaceAll(" ", "");
                            File f = new File(ModSimReloaded.getSavesDataFolder() + "Relationships" + File.separator + fn + ".sk2");
                            f.delete();
                            ModSimReloaded.theRelationships.remove(q);
                        }
                    } catch (Exception e) {
                        //var11.printStackTrace();
                        StackTraceElement element = e.getStackTrace()[0];
                        ModSimReloaded.log.error("删除npc 所有关系失败：" + e.getMessage() + "行数：" + element.getLineNumber());
                    }
                }

                File f = new File(ModSimReloaded.getSavesDataFolder() + "folks" + File.separator + this.name + ".sk2");
                f.delete();
                if (i >= 0 && i < ModSimReloaded.theFolks.size()) {
                    ModSimReloaded.theFolks.remove(i);
                }
            } catch (Exception e) {
                StackTraceElement element = e.getStackTrace()[0];
                ModSimReloaded.log.error("FolkData: eventDied() " + e.getMessage() + "行数：" + element.getLineNumber());
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("eventDied出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 当他们去世时，也当他们年满18岁从父母家被驱逐时，都会打电话给他们
     */
    public void evictThem() {
        try {
            //将他们从家中驱逐出去：-）
            if (getHome() != null) {
                for (int b = 0; b < ModSimReloaded.theBuildings.size(); b++) {
                    Building building = ModSimReloaded.theBuildings.get(b);
                    if (building != null && getHome() != null) {
                        if (building.primaryXYZ.isSameCoordsAs(getHome().primaryXYZ, true, false)) {
                            building.removeTennant(name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("evictThem出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 在加载他们以及通过guiemploypeople（）雇用他们时调用此函数。如果他们没有工作，则传入null
     *
     * @param vocation
     */
    public void setTheirJob(Vocation vocation) {
        try {
            if (vocation != null) {
                this.vocation = vocation;
                //建筑师
                if (vocation == Vocation.BUILDER) {
                    this.theirJob = new JobBuilder(this);
                    //面包师
                } else if (vocation == Vocation.BAKER) {
                    this.theirJob = new JobBaker(this);
                    //屠夫
                } else if (vocation == Vocation.BUTCHER) {
                    this.theirJob = new JobButcher(this);
                    //牧牛人
                } else if (vocation == Vocation.CATTLEFARMER) {
                    this.theirJob = new JobLivestockFarmer(this);
                    //鸡农
                } else if (vocation == Vocation.CHICKENFARMER) {
                    this.theirJob = new JobLivestockFarmer(this);
                    //快递员
                } else if (vocation == Vocation.COURIER) {
                    this.theirJob = new JobCourier(this);
                    //农作物种植者
                } else if (vocation == Vocation.CROPFARMER) {
                    this.theirJob = new JobCropFarmer(this);
                    //玻璃制造商
                } else if (vocation == Vocation.GLASSMAKER) {
                    this.theirJob = new JobGlassMaker(this);
                    //砖匠
                } else if (vocation == Vocation.BRICKMAKER) {
                    this.theirJob = new JobBrickMaker(this);
                    //食物杂货商
                } else if (vocation == Vocation.GROCER) {
                    this.theirJob = new JobGrocer(this);
                    //伐木工人
                } else if (vocation == Vocation.LUMBERJACK) {
                    this.theirJob = new JobLumberjack(this);
                    //商人
                } else if (vocation == Vocation.MERCHANT) {
                    this.theirJob = new JobBuildersMerchant(this);
                    //矿工
                } else if (vocation == Vocation.MINER) {
                    this.theirJob = new JobMiner(this);
                    //养猪户
                } else if (vocation == Vocation.PIGFARMER) {
                    this.theirJob = new JobLivestockFarmer(this);
                    //牧羊人
                } else if (vocation == Vocation.SHEPHERD) {
                    this.theirJob = new JobShepherd(this);
                    //士兵
                } else if (vocation == Vocation.SOLDIER) {
                    this.theirJob = new JobSoldier(this);
                    //地形师
                } else if (vocation == Vocation.TERRAFORMER) {
                    this.theirJob = new JobTerraformer(this);
                    //渔夫
                } else if (vocation == Vocation.FISHERMAN) {
                    this.theirJob = new JobFisherman(this);
                    //路径生成器
                } else if (vocation != Vocation.PATHBUILDER) {
                    //奶农
                } else if (vocation == Vocation.DAIRYFARMER) {
                    this.theirJob = new JobDairyFarmer(this);
                    //奶酪制造商
                } else if (vocation == Vocation.CHEESEMAKER) {
                    this.theirJob = new JobCheesemaker(this);
                    //汉堡经理
                } else if (vocation == Vocation.BURGERSMANAGER) {
                    this.theirJob = new JobBurgersManager(this);
                    //汉堡厨师
                } else if (vocation == Vocation.BURGERSFRYCOOK) {
                    this.theirJob = new JobBurgersFryCook(this);
                    //汉堡服务员
                } else if (vocation == Vocation.BURGERSWAITER) {
                    this.theirJob = new JobBurgersWaiter(this);
                    //蛋农
                } else if (vocation == Vocation.EGGFARMER) {
                    this.theirJob = new JobEggFarmer(this);
                }
                //赤脚大夫
                //妇产科医生
                //酒保
                //铲屎官
                //消防员
                //检票员
                //售票员
                //插花师
                //杂货商

                if(this.theirJob!=null){
                    this.theirJob.resetJob();
                    this.theirJob.step = 1;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("安排NPC们的工作出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 生成特征
     * @Date 19:17 2022/7/3
     * @Param []
     **/
    public void generateTraits() {
        try {
            Random rand = new Random();
            Trait[] traits1 = Traits.traitList;
            //Trait 1
            this.trait1 = traits1[rand.nextInt(traits1.length - 1)].traitName;


            //Trait 2
            this.trait2 = traits1[rand.nextInt(traits1.length - 1)].traitName;

            while (this.trait2 == this.trait1 || traitHasOpposite(this.trait2)) {
                this.trait2 = traits1[rand.nextInt(traits1.length - 1)].traitName;
            }


            //Trait 3
            this.trait3 = traits1[rand.nextInt(traits1.length - 1)].traitName;

            while (this.trait3 == this.trait2 || this.trait3 == this.trait1 || traitHasOpposite(this.trait3)) {
                this.trait3 = traits1[rand.nextInt(traits1.length - 1)].traitName;
            }


            //Trait 4
            this.trait4 = traits1[rand.nextInt(traits1.length - 1)].traitName;

            while (this.trait4 == this.trait1 || this.trait4 == this.trait2 || this.trait4 == this.trait3 || traitHasOpposite(this.trait4)) {
                this.trait4 = traits1[rand.nextInt(traits1.length - 1)].traitName;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("generateTraits出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 特质具有相反的性质
     *
     * @param trait
     * @return
     */
    public boolean traitHasOpposite(String trait) {
        try {
            if (Trait.getTraitFromName(trait).traitOpposite != null) {
                if (trait.contains(Trait.getTraitFromName(trait).traitOpposite.traitName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("traitHasOpposite出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return false;
    }

    /**
     * 有特点
     *
     * @param trait
     * @return
     */
    public boolean hasTrait(Trait trait) {
        boolean flag = true;
        try {
            if (this.trait1.contentEquals(trait.traitName) || this.trait2.contentEquals(trait.traitName) || this.trait3.contentEquals(trait.traitName) || this.trait4.contentEquals(trait.traitName)) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("hasTrait出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return flag;
    }

    /**
     * 获取村民清单
     *
     * @return
     */
    public InventoryBasic getVillagerInventory() {
        return this.villagerInventory;
    }
}
