package com.trhsy.sim.common.entiy;

import com.trhsy.sim.ModSim;
import net.minecraft.entity.*;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
    //问候计时器
    private long greetTimer = 0L;
    //最后一次受伤
    private long lastHurt = 0L;
    //日历
    private Calendar cal = new GregorianCalendar();
    //找到路了吗
    public boolean gotPath;

    public EntityFolk(World world) {
        super(world);
        //破门而入
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        //避开水
        ((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
        //会进门
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        //会游泳
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        //会捡起地上的东西
        this.setCanPickUpLoot(true);
//        this.setEquipmentDropChance(1, 1);
        //实体人任务
        //避免实体
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        //闲置任务
        this.tasks.addTask(1, new EntityAILookIdle(this));
        //住进屋子
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        //室内移动
        this.tasks.addTask(3, new EntityAIMoveIndoors(this));
        //限制开门
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        //游泳
        this.tasks.addTask(4, new EntityAISwimming(this));
        //走向限制
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        //实体AI监视最近2
        this.tasks.addTask(10, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        //实体AI监视最近
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        //开门
        this.tasks.addTask(9, new EntityAIOpenDoor(this, true));
        //闲逛
        this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        //拾取战利品
        this.setCanPickUpLoot(true);
        //启动
        if (!ModSim.proxy.ranStartup) {
//            ModSim.log.info("实体人：重置npc");
//            this.setDead();
        }

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
     * @Author fan
     * @Description //TODO 获得纹理
     * @Date 17:16 2022/5/22
     * @Param []
     * @return java.lang.String
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
            return "male0.png";
        }
    }

}
