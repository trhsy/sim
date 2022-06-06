package com.trhsy.sim.common.entiy;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.ModSimReloaded;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 游戏状态
 */
public class GameStates implements Serializable {
    /**人口**/
    public int population = 0;
    /**金币**/
    public float credits = 10.0F;
    /**是否开启作弊**/
    public boolean cheatMode = false;
    /**游戏模式编号**/
    public int gameModeNumber = -1;
    /**星期几**/
    public int dayOfWeek = 0;
    /**上次更新检查**/
    public long lastUpdateCheck = 0L;
    /**用户ID**/
    public long userId = 0L;
    /**人口限制**/
    public int populationLimit = 100;
    /**木材面积**/
    public int lumberArea = 30;
    /**是否禁用光束效果**/
    public boolean disableBeamEffect = false;
    /**民间噪音**/
    public boolean folkNoise = true;

    public GameStates() {
    }

    public void loadStates() {
        File f = new File(ModSimReloaded.getSavesDataFolder() + "settings.sk2");
        if (!f.exists()) {
            ModSimReloaded.states = (GameStates) ModSimReloaded.loadObject(ModSimReloaded.getSavesDataFolder() + "settings.suk");
        } else {
            this.loadStates2();
        }

    }

    private void loadStates2() {
        ArrayList<String> strings = ModSimReloaded.loadSK2(ModSimReloaded.getSavesDataFolder() + "settings.sk2");
        for (String line:strings){
            if (line.contains("|")) {
                int m1 = line.indexOf("|");
                String name = line.substring(0, m1);
                String value = line.substring(m1 + 1);
                switch (name){
                    case "credits":
                        this.credits = Float.parseFloat(value);
                        break;
                    case "gamemode":
                        this.gameModeNumber = Integer.parseInt(value);
                        break;
                    case "dayofweek":
                        this.dayOfWeek = Integer.parseInt(value);
                        break;
                    case "lastupdatecheck":
                        this.lastUpdateCheck = Long.parseLong(value);
                        break;
                    case "uid":
                        this.userId = Long.parseLong(value);
                        break;
                    case "folkNoise":
                        this.userId = Long.parseLong(value);
                        break;
                    case "disableBeamEffect":
                        this.userId = Long.parseLong(value);
                        break;
                    case "lumberArea":
                        this.userId = Long.parseLong(value);
                        break;
                    case "populationLimit":
                        this.userId = Long.parseLong(value);
                        break;
                    case "cheatMode":
                        this.userId = Long.parseLong(value);
                        break;
                    case "population":
                        this.userId = Long.parseLong(value);
                        break;
                    default:
                        break;
                }
                   /* if (name.contentEquals("credits")) {
                        this.credits = Float.parseFloat(value);
                    } else if (name.contentEquals("gamemode")) {
                        this.gameModeNumber = Integer.parseInt(value);
                    } else if (name.contentEquals("dayofweek")) {
                        this.dayOfWeek = Integer.parseInt(value);
                    } else if (name.contentEquals("lastupdatecheck")) {
                        this.lastUpdateCheck = Long.parseLong(value);
                    } else if (name.contentEquals("uid")) {
                        this.userId = Long.parseLong(value);
                    }*/
            }
        }

    }

    public void saveStates() {
        String folder = ModSimReloaded.getSavesDataFolder();
        ArrayList<String> strings = new ArrayList();
        //金额
        strings.add("credits|" + this.credits);
        //游戏状态
        strings.add("gamemode|" + this.gameModeNumber);
        //星期几
        strings.add("dayofweek|" + this.dayOfWeek);
        //最后一次更新
        strings.add("lastupdatecheck|" + this.lastUpdateCheck);
        //npc 声音
        strings.add("folkNoise|" + this.folkNoise);
        //对齐光束
        strings.add("disableBeamEffect|" + this.disableBeamEffect);
        //伐木面积
        strings.add("lumberArea|" + this.lumberArea);
        //人口限制
        strings.add("populationLimit|" + this.populationLimit);
        //用户id
        strings.add("uid|" + this.userId);
        //作弊
        strings.add("cheatMode|" + this.cheatMode);
        //人口
        strings.add("population|" + this.population);

        ModSimReloaded.saveSK2(folder + "settings.sk2", strings);
        ModSim.log.info("游戏状态: saveStates() called BOTH sides, 金额存储为 " + this.credits);
    }
}
