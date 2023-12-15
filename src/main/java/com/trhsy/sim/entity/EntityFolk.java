package com.trhsy.sim.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.ai.FolkAIOpenFenceGate;
import com.trhsy.sim.entity.ai.FolkAIWander;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.loader.render.RenderEntityFolk;
import com.trhsy.sim.network.client.*;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.NpcIdentity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.*;
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
public class EntityFolk extends EntityCreature implements INpc {
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
    public RenderEntityFolk renderEntityFolk;
    long secondTimer = 0L;
    /**
     * 是否为已创建
     **/
    public boolean isBeingCreated = false;
    public EntityFolk(World worldIn) {
        super(worldIn);
        try {

            //会捡起地上的东西
            this.setCanPickUpLoot(true);
            //会进门
            ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
            //破门而入
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
            //会游泳
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
            this.setSize(0.6F, 1.8F);
            this.enablePersistence();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EntityFolk1出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 已创建
     * @param world
     * @param isCreating
     */
    public EntityFolk(World world, boolean isCreating) {
        super(world);

        try {
            //会捡起地上的东西
            this.setCanPickUpLoot(true);
            //会进门
            ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
            //破门而入
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
            //会游泳
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
            this.isBeingCreated = isCreating;
            this.setSize(0.6F, 1.8F);
            this.enablePersistence();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EntityFolk2出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 根据Uid加载
     * @param world
     * @param id
     */
    public EntityFolk(World world, String id) {
        super(world);

        try {
            this.setUniqueId(UUID.fromString(id));
            //会捡起地上的东西
            this.setCanPickUpLoot(true);
            //会进门
            ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
            //破门而入
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
            //会游泳
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
            this.isBeingCreated = false;
            this.setSize(0.6F, 1.8F);
            this.enablePersistence();
        }catch (Exception e){
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
            //智能游泳
            this.tasks.addTask(0, new EntityAISwimming(this));
            //限制打开门
            this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
            //开门
            this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
            //打开栅栏门
            this.tasks.addTask(5, new FolkAIOpenFenceGate(this, true));
            //自由闲逛
            this.tasks.addTask(6, new FolkAIWander(this, 1.0D));
            //最近观看
            this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
            this.tasks.addTask(7, new EntityAIWatchClosest2(this, EntityPlayer.class, 8.0F, 1));
            //看起来很空闲
            this.tasks.addTask(8, new EntityAILookIdle(this));
            //住进屋子
            this.tasks.addTask(9, new EntityAIMoveIndoors(this));
            //限制走向
            this.tasks.addTask(12, new EntityAIMoveTowardsRestriction(this, 0.3D));
            //避开实体僵尸
            this.tasks.addTask(13, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
            //受到伤害会跑
            this.tasks.addTask(13,new EntityAIPanic(this,0.1));

        }catch (Exception e){
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
        }catch (Exception e){
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
                List<Entity> list1 = this.world.getEntitiesWithinAABBExcludingEntity(this, (new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D)).expand(2.0D, 4.0D, 2.0D));
                for (Entity entity1 : list1) {
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem) entity1;
                        ItemStack is = entityitem.getItem();

                        try {
                            Item item = is.getItem();
                            ModSimLoader.log.info("NPC手持物品：" + item.getUnlocalizedName());
                            if (item instanceof ItemFood) {
                                ModSimLoader.log.info("NPC手持物品：" + item.getUnlocalizedName()+",为食物。");
                                //如果手里拿的是食物就，并且饿了就吃了
                                if (this.theData != null && this.theData.hunger < 10 && item != null) {
                                    ModSimLoader.log.info("NPC手持物品：" + item.getUnlocalizedName()+",为食物，并且当前饥饿值为："+this.theData.hunger);
                                    entityitem.setDead();
                                    ++this.theData.hunger;
                                    ModSimLoader.log.info("NPC吃东西了，饥饿值："+this.theData.hunger);
                                }
                            }
                        } catch (Exception var8) {
                            ModSimLoader.log.error("Npc 吃东西出错了：" + var8.getMessage());
                        }
                    } else if (entity1 instanceof EntityFolk && (int) this.posX == (int) entity1.posX && (int) this.posZ == (int) entity1.posZ) {
                        this.motionX += 0.10000000149011612D;

                        try {
                            this.theData.stayPut = false;
                        } catch (Exception var7) {
                        }
                    }
                }
            }
            if (this.theData != null && !this.world.isRemote && !ModSimLoader.folks.contains(this.theData)) {
                ModSimLoader.folks.add(this.theData);
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
                EntityFolk e = ModSimLoader.getFolkDataByUID(this.getUniqueID().toString()).entity;

                if (e != null && this.theData != null) {
                    if (this.theData.entity != null && !this.theData.entity.equals(this)) {
                        ModSimLoader.log.info("获得重复的民间实体");
                        this.setDead();
                    }

                    if (!e.equals(this)) {
                    }
                }
            }

            if (this.theData == null && !this.isBeingCreated && !this.world.isRemote) {
                this.theData = ModSimLoader.getFolkDataByUID(this.getUniqueID().toString());
                this.theData.entity = this;
                this.theData.sendSkinPathToClient();
            }
        }catch (Exception e){
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
            }else{
                boolean fs_flog=true;
                for (NpcData npcData : ModSimLoader.folks) {
                    if (npcData.ID.contentEquals(this.getUniqueID().toString())) {
                        npcData.onDeath(cause);
                        fs_flog=false;
                    }
                }
                if(fs_flog){
                    this.onKillEntity(this);
                }
            }
            super.onDeath(cause);
            this.setDead();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("onDeath出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                if(this.theData == null){
                    this.theData = ModSimLoader.getFolkDataByUID(this.getUniqueID().toString());
                }
                if (this.theData != null) {
                    if(this.theData.job!=null){
                        String jobName=this.theData.job.jobName;
                        //工作是建筑商
                        if(jobName.equals(new TextComponentTranslation("container.sim.Vocation11",new Object[0]).getUnformattedText())&&this.theData.isAtLocation(this.theData.job.workPlace)){
                            //打开建筑商gui
                            NetWorkLoader.net.sendTo(new PacketOpenMerchantGui(this.theData), (EntityPlayerMP) player);
                            //行长
                        }else if(jobName.equals(new TextComponentTranslation("container.sim.Vocation31",new Object[0]).getUnformattedText())&&this.theData.isAtLocation(this.theData.job.workPlace)){
                            //打开建银行gui
                            NetWorkLoader.net.sendTo(new PacketOpenBankATMGui(this.theData), (EntityPlayerMP) player);
                        }else if(jobName.equals(new TextComponentTranslation("container.sim.Vocation9",new Object[0]).getUnformattedText())&&this.theData.isAtLocation(this.theData.job.workPlace)){
                            //打开建杂货商gui
                            NetWorkLoader.net.sendTo(new PacketOpenMerchantsGui(this.theData), (EntityPlayerMP) player);
                        }else if(jobName.equals(new TextComponentTranslation("container.sim.Vocation32",new Object[0]).getUnformattedText())&&this.theData.isAtLocation(this.theData.job.workPlace)){
                            //打开建插花师gui
                            NetWorkLoader.net.sendTo(new PacketOpenFlowerGui(this.theData), (EntityPlayerMP) player);
                        }else{
                            NetWorkLoader.net.sendTo(new PacketOpenFolkGui(this.theData), (EntityPlayerMP) player);
                        }
                    }else{
                        NetWorkLoader.net.sendTo(new PacketOpenFolkGui(this.theData), (EntityPlayerMP) player);
                    }
                    //互动的时候说哈喽
                    if (this.theData.age < 18) {
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":helloc"));
                        Minecraft mc = Minecraft.getMinecraft();
                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                            mc.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                        }
                    } else if (this.theData.gender == 0) {
                        //女声
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":hellom"));
                        Minecraft mc = Minecraft.getMinecraft();
                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                            mc.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                        }
                    } else {
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":hellof"));
                        Minecraft mc = Minecraft.getMinecraft();
                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                            mc.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                        }
                    }
                }else{
                    return false;
                }
            }

            return true;
        }catch (Exception e){
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
            ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketAnimation(this,3));
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("swing出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("isChild出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return false;
        }
    }
}
