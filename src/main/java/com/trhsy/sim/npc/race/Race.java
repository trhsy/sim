package com.trhsy.sim.npc.race;

import java.io.File;
import java.io.Serializable;

import com.trhsy.sim.loader.ModSimLoader;

/**
 * 种族
 */
public class Race implements Serializable {
    /**名称**/
    public String raceName = "";
    /**描述**/
    public String desc;
    /**皮肤**/
    public String skinName;
    /**寿命**/
    public int lifespan;
    /**成熟期**/
    public int maturity;
    static File simfolder = new File(ModSimLoader.getSimFolder() + "races/");
    static String humanFolder = ModSimLoader.getSimFolder() + "races/" + "Human/";
    static String elfFolder = ModSimLoader.getSimFolder() + "races/" + "Elf/";
    static String darkElfFolder = ModSimLoader.getSimFolder() + "races/" + "DarkElf/";


    public Race() {

    }
    public static void loadRaces() {
        try {
            Races.loadRaces();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("Race-loadRaces出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    public String getRaceName() {
        return raceName;
    }
    public void setRaceName(String name) {
        name = raceName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public void setMaturity(int maturity) {
        this.maturity = maturity;
    }

    public String getDesc() {
        return desc;
    }

    public String getSkinName() {
        return skinName;
    }

    public int getLifespan() {
        return lifespan;
    }

    public int getMaturity() {
        return maturity;
    }


}
