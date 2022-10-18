package com.trhsy.sim.npc;

import com.trhsy.sim.entity.EntityFolk;
import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ConfigLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import com.trhsy.sim.network.client.PacketSendFolkSkin;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.npc.moodbuff.MoodBuff;
import com.trhsy.sim.npc.race.Race;
import com.trhsy.sim.npc.race.Races;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.npc.traits.Trait;
import com.trhsy.sim.npc.traits.Traits;
import com.trhsy.sim.util.EnumFamilyType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
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
    public String status = I18n.format("container.sim.folk_data.Wandering");
    /**饱食度**/
    public int hunger = 10;
    /**种族**/
    public Race race;
    /**工作**/
    public Job job;
    /**npc实体**/
    public EntityFolk entity;
    /**情感关系**/
    public List<FolkRelationship> relationships = new CopyOnWriteArrayList();
    /**情绪**/
    public List<MoodBuff> buffs = new ArrayList();
    /**物品栏**/
    public List<ItemStack> inventory = new CopyOnWriteArrayList<>();
    /**任务**/
    public List<Task> tasks = new ArrayList();
    /**当前任务**/
    public Task currentTask;
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
        /**皮肤随机*/
        this.skinnumber =rand.nextInt(64);
        //种族分配
        this.assignRace();
        /**年龄**/
        this.age = this.race.maturity;
        //特征
        generateTraits();
        ModSimLoader.folks.add(this);
        EntityFolk e = new EntityFolk(world, true);
        e.isBeingCreated = true;
        EntityPlayer thePlayer = world.playerEntities.get(0);
        e.setPositionAndUpdate(thePlayer.posX, thePlayer.posY, thePlayer.posZ);
        this.pos = V3.fromBlockPos(thePlayer.getPosition());
        if (!fromCommand) {
            Vec3d newPos;
            newPos = RandomPositionGenerator.findRandomTarget(e, 30, 7);
            if(newPos == null){
                newPos = RandomPositionGenerator.findRandomTarget(e, 30, 7);
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
    public NpcData(World world, UUID uuid) {
        this.holding = null;
        this.matingStage = -1.0F;
        this.rand = new Random();
        /**皮肤随机*/
        this.skinnumber =rand.nextInt(64);
        this.tempStage = -1;
        this.timeSinceLastStatusUpdate = 0L;
        this.minuteUpdate = 0L;
        this.tempEmployLoc = null;
        this.lastPathAttempt = 0L;

        if (!world.isRemote) {
            this.loadFolk(world, uuid);
        }

    }

    public NpcData(World world, NpcData mother, NpcData father) {
        this.holding = null;
        this.matingStage = -1.0F;
        this.rand = new Random();
        this.tempStage = -1;
        this.timeSinceLastStatusUpdate = 0L;
        /**皮肤随机*/
        this.skinnumber =rand.nextInt(64);
        this.minuteUpdate = 0L;
        this.tempEmployLoc = null;
        this.lastPathAttempt = 0L;
        this.gender = this.rand.nextInt(2);
        if (this.rand.nextInt(2) == 0) {
            this.assignRace(mother.race.raceName, true);
        } else {
            this.assignRace(father.race.raceName, true);
        }

        this.surname = father.surname;
        generateTraits();
        EntityFolk e = new EntityFolk(world, true);
        e.isBeingCreated = true;
        e.setPositionAndUpdate(mother.entity.posX, mother.entity.posY, mother.entity.posZ);
        this.pos = mother.pos;
        e.theData = this;
        this.ID = e.getUniqueID().toString();
        this.entity = e;
        this.assignFamilyMembers(mother, father);
        this.home = mother.home;
        mother.home.occupants.add(this);
        world.spawnEntityInWorld(e);
        NetWorkLoader.net.sendToAll(new PacketReturnHireableFolks());
        this.sendSkinPathToClient();
        this.saveFolk();
        this.isLoaded = true;
    }
    public void assignFamilyMembers(NpcData mother, NpcData father) {
        this.assignRelationshipsFromParent(mother);
        this.assignRelationshipsFromParent(father);
    }
    public void assignRelationshipsFromParent(NpcData parent) {
        this.relationships.add(new FolkRelationship(this, parent, EnumFamilyType.PARENT));
        parent.relationships.add(new FolkRelationship(parent, this, EnumFamilyType.CHILD));
        for(FolkRelationship rel:parent.relationships){
            NpcData folk2 = rel.getOther();
            if(rel.familyType != EnumFamilyType.EXTENDED && rel.familyType != EnumFamilyType.GRANDCHILD && rel.familyType != EnumFamilyType.GRANDPARENT && rel.familyType != EnumFamilyType.PARENTSIBLING){
                if(folk2==null){
                    folk2 = rel.getOther();
                }
                if (rel.familyType == EnumFamilyType.CHILD && this.getRelationshipWith(folk2) == null) {
                    this.relationships.add(new FolkRelationship(this, folk2, EnumFamilyType.SIBLING));
                    folk2.relationships.add(new FolkRelationship(folk2, this, EnumFamilyType.SIBLING));
                }

                if (rel.familyType == EnumFamilyType.SIBLING && this.getRelationshipWith(folk2) == null) {
                    this.relationships.add(new FolkRelationship(this, folk2, EnumFamilyType.PARENTSIBLING));
                    folk2.relationships.add(new FolkRelationship(folk2, this, EnumFamilyType.SIBLINGCHILD));
                }

                if (rel.familyType == EnumFamilyType.SIBLINGCHILD && this.getRelationshipWith(folk2) == null) {
                    this.relationships.add(new FolkRelationship(this, folk2, EnumFamilyType.COUSIN));
                    folk2.relationships.add(new FolkRelationship(folk2, this, EnumFamilyType.COUSIN));
                }

                if (rel.familyType == EnumFamilyType.PARENT && this.getRelationshipWith(folk2) == null) {
                    this.relationships.add(new FolkRelationship(this, folk2, EnumFamilyType.GRANDPARENT));
                    folk2.relationships.add(new FolkRelationship(folk2, this, EnumFamilyType.GRANDCHILD));
                }
            }
            if (this.getRelationshipWith(folk2) == null) {
                this.relationships.add(new FolkRelationship(this, folk2, EnumFamilyType.EXTENDED));
                folk2.relationships.add(new FolkRelationship(folk2, this, EnumFamilyType.EXTENDED));
            }
        }
    }
    /**
     * @Author fan
     * @Description //TODO 加载NPC
     * @Date 19:59 2022/10/15
     * @Param [world, loadID]
     * @return void
     **/
    public void loadFolk(World world, UUID loadID) {
        DimensionManager d = new DimensionManager();
        File npcFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "npc");
        if(npcFolder.exists()){
            npcFolder.mkdirs();
        }

        this.entity = (EntityFolk)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(loadID);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(npcFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                int m1 = line.indexOf("|");
                String name = line.substring(0, m1).toLowerCase();
                String value = line.substring(m1 + 1).toLowerCase();
                ModSimLoader.log.info(line);
                if (line.contains("id|")) {
                    this.ID = value;
                    if (this.entity == null) {
                        this.entity = (EntityFolk)world.getMinecraftServer().getEntityFromUuid(UUID.fromString(this.ID));
                    }
                }

                if (line.contains("fname|")) {
                    this.forename = value.substring(0, 1).toUpperCase() + value.substring(1);
                } else if (line.contains("sname|")) {
                    this.surname = value.substring(0, 1).toUpperCase() + value.substring(1);
                } else if (line.contains("gender|")) {
                    this.gender = Integer.valueOf(value);
                } else if (line.contains("age|") && !line.contains("jobstage")) {
                    this.age = Integer.valueOf(value);
                } else if (line.contains("race|")) {
                    this.assignRace(value, false);
                } else if (line.contains("skin|")) {
                    this.race.skinName = value;
                } else if (line.contains("pos|")) {
                    this.pos = V3.fromString(value);
                }else if (line.contains("trait1|")) {
                    this.trait1=Trait.getTraitFromName(value);
                }else if (line.contains("trait2|")) {
                    this.trait2=Trait.getTraitFromName(value);
                }else if (line.contains("trait3|")) {
                    this.trait3=Trait.getTraitFromName(value);
                }else if (line.contains("hunger|")) {
                    this.hunger = Integer.valueOf(value);
                } else if (line.contains("buildingskill|")) {
                    this.skillBuilding = Float.valueOf(value);
                } else if (line.contains("farmingskill|")) {
                    this.skillFarming = Float.valueOf(value);
                } else if (line.contains("holding|")) {
                    try {
                        this.holding = new ItemStack(Item.getByNameOrId(value));
                        this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.holding);
                    } catch (Exception var16) {
                    }
                } else if (line.contains("employedat|")) {
                    if (!value.contentEquals("null")) {
                        this.tempEmployLoc = V3.fromString(value);
                    }
                } else if (line.contains("job|")) {
                    if (!value.contentEquals("null")) {
                        BlockPos p;
                        /*if (value.split(";")[0].contentEquals("builder")) {
                            p = V3.fromString(value.split(";")[1]).toBlockPos();
                            int d2 = Integer.valueOf(value.split(";")[2]);
                            this.job = new JobBuilder(this, p, d2, world);
                        } else if (value.split(";")[0].contentEquals("dairyfarmer")) {
                            this.job = new JobDairyFarmer(this, this.tempEmployLoc, world);
                        } else if (value.split(";")[0].contentEquals("baker")) {
                            this.job = new JobBaker(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("butcher")) {
                            this.job = new JobButcher(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("pig farmer")) {
                            p = this.tempEmployLoc.toBlockPos();
                            this.job = new JobLivestockFarmer(this, p, "pig", world);
                        } else if (value.split(";")[0].contentEquals("cow farmer")) {
                            p = this.tempEmployLoc.toBlockPos();
                            this.job = new JobLivestockFarmer(this, p, "cow", world);
                        } else if (value.split(";")[0].contentEquals("chicken farmer")) {
                            p = this.tempEmployLoc.toBlockPos();
                            this.job = new JobLivestockFarmer(this, p, "chicken", world);
                        } else if (value.split(";")[0].contentEquals("farmer")) {
                            p = this.tempEmployLoc.toBlockPos();
                            FarmBox fb = WorldData.getFarm(V3.fromBlockPos(p));
                            this.job = new JobFarmer(this, p, world, fb);
                        } else if (value.split(";")[0].contentEquals("miner")) {
                            p = this.tempEmployLoc.toBlockPos();
                            MineBox mb = WorldData.getMine(V3.fromBlockPos(p));
                            this.job = new JobMiner(this, p, world, mb);
                        } else if (value.split(";")[0].contentEquals("egg farmer")) {
                            this.job = new JobEggFarmer(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("fisherman")) {
                            this.job = new JobFisherman(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("grocer")) {
                            this.job = new JobGrocer(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("lumberjack")) {
                            this.job = new JobLumberjack(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("shepherd")) {
                            this.job = new JobShepherd(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("soldier")) {
                            this.job = new JobSoldier(this, this.tempEmployLoc.toBlockPos(), world);
                        } else if (value.split(";")[0].contentEquals("")) {
                        }*/
                    }

                    if (this.job != null) {
                        this.job.stage = this.tempStage;
                    }
                } else if (line.contains("jobstage|")) {
                    if (this.job != null) {
                        this.job.stage = Integer.parseInt(value);
                    } else {
                        this.tempStage = Integer.parseInt(value);
                    }
                } else if (line.contains("relationship|")) {
                    String[] rels = value.split(";");
                    String[] var12 = rels;
                    int var13 = rels.length;

                    for(int var14 = 0; var14 < var13; ++var14) {
                        String rel = var12[var14];
                        if (rel.length() > 0) {
                            this.relationships.add(new FolkRelationship(this, rel.toUpperCase()));
                        }
                    }
                }
            }

            reader.close();
            if (this.entity != null) {
                this.entity.setDead();
            }

            this.respawn(world, this.pos.toBlockPos());
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        this.isLoaded = true;
    }
    public void fire() {
        this.setStatus(I18n.format("container.sim.folk_data.Wandering"));
        this.job = null;
        if (this.entity != null) {
            this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
        }

        this.stayPut = false;
    }
    /**
     * @Author fan
     * @Description //TODO 分配种族
     * @Date 15:57 2022/10/14
     * @Param []
     * @return void
     **/
    public void assignRace() {
        try {
            this.race = Races.raceList.get(rand.nextInt(Races.raceList.size()));
            this.race.skinName=this.getTexture();
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }
    public void assignRace(String existingRaceName, boolean newChild) {
        try {
            for (int i = 0; i < Races.raceList.size(); i++) {
                Race race=Races.raceList.get(i);
                if(existingRaceName.equals(race.raceName)){
                    this.race =race;
                    break;
                }
            }

        } catch (Exception var9) {
            var9.printStackTrace();
        }
    }

    /**
     * 向客户端发送皮肤地址
     */
    public void sendSkinPathToClient() {
        if (this.entity != null) {
            NetWorkLoader.net.sendToAll(new PacketSendFolkSkin(this.entity.getUniqueID().toString(), this.race.skinName ));
        }
    }

    /**
     * 保存NPC
     */
    public void saveFolk() {
        ModSimLoader.log.info("开始保存NPC数据：");
        if (this.entity != null && !this.isDead) {
            BufferedWriter writer = null;
            try{
                File npcFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "npc");
                npcFolder.mkdirs();
                File logFile = new File(npcFolder + File.separator + this.entity.getUniqueID() + ".sk2");
                writer = new BufferedWriter(new FileWriter(logFile));
                writer.write("id|" + this.ID + "\n");
                writer.write("fname|" + this.forename + "\n");
                writer.write("sname|" + this.surname + "\n");
                writer.write("gender|" + this.gender + "\n");
                writer.write("age|" + String.valueOf(this.age) + "\n");
                writer.write("race|" + this.race.raceName + "\n");
                writer.write("skin|" + this.race.skinName + "\n");
                writer.write("pos|" + this.pos.toString() + "\n");
                writer.write("trait1|" + this.trait1.traitName + "\n");
                writer.write("trait2|" + this.trait2.traitName + "\n");
                writer.write("trait3|" + this.trait3.traitName + "\n");
                writer.write("hunger|" + String.valueOf(this.hunger) + "\n");
                writer.write("buildingskill|" + String.valueOf(this.skillBuilding) + "\n");
                writer.write("farmingskill|" + String.valueOf(this.skillFarming) + "\n");
                ItemStack itemStack=this.entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                String holdings="";
                if(itemStack!=null){
                    holdings=itemStack.getDisplayName();
                }
                writer.write("holding|" + holdings + "\n");
                boolean isBuilding = false;
                if (this.job != null) {
                    writer.write("employedat|" + this.job.workPlace.toString() + "\n");
                    if (this.job.jobName.contentEquals("builder")) {
                        /*JobBuilder jb = this.job;
                        writer.write("job|" + this.job.jobName + ";" + jb.workPlace.toString() + ";" + jb.direction + "\n");
                        if (jb.blueprint != null) {
                            writer.write("building|" + jb.blueprint.name + "\n");
                        }*/
                    } else {
                        writer.write("job|" + this.job.jobName + "\n");
                        writer.write("jobstage|" + this.job.stage + "\n");
                    }
                } else {
                    writer.write("employedat|null\n");
                    writer.write("job|null\n");
                    writer.write("jobstage|-1\n");
                }
                writer.write("relationship|");
                for(int i = 0; i < this.relationships.size(); ++i) {
                    writer.write(((FolkRelationship)this.relationships.get(i)).toString() + (i < this.relationships.size() - 1 ? ";" : ""));
                }
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

        if (this.gender == 0) {
            if(this.forename==null||this.forename==""){
                int i = rand.nextInt(ConfigLoader.configMaleNames.length);
                this.forename = ConfigLoader.configMaleNames[i].trim();
            }

        }else {
            if(this.forename==null||this.forename==""){
                int i = rand.nextInt(ConfigLoader.configFemaleNames.length);
                this.forename = ConfigLoader.configFemaleNames[i].trim();
            }
        }
        if(this.surname==null||this.surname==""){
            int i = rand.nextInt(ConfigLoader.configSurnames.length);
            this.surname = ConfigLoader.configSurnames[i].trim();
        }
        if(this.surname != null && this.forename != null){
            String lang = FMLCommonHandler.instance().getCurrentLanguage();
            if ("en_US".equals(lang)) {
                name = this.forename + " " + this.surname;
            } else {
                name = this.surname + this.forename;
            }
        }
        return  name;
    }

    public NpcIdentity getClientIdentity() {
        NpcIdentity npcIdentity=null;
        if(this.entity!=null){
            String skin =this.race.skinName;
            npcIdentity=new NpcIdentity(this.ID,this.getName(),String.valueOf(this.age),this.getStatusText(), this.getJobTitle(), this.getHousingStatus(), this.getRelationshipStatus(), this.getHunger(), String.valueOf(this.race.maturity), skin);
        }
        return npcIdentity;
    }
    public String getStatusText() {
        return this.status;
    }
    public String getJobTitle() {
        /**被解雇的**/
        String s=I18n.format("container.sim.gui_Folk_unemployed");
        return this.job != null ? this.job.toString() : s;
//        return "Unemployed";
    }

    public String getHousingStatus() {
        String s=I18n.format("container.sim.folkData3");
        String s1=I18n.format("container.sim.folkData2");
        return this.home != null ? s1 : s;
//        return "Homeowner";
    }
    /**
     * @Author fan
     * @Description //TODO 重生
     * @Date 13:29 2022/10/17
     * @Param [world, bp]
     * @return void
     **/
    public void respawn(World world, BlockPos bp) {
        if (this.entity == null && !this.isLoaded) {
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
        FolkRelationship rels=null;
        for (FolkRelationship rel:this.relationships){
            if(rel.getOther() != folk2){
                rels=rel;
                return rels;
            }
        }
        return rels;
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
            return I18n.format("container.sim.folkData6");
        } else if (this.hunger > 4) {
            return I18n.format("container.sim.folkData7");
        } else {
            return this.hunger > 1 ? I18n.format("container.sim.folkData9") : I18n.format("container.sim.folkData8");
        }
    }
    public NpcData getFamily(EnumFamilyType fam) {
        FolkRelationship rels=null;
        for(FolkRelationship rel:this.relationships){
            if(rel.familyType == fam){
                rels=rel;
            }
        }
        if(rels==null){
            return null;
        }else{
            return rels.getOther();
        }
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
    /**更新NPC状态**/
    public void onUpdate() {
        Long now = System.currentTimeMillis();
        //每秒更新
        if (now - this.timeSinceLastStatusUpdate > 1000L) {
            this.onSecond();
            this.timeSinceLastStatusUpdate = now;
        }
        //每分钟更新
        if (now - this.minuteUpdate > 60000L) {
            this.onMinute();
            this.minuteUpdate = now;
        }
        //实体是空的
        if (this.entity == null) {
            PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
            for (EntityPlayerMP player:players.getPlayerList()){
                if (this.pos != null && player.getDistance(this.pos.x, this.pos.y, this.pos.z) < 50.0D && !player.worldObj.isRemote) {
                    ModSimLoader.hasLoadedFolks = true;
                    //重生
                    this.respawn(player.worldObj, this.pos.toBlockPos());
                }
            }
        }

        if (this.entity != null && !this.entity.worldObj.isRemote) {
            this.entity.onFolkUpdate();
            this.pos = V3.fromVec3d(this.entity.getPositionVector());
            this.pos.dimension = this.entity.dimension;
            boolean shouldDespawn = true;
            PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
            for(EntityPlayerMP player :players.getPlayerList()){
                if (player.getDistance(this.entity.posX,this.entity.posY,this.entity.posZ) < 80.0F) {
                    shouldDespawn = false;
                }
            }

            if (shouldDespawn) {
                this.entity.setDead();
                this.entity.theData = null;
                this.entity = null;
            }
        }

        if (this.job != null && this.shouldWork()) {
            if (!this.job.atWork) {
            }

            this.job.onUpdate();
        } else if (this.job != null && !this.shouldWork() && this.entity != null && this.job.atWork) {
            try {
                this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
            } catch (Exception var6) {
            }

            this.setStatus(I18n.format("container.sim.folk_data.Wandering"));
            this.job.stage = 0;
            this.job.atWork = false;
            this.job.onWayToWork = false;
            this.job.currentTask = null;
            this.stayPut = false;
        }

        if ((this.job == null || !this.shouldWork()) && this.entity != null) {
            if (!this.tasks.isEmpty()) {
                if (this.currentTask != null) {
                    this.currentTask.update();
                } else {
                    this.currentTask = (Task)this.tasks.get(0);
                    this.currentTask.begin();
                }
            } else if (ModSimLoader.isDayTime(this.entity.worldObj)) {
                this.pickRandomTask();
            } else {
                //睡觉
                this.addTask(new TaskSleep(this, -1L, I18n.format("container.sim.folk_data.Sleeping")));
            }
        }
    }
    public boolean shouldWork() {

        if (this.entity == null) {
            return false;
        } else if (this.job == null) {
            return false;
        } else if (this.pregnancyStage > 0.0F) {
            return false;
            //士兵
        } else if (this.job != null && this.job.jobName == I18n.format("container.sim.Vocation7")) {
            return !ModSimLoader.isDayTime(this.entity.worldObj);
        } else {
            return ModSimLoader.isDayTime(this.entity.worldObj);
        }
    }
    public void onSecond() {
        if (this.entity != null) {
            if (this.job != null && this.shouldWork()) {
                this.job.onSecond();
            }

            if (this.home != null) {
                if (this.entity == null) {
                    return;
                }

                if (ModSimLoader.isDayTime(this.entity.worldObj) && this.stayPut && this.isSleeping && this.shouldWork()) {
                    this.setStatus(I18n.format("container.sim.folk_data.Wandering"));
                    this.stayPut = false;
                    this.isSleeping = false;
                }
            }

            if (this.entity != null) {
                List<EntityFolk> nearbyFolks = this.entity.worldObj.getEntitiesWithinAABB(EntityFolk.class, new AxisAlignedBB(this.entity.posX - 3.0D, this.entity.posY - 1.0D, this.entity.posZ - 3.0D, this.entity.posX + 3.0D, this.entity.posY + 1.0D, this.entity.posZ + 3.0D));
                for (EntityFolk f:nearbyFolks){
                    NpcData fd = f.theData;
                    if (fd != null && fd.ID != this.ID) {
                        FolkRelationship rel = this.getRelationshipWith(fd);
                        if (rel != null) {
                            if (this.rand.nextInt(20) > 18) {
                                if (this.rand.nextInt(2) > 0) {
                                    rel.addLevel(1);
                                } else {
                                    rel.addLevel(-1);
                                }
                            }
                        } else if (this.rand.nextInt(10) > 8) {
                            this.addRelationship(fd);
                        }
                    }
                }

            }

        }
    }

    public void onMinute() {
        if (this.entity != null && !this.isDead) {
            if (this.job != null && this.shouldWork()) {
                this.job.onMinute();
            }

            NpcData father;
            if (this.home == null) {
                if (this.job == null || this.job != null && !this.shouldWork()) {
                    if (this.age < this.race.maturity) {
                        father = this.getParent(0);
                        NpcData mother = this.getParent(1);
                        Building newHome = null;
                        if (father != null && father.home != null) {
                            newHome = father.home;
                        }

                        if (mother != null && mother.home != null) {
                            newHome = mother.home;
                        }

                        if (newHome != null) {
                            newHome.occupants.add(this);
                            this.home = newHome;
                            String s1=I18n.format("container.sim.npcData_onupdate1");
                            String s2=I18n.format("container.sim.npcData_onupdate2");
                            ModSimLoader.sendChat(this.getName() + s1 + this.home.buildingName + s2);
                        }
                    } else {
                        Building empty = ModSimLoader.getEmptyHome();
                        if (empty != null) {
                            empty.occupants.add(this);
                            this.home = empty;

                            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString(this.getName() + I18n.format("container.sim.npcData_onupdate3") + empty.buildingName));
                            this.moveToXYZ(this.home.livingXYZ);
                        }
                    }
                }
            } else {
                if (this.currentTask instanceof TaskSleep && this.isAtBuilding(this.home) && this.gender == 0 && this.getSpouse() != null) {
                    father = this.getSpouse();
                    if (father.isAtBuilding(this.home) && father.pregnancyStage < 0.1F && this.rand.nextInt(6) == 5) {
                        this.addTask(new TaskProcreate(this, 10000L, father));
                        father.addTask(new TaskProcreate(father, 10000L, this));
                        this.currentTask.completeTask();
                        father.currentTask.completeTask();
                    }
                }

                if (this.pregnancyStage >= 1.0F) {
                    this.pregnancyStage = 0.0F;
                    new NpcData(this.entity.worldObj, this.getSpouse(), this);
                    this.setStatus(I18n.format("container.sim.folk_data_a_baby"));
                }
            }

        }
    }
    public void addTask(Task task) {
        if (task instanceof TaskSleep && this.currentTask != null && !(this.currentTask instanceof TaskSleep) && !this.currentTask.interruptSleep) {
            this.currentTask = null;
            this.tasks.clear();
        }

        this.tasks.add(task);
    }

    public void nextTask() {
        this.currentTask = null;
        this.tasks.remove(0);
        if (this.tasks.size() > 0) {
            this.currentTask = (Task)this.tasks.get(0);
            this.currentTask.begin();
        }

    }

    public void pickRandomTask() {
        if (this.home != null && this.rand.nextInt(4) == 3) {
            this.addTask(new TaskGoTo(this, (long)(this.rand.nextInt(30000) + 30000), this.home, "Relaxing at home"));
        } else if (this.rand.nextInt(4) == 3) {
            Iterator var1 = ModSimLoader.buildings.iterator();

            while(var1.hasNext()) {
                Building b = (Building)var1.next();
                if (b.controlXYZ.getDistanceTo(this.pos) < 40 && this.rand.nextInt(4) == 3) {
                    if (b.buildingType.contentEquals("Residential")) {
                        Iterator var3 = b.occupants.iterator();

                        label61:
                        while(true) {
                            NpcData fd;
                            do {
                                do {
                                    do {
                                        do {
                                            if (!var3.hasNext()) {
                                                break label61;
                                            }

                                            fd = (NpcData)var3.next();
                                        } while(fd.ID == this.ID);
                                    } while(this.isAdult() != fd.isAdult());
                                } while(fd.shouldWork());
                            } while(!(fd.currentTask instanceof TaskWander) && !(fd.currentTask instanceof TaskGoTo) && fd.currentTask != null);

                            this.addTask(new TaskSocialise(this, (long)(this.rand.nextInt(15000) + 15000), fd, b, false));
                            fd.currentTask = null;
                            fd.tasks.clear();
                            fd.addTask(new TaskSocialise(fd, (long)(this.rand.nextInt(15000) + 15000), this, b, true));
                        }
                    }

                    if (b.buildingType.contentEquals("Commercial")) {

                        this.addTask(new TaskGoTo(this, (long)(this.rand.nextInt(30000) + 30000), b, I18n.format("container.sim.folk_data_Shopping") + b.buildingName));
                    } else if (b.buildingType.contentEquals("Industrial")) {
                        this.addTask(new TaskGoTo(this, (long)(this.rand.nextInt(30000) + 30000), b, I18n.format("container.sim.folk_data_Visiting") + b.buildingName));
                    } else if (!b.buildingType.contentEquals("Residential")) {
                        this.addTask(new TaskGoTo(this, (long)(this.rand.nextInt(30000) + 30000), b, I18n.format("container.sim.folk_data_Visiting") + b.buildingName));
                    }
                    break;
                }
            }
        } else {
            this.addTask(new TaskWander(this, (long)(this.rand.nextInt(30000) + 30000)));
        }

    }
    public void addRelationship(NpcData folk2) {
        boolean relExists = false;

        Iterator var3 = this.relationships.iterator();

        while(var3.hasNext()) {
            FolkRelationship rel = (FolkRelationship)var3.next();
            if (rel.getOther() == folk2) {
                relExists = true;
            }
        }

        if (!relExists) {
            this.relationships.add(new FolkRelationship(this, folk2, EnumFamilyType.UNRELATED));
            folk2.relationships.add(new FolkRelationship(folk2, this, EnumFamilyType.UNRELATED));
        }

    }

    public NpcData getRelation(EnumFamilyType relCheck) {
        Iterator var2 = this.relationships.iterator();

        FolkRelationship rel;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            rel = (FolkRelationship)var2.next();
        } while(rel.familyType != relCheck);

        return rel.getOther();
    }

    public NpcData getParent(int gender) {
        NpcData npcData = null;
        for (FolkRelationship rel:this.relationships){
            if(rel.familyType != EnumFamilyType.PARENT || rel.getOther().gender != gender){
                npcData=rel.getOther();

            }
        }
        return npcData;
    }

    public NpcData getSpouse() {
        NpcData npcData = null;
        for (FolkRelationship rel:this.relationships){
            if(rel.familyType != EnumFamilyType.SPOUSE){
                npcData=rel.getOther();

            }
        }
        return npcData;
    }
    public boolean moveToXYZ(V3 v3) {
        if (!this.stayPut && this.entity != null && this.entity.getNavigator().tryMoveToXYZ(v3.x, v3.y, v3.z, 1.0D)) {
            double dist = Math.sqrt(Math.pow(v3.x - this.entity.posX, 2.0D) + Math.pow(v3.y - this.entity.posY, 2.0D) + Math.pow(v3.z - this.entity.posZ, 2.0D));
            double expectedtime = (double)System.currentTimeMillis() + dist * 0.6D;
            return true;
        } else {
            return false;
        }
    }

    public boolean forceMoveToXYZ(V3 v3) {
        this.entity.getNavigator().clearPathEntity();
        v3 = new V3(v3.x, v3.y + 1.0D, v3.z);
        if (this.entity.getNavigator().tryMoveToXYZ(v3.x, v3.y, v3.z, 1.0D)) {
            double dist = Math.sqrt(Math.pow(v3.x - this.entity.posX, 2.0D) + Math.pow(v3.y - this.entity.posY, 2.0D) + Math.pow(v3.z - this.entity.posZ, 2.0D));
            double expectedtime = (double)System.currentTimeMillis() + dist * 0.6D;
            return true;
        } else if (this.entity.getNavigator().setPath(this.entity.getNavigator().getPathToPos(v3.toBlockPos()), 1.0D)) {
            return true;
        } else {
            if (System.currentTimeMillis() - this.lastPathAttempt < 5000L) {
                if (System.currentTimeMillis() - this.lastPathAttempt > 2000L && this.entity.worldObj.getBlockState(v3.toBlockPos().up(2)).getBlock() == Blocks.AIR) {
                    this.entity.setPositionAndUpdate(v3.x + 0.5D, v3.y, v3.z + 0.5D);
                    this.entity.getNavigator().clearPathEntity();
                }
            } else {
                this.lastPathAttempt = System.currentTimeMillis();
            }

            return false;
        }
    }

    public boolean forceMoveToXYZNoWarp(V3 v3) {
        v3 = new V3(v3.x, v3.y + 1.0D, v3.z);
        if (this.entity.getNavigator().tryMoveToXYZ(v3.x, v3.y, v3.z, 1.0D)) {
            return true;
        } else {
            return this.entity.getNavigator().setPath(this.entity.getNavigator().getPathToPos(v3.toBlockPos()), 1.0D);
        }
    }
    public boolean isAtBuilding(Building b) {
        return this.isAtBuilding(b, 2.0F);
    }

    public boolean isAtBuilding(Building b, float maxDist) {
        if (this.entity == null) {
            return false;
        } else if (b.buildingType.toLowerCase().contentEquals("residential")) {
            return (float)b.livingXYZ.getDistanceTo(this.pos) < maxDist;
        } else {
            return (float)b.controlXYZ.getDistanceTo(this.pos) < maxDist;
        }
    }

    public boolean isAtLocation(V3 v3) {
        if (this.entity == null) {
            return false;
        } else {
            return Math.abs(this.entity.posX - v3.x) < 2.0D && Math.abs(this.entity.posZ - v3.z) < 2.0D;
        }
    }

    public boolean isAtLocation(V3 v3, int dist) {
        if (this.entity == null) {
            return false;
        } else {
            return Math.abs(this.entity.posX - v3.x) < (double)dist && Math.abs(this.entity.posZ - v3.z) < (double)dist;
        }
    }

    public boolean isAtLocation(BlockPos blockPos) {
        if (this.entity == null) {
            return false;
        } else {
            return (new BlockPos(this.entity)).distanceSq(blockPos) < 2.0D;
        }
    }
    public void adjustRelationship(NpcData other, int amount) {
        FolkRelationship rel = this.getRelationshipWith(other);
        if (rel != null) {
            rel.addLevel(amount);
        } else {
            this.addRelationship(other);
        }

    }
    /**
     * @Author fan
     * @Description //TODO 是成年人
     * @Date 18:57 2022/10/16
     * @Param []
     * @return boolean
     **/
    public boolean isAdult() {
        return this.age >= this.race.maturity;
    }
    /**
     * @Author fan
     * @Description //TODO 获取皮肤
     * @Date 13:17 2022/10/17
     * @Param []
     * @return java.lang.String
     **/
    public String getTexture() {
        String texture = "";
        try {
                //System.out.println("实体人性别："+theData.gender);
                if (this.gender == 0) {
                    texture = "male" + this.skinnumber + ".png";
                } else {
                    texture = "female" + this.skinnumber + ".png";
                }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("getTexture出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return texture;
    }
    /**
     * @Author fan
     * @Description //TODO NPC死亡
     * @Date 15:20 2022/10/18
     * @Param [cause]
     * @return void
     **/
    public void onDeath(DamageSource cause) {
        String deathMessage = "";
        if (cause == DamageSource.starve) {
            //张三 饿死了。他们当时18岁。
            deathMessage = this.getName() + I18n.format("container.sim.folk_data_death_by_They") + this.age + I18n.format("container.sim.folk_data_death_by_years");
        } else {
            deathMessage = this.getName() + I18n.format("container.sim.folk_data_death_by_were") + this.age + I18n.format("container.sim.folk_data_death_by_years");
        }

        this.isDead = true;
        for (NpcData npcData:ModSimLoader.folks){
            ModSimLoader.log.info("比较npc-ID: " + npcData.ID + " 和Id： " + this.ID);
            if (npcData.ID.contentEquals(this.ID) && !npcData.entity.worldObj.isRemote) {

            }
            ModSimLoader.log.info("找到匹配ID");
            ModSimLoader.sendChat(deathMessage);
            if (this.home != null) {
                this.home.occupants.remove(this);
                this.home.saveBuilding();
            }

            ModSimLoader.folks.remove(npcData);
            try {
                Files.deleteIfExists((new File(this.getSaveFolder() + File.separator + "npc" + File.separator + this.ID + ".sk2")).toPath());
                return;
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
    }
    /**
     * @Author fan
     * @Description //TODO 找到保存位置
     * @Date 15:43 2022/10/18
     * @Param []
     * @return java.lang.String
     **/
    public String getSaveFolder() {
        DimensionManager d = new DimensionManager();
        String worldPath = "";
        worldPath = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + File.separator + "sim";
        return worldPath;
    }
    /**
     * @Author fan
     * @Description //TODO 生成特征
     * @Date 17:30 2022/10/18
     * @Param []
     * @return void
     **/
    public void generateTraits() {
        try {
            Random rand = new Random();
            Trait[] traits1 = Traits.traitList;
            //Trait 1
            this.trait1 = Trait.getTraitFromName(traits1[rand.nextInt(traits1.length - 1)].traitName);


            //Trait 2
            this.trait2 = Trait.getTraitFromName(traits1[rand.nextInt(traits1.length - 1)].traitName);

            while (this.trait2 == this.trait1 || traitHasOpposite(this.trait2)) {
                this.trait2 = Trait.getTraitFromName(traits1[rand.nextInt(traits1.length - 1)].traitName);
            }


            //Trait 3
            this.trait3 = Trait.getTraitFromName(traits1[rand.nextInt(traits1.length - 1)].traitName);

            while (this.trait3 == this.trait2 || this.trait3 == this.trait1 || traitHasOpposite(this.trait3)) {
                this.trait3 = Trait.getTraitFromName(traits1[rand.nextInt(traits1.length - 1)].traitName);
            }



        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("generateTraits出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    /**
     * 特质具有相反的性质
     *
     * @param trait
     * @return
     */
    public boolean traitHasOpposite(Trait trait) {
        try {
            if (Trait.getTraitFromName(trait.traitName).traitOpposite != null) {
                if (trait.traitName.contains(Trait.getTraitFromName(trait.traitName).traitOpposite.traitName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("traitHasOpposite出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return false;
    }
    /**
     * 有特点
     *
     * @param trait
     * @return
     */
    public boolean hasTrait(Trait trait) {
        boolean flag = true;
        try {
            if (this.trait1.traitName.contentEquals(trait.traitName) || this.trait2.traitName.contentEquals(trait.traitName) || this.trait3.traitName.contentEquals(trait.traitName)) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("hasTrait出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return flag;
    }
}
