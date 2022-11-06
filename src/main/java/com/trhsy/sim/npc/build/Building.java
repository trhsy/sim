package com.trhsy.sim.npc.build;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
    public String buildingType = "";
    public String jobType = "";
    public int length = 0;
    public int width = 0;
    public int height = 0;
    public int dimension = 0;
    public float rent = 0.0F;
    public V3 controlXYZ;
    public V3 livingXYZ;
    public List<V3> structure = new ArrayList();
    public List<NpcData> occupants = new ArrayList();
    public BlockPos bed;
    public BlockPos furnace;
    public BlockPos craftingTable;
    public BlockPos buyingPos;
    public boolean markedForDeletion;

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
                var14.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (Exception var13) {
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
                                ModSimLoader.log.info("Found occupant: " + ModSimLoader.getFolkDataByUID(f).getName());
                            }
                        }
                    }

                    line = reader.readLine();
                }

                reader.close();
                break;
            }
        } catch (Exception var15) {
            var15.printStackTrace();
        }

    }

    public void demolish(World world, boolean removeStructure) {
        this.markedForDeletion = true;

        while(this.occupants.size() > 0) {
            ((NpcData)this.occupants.get(0)).evict();
        }
        for (NpcData fd:ModSimLoader.folks){
            if (fd.job != null && fd.job.workPlace == this.controlXYZ) {
                fd.fire();
            }
        }
        if (removeStructure) {
            for (V3 v3:this.structure){
                world.setBlockToAir(v3.toBlockPos());
            }
        }

        removeBuilding(this.ID);
    }
    public void removeBuilding(UUID uid){
        new DimensionManager();
        File buildingFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "buildings");
        ModSimLoader.buildings.remove(this);
        File logFile = new File(buildingFolder + File.separator + uid + ".sk2");
        logFile.delete();
    }

}
