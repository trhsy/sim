package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.loader.ModSimReloaded;

import java.io.*;

public class job implements Serializable {
    //显示名称
    public static String displayName;
    //工作建筑名称
    public static String jobBuildingName;
    //工作建筑
    public static Building jobBuilding;
    //workOvernight
    public static boolean workOvernight = false;
    //工作日
    public static boolean workDay = true;
    //忽略怀孕
    public static boolean ignorePregnancy = false;

    public static void loadJobs() {
        File simfolder = new File(ModSimReloaded.getSimukraftFolder() + "/jobs/");
        try {
            for (File g : simfolder.listFiles()) {
				
	        
			/*File f = new File(ModSimReloaded.getSimukraftFolder() + "/jobs/" + displayName + ".txt");
			if (!f.exists())
			{
				return;
			}*/

                FileInputStream fstream = new FileInputStream(ModSimReloaded.getSimukraftFolder() + "/jobs/" + g.getName());
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                for (int i = 0; i < g.length(); i++) {
                    String strLine = br.readLine().toString().trim();
                    String[] jobFileLine = strLine.split("=");

                    /*工作名称 (name)*/
                    if (jobFileLine[0] == "Name") {
                        displayName = jobFileLine[1];
                    }

                    /*工作建筑 (building)*/
                    if (jobFileLine[0] == "Building") {
                        jobBuildingName = jobFileLine[1];
                        jobBuilding = Building.getBuildingBySearch(jobBuildingName);
                    }

                    /*NPC是否可以夜晚工作? (WorkAtNight)*/
                    if (jobFileLine[0] == "WorkAtNight") {
                        if (Byte.parseByte(jobFileLine[1]) == 0) {
                            workOvernight = false;
                        } else if (Byte.parseByte(jobFileLine[1]) == 1) {
                            workOvernight = true;
                        } else {
                            ModSimReloaded.log.info("夜间工作不是1或0，默认设置为0");
                            workOvernight = false;
                        }
                    }

                    /*民间应该在白天工作吗？（白天工作）*/
                    if (jobFileLine[0] == "WorkAtNight") {
                        if (Byte.parseByte(jobFileLine[1]) == 0) {
                            workDay = false;
                        } else if (Byte.parseByte(jobFileLine[1]) == 1) {
                            workDay = true;
                        } else if (Byte.parseByte(jobFileLine[1]) == 0 && workOvernight == false) {
                            ModSimReloaded.log.info( "“白天工作”为0，“夜间工作”为0，将“白天工作”设置为1以防止出现问题");
                            workDay = true;
                        } else {
                            ModSimReloaded.log.info( "工作日不是1或0，默认设置为1");
                            workDay = true;
                        }
                    }


                    /*即使怀孕了，人们也应该工作吗？（忽略妊娠）*/
                    if (jobFileLine[0] == "IgnorePregnancy") {
                        if (Byte.parseByte(jobFileLine[1]) == 0) {
                            ignorePregnancy = false;
                        } else if (Byte.parseByte(jobFileLine[1]) == 1) {
                            ignorePregnancy = true;
                        } else {
                            ModSimReloaded.log.info( "“忽略怀孕”不是1或0，默认设置为0");
                            ignorePregnancy = false;
                        }
                    }


                }

                br.close();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("加载工作失败："+e.getMessage()+"行数："+element.getLineNumber());
            //e.printStackTrace();
        }

    }
}
