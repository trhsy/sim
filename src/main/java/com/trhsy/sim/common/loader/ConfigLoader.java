package com.trhsy.sim.common.loader;/**
 * @author trhsy
 * @date 2022/2/8 0008
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;

/**
 * ========================================
 *
 * @ClassName ConfigLoader
 * @Description todo 配置文件管理类
 * @Author Administrator
 * @Date 2022/2/8 0008下午 5:36
 * ========================================
 **/
public class ConfigLoader {
    private static Configuration config;

    private static Logger logger;

    public ConfigLoader(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        //为参数中给定的文件创建配置文件。
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        load();
    }

    public static void load() {
        logger.info("开始加载配置... ");
        try {
            //自定义类别注释
            config.addCustomCategoryComment("Blocks", "Blocks");

            //禁用光束效果
            Property p = config.get("Settings", "DisableBeamingEffect", false);

            ModSim.configDisableBeamEffect = p.getBoolean(false);
            //这将启用或禁用光束效果（紫色粒子）-设置为true可将其禁用。
            p.comment = "This enables or disables the beaming effect (purple particles) - Set to true to turn them off.";
            p = config.get("Settings", "FolkTalking", true);

            ModSim.configFolkTalking = p.getBoolean(true);
            //如果人们喋喋不休的谈话变得烦人，那么将其设置为false
            p.comment = "If the folks BLARG talking gets annoying, set this to false";

            p = config.get("Settings", "FolkTalkingEnglish", true);

            ModSim.configFolkTalkingEnglish = p.getBoolean(true);

            //如果说英语的人变得烦人，请将其设置为false
            p.comment = "If the folks ENGLISH talking gets annoying, set this to false";

            p = config.get("Settings", "LumbermillArea", 40);
            ModSim.configLumberArea = p.getInt();


            //伐木工人从起点开始寻找树木的块半径，不要设置得太高，否则每次他们扫描最近的树时，MC都会减慢速度，所以30到1000应该是可以的（1000是1公里）
            p.comment = "The radius in blocks that the lumberjack will look for trees from the starting point, don't set this too high, otherwise it will slow down MC every time they scan for the nearest tree, so 30 to 1000 should be ok (1000 is 1 Kilometre)";
            p = config.get("Settings", "PopulationLimit", 200);
            ModSim.configPopulationLimit = p.getInt();


            //如果你有一台旧电脑，限制人口增长超过这个数字
            p.comment = "Limit the population from growing beyond this number if you have an older computer";
            p = config.get("Settings", "EnableMarkerAlignmentBeams", false);

            ModSim.configEnableMarkerAlignmentBeams = p.getBoolean(true);
            //放置标记时，它会触发4个对齐梁，将其设置为false将关闭这些梁
            p.comment = "When placing a marker it fires out 4 alignment beams, setting this to false will turn those beams off";
            p = config.get("Settings", "UseExpensiveRecipes", true);

            ModSim.configUseExpensiveRecipies = p.getBoolean(false);
            //如果你认为采矿/耕种的盒子太便宜/价格太高，那么将其设置为true，以使Recipes需要钻石工具而不是石头工具
            p.comment = "If you think the mining/farming boxes are too cheap/overpowered, set this to true to make the recipies require diamond tools instead of stone tools";
            p = config.get("Settings", "MaterialReminderInterval", 3);
            ModSim.configMaterialReminderInterval = p.getInt(3);


            //当建筑商用完材料时，他们会每3分钟通知你一次，设置为0表示不再提醒。
            p.comment = "When a builder runs out of materials, they will let you know about it every 3 minutes, set to 0 for no further reminders.";
            if (ModSim.configMaterialReminderInterval <= 0) {
                ModSim.configMaterialReminderInterval = 2000;
            }
            p = config.get("Settings", "HUDoffset", 0);
            ModSim.configHUDoffset = p.getInt(0);


            //这将定位HUD（屏幕顶部的人口和货币文本）-默认值为0，即顶部，值以像素为单位，因此设置320将显示距屏幕顶部320像素的内容。更改此选项以适应您的屏幕分辨率，并避免与其他文本冲突，设置为-10将在屏幕外显示。
            p.comment = "This positions the HUD (population and money text at the top of the screen) - default is 0, which is the top, value is in pixels, so setting 320 will display it 320 pixels from the top of the screen. Alter this to suit your screen resolution and avoid clashing with other text, setting to minus 10 will display it offscreen.";
            p = config.get("Settings", "StopRain", false);

            ModSim.configStopRain = p.getBoolean(false);
            //这只是一个个人模式：-）如果你也发现你的世界里一直在下雨，这会让你感到烦恼/导致延迟，那么把这个设置为真，你只会有短暂的淋浴
            p.comment = "This is just a personal mod :-) If you too find it rains ALL THE F***ING TIME in your world and it annoys you/causes lag, set this to true and you'll only have brief showers instead";

            p = config.get("Names", "MaleNames", I18n.format("container.sim.MaleNames"));
            String temp = p.getString();
            //这些是随机名字生成器使用的男性名字，请保持格式不变，否则会发生不好的事情。
            p.comment = "These are the male first names used by the random name generator, keep the format the same or bad things will happen.";
            ModSim.configMaleNames = temp.split(",");

            p = config.get("Names", "FemaleNames", I18n.format("container.sim.FemaleNames"));
            temp = p.getString();
            //这些是随机名字生成器使用的女性名字，保持格式不变，否则会发生不好的事情。
            p.comment = "These are the female first names used by the random name generator, keep the format the same or bad things will happen.";
            ModSim.configFemaleNames = temp.split(",");

            p = config.get("Names", "LastNames", I18n.format("container.sim.LastNames"));
            temp = p.getString();
            //这些是随机名称生成器使用的姓氏，请保持格式不变，否则会发生错误。
            p.comment = "These are the last names used by the random name generator, keep the format the same or bad things will happen.";
            ModSim.configSurnames = temp.split(",");

        } catch (Exception var7) {
            logger.error("无法分配 block/item ID - " + var7.toString());
        } finally {
            config.save();
            config.load();
            logger.info("已完成加载配置... ");
        }
    }

    public static Logger logger() {
        return logger;
    }
}
