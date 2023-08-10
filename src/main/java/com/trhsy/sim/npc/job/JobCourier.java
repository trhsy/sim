package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobCourier
 * @Description: 仓库快递员
 * @date 2023/08/04 上午 9:59
 */
public class JobCourier extends Job{
    public JobCourier(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
            //快递员
            this.jobName = I18n.format("container.sim.Vocation10");
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobDairyFarmer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 加载并获取快递点
     */
    public void showCourierPoint(){

    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
