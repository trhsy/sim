package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobBartender
 * @Description: 酒保
 * @date 2023/08/04 上午 10:01
 */
public class JobBartender extends Job{
    //要收集的物品
    public List<ItemStack> colItems = new ArrayList();
    public JobBartender(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(ItemLoader.tinAxe);
            this.jobName = I18n.format("container.sim.Vocation28");
            //小麦
            this.colItems.add(new ItemStack(Items.WHEAT, 16));
            //玻璃
            colItems.add(new ItemStack(Blocks.GLASS, 16));
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
                    List<ItemStack> cakes = new CopyOnWriteArrayList<ItemStack>();
                    //小麦
                    this.colItems.add(new ItemStack(Items.WHEAT, 3));
                    //玻璃
                    colItems.add(new ItemStack(Blocks.GLASS, 3));
                    //制作啤酒
                    this.addJobTask(new JobTaskProduceItem(this, 60000L, Items.CAKE, cakes, I18n.format("container.sim.job.Baker_Baking_per")));
                    this.stage = 4;
                }else if(this.stage == 4){

                    /**
                     * @Author fan
                     * @Description //TODO 卖酒
                     * @Date 16:44 2023/4/9
                     * @Param [folk, pos, world]
                     * @return
                     **/
                    this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("item.drinkBeer.name")));
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
            ModSimLoader.log.error("JobButcher-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
