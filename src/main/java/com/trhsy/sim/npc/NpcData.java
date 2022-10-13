package com.trhsy.sim.npc;

import com.trhsy.sim.entity.EntityFolk;
import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import com.trhsy.sim.network.client.PacketSendFolkSkin;
import com.trhsy.sim.npc.geneics.NpcRace;
import com.trhsy.sim.npc.geneics.Race;
import com.trhsy.sim.npc.traits.Trait;
import com.trhsy.sim.util.EnumFamilyType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: npcData
 * @Description:
 * @date 2022/10/11 14:44
 */
public class NpcData {
    /**id**/
    public String ID;
    /**名**/
    public String forename;
    /**姓**/
    public String surname;
    /**年龄 0至17岁的人=儿童18+成人**/
    public int age;
    /**性别 0=男性1=女性**/
    public int gender;
    /**状态 **/
    public String status = "Wandering";
    /**饱食度**/
    public int hunger = 10;
    /**种族**/
    public NpcRace race;
    /**工作**/
    public Job job;
    /**npc实体**/
    public EntityFolk entity;
    /**情感关系**/
    public List<FolkRelationship> relationships = new CopyOnWriteArrayList();
    /**情绪**/
    /**物品栏**/
    public List<ItemStack> inventory = new CopyOnWriteArrayList<>();
    /**任务**/
    /**特征1**/
    public Trait trait1;
    /**特征2**/
    public Trait trait2;
    /**特征3**/
    public Trait trait3;
    /**正在移动**/
    public boolean isMoving;
    /**建筑等级**/
    public float skillBuilding = 1.0F;
    /**耕种等级**/
    public float skillFarming = 1.0F;
    /**采矿等级**/
    public float skillMining = 1.0F;
    /**家**/
    public Building home;
    /**位置**/
    public V3 pos;
    /**手持物品**/
    public ItemStack holding;
    //皮肤数
    public int skinnumber = 1;
    /**交配阶段**/
    public float matingStage;
    /**怀孕阶段**/
    public float pregnancyStage;
    /**随机**/
    Random rand;
    /**留在原地**/
    public boolean stayPut;
    /**正在睡觉**/
    public boolean isSleeping;
    /**已死亡*/
    public boolean isDead;
    /**已加载**/
    public boolean isLoaded;
    /**临时雇员**/
    private int tempStage;
    /**自上次状态更新以来的时间**/
    private transient long timeSinceLastStatusUpdate;
    /**分钟更新**/
    private transient long minuteUpdate;
    /**临时员工位置**/
    V3 tempEmployLoc;
    /**上次路径尝试**/
    Long lastPathAttempt;

    public NpcData(World world, boolean fromCommand) {
        //手持空
        this.holding = null;
        //交配阶段 没有需求
        this.matingStage = -1.0F;
        this.rand = new Random();
        //雇佣信息无
        this.tempStage = -1;
        //更新时间0
        this.timeSinceLastStatusUpdate = 0L;
        //更新时间0
        this.minuteUpdate = 0L;
        //雇佣位置无
        this.tempEmployLoc = null;
        //路径尝试无
        this.lastPathAttempt = 0L;
        //性别随机
        this.gender = this.rand.nextInt(2);
        //种族分配
        this.assignRace();
        /**年龄**/
        this.age = this.race.maturity;
        ModSimLoader.folks.add(this);
        EntityFolk e = new EntityFolk(world, true);
        e.isBeingCreated = true;
        EntityPlayer thePlayer = (EntityPlayer)world.playerEntities.get(0);
        e.setPositionAndUpdate(thePlayer.posX, thePlayer.posY, thePlayer.posZ);
        this.pos = V3.fromBlockPos(thePlayer.getPosition());
        if (!fromCommand) {
            Vec3d newPos;
            for(newPos = RandomPositionGenerator.findRandomTarget(e, 30, 7); newPos == null; newPos = RandomPositionGenerator.findRandomTarget(e, 30, 7)) {
            }

            while(!world.isAirBlock((new BlockPos(newPos)).up())) {
                newPos = RandomPositionGenerator.findRandomTarget(e, 30, 7);
            }

            e.setPositionAndUpdate(newPos.xCoord, newPos.yCoord + 1.0D, newPos.zCoord);
            this.pos = V3.fromVec3d(newPos);
        }

        e.theData = this;
        this.ID = e.getUniqueID().toString();
        this.entity = e;
        world.spawnEntityInWorld(e);
        String fs_ldzl=I18n.format("container.sim.folk_data_just");
        ModSimLoader.sendChat(this.getName() + fs_ldzl);
        //返回可雇佣的人
        NetWorkLoader.net.sendToAll(new PacketReturnHireableFolks());
        //向客户端发送皮肤地址
        this.sendSkinPathToClient();
        //保存NPC
        this.saveFolk();
        this.isLoaded = true;
    }
    public void fire() {
        this.setStatus("Wandering");
        this.job = null;
        if (this.entity != null) {
            this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
        }

        this.stayPut = false;
    }
    public void assignRace() {
        try {
            File raceFolder = new File(ModSimLoader.getSimFolder() + File.separator + "races");
            File[] races = raceFolder.listFiles();
            File pickedRace = races[this.rand.nextInt(races.length)];
            File raceData = new File(pickedRace.getAbsolutePath() + File.separator + pickedRace.getName() + ".RACE");
            BufferedReader reader = new BufferedReader(new FileReader(raceData));
            String line = reader.readLine();

            for(this.race = new NpcRace(); line != null; line = reader.readLine()) {
                if (line.contains("name=")) {
                    this.race.name = line.split("=")[1];
                } else if (line.contains("description")) {
                    this.race.desc = line.split("=")[1];
                } else if (line.contains("lifespan")) {
                    this.race.lifespan = Integer.valueOf(line.split("=")[1]);
                } else if (line.contains("maturity")) {
                    this.race.maturity = Integer.valueOf(line.split("=")[1]);
                } else if (line.contains("malenameset") && this.gender == 0) {
                    this.forename = line.split("=")[1].split(";")[this.rand.nextInt(line.split("=")[1].split(";").length)];
                } else if (line.contains("femalenameset") && this.gender == 1) {
                    this.forename = line.split("=")[1].split(";")[this.rand.nextInt(line.split("=")[1].split(";").length)];
                } else if (line.contains("surnameset")) {
                    this.surname = line.split("=")[1].split(";")[this.rand.nextInt(line.split("=")[1].split(";").length)];
                }
            }

            reader.close();
            File[] skins = null;
            if (this.gender == 0) {
                skins = (new File(pickedRace.getAbsolutePath() + File.separator + "male")).listFiles();
            } else {
                skins = (new File(pickedRace.getAbsolutePath() + File.separator + "female")).listFiles();
            }

            this.race.skinName = skins[this.rand.nextInt(skins.length)].getName();
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    /**
     * 向客户端发送皮肤地址
     */
    public void sendSkinPathToClient() {
        if (this.entity != null) {
            NetWorkLoader.net.sendToAll(new PacketSendFolkSkin(this.entity.getUniqueID().toString(), this.entity.getTexture() ));
        }
    }

    /**
     * 保存NPC
     */
    public void saveFolk() {
        if (this.entity != null && !this.isDead) {
            BufferedWriter writer = null;
            try{
                File npcFolder = new File(ModSimLoader.getSimFolder() + File.separator + "npc");
                npcFolder.mkdirs();
                File logFile = new File(npcFolder + File.separator + this.entity.getUniqueID() + ".sk2");
                writer = new BufferedWriter(new FileWriter(logFile));
                writer.write("id|" + this.ID + "\n");
                writer.write("fname|" + this.forename + "\n");
                writer.write("sname|" + this.surname + "\n");
                writer.write("gender|" + this.gender + "\n");
                writer.write("age|" + String.valueOf(this.age) + "\n");
                writer.write("race|" + this.race.name + "\n");
                writer.write("skin|" + this.race.skinName + "\n");
                writer.write("pos|" + this.pos.toString() + "\n");
                writer.write("hunger|" + String.valueOf(this.hunger) + "\n");
                writer.write("buildingskill|" + String.valueOf(this.skillBuilding) + "\n");
                writer.write("farmingskill|" + String.valueOf(this.skillFarming) + "\n");
                writer.write("holding|" + this.entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getDisplayName() + "\n");
                boolean isBuilding = false;
                /*if (this.job != null) {
                    writer.write("employedat|" + this.job.workPlace.toString() + "\n");
                    if (this.job.jobName.contentEquals("builder")) {
                        JobBuilder jb = (JobBuilder)this.job;
                        writer.write("job|" + this.job.jobName + ";" + jb.workPlace.toString() + ";" + jb.direction + "\n");
                        if (jb.blueprint != null) {
                            writer.write("building|" + jb.blueprint.name + "\n");
                        }
                    } else {
                        writer.write("job|" + this.job.jobName + "\n");
                        writer.write("jobstage|" + this.job.stage + "\n");
                    }
                } else {*/
                    writer.write("employedat|null\n");
                    writer.write("job|null\n");
                    writer.write("jobstage|-1\n");
                //}
                writer.write("relationship|");
                /*for(int i = 0; i < this.relationships.size(); ++i) {
                    writer.write(((FolkRelationship)this.relationships.get(i)).toString() + (i < this.relationships.size() - 1 ? ";" : ""));
                }*/
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    writer.close();
                } catch (Exception var15) {
                }

            }

        }
    }

    public String getName() {
        String name="";
        if(this.surname != null && this.forename != null){
            String lang = FMLCommonHandler.instance().getCurrentLanguage();
            if ("en_US".equals(lang)) {
                name = forename + " " + surname;
            } else {
                name = surname + forename;
            }
        }
        return  name;
    }

    public NpcIdentity getClientIdentity() {
        //String skin = "races/" + this.race.name.toLowerCase() + (this.gender == 0 ? "/male/" : "/female/") + this.race.skinName;
        String skin =(this.gender == 0 ? "/male/" : "/female/")+skinnumber;
        NpcIdentity npcIdentity=new NpcIdentity(this.ID,this.getName(),String.valueOf(this.age),this.getStatusText(), this.getJobTitle(), this.getHousingStatus(), this.getRelationshipStatus(), this.getHunger(), String.valueOf(this.race.maturity), skin);
        return npcIdentity;
    }
    public String getStatusText() {
        return this.status;
    }
    public String getJobTitle() {
        //return this.job != null ? this.job.toString() : "Unemployed";
        return "Unemployed";
    }

    public String getHousingStatus() {
        //return this.home != null ? "Homeowner" : "Homeless";
        return "Homeowner";
    }
    public void respawn(World world, BlockPos bp) {
        if (this.entity == null && this.isLoaded) {
            EntityFolk ef = new EntityFolk(world, this.ID);
            ef.setHeldItem(EnumHand.MAIN_HAND, this.holding);
            this.pos = new V3(bp);
            ef.setPositionAndUpdate((double)bp.getX() + 0.5D, (double)bp.getY() + 1.0D, (double)bp.getZ() + 0.5D);
            this.entity = ef;
            ef.theData = this;
            world.spawnEntityInWorld(ef);
        }
    }
    public FolkRelationship getRelationshipWith(NpcData folk2) {
        Iterator var2 = this.relationships.iterator();

        FolkRelationship rel;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            rel = (FolkRelationship)var2.next();
        } while(rel.getOther() != folk2);

        return rel;
    }
    public String getRelationshipStatus() {
        if (this.getFamily(EnumFamilyType.SPOUSE) != null) {
            //已婚
            String married=I18n.format("container.sim.relation_ship_Married");
            return married;
        } else {
            //单身狗
            String single=I18n.format("container.sim.gui_Folk_Single");
            //有对象
            String In_a_relationship=I18n.format("container.sim.In_a_relationship");

            return this.getFamily(EnumFamilyType.PARTNER) != null ? In_a_relationship : single;
        }
    }

    public String getHunger() {
        if (this.hunger > 8) {
            return "Well fed";
        } else if (this.hunger > 4) {
            return "A little hungry";
        } else {
            return this.hunger > 1 ? "Very hungry" : "Starving";
        }
    }
    public NpcData getFamily(EnumFamilyType fam) {
        FolkRelationship rels=null;
        for(FolkRelationship rel:this.relationships){
            if(rel.familyType == fam){
                rels=rel;
            }
        }
        /*Iterator var2 = this.relationships.iterator();

        FolkRelationship rel;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            rel = (FolkRelationship)var2.next();
        } while(rel.familyType != fam);*/

        return rels.getOther();
    }

    /**
     * 移除
     */
    public void evict() {
        this.home.occupants.remove(this);
        this.home.saveBuilding();
        this.home = null;
    }
    public void setStatus(String sts) {
        this.status = sts;
    }
    public void onUpdate() {

    }
}
