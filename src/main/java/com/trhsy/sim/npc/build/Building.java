package com.trhsy.sim.npc.build;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: Building
 * @Description: 建筑
 * @date 2022/10/13 17:30
 */
public class Building {
    public UUID ID = null;
    public String buildingName = "";
    public String buildingType = "NULL";
    public String jobType = "";
    public String author = "Trhsy";
    public int length = 0;
    public int width = 0;
    public int height = 0;
    public int dimension = 0;
    public float rent = 0.0F;
    public V3 controlXYZ;
    public V3 livingXYZ;
    public List<V3> structure = new CopyOnWriteArrayList<>();
    public List<NpcData> occupants = new CopyOnWriteArrayList();
    public BlockPos bed;
    public BlockPos furnace;
    public BlockPos craftingTable;
    public BlockPos buyingPos;
    public boolean markedForDeletion;
    /**
     * @Author fan
     * @Description //TODO
     * @Date 22:00 2022/11/7
     * @Param [bName, rent, ctrl, lv] 建筑 租金 控制箱 生活区
     * @return
     **/
    public Building(String bName, float rent, V3 ctrl, V3 lv) {
        this.buildingName = bName;
        this.controlXYZ = ctrl;
        this.livingXYZ = lv;
        this.rent = rent;
        this.ID = UUID.randomUUID();
        ModSimLoader.log.info(this.ID.toString());
        this.saveBuilding();
    }

    public Building(int l, int w, int h, float rent, V3 ctrl, V3 lv) {
        this.length = l;
        this.width = w;
        this.height = h;
        this.rent = rent;
        this.controlXYZ = ctrl;
        this.livingXYZ = lv;
        this.ID = UUID.randomUUID();
        this.saveBuilding();
    }

    public Building(World world, UUID uuid) {
        this.loadBuilding(world, uuid);
    }
    /**
     * @Author fan
     * @Description //TODO 保存建筑
     * @Date 18:59 2022/11/6
     * @Param []
     * @return void
     **/
    public void saveBuilding() {
        if (!this.markedForDeletion) {
            new DimensionManager();
            File buildingFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "buildings");
            if(!buildingFolder.exists()){
                buildingFolder.mkdirs();
            }
            BufferedWriter writer = null;

            try {
                File logFile = new File(buildingFolder + File.separator + this.ID + ".sk2");
                writer = new BufferedWriter(new FileWriter(logFile));
                writer.write("id|" + this.ID.toString() + "\n");
                writer.write("name|" + this.buildingName + "\n");
                writer.write("type|" + this.buildingType + "\n");
                writer.write("dim|" + this.length + "," + this.width + "," + this.height + "\n");
                writer.write("dimension|" + this.dimension + "\n");
                writer.write("rent|" + this.rent + "\n");
                writer.write("jobtypes|" + this.jobType + "\n");
                writer.write("cpos|" + this.controlXYZ.toString() + "\n");
                writer.write("lpos|" + this.livingXYZ.toString() + "\n");
                writer.write("structure|");
                Iterator var4 = this.structure.iterator();

                while(var4.hasNext()) {
                    V3 pos = (V3)var4.next();
                    writer.write(pos.toString() + ";");
                }

                writer.write("\noccupants|");
                var4 = this.occupants.iterator();

                while(var4.hasNext()) {
                    NpcData folk = (NpcData)var4.next();
                    if (folk != null) {
                        writer.write(folk.ID + ";");
                    }
                }
            } catch (Exception var14) {
                StackTraceElement element = var14.getStackTrace()[0];
                ModSimLoader.log.error("建筑保存，出错了：" + var14.getMessage() + "行数：" + element.getLineNumber());
            } finally {
                try {
                    writer.close();
                } catch (Exception var13) {
                    StackTraceElement element = var13.getStackTrace()[0];
                    ModSimLoader.log.error("建筑保存，关闭BufferedWriter出错了：" + var13.getMessage() + "行数：" + element.getLineNumber());
                }

            }

        }
    }
    /**
     * @Author fan
     * @Description //TODO 加载建筑
     * @Date 18:58 2022/11/6
     * @Param [world, loadID]
     * @return void
     **/
    public void loadBuilding(World world, UUID loadID) {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();

        File buildingFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "buildings");
        if(!buildingFolder.exists()){
            buildingFolder.mkdirs();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(buildingFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));
            String line = reader.readLine();

            while(true) {
                while(line != null) {
                    int m1 = line.indexOf("|");
                    line.substring(0, m1);
                    String value = line.substring(m1 + 1);
                    if (line.contains("id|")) {
                        this.ID = UUID.fromString(value);
                    } else if (line.contains("name|")) {
                        this.buildingName = value;
                    } else if (line.contains("type|")) {
                        this.buildingType = value;
                    } else if (line.contains("dim|")) {
                        this.length = Integer.valueOf(value.split(",")[0]);
                        this.width = Integer.valueOf(value.split(",")[1]);
                        this.height = Integer.valueOf(value.split(",")[2]);
                    } else if (line.contains("dimension")) {
                        this.dimension = Integer.valueOf(value);
                    } else if (line.contains("rent")) {
                        this.rent = Float.valueOf(value);
                    } else if (line.contains("jobtypes|")) {
                        this.jobType = value;
                    } else if (line.contains("cpos")) {
                        this.controlXYZ = V3.fromString(value);
                    } else if (line.contains("lpos")) {
                        this.livingXYZ = V3.fromString(value);
                    } else {
                        String[] folk;
                        String[] var10;
                        int var11;
                        int var12;
                        String f;
                        //结构
                        if (line.contains("structure")) {
                            if (value.length() < 1) {
                                line = reader.readLine();
                                continue;
                            }

                            folk = value.split(";");
                            var10 = folk;
                            var11 = folk.length;

                            for(var12 = 0; var12 < var11; ++var12) {
                                f = var10[var12];
                                this.structure.add(V3.fromString(f));
                            }
                            //居住者
                        } else if (line.contains("occupants")) {
                            if (value.length() < 1) {
                                line = reader.readLine();
                                continue;
                            }

                            folk = value.split(";");
                            var10 = folk;
                            var11 = folk.length;

                            for(var12 = 0; var12 < var11; ++var12) {
                                f = var10[var12];
                                NpcData fd = ModSimLoader.getFolkDataByUID(f);
                                this.occupants.add(fd);
                                fd.home = this;
                                //ModSimLoader.log.info("找到居住者: " + ModSimLoader.getFolkDataByUID(f).getName());
                            }
                        }
                    }

                    line = reader.readLine();
                }

                reader.close();
                break;
            }
        } catch (Exception var15) {
            StackTraceElement element = var15.getStackTrace()[0];
            ModSimLoader.log.error("loadBuilding出错了：" + var15.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    /**
     * @Author fan
     * @Description //TODO 建筑拆除
     * @Date 10:23 2022/11/8
     * @Param [world, removeStructure]
     * @return void
     **/
    public void demolish(World world, boolean removeStructure) {
        this.markedForDeletion = true;
        for (NpcData npcData:this.occupants){
            npcData.evict();
        }
        for (NpcData fd:ModSimLoader.folks){
            if (fd.job != null && fd.job.workPlace == this.controlXYZ) {
                fd.fire();
            }
        }
        if (removeStructure) {
            for (V3 v3:this.structure){
                //播放拆除音效
//                world.playSound(v3.x,v3.y,v3.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1,false);
                BlockPos block=v3.toBlockPos();
                world.destroyBlock(block,false);
//                world.setBlockToAir(block);
            }
        }

        removeBuilding(this.ID);
    }
    /**
     * @Author fan
     * @Description //TODO 移除建筑
     * @Date 20:54 2022/11/14
     * @Param [uid]
     * @return void
     **/
    public void removeBuilding(UUID uid){
        new DimensionManager();
        File buildingFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "buildings");
        ModSimLoader.buildings.remove(this);
        File logFile = new File(buildingFolder + File.separator + uid + ".sk2");
        logFile.delete();
    }

}
