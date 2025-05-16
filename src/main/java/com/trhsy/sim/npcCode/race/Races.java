package com.trhsy.sim.npcCode.race;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Races {

    public static Race raceHuman = new RaceHuman();
    public static Race raceElf = new RaceElf();
    public static Race raceDarkElf = new RaceDarkElf();
    public static Race raceOrc = new RaceOrc();

    public static List<Race> raceList = new CopyOnWriteArrayList<Race>();

    public static void loadRaces() {
        raceList.add(raceHuman);
        raceList.add(raceElf);
        raceList.add(raceDarkElf);
        raceList.add(raceOrc);
    }

    public static Race getRaceHuman() {
        return raceHuman;
    }

    public static void setRaceHuman(Race raceHuman) {
        Races.raceHuman = raceHuman;
    }

    public static Race getRaceElf() {
        return raceElf;
    }

    public static void setRaceElf(Race raceElf) {
        Races.raceElf = raceElf;
    }

    public static Race getRaceDarkElf() {
        return raceDarkElf;
    }

    public static void setRaceDarkElf(Race raceDarkElf) {
        Races.raceDarkElf = raceDarkElf;
    }

    public static Race getRaceOrc() {
        return raceOrc;
    }

    public static void setRaceOrc(Race raceOrc) {
        Races.raceOrc = raceOrc;
    }

    public static List<Race> getRaceList() {
        return raceList;
    }

    public static void setRaceList(List<Race> raceList) {
        Races.raceList = raceList;
    }
}
