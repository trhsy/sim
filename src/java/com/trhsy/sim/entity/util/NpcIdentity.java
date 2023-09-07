package com.trhsy.sim.entity.util;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util.entity
 * @ClassName: NpcIdentity
 * @Description: NPC实体工具类
 * @date 2022/10/9 10:50
 */
public class NpcIdentity {
    /**id**/
    public String id;
    /**姓名**/
    public String name;
    /**年龄**/
    public String age;
    /**状态**/
    public String status;
    /**工作**/
    public String job;
    /**住房**/
    public String house;
    /**人际关系**/
    public String relationship;
    /**饥饿程度**/
    public String hunger;
    /**成年**/
    public String maturityAge;
    /**皮肤**/
    public String skinPath;

    public NpcIdentity(String id, String name, String age, String status, String job, String house, String relationship, String hunger, String maturityAge, String skinPath) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
        this.job = job;
        this.house = house;
        this.relationship = relationship;
        this.hunger = hunger;
        this.maturityAge = maturityAge;
        this.skinPath = skinPath;
    }

    public NpcIdentity(String combinedInfo) {
        this.id = combinedInfo.split(",_,")[0];
        this.name = combinedInfo.split(",_,")[1];
        this.age = combinedInfo.split(",_,")[2];
        this.status = combinedInfo.split(",_,")[3];
        this.job = combinedInfo.split(",_,")[4];
        this.house = combinedInfo.split(",_,")[5];
        this.relationship = combinedInfo.split(",_,")[6];
        this.hunger = combinedInfo.split(",_,")[7];
        this.maturityAge = combinedInfo.split(",_,")[8];
        this.skinPath = combinedInfo.split(",_,")[9];
    }

    @Override
    public String toString() {
        return this.id + ",_," + this.name + ",_," + this.age + ",_," + this.status + ",_," + this.job + ",_," + this.house + ",_," + this.relationship + ",_," + this.hunger + ",_," + this.maturityAge + ",_," + this.skinPath;
    }
}
