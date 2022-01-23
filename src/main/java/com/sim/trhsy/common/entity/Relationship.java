package com.sim.trhsy.common.entity;

import com.sim.trhsy.common.ModSimukraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @ClassName Relationship
 * @Description todo 人物与人物之间的关系
 * @Author Tian
 * @Date 2022/1/2320:14
 **/
public class Relationship implements Serializable {
    private static final long serialVersionUID = -1617919828251928361L;
    public FolkData folk1 = null;
    public FolkData folk2 = null;
    public Relationship.Level theLevel;
    public int theSubLevel;
    public boolean isBloodRelation;
    private Random rand;

    public Relationship() {
        this.theLevel = Relationship.Level.AQUAINTANCE;
        this.theSubLevel = 0;
        this.isBloodRelation = false;
        this.rand = new Random();
    }

    public Relationship(FolkData folk1, FolkData folk2, Relationship.Level startingLevel, boolean isBlood) {
        this.theLevel = Relationship.Level.AQUAINTANCE;
        this.theSubLevel = 0;
        this.isBloodRelation = false;
        this.rand = new Random();
        this.folk1 = folk1;
        this.folk2 = folk2;
        this.theLevel = startingLevel;
        this.isBloodRelation = isBlood;
    }

    public String toString() {
        if (this.theLevel == Relationship.Level.AQUAINTANCE) {
            return "is an aquaintance with";
        } else if (this.theLevel == Relationship.Level.BESTFRIENDS) {
            return "is best friends with";
        } else if (this.theLevel == Relationship.Level.DESPISE) {
            return "despises";
        } else if (this.theLevel == Relationship.Level.DISLIKE) {
            return "dislikes";
        } else if (this.theLevel == Relationship.Level.ENEMY) {
            return "is an ememy of";
        } else if (this.theLevel == Relationship.Level.FRIEND) {
            return "is friends with";
        } else if (this.theLevel == Relationship.Level.GOODFRIEND) {
            return "is good friends with";
        } else if (this.theLevel == Relationship.Level.HATE) {
            return "hates";
        } else if (this.theLevel == Relationship.Level.MARRIED) {
            return "is married to";
        } else {
            return this.theLevel == Relationship.Level.PARTNER ? "is living with" : "has an unknown relationship with";
        }
    }

    public String toStringPersepctive(FolkData folk) {
        String other = "";
        FolkData fother;
        if (folk.name.contentEquals(this.folk1.name)) {
            other = this.folk2.name;
            fother = this.folk2;
        } else {
            other = this.folk1.name;
            fother = this.folk1;
        }

        if (this.theLevel == Relationship.Level.AQUAINTANCE) {
            return other + ": Aquaintance";
        } else if (this.theLevel == Relationship.Level.BESTFRIENDS) {
            return other + ": Best friends";
        } else if (this.theLevel == Relationship.Level.DESPISE) {
            return other + ": Despise";
        } else if (this.theLevel == Relationship.Level.DISLIKE) {
            return other + ": Dislike";
        } else if (this.theLevel == Relationship.Level.ENEMY) {
            return other + ": Enemy";
        } else if (this.theLevel == Relationship.Level.FRIEND) {
            return other + ": Friends";
        } else if (this.theLevel == Relationship.Level.GOODFRIEND) {
            return other + ": Good friends";
        } else if (this.theLevel == Relationship.Level.HATE) {
            return other + ": Hate";
        } else if (this.theLevel == Relationship.Level.MARRIED) {
            return other + ": Married";
        } else if (this.theLevel == Relationship.Level.PARTNER) {
            return other + ": Partner";
        } else if (this.theLevel == Relationship.Level.MOTHERDAUGHTER) {
            return fother.age > folk.age ? other + ": Mother" : other + ": Daughter";
        } else if (this.theLevel == Relationship.Level.MOTHERSON) {
            return fother.age > folk.age ? other + ": Mother" : other + ": Son";
        } else if (this.theLevel == Relationship.Level.FATHERSON) {
            return fother.age > folk.age ? other + ": Father" : other + ": Son";
        } else if (this.theLevel == Relationship.Level.FATHERDAUGHTER) {
            return fother.age > folk.age ? other + ": Father" : other + ": Daughter";
        } else if (this.theLevel == Relationship.Level.SISTERSISTER) {
            return other + ": Sister";
        } else if (this.theLevel == Relationship.Level.BROTHERBROTHER) {
            return other + ": Brother";
        } else if (this.theLevel == Relationship.Level.SISTERBROTHER) {
            return fother.gender == 0 ? other + ": Brother" : other + ": Sister";
        } else if (this.theLevel == Relationship.Level.GRANDFATHERDAUGHTER) {
            return fother.age > folk.age ? other + ": Grandfather" : other + ": Granddaughter";
        } else if (this.theLevel == Relationship.Level.GRANDFATHERSON) {
            return fother.age > folk.age ? other + ": Grandfather" : other + ": Grandson";
        } else if (this.theLevel == Relationship.Level.GRANDMOTHERDAUGHTER) {
            return fother.age > folk.age ? other + ": Grandmother" : other + ": Granddaughter";
        } else if (this.theLevel == Relationship.Level.GRANDMOTHERSON) {
            return fother.age > folk.age ? other + ": Grandmother" : other + ": Grandson";
        } else if (this.theLevel == Relationship.Level.AUNTNEPHEW) {
            return fother.age > folk.age ? other + ": Aunt" : other + ": Nephew";
        } else if (this.theLevel == Relationship.Level.AUNTNEICE) {
            return fother.age > folk.age ? other + ": Aunt" : other + ": Neice";
        } else if (this.theLevel == Relationship.Level.UNCLENEPHEW) {
            return fother.age > folk.age ? other + ": Uncle" : other + ": Nephew";
        } else if (this.theLevel == Relationship.Level.UNCLENEICE) {
            return fother.age > folk.age ? other + ": Uncle" : other + ": Neice";
        } else {
            return other + ": has an unknown relationship";
        }
    }

    public static FolkData getMotherOf(FolkData sonDaughter) {
        ArrayList<Relationship> rels = getRelationshipsFor(sonDaughter);
        Iterator i$ = rels.iterator();

        Relationship rel;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            rel = (Relationship)i$.next();
        } while(rel.theLevel != Relationship.Level.MOTHERDAUGHTER && rel.theLevel != Relationship.Level.MOTHERSON);

        return rel.folk1.age > rel.folk2.age ? FolkData.getFolkByName(rel.folk1.name) : FolkData.getFolkByName(rel.folk2.name);
    }

    public static void addRelationship(Relationship rel) {
        boolean got = false;
        Iterator i$ = ModSimukraft.theRelationships.iterator();

        while(i$.hasNext()) {
            Relationship relation = (Relationship)i$.next();

            try {
                if (relation.folk1.name.contentEquals(rel.folk1.name) && relation.folk2.name.contentEquals(rel.folk2.name)) {
                    got = true;
                }

                if (relation.folk1.name.contentEquals(rel.folk2.name) && relation.folk2.name.contentEquals(rel.folk1.name)) {
                    got = true;
                }
            } catch (Exception var5) {
            }
        }

        if (!got) {
            ModSimukraft.theRelationships.add(rel);
        }

    }

    public static void setupBloodRelationships(FolkData newChild, FolkData father, FolkData mother) {
        ArrayList<Relationship> mothers = getRelationshipsFor(mother);
        Iterator i$ = mothers.iterator();

        while(i$.hasNext()) {
            Relationship rel = (Relationship)i$.next();
            FolkData other;
            if (rel.folk1.name.contentEquals(mother.name)) {
                other = rel.folk2;
            } else {
                other = rel.folk1;
            }

            if (rel.theLevel == Relationship.Level.MOTHERDAUGHTER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.SISTERBROTHER, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.SISTERSISTER, true));
                }
            } else if (rel.theLevel == Relationship.Level.MOTHERSON) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.BROTHERBROTHER, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.SISTERBROTHER, true));
                }
            }

            if (rel.theLevel == Relationship.Level.MOTHERDAUGHTER && !rel.folk1.name.contentEquals(newChild.name) && !rel.folk2.name.contentEquals(newChild.name)) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDMOTHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDMOTHERDAUGHTER, true));
                }
            } else if (rel.theLevel == Relationship.Level.FATHERDAUGHTER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDFATHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDFATHERDAUGHTER, true));
                }
            }

            if (rel.theLevel == Relationship.Level.SISTERBROTHER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.UNCLENEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.UNCLENEICE, true));
                }
            } else if (rel.theLevel == Relationship.Level.SISTERSISTER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.AUNTNEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.AUNTNEICE, true));
                }
            }
        }

        ArrayList<Relationship> fathers = getRelationshipsFor(father);
        Iterator i$ = fathers.iterator();

        while(i$.hasNext()) {
            Relationship rel = (Relationship)i$.next();
            FolkData other;
            if (rel.folk1.name.contentEquals(father.name)) {
                other = rel.folk2;
            } else {
                other = rel.folk1;
            }

            if (rel.theLevel == Relationship.Level.FATHERSON && !rel.folk1.name.contentEquals(newChild.name) && !rel.folk2.name.contentEquals(newChild.name)) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDFATHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDFATHERDAUGHTER, true));
                }
            } else if (rel.theLevel == Relationship.Level.MOTHERSON) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDMOTHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.GRANDMOTHERDAUGHTER, true));
                }
            }

            if (rel.theLevel == Relationship.Level.SISTERBROTHER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.AUNTNEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.AUNTNEICE, true));
                }
            } else if (rel.theLevel == Relationship.Level.BROTHERBROTHER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.UNCLENEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Relationship.Level.UNCLENEICE, true));
                }
            }
        }

        if (newChild.gender == 0) {
            addRelationship(new Relationship(newChild, mother, Relationship.Level.MOTHERSON, true));
        } else {
            addRelationship(new Relationship(newChild, mother, Relationship.Level.MOTHERDAUGHTER, true));
        }

        if (newChild.gender == 0) {
            addRelationship(new Relationship(newChild, father, Relationship.Level.FATHERSON, true));
        } else {
            addRelationship(new Relationship(newChild, father, Relationship.Level.FATHERDAUGHTER, true));
        }

    }

    public String toFullString() {
        String folk2name = "";
        if (this.folk2 == null) {
            folk2name = "You";
        } else {
            folk2name = this.folk2.name;
        }

        return this.folk1.name + " " + this.toString() + " " + folk2name;
    }

    public void levelIncrease(int byAmount) {
        String oldLevel = this.toFullString();
        ModSimukraft.log.info("Relationship: + current level and sublevel:" + this.theLevel.toString() + " " + this.theSubLevel);
        this.theSubLevel += byAmount;
        if (this.theSubLevel > 100) {
            if (this.theLevel == Relationship.Level.AQUAINTANCE) {
                this.theLevel = Relationship.Level.FRIEND;
                this.theSubLevel = 0;
            } else if (this.theLevel == Relationship.Level.BESTFRIENDS) {
                if (this.folk2 == null) {
                    this.theSubLevel = 100;
                } else if (this.folk1.gender != this.folk2.gender && this.folk1.getHome() != null && this.folk2.getHome() != null && !isFolkLivingWithSomeone(this.folk1) && !isFolkLivingWithSomeone(this.folk2) && this.folk1.age >= 18 && this.folk2.age >= 18 && !this.isBloodRelation) {
                    this.theSubLevel = 50;
                    if (this.rand.nextBoolean()) {
                        this.theLevel = Relationship.Level.MARRIED;
                        this.changeFemaleSurname();
                    } else {
                        this.theLevel = Relationship.Level.PARTNER;
                    }

                    Building oldhome = this.folk1.getHome();
                    Building newhome = this.folk2.getHome();
                    if (oldhome != null) {
                        oldhome.removeTennant(this.folk1.name);
                    }

                    if (newhome != null) {
                        newhome.tennants.add(this.folk1.name);
                    }

                    Building.saveAllBuildings();
                    if (this.folk1.employedAt == null && this.folk2.employedAt == null) {
                        try {
                            this.folk1.action = FolkAction.GOINGHOME;
                            this.folk1.actionArrival = FolkAction.ATHOME;
                            this.folk1.gotoXYZ(this.folk1.getHome().primaryXYZ, (GotoMethod)null);
                            this.folk2.action = FolkAction.GOINGHOME;
                            this.folk2.actionArrival = FolkAction.ATHOME;
                            this.folk2.gotoXYZ(this.folk2.getHome().primaryXYZ, (GotoMethod)null);
                        } catch (Exception var6) {
                        }
                    }
                }
            } else if (this.theLevel == Relationship.Level.DESPISE) {
                this.theLevel = Relationship.Level.HATE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.DISLIKE) {
                this.theLevel = Relationship.Level.FRIEND;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.ENEMY) {
                this.theLevel = Relationship.Level.DESPISE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.FRIEND) {
                this.theLevel = Relationship.Level.GOODFRIEND;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.GOODFRIEND) {
                this.theLevel = Relationship.Level.BESTFRIENDS;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.HATE) {
                this.theLevel = Relationship.Level.DISLIKE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.MARRIED) {
                this.theSubLevel = 100;
            } else if (this.theLevel == Relationship.Level.PARTNER) {
                this.theSubLevel = 100;
            }

            if (!this.toFullString().contentEquals(oldLevel)) {
                this.notifyRelationshipChange();
            }
        }

    }

    private void changeFemaleSurname() {
        FolkData femaleFolk;
        FolkData maleFolk;
        if (this.folk1.gender == 1) {
            femaleFolk = this.folk1;
            maleFolk = this.folk2;
        } else {
            femaleFolk = this.folk2;
            maleFolk = this.folk1;
        }

        for(int b = 0; b < ModSimukraft.theBuildings.size(); ++b) {
            Building building = (Building)ModSimukraft.theBuildings.get(b);
            if (building != null && femaleFolk.getHome() != null && building.primaryXYZ.isSameCoordsAs(femaleFolk.getHome().primaryXYZ, true, false)) {
                building.removeTennant(femaleFolk.name);
            }
        }

        File f = new File(ModSimukraft.getSavesDataFolder() + "Folks" + File.separator + femaleFolk.name + ".sk2");
        f.delete();
        String surname = maleFolk.name.substring(maleFolk.name.indexOf(" ") + 1).trim();
        int m = femaleFolk.name.indexOf(" ");
        femaleFolk.name = femaleFolk.name.substring(0, m).trim() + " " + surname;
    }

    public void levelDecrease(int byAmount) {
        String oldLevel = this.toFullString();
        this.theSubLevel -= byAmount;
        if (this.theSubLevel < 0) {
            if (this.theLevel == Relationship.Level.AQUAINTANCE) {
                this.theLevel = Relationship.Level.DISLIKE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.BESTFRIENDS) {
                if (this.folk2 == null) {
                    this.theSubLevel = 50;
                } else {
                    this.theLevel = Relationship.Level.GOODFRIEND;
                    this.theSubLevel = 50;
                }
            } else if (this.theLevel == Relationship.Level.DESPISE) {
                this.theLevel = Relationship.Level.ENEMY;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.DISLIKE) {
                this.theLevel = Relationship.Level.HATE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.ENEMY) {
                this.theSubLevel = 0;
            } else if (this.theLevel == Relationship.Level.FRIEND) {
                this.theLevel = Relationship.Level.DISLIKE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.GOODFRIEND) {
                this.theLevel = Relationship.Level.FRIEND;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.HATE) {
                this.theLevel = Relationship.Level.DESPISE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.MARRIED) {
                this.theSubLevel = 50;
            } else if (this.theLevel == Relationship.Level.PARTNER) {
                this.theSubLevel = 50;
            }

            if (!this.toFullString().contentEquals(oldLevel)) {
                this.notifyRelationshipChange();
            }
        }

    }

    private void notifyRelationshipChange() {
        if (this.folk2 == null || this.theLevel == Relationship.Level.MARRIED || this.theLevel == Relationship.Level.PARTNER) {
            ModSimukraft.sendChat(this.toFullString().replaceAll(" is ", " is now "));
        }

    }

    public static void loadRelationships() {
        File relFiles = new File(ModSimukraft.getSavesDataFolder() + "Relationships" + File.separator);
        relFiles.mkdirs();
        boolean useNewFormat = false;
        File[] arr$ = relFiles.listFiles();
        int len$ = arr$.length;

        int i$;
        File f;
        for(i$ = 0; i$ < len$; ++i$) {
            f = arr$[i$];
            if (f.getName().endsWith(".sk2")) {
                useNewFormat = true;
                break;
            }
        }

        if (useNewFormat) {
            ModSimukraft.theRelationships.clear();
            arr$ = relFiles.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".sk2")) {
                    ArrayList<String> strings = ModSimukraft.loadSK2(f.getAbsoluteFile().toString());
                    Relationship rel = new Relationship();
                    Iterator i$ = strings.iterator();

                    while(i$.hasNext()) {
                        String line = (String)i$.next();
                        if (line.contains("|")) {
                            int m1 = line.indexOf("|");
                            String name = line.substring(0, m1);
                            String value = line.substring(m1 + 1);
                            FolkData folk;
                            if (name.contentEquals("folk1")) {
                                if (!value.contentEquals("") && !value.contentEquals("null")) {
                                    folk = FolkData.getFolkByName(value);
                                    if (folk != null) {
                                        rel.folk1 = folk;
                                    }
                                }
                            } else if (name.contentEquals("folk2")) {
                                if (!value.contentEquals("") && !value.contentEquals("null")) {
                                    folk = FolkData.getFolkByName(value);
                                    if (folk != null) {
                                        rel.folk2 = folk;
                                    }
                                }
                            } else if (name.contentEquals("level")) {
                                rel.theLevel = Relationship.Level.valueOf(value);
                            } else if (name.contentEquals("sublevel")) {
                                rel.theSubLevel = Integer.parseInt(value);
                            } else if (name.contentEquals("bloodrelation")) {
                                rel.isBloodRelation = Boolean.parseBoolean(value);
                            }
                        }
                    }

                    if (rel.folk1 != null && rel.folk2 != null) {
                        addRelationship(rel);
                    }
                }
            }
        } else {
            arr$ = relFiles.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".suk")) {
                    Relationship rel = (Relationship)ModSimukraft.proxy.loadObject(f.getAbsoluteFile().toString());
                    if (rel != null) {
                        addRelationship(rel);
                    }
                }
            }
        }

    }

    public static void saveRelationships() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER) {
            ArrayList<String> strings = new ArrayList();

            for(int b = 0; b < ModSimukraft.theRelationships.size(); ++b) {
                try {
                    Relationship rel = (Relationship)ModSimukraft.theRelationships.get(b);
                    String fn = rel.folk1.name.replaceAll(" ", "") + rel.folk2.name.replaceAll(" ", "");
                    strings.clear();
                    strings.add("folk1|" + rel.folk1.name);
                    strings.add("folk2|" + rel.folk2.name);
                    strings.add("level|" + rel.theLevel.name());
                    strings.add("sublevel|" + rel.theSubLevel);
                    strings.add("bloodrelation|" + rel.isBloodRelation);
                    ModSimukraft.saveSK2(ModSimukraft.getSavesDataFolder() + "Relationships" + File.separator + fn + ".sk2", strings);
                } catch (Exception var5) {
                }
            }
        }

    }

    public static void meddleWithRelationship(FolkData folk1, FolkData folk2) {
        if (folk1.name.contentEquals(folk2.name)) {
            ModSimukraft.log.warning("Relationship: meddleWithRelationship() with same folk for both");
        } else {
            Relationship rel = getRelationshipBetween(folk1, folk2);
            if (rel == null) {
                addRelationship(new Relationship(folk1, folk2, Relationship.Level.AQUAINTANCE, false));
            } else {
                Random r = new Random();
                int rr = r.nextInt(5);
                if (rr == 0) {
                    rel.levelDecrease(r.nextInt(30));
                } else {
                    rel.levelIncrease(r.nextInt(30));
                }
            }

        }
    }

    public static Relationship getRelationshipBetween(FolkData folk1, FolkData folk2) {
        for(int b = 0; b < ModSimukraft.theRelationships.size(); ++b) {
            Relationship rel = (Relationship)ModSimukraft.theRelationships.get(b);

            try {
                if (folk2 == null && rel.folk2 == null && rel.folk1.name.contentEquals(folk1.name)) {
                    return rel;
                }

                if (rel.folk1.name.contentEquals(folk1.name) && rel.folk2.name.contentEquals(folk2.name)) {
                    return rel;
                }

                if (rel.folk1.name.contentEquals(folk2.name) && rel.folk2.name.contentEquals(folk1.name)) {
                    return rel;
                }
            } catch (Exception var5) {
            }
        }

        return null;
    }

    public static ArrayList<Relationship> getRelationshipsFor(FolkData theFolk) {
        ArrayList<Relationship> rels = new ArrayList();

        for(int i = 0; i < ModSimukraft.theRelationships.size(); ++i) {
            try {
                Relationship rel = (Relationship)ModSimukraft.theRelationships.get(i);
                if (rel.folk1.name.contentEquals(theFolk.name) || rel.folk2.name.contentEquals(theFolk.name)) {
                    rels.add(rel);
                }
            } catch (Exception var4) {
            }
        }

        return rels;
    }

    public static boolean isFolkLivingWithSomeone(FolkData theFolk) {
        ArrayList<Relationship> rels = getRelationshipsFor(theFolk);
        boolean ret = false;

        for(int i = 0; i < rels.size(); ++i) {
            Relationship rel = (Relationship)rels.get(i);
            if (rel.theLevel == Relationship.Level.MARRIED || rel.theLevel == Relationship.Level.PARTNER) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public static FolkData isFolkLivingWithSomeone(FolkData theFolk, boolean returnFolk) {
        ArrayList<Relationship> rels = getRelationshipsFor(theFolk);

        for(int i = 0; i < rels.size(); ++i) {
            Relationship rel = (Relationship)rels.get(i);
            if (rel.theLevel == Relationship.Level.MARRIED || rel.theLevel == Relationship.Level.PARTNER) {
                return rel.folk1.name.contentEquals(theFolk.name) ? FolkData.getFolkByName(rel.folk2.name) : FolkData.getFolkByName(rel.folk1.name);
            }
        }

        return null;
    }

    public static enum Level {
        ENEMY,
        DESPISE,
        HATE,
        DISLIKE,
        AQUAINTANCE,
        FRIEND,
        GOODFRIEND,
        BESTFRIENDS,
        PARTNER,
        MARRIED,
        MOTHERDAUGHTER,
        MOTHERSON,
        FATHERDAUGHTER,
        FATHERSON,
        SISTERSISTER,
        BROTHERBROTHER,
        SISTERBROTHER,
        GRANDMOTHERDAUGHTER,
        GRANDMOTHERSON,
        GRANDFATHERDAUGHTER,
        GRANDFATHERSON,
        AUNTNEPHEW,
        AUNTNEICE,
        UNCLENEPHEW,
        UNCLENEICE;

        private Level() {
        }
    }
}
