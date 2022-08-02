package com.trhsy.sim.common.core.entity.folk.genetics;

import java.io.File;
import java.io.Serializable;

import com.trhsy.sim.common.loader.ModSimReloaded;

public class Race implements Serializable {
    public String raceName = "";

    static File simfolder = new File(ModSimReloaded.getSimukraftFolder() + "races/");
    static String humanFolder = ModSimReloaded.getSimukraftFolder() + "races/" + "Human/";
    static String elfFolder = ModSimReloaded.getSimukraftFolder() + "races/" + "Elf/";
    static String darkElfFolder = ModSimReloaded.getSimukraftFolder() + "races/" + "DarkElf/";
    protected static String[] names = simfolder.list();

    public static int humanAdultMaleSkinCount = 0;
    public static int humanAdultFemaleSkinCount = 0;

    public static int elvenAdultMaleSkinCount = 0;
    public static int elvenAdultFemaleSkinCount = 0;

    public static int darkElvenAdultMaleSkinCount = 0;
    public static int darkElvenAdultFemaleSkinCount = 0;

    public Race() {

    }

    public void setRaceName(String name) {
        name = raceName;
    }

    public String getRaceName() {
        return raceName;
    }

    public static void loadRaces() {
        try {
            humanAdultMaleSkinCount = 64;
            humanAdultFemaleSkinCount = 64;

            elvenAdultMaleSkinCount = 64;
            elvenAdultFemaleSkinCount = 64;

            darkElvenAdultMaleSkinCount =64;
            darkElvenAdultFemaleSkinCount = 64;

            Races.loadRaces();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("Race-loadRaces出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public int getRandomSkinForRace(Race race, int gender) {
        try {
            if (gender == 0) {
                if (race == Races.raceHuman) {
                    return humanAdultMaleSkinCount;
                } else if (race == Races.raceElf) {
                    return elvenAdultMaleSkinCount;
                } else if (race == Races.raceDarkElf) {
                    return darkElvenAdultMaleSkinCount;
                }
            } else {
                if (race == Races.raceHuman) {
                    return humanAdultFemaleSkinCount;
                } else if (race == Races.raceElf) {
                    return elvenAdultFemaleSkinCount;
                } else if (race == Races.raceDarkElf) {
                    return darkElvenAdultFemaleSkinCount;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getRandomSkinForRace出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return 0;
    }
	
	/*public String toString()
	{
		return raceName;
	}
	
	public void loadRaces()
	{
		try
		{
			for (String g : names)
			{
				if(new File(ModSimReloaded.getSimukraftFolder()+ "/races/" + g).isDirectory())
				{
					races.add(g);
				}
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
    	}
	}*/

    public static Race getRaceFromName(String searchTerm) {
        Race race;
        try {
            for (int i = 0; i < Races.raceList.size(); i++) {
                race=Races.raceList.get(i);
                if (race.raceName.contentEquals(searchTerm)) {
                    return race;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getRaceFromName出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

}
