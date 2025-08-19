package com.trhsy.sim.entity;

import com.trhsy.sim.entity.ai.FolkAIOpenFenceGate;
import com.trhsy.sim.entity.ai.FolkAIWander;
import com.trhsy.sim.entity.container.ContainerNpc;
import com.trhsy.sim.entity.container.InventoryNpc;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.loader.SoundRegistry;
import com.trhsy.sim.network.client.*;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.NpcIdentity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity
 * @ClassName: EntityFolk
 * @Description: 实体npc
 * @date 2023/11/20 下午 3:57
 */
public class EntityNpc extends EntityCreature implements INpc {
    /**
     * 上次更新状态时间
     **/
    private transient long timeSinceLastStatusUpdate = 0L;
    /**
     * 更新状态分钟
     **/
    private transient long timeSinceLastMinute = 0L;
    /**
     * NPC数据
     **/
    public NpcData theData;
    long secondTimer = 0L;
    /**
     * 是否为已创建
     **/
    public boolean isBeingCreated = false;
    /**
     * 物品栏
     */
    public Container inventoryContainer;

    public InventoryNpc inventory = new InventoryNpc(this);

    private boolean aiInitialized =false;//新增：AI初始化标志

    public EntityNpc(World worldIn) {
        super(worldIn);
        this.inventoryContainer = new ContainerNpc(this.inventory, !worldIn.isRemote, this);
        try {

            //会捡起地上的东西
            this.setCanPickUpLoot(false);
            //会进门
            ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
            //破门而入
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
            //会游泳
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
            this.setSize(0.4F, 1.8F);
            this.enablePersistence();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EntityFolk1出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 已创建
     *
     * @param world
     * @param isCreating
     */
    public EntityNpc(World world, boolean isCreating) {
        super(world);
        this.inventoryContainer = new ContainerNpc(this.inventory, !world.isRemote, this);
        try {
            //会捡起地上的东西
            this.setCanPickUpLoot(false);
            //会进门
            ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
            //破门而入
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
            //会游泳
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
            this.isBeingCreated = isCreating;
            this.setSize(0.4F, 1.8F);
            this.enablePersistence();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EntityFolk2出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 根据Uid加载
     *
     * @param world
     * @param id
     */
    public EntityNpc(World world, UUID id) {
        super(world);
        this.inventoryContainer = new ContainerNpc(this.inventory, !world.isRemote, this);
        try {
            this.setUniqueId(id);
            //会捡起地上的东西
            this.setCanPickUpLoot(false);
            //会进门
            ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
            //破门而入
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
            //会游泳
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
            this.isBeingCreated = false;
            this.setSize(0.4F, 1.8F);
            this.enablePersistence();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EntityFolk3出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /***
     * 初始化 实体 ai
     */
    @Override
    public void initEntityAI() {
        try {
            if (theData == null) {
            return;
            }
                //智能游泳
                this.tasks.addTask(0, new EntityAISwimming(this));

                //开门
                this.tasks.addTask(1, new EntityAIOpenDoor(this, true));
                //限制打开门
                this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
                //打开栅栏门
                this.tasks.addTask(3, new FolkAIOpenFenceGate(this, true));

                //自由闲逛
                this.tasks.addTask(6, new FolkAIWander(this, 1.0D));
                //最近观看
//            this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 1.0F));
                this.tasks.addTask(7, new EntityAIWatchClosest2(this, EntityPlayer.class, 1.0F, 1));
                //看起来很空闲
                this.tasks.addTask(8, new EntityAILookIdle(this));

                //住进屋子
                this.tasks.addTask(2, new EntityAIMoveIndoors(this));
                //限制走向
//            this.tasks.addTask(12, new EntityAIMoveTowardsRestriction(this, 0.3D));
                //避开实体僵尸
//            this.tasks.addTask(13, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
                //受到伤害会跑
                this.tasks.addTask(0, new EntityAIPanic(this, 1.5));
                this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 1.0F));
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("initEntityAI出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 应用实体属性
     */
    @Override
    protected void applyEntityAttributes() {
        try {
            super.applyEntityAttributes();
            //最大生命 40
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
            //移动_速度
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
            //跟随范围
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(256.0D);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("applyEntityAttributes出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 能否重生
     * @Date 15:18 2022/10/18
     * @Param []
     **/
    @Override
    protected boolean canDespawn() {
        return true;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 实体更新
     * @Date 15:18 2022/10/18
     * @Param []
     **/
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (System.currentTimeMillis() - this.secondTimer > 1000L) {
                this.secondTimer = System.currentTimeMillis();
                //获取NPC旁边的所有实体
                List<Entity> list1 = this.world.getEntitiesWithinAABBExcludingEntity(this, (new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D)).expand(2.0D, 4.0D, 2.0D));
                for (Entity entity1 : list1) {
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem) entity1;
                        ItemStack is = entityitem.getItem();

                        try {
                            Item item = is.getItem();
//                            ModSimLoader.log.info("NPC手持物品：" + item.getUnlocalizedName());
                            if (item instanceof ItemFood) {
//                                ModSimLoader.log.info("NPC手持物品：" + item.getUnlocalizedName()+",为食物。");
                                //如果手里拿的是食物就，并且饿了就吃了
                                if (this.theData != null && this.theData.hunger < 10 && item != null) {
                                    ModSimLoader.log.info("NPC手持物品：" + item.getUnlocalizedName() + ",为食物，并且当前饥饿值为：" + this.theData.hunger);
                                    ItemFood itemFood = (ItemFood) item;
                                    int healAmount = itemFood.getHealAmount(is);
                                    this.theData.hunger += healAmount;
                                    entityitem.setDead();
                                    ModSimLoader.log.info("NPC吃东西了，饥饿值：" + this.theData.hunger);
                                }
                            }
                        } catch (Exception var8) {
                            ModSimLoader.log.error("Npc 吃东西出错了：" + var8.getMessage());
                        }
                    } /*else if (entity1 instanceof EntityNpc && (int) this.posX == (int) entity1.posX && (int) this.posZ == (int) entity1.posZ) {
                        this.motionX += 0.10000000149011612D;

                        try {
                            this.theData.stayPut = false;
                        } catch (Exception var7) {
                        }
                    }*/
                }
                if (this.theData != null) {
                    if(!aiInitialized){
                        initEntityAI();
                        aiInitialized = true;
                    }
                    //获取设备
                    Iterable<ItemStack> itemStacks = this.getHeldEquipment();
                    for (ItemStack items : itemStacks) {
                        Item item = items.getItem();
                        if (item instanceof ItemFood) {
                            ItemFood itemFood = (ItemFood) item;
                            int healAmount = itemFood.getHealAmount(items);
                            this.theData.hunger += healAmount;
                            ModSimLoader.log.info("当前npc[" + this.theData.getName() + "]饱和度：" + this.theData.hunger);
                            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        }
                    }
                }
            }

            if (this.theData != null && !this.world.isRemote && !ModSimLoader.folks.contains(this.theData)) {
                ModSimLoader.folks.add(this.theData);
                NetWorkLoader.net.sendToAll(new PacketUpdateNPC());
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("onUpdate实体更新出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 当NPC更新时
     * @Date 10:40 2022/10/16
     * @Param []
     **/
    public void onFolkUpdate() {
        try {
            if (!this.world.isRemote) {

                if (this.theData != null) {
                    if (this.theData.entity != null && !this.theData.entity.equals(this)) {
                        ModSimLoader.log.info("获得重复的民间实体");
                        this.setDead();
                    }

                }
            }

            if (this.theData == null && !this.isBeingCreated && !this.world.isRemote) {
                this.theData = ModSimLoader.getFolkDataByUID(this.getUniqueID());
                this.theData.entity = this;
                this.theData.sendSkinPathToClient();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("onFolkUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 死亡
     * @Date 15:22 2022/10/18
     * @Param [cause]
     **/
    @Override
    public void onDeath(DamageSource cause) {
        try {
            if (this.theData != null) {
                this.theData.onDeath(cause);
            } else {
                // 2. 高效查找匹配的 NpcData（建议用 Map 存储优化查找）
                NpcData matchedData = null;
                for (NpcData npcData : ModSimLoader.folks) {
                    if (npcData.ID==this.getUniqueID()) {
                        npcData.onDeath(cause);
                        matchedData = npcData;
                        break; // 找到后立即退出循环，提高效率
                    }
                }
                if (matchedData != null) {
                    matchedData.onDeath(cause);
                } else {
                    // 3. 无匹配数据时，直接从列表中清理当前实体（替代不合理的 onKillEntity）
                    ModSimLoader.folks.removeIf(data -> data.ID.equals(this.getUniqueID()));
                    ModSimLoader.log.info("NPC 已死亡且无匹配数据，已从列表中清理: " + this.getUniqueID());
                }
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EtityNPC-onDeath出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }finally {
            // 5. 确保父类死亡逻辑执行，强制标记死亡状态（无论是否发生异常）
            super.onDeath(cause);
            this.setHealth(0);
            this.setDead();// 强制标记实体为死亡，避免残留
        }
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 交互
     * @Date 15:01 2022/10/18
     * @Param [player, hand]
     **/
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        try {
            if (!player.world.isRemote) {
                if (this.theData == null) {
                    this.theData = ModSimLoader.getFolkDataByUID(this.getUniqueID());
                }
                if (this.theData != null) {
                    if (this.theData.job != null) {
                        String jobName = this.theData.job.jobName;
                        //工作是建筑商
                        if (jobName.equals(new TextComponentTranslation("container.sim.Vocation11", new Object[0]).getUnformattedText()) && this.theData.isAtLocation(this.theData.job.workPlace, 5)) {
                            //打开建筑商gui
                            NetWorkLoader.net.sendTo(new PacketOpenMerchantGui(this.theData), (EntityPlayerMP) player);
                            //行长
                        } else if (jobName.equals(new TextComponentTranslation("container.sim.Vocation31", new Object[0]).getUnformattedText()) && this.theData.isAtLocation(this.theData.job.workPlace, 5)) {
                            //打开建银行gui
                            NetWorkLoader.net.sendTo(new PacketOpenBankATMGui(this.theData), (EntityPlayerMP) player);
                        } else if (jobName.equals(new TextComponentTranslation("container.sim.Vocation9", new Object[0]).getUnformattedText()) && this.theData.isAtLocation(this.theData.job.workPlace, 5)) {
                            //打开建杂货商gui
                            NetWorkLoader.net.sendTo(new PacketOpenMerchantsGui(this.theData), (EntityPlayerMP) player);
                        } else if (jobName.equals(new TextComponentTranslation("container.sim.Vocation32", new Object[0]).getUnformattedText()) && this.theData.isAtLocation(this.theData.job.workPlace)) {
                            //打开建插花师gui
                            NetWorkLoader.net.sendTo(new PacketOpenFlowerGui(this.theData), (EntityPlayerMP) player);
                        } else {
                            NetWorkLoader.net.sendTo(new PacketOpenFolkGui(this.theData), (EntityPlayerMP) player);
                        }
                    } else {
                        NetWorkLoader.net.sendTo(new PacketOpenFolkGui(this.theData), (EntityPlayerMP) player);
                    }
                    //互动的时候说哈喽
                    if (this.theData.age < 18) {
//                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":helloc"));
                        SoundEvent helloc = SoundRegistry.HELLOC;
                        if (helloc == null || helloc.getRegistryName() == null) {
                            ModSimLoader.log.error("播放失败：sim:windmill 声音事件未注册");
                        } else {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, helloc, SoundCategory.PLAYERS, 1.0F, 1.0F);}
                    } else if (this.theData.gender == 0) {
                        //女声
//                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":hellom"));
                        SoundEvent hellom = SoundRegistry.HELLOF;
                        if (hellom == null || hellom.getRegistryName() == null) {
                            ModSimLoader.log.error("播放失败：sim:hellom 声音事件未注册");
                        } else {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, hellom, SoundCategory.PLAYERS, 1.0F, 1.0F);}
                    } else {
//                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":hellof"));
                        SoundEvent hellof = SoundRegistry.HELLOM;
                            if (hellof == null || hellof.getRegistryName() == null) {
                                ModSimLoader.log.error("播放失败：sim:hellof 声音事件未注册");
                            } else {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, hellof, SoundCategory.PLAYERS, 1.0F, 1.0F);}
                    }
                } else {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("processInteract出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return false;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 摆动手臂
     * @Date 15:44 2022/10/18
     * @Param []
     **/
    public void swing() {
        try {
            ItemStack stack = this.theData.holding;
            if (!stack.isEmpty()) {
                if (stack.getItem().onEntitySwing(this, stack)) {
                    return;
                }
            }
            ((WorldServer) this.world).getEntityTracker().sendToTracking(this, new SPacketAnimation(this, 3));
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("swing出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    /**
     * 让实体挥动手臂，处理客户端和服务器端同步
     * @param entity 要挥动手臂的实体
     * @param hand 要挥动的手（主手或副手）
     * @param force 是否强制挥动（忽略冷却）
     */
    public static void swingEntityArm(EntityLivingBase entity, EnumHand hand, boolean force) {
        // 仅在服务器端处理（客户端会通过网络包同步）
        if (entity.world.isRemote) {
            entity.swingArm(hand);
            return;
        }
        // 挥动手臂
        entity.swingArm(hand);

        // 如果是主手挥动，并且需要同步到客户端
        if (hand == EnumHand.MAIN_HAND) {
            ((WorldServer)entity.world).getEntityTracker().sendToTracking(entity, new SPacketAnimation(entity, 0));
        }
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 移动
     * @Date 15:45 2022/10/18
     * @Param []
     **/
    public boolean isMoving() {
        return this.motionX > 0.0D || this.motionY > 0.0D || this.motionZ > 0.0D;
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 是儿童
     * @Date 15:46 2022/10/18
     * @Param []
     **/
    @Override
    public boolean isChild() {
        try {
            NpcIdentity cfi = ModSimClientLoader.getFolkByUUID(this.getUniqueID());
            if (cfi != null) {
                return Integer.parseInt(cfi.age) < Integer.parseInt(cfi.maturityAge);
            } else {
                return false;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("isChild出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return false;
        }
    }
}
