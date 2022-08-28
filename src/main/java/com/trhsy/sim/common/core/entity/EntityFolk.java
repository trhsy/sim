package com.trhsy.sim.common.core.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.ai.EntityAIWanderSUK;
import com.trhsy.sim.client.gui.folk.GuiEntityFolk;
import com.trhsy.sim.client.gui.folk.GuiMerchant;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.jobs.JobFisherman;
import com.trhsy.sim.common.jobs.Stage;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ItemLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.*;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * @ClassName EntityFolk
 * @Description todo 实体只是为了展示，实体数据实际上完成了工作，这样做不需要区块加载，因此玩家可以在几英里之外完成工作。不确定是否需要INpc，也可以使用EntityAgeable来重写？
 * @Author Tian
 * @Date 2022/5/2120:40
 **/
public class EntityFolk extends EntityCreature implements INpc {
    //参考实际的NPC代码/属性/逻辑等
    public FolkData theData;
    //用于杀死繁殖的npc，我们将繁殖它们
    private long ghostTimer = -1L;
    //问候/打招呼计时器
    private long greetTimer = 0L;
    //最后一次受伤
    private long lastHurt = 0L;

    //找到路了吗
    public boolean gotPath;


    public EntityFolk(World world) {
        super(world);
        try {
            //避开水
            ((PathNavigateGround) this.getNavigator()).setAvoidsWater(false);
            //会进门
            ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
            //破门而入
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
            //会游泳
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);

            //会捡起地上的东西
            this.setCanPickUpLoot(true);

            //闲置任务
            this.tasks.addTask(1, new EntityAILookIdle(this));

            //住进屋子
            this.tasks.addTask(2, new EntityAIMoveIndoors(this));
            //限制开门
            this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
            //实体AI监视最近2
            this.tasks.addTask(10, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1));
            //实体AI监视最近
            this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
//开门
            this.tasks.addTask(9, new EntityAIOpenDoor(this, true));
            //闲逛
            //this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
            this.tasks.addTask(9, new EntityAIWanderSUK(this, 0.5D));
            this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.3D));
            //避开实体
            this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
//            this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
//            this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D));
//            this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
            //游泳
            this.tasks.addTask(4, new EntityAISwimming(this));
            this.isDead=false;

            //启动
            if (!ModSim.proxy.ranStartup) {
                ModSimReloaded.log.info("实体人：重置npc");
                this.setDead();
            }
//停用算时间是否是圣诞节
//        int dom = this.cal.get(5);
//        int moy = this.cal.get(2);
//        if (dom > 23 && dom < 27 && moy == 11) {
//            this.isXmas = true;
//        }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("初始化NPC实体出问题了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 频繁调用，因此实体可以根据需要在每次滴答声时更新其状态。例如，僵尸和骷髅会利用这一点对阳光做出反应并开始燃烧。
     * @Date 20:29 2022/8/20
     * @Param []
     **/
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    /**
     * @return java.lang.String
     * @Author fan
     * @Description //TODO 获得纹理
     * @Date 17:16 2022/5/22
     * @Param []
     **/
    @SideOnly(Side.CLIENT)
    public String getTexture() {
        String texture = "";
        try {
            if (theData != null) {
                //System.out.println("实体人性别："+theData.gender);
                if (theData.gender == 0) {
                    texture = "male" + theData.skinnumber + ".png";
                } else {
                    texture = "female" + theData.skinnumber + ".png";
                }
            } else {
                texture = "male0.png";
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getTexture出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return texture;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 实体属性
     * @Date 15:48 2022/6/5
     * @Param []
     **/
    @Override
    protected void applyEntityAttributes() {
        try {
            super.applyEntityAttributes();
            //共享怪物属性 移动速度
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("applyEntityAttributes出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 实体数据更新
     * @Date 15:50 2022/6/5
     * @Param []
     **/
    @Override
    public void onUpdate() {
        try {
            //如果NPC数据信息为空
            if (theData == null) {
                //npc 没有死
                if (!this.isDead) {
                    //定时器为初始值，定义当前时间
                    if (this.ghostTimer == -1) {
                        this.ghostTimer = System.currentTimeMillis();
                    }
                    //重新赋值NPC数据，实体 ID 的民间数据
                    theData = FolkData.getFolkDataByEntityId(this.getEntityId());
                    //如果实体人数据还是空，并且五秒钟没有回复判断为死亡
                    if (theData == null && System.currentTimeMillis() - this.ghostTimer > 5000L) {
                        ModSimReloaded.log.info("NPC: " + this.getEntityId() + " - 他们的数据已经空了5秒多，所以判定为死亡");
                        //设置死亡
                        //this.onDeath(DamageSource.inWall);
                        this.setDead();
                    }
                }

            } else {
                //this.setLocationAndAngles(theData.location.xCoord, theData.location.yCoord, theData.location.zCoord, 0.0F, 0.0F);
                //如果实体人工作中
                if (theData.isWorking) {

                    float s = (float) (Math.sin((double) System.currentTimeMillis() * 0.01) / 10) + 0.1F;
                    //工作进度
                    this.swingProgress = s;
                } else {
                    this.swingProgress = 0.0F;
                }
                //当前时间减去打招呼的时间大于1000
                if (System.currentTimeMillis() - this.greetTimer > 1000L) {
                    //定义随机值
                    Random r = new Random();
                    //获取到玩家的距离
                    double dist = theData.getDistanceToPlayer();

                    if (ModSimReloaded.states != null) {
                        long var10000 = System.currentTimeMillis();
                        Long ls = var10000 - FolkData.anyFolkLastSpoke;
                        //配置NPC说英文
                        if (ConfigLoader.configFolkTalkingEnglish && ls > 5000L && (!theData.greetedToday & dist < 5.0 || theData.vocation == Vocation.BURGERSWAITER && dist < 5.0 && r.nextInt(20) == 2)) {
                            theData.greetedToday = true;
                            FolkData.anyFolkLastSpoke = System.currentTimeMillis();
                            int sf = r.nextInt(25) + 1;
                            String fn = ModSim.MODID + ":";
                            if (theData.vocation != null && theData.vocation == Vocation.BURGERSWAITER) {
                                sf = r.nextInt(6);
                                fn = fn + "burger";
                                switch (sf) {
                                    case 0:
                                        if (theData.gender == 0) {
                                            fn = fn + "ma";
                                        } else {
                                            fn = fn + "fa";
                                        }
                                        break;
                                    case 1:
                                        if (theData.gender == 0) {
                                            fn = fn + "mb";
                                        } else {
                                            fn = fn + "fb";
                                        }
                                        break;
                                    case 2:
                                        if (theData.gender == 0) {
                                            fn = fn + "mc";
                                        } else {
                                            fn = fn + "fc";
                                        }
                                        break;
                                    case 3:
                                        if (theData.gender == 0) {
                                            fn = fn + "md";
                                        } else {
                                            fn = fn + "fd";
                                        }
                                        break;
                                    case 4:
                                        if (theData.gender == 0) {
                                            fn = fn + "me";
                                        } else {
                                            fn = fn + "fe";
                                        }
                                        break;
                                    case 5:
                                        if (theData.gender == 0) {
                                            fn = fn + "mf";
                                        } else {
                                            fn = fn + "ff";
                                        }
                                }
                            } else if (theData.age >= 18) {
                                if (ModSimReloaded.isDayTime()) {
                                    if (sf == 1) {
                                        if (theData.gender == 0) {
                                            fn = fn + "daymone";
                                        } else {
                                            fn = fn + "dayfone";
                                        }
                                    } else if (sf == 2) {
                                        if (theData.gender == 0) {
                                            fn = fn + "daymtwo";
                                        } else {
                                            fn = fn + "dayftwo";
                                        }
                                    } else if (sf == 3) {
                                        if (!this.worldObj.isRaining()) {
                                            if (theData.gender == 0) {
                                                fn = fn + "daymthree";
                                            } else {
                                                fn = fn + "dayfthree";
                                            }
                                        } else if (theData.gender == 0) {
                                            fn = fn + "mwxbad";
                                        } else {
                                            fn = fn + "fwxbad";
                                        }
                                    } else if (sf == 4) {
                                        if (theData.gender == 0) {
                                            fn = fn + "meight";
                                        } else {
                                            fn = fn + "feight";
                                        }
                                    } else if (sf == 5) {
                                        if (theData.gender == 0) {
                                            fn = fn + "mnine";
                                        } else {
                                            fn = fn + "fnine";
                                        }
                                    } else if (sf == 6) {
                                        if (theData.gender == 0) {
                                            fn = fn + "mseven";
                                        } else {
                                            fn = fn + "fseven";
                                        }
                                    } else if (sf > 6) {
                                        if (theData.gender == 0) {
                                            fn = fn + "mspeak";
                                        } else {
                                            fn = fn + "fspeak";
                                        }

                                        fn = fn + Character.toString((char) (sf + 90));
                                    }
                                } else if (sf == 1) {
                                    if (theData.gender == 0) {
                                        fn = fn + "nightmone";
                                    } else {
                                        fn = fn + "nightfone";
                                    }
                                } else if (sf == 2) {
                                    if (theData.gender == 0) {
                                        fn = fn + "nightmtwo";
                                    } else {
                                        fn = fn + "nightftwo";
                                    }
                                } else if (sf == 3) {
                                    if (theData.gender == 0) {
                                        fn = fn + "nightmthree";
                                    } else {
                                        fn = fn + "nightfthree";
                                    }
                                } else if (sf == 4) {
                                    if (theData.gender == 0) {
                                        fn = fn + "meight";
                                    } else {
                                        fn = fn + "feight";
                                    }
                                } else if (sf == 5) {
                                    if (theData.gender == 0) {
                                        fn = fn + "mnine";
                                    } else {
                                        fn = fn + "fnine";
                                    }
                                } else if (sf == 6) {
                                    if (theData.gender == 0) {
                                        fn = fn + "mseven";
                                    } else {
                                        fn = fn + "fseven";
                                    }
                                }
                            } else {
                                sf = r.nextInt(3) + 1;
                                fn = fn + "cspeak";
                                fn = fn + Character.toString((char) (sf + 96));
                            }

                            if (r.nextBoolean()) {
                                ModSim.proxy.getClientWorld().playSound(this.posX, this.posY, this.posZ, fn, 1, 1, false);
                            }
                        }
                    }
                    //饿死
                    if (theData.levelFood < 0) {
                        this.onDeath(DamageSource.starve);
                    }

                    this.greetTimer = System.currentTimeMillis();
                }
            }

            List list1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX, this.posY, this.posZ,this.posX + 1.0, this.posY + 1.0, this.posZ + 1.0).expand(2.0, 4.0, 2.0));
            if (!list1.isEmpty()) {
                for (Object entitys : list1) {
                    Entity entity = (Entity) entitys;
                    if (entity instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem) entity;
                        ItemStack is = entityitem.getEntityItem();
                        Item item = is.getItem();
                        if (item != null) {
                            String itemName = item.getRegistryName();
                            if (itemName.contains("mutton") || itemName.contains("rabbit") || itemName.contains("pie") || itemName.contains("carrot") || itemName.contains("potato") || itemName.contains("carrot") || itemName.contains("eye") || itemName.contains("flesh") || itemName.contains("chicken") || itemName.contains("beef") || itemName.contains("melon") || itemName.contains("cookie") || itemName.contains("fish") || itemName.contains("bread") || itemName.contains("apple") || itemName.contains("burger") || itemName.contains("fries") || itemName.contains("cheese")) {
                                System.out.println(itemName);
                                ItemFood food = (ItemFood) item;
                                if (theData.levelFood < 10 && food != null) {
                                    this.worldObj.playSoundAtEntity(this, "random.burp", 1, 1);
                                    entityitem.setDead();
                                    ++theData.levelFood;
                                }
                            }
                        }

                    } else if (entity instanceof EntityFolk && (int) this.posX == (int) entity.posX && (int) this.posZ == (int) entity.posZ) {
                        if (theData != null) {
                            this.motionX += 0.10000000149011612D;
                            theData.stayPut = false;
                        }
                    }
                }
            }
            super.onUpdate();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("EntityFolk-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            e.printStackTrace();
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 移动实体开始传送
     * @Date 15:59 2022/6/5
     * @Param [d, d1, d2]
     **/
    @Override
    public void moveEntity(double x, double y, double z) {
        try {
            if (this.isDead || theData == null) {
                return;
            }
            double dist = 0;
            if (theData.destination != null && theData.beamingTo == null) {
                try {
                    dist = this.getDistance(theData.destination.xCoord, theData.destination.yCoord, theData.destination.zCoord);
                } catch (Exception e) {
                    ModSimReloaded.log.warn("人们 theData.destination 中的目标为空 moveEntity()");
                    return;
                }
                if (dist <= 2.0) {
                    theData.updateLocationFromEntity();
                    this.motionX = 0;
                    this.motionZ = 0;
                    theData.stayPut = true;
                    theData.destination = null;
                    this.getNavigator().clearPathEntity();
                    this.gotPath = false;
                    if (theData.actionArrival != null) {
                        theData.action = theData.actionArrival;
                        theData.actionArrival = null;
                    }
                } else {
                    if (!this.gotPath) {
                        V3 v = new V3(theData.destination.xCoord + 0.5, theData.destination.yCoord, theData.destination.zCoord + 0.5);
                        theData.destination = v;
                        PathEntity path = this.getNavigator().getPathToXYZ(v.xCoord, v.yCoord, v.zCoord);
                        if (path != null) {
                            ModSimReloaded.log.info("实体人:[ " + theData.name + " ]即走过去☞x:" + v.xCoord + ",y:" + v.yCoord + ",z:" + v.zCoord);
                            this.getNavigator().setPath(path, 0.3D);
                            this.gotPath = true;
                        }
                    }
                }

                boolean donttimeout = false;
                if (theData.destination != null) {
                    donttimeout = theData.destination.doNotTimeout;
                }
                if (theData.timeStartedGotoing != null && donttimeout == false && System.currentTimeMillis() - theData.timeStartedGotoing > 40000L && theData.beamingTo == null) {
                    this.getNavigator().clearPathEntity();
                    if (dist > 2.0) {
                        V3 v = theData.destination;
                        if (v != null) {
                            ModSimReloaded.log.info("实体人: " + theData.name + " 即将传输至☞x:" + v.xCoord + ",y:" + v.yCoord + ",z:" + v.zCoord);
                            theData.stayPut = true;
                            theData.timeStartedGotoing = System.currentTimeMillis();
                            theData.beamMeTo(v);
                        }

                    }
                }
            }
            if (theData.stayPut) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                this.getNavigator().clearPathEntity();
            } else {
               /* if (x <= 0) {
                    x = theData.location.xCoord;
                }
                if (y <= 0) {
                    y = theData.location.yCoord;
                }
                if (z <= 0) {
                    z = theData.location.zCoord;
                }*/
                //theData.destination = new V3(x, y, z);
                //ModSimReloaded.log.info("moveEntity,x:" + x + ",y:" + y + ",z:" + z);
                super.moveEntity(x, y, z);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("moveEntity出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return net.minecraft.item.ItemStack
     * @Author fan
     * @Description //TODO 持有物品
     * @Date 16:00 2022/6/5
     * @Param []
     **/
    @Override
    public ItemStack getHeldItem() {
        ItemStack itemStack = null;
        try {
            if (theData == null) {
                return null;
            } else if (theData.theirJob == null) {
                return null;
            } else if (theData.vocation == Vocation.CROPFARMER) {
                //农民
                itemStack = new ItemStack(ItemLoader.tinHoe, 1);
            } else if (theData.vocation == Vocation.LUMBERJACK) {
                //伐木工人
                itemStack = new ItemStack(ItemLoader.tinAxe, 1);
            } else if (theData.vocation == Vocation.MINER) {
                //矿工
                itemStack = new ItemStack(ItemLoader.tinPickaxe, 1);
            } else if (theData.vocation == Vocation.BAKER) {
                //面包师
                itemStack = new ItemStack(Items.wooden_shovel, 1);
            } else if (theData.vocation == Vocation.SOLDIER) {
                //战士
                itemStack = new ItemStack(ItemLoader.tinSword, 1);
            } else if (theData.vocation == Vocation.BUILDER) {
                //建筑者
                itemStack = new ItemStack(Blocks.cobblestone, 1);
            } else if (theData.vocation == Vocation.SHEPHERD) {
                //牧羊人
                itemStack = new ItemStack(Items.shears, 1);
            } else if (theData.vocation == Vocation.GROCER) {
                //杂货商
                itemStack = new ItemStack(Items.melon, 1);
            } else if (theData.vocation == Vocation.COURIER) {
                //快递员
                itemStack = new ItemStack(Blocks.chest, 1);
            } else if (theData.vocation == Vocation.MERCHANT) {
                //建筑商
                itemStack = new ItemStack(Blocks.brick_block, 1);
            } else if (theData.vocation == Vocation.BUTCHER) {
                //屠夫
                itemStack = new ItemStack(Items.porkchop, 1);
            } else if (theData.vocation == Vocation.CATTLEFARMER) {
                //养牛户
                itemStack = new ItemStack(ItemLoader.tinAxe, 1);
            } else if (theData.vocation == Vocation.PIGFARMER) {
                //养猪户
                itemStack = new ItemStack(ItemLoader.tinAxe, 1);
            } else if (theData.vocation == Vocation.CHICKENFARMER) {
                //养鸡户
                itemStack = new ItemStack(ItemLoader.tinAxe, 1);
            } else if (theData.vocation == Vocation.TERRAFORMER) {
                //地形成型机
                itemStack = new ItemStack(Items.diamond_shovel, 1);
            } else if (theData.vocation == Vocation.GLASSMAKER) {
                //玻璃工人
                itemStack = new ItemStack(Blocks.glass_pane, 1);
            } else if (theData.vocation == Vocation.DAIRYFARMER) {
                //牛奶农
                itemStack = new ItemStack(Items.milk_bucket, 1);
            } else if (theData.vocation == Vocation.CHEESEMAKER) {
                //奶酪匠
                itemStack = new ItemStack(ItemLoader.itemCheese, 1, 0);
            } else if (theData.vocation == Vocation.BURGERSMANAGER) {
                //汉堡经理
                itemStack = new ItemStack(ItemLoader.itemCheeseburger, 1, 3);
            } else if (theData.vocation == Vocation.BURGERSFRYCOOK) {
                //后厨
                itemStack = new ItemStack(ItemLoader.tinSpade, 1);
            } else if (theData.vocation == Vocation.BURGERSWAITER) {
                //汉堡服务员
                itemStack = new ItemStack(ItemLoader.itemFries, 1, 2);
            } else if (theData.vocation == Vocation.FISHERMAN) {
                //职业渔夫
                JobFisherman jf = (JobFisherman) theData.theirJob;
                itemStack = jf.theStage == Stage.IDLE ? new ItemStack(Items.fish, 1) : new ItemStack(Items.fishing_rod, 1);
            } else {
                return null;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getHeldItem出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return itemStack;
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 互动
     * @Date 16:02 2022/6/5
     * @Param [entityplayer]
     **/
    @Override
    @SideOnly(Side.CLIENT)
    public boolean interact(EntityPlayer entityplayer) {
        boolean falg = true;
        try {
            Minecraft mc = Minecraft.getMinecraft();
            mc.currentScreen = null;
            GuiScreen ui = null;
            if (theData == null) {
                this.setDead();
                falg = false;
                return falg;
            } else {
                if (theData.theirJob != null) {
                    if (theData.vocation == Vocation.MERCHANT && ModSimReloaded.isDayTime()) {
                        ui = new GuiMerchant();
                    } else {
                        ui = new GuiEntityFolk(theData, entityplayer);
                    }
                } else {
                    ui = new GuiEntityFolk(theData, entityplayer);
                }

                mc.displayGuiScreen(ui);
                if (theData.age < 18) {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, ModSim.MODID + ":helloc", 1, 1, false);
                } else if (theData.gender == 0) {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, ModSim.MODID + ":hellom", 1, 1, false);
                } else {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, ModSim.MODID + ":hellof", 1, 1, false);
                }

                falg = true;
                return falg;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("interact出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return falg;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 死亡
     * @Date 16:03 2022/6/5
     * @Param [d]
     **/
    @Override
    public void onDeath(DamageSource d) {
        try {
            theData.eventDied(d);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("onDeath出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 可以推
     * @Date 16:03 2022/6/5
     * @Param []
     **/
    @Override
    public boolean canBePushed() {
        return true;
    }

    /**
     * @return java.lang.String
     * @Author fan
     * @Description //TODO 受到 伤害声音
     * @Date 16:04 2022/6/5
     * @Param []
     **/
    @Override
    protected String getHurtSound() {
        String hurtSound = null;
        try {
            if (!this.isBurning()) {
                this.heal(10.0F);
                if (theData != null) {
                    if (theData.stayPut) {
                        theData.stayPut = false;
                    }
                    /*BlockPos blockPos1 = new BlockPos(this.posX + 1, this.posY, this.posZ);
                    BlockPos blockPos2 = new BlockPos(this.posX - 1, this.posY, this.posZ);
                    BlockPos blockPos3 = new BlockPos(this.posX, this.posY, this.posZ + 1);
                    BlockPos blockPos4 = new BlockPos(this.posX + 1, this.posY, this.posZ - 1);
                    Block idx = this.worldObj.getBlockState(blockPos1).getBlock();
                    Block idX2 = this.worldObj.getBlockState(blockPos2).getBlock();
                    Block idz = this.worldObj.getBlockState(blockPos3).getBlock();
                    Block idZ2 = this.worldObj.getBlockState(blockPos4).getBlock();


                    if (idx != null && idx == Blocks.air) {
                        this.motionX = theData.location.xCoord + 1;
                    } else if (idX2 != null && idX2 == Blocks.air) {
                        this.motionX = theData.location.xCoord - 1;
                    } else if (idz != null && idz == Blocks.air) {
                        this.motionZ = theData.location.zCoord + 1;
                    } else if (idZ2 != null && idZ2 == Blocks.air) {
                        this.motionZ = theData.location.zCoord - 1;
                    }*/
                    if (theData.location != null) {
                        this.motionY = theData.location.yCoord + 0.5;
                        this.motionX = theData.location.xCoord + 0.5;
                        this.motionZ = theData.location.zCoord + 0.5;
                    }
                    //受伤要跑出受伤范围
                    theData.gotoXYZ(new V3(motionX, motionY, motionZ, 0), GotoMethod.SHIFT);
                }

                if (theData == null) {
                    hurtSound = null;
                    return hurtSound;
                }
                if (ConfigLoader.configFolkTalking) {
                    if (System.currentTimeMillis() - this.lastHurt > 10000L) {
                        this.lastHurt = System.currentTimeMillis();
                        hurtSound = theData.gender == 0 ? ModSim.MODID + ":OuchM" : ModSim.MODID + ":OuchF";
                    } else {
                        hurtSound = null;
                    }
                } else {
                    hurtSound = null;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getHurtSound出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return hurtSound;
    }

    @Override
    public int getTalkInterval() {
        Random r = new Random();
        return 1000 + r.nextInt(1000);
    }

    //@Override
    //public boolean isAIEnabled() {
    //    return true;
    //}

    @Override
    public int getMaxSpawnedInChunk() {
        return 200;
    }

    @Override
    public boolean canDespawn() {
        return true;
    }

   /* @Override
    public AxisAlignedBB getCollisionBoundingBox(Entity par1Entity) {
        return par1Entity.getCollisionBox(par1Entity);
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return this.getEntityBoundingBox();
    }*/

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public void onPlayerLogin(EntityPlayer player) {
    }

    public void onPlayerLogout(EntityPlayer player) {
    }

    public void onPlayerRespawn(EntityPlayer player) {
    }


}
