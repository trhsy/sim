package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;

/**
 * @ClassName JobTaskShopkeep
 * @Description todo 售卖任务
 * @Author TRHSY
 * @Date 2022/11/1521:20
 **/
public class JobTaskShopkeep extends JobTask {
    String product;
    boolean hasFed = false;

    public JobTaskShopkeep(Job j, long ms, String product) {
        super(j, ms);
        this.product = product;
        this.hasFed = false;
    }
    @Override
    public void onTaskBegin() {
    }
    @Override
    public void onUpdate() {
        if (this.job.jobWorld.getWorldTime() % 24000L < 11600L) {
            //售卖
            this.folk.setStatus(I18n.format("container.sim.job_task_Selling")+" " + this.product);
            this.hasFed = false;
        } else {
            //关闭店铺
            this.folk.setStatus(I18n.format("container.sim.job.Baker_Closing"));
            if (!this.hasFed) {
                int sell = this.job.feedFolks();
                this.hasFed = true;
                if (sell > 0) {
                    //张三 （工作/面包师）今天卖了 000 个食物给人们的。
                    ModSimLoader.sendChat(this.folk.getName() + "(" + this.job.toString() + ")  "+I18n.format("container.sim.job.has_sold") + sell +I18n.format("container.sim.job.grocer.farmer.folks"));
                } else {
                    //张三 今天没有产品可以卖给人们。
                    ModSimLoader.sendChat(this.folk.getName() + I18n.format("container.sim.job.grocer.farmer.today"));
                }
            }
        }

    }
    @Override
    public void onTaskComplete() {
    }
}
