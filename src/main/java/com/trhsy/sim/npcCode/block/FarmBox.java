package com.trhsy.sim.npcCode.block;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.enums.FarmType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.util.UUID;

/**
 * @ClassName FarmBox
 * @Description todo
 * @Author TRHSY
 * @Date 2022/10/2117:25
 **/
public class FarmBox {
    public UUID ID;
    /**所在位置**/
    public V3 loc;
    /**雇佣的员工**/
    public NpcData employee;
    /**等级**/
    public int level;
    /**方向**/
    public EnumFacing facing;
    /**农场类型**/
    public FarmType farmType;
    public int x;
    public int z;

    public FarmBox(V3 pos, V3 start, int x, int z) {
        this.facing = EnumFacing.EAST;
        this.farmType=FarmType.WHEAT;
        this.ID = UUID.randomUUID();
        this.loc = pos;
        this.x = x;
        this.z = z;
    }

    public FarmBox(V3 pos, V3 start) {
        this.facing = EnumFacing.EAST;
        this.farmType=FarmType.WHEAT;
        this.ID = UUID.randomUUID();
        this.loc = pos;
        this.x = 9;
        this.z = 9;
    }

    public FarmBox(UUID uuid) {
        this.facing = EnumFacing.EAST;
        this.farmType=FarmType.WHEAT;
        this.loadFarm(uuid);
    }
    /**
     * @Author fan
     * @Description //TODO 获取角落
     * @Date 10:03 2022/12/10
     * @Param []
     * @return net.minecraft.util.math.BlockPos
     **/
    public BlockPos getCorner() {
        return this.loc.toBlockPos().offset(this.facing);
    }

    public void saveFarm() {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();
        File farmFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "farms");
        if(!farmFolder.exists()){
            farmFolder.mkdirs();
        }
        BufferedWriter writer = null;

        try {
            File logFile = new File(farmFolder + File.separator + this.ID + ".sk2");
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("id|" + this.ID.toString() + "\n");
            writer.write("loc|" + this.loc.toString() + "\n");
            writer.write("dimension|" + this.loc.dimension + "\n");
            writer.write("facing|" + this.facing.toString() + "\n");
            writer.write("farmType|" + this.farmType.toString() + "\n");
            if(this.employee!=null){
                writer.write("npc|" + this.employee.ID + "\n");
            }
            writer.write("x|" + this.x + "\n");
            writer.write("z|" + this.z + "\n");
            writer.write("level|" + this.level + "\n");
        } catch (Exception var12) {
            StackTraceElement element = var12.getStackTrace()[0];
            ModSimLoader.log.error("saveFarm出错了：" + var12.getMessage() + "行数：" + element.getLineNumber());
        } finally {
            try {
                writer.close();
            } catch (Exception var11) {
                StackTraceElement element = var11.getStackTrace()[0];
                ModSimLoader.log.error("saveFarm-writer出错了：" + var11.getMessage() + "行数：" + element.getLineNumber());
            }

        }

    }

    public void loadFarm(UUID loadID) {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();
        File farmFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "farms");
        if(!farmFolder.exists()){
            farmFolder.mkdirs();
        }

        try {
            //BufferedReader reader = new BufferedReader(new FileReader(farmFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));
            InputStream inputStream =new FileInputStream(new File(farmFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                int m1 = line.indexOf("|");
                line.substring(0, m1);
                String value = line.substring(m1 + 1);
                if (line.contains("id|")) {
                    this.ID = UUID.fromString(value);
                } else if (line.contains("loc")) {
                    this.loc = V3.fromString(value);
                } else if (line.contains("dimension")) {
                    this.loc.dimension = Integer.valueOf(value);
                } else if (line.contains("facing")) {
                    this.facing = EnumFacing.byName(value);
                } else if (line.contains("farmType")) {
                    this.farmType = FarmType.byName(value);
                }else if (line.contains("npc")) {
                    this.employee = ModSimLoader.getFolkDataByUID(UUID.fromString(value));
                } else if (line.contains("x|")) {
                    this.x = Integer.valueOf(value);
                } else if (line.contains("z|")) {
                    this.z = Integer.valueOf(value);
                } else if (line.contains("level|")) {
                    this.level = Integer.valueOf(value);
                }
            }

            reader.close();
        } catch (Exception var8) {
            StackTraceElement element = var8.getStackTrace()[0];
            ModSimLoader.log.error("loadFarm出错了：" + var8.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    public void removeFarm (UUID uid){
        ModSimLoader.buildings.remove(this);
        File logFile = new File(ModSimLoader.getSavesDataFolder() + File.separator + "farms" + File.separator + uid + ".sk2");
        if(logFile.delete()){
            logFile.deleteOnExit();
        }
    }
}