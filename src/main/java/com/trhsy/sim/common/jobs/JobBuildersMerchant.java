package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;

import java.io.Serializable;

/**
 * ========================================
 *
 * @ClassName JobBuildersMerchant
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:39
 * ========================================
 **/
public class JobBuildersMerchant extends Job implements Serializable {
    private static final long serialVersionUID = 1177112214324279141L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;

    public JobBuildersMerchant() {
    }

    public JobBuildersMerchant(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod) null);
            }

        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!ModSimukraft.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.INSTORE) {
            this.runDelay = 10000;
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long) this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if ((this.theStage != Stage.IDLE || !ModSimukraft.isDayTime()) && this.theStage == Stage.INSTORE) {
                this.theFolk.statusText = "Serving customers";
                this.theFolk.updateLocationFromEntity();
                double dist = (double) this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist > 5.0D && this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod) null);
                }

                if (dist <= 5.0D) {
                    this.theFolk.stayPut = true;
                }
            }

        }
    }

    @Override
    public void onArrivedAtWork() {
        int dist = false;
        this.theFolk.updateLocationFromEntity();
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Arrived at the store";
            this.theStage = Stage.INSTORE;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod) null);
        }

    }


}
