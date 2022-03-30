package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.jobs.*;
import com.trhsy.sim.packets.client.UpdateFolkPositionMessage;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @ClassName FolkData
 * @Description todo 百姓实体类
 * @Author Tian
 * @Date 2022/1/2319:46
 **/
public class FolkData implements Serializable {
    private static final long serialVersionUID = -2617939828256928361L;
    /**
     * @Author fan
     * @Description //TODO 雇佣的
     * @Date 11:04 2022/3/26
     * @Param 
     * @return 
     **/
    public V3 employedAt = null;
    public Vocation vocation = null;
    public transient Job theirJob = null;
    public String name = "";
    public int age = 18;
    public int gender = 0;
    //皮肤数
    public int skinnumber = 1;
    public int levelFood = 10;
    public int levelFun = 5;
    public float levelBuilder = 1.0F;
    public float levelMiner = 1.0F;
    public float levelSoldier = 1.0F;
    /**
     * @Author fan
     * @Description //TODO 活动 行动
     * @Date 11:06 2022/3/26
     * @Param 
     * @return 
     **/
    public FolkAction action;
    public FolkAction actionArrival;
    /**
     * 留在原地
     */
    public Boolean stayPut;
    /**
     * 目的地
     */
    public V3 destination;
    public V3 location;
    public String statusText;
    public String status1;
    public String status2;
    public String status3;
    public String status4;
    public String status5;
    public float shaggingStage;
    /*
     * @Author fan
     * @Description //TODO 妊娠期
     * @Date 11:06 2022/3/26
     * @Param
     * @return
     **/
    public float pregnancyStage;
    public transient boolean isWorking;
    public boolean greetedToday;
    public static transient long anyFolkLastSpoke = 0L;
    public Building theBuilding;
    public TerraformerType terraformerType;
    public int terraformerRadius;
    public transient ArrayList<ItemStack> inventory;
    public transient EntityFolk theEntity;
    public transient Long timeStartedGotoing;
    public transient GotoMethod gotoMethod;
    private transient long timeSinceLastSave;
    private transient long timeSinceLastStatusUpdate;
    private transient long timeSinceLastMinute;
    public transient V3 beamingTo;
    private transient FolkData hangingWith;
    private transient int talkCounter;
    protected transient float matingStage;
    private transient int entityId;

    public FolkData() {
        this.action = FolkAction.WANDER;
        this.actionArrival = null;
        this.stayPut = false;
        this.destination = null;
        this.location = null;
        this.statusText = I18n.format("container.sim.folk_data.Wandering");
        this.status1 = "";
        this.status2 = "";
        this.status3 = "";
        this.status4 = "";
        this.status5 = "";
        this.shaggingStage = 0.0F;
        this.pregnancyStage = 0.0F;
        this.isWorking = false;
        this.greetedToday = false;
        this.theBuilding = null;
        this.terraformerType = null;
        this.terraformerRadius = 1;
        this.inventory = new ArrayList();
        this.theEntity = null;
        this.timeStartedGotoing = 0L;
        this.gotoMethod = null;
        this.timeSinceLastSave = 0L;
        this.timeSinceLastStatusUpdate = 0L;
        this.timeSinceLastMinute = 0L;
        this.beamingTo = null;
        this.hangingWith = null;
        this.talkCounter = 0;
        this.matingStage = -1.0F;
    }

    public void hasLoaded() {
        String voc = "none";
        String vocat = "";
        if (this.vocation != null && this.employedAt != null) {
            voc = this.vocation.toString();
            vocat = this.employedAt.toString();
        }

        if (this.employedAt == null) {
            this.vocation = null;
        }

        if (this.vocation == null) {
            this.employedAt = null;
        }

        if (this.levelMiner < 1.0F) {
            this.levelMiner = 1.0F;
        }

        if (this.levelBuilder < 1.0F) {
            this.levelBuilder = 1.0F;
        }

        if (this.levelSoldier < 1.0F) {
            this.levelSoldier = 1.0F;
        }

        try {
            ModSim.log.info("FolkData: hasLoaded() " + this.name + " (" + voc + ") at " + vocat + " location= " + this.location.toString() + "  " + ModSim.theFolks.size() + " 所有人");
        } catch (Exception var4) {
        }

        this.inventory = new ArrayList();
        this.setTheirJob(this.vocation);
        this.respawnEntity(MinecraftServer.getServer().worldServerForDimension(this.location.theDimension));
        ModSim.theFolks.add(this);
    }

    public FolkData(World theWorld) {
        this.action = FolkAction.WANDER;
        this.actionArrival = null;
        this.stayPut = false;
        this.destination = null;
        this.location = null;
        this.statusText = I18n.format("container.sim.folk_data.Wandering");
        this.status1 = "";
        this.status2 = "";
        this.status3 = "";
        this.status4 = "";
        this.status5 = "";
        this.shaggingStage = 0.0F;
        this.pregnancyStage = 0.0F;
        this.isWorking = false;
        this.greetedToday = false;
        this.theBuilding = null;
        this.terraformerType = null;
        this.terraformerRadius = 1;
        this.inventory = new ArrayList();
        this.theEntity = null;
        this.timeStartedGotoing = 0L;
        this.gotoMethod = null;
        this.timeSinceLastSave = 0L;
        this.timeSinceLastStatusUpdate = 0L;
        this.timeSinceLastMinute = 0L;
        this.beamingTo = null;
        this.hangingWith = null;
        this.talkCounter = 0;
        this.matingStage = -1.0F;
        Random rand = new Random();
        this.gender = rand.nextInt(2);
        this.name = generateName(this.gender, false, "");
        this.age = 18;
        if (this.gender == 0) {
            this.skinnumber = rand.nextInt(64);
        } else {
            this.skinnumber = rand.nextInt(64);
        }

        this.location = this.getLocationCloseToPlayer();
        if (this.location != null) {
            this.respawnEntity(theWorld);
            ModSim.theFolks.add(this);
            String just = I18n.format("container.sim.folk_data_just");
            ModSim.sendChat(this.name + just);
        }
    }

    public FolkData(World theWorld, FolkData mother, FolkData father) {
        this.action = FolkAction.WANDER;
        this.actionArrival = null;
        this.stayPut = false;
        this.destination = null;
        this.location = null;
        this.statusText = I18n.format("container.sim.folk_data.Wandering");
        this.status1 = "";
        this.status2 = "";
        this.status3 = "";
        this.status4 = "";
        this.status5 = "";
        this.shaggingStage = 0.0F;
        this.pregnancyStage = 0.0F;
        this.isWorking = false;
        this.greetedToday = false;
        this.theBuilding = null;
        this.terraformerType = null;
        this.terraformerRadius = 1;
        this.inventory = new ArrayList();
        this.theEntity = null;
        this.timeStartedGotoing = 0L;
        this.gotoMethod = null;
        this.timeSinceLastSave = 0L;
        this.timeSinceLastStatusUpdate = 0L;
        this.timeSinceLastMinute = 0L;
        this.beamingTo = null;
        this.hangingWith = null;
        this.talkCounter = 0;
        this.matingStage = -1.0F;
        Random rand = new Random();
        String surname = "Unknown";
        if (father != null) {
            surname = father.name.substring(father.name.indexOf(" ") + 1).trim();
        } else if (mother != null) {
            surname = mother.name.substring(mother.name.indexOf(" ") + 1).trim();
        }

        this.gender = rand.nextInt(2);
        this.name = generateName(this.gender, true, surname) + " " + surname;
        this.age = 0;
        if (this.gender == 0) {
            this.skinnumber = rand.nextInt(63) + 1;
        } else {
            this.skinnumber = rand.nextInt(63) + 1;
        }

        if (mother.getHome() != null) {
            mother.getHome().tennants.add(this.name);
        }

        mother.updateLocationFromEntity();
        World mworld = null;
        if (mother.isSpawned()) {
            mworld = mother.theEntity.worldObj;
        }

        this.location = Job.findAdjacentSpace(mother.location, mworld);
        this.respawnEntity(theWorld);
        ModSim.theFolks.add(this);
        String born = I18n.format("container.sim.folk_data_born");
        ModSim.sendChat(this.name + born);
        World world = ModSim.proxy.getClientWorld();
        if (world != null) {
            EntityPlayer p = Minecraft.getMinecraft().thePlayer;
            if (p != null) {
                ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":birth", 1.0F, 1.0F, false);
            }
        }

        Relationship.setupBloodRelationships(this, father, mother);

        try {
            this.levelBuilder = (float)Math.floor((double)(father.levelBuilder / 2.0F)) + (float)Math.floor((double)(mother.levelBuilder / 2.0F));
            if (this.levelBuilder > 10.0F) {
                this.levelBuilder = 10.0F;
            }

            this.levelMiner = (float)Math.floor((double)(father.levelMiner / 2.0F)) + (float)Math.floor((double)(mother.levelMiner / 2.0F));
            if (this.levelMiner > 10.0F) {
                this.levelMiner = 10.0F;
            }

            this.levelSoldier = (float)Math.floor((double)(father.levelSoldier / 2.0F)) + (float)Math.floor((double)(mother.levelSoldier / 2.0F));
            if (this.levelSoldier > 10.0F) {
                this.levelSoldier = 10.0F;
            }
        } catch (Exception var9) {
        }

    }

    public void updateLocationFromEntity() {
        if (this.isSpawned()) {
            this.location = new V3(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ, this.location.theDimension);
        }

    }

    public void respawnEntity(World world) {
        if (world != null) {
            if (this.beamingTo == null) {
                if (this.theEntity == null || this.theEntity.isDead) {
                    if (this.getDistanceToPlayer() < 50) {
                        this.theEntity = new EntityFolk(world);
                        this.theEntity.setLocationAndAngles(this.location.x, this.location.y, this.location.z, 0.0F, 0.0F);
                        if (!world.isRemote) {
                            world.spawnEntityInWorld(this.theEntity);
                        }

                        this.entityId = this.theEntity.getEntityId();
                        ModSim.log.info("FolkData:repawnEntity() " + this.name + " 在 " + this.location.toString() + " 昏暗中 " + this.location.theDimension + " ENTITY:" + this.theEntity.getEntityId());
                    }

                }
            }
        }
    }

    public static void triggerAllUpdates() {
        for (int f = 0; f < ModSim.theFolks.size(); ++f) {
            FolkData fd = (FolkData) ModSim.theFolks.get(f);
            fd.onUpdate();
        }

    }

    public void serverToClientLocationUpdate(V3 newLocation) {
        this.location = newLocation.clone();
        if (this.theEntity != null) {
            newLocation.x = Math.floor(newLocation.x) + 0.5D;
            newLocation.z = Math.floor(newLocation.z) + 0.5D;
            this.theEntity.posX = newLocation.x;
            this.theEntity.posY = newLocation.y;
            this.theEntity.posZ = newLocation.z;
        }

    }

    public void onUpdate() {
        Random rand = new Random();
        Long now = System.currentTimeMillis();
        //FolkData male;
        FolkData male;
        if (now - this.timeSinceLastMinute > 60000L) {
            if (this.getHome() == null && this.timeSinceLastMinute > 0L) {
                this.getHomeForHomeless();
            }

            long t = MinecraftServer.getServer().worldServers[0].getWorldTime() % 24000L;
            if (t < 2000L && this.pregnancyStage >= 1.0F) {
                Iterator i$ = ModSim.theBuildings.iterator();

                while(i$.hasNext()) {
                    Building build = (Building)i$.next();
                    if (build != null && build.primaryXYZ != null && build.displayName.contains("Clinic") && this.destination == null && !build.blockSpecial.isEmpty()) {
                        V3 bed = (V3) build.blockSpecial.get(0);
                        this.gotoXYZ(bed, (GotoMethod) null);
                        String baby = I18n.format("container.sim.folk_data_baby");
                        ModSim.sendChat(this.name + baby);
                    }
                }

                this.action = FolkAction.HAVINGBABY;
            } else if (t > 2000L && this.pregnancyStage >= 1.0F) {
                this.statusText = I18n.format("container.sim.folk_data_a_baby");
                this.pregnancyStage = 0.0F;
                male = Relationship.isFolkLivingWithSomeone(this, true);
                new FolkData(MinecraftServer.getServer().worldServerForDimension(0), this, male);
            }

            if (this.action == FolkAction.ATHOME || this.action == FolkAction.STAYINGHOME) {
                this.updateLocationFromEntity();

                try {
                    V3 liveAt = null;
                    if (this.getHome().livingXYZ != null) {
                        liveAt = this.getHome().livingXYZ.clone();
                    }

                    if (liveAt == null) {
                        liveAt = this.getHome().primaryXYZ.clone();
                    }

                    if (this.location.getDistanceTo(liveAt) > 5 && this.destination == null || this.location.theDimension != this.getHome().primaryXYZ.theDimension) {
                        this.actionArrival = this.action;
                        if (liveAt != null) {
                            this.gotoXYZ(liveAt, GotoMethod.WALK);
                        }
                    }
                } catch (Exception var16) {
                    ModSim.log.info("模拟城镇的关键异常:" + var16.getMessage());
                    //var16.printStackTrace();
                }
            }

            boolean gotWanderPoint = false;
            if (this.action == FolkAction.WANDER && this.isSpawned() && this.employedAt == null && this.age >= 18 && !this.statusText.contains(I18n.format("container.sim.folk_data.baby"))) {
                int xo;
                for (xo = 0; xo < ModSim.theBuildings.size(); ++xo) {
                    Building b = (Building) ModSim.theBuildings.get(rand.nextInt(ModSim.theBuildings.size()));
                    double dist = (double) this.location.getDistanceTo(b.primaryXYZ);
                    if (b.type.contentEquals("commercial") && dist < 40.0D) {
                        boolean hasShopKeeper = false;

                        for (int f = 0; f < ModSim.theFolks.size(); ++f) {
                            FolkData keeper = (FolkData) ModSim.theFolks.get(f);
                            if (keeper.employedAt != null && keeper.employedAt.isSameCoordsAs(b.primaryXYZ, true, true)) {
                                hasShopKeeper = true;
                                break;
                            }
                        }

                        if (hasShopKeeper) {
                            ModSim.log.info("FolkData:onUpdate() " + this.name + " 徘徊在 " + b.displayName + " " + dist + " 个街区之外。");
                            this.gotoXYZ(b.primaryXYZ, GotoMethod.WALK);
                            this.destination.doNotTimeout = true;
                            this.statusText = I18n.format("container.sim.folk_data_Shopping") + b.displayName;
                            gotWanderPoint = true;
                            if (this.hangingWith != null) {
                                this.hangingWith.statusText = I18n.format("container.sim.folk_data.Wandering");
                                this.hangingWith.hangingWith = null;
                                this.hangingWith = null;
                            }
                            break;
                        }
                    } else {
                        if (b.type.contentEquals("industrial") && dist < 40.0D && !b.displayName.toLowerCase().contains("farm")) {
                            try {
                                ModSim.log.info("FolkData: onUpdate() " + this.name + "徘徊在" + b.displayName + " " + dist + " 个街区之外。");
                                this.gotoXYZ(b.primaryXYZ, GotoMethod.WALK);
                                this.destination.doNotTimeout = true;
                                this.statusText = I18n.format("container.sim.folk_data_Visiting") + b.displayName;
                                gotWanderPoint = true;
                            } catch (Exception var14) {
                            }

                            if (this.hangingWith != null) {
                                this.hangingWith.statusText = I18n.format("container.sim.folk_data.Wandering");
                                this.hangingWith.hangingWith = null;
                                this.hangingWith = null;
                            }
                            break;
                        }

                        if (b.type.contentEquals("residential") && dist < 40.0D && this.hangingWith == null && b.tennants != null && b.tennants.size() > 0) {
                            FolkData resy = getFolkByName((String)b.tennants.get(0));

                            try {
                                if (!resy.name.contentEquals(this.name) && resy.hangingWith == null && (resy.action == FolkAction.WANDER || resy.action == FolkAction.STAYINGHOME)) {
                                    ModSim.log.info("FolkData:onUpdate() " + this.name + " 徘徊在 " + b.displayName + " " + dist + " 个街区之外。");
                                    this.gotoXYZ(b.primaryXYZ, GotoMethod.WALK);
                                    gotWanderPoint = true;
                                    String hanging = I18n.format("container.sim.folk_data_Hanging");
                                    this.statusText = hanging + resy.name;
                                    resy.gotoXYZ(b.primaryXYZ, GotoMethod.WALK);
                                    if (this.destination != null) {
                                        this.destination.doNotTimeout = true;
                                    }

                                    resy.statusText = hanging + this.name;
                                    this.hangingWith = resy;
                                    resy.hangingWith = this;
                                    break;
                                }
                            } catch (Exception var15) {
                            }
                        }
                    }
                }

                if (!gotWanderPoint) {
                    xo = rand.nextInt(60) - 30;
                    int zo = rand.nextInt(60) - 30;
                    V3 wanderTo = new V3(this.location.x + (double)xo, this.location.y, this.location.z + (double)zo, this.location.theDimension);

                    Double var40;
                    for(WorldServer world = MinecraftServer.getServer().worldServerForDimension(this.location.theDimension); world.getBlock(wanderTo.x.intValue(), wanderTo.y.intValue(), wanderTo.z.intValue()) != null && wanderTo.y < 255.0D; var40 = wanderTo.y = wanderTo.y + 1.0D) {
                        Double var38 = wanderTo.y;
                    }

                    ModSim.log.info("FolkData:onUpdate() 漫游命令 " + this.name + " to " + wanderTo.toString());
                    this.gotoXYZ(wanderTo, GotoMethod.WALK);
                    if (this.destination != null) {
                        this.destination.doNotTimeout = true;
                    }

                    this.statusText = I18n.format("container.sim.folk_data.Wandering");
                    this.stayPut = false;
                }
            } else if (this.action == FolkAction.WANDER && this.isSpawned() && this.age < 18) {
                male = Relationship.getMotherOf(this);
                if (male != null) {
                    this.gotoXYZ(male.location, (GotoMethod)null);
                }
            }

            if (this.hangingWith != null && !this.hangingWith.statusText.contains(this.name)) {
                this.hangingWith = null;
                this.statusText = I18n.format("container.sim.folk_data.Wandering");
            }

            if (!ModSim.isDayTime() && Relationship.isFolkLivingWithSomeone(this) && this.matingStage < 0.0F) {
                this.tryForBaby();
            }

            this.timeSinceLastMinute = now - (long)rand.nextInt(20000);
        }

        if (now - this.timeSinceLastStatusUpdate > 1000L) {
            this.updateStatusLines();
            if (this.statusText.contentEquals(I18n.format("container.sim.gui.button_Going"))) {
                this.stayPut = false;
            }

            int range;
            if (!this.isSpawned()) {
                range = this.getDistanceToPlayer();
                if (range < 50) {
                    this.respawnEntity(MinecraftServer.getServer().worldServerForDimension(this.location.theDimension));
                }
            } else {
                this.theEntity.dimension = this.location.theDimension;
                this.updateLocationFromEntity();
                range = this.getDistanceToPlayer();
                if (range >= 50 && this.theEntity != null) {
                    ModSim.log.info("FolkData: onSecTasks - 手动剥离 " + this.name + " 因为它们距离 " + range + " 街区远");
                    this.theEntity.setDead();
                }
            }

            if (ModSim.isDayTime() && this.employedAt != null && this.action != FolkAction.ONWAYTOWORK && this.action != FolkAction.ATWORK && this.pregnancyStage == 0.0F) {
                ModSim.log.info("FolkData: " + this.name + " 要工作了");
                this.statusText = I18n.format("container.sim.folk_data_Going_work");
                this.action = FolkAction.ONWAYTOWORK;
                this.gotoXYZ(this.employedAt, (GotoMethod) null);
                return;
            }

            if (this.pregnancyStage > 0.0F && this.employedAt != null && ModSim.isDayTime()) {
                this.statusText = I18n.format("container.sim.folk_data_Maternity_leave");
            }

            if (ModSim.isDayTime() && this.employedAt != null && this.action != FolkAction.ATWORK && this.destination == null && this.pregnancyStage == 0.0F) {
                this.statusText = I18n.format("container.sim.folk_data_Going_work");
                this.action = FolkAction.ONWAYTOWORK;
                ModSim.log.warn("FolkData:onUpdate() " + this.name + " 还在工作");
                this.updateLocationFromEntity();
                V3 temp = this.employedAt.clone();
                temp.x = temp.x + 5.0D;
                this.gotoXYZ(temp, GotoMethod.SHIFT);
                this.gotoXYZ(this.employedAt, (GotoMethod) null);
                return;
            }

            if (this.action == FolkAction.ONWAYTOWORK) {
                this.statusText = I18n.format("container.sim.folk_data_Going_work");
                this.stayPut = false;
                if (this.destination == null) {
                    this.gotoXYZ(this.employedAt, (GotoMethod) null);
                }
            }

            if (this.action == FolkAction.STAYINGHOME && this.hangingWith == null) {
                this.statusText = I18n.format("container.sim.folk_data_Staying_home");
                this.stayPut = true;
            }

            if (ModSim.isDayTime() && this.statusText.contains(I18n.format("container.sim.folk_data.for_a_baby"))) {
                this.statusText = I18n.format("container.sim.folk_data.Wandering");
                this.action = FolkAction.WANDER;
            }

            if (this.action == FolkAction.HAVINGBABY) {
                if (this.pregnancyStage < 1.0F) {

                    this.statusText = I18n.format("container.sim.folk_data_a_baby");
                } else {
                    this.statusText = I18n.format("container.sim.folk_data_Having_baby");
                }
            }

            if (ModSim.isDayTime() && this.employedAt == null && this.action == FolkAction.ATHOME) {
                this.isWorking = false;
                if ((new Random()).nextInt(4) == 1) {
                    this.statusText = I18n.format("container.sim.folk_data_Staying_home");
                    this.action = FolkAction.STAYINGHOME;
                } else {
                    this.statusText = I18n.format("container.sim.folk_data.Wandering");
                    this.action = FolkAction.WANDER;
                }
            }

            if (this.action == FolkAction.STAYINGHOME && this.employedAt != null) {
                this.action = FolkAction.WANDER;
            }

            boolean isSoldier = false;
            if (this.vocation != null && this.vocation == Vocation.SOLDIER) {
                isSoldier = true;
            }

            int chance;
            if (!ModSim.isDayTime() && !isSoldier) {
                this.action = FolkAction.WANDER;
                this.isWorking = false;
                if (this.getHome() == null) {
                    this.statusText = I18n.format("container.sim.folk_data.Wandering");
                    this.stayPut = false;
                } else {
                    if (this.gotoMethod == GotoMethod.WALK) {
                        this.updateLocationFromEntity();
                    }

                    V3 liveAt = null;

                    try {
                        liveAt = this.getHome().livingXYZ.clone();
                        if (liveAt == null) {
                            liveAt = this.getHome().primaryXYZ.clone();
                        }
                    } catch (Exception var13) {
                        ModSim.log.warn(this.name + " 没有现场直播" + var13.getMessage());
                    }

                    if (liveAt != null) {
                        chance = this.location.getDistanceTo(liveAt);
                        if (chance > 1 && this.destination == null) {
                            this.stayPut = false;
                            this.gotoXYZ(liveAt, (GotoMethod)null);
                            this.action = FolkAction.GOINGHOME;
                            this.statusText = I18n.format("container.sim.folk_data_Going_home");
                            this.isWorking = false;
                        }

                        if (chance <= 1 && !this.statusText.contains(I18n.format("container.sim.folk_data.baby"))) {
                            this.stayPut = true;
                            this.action = FolkAction.ATHOME;
                            this.statusText = I18n.format("container.sim.folk_data_Relaxing_home");
                            this.isWorking = false;
                        }
                    }
                }
            } else {
                this.stayPut = false;
            }

            if (this.action == FolkAction.WANDER) {
                this.stayPut = false;
            }

            int about10 = rand.nextInt(10000) + 10000;
            if (System.currentTimeMillis() - this.timeSinceLastSave > (long)about10) {
                Side side = FMLCommonHandler.instance().getEffectiveSide();
                if (side == Side.SERVER) {
                    ModSim.network.sendToServer(new UpdateFolkPositionMessage(this.location.toString() + ";" + this.name));
                    this.saveThisFolk();
                }

                this.timeSinceLastSave = System.currentTimeMillis();
            }

            if ((this.statusText.startsWith("Hanging out ") || this.statusText.startsWith("Shopping at the ")) && this.destination == null && this.theEntity != null) {
                ++this.talkCounter;
                if (this.talkCounter == 12) {
                    if (ModSim.configFolkTalking) {
                        chance = rand.nextInt(26) + 97;
                        String letter = ModSim.MODID + ":blarg" + Character.toString((char) chance);
                        ModSim.proxy.getClientWorld().playSound(this.location.x, this.location.y, this.location.z, letter, 1.0F, 1.0F, false);
                    }

                    this.talkCounter = 0;
                    if (this.hangingWith != null) {
                        Relationship.meddleWithRelationship(this, this.hangingWith);
                    }
                }
            }

            if (this.matingStage >= 0.0F && this.matingStage < 1.0F && this.gender == 1 && this.pregnancyStage == 0.0F) {
                if (ModSim.isDayTime()) {
                    this.matingStage = -1.0F;
                } else {
                    male = Relationship.isFolkLivingWithSomeone(this, true);
                    if (male != null) {
                        this.matingStage += 0.02F;
                        if (this.isSpawned()) {
                            World theWorld = Minecraft.getMinecraft().theWorld;
                            double d0 = rand.nextDouble() * 0.5D;
                            double d1 = rand.nextDouble() * 0.5D;
                            double d2 = rand.nextDouble() * 0.5D;
                            theWorld.spawnParticle("heart", this.theEntity.posX, this.theEntity.posY + 2.1D, this.theEntity.posZ, d0, d1, d2);
                            male.updateLocationFromEntity();
                            if ((double) this.matingStage < 0.15D) {
                                this.gotoXYZ(male.location, GotoMethod.SHIFT);
                            }

                            theWorld.spawnParticle("heart", male.location.x, male.location.y + 2.1D, male.location.z, d0, d1, d2);
                            this.statusText = I18n.format("container.sim.folk_data_Trying_baby");
                            male.statusText = I18n.format("container.sim.folk_data_Trying_baby");
                            male.stayPut = true;
                        }
                    }
                }
            } else if (this.matingStage >= 1.0F && this.matingStage < 1.1F) {
                this.matingStage = 1.1F;
                chance = rand.nextInt(7);
                ModSim.log.info("FolkData: 完成了婴儿机会的尝试=" + chance);
                male = Relationship.isFolkLivingWithSomeone(this, true);
                this.statusText = I18n.format("container.sim.folk_data_Relaxing_home");
                male.statusText = I18n.format("container.sim.folk_data_Relaxing_home");
                if (chance == 1 && this.age < 45) {
                    this.pregnancyStage = 0.1F;

                    String news = I18n.format("container.sim.folk_data_Good_news");
                    String and = I18n.format("container.sim.folk_data_and");
                    String expecting_a_baby = I18n.format("container.sim.folk_data_expecting_a_baby");
                    ModSim.sendChat(news + this.name + and + male.name + expecting_a_baby);
                    if (this.isSpawned()) {
                        this.theEntity.setJumping(true);
                    }

                    if (male.isSpawned()) {
                        male.theEntity.setJumping(true);
                    }

                    World world = ModSim.proxy.getClientWorld();
                    if (world != null) {
                        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                        if (p != null) {
                            ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":pregnant", 1.0F, 1.0F, false);
                        }
                    }
                }
            }

            this.timeSinceLastStatusUpdate = now;
        }

        if (this.beamingTo != null) {
            this.doBeaming();
        }

        if (this.theirJob != null) {
            this.theirJob.onUpdate();
        }

    }

    private void tryForBaby() {
        if (this.gender == 1 && this.pregnancyStage == 0.0F) {
            FolkData malePartner = Relationship.isFolkLivingWithSomeone(this, true);
            if (malePartner != null && this.action == FolkAction.ATHOME && malePartner.action == FolkAction.ATHOME) {
                this.matingStage = 0.0F;
                if (malePartner.isSpawned()) {
                    this.gotoXYZ(new V3(malePartner.theEntity.posX, malePartner.theEntity.posY, malePartner.theEntity.posZ, malePartner.theEntity.dimension), GotoMethod.WALK);
                }
            }
        } else {
            this.matingStage = -1.0F;
        }

    }

    private void getHomeForHomeless() {
        if (this.action == FolkAction.WANDER) {
            Building.loadAllBuildings();

            for (int b = 0; b < ModSim.theBuildings.size(); ++b) {
                Building building = (Building) ModSim.theBuildings.get(b);
                if (building.tennants.size() == 0 && building.buildingComplete && building.type.contentEquals("residential")) {
                    building.tennants.add(this.name);
                    this.action = FolkAction.GOINGHOME;
                    this.actionArrival = FolkAction.STAYINGHOME;
                    if (building.livingXYZ != null) {
                        this.gotoXYZ(building.livingXYZ, (GotoMethod) null);
                    } else {
                        this.gotoXYZ(building.primaryXYZ, (GotoMethod) null);
                    }

                    ModSim.states.saveStates();
                    String moving = I18n.format("container.sim.folk_data_moving");
                    String Moved = I18n.format("container.sim.folk_data_Moved");
                    ModSim.sendChat(this.name + moving + building.displayNameWithoutPK);
                    this.statusText = Moved + building.displayNameWithoutPK;
                    Building.saveAllBuildings();
                    break;
                }
            }
        }

    }

    private void updateStatusLines() {
        if (this.vocation == null) {
            this.status1 = I18n.format("container.sim.folkData1");
        } else {
            try {
                this.status1 = this.vocation.toString();
            } catch (Exception var2) {
                this.status1 = "";
            }
        }

        if (this.getHome() != null) {
            this.status2 = I18n.format("container.sim.folkData2");
        } else {
            this.status2 = I18n.format("container.sim.folkData3");
        }

        if (!Relationship.isFolkLivingWithSomeone(this)) {
            this.status3 = I18n.format("container.sim.folkData4");
        } else {
            this.status3 = I18n.format("container.sim.folkData5");
        }

        if (this.levelFood == 10) {
            this.status4 =I18n.format("container.sim.folkData6");
        } else if (this.levelFood > 5) {
            this.status4 = I18n.format("container.sim.folkData7");
        } else if (this.levelFood > 1) {
            this.status4 = I18n.format("container.sim.folkData8");
        } else {
            this.status4 = I18n.format("container.sim.folkData9");
        }

        if (this.levelFun == 10) {
            this.status5 = I18n.format("container.sim.folkData10");
        } else if (this.levelFun > 5) {
            this.status5 = I18n.format("container.sim.folkData11");
        } else if (this.levelFun > 1) {
            this.status5 = I18n.format("container.sim.folkData12");
        } else {
            this.status5 = I18n.format("container.sim.folkData13");
        }

    }

    public boolean isSpawned() {
        if (this.theEntity == null) {
            try {
                this.theEntity = getFolkByName(this.name).theEntity;
            } catch (Exception var2) {
            }
        }

        if (this.theEntity == null) {
            return false;
        } else {
            return !this.theEntity.isDead;
        }
    }

    public int getDistanceToPlayer() {
        EntityPlayer p = getClosestPlayer(this.location);
        if (p == null) {
            return 9999;
        } else {
            V3 pv = new V3(p.posX, p.posY, p.posZ, this.location.theDimension);
            return this.location.getDistanceTo(pv);
        }
    }

    public V3 getLocationCloseToPlayer() {
        EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;

        V3 ret;
        try {
            ret = new V3(p.posX, 5.0D, p.posZ, p.dimension);
        } catch (Exception var9) {
            ModSim.log.warn("getLocationCloseToPlayer: 玩家为空，返回空V3" + var9.getMessage());
            return new V3(0.0D, 5.0D, 0.0D, 0);
        }

        boolean found = false;
        Block bid;

        try {
            for(int go = 30; go > 1; --go) {
                ret = new V3(p.posX, 5.0D, p.posZ + (double)go, p.dimension);

                while(!found) {
                    bid = p.worldObj.getBlock(ret.x.intValue(), ret.y.intValue(), ret.z.intValue());
                    if ((p.worldObj.canBlockSeeTheSky(ret.x.intValue(), ret.y.intValue(), ret.z.intValue()) || p.dimension != 0) && bid != Blocks.leaves && bid == null) {
                        found = true;
                    }

                    Double var7 = ret.y;
                    Double var8 = ret.y = ret.y + 1.0D;
                    if (ret.y > 200.0D) {
                        break;
                    }
                }

                if (found) {
                    break;
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return !found ? new V3(0.0D, 5.0D, 0.0D, 0) : ret;
    }

    public static EntityPlayer getClosestPlayer(V3 location) {
        try {
            World world = MinecraftServer.getServer().worldServerForDimension(location.theDimension);
            EntityPlayer ret = world.getClosestPlayer(location.x, location.y, location.z, 60.0D);
            return ret;
        } catch (Exception var3) {
            return null;
        }
    }

    public static String generateName(int gender, boolean firstNameOnly, String lastNameOptional) {
        Random randomGenerator = new Random();
        String firstName = "";
        String lastName = "";
        FolkData test = null;

        for(int go = 0; go < 200; ++go) {
            int i;
            if (gender == 0) {
                i = randomGenerator.nextInt(ModSim.configMaleNames.length);
                firstName = ModSim.configMaleNames[i].trim();
            } else {
                i = randomGenerator.nextInt(ModSim.configFemaleNames.length);
                firstName = ModSim.configFemaleNames[i].trim();
            }

            i = randomGenerator.nextInt(ModSim.configSurnames.length);
            if (lastName.contentEquals("")) {
                lastName = ModSim.configSurnames[i].trim();
            } else {
                lastName = lastNameOptional;
            }
            test = getFolkByName(firstName + " " + lastName);
            /*if("en_US".equals(lang)){

            }else{
                test = getFolkByName(lastName +firstName );
            }*/

            if (test == null) {
                break;
            }
        }

        if (test != null) {
            lastName = lastName + " II";
        }

        return !firstNameOnly ? firstName + " " + lastName : firstName;
    }

    public void selfFire() {
        ModSim.log.info("FolkData: selfFire() " + this.name);
        this.isWorking = false;
        if (this.inventory.size() > 0) {
            int count = 0;

            for(int inv = 0; inv < this.inventory.size(); ++inv) {
                ItemStack is = (ItemStack)this.inventory.get(inv);
                if (is != null) {
                    if (this.theEntity != null) {
                        try {
                            this.theEntity.entityDropItem(is, (float)is.stackSize);
                        } catch (Exception var6) {
                        }
                    } else {
                        try {
                            getClosestPlayer(this.location).entityDropItem(is, (float)is.stackSize);
                        } catch (Exception var5) {
                        }
                    }

                    count += is.stackSize;
                }
            }

            if (count > 0) {
                String has_dropped = I18n.format("container.sim.folk_data_has_dropped");
                String inventory = I18n.format("container.sim.folk_data_inventory");

                ModSim.sendChat(this.name + has_dropped + count + inventory);
            }
        }

        this.inventory.clear();
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
        this.action = FolkAction.WANDER;
        this.statusText = I18n.format("container.sim.folk_data.Wandering");
        this.stayPut = false;
        this.saveThisFolk();
    }

    public void gotoXYZ(V3 whereTo, GotoMethod methodOfTravel) {
        if (whereTo != null) {
            this.stayPut = false;
            this.destination = whereTo.clone();
            if (this.destination != null) {
                this.destination.doNotTimeout = false;
                int dist = this.location.getDistanceTo(whereTo);
                if (!this.isSpawned()) {
                    methodOfTravel = null;
                }

                if (methodOfTravel == null) {
                    V3 playpos = null;

                    try {
                        EntityPlayer pl = getClosestPlayer(this.location);
                        if (pl != null && this.location.theDimension == this.destination.theDimension) {
                            playpos = new V3(pl.posX, pl.posY, pl.posZ, pl.dimension);
                        } else {
                            dist = 999;
                        }
                    } catch (Exception var11) {
                        dist = 999;
                    }

                    if (dist < 40) {
                        this.gotoMethod = GotoMethod.WALK;
                    }

                    if (!this.isSpawned() || dist >= 40) {
                        this.gotoMethod = GotoMethod.BEAM;
                    }

                    if (playpos != null) {
                        if (this.location.getDistanceTo(playpos) >= 40 && whereTo.getDistanceTo(playpos) >= 40) {
                            this.gotoMethod = GotoMethod.SHIFT;
                        }

                        try {
                            if (this.location.theDimension != Minecraft.getMinecraft().thePlayer.dimension && this.destination.theDimension != Minecraft.getMinecraft().thePlayer.dimension) {
                                this.gotoMethod = GotoMethod.SHIFT;
                            }
                        } catch (Exception var10) {
                            this.gotoMethod = GotoMethod.SHIFT;
                        }
                    }

                    if (methodOfTravel == null) {
                        methodOfTravel = GotoMethod.SHIFT;
                    }
                } else {
                    this.gotoMethod = methodOfTravel;
                }

                try {
                    ModSim.log.info("FolkData: GOTOXYZ() for " + this.name + " to " + whereTo.toString() + " - Method:" + this.gotoMethod.toString() + " DIM:" + whereTo.theDimension);
                } catch (Exception var9) {
                    ModSim.log.info("FolkData: GOTOXYZ() for " + this.name + " - NULL whereTo");
                    return;
                }

                if (this.destination != null) {
                    if (this.gotoMethod == GotoMethod.SHIFT) {
                        int xxx = this.destination.x.intValue();
                        int zzz = this.destination.z.intValue();
                        this.destination.x = (double)xxx + 0.5D;
                        this.destination.z = (double)zzz + 0.5D;
                        if (this.theEntity != null) {
                            this.theEntity.posX = this.destination.x;
                            this.theEntity.posY = this.destination.y;
                            this.theEntity.posZ = this.destination.z;

                            try {
                                if (this.location.theDimension != this.destination.theDimension) {
                                    this.theEntity.travelToDimension(this.destination.theDimension);
                                    this.theEntity.dimension = this.destination.theDimension;
                                    this.location.theDimension = this.destination.theDimension;
                                }
                            } catch (Exception var8) {
                            }
                        }

                        try {
                            this.location = this.destination.clone();
                        } catch (Exception var7) {
                        }

                        this.destination = null;
                    } else if (this.gotoMethod == GotoMethod.BEAM) {
                        this.timeStartedGotoing = System.currentTimeMillis();
                        this.beamMeTo(whereTo);
                    } else if (this.gotoMethod == GotoMethod.WALK) {
                        this.stayPut = false;
                        this.timeStartedGotoing = System.currentTimeMillis();
                        if (this.theEntity != null) {
                            this.theEntity.gotPath = false;
                        }
                    }

                }
            }
        }
    }

    public void beamMeTo(V3 whereToIn) {
        this.stayPut = true;
        this.updateLocationFromEntity();
        if (this.beamingTo != null) {
            ModSim.log.warn("FolkData:beamMeTo()已经喜气洋洋了 " + this.name);
        } else if (whereToIn == null) {
            ModSim.log.warn("FolkData: beamMeTo() whereTo was NULL, cancelled beaming");
        } else {
            this.timeStartedGotoing = System.currentTimeMillis();
            V3 whereTo = whereToIn.clone();
            World destWorld = MinecraftServer.getServer().worldServerForDimension(whereTo.theDimension);

            int xxx;
            for(xxx = 0; xxx < 200; ++xxx) {
                Block id1 = destWorld.getBlock(whereTo.x.intValue(), whereTo.y.intValue(), whereTo.z.intValue());
                Block id2 = destWorld.getBlock(whereTo.x.intValue(), whereTo.y.intValue() + 1, whereTo.z.intValue());
                if (id1 == null && id2 == null) {
                    break;
                }

                Double var8 = whereTo.y;
                Double var9 = whereTo.y = whereTo.y + 1.0D;
            }

            try {
                xxx = whereTo.x.intValue();
                whereTo.x = (double)xxx + 0.5D;
                xxx = whereTo.z.intValue();
                whereTo.z = (double)xxx + 0.5D;
                whereTo.y = whereTo.y - 199.0D;
            } catch (Exception var11) {
                return;
            }

            this.destination = whereTo.clone();
            ModSim.log.info("FolkData: BeamMeTo() for " + this.name + " to " + whereTo.toString() + " Dim:" + whereTo.theDimension);
            this.stayPut = true;
            if (this.isSpawned()) {
                this.theEntity.getNavigator().clearPathEntity();
            }

            try {
                if (ModSim.proxy.getClientWorld() != null) {
                }

                this.beamingTo = whereTo.clone();
            } catch (Exception var10) {
            }

        }
    }

    private void doBeaming() {
        try {
            if (System.currentTimeMillis() - this.timeStartedGotoing > 4000L || this.beamingTo == null) {
                if (this.theEntity != null) {
                    this.theEntity.setPosition(this.beamingTo.x, this.beamingTo.y, this.beamingTo.z);
                    if (this.theEntity.dimension != this.beamingTo.theDimension) {
                        this.theEntity.travelToDimension(this.beamingTo.theDimension);
                        this.theEntity.dimension = this.beamingTo.theDimension;
                        this.location.theDimension = this.beamingTo.theDimension;
                    }
                }

                ModSim.log.info("FolkData: doBeaming() 完成 " + this.name + " to " + this.beamingTo.toString() + " (dim " + this.beamingTo.theDimension + ")");
                this.location = this.beamingTo.clone();
                this.destination = null;
                this.beamingTo = null;
                this.respawnEntity(MinecraftServer.getServer().worldServerForDimension(this.location.theDimension));
                return;
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            this.destination = null;
            this.beamingTo = null;
            return;
        }

        Random random = new Random();
        Double d4 = ((double)random.nextFloat() - 2.0D) * 2.0D;
        this.stayPut = true;
        World theWorld = Minecraft.getMinecraft().theWorld;

        for(int p = 0; p < 10; ++p) {
            try {
                if (!ModSim.configDisableBeamEffect) {
                    theWorld.spawnParticle("portal", this.location.x + random.nextDouble() - 0.5D, this.location.y - 1.0D, this.location.z + random.nextDouble() - 0.5D, 0.0D, -d4, 0.0D);
                }
            } catch (Exception var7) {
            }

            try {
                if (!ModSim.configDisableBeamEffect) {
                    theWorld.spawnParticle("portal", this.beamingTo.x + random.nextDouble() - 0.5D, this.beamingTo.y - 1.0D, this.beamingTo.z + random.nextDouble() - 0.5D, 0.0D, -d4, 0.0D);
                }
            } catch (Exception var6) {
            }
        }

    }

    public static void loadAndSpawnFolks() {
        ModSim.theFolks.clear();
        File folksFolder = new File(ModSim.getSavesDataFolder() + "folks" + File.separator);
        if (!folksFolder.exists()) {
            folksFolder.mkdirs();
        }

        boolean useNewFormat = false;
        File[] arr$ = folksFolder.listFiles();
        int len$ = arr$.length;

        int i$;
        File f;
        for(i$ = 0; i$ < len$; ++i$) {
            f = arr$[i$];
            if (f.getName().endsWith(".sk2")) {
                useNewFormat = true;
                break;
            }
        }

        if (useNewFormat) {
            arr$ = folksFolder.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".sk2")) {
                    ArrayList<String> strings = ModSim.loadSK2(f.getAbsoluteFile().toString());
                    FolkData folkd = new FolkData();
                    Iterator iterator = strings.iterator();

                    while(iterator.hasNext()) {
                        String line = (String)iterator.next();
                        if (line.contains("|")) {
                            int m1 = line.indexOf("|");
                            String name = line.substring(0, m1);
                            String value = line.substring(m1 + 1);

                            try {
                                if (name.contentEquals("employedat")) {
                                    if (!value.contentEquals("null")) {
                                        folkd.employedAt = new V3(value);
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
                                    folkd.location = new V3(value);
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
                                        folkd.theBuilding.buildDirection = dir;
                                    }
                                } else if (name.contentEquals("terraformtype")) {
                                    if (!value.contentEquals("null")) {
                                        folkd.terraformerType = TerraformerType.valueOf(value);
                                    }
                                } else if (name.contentEquals("terraformradius")) {
                                    folkd.terraformerRadius = Integer.parseInt(value);
                                }
                            } catch (Exception var18) {
                                var18.printStackTrace();
                            }
                        }
                    }

                    if (folkd != null) {
                        ModSim.log.info("FolkData: loadAndSpawnFolks() 加载 " + folkd.name + " 使用新的文件系统");
                        folkd.hasLoaded();
                    }
                }
            }
        } else {
            arr$ = folksFolder.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".suk")) {
                    FolkData folkd = (FolkData) ModSim.proxy.loadObject(f.getAbsoluteFile().toString());
                    if (folkd != null) {
                        folkd.hasLoaded();
                    } else {
                        f.delete();
                    }
                }
            }
        }

    }

    public void saveThisFolk() {
        String folder = ModSim.getSavesDataFolder() + "folks" + File.separator;
        File f = new File(folder);
        if (!f.exists()) {
            f.mkdirs();
        }

        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER) {
            ArrayList<String> strings = new ArrayList();
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
            strings.add("levelfood|" + this.levelFood);
            strings.add("levelfun|" + this.levelFun);
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
            if (!this.name.contentEquals("")) {
                ModSim.saveSK2(folder + this.name + ".sk2", strings);
            }
        }

    }

    public Building getHome() {
        for (int b = 0; b < ModSim.theBuildings.size(); ++b) {
            Building home = (Building) ModSim.theBuildings.get(b);

            for (int t = 0; t < home.tennants.size(); ++t) {
                String tennant = (String) home.tennants.get(t);
                if (tennant.contentEquals(this.name)) {
                    return home;
                }
            }
        }

        return null;
    }

    public static void generateNewFolk(World world) {
        ArrayList<FolkData> fds = getFolkHomeless();
        if (fds.size() == 0 && ModSim.theFolks.size() < ModSim.configPopulationLimit) {
            new FolkData(world);
        }

    }

    public static FolkData getFolkByName(String name) {
        FolkData f = null;

        for (int x = 0; x < ModSim.theFolks.size(); ++x) {
            f = (FolkData) ModSim.theFolks.get(x);
            if (f.name.contentEquals(name)) {
                return f;
            }
        }

        return null;
    }

    public static FolkData getFolkByLocation(V3 loc) {
        FolkData f = null;

        for (int x = 0; x < ModSim.theFolks.size(); ++x) {
            f = (FolkData) ModSim.theFolks.get(x);
            if (f.location.isSameCoordsAs(loc, true, false)) {
                return f;
            }
        }

        return null;
    }

    public static FolkData getFolkByEmployedAt(V3 employedAt) {
        FolkData f = null;

        for (int x = 0; x < ModSim.theFolks.size(); ++x) {
            f = (FolkData) ModSim.theFolks.get(x);
            if (f.employedAt != null && f.employedAt.isSameCoordsAs(employedAt, true, false)) {
                return f;
            }
        }

        return null;
    }

    public static ArrayList<FolkData> getFolksByEmployedAt(V3 v) {
        ArrayList<FolkData> ret = new ArrayList();

        for (int x = 0; x < ModSim.theFolks.size(); ++x) {
            FolkData f = (FolkData) ModSim.theFolks.get(x);
            if (f.employedAt != null && f.employedAt.isSameCoordsAs(v, true, false)) {
                ret.add(f);
            }
        }

        return ret;
    }

    public static ArrayList getFolkUnemployed(boolean showEmployed) {
        ArrayList f = new ArrayList();

        for (int x = 0; x < ModSim.theFolks.size(); ++x) {
            FolkData folk = (FolkData) ModSim.theFolks.get(x);
            if (showEmployed) {
                if (folk.employedAt != null) {
                    f.add(folk);
                }
            } else if (folk.employedAt == null && folk.age > 17 && folk.pregnancyStage == 0.0F) {
                f.add(folk);
            }
        }

        return f;
    }

    public static ArrayList getFolkHomeless() {
        ArrayList f = new ArrayList();

        for (int x = 0; x < ModSim.theFolks.size(); ++x) {
            FolkData folk = (FolkData) ModSim.theFolks.get(x);
            if (folk.getHome() == null) {
                f.add(folk);
            }
        }

        return f;
    }

    public static FolkData getFolkDataByEntityId(int id) {
        for (int i = 0; i < ModSim.theFolks.size(); ++i) {
            FolkData fd = (FolkData) ModSim.theFolks.get(i);
            if (fd.theEntity != null && fd.theEntity.getEntityId() == id) {
                return fd;
            }
        }

        return null;
    }

    public void eventDied(DamageSource d) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        ModSim.log.info("在FolkData发生的事件中死亡 " + side.toString() + " side");
        String oldJob = "";
        if (this.vocation != null) {
            oldJob = " (" + this.vocation.toString() + ")";
        }

        this.employedAt = null;
        this.vocation = null;
        String deathBy = "";
        if (d == DamageSource.cactus) {
            deathBy = I18n.format("container.sim.folk_data_death_cactus");
        }

        if (d == DamageSource.drown) {
            deathBy = I18n.format("container.sim.folk_data_death_drowned");
        }

        if (d == DamageSource.generic) {
            deathBy = I18n.format("container.sim.folk_data_death_old");
        }

        if (d == DamageSource.inFire) {
            deathBy = I18n.format("container.sim.folk_data_death_combustion");
        }

        if (d == DamageSource.lava) {
            deathBy = I18n.format("container.sim.folk_data_death_lava");

            if (d == DamageSource.onFire) {
                deathBy = I18n.format("container.sim.folk_data_death_Burned");
            }

            if (d == DamageSource.outOfWorld) {
                deathBy = I18n.format("container.sim.folk_data_death_Fell");
            }

            if (d == DamageSource.starve) {
                deathBy = I18n.format("container.sim.folk_data_death_starvation");
            }

            if (d == DamageSource.fall) {
                deathBy = I18n.format("container.sim.folk_data_death_cliff");
            }

            if (d == DamageSource.inWall) {
                deathBy = I18n.format("container.sim.folk_data_death_under");
            }

            int i;
            if (deathBy.contentEquals("")) {
                Random r = new Random();
                i = r.nextInt(6);
                if (i == 0) {
                    deathBy = I18n.format("container.sim.folk_data_death_by_Electrocuted");
                } else if (i == 1) {
                    deathBy = I18n.format("container.sim.folk_data_death_by_Tripped");
                } else if (i == 2) {
                    deathBy = I18n.format("container.sim.folk_data_death_by_Trampled");
                } else if (i == 3) {
                    deathBy = I18n.format("container.sim.folk_data_death_by_Ran");
                } else if (i == 4) {
                    deathBy = I18n.format("container.sim.folk_data_death_by_Slipped");
                } else if (i == 5) {
                    deathBy = I18n.format("container.sim.folk_data_death_by_killed");
                }
            }

            String only = "";
            if (this.age < 80) {
                only = I18n.format("container.sim.folk_data_death_by_They") + this.age + I18n.format("container.sim.folk_data_death_by_years");
            } else {
                only = I18n.format("container.sim.folk_data_death_by_were") + this.age + I18n.format("container.sim.folk_data_death_by_life");
            }

            ModSim.sendChat(this.name + oldJob + I18n.format("container.sim.folk_data_death_by_died") + deathBy + only);
            this.action = FolkAction.WANDER;
            i = 0;

            int q;
            for (q = 0; q < ModSim.theFolks.size(); ++q) {
                FolkData fo = (FolkData) ModSim.theFolks.get(q);
                if (fo.name.contentEquals(this.name)) {
                    i = q;
                    break;
                }
            }

            this.evictThem();

            try {
                for (q = 0; q < ModSim.theRelationships.size(); ++q) {
                    try {
                        Relationship rel = (Relationship) ModSim.theRelationships.get(q);
                        if (rel.folk1.name.contentEquals(this.name) || rel.folk2.name.contentEquals(this.name)) {
                            String fn = rel.folk1.name.replaceAll(" ", "") + rel.folk2.name.replaceAll(" ", "");
                            File f = new File(ModSim.getSavesDataFolder() + "Relationships" + File.separator + fn + ".sk2");
                            f.delete();
                            ModSim.theRelationships.remove(q);
                        }
                    } catch (Exception var11) {
                        var11.printStackTrace();
                    }
                }

                File f = new File(ModSim.getSavesDataFolder() + "folks" + File.separator + this.name + ".sk2");
                f.delete();
                if (i >= 0 && i < ModSim.theFolks.size()) {
                    ModSim.theFolks.remove(i);
                }
            } catch (Exception var12) {
                ModSim.log.warn("FolkData: eventDied() " + var12.toString());
            }

        }
    }
    public void evictThem() {
        if (this.getHome() != null) {
            for (int b = 0; b < ModSim.theBuildings.size(); ++b) {
                Building building = (Building) ModSim.theBuildings.get(b);
                if (building != null && this.getHome() != null && building.primaryXYZ.isSameCoordsAs(this.getHome().primaryXYZ, true, false)) {
                    building.removeTennant(this.name);
                }
            }
        }

    }

    public void setTheirJob(Vocation vocation) {
        if (vocation != null) {
            this.vocation = vocation;
            if (this.vocation == Vocation.BUILDER) {
                this.theirJob = new JobBuilder(this);
            } else if (this.vocation == Vocation.BAKER) {
                this.theirJob = new JobBaker(this);
            } else if (this.vocation == Vocation.BUTCHER) {
                this.theirJob = new JobButcher(this);
            } else if (this.vocation == Vocation.CATTLEFARMER) {
                this.theirJob = new JobLivestockFarmer(this);
            } else if (this.vocation == Vocation.CHICKENFARMER) {
                this.theirJob = new JobLivestockFarmer(this);
            } else if (this.vocation == Vocation.COURIER) {
                this.theirJob = new JobCourier(this);
            } else if (this.vocation == Vocation.CROPFARMER) {
                this.theirJob = new JobCropFarmer(this);
            } else if (this.vocation == Vocation.GLASSMAKER) {
                this.theirJob = new JobGlassMaker(this);
            } else if (this.vocation == Vocation.GROCER) {
                this.theirJob = new JobGrocer(this);
            } else if (this.vocation == Vocation.LUMBERJACK) {
                this.theirJob = new JobLumberjack(this);
            } else if (this.vocation == Vocation.MERCHANT) {
                this.theirJob = new JobBuildersMerchant(this);
            } else if (this.vocation == Vocation.MINER) {
                this.theirJob = new JobMiner(this);
            } else if (this.vocation == Vocation.PIGFARMER) {
                this.theirJob = new JobLivestockFarmer(this);
            } else if (this.vocation == Vocation.SHEPHERD) {
                this.theirJob = new JobShepherd(this);
            } else if (this.vocation == Vocation.SOLDIER) {
                this.theirJob = new JobSoldier(this);
            } else if (this.vocation == Vocation.TERRAFORMER) {
                this.theirJob = new JobTerraformer(this);
            } else if (this.vocation == Vocation.FISHERMAN) {
                this.theirJob = new JobFisherman(this);
            } else if (this.vocation != Vocation.PATHBUILDER) {
                if (this.vocation == Vocation.DAIRYFARMER) {
                    this.theirJob = new JobDairyFarmer(this);
                } else if (this.vocation == Vocation.CHEESEMAKER) {
                    this.theirJob = new JobCheesemaker(this);
                } else if (this.vocation == Vocation.BURGERSMANAGER) {
                    this.theirJob = new JobBurgersManager(this);
                } else if (this.vocation == Vocation.BURGERSFRYCOOK) {
                    this.theirJob = new JobBurgersFryCook(this);
                } else if (this.vocation == Vocation.BURGERSWAITER) {
                    this.theirJob = new JobBurgersWaiter(this);
                } else if (this.vocation == Vocation.EGGFARMER) {
                    this.theirJob = new JobEggFarmer(this);
                }
            }

            this.theirJob.resetJob();
            this.theirJob.step = 1;
        }
    }

}
