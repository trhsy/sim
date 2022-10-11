package com.trhsy.sim.util;

import com.trhsy.sim.loader.ModSimLoader;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName GameStates
 * @Description todo
 * @Author Tian
 * @Date 2022/9/2515:53
 **/
public class GameStates {
    /**人口**/
    public int population = 0;
    /**游戏模式编号**/
    public int gameModeNumber = -1;
    /**金币**/
    public float credits = 10.0F;
    /**是否开启作弊**/
    public boolean cheatMode = false;
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

    /**
     * 加载配置文件
     */
    public void loadStates() {
        try {
            File f = new File(ModSimLoader.getSavesDataFolder() + "settings.sk2");
            if (!f.exists()) {
                saveStates();
            } else {
                this.loadStates2();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("loadStates出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 从配置文件读取并写入
     */
    private void loadStates2() {
        try {
            List<String> strings = ModSimLoader.loadSK2(ModSimLoader.getSavesDataFolder() + "settings.sk2");
            for (String line:strings){
                if (line.contains("|")) {
                    int m1 = line.indexOf("|");
                    String name = line.substring(0, m1);
                    String value = line.substring(m1 + 1);
                    if ("credits".equals(name)) {
                        this.credits = Float.parseFloat(value);
                    } else if ("gamemode".equals(name)) {
                        this.gameModeNumber = Integer.parseInt(value);
                    } else if ("dayofweek".equals(name)) {
                        this.dayOfWeek = Integer.parseInt(value);
                    } else if ("lastupdatecheck".equals(name)) {
                        this.lastUpdateCheck = Long.parseLong(value);
                    } else if ("uid".equals(name)) {
                        this.userId = Long.parseLong(value);
                    } else if ("folkNoise".equals(name)) {
                        this.folkNoise = Boolean.parseBoolean(value);
                    } else if ("disableBeamEffect".equals(name)) {
                        this.disableBeamEffect = Boolean.parseBoolean(value);
                    } else if ("lumberArea".equals(name)) {
                        this.lumberArea = Integer.parseInt(value);
                    } else if ("populationLimit".equals(name)) {
                        this.populationLimit = Integer.parseInt(value);
                    } else if ("cheatMode".equals(name)) {
                        this.cheatMode = Boolean.parseBoolean(value);
                    } else if ("population".equals(name)) {
                        this.population = Integer.parseInt(value);
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("loadStates2出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }
    /**
     * 保存配置文件
     */
    public void saveStates() {
        try {
            String folder = ModSimLoader.getSavesDataFolder();
            List<String> strings = new CopyOnWriteArrayList();
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

            ModSimLoader.saveSK2(folder + "settings.sk2", strings);
            ModSimLoader.log.info("游戏状态: saveStates() called BOTH sides, 金额存储为 " + this.credits);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("saveStates出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
