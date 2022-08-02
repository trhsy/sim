package com.trhsy.sim.common.core.entity.functionality;

import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName MiningBox
 * @Description todo 采矿箱
 * @Author Tian
 * @Date 2022/1/2319:58
 **/
public class MiningBox implements Serializable {
    private static final long serialVersionUID = 2951402828966206500L;
    public V3 location;
    public V3 marker1XYZ;
    public V3 marker2XYZ;
    public V3 marker3XYZ;
    public int discards = 0;
    public boolean addGlassCover = true;
    public int size = 3;

    public MiningBox() {
    }

    public MiningBox(V3 location) {
        this.location = location;
    }

    public MiningBox(V3 inxyz, V3 m1xyz, V3 m2xyz, V3 m3xyz, int filterblocks, int size) {
        try {
            this.location = inxyz;
            this.marker1XYZ = m1xyz;
            this.marker2XYZ = m2xyz;
            this.marker3XYZ = m3xyz;
            this.discards = filterblocks;
            this.size = size;
        } catch (Exception e) {
        }


    }

    public static MiningBox getMiningBlockByBoxXYZ(V3 location) {
        MiningBox ret = null;
        try {
            int x;
            MiningBox block;
            for (x = 0; x < ModSimReloaded.theMiningBoxes.size(); ++x) {
                block = (MiningBox) ModSimReloaded.theMiningBoxes.get(x);
                if (block.location.isSameCoordsAs(location, true, true)) {
                    ret = block;
                    break;
                }
            }
            if (ret == null) {
                for (x = 0; x < ModSimReloaded.theMiningBoxes.size(); ++x) {
                    block = (MiningBox) ModSimReloaded.theMiningBoxes.get(x);
                    if (block.location.isSameCoordsAs(location, false, true)) {
                        ret = block;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getMiningBlockByBoxXYZ出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    public static void loadMiningBoxes() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            File mineFiles = new File(ModSimReloaded.getSavesDataFolder() + "Mining" + File.separator);
            mineFiles.mkdirs();
            boolean useNewFormat = false;
            File[] arr$ = mineFiles.listFiles();
            int len$ = arr$.length;

            int i$;
            File f;
            for (i$ = 0; i$ < len$; i$++) {
                f = arr$[i$];
                if (f.getName().endsWith(".sk2")) {
                    useNewFormat = true;
                    break;
                }
            }

            WorldServer theWorld;
            Block id;
            if (useNewFormat) {
                ModSimReloaded.theMiningBoxes.clear();
                arr$ = mineFiles.listFiles();
                len$ = arr$.length;

                for (i$ = 0; i$ < len$; i$++) {
                    f = arr$[i$];
                    if (f.getName().endsWith(".sk2")) {
                        List<String> strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                        MiningBox box = new MiningBox();
                        Iterator iterator = strings.iterator();

                        while (iterator.hasNext()) {
                            String line = (String) iterator.next();
                            if (line.contains("|")) {
                                int m1 = line.indexOf("|");
                                String name = line.substring(0, m1);
                                String value = line.substring(m1 + 1);
                                if (name.contentEquals("location")) {
                                    String[] v = value.split(",");
                                    double x = Double.parseDouble(v[0]);
                                    double y = Double.parseDouble(v[1]);
                                    double z = Double.parseDouble(v[2]);
                                    box.location = new V3(x,y,z);
                                } else if (name.contentEquals("m1")) {
                                    if (!value.contentEquals("null")) {
                                        String[] v = value.split(",");
                                        double x = Double.parseDouble(v[0]);
                                        double y = Double.parseDouble(v[1]);
                                        double z = Double.parseDouble(v[2]);
                                        box.marker1XYZ = new V3(x,y,z);
                                    }
                                } else if (name.contentEquals("m2")) {
                                    if (!value.contentEquals("null")) {
                                        String[] v = value.split(",");
                                        double x = Double.parseDouble(v[0]);
                                        double y = Double.parseDouble(v[1]);
                                        double z = Double.parseDouble(v[2]);
                                        box.marker2XYZ = new V3(x,y,z);
                                    }
                                } else if (name.contentEquals("m3")) {
                                    if (!value.contentEquals("null")) {
                                        String[] v = value.split(",");
                                        double x = Double.parseDouble(v[0]);
                                        double y = Double.parseDouble(v[1]);
                                        double z = Double.parseDouble(v[2]);
                                        box.marker3XYZ = new V3(x,y,z);
                                    }
                                } else if (name.contentEquals("discards")) {
                                    box.discards = Integer.parseInt(value);
                                } else if (name.contentEquals("cover")) {
                                    box.addGlassCover = Boolean.parseBoolean(value);
                                } else if (name.contentEquals("hsize")) {
                                    box.size = Integer.parseInt(value);
                                }
                            }
                        }

                        theWorld = MinecraftServer.getServer().worldServerForDimension(box.location.theDimension);
                        if (theWorld != null) {
                            id = theWorld.getBlockState(new BlockPos(box.location.x, box.location.y, box.location.z)).getBlock();
                            if (id == BlockLoader.blockMiningBox) {
                                ModSimReloaded.theMiningBoxes.add(box);
                            } else {
                                f.delete();
                            }
                        }
                    }
                }
            } else {
                arr$ = mineFiles.listFiles();
                len$ = arr$.length;

                for (i$ = 0; i$ < len$; i$++) {
                    f = arr$[i$];
                    if (f.getName().endsWith(".suk")) {
                        MiningBox mining = (MiningBox) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
                        if (mining != null) {
                            V3 xyz = mining.location;
                            theWorld = MinecraftServer.getServer().worldServerForDimension(xyz.theDimension);
                            if (theWorld == null) {
                                f.delete();
                            } else {
                                try {
                                    id = theWorld.getBlockState(new BlockPos(xyz.x, xyz.y, xyz.z)).getBlock();
                                    if (id == BlockLoader.blockMiningBox && mining != null) {
                                        ModSimReloaded.theMiningBoxes.add(mining);
                                    } else {
                                        f.delete();
                                    }
                                } catch (Exception e) {
                                    //var14.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("loadMiningBoxes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    public static void saveMiningBoxes() {
        try {
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.SERVER) {
                List<String> strings = new CopyOnWriteArrayList();

                for (int b = 0; b < ModSimReloaded.theMiningBoxes.size(); ++b) {
                    try {
                        MiningBox mining = (MiningBox) ModSimReloaded.theMiningBoxes.get(b);
                        strings.clear();
                        strings.add("location|" + mining.location.toString());
                        if (mining.marker1XYZ != null) {
                            strings.add("m1|" + mining.marker1XYZ.toString());
                            if (mining.marker2XYZ != null) {
                                strings.add("m2|" + mining.marker2XYZ.toString());
                            }

                            if (mining.marker3XYZ != null) {
                                strings.add("m3|" + mining.marker3XYZ.toString());
                            }

                            strings.add("discards|" + mining.discards);
                            strings.add("cover|" + mining.addGlassCover);
                            strings.add("hsize|" + mining.size);
                            String xyz = "m" + mining.location.toString().replaceAll(",", "_");
                            ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Mining" + File.separator + xyz + ".sk2", strings);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("saveMiningBoxes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }
}
