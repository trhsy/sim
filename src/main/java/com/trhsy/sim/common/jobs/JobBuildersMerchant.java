package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;

import java.io.Serializable;

/**
 * ========================================
 *
 * @ClassName JobBuildersMerchant
 * @Description todo 建设商人的工作
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:39
 * ========================================
 **/
public class JobBuildersMerchant extends Job implements Serializable {
    private static final long serialVersionUID = 1177112214324279141L;
    public Vocation vocation = null;
    public FolkData theFolk =new FolkData();
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;

    public JobBuildersMerchant() {
    }

    public JobBuildersMerchant(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+0.5,this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBuildersMerchant出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            if (!ModSimReloaded.isDayTime()) {
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.INSTORE) {
                this.runDelay = 10000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long) this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if ((this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) && this.theStage == Stage.INSTORE) {
                    //为顾客服务中
                    this.theFolk.statusText = I18n.format("container.sim.job.serving_customers");
                    this.theFolk.updateLocationFromEntity();
                    double dist = (double) this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                    if (dist > 5 && this.theFolk.destination == null) {
                        V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+0.5,this.theFolk.employedAt.zCoord);
                        this.theFolk.gotoXYZ(v3, null);
                    }

                    if (dist <= 5) {
                        this.theFolk.stayPut = true;
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBuildersMerchant-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 抵达商店
     */
    @Override
    public void onArrivedAtWork() {
        try {
            this.theFolk.updateLocationFromEntity();
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                //在工作中原地不动
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.Arrived_at_the_store");
                this.theStage = Stage.INSTORE;
            } else {
                V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+0.5,this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }


}
