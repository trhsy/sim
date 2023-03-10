package com.trhsy.sim.npc.race;

import java.io.File;
import java.io.Serializable;

import com.trhsy.sim.loader.ModSimLoader;

/**
 * 种族
 */
public class Race implements Serializable {
    /**名称**/
    public String raceName;
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

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSkinName() {
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public int getMaturity() {
        return maturity;
    }

    public void setMaturity(int maturity) {
        this.maturity = maturity;
    }

    public static File getSimfolder() {
        return simfolder;
    }

    public static void setSimfolder(File simfolder) {
        Race.simfolder = simfolder;
    }

    public static String getHumanFolder() {
        return humanFolder;
    }

    public static void setHumanFolder(String humanFolder) {
        Race.humanFolder = humanFolder;
    }

    public static String getElfFolder() {
        return elfFolder;
    }

    public static void setElfFolder(String elfFolder) {
        Race.elfFolder = elfFolder;
    }

    public static String getDarkElfFolder() {
        return darkElfFolder;
    }

    public static void setDarkElfFolder(String darkElfFolder) {
        Race.darkElfFolder = darkElfFolder;
    }
}
