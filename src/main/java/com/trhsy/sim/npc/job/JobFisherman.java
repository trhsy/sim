package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskChopTrees;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskPlaceInChest;
import com.trhsy.sim.npc.task.JobTaskShopkeep;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Iterator;
import java.util.Random;

/**
 * @ClassName JobFisherman
 * @Description todo 渔夫
 * @Author TRHSY
 * @Date 2023/4/916:50
 **/
public class JobFisherman extends Job {
    /**
     * @Author fan
     * @Description //TODO 是否靠近水
     * @Date 20:51 2023/4/9
     * @Param
     * @return
     **/
    boolean nearWater;
    public BlockPos pos;
    public World world;
    /**
     * 自上一个块位置的时间
     **/
    private transient long timeSinceLastBlockPlace = 0L;
    public JobFisherman(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
//手持鱼竿
            folk.holding = new ItemStack(Items.FISHING_ROD);
            //渔夫
            this.jobName = I18n.format("container.sim.Vocation18");
            this.pos=pos;
            this.world=world;
            findWater();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFisherman出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public void onArrive() {

    }
    public void findWater(){
        try {
            //寻找附近水
            BlockPos corner1 = this.pos.north(5).east(5).down(2);
            BlockPos corner2 =  this.pos.south(5).west(5).down(2);

            Iterator var6 = BlockPos.getAllInBox(corner1, corner2).iterator();
            //找到水
            while (var6.hasNext()) {
                BlockPos p = (BlockPos) var6.next();
                if(this.folk!=null&&this.folk.entity!=null){
                    Block b= this.folk.entity.worldObj.getBlockState(p).getBlock();
                    if ( b== Blocks.WATER ||  this.folk.entity.worldObj.getBlockState(p).getBlock() == Blocks.FLOWING_WATER) {
                        this.nearWater = true;
                        break;
                    }
                }
            }
        }catch (Exception e){
            ModSimLoader.log.error("JobFisherman-findWater寻找水出错了",e.getMessage());
        }
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.atWork) {
                if (this.stage == -1) {
                    this.stage = 0;
                } else if (this.stage == 0) {
                    this.stage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
                } else if (this.nearWater) {
                    /**
                     * @Author fan
                     * @Description //TODO 如果找到水
                     * @Date 20:56 2023/4/9
                     * @Param [folk, pos, world]
                     * @return
                     **/
                    if (this.stage == 1) {
                        this.stage = 2;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, I18n.format("container.sim.FISHING")));
                    } else if (this.stage == 2) {
                        this.stage = 3;
                        //把鱼放到箱子里 生鱼/鳕鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(3) + 1,0)));
                    } else if (this.stage == 3) {
                        this.stage = 4;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, I18n.format("container.sim.FISHING")));
                    } else if (this.stage == 4) {
                        this.stage = 5;
                        //把生鲑鱼放到箱子里 鲑鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(3) + 1,1)));
                    } else if (this.stage == 5) {
                        this.stage = 6;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, I18n.format("container.sim.FISHING")));
                    } else if (this.stage == 6) {
                        this.stage = 7;
                        //把鱼放到箱子里 小丑鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(3) + 1,2)));
                    } else if (this.stage == 7) {
                        this.stage = 8;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, I18n.format("container.sim.FISHING")));
                    } else if (this.stage == 8) {
                        this.stage = 9;
                        //把鱼放到箱子里 河豚
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1,3)));
                    } else if (this.stage == 9) {
                        this.stage = 10;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, I18n.format("container.sim.FISHING")));
                    } else if (this.stage == 10) {
                        this.stage = 11;
                        //把鱼放到箱子里 生鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1)));
                    } else if (this.stage == 11) {
                        this.stage = 12;
                        //卖鱼
                        this.addJobTask(new JobTaskShopkeep(this, -1L, "fish"));
                    }
                } else {
                    //当前时间
                    long now = System.currentTimeMillis();
                    if(now-this.timeSinceLastBlockPlace>3000L){
                        this.timeSinceLastBlockPlace = now;
                        //找不到可以钓鱼的水。试着在离水更近的地方重建鱼场
//                    ModSimLoader.sendChat(folk.getName() + " " + I18n.format("container.sim.job_task_Fisherman1"));
                        //this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job_task_Fisherman")));
                        findWater();
                    }
                }
                if (this.jobTasks.size() > 0 && this.currentTask == null) {
                    this.currentTask = (JobTask) this.jobTasks.get(0);
                    this.currentTask.begin();
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFisherman-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation18");
    }
}
