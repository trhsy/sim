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
}
