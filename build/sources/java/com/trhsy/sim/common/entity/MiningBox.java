package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.ModSim;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

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
        this.location = inxyz;
        this.marker1XYZ = m1xyz;
        this.marker2XYZ = m2xyz;
        this.marker3XYZ = m3xyz;

        try {
            this.discards = filterblocks;
        } catch (Exception var8) {
        }

        this.size = size;
    }

    public static MiningBox getMiningBlockByBoxXYZ(V3 location) {
        MiningBox ret = null;

        int x;
        MiningBox block;
        for(x = 0; x < ModSim.theMiningBoxes.size(); ++x) {
            block = (MiningBox) ModSim.theMiningBoxes.get(x);
            if (block.location.isSameCoordsAs(location, true, true)) {
                ret = block;
                break;
            }
        }

        if (ret == null) {
            for(x = 0; x < ModSim.theMiningBoxes.size(); ++x) {
                block = (MiningBox) ModSim.theMiningBoxes.get(x);
                if (block.location.isSameCoordsAs(location, false, true)) {
                    ret = block;
                    break;
                }
            }
        }

        return ret;
    }

    public static void loadMiningBoxes() {
        Minecraft mc = Minecraft.getMinecraft();
        File mineFiles = new File(ModSim.getSavesDataFolder() + "Mining" + File.separator);
        mineFiles.mkdirs();
        boolean useNewFormat = false;
        File[] arr$ = mineFiles.listFiles();
        int len$ = arr$.length;

        int i$;
        File f;
        for(i$ = 0; i$ < len$; ++i$) {
            f = arr$[i$];
            if (f.getName().endsWith(".sk2")) {
                useNewFormat = true;
                break;
            }
        }

        WorldServer theWorld;
        Block id;
        if (useNewFormat) {
            ModSim.theMiningBoxes.clear();
            arr$ = mineFiles.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".sk2")) {
                    ArrayList<String> strings = ModSim.loadSK2(f.getAbsoluteFile().toString());
                    MiningBox box = new MiningBox();
                    Iterator iterator = strings.iterator();

                    while(iterator.hasNext()) {
                        String line = (String)iterator.next();
                        if (line.contains("|")) {
                            int m1 = line.indexOf("|");
                            String name = line.substring(0, m1);
                            String value = line.substring(m1 + 1);
                            if (name.contentEquals("location")) {
                                box.location = new V3(value);
                            } else if (name.contentEquals("m1")) {
                                if (!value.contentEquals("null")) {
                                    box.marker1XYZ = new V3(value);
                                }
                            } else if (name.contentEquals("m2")) {
                                if (!value.contentEquals("null")) {
                                    box.marker2XYZ = new V3(value);
                                }
                            } else if (name.contentEquals("m3")) {
                                if (!value.contentEquals("null")) {
                                    box.marker3XYZ = new V3(value);
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
                        id = theWorld.getBlock(box.location.x.intValue(), box.location.y.intValue(), box.location.z.intValue());
                        if (id == ModSim.miningBox) {
                            ModSim.theMiningBoxes.add(box);
                        } else {
                            f.delete();
                        }
                    }
                }
            }
        } else {
            arr$ = mineFiles.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".suk")) {
                    MiningBox mining = (MiningBox) ModSim.proxy.loadObject(f.getAbsoluteFile().toString());
                    if (mining != null) {
                        V3 xyz = mining.location;
                        theWorld = MinecraftServer.getServer().worldServerForDimension(xyz.theDimension);
                        if (theWorld == null) {
                            f.delete();
                        } else {
                            try {
                                id = theWorld.getBlock(xyz.x.intValue(), xyz.y.intValue(), xyz.z.intValue());
                                if (id == ModSim.miningBox && mining != null) {
                                    ModSim.theMiningBoxes.add(mining);
                                } else {
                                    f.delete();
                                }
                            } catch (Exception var14) {
                                var14.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }

    public static void saveMiningBoxes() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER) {
            ArrayList<String> strings = new ArrayList();

            for(int b = 0; b < ModSim.theMiningBoxes.size(); ++b) {
                try {
                    MiningBox mining = (MiningBox) ModSim.theMiningBoxes.get(b);
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
                        ModSim.saveSK2(ModSim.getSavesDataFolder() + "Mining" + File.separator + xyz + ".sk2", strings);
                    }
                } catch (Exception var5) {
                }
            }
        }

    }
}
