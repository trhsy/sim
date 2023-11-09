package com.trhsy.sim.loader;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: ConfigLoader
 * @Description: 配置文件加载
 * @date 2023/11/09 下午 5:59
 */
public class ConfigLoader {
    /**高级配置功能，允许为配置变量提供各种类别**/
    public static ConfigLoader instance = new ConfigLoader();

    /**配置人口限制 0为不限制**/
    public static int configPopulationLimit = 0;

    /**配置木材面积**/
    public static int configLumberArea = 60;

    /**配置NPC谈话**/
    public static boolean configFolkTalking = true;

    /**配置养殖箱和建筑箱是否用钻石**/
    public static boolean configUseExpensiveRecipies = false;

    /**配置物料提醒间隔**/
    public static int configMaterialReminderInterval = 3;

    /**停止降雨**/
    public static boolean configStopRain = true;
    /**配置NPC说英文**/
    public static boolean configFolkTalkingEnglish = true;
    /**配置男性姓名**/
    public static String[] configMaleNames;
    /**配置女性名称**/
    public static String[] configFemaleNames;
    /**配置姓氏**/
    public static String[] configSurnames;
}
