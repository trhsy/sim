package com.trhsy.sim.common.loader;

import com.google.common.collect.Lists;
import com.trhsy.sim.common.config.SimConfig;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName ConfigLoader
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1122:25
 **/
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
    //public static ConfigCategory Modules;
    public static Configuration configFile;
    public static ConfigCategory Gameplay;
    public static ConfigCategory Nameplay;
    public static Logger logger;
    private ConfigLoader() {
    }
    public static void load(FMLPreInitializationEvent event) {
        try {
            configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.1", false);
            MinecraftForge.EVENT_BUS.register(instance);
            syncConfig();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("load出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @SubscribeEvent
    public void update(ConfigChangedEvent.OnConfigChangedEvent event) {
        try {
            if (event.modID.equals("sim")) {
                syncConfig();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("update出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            p.comment = "This enables or disables the beaming effect (purple particles) - Set to true to turn them off.";
            configDisableBeamEffect = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "FolkTalking", configFolkTalking);
            //如果人们喋喋不休的谈话变得烦人，那么将其设置为false
            p.comment = "If the folks BLARG talking gets annoying, set this to false";
            configFolkTalking = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "FolkTalkingEnglish", configFolkTalkingEnglish);
            //如果说英语的人变得烦人，请将其设置为false
            p.comment = "If the folks ENGLISH talking gets annoying, set this to false";
            configFolkTalkingEnglish = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "LumbermillArea", configLumberArea);
            //伐木工人从起点开始寻找树木的块半径，不要设置得太高，否则每次他们扫描最近的树时，MC都会减慢速度，所以30到1000应该是可以的（1000是1公里）
            p.comment = "The radius in blocks that the lumberjack will look for trees from the starting point, don't set this too high, otherwise it will slow down MC every time they scan for the nearest tree, so 30 to 1000 should be ok (1000 is 1 Kilometre)";
            configLumberArea = p.getInt();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "PopulationLimit", configPopulationLimit);
            //如果你有一台旧电脑，限制人口增长超过这个数字
            p.comment = "Limit the population from growing beyond this number if you have an older computer";
            configPopulationLimit = p.getInt();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "EnableMarkerAlignmentBeams", configEnableMarkerAlignmentBeams);
            //放置标记时，它会触发4个对齐梁，将其设置为false将关闭这些梁
            p.comment = "When placing a marker it fires out 4 alignment beams, setting this to false will turn those beams off";
            configEnableMarkerAlignmentBeams = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "UseExpensiveRecipes", configUseExpensiveRecipies);
            //如果你认为采矿/耕种的盒子太便宜/价格太高，那么将其设置为true，以使Recipes需要钻石工具而不是石头工具
            p.comment = "If you think the mining/farming boxes are too cheap/overpowered, set this to true to make the recipies require diamond tools instead of stone tools";
            configUseExpensiveRecipies = p.getBoolean();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "MaterialReminderInterval", configMaterialReminderInterval);
            //当建筑商用完材料时，他们会每3分钟通知你一次，设置为0表示不再提醒。
            p.comment = "When a builder runs out of materials, they will let you know about it every 3 minutes, set to 0 for no further reminders.";
            configMaterialReminderInterval = p.getInt();
            if (configMaterialReminderInterval <= 0) {
                configMaterialReminderInterval = 2000;
            }
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "HUDoffset", configHUDoffset);
            //这将定位HUD（屏幕顶部的人口和货币文本）-默认值为0，即顶部，值以像素为单位，因此设置320将显示距屏幕顶部320像素的内容。更改此选项以适应您的屏幕分辨率，并避免与其他文本冲突，设置为-10将在屏幕外显示。
            p.comment = "This positions the HUD (population and money text at the top of the screen) - default is 0, which is the top, value is in pixels, so setting 320 will display it 320 pixels from the top of the screen. Alter this to suit your screen resolution and avoid clashing with other text, setting to minus 10 will display it offscreen.";
            configHUDoffset = p.getInt();
            propOrder.add(p.getName());

            p = configFile.get(simConfig, "StopRain", configStopRain);
            //这只是一个个人模式：-）如果你也发现你的世界里一直在下雨，这会让你感到烦恼/导致延迟，那么把这个设置为真，你只会有短暂的淋浴
            p.comment = "This is just a personal mod :-) If you too find it rains ALL THE F***ING TIME in your world and it annoys you/causes lag, set this to true and you'll only have brief showers instead";
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
            p.comment = "These are the male first names used by the random name generator, keep the format the same or bad things will happen.";
            configMaleNames = temp.split(",");
            propOrder.add(p.getName());

            p = configFile.get(npcNames, "FemaleNames", I18n.format("container.sim.FemaleNames"));
            p.setValue(I18n.format("container.sim.FemaleNames"));
            temp = p.getString();
            //这些是随机名字生成器使用的女性名字，保持格式不变，否则会发生不好的事情。
            p.comment = "These are the female first names used by the random name generator, keep the format the same or bad things will happen.";
            configFemaleNames = temp.split(",");
            propOrder.add(p.getName());

            p = configFile.get(npcNames, "LastNames", I18n.format("container.sim.LastNames"));
            p.setValue(I18n.format("container.sim.LastNames"));
            temp = p.getString();
            //这些是随机名称生成器使用的姓氏，请保持格式不变，否则会发生错误。
            p.comment = "These are the last names used by the random name generator, keep the format the same or bad things will happen.";
            configSurnames = temp.split(",");
            propOrder.add(p.getName());

            Nameplay.setPropertyOrder(propOrder);


            if (configFile.hasChanged()) {
                configFile.save();
                changed = true;
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("syncConfig出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return changed;
    }
}
