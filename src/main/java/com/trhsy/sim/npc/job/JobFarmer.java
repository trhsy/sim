package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.task.JobTask;
import com.trhsy.sim.util.FarmType;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.IPlantable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName JobFarmer
 * @Description todo 农民
 * @Author TRHSY
 * @Date 2022/12/622:48
 **/
public class JobFarmer extends Job {
    //农田箱
    public FarmBox farm;

    public JobFarmer(NpcData folk, BlockPos pos, World world, FarmBox fb) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(ItemLoader.tinHoe);
            this.jobName = I18n.format("container.sim.Vocation5");
            this.farm = fb;
            if(this.farm!=null){
                this.farm.employee = folk;
            }
            this.stage = -1;
           /* //去上班
            this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
            //开始在农场工作
            this.addJobTask(new JobTaskFarmer(this, -1L, I18n.format("container.sim.job.livestock.farmer.Starting"),this.farm));*/
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                } else if (this.stage == 1) {
                    this.stage = 2;
                    //锄地
                    this.addJobTask(new JobTaskFarmer(this, -1L, I18n.format("container.sim.job.livestock.farmer.Starting"),this.farm));
                } else if (this.stage == 2) {
                    this.stage = 3;
                    //种植
                    this.addJobTask(new JobTaskFarmerPlant(this, -1L, I18n.format("container.sim.job.livestock.farmer.Starting"),this.farm));
                } else if (this.stage == 3) {
                    this.stage = 4;
                    //等待
                    this.addJobTask(new JobTaskFarmerGrow(this, -1L, I18n.format("container.sim.job.livestock.farmer.Starting"),this.farm));
                } else if (this.stage == 4) {
                    this.stage = 5;
                    //收获
                    this.addJobTask(new JobTaskFarmerHarvest(this, -1L, I18n.format("container.sim.job.livestock.farmer.Starting"),this.farm));
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(1);
                        this.currentTask.begin();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        //农民
        return I18n.format("container.sim.Vocation5");
    }
}
