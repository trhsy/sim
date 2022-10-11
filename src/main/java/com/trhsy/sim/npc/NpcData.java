package com.trhsy.sim.npc;

import com.trhsy.sim.npc.geneics.Race;
import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: npcData
 * @Description:
 * @date 2022/10/11 14:44
 */
public class NpcData {
    /**id**/
    public String ID;
    /**名**/
    public String forename;
    /**姓**/
    public String surname;
    /**年龄 0至17岁的人=儿童18+成人**/
    public int age;
    /**性别 0=男性1=女性**/
    public int gender;
    /**状态 **/
    public String status = "Wandering";
    /**饱食度**/
    public int hunger = 10;
    /**种族**/
    public Race folkRace;
    /**工作**/
    /**npc实体**/
    public EntityFolk entity;
    /**情感关系**/
    /**情绪**/
    /**物品栏**/
    /**任务**/
    /**特征1**/
    /**特征2**/
    /**特征3**/
    /**正在移动**/
    public boolean isMoving;
    /**建筑等级**/
    public float skillBuilding = 1.0F;
    /**耕种等级**/
    public float skillFarming = 1.0F;
    /**采矿等级**/
    public float skillMining = 1.0F;
    /**家**/
    //public Building home;
    /**位置**/
    public V3 pos;
    /**手持物品**/
    public ItemStack holding;

    public float matingStage;
    public float pregnancyStage;
    Random rand;
    public boolean stayPut;
    public boolean isSleeping;
    public boolean isDead;
    public boolean isLoaded;
    private int tempStage;
    private transient long timeSinceLastStatusUpdate;
    private transient long minuteUpdate;
    V3 tempEmployLoc;
    Long lastPathAttempt;
}
