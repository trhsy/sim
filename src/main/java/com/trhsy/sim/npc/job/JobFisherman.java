package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskPlaceInChest;
import com.trhsy.sim.npc.task.JobTaskShopkeep;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

    public JobFisherman(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //渔夫
        this.jobName = I18n.format("container.sim.Vocation18");
        //寻找附近水
        BlockPos corner1 = pos.north(5).east(5).down(2);
        BlockPos corner2 = pos.south(5).west(5).down(2);

        Iterator var6 = BlockPos.getAllInBox(corner1, corner2).iterator();
        //找到水
        while (var6.hasNext()) {
            BlockPos p = (BlockPos) var6.next();
            if (world.getBlockState(p).getBlock() == Blocks.WATER || world.getBlockState(p).getBlock() == Blocks.FLOWING_WATER) {
                this.nearWater = true;
                break;
            }
        }
        /**
         * @Author fan
         * @Description //TODO 如果找到水
         * @Date 20:56 2023/4/9
         * @Param [folk, pos, world]
         * @return
         **/
        if (this.nearWater) {
            //去上班
            this.addJobTask(new JobTaskIdle(this, 5000L, I18n.format("container.sim.job.builder_Arrived")));
            //钓鱼
            this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.FISHING")));
//把鱼放到箱子里
            this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1)));
//钓鱼
            this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.FISHING")));
//把鱼放到箱子里
            this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1)));
//钓鱼
            this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.FISHING")));
//把鱼放到箱子里
            this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1)));
//钓鱼
            this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.FISHING")));
//把鱼放到箱子里
            this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1)));
//钓鱼
            this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.FISHING")));
//把鱼放到箱子里
            this.addJobTask(new JobTaskPlaceInChest(this, 11000L, new ItemStack(Items.FISH, new Random().nextInt(2) + 1)));
            //卖鱼
            this.addJobTask(new JobTaskShopkeep(this, -1L, "fish"));
        } else {
            //找不到可以钓鱼的水。试着在离水更近的地方重建鱼场
            ModSimLoader.sendChat(folk.getName() + " " + I18n.format("container.sim.job_task_Fisherman1"));
            this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job_task_Fisherman")));
        }
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation18");
    }
}
