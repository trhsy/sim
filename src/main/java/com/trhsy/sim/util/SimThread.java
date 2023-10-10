package com.trhsy.sim.util;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util
 * @ClassName: SimThread
 * @Description:
 * @date 2023/09/20 上午 9:41
 */
public class SimThread extends Thread{
    private List<V3> closestBlocks ;
    private int totalBlockCount ;
    private NpcData folk;
    private long timeSinceLastBlockPlace;
    private Job job;

    public List<V3> getClosestBlocks() {
        return closestBlocks;
    }

    public void setClosestBlocks(List<V3> closestBlocks) {
        this.closestBlocks = closestBlocks;
    }

    public int getTotalBlockCount() {
        return totalBlockCount;
    }

    public void setTotalBlockCount(int totalBlockCount) {
        this.totalBlockCount = totalBlockCount;
    }

    public NpcData getFolk() {
        return folk;
    }

    public void setFolk(NpcData folk) {
        this.folk = folk;
    }

    public long getTimeSinceLastBlockPlace() {
        return timeSinceLastBlockPlace;
    }

    public void setTimeSinceLastBlockPlace(long timeSinceLastBlockPlace) {
        this.timeSinceLastBlockPlace = timeSinceLastBlockPlace;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
