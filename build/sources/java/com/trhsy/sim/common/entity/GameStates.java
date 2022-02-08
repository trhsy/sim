package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.ModSimukraft;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @ClassName GameStates
 * @Description todo 游戏状态
 * @Author Tian
 * @Date 2022/1/2320:16
 **/
public class GameStates implements Serializable {
    private static final long serialVersionUID = -2617900998876928361L;
    public int population = 0;
    public float credits = 10.0F;
    public boolean cheatMode = false;
    public int gameModeNumber = -1;
    public int dayOfWeek = 0;
    public long lastUpdateCheck = 0L;
    public long userId = 0L;
    public int populationLimit = 100;
    public int lumberArea = 30;
    public boolean disableBeamEffect = false;
    public boolean folkNoise = true;

    public GameStates() {
    }

    public void loadStates() {
        File f = new File(ModSimukraft.getSavesDataFolder() + "settings.sk2");
        if (!f.exists()) {
            ModSimukraft.states = (GameStates)ModSimukraft.proxy.loadObject(ModSimukraft.getSavesDataFolder() + "settings.suk");
        } else {
            this.loadStates2();
        }

    }

    private void loadStates2() {
        ArrayList<String> strings = ModSimukraft.loadSK2(ModSimukraft.getSavesDataFolder() + "settings.sk2");
        Iterator i$ = strings.iterator();

        while(i$.hasNext()) {
            String line = (String)i$.next();
            if (line.contains("|")) {
                int m1 = line.indexOf("|");
                String name = line.substring(0, m1);
                String value = line.substring(m1 + 1);

                try {
                    if (name.contentEquals("credits")) {
                        this.credits = Float.parseFloat(value);
                    } else if (name.contentEquals("gamemode")) {
                        this.gameModeNumber = Integer.parseInt(value);
                    } else if (name.contentEquals("dayofweek")) {
                        this.dayOfWeek = Integer.parseInt(value);
                    } else if (name.contentEquals("lastupdatecheck")) {
                        this.lastUpdateCheck = Long.parseLong(value);
                    } else if (name.contentEquals("uid")) {
                        this.userId = Long.parseLong(value);
                    }
                } catch (Exception var8) {
                    var8.printStackTrace();
                }
            }
        }

    }

    public void saveStates() {
        String folder = ModSimukraft.getSavesDataFolder();
        ArrayList<String> strings = new ArrayList();
        strings.add("credits|" + this.credits);
        strings.add("gamemode|" + this.gameModeNumber);
        strings.add("dayofweek|" + this.dayOfWeek);
        strings.add("lastupdatecheck|" + this.lastUpdateCheck);
        ModSimukraft.saveSK2(folder + "settings.sk2", strings);
        ModSimukraft.log.info("GameStates: saveStates() called BOTH sides, credits saved as " + this.credits);
    }
}
