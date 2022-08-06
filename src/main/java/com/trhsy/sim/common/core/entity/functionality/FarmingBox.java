package com.trhsy.sim.common.core.entity.functionality;

import com.trhsy.sim.common.core.entity.enums.FarmType;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
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
        try {
            this.farmType = FarmType.WHEAT;
            this.level = 1;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("养殖箱FarmingBox出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public FarmingBox(V3 inxyz) {
        try {
            this.farmType = FarmType.WHEAT;
            this.level = 1;
            this.location = inxyz;
            if (this.level == 0) {
                this.level = 1;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("养殖箱FarmingBox出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public FarmingBox(V3 inxyz, V3 m1xyz, V3 m2xyz, V3 m3xyz) {
        try {
            this.farmType = FarmType.WHEAT;
            this.level = 1;
            this.location = inxyz;
            this.marker1XYZ = m1xyz;
            this.marker2XYZ = m2xyz;
            this.marker3XYZ = m3xyz;
            if (this.level == 0) {
                this.level = 1;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("养殖箱FarmingBox出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
        } catch (Exception e) {
            return new V3(0, 0, 0, 0);
        }
    }

    /**
     * 获得大小 宽度
     *
     * @return
     */
    public int getSizeWidth() {
        boolean var1 = false;

        int ltr;
        try {
            V3 m1 = this.getMarkerVector(1);
            V3 m2 = this.getMarkerVector(2);
            V3 m3 = this.getMarkerVector(3);
            if (m1.xCoord == m2.xCoord) {
                ltr = (int) (Math.abs(m2.zCoord - m1.zCoord) + 1.0);
            } else {
                ltr = (int) (Math.abs(m2.xCoord - m1.xCoord) + 1.0);
            }
        } catch (Exception e) {
            return 5;
        }

        return Math.abs(ltr);
    }

    /**
     * 获得大小长度
     *
     * @return
     */
    public int getSizeLength() {
        boolean var1 = false;

        int ftb;
        try {
            V3 m1 = this.getMarkerVector(1);
            V3 m2 = this.getMarkerVector(2);
            V3 m3 = this.getMarkerVector(3);
            if (m1.xCoord == m3.xCoord) {
                ftb = (int) (Math.abs(m3.zCoord - m1.zCoord) + 1.0);
            } else {
                ftb = (int) (Math.abs(m3.xCoord - m1.xCoord) + 1.0);
            }
        } catch (Exception e) {
            return 5;
        }

        return Math.abs(ftb);
    }

    public List<V3> getSoilBlockPoints() {
        List<V3> ret = new CopyOnWriteArrayList();
        try {
            V3 m1 = this.getMarkerVector(1);
            V3 m2 = this.getMarkerVector(2);
            V3 m3 = this.getMarkerVector(3);
            V3 c = m1.clone();
            int length = this.getSizeLength();
            if (length == 1) {
                ModSimReloaded.log.warn("FarmingBox: 无法使用5x5默认值确定农场大小");
            }

            for (int o = 0; o <= length; ++o) {
                for (int i = 0; i <= this.getSizeWidth(); i++) {
                    ret.add(c.clone());
                    if (m2.xCoord > m1.xCoord) {
                        c.addVector(m1.xCoord+i,c.yCoord,c.zCoord);
                        //c.xCoord = m1.xCoord + (double) i;
                    } else if (m2.xCoord < m1.xCoord) {
                        //c.xCoord = m1.xCoord - (double) i;
                        c.addVector(m1.xCoord-i,c.yCoord,c.zCoord);
                    } else if (m2.zCoord > m1.zCoord) {
                        //c.zCoord = m1.zCoord + (double) i;
                        c.addVector(c.xCoord,c.yCoord,m1.zCoord+i);
                    } else if (m2.zCoord < m1.zCoord) {
                        //c.zCoord = m1.zCoord - (double) i;
                        c.addVector(c.xCoord,c.yCoord,m1.zCoord-i);
                    }
                }

                if (m3.xCoord > m1.xCoord) {
                    //c.xCoord = m1.xCoord + (double) o;
                    c.addVector(m1.xCoord+o,c.yCoord,c.zCoord);
                } else if (m3.xCoord < m1.xCoord) {
                    //c.xCoord = m1.xCoord - (double) o;
                    c.addVector(m1.xCoord-o,c.yCoord,c.zCoord);
                } else if (m3.zCoord > m1.zCoord) {
                    //c.zCoord = m1.zCoord + (double) o;
                    c.addVector(c.xCoord,c.yCoord,m1.zCoord+o);
                } else if (m3.zCoord < m1.zCoord) {
                    //c.zCoord = m1.zCoord - (double) o;
                    c.addVector(c.xCoord,c.yCoord,m1.zCoord-o);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("养殖箱getSoilBlockPoints出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


        return ret;
    }

    /**
     * 获取周边节点
     *
     * @return
     */
    public List<V3> getPerimeterPoints() {
        CopyOnWriteArrayList ret = new CopyOnWriteArrayList();

        try {
            V3 m1 = this.getMarkerVector(1);
            V3 m2 = this.getMarkerVector(2);
            V3 m3 = this.getMarkerVector(3);
            V3 b = this.getLocation();
            V3 c = b.clone();

            for (int i = 0; i <= this.getSizeWidth() + 2; i++) {
                if (m2.xCoord - b.xCoord > 1.0) {
                    //c.xCoord = c.xCoord + 1.0;
                    c.addVector(c.xCoord+1,c.yCoord,c.zCoord);
                } else if (m2.xCoord - b.xCoord < -1.0) {
                    //c.xCoord = c.xCoord - 1.0;
                    c.addVector(c.xCoord-1,c.yCoord,c.zCoord);
                } else if (m2.zCoord - b.zCoord > 1.0) {
                    //c.zCoord = c.zCoord + 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord+1);
                } else if (m2.zCoord - b.zCoord < -1.0) {
                    //c.zCoord = c.zCoord - 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord-1);
                }
                ret.add(c.clone());
            }
            for (int i = 0; i <= this.getSizeLength() + 2; i++) {
                if (m3.xCoord - b.xCoord > 1.0) {
                    //c.xCoord = c.xCoord + 1.0;
                    c.addVector(c.xCoord+1,c.yCoord,c.zCoord);
                } else if (m3.xCoord - b.xCoord < -1.0) {
                    //c.xCoord = c.xCoord - 1.0;
                    c.addVector(c.xCoord-1,c.yCoord,c.zCoord);
                } else if (m3.zCoord - b.zCoord > 1.0) {
                    //c.zCoord = c.zCoord + 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord+1);
                } else if (m3.zCoord - b.zCoord < -1.0) {
                    //c.zCoord = c.zCoord - 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord-1);
                }
                ret.add(c.clone());
            }
            for (int i = 0; i <= this.getSizeWidth() + 2; i++) {
                if (m2.xCoord - b.xCoord + 1 > 1.0) {
                    //c.xCoord = c.xCoord - 1.0;
                    c.addVector(c.xCoord-1,c.yCoord,c.zCoord);
                } else if (m2.xCoord - b.xCoord + 1 < -1.0) {
                    //c.xCoord = c.xCoord + 1.0;
                    c.addVector(c.xCoord+1,c.yCoord,c.zCoord);
                } else if (m2.zCoord - b.zCoord > 1.0) {
                    //c.zCoord = c.zCoord - 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord-1);
                } else if (m2.zCoord - b.zCoord < -1.0) {
                    //c.zCoord = c.zCoord + 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord+1);
                }
                ret.add(c.clone());
            }
            for (int i = 0; i <= this.getSizeLength() + 2; i++) {
                if (m3.xCoord - b.xCoord > 1.0) {
                    //c.xCoord = c.xCoord - 1.0;
                    c.addVector(c.xCoord-1,c.yCoord,c.zCoord);
                } else if (m3.xCoord - b.xCoord < -1.0) {
                    //c.xCoord = c.xCoord + 1.0;
                    c.addVector(c.xCoord+1,c.yCoord,c.zCoord);
                } else if (m3.zCoord - b.zCoord > 1.0) {
                    //c.zCoord = c.zCoord - 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord-1);
                } else if (m3.zCoord - b.zCoord < -1.0) {
                    //c.zCoord = c.zCoord + 1.0;
                    c.addVector(c.xCoord,c.yCoord,c.zCoord+1);
                }
                ret.add(c.clone());
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("获取周边节点发生错误：" + e.getMessage()+"行数："+element.getLineNumber());
            //var11.printStackTrace();
        }
        return ret;
    }

    public V3 getLocation() {
        return this.location;
    }

    public static FarmingBox getFarmingBlockByBoxXYZ(V3 xyz) {
        FarmingBox ret = null;
        try {
            if (ModSimReloaded.theFarmingBoxes.size() == 0) {
                loadFarmingBoxes();
            }

            for (int x = 0; x < ModSimReloaded.theFarmingBoxes.size(); ++x) {
                FarmingBox block = (FarmingBox) ModSimReloaded.theFarmingBoxes.get(x);
                if (block.location.isSameCoordsAs(xyz, true, true)) {
                    ret = block;
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("养殖箱getFarmingBlockByBoxXYZ出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    public static void loadFarmingBoxes() {
        try {
            File farmFiles = new File(ModSimReloaded.getSavesDataFolder() + "Farming" + File.separator);
            farmFiles.mkdirs();
            boolean useNewFormat = false;
            File[] arrFiles = farmFiles.listFiles();
            File f;
            for (int i = 0; i < arrFiles.length; i++) {
                f = arrFiles[i];
                if (f.getName().endsWith(".sk2")) {
                    useNewFormat = true;
                    break;
                }
            }

            WorldServer theWorld;
            Block id;
            if (useNewFormat) {
                ModSimReloaded.theFarmingBoxes.clear();
                for (int i = 0; i < arrFiles.length; i++) {
                    f = arrFiles[i];
                    if (f.getName().endsWith(".sk2")) {
                        List<String> strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                        FarmingBox box = new FarmingBox();
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
                                } else if (name.contentEquals("type")) {
                                    box.farmType = FarmType.valueOf(value);
                                } else if (name.contentEquals("level")) {
                                    box.level = Integer.parseInt(value);
                                }
                            }
                        }

                        theWorld = MinecraftServer.getServer().worldServerForDimension(box.location.theDimension);
                        if (theWorld != null) {
                            id = theWorld.getBlockState(new BlockPos(box.location.xCoord, box.location.yCoord, box.location.zCoord)).getBlock();
                            if (id == BlockLoader.blockFarmingBox) {
                                ModSimReloaded.theFarmingBoxes.add(box);
                            } else {
                                f.delete();
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < arrFiles.length; i++) {
                    f = arrFiles[i];
                    if (f.getName().endsWith(".suk")) {
                        FarmingBox farming = (FarmingBox) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
                        if (farming != null) {
                            V3 xyz = farming.location;
                            theWorld = MinecraftServer.getServer().worldServerForDimension(xyz.theDimension);
                            if (theWorld == null) {
                                f.delete();
                            } else {
                                try {
                                    id = theWorld.getBlockState(new BlockPos(xyz.xCoord, xyz.yCoord, xyz.zCoord)).getBlock();
                                    //id = theWorld.getBlock(xyz.xCoord, xyz.yCoord, xyz.zCoord);
                                    if (id == BlockLoader.blockFarmingBox) {
                                        ModSimReloaded.theFarmingBoxes.add(farming);
                                    } else {
                                        f.delete();
                                    }
                                } catch (Exception e) {
                                    //var14.printStackTrace();
                                }
                            }
                        } else {
                            f.delete();
                            String s = I18n.format("container.sim.farming_box_boxes");
                            ModSimReloaded.sendChat(s);
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("养殖箱loadFarmingBoxes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static void saveFarmingBoxes() {
        try {
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.SERVER) {
                List<String> strings = new CopyOnWriteArrayList();

                for (int b = 0; b < ModSimReloaded.theFarmingBoxes.size(); ++b) {
                    FarmingBox farming = (FarmingBox) ModSimReloaded.theFarmingBoxes.get(b);
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
                            ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Farming" + File.separator + xyz + ".sk2", strings);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("养殖箱saveFarmingBoxes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
