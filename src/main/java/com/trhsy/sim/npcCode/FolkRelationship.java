package com.trhsy.sim.npcCode;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.enums.EnumFamilyType;
import com.trhsy.sim.npcCode.enums.EnumLevel;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: FolkRelationship
 * @Description: 情感信息
 * @date 2022/10/13 16:10
 */
public class FolkRelationship {
    public NpcData folk1;
    public String folk2;
    public EnumFamilyType familyType;
    //等级
    public EnumLevel level;
    //级别
    public int subLevel;

    public FolkRelationship(NpcData folk1, NpcData folk2, EnumFamilyType family) {
        try {
//不相关的
            this.familyType = EnumFamilyType.UNRELATED;
            //熟人
            this.level = EnumLevel.AQUAINTANCE;
            this.subLevel = 5;
            this.folk1 = folk1;
            this.folk2 = folk2.ID;
            this.familyType = family;
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("FolkRelationship出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    public FolkRelationship(NpcData folk1, String save) {
        try {
            this.familyType = EnumFamilyType.UNRELATED;
            this.level = EnumLevel.AQUAINTANCE;
            this.subLevel = 5;
            this.folk1 = folk1;
            this.folk2 = save.split(",")[0];
            this.familyType = EnumFamilyType.valueOf(save.split(",")[1]);
            this.level = EnumLevel.valueOf(save.split(",")[2]);
            this.subLevel = Integer.parseInt(save.split(",")[3]);
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("FolkRelationship1出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 获取反向
     * @return
     */
    public FolkRelationship getInverse() {
        FolkRelationship folkRelationship=null;
        try {
            NpcData npcData= this.getOther();
            if(this.folk1!=null&&npcData!=null){
                folkRelationship=npcData.getRelationshipWith(this.folk1);
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("getInverse出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return folkRelationship;
    }
    public FolkRelationship getNewInverse(NpcData npcData) {
        FolkRelationship folkRelationship=null;
        try {
            if(this.folk1!=null&&npcData!=null){
                folkRelationship=this.folk1.getRelationshipWith(npcData);
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("getInverse出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return folkRelationship;
    }
    /**
     * 尝试结婚
     */
    public void tryMarry() {
        try {
            NpcData folk2 = this.getOther();
            //单身狗
            String singe= new TextComponentTranslation("container.sim.folkData4",new Object[0]).getUnformattedText();
            //当前NPC不是空，情感对象不为空，是单身狗，
            if (this.folk1!=null&&folk2!=null&&this.folk1.getRelationshipStatus().equals(singe) && folk2.getRelationshipStatus().equals(singe)) {
                //双方性别不同 都成年了 都有房子
                if (this.folk1.gender != folk2.gender && this.folk1.age >= this.folk1.race.maturity && folk2.age >= folk2.race.maturity && this.folk1.home != null && folk2.home != null && this.familyType == EnumFamilyType.UNRELATED) {
                    String and=new TextComponentTranslation("container.sim.Mining13",new Object[0]).getUnformattedText();//和
                    String married=new TextComponentTranslation("container.sim.married",new Object[0]).getUnformattedText();//正在结婚！
                    String moving=new TextComponentTranslation("container.sim.moving",new Object[0]).getUnformattedText();//正在搬进
                    String moving1=new TextComponentTranslation("container.sim.moving1",new Object[0]).getUnformattedText();//的家

                    //仲孙锐翰和栾平怡正在结婚！仲孙锐翰 正在搬进 栾平怡的家.
                    String marriageMessage = this.folk1.getName() +and + folk2.getName() + married + this.folk1.getName() +moving + folk2.getName() + moving1+".";
                    ModSimLoader.sendChat(marriageMessage);
                    //取消更改妻子姓名
                /*if (this.folk1.gender == 1) {
                    this.folk1.surname = folk2.surname;
                } else {
                    folk2.surname = this.folk1.surname;
                }*/

                    //配偶
                    FolkRelationship folkRelationship1=this.folk1.getRelationshipWith(folk2);
                    if(folkRelationship1!=null){
                        folkRelationship1.familyType = EnumFamilyType.SPOUSE;
                        folkRelationship1.subLevel=9;
                        folkRelationship1.level=EnumLevel.BESTFRIENDS;
                    }

//                    this.familyType = EnumFamilyType.SPOUSE;
//                    this.subLevel=9;
//                    this.level=EnumLevel.BESTFRIENDS;
                    folk2.adjustRelationship(this.folk1,999);

                    FolkRelationship folkRelationship=folk2.getRelationshipWith(this.folk1);//.familyType=EnumFamilyType.SPOUSE;
                    //FolkRelationship folkRelationship=folk2.getRelationshipWith(this.folk1);
                    if(folkRelationship!=null){
                        //配偶
                        folkRelationship.familyType = EnumFamilyType.SPOUSE;
                        folkRelationship.subLevel=9;
                        folkRelationship.level=EnumLevel.BESTFRIENDS;
                    }
                    this.folk1.adjustRelationship(folk2,999);
                    this.folk1.evict();
                    this.folk1.home = folk2.home;
                    folk2.home.occupants.add(this.folk1);
                    folk2.home.saveBuilding();
                }

            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("tryMarry出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    public void addLevel(int amount) {
        this.addLevel(amount, true);
    }
    /**
     * @Author fan
     * @Description //TODO 增加感情级别
     * @Date 22:28 2022/11/2
     * @Param [amount, inverse]
     * @return void 反向
     **/
    private void addLevel(int amount, boolean inverse) {
        this.subLevel += amount;
        if (this.subLevel < 0) {
            this.subLevel += 10;
            //等级
            switch(this.level) {
                //最好的朋友
                case BESTFRIENDS:
                    this.level = EnumLevel.GOODFRIEND;
                    break;
                    //好朋友
                case GOODFRIEND:
                    this.level = EnumLevel.FRIEND;
                    break;
                    //朋友
                case FRIEND:
                    this.level = EnumLevel.AQUAINTANCE;
                    break;
                    //是熟人
                case AQUAINTANCE:
                    this.level = EnumLevel.DISLIKE;
                    break;
                    //不喜欢
                case DISLIKE:
                    this.level = EnumLevel.HATE;
                    break;
                    //讨厌
                case HATE:
                    this.level = EnumLevel.DESPISE;
                    break;
                    //看不上眼
                case DESPISE:
                    this.level = EnumLevel.ENEMY;
                    //敌人
                case ENEMY:
            }
        } else if (this.subLevel > 9) {
            this.subLevel -= 10;
            switch(this.level) {
                case BESTFRIENDS:
                    this.subLevel = 9;
                    this.tryMarry();
                    break;
                case GOODFRIEND:
                    this.level = EnumLevel.BESTFRIENDS;
                    break;
                case FRIEND:
                    this.level = EnumLevel.GOODFRIEND;
                    break;
                case AQUAINTANCE:
                    this.level = EnumLevel.FRIEND;
                    break;
                case DISLIKE:
                    this.level = EnumLevel.AQUAINTANCE;
                    break;
                case HATE:
                    this.level = EnumLevel.DISLIKE;
                    break;
                case DESPISE:
                    this.level = EnumLevel.HATE;
                    break;
                case ENEMY:
                    this.level = EnumLevel.DESPISE;
            }
        }

        if (inverse) {
            FolkRelationship folkRelationship=this.getInverse();
            if(folkRelationship!=null){
                folkRelationship.addLevel(amount, false);
            }
        }

    }

    public NpcData getOther() {
        //System.out.println(this.folk2);
        NpcData npcData=ModSimLoader.getFolkDataByUID(this.folk2);
        return npcData;
    }
    /**
     * @Author fan
     * @Description //TODO 获取状态
     * @Date 16:37 2022/11/13
     * @Param []
     * @return java.lang.String
     **/
    public String getText() {
        String txt = "";
        NpcData npcData=this.getOther();
        if(npcData!=null){
            String name=npcData.getName();
            //是否女性
            boolean female = npcData.gender == 1;
            switch(this.familyType) {
                //伴侣
                case PARTNER:
                  String girlFriend=new TextComponentTranslation("container.sim.girlFriend",new Object[0]).getUnformattedText();
                    String boyFriend=new TextComponentTranslation("container.sim.boyFriend",new Object[0]).getUnformattedText();
                    txt = female ? girlFriend : boyFriend;
                    break;
                    //配偶
                case SPOUSE:
                    String wife=new TextComponentTranslation("container.sim.Wife",new Object[0]).getUnformattedText();
                    String husband=new TextComponentTranslation("container.sim.Husband",new Object[0]).getUnformattedText();
                    txt = female ? wife : husband;
                    break;
                    //父亲
                case PARENT:
                    String mother=new TextComponentTranslation("container.sim.relation_ship_Mother",new Object[0]).getUnformattedText();
                    String father=new TextComponentTranslation("container.sim.relation_ship_Father",new Object[0]).getUnformattedText();
                    txt = female ? mother : father;
                    break;
                    //孩子
                case CHILD:
                    String son=new TextComponentTranslation("container.sim.relation_ship_Son",new Object[0]).getUnformattedText();
                    String daughter=new TextComponentTranslation("container.sim.relation_ship_Daughter",new Object[0]).getUnformattedText();
                    txt = female ? daughter : son;
                    break;
                    //兄弟 姐妹
                case SIBLING:
                    String brother=new TextComponentTranslation("container.sim.relation_ship_Brother",new Object[0]).getUnformattedText();
                    String sister=new TextComponentTranslation("container.sim.relation_ship_Sister",new Object[0]).getUnformattedText();
                    txt = female ? sister : brother;
                    break;
                    //祖父母
                case GRANDPARENT:
                    String grandfather=new TextComponentTranslation("container.sim.relation_ship_Grandfather",new Object[0]).getUnformattedText();
                    String grandmother=new TextComponentTranslation("container.sim.relation_ship_Grandmother",new Object[0]).getUnformattedText();
                    txt = female ? grandmother : grandfather;
                    break;
                    //孙子孙女
                case GRANDCHILD:
                    String granddaughter=new TextComponentTranslation("container.sim.relation_ship_Granddaughter",new Object[0]).getUnformattedText();
                    String grandson=new TextComponentTranslation("container.sim.relation_ship_Grandson",new Object[0]).getUnformattedText();
                    txt = female ? granddaughter : grandson;
                    break;
                    //亲子关系
                case PARENTSIBLING:
                    String aunt=new TextComponentTranslation("container.sim.relation_ship_Aunt",new Object[0]).getUnformattedText();
                    String uncle=new TextComponentTranslation("container.sim.relation_ship_Uncle",new Object[0]).getUnformattedText();
                    txt = female ? aunt : uncle;
                    break;
                    //兄弟姐妹子女
                case SIBLINGCHILD:
                    String niece=new TextComponentTranslation("container.sim.relation_ship_Neice",new Object[0]).getUnformattedText();
                    String nephew=new TextComponentTranslation("container.sim.relation_ship_Nephew",new Object[0]).getUnformattedText();
                    txt = female ? niece : nephew;
                    break;
                    //表亲
                case COUSIN:
                    String cousin=new TextComponentTranslation("container.sim.cousin",new Object[0]).getUnformattedText();
                    txt = cousin;
                    break;
                    //堂亲
                case EXTENDED:
                    String extendedFamily=new TextComponentTranslation("container.sim.extendedFamily",new Object[0]).getUnformattedText();
                    txt = extendedFamily;
                    break;
                    //不相干
                case UNRELATED:
                    txt = "";
            }

            if (txt == "") {
                txt = this.level.getText();
            }
            txt=name+":"+txt;
        }


        return txt;
    }

    @Override
    public String toString() {
        //ID ， 家庭情况， 等级 ， 级别
        return this.folk2 + "," + this.familyType.toString() + "," + this.level.toString() + "," + this.subLevel;
    }
}
