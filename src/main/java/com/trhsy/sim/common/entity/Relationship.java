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
import java.util.concurrent.CopyOnWriteArrayList;

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
        try {
            this.theLevel = Level.AQUAINTANCE;
            this.theSubLevel = 0;
            this.isBloodRelation = false;
            this.rand = new Random();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("Relationship出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public Relationship(FolkData folk1, FolkData folk2, Level startingLevel, boolean isBlood) {

        try {
            this.theLevel = Level.AQUAINTANCE;
            this.theSubLevel = 0;
            this.isBloodRelation = false;
            this.rand = new Random();
            this.folk1 = folk1;
            this.folk2 = folk2;
            this.theLevel = startingLevel;
            this.isBloodRelation = isBlood;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("Relationship出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public String toString() {
        String s = null;
        try {
            if (this.theLevel == Level.AQUAINTANCE) {
                //是熟人
                s = I18n.format("container.sim.relation_ship_aquaintance");
            } else if (this.theLevel == Level.BESTFRIENDS) {
                //是最好的朋友
                s = I18n.format("container.sim.relation_ship_friends");
            } else if (this.theLevel == Level.DESPISE) {
                //蔑视
                s = I18n.format("container.sim.relation_ship_despises");
            } else if (this.theLevel == Level.DISLIKE) {
                //不喜欢
                s = I18n.format("container.sim.relation_ship_dislikes");
            } else if (this.theLevel == Level.ENEMY) {
                //是敌人
                s = I18n.format("container.sim.relation_ship_ememy");
            } else if (this.theLevel == Level.FRIEND) {
                //是朋友
                s = I18n.format("container.sim.relation_ship_is_friends");
            } else if (this.theLevel == Level.GOODFRIEND) {
                //是好朋友
                s = I18n.format("container.sim.relation_ship_good_friends");
            } else if (this.theLevel == Level.HATE) {
                //讨厌
                s = I18n.format("container.sim.relation_ship_hates");
            } else if (this.theLevel == Level.MARRIED) {
                //结婚了
                s = I18n.format("container.sim.relation_ship_married");
            } else {
                //生活再一起    有未知的关系
                s = this.theLevel == Level.PARTNER ? I18n.format("container.sim.relation_ship_living") : I18n.format("container.sim.relation_ship_relationship");
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toString出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return s;
    }

    public String toStringPersepctive(FolkData folk) {
        String other = "";
        FolkData fother;
        try {
            if (folk.name.contentEquals(this.folk1.name)) {
                other = this.folk2.name;
                fother = this.folk2;
            } else {
                other = this.folk1.name;
                fother = this.folk1;
            }

            if (this.theLevel == Level.AQUAINTANCE) {
                other = other + ": " + I18n.format("container.sim.relation_ship_level_Aquaintance");
            } else if (this.theLevel == Level.BESTFRIENDS) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Best_friends");
            } else if (this.theLevel == Level.DESPISE) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Despise");
            } else if (this.theLevel == Level.DISLIKE) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Dislike");
            } else if (this.theLevel == Level.ENEMY) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Enemy");
            } else if (this.theLevel == Level.FRIEND) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Friends_");
            } else if (this.theLevel == Level.GOODFRIEND) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Good_friends");
            } else if (this.theLevel == Level.HATE) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Hate");
            } else if (this.theLevel == Level.MARRIED) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Married");
            } else if (this.theLevel == Level.PARTNER) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Partner");
            } else if (this.theLevel == Level.MOTHERDAUGHTER) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Mother") : other + ": " + I18n.format("container.sim.relation_ship_Daughter");
            } else if (this.theLevel == Level.MOTHERSON) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Mother") : other + ": " + I18n.format("container.sim.relation_ship_Son");
            } else if (this.theLevel == Level.FATHERSON) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Father") : other + ": " + I18n.format("container.sim.relation_ship_Son");
            } else if (this.theLevel == Level.FATHERDAUGHTER) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Father") : other + ": " + I18n.format("container.sim.relation_ship_Daughter");
            } else if (this.theLevel == Level.SISTERSISTER) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Sister");
            } else if (this.theLevel == Level.BROTHERBROTHER) {
                other = other + ": " + I18n.format("container.sim.relation_ship_Brother");
            } else if (this.theLevel == Level.SISTERBROTHER) {
                other = fother.gender == 0 ? other + ": " + I18n.format("container.sim.relation_ship_Brother") : other + ": " + I18n.format("container.sim.relation_ship_Sister");
            } else if (this.theLevel == Level.GRANDFATHERDAUGHTER) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandfather") : other + ": " + I18n.format("container.sim.relation_ship_Granddaughter");
            } else if (this.theLevel == Level.GRANDFATHERSON) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandfather") : other + ": " + I18n.format("container.sim.relation_ship_Grandson");
            } else if (this.theLevel == Level.GRANDMOTHERDAUGHTER) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandmother") : other + ": " + I18n.format("container.sim.relation_ship_Granddaughter");
            } else if (this.theLevel == Level.GRANDMOTHERSON) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Grandmother") : other + ": " + I18n.format("container.sim.relation_ship_Grandson");
            } else if (this.theLevel == Level.AUNTNEPHEW) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Aunt") : other + ": " + I18n.format("container.sim.relation_ship_Nephew");
            } else if (this.theLevel == Level.AUNTNEICE) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Aunt") : other + ": " + I18n.format("container.sim.relation_ship_Neice");
            } else if (this.theLevel == Level.UNCLENEPHEW) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Uncle") : other + ": " + I18n.format("container.sim.relation_ship_Nephew");
            } else if (this.theLevel == Level.UNCLENEICE) {
                other = fother.age > folk.age ? other + ": " + I18n.format("container.sim.relation_ship_Uncle") : other + ": " + I18n.format("container.sim.relation_ship_Neice");
            } else {
                other = other + ": " + I18n.format("container.sim.relation_ship_relationship");
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toStringPersepctive出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return other;
    }

    public static FolkData getMotherOf(FolkData sonDaughter) {
        FolkData folkData = null;
        try {
            CopyOnWriteArrayList<Relationship> rels = getRelationshipsFor(sonDaughter);
            Iterator i$ = rels.iterator();

            Relationship rel;
            do {
                if (!i$.hasNext()) {
                    return null;
                }

                rel = (Relationship) i$.next();
            } while (rel.theLevel != Level.MOTHERDAUGHTER && rel.theLevel != Level.MOTHERSON);
            folkData = rel.folk1.age > rel.folk2.age ? FolkData.getFolkByName(rel.folk1.name) : FolkData.getFolkByName(rel.folk2.name);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getMotherOf出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return folkData;
    }

    public static void addRelationship(Relationship rel) {
        boolean got = false;
        try {
            for (Relationship relation : ModSimReloaded.theRelationships) {
                if (relation.folk1.name.contentEquals(rel.folk1.name) && relation.folk2.name.contentEquals(rel.folk2.name)) {
                    got = true;
                }

                if (relation.folk1.name.contentEquals(rel.folk2.name) && relation.folk2.name.contentEquals(rel.folk1.name)) {
                    got = true;
                }
            }

            if (!got) {
                ModSimReloaded.theRelationships.add(rel);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("addRelationship出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static void setupBloodRelationships(FolkData newChild, FolkData father, FolkData mother) {
        try {
            CopyOnWriteArrayList<Relationship> mothers = getRelationshipsFor(mother);
            for (Relationship rel : mothers) {
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

            CopyOnWriteArrayList<Relationship> fathers = getRelationshipsFor(father);
            for (Relationship rel : fathers) {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setupBloodRelationships出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    /**
     * 到完整字符串
     *
     * @return
     */
    public String toFullString() {
        String folk2name = "";
        try {
            if (this.folk2 == null) {
                folk2name = "You";
            } else {
                folk2name = this.folk2.name;
            }
            folk2name = this.folk1.name + " " + this.toString() + " " + folk2name;

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toFullString出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return folk2name;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 等级提升
     * @Date 21:56 2022/7/9
     * @Param [byAmount]
     **/
    public void levelIncrease(int byAmount) {
        try {
            String oldLevel = this.toFullString();
            ModSimReloaded.log.info("Relationship: 【"+this.folk1.name+"】和【"+this.folk2.name+"】 当前级别和子级别:" + this.theLevel.toString() + " " + this.theSubLevel);
            this.theSubLevel += byAmount;
            if (this.theSubLevel > 100) {
                //熟人
                if (this.theLevel == Level.AQUAINTANCE) {
                    //朋友
                    this.theLevel = Level.FRIEND;
                    this.theSubLevel = 0;
                    //最好的朋友
                } else if (this.theLevel == Level.BESTFRIENDS) {
                    if (this.folk2 == null) {
                        this.theSubLevel = 100;
                        //性别不同并且 两个人住所不为空 没有和人住一起 没有血缘关系
                    } else if (this.folk1.gender != this.folk2.gender){
                        if(this.folk1.getHome() != null && this.folk2.getHome() != null ){
                            if(!isFolkLivingWithSomeone(this.folk1) && !isFolkLivingWithSomeone(this.folk2)){
                                if(this.folk1.age >= 18 && this.folk2.age >= 18 ){
                                    if(!this.isBloodRelation){
                                        this.theSubLevel = 50;
                                        if (this.rand.nextBoolean()) {
                                            //已婚
                                            this.theLevel = Level.MARRIED;
                                            this.changeFemaleSurname();
                                        } else {
                                            //伙伴
                                            this.theLevel = Level.PARTNER;
                                        }
                                        //两个人住一起
                                        Building oldhome = this.folk1.getHome();
                                        Building newhome = this.folk2.getHome();
                                        if (oldhome != null) {
                                            oldhome.removeTennant(this.folk1.name);
                                        }
                                        if (newhome != null) {
                                            newhome.tenants.add(this.folk1.name);
                                        }
                                        //保存
                                        Building.saveAllBuildings();
                                        if (this.folk1.employedAt == null && this.folk2.employedAt == null) {
                                            this.folk1.action = FolkAction.GOINGHOME;
                                            this.folk1.actionArrival = FolkAction.ATHOME;
                                            V3 v3 = this.folk1.getHome().primaryXYZ;
                                            this.folk1.gotoXYZ(v3, null);

                                            this.folk2.action = FolkAction.GOINGHOME;
                                            this.folk2.actionArrival = FolkAction.ATHOME;
                                            V3 v32 = this.folk2.getHome().primaryXYZ;
                                            this.folk2.gotoXYZ(v32, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //轻视
                } else if (this.theLevel == Level.DESPISE) {
                    //仇恨
                    this.theLevel = Level.HATE;
                    this.theSubLevel = 50;
                    //不喜欢
                } else if (this.theLevel == Level.DISLIKE) {
                    //朋友
                    this.theLevel = Level.FRIEND;
                    this.theSubLevel = 50;
                    //敌人
                } else if (this.theLevel == Level.ENEMY) {
                    //轻视
                    this.theLevel = Level.DESPISE;
                    this.theSubLevel = 50;
                    //朋友
                } else if (this.theLevel == Level.FRIEND) {
                    //好朋友
                    this.theLevel = Level.GOODFRIEND;
                    this.theSubLevel = 50;
                    //好朋友
                } else if (this.theLevel == Level.GOODFRIEND) {
                    //最好的朋友
                    this.theLevel = Level.BESTFRIENDS;
                    this.theSubLevel = 50;
                    //仇恨
                } else if (this.theLevel == Level.HATE) {
                    //不喜欢
                    this.theLevel = Level.DISLIKE;
                    this.theSubLevel = 50;
                    //已婚
                } else if (this.theLevel == Level.MARRIED) {
                    this.theSubLevel = 100;
                    //配偶
                } else if (this.theLevel == Level.PARTNER) {
                    this.theSubLevel = 100;
                }

                if (!this.toFullString().contentEquals(oldLevel)) {
                    this.notifyRelationshipChange();
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("levelIncrease出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 更改女性姓氏
     * @Date 21:43 2022/7/9
     * @Param []
     **/
    private void changeFemaleSurname() {
        try {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("更改女性姓氏出问题了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public void levelDecrease(int byAmount) {
        try {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("levelDecrease出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    private void notifyRelationshipChange() {
        try {
            if (this.folk2 == null || this.theLevel == Level.MARRIED || this.theLevel == Level.PARTNER) {
                ModSimReloaded.sendChat(this.toFullString().replaceAll(" is ", " is now "));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("notifyRelationshipChange出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static void loadRelationships() {
        try {
            File relFiles = new File(ModSimReloaded.getSavesDataFolder() + "Relationships" + File.separator);
            relFiles.mkdirs();
            boolean useNewFormat = false;
            File[] files = relFiles.listFiles();
            File f = null;
            for (int i = 0; i < files.length; i++) {
                f = files[i];
                if (f.getName().endsWith(".sk2")) {
                    useNewFormat = true;
                    break;
                }
            }

            if (useNewFormat) {
                ModSimReloaded.theRelationships.clear();
                for (int i = 0; i < files.length; i++) {
                    f = files[i];
                    if (f.getName().endsWith(".sk2")) {
                        CopyOnWriteArrayList<String> strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                        Relationship rel = new Relationship();
                        Iterator iterator = strings.iterator();
                        for (String line : strings) {
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
                for (int i = 0; i < files.length; i++) {
                    f = files[i];
                    if (f.getName().endsWith(".suk")) {
                        Relationship rel = (Relationship) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
                        if (rel != null) {
                            addRelationship(rel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("loadRelationships出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 保存关系
     */
    public static void saveRelationships() {
        try {
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.SERVER) {
                CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList();

                for (int b = 0; b < ModSimReloaded.theRelationships.size(); ++b) {
                    try {
                        Relationship rel = (Relationship) ModSimReloaded.theRelationships.get(b);
                        String fn = rel.folk1.name.replaceAll(" ", "") + rel.folk2.name.replaceAll(" ", "");
                        strings.clear();
                        strings.add("folk1|" + rel.folk1.name);
                        strings.add("folk2|" + rel.folk2.name);
                        strings.add("level|" + rel.theLevel.name());
                        strings.add("sublevel|" + rel.theSubLevel);
                        //血缘关系
                        strings.add("bloodrelation|" + rel.isBloodRelation);
                        ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Relationships" + File.separator + fn + ".sk2", strings);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("saveRelationships出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 干涉关系
     *
     * @param folk1
     * @param folk2
     */
    public static void meddleWithRelationship(FolkData folk1, FolkData folk2) {
        try {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("meddleWithRelationship出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 获取之间的关系
     *
     * @param folk1
     * @param folk2
     * @return
     */
    public static Relationship getRelationshipBetween(FolkData folk1, FolkData folk2) {
        try {
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
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getRelationshipBetween出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    /**
     * 获取关系
     *
     * @param theFolk
     * @return
     */
    public static CopyOnWriteArrayList<Relationship> getRelationshipsFor(FolkData theFolk) {
        CopyOnWriteArrayList<Relationship> rels = new CopyOnWriteArrayList();
        try {
            for (int i = 0; i < ModSimReloaded.theRelationships.size(); i++) {
                Relationship rel = (Relationship) ModSimReloaded.theRelationships.get(i);
                if (rel.folk1.name.contentEquals(theFolk.name) || rel.folk2.name.contentEquals(theFolk.name)) {
                    rels.add(rel);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getRelationshipsFor出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return rels;
    }

    /**
     * 是和某人一起生活吗
     *
     * @param theFolk
     * @return
     */
    public static boolean isFolkLivingWithSomeone(FolkData theFolk) {
        CopyOnWriteArrayList<Relationship> rels = getRelationshipsFor(theFolk);
        boolean ret = false;
        try {
            for (int i = 0; i < rels.size(); i++) {
                Relationship rel = (Relationship) rels.get(i);
                if (rel.theLevel == Level.MARRIED || rel.theLevel == Level.PARTNER) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("isFolkLivingWithSomeone出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 和某人在一起
     *
     * @param theFolk
     * @param returnFolk
     * @return
     */
    public static FolkData isFolkLivingWithSomeone(FolkData theFolk, boolean returnFolk) {
        CopyOnWriteArrayList<Relationship> rels = getRelationshipsFor(theFolk);
        try {
            for (int i = 0; i < rels.size(); i++) {
                Relationship rel = (Relationship) rels.get(i);
                if (rel.theLevel == Level.MARRIED || rel.theLevel == Level.PARTNER) {
                    return rel.folk1.name.contentEquals(theFolk.name) ? FolkData.getFolkByName(rel.folk2.name) : FolkData.getFolkByName(rel.folk1.name);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("isFolkLivingWithSomeone出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

}
