package com.trhsy.sim.common.core.entity;

import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 仓库快递员任务
 */
public class CourierTask implements Serializable {
    private static final long serialVersionUID = 1825349728277158061L;
    public String name = "";
    public String folkname = "";
    public V3 pickup ;
    public V3 dropoff;
    public boolean repeat = true;

    public CourierTask() {
    }

    public static V3 getCourierPoint(String name) {
        try {
            for (int x = 0; x < ModSimReloaded.theCourierPoints.size(); ++x) {
                V3 v = (V3) ModSimReloaded.theCourierPoints.get(x);
                if (v.name.contentEquals(name)) {
                    return v;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getCourierPoint出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    private static boolean alreadyGotTask(CourierTask theTask) {
        boolean got = false;
        try {
            for (int i = 0; i < ModSimReloaded.theCourierTasks.size(); i++) {
                CourierTask checkTask = (CourierTask) ModSimReloaded.theCourierTasks.get(i);
                if (checkTask.pickup.name.contentEquals(theTask.name)) {
                    got = true;
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("alreadyGotTask出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return got;
    }

    private static boolean alreadyGotPoint(V3 thePoint) {
        boolean got = false;
        try {
            for (int i = 0; i < ModSimReloaded.theCourierPoints.size(); i++) {
                V3 checkPoint = (V3) ModSimReloaded.theCourierPoints.get(i);
                if (checkPoint.isSameCoordsAs(thePoint, true, true)) {
                    got = true;
                    break;
                }
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("alreadyGotPoint出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return got;
    }

    public static void loadCourierTasksAndPoints() {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ModSimReloaded.log.info("***********************开始所有快递点***************");
                    ModSimReloaded.theCourierPoints.clear();
                    ModSimReloaded.theCourierTasks.clear();
                    //快递点
                    File courierPoints = new File(ModSimReloaded.getSavesDataFolder() + "CourierPoints" + File.separator);
                    courierPoints.mkdirs();
                    //快递任务
                    File courierTasks = new File(ModSimReloaded.getSavesDataFolder() + "CourierTasks" + File.separator);
                    courierTasks.mkdirs();

                    boolean useNewFormat = false;
                    File[] listFiles = courierPoints.listFiles();
                    int lengths = listFiles.length;

                    int i;
                    File f;
                    for (i = 0; i < lengths; i++) {
                        f = listFiles[i];
                        if (f.getName().endsWith(".sk2")) {
                            useNewFormat = true;
                            break;
                        }
                    }

                    if (useNewFormat) {
                        listFiles = courierPoints.listFiles();
                        lengths = listFiles.length;

                        List<String> strings;
                        Iterator iterator;
                        int m1;
                        String name;
                        String value;
                        for (m1 = 0; m1 < lengths; ++m1) {
                            f = listFiles[m1];
                            if (f.getName().endsWith(".sk2")) {
                                strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                                V3 v = null;
                                for (String line : strings) {
                                    if (line.contains("|")) {
                                        m1 = line.indexOf("|");
                                        name = line.substring(0, m1);
                                        value = line.substring(m1 + 1);
                                        if (name.contentEquals("location")) {
                                            String[] v1 = value.split(",");
                                            double x = Double.parseDouble(v1[0]);
                                            double y = Double.parseDouble(v1[1]);
                                            double z = Double.parseDouble(v1[2]);
                                            v = new V3(x, y, z);
                                        } else if (name.contentEquals("name")) {
                                            v.name = value;
                                        }
                                    }
                                }

                                if (v != null && !alreadyGotPoint(v)) {
                                    ModSimReloaded.theCourierPoints.add(v);
                                } else {
                                    f.delete();
                                }
                            }
                        }

                        listFiles = courierTasks.listFiles();
                        lengths = listFiles.length;

                        for (m1 = 0; m1 < lengths; ++m1) {
                            f = listFiles[m1];
                            if (f.getName().endsWith(".sk2")) {
                                strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                                CourierTask ct = new CourierTask();
                                for (String line : strings) {
                                    if (line.contains("|")) {
                                        m1 = line.indexOf("|");
                                        name = line.substring(0, m1);
                                        value = line.substring(m1 + 1);
                                        if (name.contentEquals("folk")) {
                                            ct.folkname = value;
                                        } else if (name.contentEquals("pickup")) {
                                            String[] v = value.split(",");
                                            double x = Double.parseDouble(v[0]);
                                            double y = Double.parseDouble(v[1]);
                                            double z = Double.parseDouble(v[2]);
                                            ct.pickup = new V3(x, y, z);
                                        } else if (name.contentEquals("dropoff")) {
                                            if (!value.contentEquals("null")) {
                                                String[] v = value.split(",");
                                                double x = Double.parseDouble(v[0]);
                                                double y = Double.parseDouble(v[1]);
                                                double z = Double.parseDouble(v[2]);
                                                ct.dropoff = new V3(x, y, z);
                                            }
                                        } else if (name.contentEquals("repeat")) {
                                            ct.repeat = Boolean.parseBoolean(value);
                                        } else if (name.contentEquals("name")) {
                                            ct.name = value;
                                        }
                                    }
                                }

                                if (!alreadyGotTask(ct) && ct != null && ct.dropoff != null && ct.pickup != null) {
                                    ModSimReloaded.theCourierTasks.add(ct);
                                } else {
                                    f.delete();
                                }
                            }
                        }
                    } else {
                        listFiles = courierPoints.listFiles();
                        lengths = listFiles.length;

                        for (i = 0; i < lengths; i++) {
                            f = listFiles[i];
                            if (f.getName().endsWith(".suk")) {
                                V3 point = (V3) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
                                if (!alreadyGotPoint(point)) {
                                    ModSimReloaded.theCourierPoints.add(point);
                                } else {
                                    f.delete();
                                }
                            }
                        }

                        listFiles = courierTasks.listFiles();
                        lengths = listFiles.length;

                        for (i = 0; i < lengths; i++) {
                            f = listFiles[i];
                            if (f.getName().endsWith(".suk")) {
                                CourierTask task = (CourierTask) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
                                if (!alreadyGotTask(task)) {
                                    ModSimReloaded.theCourierTasks.add(task);
                                } else {
                                    f.delete();
                                }
                            }
                        }
                    }
                    ModSimReloaded.log.info("***********************加载所有快递点完成***************");
                }},"loadCourierTasksAndPoints_sim");
            thread.start();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("loadCourierTasksAndPoints出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static void saveCourierTasksAndPoints() {
        try {
            String names = "";
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.SERVER) {
                int mofo;
                CopyOnWriteArrayList strings;
                for (mofo = 0; mofo < ModSimReloaded.theCourierPoints.size(); ++mofo) {
                    strings = new CopyOnWriteArrayList();
                    V3 point = (V3) ModSimReloaded.theCourierPoints.get(mofo);
                    if (point != null) {
                        List<IInventory> chests = Job.inventoriesFindClosest(point, 5);
                        String fn = "cp" + point.xCoord + "_" + point.yCoord + "_" + point.zCoord + "_D" + point.theDimension;
                        if (chests.size() > 0) {
                            if (!names.contains(point.name)) {
                                names = names + " " + point.name;
                                strings.add("location|" + point.toString());
                                strings.add("name|" + point.name);

                                ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "CourierPoints" + File.separator + fn + ".sk2", strings);
                            }
                        } else {
                            try {
                                File fi = new File(ModSimReloaded.getSavesDataFolder() + "CourierPoints" + File.separator + fn + ".sk2");
                                fi.delete();
                            } catch (Exception e) {
                                //var9.printStackTrace();
                            }
                        }
                    }
                }

                for (mofo = 0; mofo < ModSimReloaded.theCourierTasks.size(); ++mofo) {
                    strings = new CopyOnWriteArrayList();
                    CourierTask task = (CourierTask) ModSimReloaded.theCourierTasks.get(mofo);
                    String fn = "ct" + mofo + task.folkname.replace(" ", "");
                    boolean okToSave = true;
                    strings.add("folk|" + task.folkname);
                    strings.add("pickup|" + task.pickup.toString());
                    if (task.dropoff == null) {
                        strings.add("dropoff|null");
                    } else {
                        try {
                            strings.add("dropoff|" + task.dropoff.toString());
                        } catch (Exception e) {
                            okToSave = false;
                        }
                    }

                    strings.add("repeat|" + task.repeat);
                    strings.add("name|" + task.name);
                    if (okToSave) {
                        ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "CourierTasks" + File.separator + fn + ".sk2", strings);
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("saveCourierTasksAndPoints出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }
}
