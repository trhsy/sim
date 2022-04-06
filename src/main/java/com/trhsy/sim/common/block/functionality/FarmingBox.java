package com.trhsy.sim.common.block.functionality;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.loader.BlockLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @ClassName FarmingBox
 * @Description todo 养殖箱
 * @Author Tian
 * @Date 2022/1/2320:01
 **/
public class FarmingBox implements Serializable {
    private static final long serialVersionUID = -4049876797684922153L;

    public V3 location;
    public V3 marker1XYZ;
    public V3 marker2XYZ;
    public V3 marker3XYZ;
    public FarmType farmType;
    public int level;

    public FarmingBox() {
        this.farmType = FarmType.WHEAT;
        this.level = 1;
    }

    public FarmingBox(V3 inxyz) {
        this.farmType = FarmType.WHEAT;
        this.level = 1;
        this.location = inxyz;
        if (this.level == 0) {
            this.level = 1;
        }

    }

    public FarmingBox(V3 inxyz, V3 m1xyz, V3 m2xyz, V3 m3xyz) {
        this.farmType = FarmType.WHEAT;
        this.level = 1;
        this.location = inxyz;
        this.marker1XYZ = m1xyz;
        this.marker2XYZ = m2xyz;
        this.marker3XYZ = m3xyz;
        if (this.level == 0) {
            this.level = 1;
        }

    }

    public V3 getMarkerVector(int markerNum) {
        V3 ret = null;

        try {
            if (markerNum == 1) {
                ret = this.marker1XYZ;
            } else if (markerNum == 2) {
                ret = this.marker2XYZ;
            } else if (markerNum == 3) {
                ret = this.marker3XYZ;
            }

            return ret;
        } catch (Exception var5) {
            var5.printStackTrace();
            return new V3(0.0D, 0.0D, 0.0D, 0);
        }
    }

    public int getSizeWidth() {
        boolean var1 = false;

        int ltr;
        try {
            V3 m1 = this.getMarkerVector(1);
            V3 m2 = this.getMarkerVector(2);
            V3 m3 = this.getMarkerVector(3);
            if (m1.x.intValue() == m2.x.intValue()) {
                ltr = (int)(Math.abs(m2.z - m1.z) - 1.0D);
            } else {
                ltr = (int)(Math.abs(m2.x - m1.x) - 1.0D);
            }
        } catch (Exception var5) {
            return 5;
        }

        return Math.abs(ltr);
    }

    public int getSizeLength() {
        boolean var1 = false;

        int ftb;
        try {
            V3 m1 = this.getMarkerVector(1);
            V3 m2 = this.getMarkerVector(2);
            V3 m3 = this.getMarkerVector(3);
            if (m1.x.intValue() == m3.x.intValue()) {
                ftb = (int)(Math.abs(m3.z - m1.z) - 1.0D);
            } else {
                ftb = (int)(Math.abs(m3.x - m1.x) - 1.0D);
            }
        } catch (Exception var5) {
            return 5;
        }

        return Math.abs(ftb);
    }

    public ArrayList<V3> getSoilBlockPoints() {
        ArrayList<V3> ret = new ArrayList();
        V3 m1 = this.getMarkerVector(1);
        V3 m2 = this.getMarkerVector(2);
        V3 m3 = this.getMarkerVector(3);
        V3 c = m1.clone();
        int length = this.getSizeLength();
        if (length == 1) {
            ModSim.log.warn("FarmingBox: 无法使用5x5默认值确定农场大小");
        }

        for(int o = 0; o <= length; ++o) {
            for(int i = 0; i <= this.getSizeWidth(); ++i) {
                ret.add(c.clone());
                if (m2.x > m1.x) {
                    c.x = m1.x + (double)i;
                } else if (m2.x < m1.x) {
                    c.x = m1.x - (double)i;
                } else if (m2.z > m1.z) {
                    c.z = m1.z + (double)i;
                } else if (m2.z < m1.z) {
                    c.z = m1.z - (double)i;
                }
            }

            if (m3.x > m1.x) {
                c.x = m1.x + (double)o;
            } else if (m3.x < m1.x) {
                c.x = m1.x - (double)o;
            } else if (m3.z > m1.z) {
                c.z = m1.z + (double)o;
            } else if (m3.z < m1.z) {
                c.z = m1.z - (double)o;
            }
        }

        return ret;
    }

    public ArrayList<V3> getPerimeterPoints() {
        ArrayList ret = new ArrayList();

        try {
            V3 m1 = this.getMarkerVector(1);
            V3 m2 = this.getMarkerVector(2);
            V3 m3 = this.getMarkerVector(3);
            V3 b = this.getLocation();
            V3 c = b.clone();

            int i;
            Double var9;
            Double var10;
            for(i = 0; i <= this.getSizeWidth() + 1; ++i) {
                if (m2.x - b.x > 1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x + 1.0D;
                } else if (m2.x - b.x < -1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x - 1.0D;
                } else if (m2.z - b.z > 1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z + 1.0D;
                } else if (m2.z - b.z < -1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z - 1.0D;
                }

                ret.add(c.clone());
            }

            for(i = 0; i <= this.getSizeLength() + 2; ++i) {
                if (m3.x - b.x > 1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x + 1.0D;
                } else if (m3.x - b.x < -1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x - 1.0D;
                } else if (m3.z - b.z > 1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z + 1.0D;
                } else if (m3.z - b.z < -1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z - 1.0D;
                }

                ret.add(c.clone());
            }

            for(i = 0; i <= this.getSizeWidth() + 2; ++i) {
                if (m2.x - b.x > 1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x - 1.0D;
                } else if (m2.x - b.x < -1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x + 1.0D;
                } else if (m2.z - b.z > 1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z - 1.0D;
                } else if (m2.z - b.z < -1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z + 1.0D;
                }

                ret.add(c.clone());
            }

            for(i = 0; i <= this.getSizeLength() + 2; ++i) {
                if (m3.x - b.x > 1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x - 1.0D;
                } else if (m3.x - b.x < -1.0D) {
                    var9 = c.x;
                    var10 = c.x = c.x + 1.0D;
                } else if (m3.z - b.z > 1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z - 1.0D;
                } else if (m3.z - b.z < -1.0D) {
                    var9 = c.z;
                    var10 = c.z = c.z + 1.0D;
                }

                ret.add(c.clone());
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return ret;
    }

    public V3 getLocation() {
        return this.location;
    }

    public static FarmingBox getFarmingBlockByBoxXYZ(V3 xyz) {
        FarmingBox ret = null;
        if (ModSim.theFarmingBoxes.size() == 0) {
            loadFarmingBoxes();
        }

        for (int x = 0; x < ModSim.theFarmingBoxes.size(); ++x) {
            FarmingBox block = (FarmingBox) ModSim.theFarmingBoxes.get(x);
            if (block.location.isSameCoordsAs(xyz, true, true)) {
                ret = block;
                break;
            }
        }

        return ret;
    }

    public static void loadFarmingBoxes() {
        Minecraft mc = Minecraft.getMinecraft();
        File farmFiles = new File(ModSim.getSavesDataFolder() + "Farming" + File.separator);
        farmFiles.mkdirs();
        boolean useNewFormat = false;
        File[] arr$ = farmFiles.listFiles();
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
            ModSim.theFarmingBoxes.clear();
            arr$ = farmFiles.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".sk2")) {
                    ArrayList<String> strings = ModSim.loadSK2(f.getAbsoluteFile().toString());
                    FarmingBox box = new FarmingBox();
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
                            } else if (name.contentEquals("type")) {
                                box.farmType = FarmType.valueOf(value);
                            } else if (name.contentEquals("level")) {
                                box.level = Integer.parseInt(value);
                            }
                        }
                    }

                    theWorld = MinecraftServer.getServer().worldServerForDimension(box.location.theDimension);
                    if (theWorld != null) {
                        id = theWorld.getBlock(box.location.x.intValue(), box.location.y.intValue(), box.location.z.intValue());
                        if (id == BlockLoader.blockFarmingBox) {
                            ModSim.theFarmingBoxes.add(box);
                        } else {
                            f.delete();
                        }
                    }
                }
            }
        } else {
            arr$ = farmFiles.listFiles();
            len$ = arr$.length;

            for(i$ = 0; i$ < len$; ++i$) {
                f = arr$[i$];
                if (f.getName().endsWith(".suk")) {
                    FarmingBox farming = (FarmingBox) ModSim.proxy.loadObject(f.getAbsoluteFile().toString());
                    if (farming != null) {
                        V3 xyz = farming.location;
                        theWorld = MinecraftServer.getServer().worldServerForDimension(xyz.theDimension);
                        if (theWorld == null) {
                            f.delete();
                        } else {
                            try {
                                id = theWorld.getBlock(xyz.x.intValue(), xyz.y.intValue(), xyz.z.intValue());
                                if (id == BlockLoader.blockFarmingBox) {
                                    ModSim.theFarmingBoxes.add(farming);
                                } else {
                                    f.delete();
                                }
                            } catch (Exception var14) {
                                var14.printStackTrace();
                            }
                        }
                    } else {
                        f.delete();
                        String s = I18n.format("container.sim.farming_box_boxes");
                        ModSim.sendChat(s);
                    }
                }
            }
        }

    }

    public static void saveFarmingBoxes() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER) {
            ArrayList<String> strings = new ArrayList();

            for (int b = 0; b < ModSim.theFarmingBoxes.size(); ++b) {
                FarmingBox farming = (FarmingBox) ModSim.theFarmingBoxes.get(b);
                strings.clear();
                if (farming != null && farming.location != null && farming.marker1XYZ != null) {
                    try {
                        strings.add("location|" + farming.location.toString());
                        strings.add("m1|" + farming.marker1XYZ.toString());
                        strings.add("m2|" + farming.marker2XYZ.toString());
                        strings.add("m3|" + farming.marker3XYZ.toString());
                        strings.add("type|" + farming.farmType.name());
                        strings.add("level|" + farming.level);
                        String xyz = "f" + farming.location.toString().replaceAll(",", "_");
                        ModSim.saveSK2(ModSim.getSavesDataFolder() + "Farming" + File.separator + xyz + ".sk2", strings);
                    } catch (Exception var5) {
                    }
                }
            }
        }

    }
}
