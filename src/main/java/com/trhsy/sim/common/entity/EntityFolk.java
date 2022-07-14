package com.trhsy.sim.common.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.ai.EntityAIWanderSUK;
import com.trhsy.sim.common.gui.folk.GuiEntityFolk;
import com.trhsy.sim.common.gui.folk.GuiMerchant;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
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
 * @Description todo
 * @Author Tian
 * @Date 2022/5/2120:40
 **/
public class EntityFolk extends EntityCreature implements INpc {
    public FolkData theData = null;
    //记忆计时器
    private long ghostTimer = -1L;
    //问候/打招呼计时器
    private long greetTimer = 0L;
    //最后一次受伤
    private long lastHurt = 0L;
    //日历
    private Calendar cal = new GregorianCalendar();
    //找到路了吗
    public boolean gotPath;


    public EntityFolk(World world) {
        super(world);

        //避开水
        ((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
        //会进门
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        //破门而入
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        //会游泳
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);

        //会捡起地上的东西
        this.setCanPickUpLoot(true);
        //this.setEquipmentDropChance(1, 1);
        //闲逛
        this.tasks.addTask(0, new EntityAIWanderSUK(this, 0.3f));
        //闲置任务
        this.tasks.addTask(1, new EntityAILookIdle(this));
        //住进屋子
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        //限制开门
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        //实体AI监视最近2
        this.tasks.addTask(10, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        //实体AI监视最近
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        //开门
        this.tasks.addTask(9, new EntityAIOpenDoor(this, true));
        //走向限制
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.3));
        //避免实体
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        //游泳
        this.tasks.addTask(4, new EntityAISwimming(this));


        //闲逛
        //this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        //拾取战利品
        this.setCanPickUpLoot(true);
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
    }

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
        if (this.theData != null) {
            //System.out.println("实体人性别："+this.theData.gender);
            if (this.theData.gender == 0) {
                return "male" + this.theData.skinnumber + ".png";
            } else {
                return "female" + this.theData.skinnumber + ".png";
            }
        } else {
            return "male0.png";
        }
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
        super.applyEntityAttributes();
        //共享怪物属性 移动速度
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0);
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
        //如果NPC数据信息为空
        if (this.theData == null) {
            //npc 没有死
            if (!this.isDead) {
                //定时器为初始值，定义当前时间
                if (this.ghostTimer == -1L) {
                    this.ghostTimer = System.currentTimeMillis();
                }
                //重新赋值NPC数据，实体 ID 的民间数据
                this.theData = FolkData.getFolkDataByEntityId(this.getEntityId());
                //如果实体人数据还是空，并且五秒钟没有回复判断为死亡
                if (this.theData == null && System.currentTimeMillis() - this.ghostTimer > 5000L) {
                    ModSimReloaded.log.info("实体人: " + this.getEntityId() + " - 他们的数据已经空了5秒多，所以判定为死亡");
                    //设置死亡
                    this.setDead();
                }
            }

        } else {
            //如果实体人工作中
            if (this.theData.isWorking) {

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
                double dist = (double) this.theData.getDistanceToPlayer();

                if (ModSimReloaded.states != null) {
                    long var10000 = System.currentTimeMillis();
                    FolkData var10001 = this.theData;
                    Long ls = var10000 - FolkData.anyFolkLastSpoke;
                    //配置NPC说英文
                    if (ConfigLoader.configFolkTalkingEnglish && ls > 5000L && (!this.theData.greetedToday & dist < 5.0 || this.theData.vocation == Vocation.BURGERSWAITER && dist < 5.0 && r.nextInt(20) == 2)) {
                        this.theData.greetedToday = true;
                        FolkData var18 = this.theData;
                        FolkData.anyFolkLastSpoke = System.currentTimeMillis();
                        int sf = r.nextInt(25) + 1;
                        String fn = ModSim.MODID + ":";
                        if (this.theData.vocation != null && this.theData.vocation == Vocation.BURGERSWAITER) {
                            sf = r.nextInt(6);
                            fn = fn + "burger";
                            switch (sf) {
                                case 0:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "ma";
                                    } else {
                                        fn = fn + "fa";
                                    }
                                    break;
                                case 1:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mb";
                                    } else {
                                        fn = fn + "fb";
                                    }
                                    break;
                                case 2:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mc";
                                    } else {
                                        fn = fn + "fc";
                                    }
                                    break;
                                case 3:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "md";
                                    } else {
                                        fn = fn + "fd";
                                    }
                                    break;
                                case 4:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "me";
                                    } else {
                                        fn = fn + "fe";
                                    }
                                    break;
                                case 5:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mf";
                                    } else {
                                        fn = fn + "ff";
                                    }
                            }
                        } else if (this.theData.age >= 18) {
                            if (ModSimReloaded.isDayTime()) {
                                if (sf == 1) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "daymone";
                                    } else {
                                        fn = fn + "dayfone";
                                    }
                                } else if (sf == 2) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "daymtwo";
                                    } else {
                                        fn = fn + "dayftwo";
                                    }
                                } else if (sf == 3) {
                                    if (!this.worldObj.isRaining()) {
                                        if (this.theData.gender == 0) {
                                            fn = fn + "daymthree";
                                        } else {
                                            fn = fn + "dayfthree";
                                        }
                                    } else if (this.theData.gender == 0) {
                                        fn = fn + "mwxbad";
                                    } else {
                                        fn = fn + "fwxbad";
                                    }
                                } else if (sf == 4) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "meight";
                                    } else {
                                        fn = fn + "feight";
                                    }
                                } else if (sf == 5) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mnine";
                                    } else {
                                        fn = fn + "fnine";
                                    }
                                } else if (sf == 6) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mseven";
                                    } else {
                                        fn = fn + "fseven";
                                    }
                                } else if (sf > 6) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mspeak";
                                    } else {
                                        fn = fn + "fspeak";
                                    }

                                    fn = fn + Character.toString((char) (sf + 90));
                                }
                            } else if (sf == 1) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "nightmone";
                                } else {
                                    fn = fn + "nightfone";
                                }
                            } else if (sf == 2) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "nightmtwo";
                                } else {
                                    fn = fn + "nightftwo";
                                }
                            } else if (sf == 3) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "nightmthree";
                                } else {
                                    fn = fn + "nightfthree";
                                }
                            } else if (sf == 4) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "meight";
                                } else {
                                    fn = fn + "feight";
                                }
                            } else if (sf == 5) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "mnine";
                                } else {
                                    fn = fn + "fnine";
                                }
                            } else if (sf == 6) {
                                if (this.theData.gender == 0) {
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
                            try {
                                ModSim.proxy.getClientWorld().playSound(this.posX, this.posY, this.posZ, fn, 1.0F, 1.0F, false);
                            } catch (Exception var12) {
                                //log.error("错误"+var12.getMessage());
                            }
                        }
                    }
                }

                try {
                    if (this.theData.levelFood < 0) {
                        this.onDeath(DamageSource.starve);
                    }
                } catch (Exception var11) {
                    //log.error("错误"+var11.getMessage());
                }

                this.greetTimer = System.currentTimeMillis();
            }
        }

        List list1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1.0, this.posY + 1.0, this.posZ + 1.0).expand(2.0, 4.0, 2.0));
        Iterator iterator1 = list1.iterator();
        if (!list1.isEmpty()) {
            for (Object entity : list1) {
                Entity entity1 = (Entity) entity;
                if (entity1 instanceof EntityItem) {
                    EntityItem entityitem = (EntityItem) entity1;
                    ItemStack is = entityitem.getEntityItem();

                    try {
                        ItemFood food = (ItemFood) is.getItem();
                        if (this.theData.levelFood < 10 && food != null) {
                            this.worldObj.playSoundAtEntity(this, "random.burp", 1.0F, 1.0F);
                            entityitem.setDead();
                            ++this.theData.levelFood;
                        }
                    } catch (Exception var10) {
                        //log.error("错误"+var10.getMessage());
                    }
                } else if (entity1 instanceof EntityFolk && (int) this.posX == (int) entity1.posX && (int) this.posZ == (int) entity1.posZ) {
                    this.motionX += 0.10000000149011612D;

                    try {
                        this.theData.stayPut = false;
                    } catch (Exception var9) {
                        //log.error("错误"+var9.getMessage());
                    }
                }
            }
        }

        try {
            super.onUpdate();
        } catch (Exception var8) {
            //log.error("错误"+var8.getMessage());
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
    public void moveEntity(double d, double d1, double d2) {
        if (!this.isDead && this.theData != null) {
            double dist = 0;
            if (this.theData.destination != null && this.theData.beamingTo == null) {
                try {
                    dist = this.getDistance(this.theData.destination.x, this.theData.destination.y, this.theData.destination.z);
                } catch (Exception var14) {
                    ModSimReloaded.log.warn("人们 theData.destination 中的目标为空 moveEntity()");
                    return;
                }

                if (dist <= 2.0) {
                    try {
                        //ModSimReloaded.log.info("实体人: " + this.theData.name + " 已经到达 " + this.theData.destination.toString() + " Dim:" + this.theData.destination.theDimension);
                    } catch (Exception var13) {
                        //log.error("错误"+var13.getMessage());
                    }

                    this.theData.updateLocationFromEntity();
                    this.motionX = 0;
                    this.motionZ = 0;
                    this.theData.stayPut = true;
                    this.theData.destination = null;
                    this.getNavigator().clearPathEntity();
                    this.gotPath = false;
                    if (this.theData.actionArrival != null) {
                        this.theData.action = this.theData.actionArrival;
                        this.theData.actionArrival = null;
                    }
                } else {
                    try {
                        if (!this.gotPath) {
                            PathEntity path = this.getNavigator().getPathToXYZ(this.theData.destination.x.intValue(), this.theData.destination.y.intValue(), this.theData.destination.z.intValue());
                            //PathEntity path = this.worldObj.getEntityPathToXYZ(this, this.theData.destination.x.intValue(), this.theData.destination.y.intValue(), this.theData.destination.z.intValue(), 40.0F, true, true, true, true);
                            if (path != null) {
                                this.getNavigator().setPath(path, 0.30000001192092896D);
                                this.gotPath = true;
                            }
                        }
                    } catch (Exception var12) {
                        // log.error("错误"+var12.getMessage());
                    }
                }

                boolean donttimeout = false;

                try {
                    donttimeout = this.theData.destination.doNotTimeout;
                } catch (Exception var11) {
                    //log.error("错误"+var11.getMessage());
                }

                if (this.theData.timeStartedGotoing != null && !donttimeout && System.currentTimeMillis() - this.theData.timeStartedGotoing > 40000L && this.theData.beamingTo == null) {
                    this.getNavigator().clearPathEntity();
                    if (dist > 2.0) {
                        ModSimReloaded.log.info("实体人: " + this.theData.name + " 散步太久，所以喜气洋洋...");
                        this.theData.stayPut = true;
                        this.theData.timeStartedGotoing = System.currentTimeMillis();
                        this.theData.beamMeTo(this.theData.destination);
                    }
                }
            }

            if (this.theData.stayPut) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                this.getNavigator().clearPathEntity();
            } else {
                super.moveEntity(d, d1, d2);
            }

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
        if (this.theData == null) {
            return null;
        } else if (this.theData.theirJob == null) {
            return null;
        } else if (this.theData.vocation == Vocation.CROPFARMER) {
            //农民
            return new ItemStack(ItemLoader.tinHoe, 1);
        } else if (this.theData.vocation == Vocation.LUMBERJACK) {
            //伐木工人
            return new ItemStack(ItemLoader.tinAxe, 1);
        } else if (this.theData.vocation == Vocation.MINER) {
            //矿工
            return new ItemStack(ItemLoader.tinPickaxe, 1);
        } else if (this.theData.vocation == Vocation.BAKER) {
            //面包师
            return new ItemStack(Items.wooden_shovel, 1);
        } else if (this.theData.vocation == Vocation.SOLDIER) {
            //战士
            return new ItemStack(ItemLoader.tinSword, 1);
        } else if (this.theData.vocation == Vocation.BUILDER) {
            //建筑者
            return new ItemStack(Blocks.cobblestone, 1);
        } else if (this.theData.vocation == Vocation.SHEPHERD) {
            //牧羊人
            return new ItemStack(Items.shears, 1);
        } else if (this.theData.vocation == Vocation.GROCER) {
            //杂货商
            return new ItemStack(Items.melon, 1);
        } else if (this.theData.vocation == Vocation.COURIER) {
            //快递员
            return new ItemStack(Blocks.chest, 1);
        } else if (this.theData.vocation == Vocation.MERCHANT) {
            //建筑商
            return new ItemStack(Blocks.brick_block, 1);
        } else if (this.theData.vocation == Vocation.BUTCHER) {
            //屠夫
            return new ItemStack(Items.porkchop, 1);
        } else if (this.theData.vocation == Vocation.CATTLEFARMER) {
            //养牛户
            return new ItemStack(ItemLoader.tinAxe, 1);
        } else if (this.theData.vocation == Vocation.PIGFARMER) {
            //养猪户
            return new ItemStack(ItemLoader.tinAxe, 1);
        } else if (this.theData.vocation == Vocation.CHICKENFARMER) {
            //养鸡户
            return new ItemStack(ItemLoader.tinAxe, 1);
        } else if (this.theData.vocation == Vocation.TERRAFORMER) {
            //地形成型机
            return new ItemStack(Items.diamond_shovel, 1);
        } else if (this.theData.vocation == Vocation.GLASSMAKER) {
            //玻璃工人
            return new ItemStack(Blocks.glass_pane, 1);
        } else if (this.theData.vocation == Vocation.DAIRYFARMER) {
            //牛奶农
            return new ItemStack(Items.milk_bucket, 1);
        } else if (this.theData.vocation == Vocation.CHEESEMAKER) {
            //奶酪匠
            return new ItemStack(ItemLoader.itemCheese, 1, 0);
        } else if (this.theData.vocation == Vocation.BURGERSMANAGER) {
            //汉堡经理
            return new ItemStack(ItemLoader.itemCheeseburger, 1, 3);
        } else if (this.theData.vocation == Vocation.BURGERSFRYCOOK) {
            //后厨
            return new ItemStack(Items.iron_shovel, 1);
        } else if (this.theData.vocation == Vocation.BURGERSWAITER) {
            //汉堡服务员
            return new ItemStack(ItemLoader.itemFries, 1, 2);
        } else if (this.theData.vocation == Vocation.FISHERMAN) {
            //职业渔夫
            JobFisherman jf = (JobFisherman) this.theData.theirJob;
            return jf.theStage == Stage.IDLE ? new ItemStack(Items.fish, 1) : new ItemStack(Items.fishing_rod, 1);
        } else {
            return null;
        }
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
        Minecraft mc = Minecraft.getMinecraft();
        mc.currentScreen = null;
        GuiScreen ui = null;
        if (this.theData == null) {
            this.setDead();
            return false;
        } else {
            if (this.theData.theirJob != null) {
                if (this.theData.vocation == Vocation.MERCHANT && ModSimReloaded.isDayTime()) {
                    ui = new GuiMerchant();
                } else {
                    ui = new GuiEntityFolk(this.theData, entityplayer);
                }
            } else {
                ui = new GuiEntityFolk(this.theData, entityplayer);
            }

            mc.displayGuiScreen((GuiScreen) ui);
            if (this.theData.age < 18) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, ModSim.MODID + ":helloc", 1.0F, 1.0F, false);
            } else if (this.theData.gender == 0) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, ModSim.MODID + ":hellom", 1.0F, 1.0F, false);
            } else {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, ModSim.MODID + ":hellof", 1.0F, 1.0F, false);
            }

            return true;
        }
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
        this.theData.eventDied(d);
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
        if (!this.isBurning()) {
            this.heal(10.0F);
            if (this.theData != null && this.theData.stayPut) {
                this.theData.stayPut = false;
            }
            BlockPos blockPos1 = new BlockPos(this.posX + 1, this.posY, this.posZ);
            BlockPos blockPos2 = new BlockPos(this.posX - 1, this.posY, this.posZ);
            BlockPos blockPos3 = new BlockPos(this.posX, this.posY, this.posZ + 1);
            BlockPos blockPos4 = new BlockPos(this.posX + 1, this.posY, this.posZ - 1);
            Block idX1 = this.worldObj.getBlockState(blockPos1).getBlock();
            Block idX2 = this.worldObj.getBlockState(blockPos2).getBlock();
            Block idZ1 = this.worldObj.getBlockState(blockPos3).getBlock();
            Block idZ2 = this.worldObj.getBlockState(blockPos4).getBlock();
            this.motionY += 0.4D;
            if (idX1 == null) {
                this.motionX += 0.8999999761581421;
            } else if (idX2 == null) {
                this.motionX -= 0.8999999761581421;
            } else if (idZ1 == null) {
                this.motionZ += 0.8999999761581421;
            } else if (idZ2 == null) {
                this.motionZ -= 0.8999999761581421;
            }
        }

        if (this.theData == null) {
            return null;
        } else if (ConfigLoader.configFolkTalking) {
            if (System.currentTimeMillis() - this.lastHurt > 10000L) {
                this.lastHurt = System.currentTimeMillis();
                return this.theData.gender == 0 ? ModSim.MODID + ":OuchM" : ModSim.MODID + ":OuchF";
            } else {
                return null;
            }
        } else {
            return null;
        }
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

    //@Override
    //public AxisAlignedBB getCollisionBox(Entity par1Entity) {
    //    return par1Entity.getCollisionBox(par1Entity);
    //}

    //@Override
    //public AxisAlignedBB getBoundingBox() {
    //    return this.boundingBox;
    //}

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
