package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobButcher
 * @Description todo 屠夫
 * @Author TRHSY
 * @Date 2023/4/916:16
 **/
public class JobButcher extends Job{
    //要收集的物品
    public List<ItemStack> colItems = new ArrayList();
    public JobButcher(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(ItemLoader.tinAxe);
            this.jobName = I18n.format("container.sim.Vocation15");
            //牛肉
            this.colItems.add(new ItemStack(Items.BEEF, 16));
            //鸡肉
            this.colItems.add(new ItemStack(Items.CHICKEN, 16));
            //羊肉
            this.colItems.add(new ItemStack(Items.MUTTON, 16));
            //猪排
            this.colItems.add(new ItemStack(Items.PORKCHOP, 16));
            //兔肉
            this.colItems.add(new ItemStack(Items.RABBIT, 16));
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobButcher出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                    //收集
                    this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
                    this.stage = 2;
                } else if (this.stage == 2) {
                    //卸货
                    this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
                    this.stage = 3;
                } else if (this.stage == 3) {
                    /**
                     * @Author fan
                     * @Description //TODO 卖肉
                     * @Date 16:44 2023/4/9
                     * @Param [folk, pos, world]
                     * @return
                     **/
                    this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.SELLINGMEAT")));
                    this.stage = 4;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobButcher-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation15");
    }
}
