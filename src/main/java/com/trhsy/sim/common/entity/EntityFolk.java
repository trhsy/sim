package com.trhsy.sim.common.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.client.gui.GuiEntityFolk;
import com.trhsy.sim.client.gui.GuiMerchant;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.jobs.JobFisherman;
import com.trhsy.sim.common.jobs.Stage;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ItemLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.*;

/**
 * ========================================
 *
 * @ClassName EntityFolk
 * @Description todo 实体人
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:53
 * ========================================
 **/
public class EntityFolk extends EntityCreature implements INpc {

    public FolkData theData = null;
    //记忆计时器
    private long ghostTimer = -1L;
    //问候计时器
    private long greetTimer = 0L;
    //最后一次受伤
    private long lastHurt = 0L;
    //日历
    private Calendar cal = new GregorianCalendar();
    //圣诞节
    private boolean isXmas = false;
    //找到路了吗
    public boolean gotPath;

    public EntityFolk(World par1World) {
        super(par1World);
        //避开水
        this.getNavigator().setAvoidsWater(false);
        //会进门
        this.getNavigator().setEnterDoors(true);
        //破门而入
        this.getNavigator().setBreakDoors(true);
        //会游泳
        this.getNavigator().setCanSwim(true);
        //实体人任务
        //闲置任务
        this.tasks.addTask(1, new EntityAILookIdle(this));
        //住进屋子
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        //限制开门
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        //密切注意
        this.tasks.addTask(10, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        //密切注意
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        //开门
        this.tasks.addTask(9, new EntityAIOpenDoor(this, true));
        //走向限制
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.3D));
        //游泳
        this.tasks.addTask(4, new EntityAISwimming(this));
        //启动
        if (!ModSim.proxy.ranStartup) {
            ModSim.log.info("实体人：被杀死的系统产生的人");
            this.setDead();
        }

        int dom = this.cal.get(5);
        int moy = this.cal.get(2);
        if (dom > 23 && dom < 27 && moy == 11) {
            this.isXmas = true;
        }

    }

    /**
     * @return java.lang.String
     * @Author fan
     * @Description //TODO 获取纹理
     * @Date 10:24 2022/3/20
     * @Param []
     **/
    @SideOnly(Side.CLIENT)
    public String getTexture() {
        if (this.theData != null) {
            if (this.theData.gender == 0) {
                return "male" + this.theData.skinnumber + ".png";
            } else {
                return "female" + this.theData.skinnumber + ".png";
            }
        } else {
            return "male1.png";
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 应用实体属性
     * @Date 10:24 2022/3/20
     * @Param []
     **/
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        //共享怪物属性 移动速度
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0D);
    }

    @Override
    public void onUpdate() {
        if (this.theData == null) {
            if (!this.isDead) {
                if (this.ghostTimer == -1L) {
                    this.ghostTimer = System.currentTimeMillis();
                }

                this.theData = FolkData.getFolkDataByEntityId(this.getEntityId());
                if (this.theData == null && System.currentTimeMillis() - this.ghostTimer > 5000L) {
                    ModSim.log.info("实体人: " + this.getEntityId() + " - 他们的数据已经空了5秒多，所以");
                    this.setDead();
                }
            }
        } else {
            if (this.theData.isWorking) {
                float s = (float) (Math.sin((double) System.currentTimeMillis() * 0.01D) / 10.0D) + 0.1F;
                this.swingProgress = s;
            } else {
                this.swingProgress = 0.0F;
            }

            if (System.currentTimeMillis() - this.greetTimer > 1000L) {
                Random r = new Random();
                double dist = (double) this.theData.getDistanceToPlayer();
                if (ModSim.states != null) {
                    long var10000 = System.currentTimeMillis();
                    FolkData var10001 = this.theData;
                    Long ls = var10000 - FolkData.anyFolkLastSpoke;
                    if (ConfigLoader.configFolkTalkingEnglish && ls > 5000L && (!this.theData.greetedToday & dist < 5.0D || this.theData.vocation == Vocation.BURGERSWAITER && dist < 5.0D && r.nextInt(20) == 2)) {
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
                            if (ModSim.isDayTime()) {
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
                            }
                        }
                    }
                }

                try {
                    if (this.theData.levelFood < 0) {
                        this.onDeath(DamageSource.starve);
                    }
                } catch (Exception var11) {
                }

                this.greetTimer = System.currentTimeMillis();
            }
        }

        List list1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(2.0D, 4.0D, 2.0D));
        Iterator iterator1 = list1.iterator();
        if (!list1.isEmpty()) {
            while (iterator1.hasNext()) {
                Entity entity1 = (Entity) iterator1.next();
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
                    }
                } else if (entity1 instanceof EntityFolk && (int) this.posX == (int) entity1.posX && (int) this.posZ == (int) entity1.posZ) {
                    this.motionX += 0.10000000149011612D;

                    try {
                        this.theData.stayPut = false;
                    } catch (Exception var9) {
                    }
                }
            }
        }

        try {
            super.onUpdate();
        } catch (Exception var8) {
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 移动实体
     * @Date 10:25 2022/3/20
     * @Param [d, d1, d2]
     **/
    @Override
    public void moveEntity(double d, double d1, double d2) {
        if (!this.isDead && this.theData != null) {
            double dist = 0.0D;
            if (this.theData.destination != null && this.theData.beamingTo == null) {
                try {
                    dist = this.getDistance(this.theData.destination.x, this.theData.destination.y, this.theData.destination.z);
                } catch (Exception var14) {
                    ModSim.log.warn("人们 theData.destination 中的目标为空 moveEntity()");
                    return;
                }

                if (dist <= 2.0D) {
                    try {
                        ModSim.log.info("实体人: " + this.theData.name + " 已经到达 " + this.theData.destination.toString() + " Dim:" + this.theData.destination.theDimension);
                    } catch (Exception var13) {
                    }

                    this.theData.updateLocationFromEntity();
                    this.motionX = 0.0D;
                    this.motionZ = 0.0D;
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
                            PathEntity path = this.worldObj.getEntityPathToXYZ(this, this.theData.destination.x.intValue(), this.theData.destination.y.intValue(), this.theData.destination.z.intValue(), 40.0F, true, true, true, true);
                            if (path != null) {
                                this.getNavigator().setPath(path, 0.30000001192092896D);
                                this.gotPath = true;
                            }
                        }
                    } catch (Exception var12) {
                    }
                }

                boolean donttimeout = false;

                try {
                    donttimeout = this.theData.destination.doNotTimeout;
                } catch (Exception var11) {
                }

                if (this.theData.timeStartedGotoing != null && !donttimeout && System.currentTimeMillis() - this.theData.timeStartedGotoing > 40000L && this.theData.beamingTo == null) {
                    this.getNavigator().clearPathEntity();
                    if (dist > 2.0D) {
                        ModSim.log.info("实体人: " + this.theData.name + " 散步太久，所以喜气洋洋...");
                        this.theData.stayPut = true;
                        this.theData.timeStartedGotoing = System.currentTimeMillis();
                        this.theData.beamMeTo(this.theData.destination);
                    }
                }
            }

            if (this.theData.stayPut) {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                this.getNavigator().clearPathEntity();
            } else {
                super.moveEntity(d, d1, d2);
            }

        }
    }

    /**
     * @return net.minecraft.item.ItemStack
     * @Author fan
     * @Description //TODO 保留项目
     * @Date 10:26 2022/3/20
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
            return new ItemStack(Items.stone_hoe, 1);
        } else if (this.theData.vocation == Vocation.LUMBERJACK) {
            //伐木工人
            return new ItemStack(Items.stone_axe, 1);
        } else if (this.theData.vocation == Vocation.MINER) {
            //矿工
            return new ItemStack(Items.stone_pickaxe, 1);
        } else if (this.theData.vocation == Vocation.BAKER) {
            //面包师
            return new ItemStack(Items.wooden_shovel, 1);
        } else if (this.theData.vocation == Vocation.SOLDIER) {
            //战士
            return new ItemStack(Items.stone_sword, 1);
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
            return new ItemStack(Items.golden_axe, 1);
        } else if (this.theData.vocation == Vocation.PIGFARMER) {
            //养猪户
            return new ItemStack(Items.iron_axe, 1);
        } else if (this.theData.vocation == Vocation.CHICKENFARMER) {
            //养鸡户
            return new ItemStack(Items.stone_axe, 1);
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
            return new ItemStack(ItemLoader.itemFoods, 1, 0);
        } else if (this.theData.vocation == Vocation.BURGERSMANAGER) {
            //汉堡经理
            return new ItemStack(ItemLoader.itemFoods, 1, 3);
        } else if (this.theData.vocation == Vocation.BURGERSFRYCOOK) {
            //后厨
            return new ItemStack(Items.iron_shovel, 1);
        } else if (this.theData.vocation == Vocation.BURGERSWAITER) {
            //汉堡服务员
            return new ItemStack(ItemLoader.itemFoods, 1, 2);
        } else if (this.theData.vocation == Vocation.FISHERMAN) {
            //职业渔夫
            JobFisherman jf = (JobFisherman) this.theData.theirJob;
            return jf.theStage == Stage.IDLE ? new ItemStack(Items.fish, 1) : new ItemStack(Items.fishing_rod, 1);
        } else {
            return null;
        }
    }
    /**
     * @Author fan
     * @Description //TODO 互动
     * @Date 10:35 2022/3/20
     * @Param [entityplayer]
     * @return boolean
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
                if (this.theData.vocation == Vocation.MERCHANT && ModSim.isDayTime()) {
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

    @Override
    public void onDeath(DamageSource d) {
        this.theData.eventDied(d);
    }

    @Override
    public boolean canBePushed() {
        return true;
    }
    /**
     * @Author fan
     * @Description //TODO 受伤的声音
     * @Date 11:04 2022/3/20
     * @Param []
     * @return java.lang.String
     **/
    @Override
    protected String getHurtSound() {
        if (!this.isBurning()) {
            this.heal(10.0F);
            if (this.theData != null && this.theData.stayPut) {
                this.theData.stayPut = false;
            }

            Block idX1 = this.worldObj.getBlock((int) this.posX + 1, (int) this.posY, (int) this.posZ);
            Block idX2 = this.worldObj.getBlock((int) this.posX - 1, (int) this.posY, (int) this.posZ);
            Block idZ1 = this.worldObj.getBlock((int) this.posX, (int) this.posY, (int) this.posZ + 1);
            Block idZ2 = this.worldObj.getBlock((int) this.posX + 1, (int) this.posY, (int) this.posZ - 1);
            this.motionY += 0.4D;
            if (idX1 == null) {
                this.motionX += 0.8999999761581421D;
            } else if (idX2 == null) {
                this.motionX -= 0.8999999761581421D;
            } else if (idZ1 == null) {
                this.motionZ += 0.8999999761581421D;
            } else if (idZ2 == null) {
                this.motionZ -= 0.8999999761581421D;
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

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 200;
    }

    @Override
    public boolean canDespawn() {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity) {
        return par1Entity.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public void onPlayerLogin(EntityPlayer player) {
    }

    public void onPlayerLogout(EntityPlayer player) {
    }

    public void onPlayerChangedDimension(EntityPlayer player) {
    }

    public void onPlayerRespawn(EntityPlayer player) {
    }
}
