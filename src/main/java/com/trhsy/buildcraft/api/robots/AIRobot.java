package com.trhsy.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * ========================================
 *
 * @ClassName AIRobot
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:46
 * ========================================
 **/
public class AIRobot {
    public EntityRobotBase robot;
    private AIRobot delegateAI;
    private AIRobot parentAI;

    public AIRobot(EntityRobotBase iRobot) {
        this.robot = iRobot;
    }

    public void start() {
    }

    public void preempt(AIRobot ai) {
    }

    public void update() {
        this.terminate();
    }

    public void end() {
    }

    public void delegateAIEnded(AIRobot ai) {
    }

    public void delegateAIAborted(AIRobot ai) {
    }

    public void writeSelfToNBT(NBTTagCompound nbt) {
    }

    public void loadSelfFromNBT(NBTTagCompound nbt) {
    }

    public boolean success() {
        return true;
    }

    public int getEnergyCost() {
        return 1;
    }

    public boolean canLoadFromNBT() {
        return false;
    }

    public ItemStack receiveItem(ItemStack stack) {
        return stack;
    }

    public final void terminate() {
        this.abortDelegateAI();
        this.end();
        if (this.parentAI != null) {
            this.parentAI.delegateAI = null;
            this.parentAI.delegateAIEnded(this);
        }

    }

    public final void abort() {
        this.abortDelegateAI();

        try {
            this.end();
            if (this.parentAI != null) {
                this.parentAI.delegateAI = null;
                this.parentAI.delegateAIAborted(this);
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
            this.delegateAI = null;
            if (this.parentAI != null) {
                this.parentAI.delegateAI = null;
            }
        }

    }

    public final void cycle() {
        try {
            this.preempt(this.delegateAI);
            if (this.delegateAI != null) {
                this.delegateAI.cycle();
            } else {
                this.robot.getBattery().extractEnergy(this.getEnergyCost(), false);
                this.update();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
            this.abort();
        }

    }

    public final void startDelegateAI(AIRobot ai) {
        this.abortDelegateAI();
        this.delegateAI = ai;
        ai.parentAI = this;
        this.delegateAI.start();
    }

    public final void abortDelegateAI() {
        if (this.delegateAI != null) {
            this.delegateAI.abort();
        }

    }

    public final AIRobot getActiveAI() {
        return this.delegateAI != null ? this.delegateAI.getActiveAI() : this;
    }

    public final AIRobot getDelegateAI() {
        return this.delegateAI;
    }

    public final void writeToNBT(NBTTagCompound nbt) {
        nbt.func_74778_a("class", this.getClass().getCanonicalName());
        NBTTagCompound data = new NBTTagCompound();
        this.writeSelfToNBT(data);
        nbt.func_74782_a("data", data);
        if (this.delegateAI != null && this.delegateAI.canLoadFromNBT()) {
            NBTTagCompound sub = new NBTTagCompound();
            this.delegateAI.writeToNBT(sub);
            nbt.func_74782_a("delegateAI", sub);
        }

    }

    public final void loadFromNBT(NBTTagCompound nbt) {
        this.loadSelfFromNBT(nbt.func_74775_l("data"));
        if (nbt.func_74764_b("delegateAI")) {
            NBTTagCompound sub = nbt.func_74775_l("delegateAI");

            try {
                this.delegateAI = (AIRobot)Class.forName(sub.func_74779_i("class")).getConstructor(EntityRobotBase.class).newInstance(this.robot);
                if (this.delegateAI.canLoadFromNBT()) {
                    this.delegateAI.parentAI = this;
                    this.delegateAI.loadFromNBT(sub);
                }
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
        }

    }

    public static AIRobot loadAI(NBTTagCompound nbt, EntityRobotBase robot) {
        AIRobot ai = null;

        try {
            ai = (AIRobot)Class.forName(nbt.func_74779_i("class")).getConstructor(EntityRobotBase.class).newInstance(robot);
            ai.loadFromNBT(nbt);
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

        return ai;
    }
}
