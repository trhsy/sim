package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskShopkeep;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @ClassName JobBuildersMerchant
 * @Description todo 建筑商
 * @Author TRHSY
 * @Date 2023/7/322:39
 **/
public class JobBuildersMerchant extends Job{
    public JobBuildersMerchant(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            folk.holding = new ItemStack(ItemLoader.copperHoe);
            //建筑商
            this.jobName = I18n.format("container.sim.Vocation11");
            //去上班
            this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
            //服务客户
            this.addJobTask(new JobTaskShopkeep(this, -1L, I18n.format("container.sim.job.serving_customers")));
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobBuildersMerchant出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
