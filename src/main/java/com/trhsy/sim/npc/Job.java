package com.trhsy.sim.npc;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.task.JobTask;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: Job
 * @Description: 工作
 * @date 2022/10/13 17:38
 */
public class Job {
    public String jobName = "null";
    public NpcData folk = null;
    public V3 workPlace = null;
    public World jobWorld = null;
    public int stage = -1;
    public ItemStack stuckItem = null;
    public boolean chestsFull = false;
    public List<JobTask> jobTasks = new ArrayList();
    public List<IInventory> jobChests = new ArrayList();
    public JobTask currentTask;
    public boolean onWayToWork;
    public boolean atWork;
    public List<Item> collectionItems = new ArrayList();
    Building collectionBuilding;
    boolean hasCollected;
    LinkedHashMap<Building, ItemStack> collectionPoints = new LinkedHashMap();
    public float pay;
    transient long itemGrabTimer = 0L;
    private int colCount = 0;
    private LinkedHashMap<Building, ItemStack> colBs;

    public Job(NpcData folk, BlockPos pos, World world) {
        this.folk = folk;
        folk.respawn(world, pos);
        this.workPlace = V3.fromBlockPos(pos);
        this.jobWorld = world;
    }

    public Job(NpcData folk, V3 pos, World world) {
        this.folk = folk;
        folk.respawn(world, pos.toBlockPos());
        this.workPlace = pos;
        this.jobWorld = world;
    }
    public void nextTask() {
        ModSimLoader.log.info("移动到下一个任务");
        int curTask = 0;

        for(int i = 0; i < this.jobTasks.size(); ++i) {
            if (this.jobTasks.get(i) == this.currentTask) {
                curTask = i;
                ModSimLoader.log.info("当前任务索引为 " + i);
            }
        }

        if (curTask >= this.jobTasks.size() - 1) {
            this.currentTask = null;
            this.stage = -1;
            ModSimLoader.log.info("没有剩余任务");
        } else {
            ModSimLoader.log.info("将任务更改为 " + this.jobTasks.get(curTask + 1) + "(" + curTask + 1 + ")");
            this.stage = curTask + 1;
            this.currentTask = (JobTask)this.jobTasks.get(curTask + 1);
            this.currentTask.begin();
        }

    }
}
