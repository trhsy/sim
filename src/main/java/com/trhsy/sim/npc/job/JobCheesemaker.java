package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskCheesemaker;
import com.trhsy.sim.npc.task.JobTaskCollectItems;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskUnloadItems;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobCheesemaker
 * @Description: 奶酪制造商
 * @date 2023/07/31 下午 3:45
 */
public class JobCheesemaker extends Job{
    public JobCheesemaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.itemCheese);
        //奶酪酱
        this.jobName = I18n.format("container.sim.Vocation21");
        this.stage = -1;
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
                }else if (this.stage == 1) {
                    List<ItemStack> colItems = new ArrayList();
                    //牛奶
                    colItems.add(new ItemStack(Items.MILK_BUCKET, 16));
                    //收集
                    this.addJobTask(new JobTaskCollectItems(this, 120000, colItems));
                    this.stage = 2;
                } else if (this.stage == 2) {
                    List<ItemStack> colItems = new ArrayList();
                    //牛奶
                    colItems.add(new ItemStack(Items.MILK_BUCKET, 16));
                    this.addJobTask(new JobTaskUnloadItems(this, 30000L, colItems));
                    this.stage = 3;
                }else if (this.stage == 3) {
                    this.addJobTask(new JobTaskCheesemaker(this, -1L, I18n.format("container.sim.JobTaskCheesemaker1")));
                    this.stage = 5;
                }else{
                    if (this.jobTasks.size() > 0&&this.currentTask==null) {
                        this.currentTask = (JobTask) this.jobTasks.get(0);
                        this.currentTask.begin();
                    }
                }
            }
            }catch (Exception e){
                StackTraceElement element = e.getStackTrace()[0];
                ModSimLoader.log.error("JobBurgers-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            }
        }
    @Override
    public String toString() {
        return this.jobName;
    }
}
