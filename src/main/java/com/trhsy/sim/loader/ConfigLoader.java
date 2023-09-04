package com.trhsy.sim.loader;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;


/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: ConfigLoader
 * @Description: 配置文件加载
 * @date 2022/10/11 10:16
 */
public class ConfigLoader {
    /**高级配置功能，允许为配置变量提供各种类别**/
    public static ConfigLoader instance = new ConfigLoader();
    /**配置人口限制**/
    public static int configPopulationLimit = 100;
    /**配置木材面积**/
    public static int configLumberArea = 30;
    /**配置禁用光束效果**/
    public static boolean configDisableBeamEffect = false;
    /**配置NPC谈话**/
    public static boolean configFolkTalking = true;
    /**配置启用标记排成直线**/
    public static boolean configEnableMarkerAlignmentBeams = true;
    /**配置养殖箱和建筑箱是否用钻石**/
    public static boolean configUseExpensiveRecipies = false;
    /**配置物料提醒间隔**/
    public static int configMaterialReminderInterval = 3;
    /**配置偏移量**/
    public static int configHUDoffset = 0;
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

    public static Configuration configFile;

    public static ConfigCategory Gameplay;
    public static ConfigCategory Nameplay;
    private ConfigLoader() {
    }
    public static void load(FMLPreInitializationEvent event) {
        try {
            configFile = new Configuration(event.getSuggestedConfigurationFile(), "1.0.8 Beta");
            syncConfig();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("load出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @SubscribeEvent
    public void update(ConfigChangedEvent.OnConfigChangedEvent event) {
        try {
            if (event.getModID().equals("sim")) {
                syncConfig();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("update出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @Author fan
     * @Description //TODO 同步配置
     * @Date 22:48 2022/5/11
     * @Param []
     * @return boolean
     **/
    public static boolean syncConfig() {
        boolean changed = false;
        try {
            String simConfig="sim_game_play";
            Gameplay =configFile.getCategory(simConfig);
            List<String> propOrder = Lists.newArrayList();

            //禁用光束效果
            Property p = configFile.get(simConfig, "DisableBeamingEffect", configDisableBeamEffect);

            //这将启用或禁用光束效果（紫色粒子）-设置为true可将其禁用。
            String s=I18n.format("container.sim.config_loader0");
            p.setComment(s);
            configDisableBeamEffect = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "FolkTalking", configFolkTalking);
            //如果人们喋喋不休的谈话变得烦人，那么将其设置为false
            String s1=I18n.format("container.sim.config_loader1");
            p.setComment(s1);
            configFolkTalking = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "FolkTalkingEnglish", configFolkTalkingEnglish);
            //如果说英语的人变得烦人，请将其设置为false
            String s2=I18n.format("container.sim.config_loader2");
            p.setComment(s2);
            configFolkTalkingEnglish = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "LumbermillArea", configLumberArea);
            //伐木工人从起点开始寻找树木的块半径，不要设置得太高，否则每次他们扫描最近的树时，MC都会减慢速度，所以30到1000应该是可以的（1000是1公里）
            String s3=I18n.format("container.sim.config_loader3");
            p.setComment(s3);
            configLumberArea = p.getInt();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "PopulationLimit", configPopulationLimit);
            //如果你有一台旧电脑，限制人口增长超过这个数字
            String s4=I18n.format("container.sim.config_loader4");
            p.setComment(s4);
            configPopulationLimit = p.getInt();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "EnableMarkerAlignmentBeams", configEnableMarkerAlignmentBeams);
            //放置标记时，它会触发4个对齐梁，将其设置为false将关闭这些梁
            String s5=I18n.format("container.sim.config_loader5");
            p.setComment(s5);
            configEnableMarkerAlignmentBeams = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "UseExpensiveRecipes", configUseExpensiveRecipies);
            //如果你认为采矿/耕种的盒子太便宜/价格太高，那么将其设置为true，以使Recipes需要钻石工具而不是石头工具
            String s6=I18n.format("container.sim.config_loader6");
            p.setComment(s6);
            configUseExpensiveRecipies = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "MaterialReminderInterval", configMaterialReminderInterval);
            //当建筑商用完材料时，他们会每3分钟通知你一次，设置为0表示不再提醒。
            String s7=I18n.format("container.sim.config_loader7");
            p.setComment(s7);
            configMaterialReminderInterval = p.getInt();
            if (configMaterialReminderInterval <= 0) {
                configMaterialReminderInterval = 2000;
            }
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "HUDoffset", configHUDoffset);
            //这将定位HUD（屏幕顶部的人口和货币文本）-默认值为0，即顶部，值以像素为单位，因此设置320将显示距屏幕顶部320像素的内容。更改此选项以适应您的屏幕分辨率，并避免与其他文本冲突，设置为-10将在屏幕外显示。
            String s8=I18n.format("container.sim.config_loader8");
            p.setComment(s8);
            configHUDoffset = p.getInt();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "StopRain", configStopRain);
            //这只是一个个人模式：-）如果你也发现你的世界里一直在下雨，这会让你感到烦恼/导致延迟，那么把这个设置为真，你只会有短暂的淋浴
            String s9=I18n.format("container.sim.config_loader9");
            p.setComment(s9);
            configStopRain = p.getBoolean();
            propOrder.add(p.getName());

            Gameplay.setPropertyOrder(propOrder);

            String npcNames="npc_names";
            propOrder = Lists.newArrayList();
            Nameplay =configFile.getCategory(npcNames);

            p = configFile.get(npcNames, "MaleNames", I18n.format("container.sim.MaleNames"));
            p.setValue(I18n.format("container.sim.MaleNames"));
            String temp = p.getString();
            //这些是随机名字生成器使用的男性名字，请保持格式不变，否则会发生不好的事情。
            String s10=I18n.format("container.sim.config_loader10");
            p.setComment(s10);
            configMaleNames = temp.split(",");
            propOrder.add(p.getName());

            p = configFile.get(npcNames, "FemaleNames", I18n.format("container.sim.FemaleNames"));
            p.setValue(I18n.format("container.sim.FemaleNames"));
            temp = p.getString();
            //这些是随机名字生成器使用的女性名字，保持格式不变，否则会发生不好的事情。
            String s11=I18n.format("container.sim.config_loader11");
            p.setComment(s11);
            configFemaleNames = temp.split(",");
            propOrder.add(p.getName());

            p = configFile.get(npcNames, "LastNames", I18n.format("container.sim.LastNames"));
            p.setValue(I18n.format("container.sim.LastNames"));
            temp = p.getString();
            //这些是随机名称生成器使用的姓氏，请保持格式不变，否则会发生错误。
            String s12=I18n.format("container.sim.config_loader12");
            p.setComment(s12);
            configSurnames = temp.split(",");
            propOrder.add(p.getName());

            Nameplay.setPropertyOrder(propOrder);


            if (configFile.hasChanged()) {
                configFile.save();
                changed = true;
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("syncConfig出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return changed;
    }
}
