package com.trhsy.sim.entity;

import com.trhsy.sim.entity.ai.FolkAIOpenFenceGate;
import com.trhsy.sim.entity.ai.FolkAIWander;
import com.trhsy.sim.entity.render.RenderEntityFolk;
import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenFolkGui;
import com.trhsy.sim.npc.NpcData;
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
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: EntityFolk
 * @Description:
 * @date 2022/10/11 16:13
 */
public class EntityFolk extends EntityCreature implements INpc {
    /**上次更新状态时间**/
    private transient long timeSinceLastStatusUpdate = 0L;
    /**更新状态分钟**/
    private transient long timeSinceLastMinute = 0L;
    /**NPC数据**/
    public NpcData theData;
    public RenderEntityFolk renderEntityFolk;
    /**正在创建**/
    public boolean isBeingCreated = false;
    long secondTimer = 0L;
    public EntityFolk(World worldIn) {
        super(worldIn);
        if(!worldIn.isRemote&& ModSimLoader.hasLoadedFolks){
            //没加载，毁灭吧
            this.setDead();
        }
        //会捡起地上的东西
        this.setCanPickUpLoot(true);
        //会进门
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        //破门而入
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        //会游泳
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.setSize(0.6F,1.8F);
        this.enablePersistence();

    }
    public EntityFolk(World world, boolean isCreating) {
        super(world);
        //会捡起地上的东西
        this.setCanPickUpLoot(true);
        //会进门
        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);
        //破门而入
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        //会游泳
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.isBeingCreated = isCreating;
        this.setSize(0.6F, 1.8F);
        this.enablePersistence();
    }

    public EntityFolk(World world, String id) {
        super(world);
        this.setUniqueId(UUID.fromString(id));
        //会捡起地上的东西
        this.setCanPickUpLoot(true);
        //会进门
        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);
        //破门而入
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        //会游泳
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.isBeingCreated = false;
        this.setSize(0.6F, 1.8F);
        this.enablePersistence();
    }

    /***
     * 初始化 实体 ai
     */
    @Override
    public void initEntityAI() {

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

    }
    /**
     * 应用实体属性
     */
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        //最大生命 40
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        //移动_速度
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        //跟随范围
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(256.0D);
    }
    /**
     * @Author fan
     * @Description //TODO 能否重生
     * @Date 15:18 2022/10/18
     * @Param []
     * @return boolean
     **/
    @Override
    protected boolean canDespawn() {
        return false;
    }
    /**
     * @Author fan
     * @Description //TODO 实体更新
     * @Date 15:18 2022/10/18
     * @Param []
     * @return void
     **/
    @Override
    public void onUpdate() {
        long i=System.currentTimeMillis() - this.secondTimer;
        try {
            if (i > 1000L) {
                this.secondTimer = System.currentTimeMillis();
                List<Entity> list1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, (new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D)).expand(2.0D, 4.0D, 2.0D));
                for (Entity entity1:list1){
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem)entity1;
                        ItemStack is = ((EntityItem)entity1).getEntityItem();

                        try {
                            Item item=is.getItem();
                            if(item instanceof ItemFood){
                                //如果手里拿的是食物就，并且饿了就吃了
                                ItemFood food = (ItemFood)item;
                                if (this.theData!=null&&this.theData.hunger < 10 && food != null) {
                                    entityitem.setDead();
                                    ++this.theData.hunger;
                                }
                            }
                        } catch (Exception var8) {
                            ModSimLoader.log.error("Npc 吃东西出错了："+var8.getMessage());
                        }
                    } else if (entity1 instanceof EntityFolk && (int)this.posX == (int)entity1.posX && (int)this.posZ == (int)entity1.posZ) {
                        this.motionX += 0.10000000149011612D;

                        try {
                            this.theData.stayPut = false;
                        } catch (Exception var7) {
                        }
                    }
                }
                if (this.theData != null && !this.worldObj.isRemote && !ModSimLoader.folks.contains(this.theData)) {
                    ModSimLoader.folks.add(this.theData);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onUpdate();
    }
    /**
     * @Author fan
     * @Description //TODO 当NPC更新时
     * @Date 10:40 2022/10/16
     * @Param []
     * @return void
     **/
    public void onFolkUpdate() {
        if (!this.worldObj.isRemote) {
            EntityFolk e = ModSimLoader.getFolkDataByUID(this.getUniqueID().toString()).entity;
            if (e != null && this.theData != null && this.addedToChunk) {
                if (this.theData.entity != null && !this.theData.entity.equals(this)) {
                    ModSimLoader.log.info("获得重复的民间实体");
                    this.setDead();
                }

                if (!e.equals(this)) {
                }
            }
        }

        if (this.theData == null && !this.isBeingCreated && !this.worldObj.isRemote) {
            this.theData = ModSimLoader.getFolkDataByUID(this.getUniqueID().toString());
            this.theData.entity = this;
            this.theData.sendSkinPathToClient();
        }
    }
    /**
     * @Author fan
     * @Description //TODO 死亡
     * @Date 15:22 2022/10/18
     * @Param [cause]
     * @return void
     **/
    @Override
    public void onDeath(DamageSource cause) {
        if (this.theData != null) {
            this.theData.onDeath(cause);
        }

        super.onDeath(cause);
    }
    /**
     * @Author fan
     * @Description //TODO 交互
     * @Date 15:01 2022/10/18
     * @Param [player, hand]
     * @return boolean
     **/
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (!player.worldObj.isRemote) {
            NetWorkLoader.net.sendTo(new PacketOpenFolkGui(this.theData), (EntityPlayerMP)player);
        }
        return true;
    }
    /**
     * @Author fan
     * @Description //TODO 摆动手臂
     * @Date 15:44 2022/10/18
     * @Param []
     * @return void
     **/
    public void swing() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int d = 0; d < 12; ++d) {
                    EntityFolk.this.swingProgress = 0.3F;

                    try {
                        Thread.sleep(100L);
                    } catch (Exception var4) {
                    }

                    EntityFolk.this.swingProgress = 0.7F;

                    try {
                        Thread.sleep(100L);
                    } catch (Exception var3) {
                    }
                }

            }
        });
        t.start();
    }
    /**
     * @Author fan
     * @Description //TODO 移动
     * @Date 15:45 2022/10/18
     * @Param []
     * @return boolean
     **/
    public boolean isMoving() {
        return this.motionX > 0.0D || this.motionY > 0.0D || this.motionZ > 0.0D;
    }
    /**
     * @Author fan
     * @Description //TODO 是儿童
     * @Date 15:46 2022/10/18
     * @Param []
     * @return boolean
     **/
    @Override
    public boolean isChild() {
        NpcIdentity cfi = ModSimLoader.getFolkByUUID(this.getUniqueID());
        if (cfi != null) {
            return Integer.parseInt(cfi.age) < Integer.parseInt(cfi.maturityAge);
        } else {
            return false;
        }
    }
}
