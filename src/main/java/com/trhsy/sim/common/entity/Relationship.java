package com.trhsy.sim.common.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.entity.enums.Level;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

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
    public Level theLevel;
    public int theSubLevel;
    public boolean isBloodRelation;
    private Random rand;

    public Relationship() {
        this.theLevel = Level.AQUAINTANCE;
        this.theSubLevel = 0;
        this.isBloodRelation = false;
        this.rand = new Random();
    }

    public Relationship(FolkData folk1, FolkData folk2, Level startingLevel, boolean isBlood) {
        this.theLevel = Level.AQUAINTANCE;
        this.theSubLevel = 0;
        this.isBloodRelation = false;
        this.rand = new Random();
        this.folk1 = folk1;
        this.folk2 = folk2;
        this.theLevel = startingLevel;
        this.isBloodRelation = isBlood;
    }

    @Override
    public String toString() {

        if (this.theLevel == Level.AQUAINTANCE) {
            return I18n.format("container.sim.relation_ship_aquaintance");
        } else if (this.theLevel == Level.BESTFRIENDS) {
            return I18n.format("container.sim.relation_ship_friends");
        } else if (this.theLevel == Level.DESPISE) {
            return I18n.format("container.sim.relation_ship_despises");
        } else if (this.theLevel == Level.DISLIKE) {
            return I18n.format("container.sim.relation_ship_dislikes");
        } else if (this.theLevel == Level.ENEMY) {
            return I18n.format("container.sim.relation_ship_ememy");
        } else if (this.theLevel == Level.FRIEND) {
            return I18n.format("container.sim.relation_ship_is_friends");
        } else if (this.theLevel == Level.GOODFRIEND) {
            return I18n.format("container.sim.relation_ship_good_friends");
        } else if (this.theLevel == Level.HATE) {
            return I18n.format("container.sim.relation_ship_hates");
        } else if (this.theLevel == Level.MARRIED) {
            return I18n.format("container.sim.relation_ship_married");
        } else {
            return this.theLevel == Level.PARTNER ? I18n.format("container.sim.relation_ship_living") : I18n.format("container.sim.relation_ship_relationship");
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

        if (this.theLevel == Level.AQUAINTANCE) {
            return other + ": " + I18n.format("container.sim.relation_ship_level_Aquaintance");
        } else if (this.theLevel == Level.BESTFRIENDS) {
            return other + ": " + I18n.format("container.sim.relation_ship_Best_friends");
        } else if (this.theLevel == Level.DESPISE) {
            return other + ": " + I18n.format("container.sim.relation_ship_Despise");
        } else if (this.theLevel == Level.DISLIKE) {
            return other + ": " + I18n.format("container.sim.relation_ship_Dislike");
        } else if (this.theLevel == Level.ENEMY) {
            return other + ": " + I18n.format("container.sim.relation_ship_Enemy");
        } else if (this.theLevel == Level.FRIEND) {
            return other + ": " + I18n.format("container.sim.relation_ship_Friends");
        } else if (this.theLevel == Level.GOODFRIEND) {
            return other + ": " + I18n.format("container.sim.relation_ship_Good_friends");
        } else if (this.theLevel == Level.HATE) {
            return other + ": " + I18n.format("container.sim.relation_ship_Hate");
        } else if (this.theLevel == Level.MARRIED) {
            return other + ": " + I18n.format("container.sim.relation_ship_Married");
        } else if (this.theLevel == Level.PARTNER) {
            return other + ": " + I18n.format("container.sim.relation_ship_Partner");
        } else if (this.theLevel == Level.MOTHERDAUGHTER) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Mother") : other + ": " + I18n.format("container.sim.relation_ship_Daughter");
        } else if (this.theLevel == Level.MOTHERSON) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Mother") : other + ": " + I18n.format("container.sim.relation_ship_Son");
        } else if (this.theLevel == Level.FATHERSON) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Father") : other + ": " + I18n.format("container.sim.relation_ship_Son");
        } else if (this.theLevel == Level.FATHERDAUGHTER) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Father") : other + ": " + I18n.format("container.sim.relation_ship_Daughter");
        } else if (this.theLevel == Level.SISTERSISTER) {
            return other + ": " + I18n.format("container.sim.relation_ship_Sister");
        } else if (this.theLevel == Level.BROTHERBROTHER) {
            return other + ": " + I18n.format("container.sim.relation_ship_Brother");
        } else if (this.theLevel == Level.SISTERBROTHER) {
            return fother.gender == 0 ? other + ": " + I18n.format("container.sim.relation_ship_Brother") : other + ": " + I18n.format("container.sim.relation_ship_Sister");
        } else if (this.theLevel == Level.GRANDFATHERDAUGHTER) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandfather") : other + ": " + I18n.format("container.sim.relation_ship_Granddaughter");
        } else if (this.theLevel == Level.GRANDFATHERSON) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandfather") : other + ": " + I18n.format("container.sim.relation_ship_Grandson");
        } else if (this.theLevel == Level.GRANDMOTHERDAUGHTER) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandmother") : other + ": " + I18n.format("container.sim.relation_ship_Granddaughter");
        } else if (this.theLevel == Level.GRANDMOTHERSON) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandmother") : other + ": " + I18n.format("container.sim.relation_ship_Grandson");
        } else if (this.theLevel == Level.AUNTNEPHEW) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Aunt") : other + ": " + I18n.format("container.sim.relation_ship_Nephew");
        } else if (this.theLevel == Level.AUNTNEICE) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Aunt") : other + ": " + I18n.format("container.sim.relation_ship_Neice");
        } else if (this.theLevel == Level.UNCLENEPHEW) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Uncle") : other + ": " + I18n.format("container.sim.relation_ship_Nephew");
        } else if (this.theLevel == Level.UNCLENEICE) {
            return fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Uncle") : other + ": " + I18n.format("container.sim.relation_ship_Neice");
        } else {
            return other + ": " + I18n.format("container.sim.relation_ship_relationship");
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
        } while(rel.theLevel != Level.MOTHERDAUGHTER && rel.theLevel != Level.MOTHERSON);

        return rel.folk1.age > rel.folk2.age ? FolkData.getFolkByName(rel.folk1.name) : FolkData.getFolkByName(rel.folk2.name);
    }

    public static void addRelationship(Relationship rel) {
        boolean got = false;
        Iterator i$ = ModSimReloaded.theRelationships.iterator();

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
            ModSimReloaded.theRelationships.add(rel);
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

            if (rel.theLevel == Level.MOTHERDAUGHTER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.SISTERBROTHER, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.SISTERSISTER, true));
                }
            } else if (rel.theLevel == Level.MOTHERSON) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.BROTHERBROTHER, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.SISTERBROTHER, true));
                }
            }

            if (rel.theLevel == Level.MOTHERDAUGHTER && !rel.folk1.name.contentEquals(newChild.name) && !rel.folk2.name.contentEquals(newChild.name)) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.GRANDMOTHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.GRANDMOTHERDAUGHTER, true));
                }
            } else if (rel.theLevel == Level.FATHERDAUGHTER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.GRANDFATHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.GRANDFATHERDAUGHTER, true));
                }
            }

            if (rel.theLevel == Level.SISTERBROTHER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.UNCLENEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.UNCLENEICE, true));
                }
            } else if (rel.theLevel == Level.SISTERSISTER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.AUNTNEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.AUNTNEICE, true));
                }
            }
        }

        ArrayList<Relationship> fathers = getRelationshipsFor(father);
        Iterator iterator = fathers.iterator();

        while(iterator.hasNext()) {
            Relationship rel = (Relationship)iterator.next();
            FolkData other;
            if (rel.folk1.name.contentEquals(father.name)) {
                other = rel.folk2;
            } else {
                other = rel.folk1;
            }

            if (rel.theLevel == Level.FATHERSON && !rel.folk1.name.contentEquals(newChild.name) && !rel.folk2.name.contentEquals(newChild.name)) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.GRANDFATHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.GRANDFATHERDAUGHTER, true));
                }
            } else if (rel.theLevel == Level.MOTHERSON) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.GRANDMOTHERSON, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.GRANDMOTHERDAUGHTER, true));
                }
            }

            if (rel.theLevel == Level.SISTERBROTHER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.AUNTNEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.AUNTNEICE, true));
                }
            } else if (rel.theLevel == Level.BROTHERBROTHER) {
                if (newChild.gender == 0) {
                    addRelationship(new Relationship(newChild, other, Level.UNCLENEPHEW, true));
                } else {
                    addRelationship(new Relationship(newChild, other, Level.UNCLENEICE, true));
                }
            }
        }

        if (newChild.gender == 0) {
            addRelationship(new Relationship(newChild, mother, Level.MOTHERSON, true));
        } else {
            addRelationship(new Relationship(newChild, mother, Level.MOTHERDAUGHTER, true));
        }

        if (newChild.gender == 0) {
            addRelationship(new Relationship(newChild, father, Level.FATHERSON, true));
        } else {
            addRelationship(new Relationship(newChild, father, Level.FATHERDAUGHTER, true));
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
        ModSimReloaded.log.info("Relationship: + 当前级别和子级别:" + this.theLevel.toString() + " " + this.theSubLevel);
        this.theSubLevel += byAmount;
        if (this.theSubLevel > 100) {
            if (this.theLevel == Level.AQUAINTANCE) {
                this.theLevel = Level.FRIEND;
                this.theSubLevel = 0;
            } else if (this.theLevel == Level.BESTFRIENDS) {
                if (this.folk2 == null) {
                    this.theSubLevel = 100;
                } else if (this.folk1.gender != this.folk2.gender && this.folk1.getHome() != null && this.folk2.getHome() != null && !isFolkLivingWithSomeone(this.folk1) && !isFolkLivingWithSomeone(this.folk2) && this.folk1.age >= 18 && this.folk2.age >= 18 && !this.isBloodRelation) {
                    this.theSubLevel = 50;
                    if (this.rand.nextBoolean()) {
                        this.theLevel = Level.MARRIED;
                        this.changeFemaleSurname();
                    } else {
                        this.theLevel = Level.PARTNER;
                    }

                    Building oldhome = this.folk1.getHome();
                    Building newhome = this.folk2.getHome();
                    if (oldhome != null) {
                        oldhome.removeTennant(this.folk1.name);
                    }

                    if (newhome != null) {
                        newhome.tenants.add(this.folk1.name);
                    }

                    Building.saveAllBuildings();
                    if (this.folk1.employedAt == null && this.folk2.employedAt == null) {
                        try {
                            this.folk1.action = FolkAction.GOINGHOME;
                            this.folk1.actionArrival = FolkAction.ATHOME;
                            V3 v3=this.folk1.getHome().primaryXYZ;
                            v3=new V3(v3.x+1.0,v3.y+1.0,v3.z,v3.theDimension);
                            this.folk1.gotoXYZ(v3, (GotoMethod)null);
                            this.folk2.action = FolkAction.GOINGHOME;
                            this.folk2.actionArrival = FolkAction.ATHOME;
                            V3 v32=this.folk2.getHome().primaryXYZ;
                            v32=new V3(v32.x+1.0,v32.y+1.0,v32.z,v32.theDimension);
                            this.folk2.gotoXYZ(v32, (GotoMethod)null);
                        } catch (Exception var6) {
                        }
                    }
                }
            } else if (this.theLevel == Level.DESPISE) {
                this.theLevel = Level.HATE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.DISLIKE) {
                this.theLevel = Level.FRIEND;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.ENEMY) {
                this.theLevel = Level.DESPISE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.FRIEND) {
                this.theLevel = Level.GOODFRIEND;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.GOODFRIEND) {
                this.theLevel = Level.BESTFRIENDS;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.HATE) {
                this.theLevel = Level.DISLIKE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.MARRIED) {
                this.theSubLevel = 100;
            } else if (this.theLevel == Level.PARTNER) {
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

        for (int b = 0; b < ModSimReloaded.theBuildings.size(); ++b) {
            Building building = (Building) ModSimReloaded.theBuildings.get(b);
            if (building != null && femaleFolk.getHome() != null && building.primaryXYZ.isSameCoordsAs(femaleFolk.getHome().primaryXYZ, true, false)) {
                building.removeTennant(femaleFolk.name);
            }
        }

        File f = new File(ModSimReloaded.getSavesDataFolder() + "Folks" + File.separator + femaleFolk.name + ".sk2");
        f.delete();
        String surname = maleFolk.name.substring(maleFolk.name.indexOf(" ") + 1).trim();
        int m = femaleFolk.name.indexOf(" ");
        femaleFolk.name = femaleFolk.name.substring(0, m).trim() + " " + surname;
    }

    public void levelDecrease(int byAmount) {
        String oldLevel = this.toFullString();
        this.theSubLevel -= byAmount;
        if (this.theSubLevel < 0) {
            if (this.theLevel == Level.AQUAINTANCE) {
                this.theLevel = Level.DISLIKE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.BESTFRIENDS) {
                if (this.folk2 == null) {
                    this.theSubLevel = 50;
                } else {
                    this.theLevel = Level.GOODFRIEND;
                    this.theSubLevel = 50;
                }
            } else if (this.theLevel == Level.DESPISE) {
                this.theLevel = Level.ENEMY;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.DISLIKE) {
                this.theLevel = Level.HATE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.ENEMY) {
                this.theSubLevel = 0;
            } else if (this.theLevel == Level.FRIEND) {
                this.theLevel = Level.DISLIKE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.GOODFRIEND) {
                this.theLevel = Level.FRIEND;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.HATE) {
                this.theLevel = Level.DESPISE;
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.MARRIED) {
                this.theSubLevel = 50;
            } else if (this.theLevel == Level.PARTNER) {
                this.theSubLevel = 50;
            }

            if (!this.toFullString().contentEquals(oldLevel)) {
                this.notifyRelationshipChange();
            }
        }

    }

    private void notifyRelationshipChange() {
        if (this.folk2 == null || this.theLevel == Level.MARRIED || this.theLevel == Level.PARTNER) {
            ModSimReloaded.sendChat(this.toFullString().replaceAll(" is ", " is now "));
        }

    }

    public static void loadRelationships() {
        File relFiles = new File(ModSimReloaded.getSavesDataFolder() + "Relationships" + File.separator);
        relFiles.mkdirs();
        boolean useNewFormat = false;
        File[] arr$ = relFiles.listFiles();
        int len$ = arr$.length;

        int i$;
        File f;
        for(i$ = 0; i$ < len$; i$++) {
            f = arr$[i$];
            if (f.getName().endsWith(".sk2")) {
                useNewFormat = true;
                break;
            }
        }

        if (useNewFormat) {
            ModSimReloaded.theRelationships.clear();
            arr$ = relFiles.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; i$++) {
                f = arr$[i$];
                if (f.getName().endsWith(".sk2")) {
                    ArrayList<String> strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                    Relationship rel = new Relationship();
                    Iterator iterator = strings.iterator();

                    while(iterator.hasNext()) {
                        String line = (String)iterator.next();
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
                                rel.theLevel = Level.valueOf(value);
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

            for(i$ = 0; i$ < len$; i$++) {
                f = arr$[i$];
                if (f.getName().endsWith(".suk")) {
                    Relationship rel = (Relationship) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
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

            for (int b = 0; b < ModSimReloaded.theRelationships.size(); ++b) {
                try {
                    Relationship rel = (Relationship) ModSimReloaded.theRelationships.get(b);
                    String fn = rel.folk1.name.replaceAll(" ", "") + rel.folk2.name.replaceAll(" ", "");
                    strings.clear();
                    strings.add("folk1|" + rel.folk1.name);
                    strings.add("folk2|" + rel.folk2.name);
                    strings.add("level|" + rel.theLevel.name());
                    strings.add("sublevel|" + rel.theSubLevel);
                    strings.add("bloodrelation|" + rel.isBloodRelation);
                    ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Relationships" + File.separator + fn + ".sk2", strings);
                } catch (Exception var5) {
                }
            }
        }

    }

    public static void meddleWithRelationship(FolkData folk1, FolkData folk2) {
        if (folk1.name.contentEquals(folk2.name)) {
            ModSimReloaded.log.warn("关系: 干涉关系() 两个人都是同一个人");
        } else {
            Relationship rel = getRelationshipBetween(folk1, folk2);
            if (rel == null) {
                addRelationship(new Relationship(folk1, folk2, Level.AQUAINTANCE, false));
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
        for (int b = 0; b < ModSimReloaded.theRelationships.size(); ++b) {
            Relationship rel = (Relationship) ModSimReloaded.theRelationships.get(b);

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

        for (int i = 0; i < ModSimReloaded.theRelationships.size(); i++) {
            try {
                Relationship rel = (Relationship) ModSimReloaded.theRelationships.get(i);
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

        for(int i = 0; i < rels.size(); i++) {
            Relationship rel = (Relationship)rels.get(i);
            if (rel.theLevel == Level.MARRIED || rel.theLevel == Level.PARTNER) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public static FolkData isFolkLivingWithSomeone(FolkData theFolk, boolean returnFolk) {
        ArrayList<Relationship> rels = getRelationshipsFor(theFolk);

        for(int i = 0; i < rels.size(); i++) {
            Relationship rel = (Relationship)rels.get(i);
            if (rel.theLevel == Level.MARRIED || rel.theLevel == Level.PARTNER) {
                return rel.folk1.name.contentEquals(theFolk.name) ? FolkData.getFolkByName(rel.folk2.name) : FolkData.getFolkByName(rel.folk1.name);
            }
        }

        return null;
    }
    
}
