package com.trhsy.sim.npcCode.race;

import com.trhsy.sim.loader.ModSimLoader;

import java.io.File;
import java.io.Serializable;

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
}
