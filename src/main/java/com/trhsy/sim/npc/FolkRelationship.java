package com.trhsy.sim.npc;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.util.EnumFamilyType;
import com.trhsy.sim.util.EnumLevel;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
    public EnumLevel level;
    public int subLevel;

    public FolkRelationship(NpcData folk1, NpcData folk2, EnumFamilyType family) {
        //不相关的
        this.familyType = EnumFamilyType.UNRELATED;
        //熟人
        this.level = EnumLevel.AQUAINTANCE;
        this.subLevel = 5;
        this.folk1 = folk1;
        this.folk2 = folk2.ID;
        this.familyType = family;
    }

    public FolkRelationship(NpcData folk1, String save) {
        this.familyType = EnumFamilyType.UNRELATED;
        this.level = EnumLevel.AQUAINTANCE;
        this.subLevel = 5;
        this.folk1 = folk1;
        this.folk2 = save.split(",")[0];
        this.familyType = EnumFamilyType.valueOf(save.split(",")[1]);
        this.level = EnumLevel.valueOf(save.split(",")[2]);
        this.subLevel = Integer.parseInt(save.split(",")[3]);
    }

    /**
     * 获取反向
     * @return
     */
    public FolkRelationship getInverse() {
        NpcData npcData= this.getOther();
        FolkRelationship folkRelationship=null;
        if(this.folk1!=null&&npcData!=null){
            folkRelationship=npcData.getRelationshipWith(this.folk1);
        }
        return folkRelationship;
    }

    /**
     * 尝试结婚
     */
    public void tryMarry() {
        NpcData folk2 = this.getOther();
        //单身狗
        String singe= I18n.format("container.sim.folkData4");
        if (this.folk1!=null&&folk2!=null&&this.folk1.getRelationshipStatus().contentEquals(singe) && folk2.getRelationshipStatus().contentEquals(singe)) {
            if (this.folk1.gender != folk2.gender && this.folk1.age >= this.folk1.race.maturity && folk2.age >= folk2.race.maturity && this.folk1.home != null && folk2.home != null && this.familyType == EnumFamilyType.UNRELATED) {
               String and=I18n.format("container.sim.Mining13");
                String married=I18n.format("container.sim.married");
                String moving=I18n.format("container.sim.moving");
                String moving1=I18n.format("container.sim.moving1");

                //仲孙锐翰和栾平怡正在结婚！仲孙锐翰 正在搬进 栾平怡的家.
                String marriageMessage = this.folk1.getName() +and + folk2.getName() + married + this.folk1.getName() +moving + folk2.getName() + moving1+".";
                ModSimLoader.sendChat(marriageMessage);
                if (this.folk1.gender == 1) {
                    this.folk1.surname = folk2.surname;
                } else {
                    folk2.surname = this.folk1.surname;
                }

                this.familyType = EnumFamilyType.SPOUSE;
                this.getInverse().familyType = EnumFamilyType.SPOUSE;
                this.folk1.evict();
                this.folk1.home = folk2.home;
                folk2.home.occupants.add(this.folk1);
                folk2.home.saveBuilding();
            }

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
            switch(this.level) {
                case BESTFRIENDS:
                    this.level = EnumLevel.GOODFRIEND;
                    break;
                case GOODFRIEND:
                    this.level = EnumLevel.FRIEND;
                    break;
                case FRIEND:
                    this.level = EnumLevel.AQUAINTANCE;
                    break;
                case AQUAINTANCE:
                    this.level = EnumLevel.DISLIKE;
                    break;
                case DISLIKE:
                    this.level = EnumLevel.HATE;
                    break;
                case HATE:
                    this.level = EnumLevel.DESPISE;
                    break;
                case DESPISE:
                    this.level = EnumLevel.ENEMY;
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
                case PARTNER:
                    String girlFriend=I18n.format("container.sim.girlFriend");
                    String boyFriend=I18n.format("container.sim.boyFriend");
                    txt = female ? girlFriend : boyFriend;
                    break;
                case SPOUSE:
                    String wife=I18n.format("container.sim.Wife");
                    String husband=I18n.format("container.sim.Husband");
                    txt = female ? wife : husband;
                    break;
                case PARENT:
                    String mother=I18n.format("container.sim.relation_ship_Mother");
                    String father=I18n.format("container.sim.relation_ship_Father");
                    txt = female ? mother : father;
                    break;
                case CHILD:
                    String son=I18n.format("container.sim.relation_ship_Son");
                    String daughter=I18n.format("container.sim.relation_ship_Daughter");
                    txt = female ? daughter : son;
                    break;
                case SIBLING:
                    String brother=I18n.format("container.sim.relation_ship_Brother");
                    String sister=I18n.format("container.sim.relation_ship_Sister");
                    txt = female ? sister : brother;
                    break;
                case GRANDPARENT:
                    String grandfather=I18n.format("container.sim.relation_ship_Grandfather");
                    String grandmother=I18n.format("container.sim.relation_ship_Grandmother");
                    txt = female ? grandmother : grandfather;
                    break;
                case GRANDCHILD:
                    String granddaughter=I18n.format("container.sim.relation_ship_Granddaughter");
                    String grandson=I18n.format("container.sim.relation_ship_Grandson");
                    txt = female ? granddaughter : grandson;
                    break;
                case PARENTSIBLING:
                    String aunt=I18n.format("container.sim.relation_ship_Aunt");
                    String uncle=I18n.format("container.sim.relation_ship_Uncle");
                    txt = female ? aunt : uncle;
                    break;
                case SIBLINGCHILD:
                    String niece=I18n.format("container.sim.relation_ship_Neice");
                    String nephew=I18n.format("container.sim.relation_ship_Nephew");
                    txt = female ? niece : nephew;
                    break;
                case COUSIN:
                    String cousin=I18n.format("container.sim.cousin");
                    txt = cousin;
                    break;
                case EXTENDED:
                    String extendedFamily=I18n.format("container.sim.extendedFamily");
                    txt = extendedFamily;
                    break;
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
        return this.folk2 + "," + this.familyType.toString() + "," + this.level.toString() + "," + this.subLevel;
    }
}
