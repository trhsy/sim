package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.jobs.Job;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.inventory.IInventory;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @ClassName CourierTask
 * @Description todo 通信业务
 * @Author Tian
 * @Date 2022/1/2319:50
 **/
public class CourierTask implements Serializable {
    private static final long serialVersionUID = 1825349728277158061L;
    public String name = "";
    public String folkname = "";
    public V3 pickup = new V3();
    public V3 dropoff = new V3();
    public boolean repeat = true;

    public CourierTask() {
    }

    public static V3 getCourierPoint(String name) {
        new V3();

        for(int x = 0; x < ModSim.theCourierPoints.size(); ++x) {
            V3 v = (V3) ModSim.theCourierPoints.get(x);
            if (v.name.contentEquals(name)) {
                return v;
            }
        }

        return null;
    }

    private static boolean alreadyGotTask(CourierTask theTask) {
        boolean got = false;

        for(int i = 0; i < ModSim.theCourierTasks.size(); ++i) {
            CourierTask checkTask = (CourierTask) ModSim.theCourierTasks.get(i);

            try {
                if (checkTask.pickup.name.contentEquals(theTask.name)) {
                    got = true;
                    break;
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return got;
    }

    private static boolean alreadyGotPoint(V3 thePoint) {
        boolean got = false;

        for(int i = 0; i < ModSim.theCourierPoints.size(); ++i) {
            V3 checkPoint = (V3) ModSim.theCourierPoints.get(i);
            if (checkPoint.isSameCoordsAs(thePoint, true, true)) {
                got = true;
                break;
            }
        }

        return got;
    }

    public static void loadCourierTasksAndPoints() {
        ModSim.theCourierPoints.clear();
        ModSim.theCourierTasks.clear();
        File courierPoints = new File(ModSim.getSavesDataFolder() + "CourierPoints" + File.separator);
        courierPoints.mkdirs();
        File courierTasks = new File(ModSim.getSavesDataFolder() + "CourierTasks" + File.separator);
        courierTasks.mkdirs();

        boolean useNewFormat = false;
        File[] listFiles = courierPoints.listFiles();
        int lengths = listFiles.length;

        int i;
        File f;
        for(i = 0; i < lengths; ++i) {
            f = listFiles[i];
            if (f.getName().endsWith(".sk2")) {
                useNewFormat = true;
                break;
            }
        }

        if (useNewFormat) {
            listFiles = courierPoints.listFiles();
            lengths = listFiles.length;

            ArrayList strings;
            Iterator iterator;
            String line;
            int m1;
            String name;
            String value;
            for(m1 = 0; m1 < lengths; ++m1) {
                f = listFiles[m1];
                if (f.getName().endsWith(".sk2")) {
                    strings = ModSim.loadSK2(f.getAbsoluteFile().toString());
                    V3 v = new V3();
                    iterator = strings.iterator();

                    while(iterator.hasNext()) {
                        line = (String)iterator.next();
                        if (line.contains("|")) {
                            m1 = line.indexOf("|");
                            name = line.substring(0, m1);
                            value = line.substring(m1 + 1);
                            if (name.contentEquals("location")) {
                                v = new V3(value);
                            } else if (name.contentEquals("name")) {
                                v.name = value;
                            }
                        }
                    }

                    if (v != null && !alreadyGotPoint(v)) {
                        ModSim.theCourierPoints.add(v);
                    } else {
                        f.delete();
                    }
                }
            }

            listFiles = courierTasks.listFiles();
            lengths = listFiles.length;

            for(m1 = 0; m1 < lengths; ++m1) {
                f = listFiles[m1];
                if (f.getName().endsWith(".sk2")) {
                    strings = ModSim.loadSK2(f.getAbsoluteFile().toString());
                    CourierTask ct = new CourierTask();
                    iterator = strings.iterator();

                    while(iterator.hasNext()) {
                        line = (String)iterator.next();
                        if (line.contains("|")) {
                            m1 = line.indexOf("|");
                            name = line.substring(0, m1);
                            value = line.substring(m1 + 1);
                            if (name.contentEquals("folk")) {
                                ct.folkname = value;
                            } else if (name.contentEquals("pickup")) {
                                ct.pickup = new V3(value);
                            } else if (name.contentEquals("dropoff")) {
                                if (!value.contentEquals("null")) {
                                    ct.dropoff = new V3(value);
                                }
                            } else if (name.contentEquals("repeat")) {
                                ct.repeat = Boolean.parseBoolean(value);
                            } else if (name.contentEquals("name")) {
                                ct.name = value;
                            }
                        }
                    }

                    if (!alreadyGotTask(ct) && ct != null && ct.dropoff != null && ct.pickup != null) {
                        ModSim.theCourierTasks.add(ct);
                    } else {
                        f.delete();
                    }
                }
            }
        } else {
            listFiles = courierPoints.listFiles();
            lengths = listFiles.length;

            for(i = 0; i < lengths; ++i) {
                f = listFiles[i];
                if (f.getName().endsWith(".suk")) {
                    V3 point = (V3) ModSim.proxy.loadObject(f.getAbsoluteFile().toString());
                    if (!alreadyGotPoint(point)) {
                        ModSim.theCourierPoints.add(point);
                    } else {
                        f.delete();
                    }
                }
            }

            listFiles = courierTasks.listFiles();
            lengths = listFiles.length;

            for(i = 0; i < lengths; ++i) {
                f = listFiles[i];
                if (f.getName().endsWith(".suk")) {
                    CourierTask task = (CourierTask) ModSim.proxy.loadObject(f.getAbsoluteFile().toString());
                    if (!alreadyGotTask(task)) {
                        ModSim.theCourierTasks.add(task);
                    } else {
                        f.delete();
                    }
                }
            }
        }

    }

    public static void saveCourierTasksAndPoints() {
        String names = "";
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER) {
            int mofo;
            ArrayList strings;
            for(mofo = 0; mofo < ModSim.theCourierPoints.size(); ++mofo) {
                strings = new ArrayList();
                V3 point = (V3) ModSim.theCourierPoints.get(mofo);
                if (point != null) {
                    ArrayList<IInventory> chests = Job.inventoriesFindClosest(point, 5);
                    String fn = "cp" + point.x.intValue() + "_" + point.y.intValue() + "_" + point.z.intValue() + "_D" + point.theDimension;
                    if (chests.size() > 0) {
                        if (!names.contains(point.name)) {
                            names = names + " " + point.name;
                            strings.add("location|" + point.toString());
                            strings.add("name|" + point.name);
                            ModSim.saveSK2(ModSim.getSavesDataFolder() + "CourierPoints" + File.separator + fn + ".sk2", strings);
                        }
                    } else {
                        try {
                            File fi = new File(ModSim.getSavesDataFolder() + "CourierPoints" + File.separator + fn + ".sk2");
                            fi.delete();
                        } catch (Exception var9) {
                            var9.printStackTrace();
                        }
                    }
                }
            }

            for(mofo = 0; mofo < ModSim.theCourierTasks.size(); ++mofo) {
                strings = new ArrayList();
                CourierTask task = (CourierTask) ModSim.theCourierTasks.get(mofo);
                String fn = "ct" + mofo + task.folkname.replace(" ", "");
                boolean okToSave = true;
                strings.add("folk|" + task.folkname);
                strings.add("pickup|" + task.pickup.toString());
                if (task.dropoff == null) {
                    strings.add("dropoff|null");
                } else {
                    try {
                        strings.add("dropoff|" + task.dropoff.toString());
                    } catch (Exception var8) {
                        okToSave = false;
                    }
                }

                strings.add("repeat|" + task.repeat);
                strings.add("name|" + task.name);
                if (okToSave) {
                    ModSim.saveSK2(ModSim.getSavesDataFolder() + "CourierTasks" + File.separator + fn + ".sk2", strings);
                }
            }
        }

    }
}
