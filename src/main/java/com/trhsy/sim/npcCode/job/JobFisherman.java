package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskPlaceInChest;
import com.trhsy.sim.npcCode.task.JobTaskShopkeep;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

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
    //工作阶段
    public int fishermanStage = 0;
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
            this.jobName = new TextComponentTranslation("container.sim.Vocation18",new Object[0]).getUnformattedText();
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
                    Block b= this.folk.entity.world.getBlockState(p).getBlock();
                    if ( b== Blocks.WATER ||  this.folk.entity.world.getBlockState(p).getBlock() == Blocks.FLOWING_WATER) {
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
                    this.fishermanStage = 0;
                    this.stage = 0;
                } else if (this.fishermanStage == 0) {
                    this.fishermanStage = 1;
                    //去上班
                    this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
                } else if (this.nearWater) {
                    /**
                     * @Author fan
                     * @Description //TODO 如果找到水
                     * @Date 20:56 2023/4/9
                     * @Param [folk, pos, world]
                     * @return
                     **/
                    if (this.fishermanStage == 1) {
                        this.fishermanStage = 2;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.FISHING",new Object[0]).getUnformattedText()));
                    } else if (this.fishermanStage == 2) {
                        this.fishermanStage = 3;
                        //把鱼放到箱子里 生鱼/鳕鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(3) + 1,0)));
                    } else if (this.fishermanStage == 3) {
                        this.fishermanStage = 4;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.FISHING",new Object[0]).getUnformattedText()));
                    } else if (this.fishermanStage == 4) {
                        this.fishermanStage = 5;
                        //把生鲑鱼放到箱子里 鲑鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(3) + 1,1)));
                    } else if (this.fishermanStage == 5) {
                        this.fishermanStage = 6;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.FISHING",new Object[0]).getUnformattedText()));
                    } else if (this.fishermanStage == 6) {
                        this.fishermanStage = 7;
                        //把鱼放到箱子里 小丑鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(3) + 1,2)));
                    } else if (this.fishermanStage == 7) {
                        this.fishermanStage = 8;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.FISHING",new Object[0]).getUnformattedText()));
                    } else if (this.fishermanStage == 8) {
                        this.fishermanStage = 9;
                        //把鱼放到箱子里 河豚
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1,3)));
                    } else if (this.fishermanStage == 9) {
                        this.fishermanStage = 10;
                        //钓鱼
                        this.addJobTask(new JobTaskIdle(this, 60000L, new TextComponentTranslation("container.sim.FISHING",new Object[0]).getUnformattedText()));
                    } else if (this.fishermanStage == 10) {
                        this.fishermanStage = 11;
                        //把鱼放到箱子里 生鱼
                        this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1)));
                    } else if (this.fishermanStage == 11) {
                        this.fishermanStage = 12;
                        //卖鱼
                        this.addJobTask(new JobTaskShopkeep(this, -1L, new TextComponentTranslation("container.sim.job.fisherman.farmer.fish",new Object[0]).getUnformattedText(),true));
                    }
                } else {
                    //当前时间
                    long now = System.currentTimeMillis();
                    if(now-this.timeSinceLastBlockPlace>3000L){
                        this.timeSinceLastBlockPlace = now;
                        //找不到可以钓鱼的水。试着在离水更近的地方重建鱼场
//                    ModSimLoader.sendChat(folk.getName() + " " + new TextComponentTranslation("container.sim.job_task_Fisherman1"));
                        //this.addJobTask(new JobTaskIdle(this, -1L, new TextComponentTranslation("container.sim.job_task_Fisherman")));
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
        return new TextComponentTranslation("container.sim.Vocation18",new Object[0]).getUnformattedText();
    }
}
