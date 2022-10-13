package com.trhsy.sim.entity;

import com.trhsy.sim.entity.ai.FolkAIOpenFenceGate;
import com.trhsy.sim.entity.ai.FolkAIWander;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

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
    public NpcData theData = null;
    /**正在创建**/
    public boolean isBeingCreated = false;
    public EntityFolk(World worldIn) {
        super(worldIn);
        if(!worldIn.isRemote&& ModSimLoader.hasLoadedFolks){
            //没加载，毁灭吧
            this.setDead();
        }
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
        //会进门
        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.isBeingCreated = isCreating;
        this.setSize(0.6F, 1.8F);
        this.enablePersistence();
    }

    public EntityFolk(World world, String id) {
        super(world);
        this.setUniqueId(UUID.fromString(id));
        //会进门
        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
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
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        //看起来很空闲
        this.tasks.addTask(8, new EntityAILookIdle(this));
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
    @Override
    public void onUpdate() {

    }

    public String getTexture() {
        String texture = "";
        try {
            if (this.theData != null) {
                //System.out.println("实体人性别："+theData.gender);
                if (this.theData.gender == 0) {
                    texture = "male" + this.theData.skinnumber + ".png";
                } else {
                    texture = "female" + this.theData.skinnumber + ".png";
                }
            } else {
                texture = "male0.png";
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("getTexture出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return texture;
    }

}
