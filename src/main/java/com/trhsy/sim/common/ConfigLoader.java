package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/2/8 0008
 * @apiNote
 */

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
            ModSim.configFolkTalking = p.getBoolean(false);


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
            p = config.get("Settings", "EnableMarkerAlignmentBeams", true);
            ModSim.configEnableMarkerAlignmentBeams = p.getBoolean(true);


            //放置标记时，它会触发4个对齐梁，将其设置为false将关闭这些梁
            p.comment = "When placing a marker it fires out 4 alignment beams, setting this to false will turn those beams off";
            p = config.get("Settings", "UseExpensiveRecipes", false);
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
            p = config.get("Names", "MaleNames", "Aaron, Adam, Alan, Albatrude, Alexander, Amaranth, Andrew, Angelo, Baldric, Bartholomew, Basher, Beau, Ben, Benie, Bennie, Bill, Blaize, Bob, Boots, Brad, Bradley, Breaker, Brian, Bruce, Butler, Cable, Caeser, Carlos, Carrington, Cassius, Clarence, CrazyDave, Dan, Darren, Darth, David, Derek, Dorian, Dougal, Drake, Drakkar, Draven, Earl, Ed, Edward, Fane, Fark, Fernando, Frank, Frankie, Fred, Gabe, Gary, Ged, Gerry, Glynne, Godfrey, Grendel, Grunter, Happy, Harry, Hercules, Horatio, Howard, Ike, Isaac, Jack, James, Jay, Jean-Luc, Jens, Jeremy, Jerry, Jessie, Jesus, Jim, Jimmy, Joe, John, Jose, Jose, Joseph, Juan, Justin, Justin, Kellam, Ken, Kevin, Knuckles, Lars, Lazarus, Lewis, Loki, Lorenzo, Louis,Lumpy, Lynk, Malcolm, Markus, Martin, Maximus, Michael,Mozart, Noire, Norman, Notch, Obsidian, Olaf, Oswaldo, Oxnard, Ozzy, Perkin, Pete, Philip, Pumpkin, Ralph, Randy, Red, Reks, Rick, Rogue, Romeo, Roy,Samuel, Schmitty, Scott, Sean, Seifer, Seth, Seymour, Sheldon, Sid, Simon, Slash, Spud, Steele, Stephen, Steve, Steven, Storm, Stryker, Tazer, Thunder, Tidus, Todd, Tom, Uther, Valen, Vance, Velderveer, Victor, Virion, Wayne, Wendle, William, Willie, Wolfgang, Wyatt, Xensor, Yoda, Zac, Zander, Zelroth, Zero, Zorro");
            String temp = p.getString();


            //这些是随机名字生成器使用的男性名字，请保持格式不变，否则会发生不好的事情。
            p.comment = "These are the male first names used by the random name generator, keep the format the same or bad things will happen.";
            ModSim.configMaleNames = temp.split(",");
            p = config.get("Names", "FemaleNames", "Adele, Agnes, Alice, Alouette, Amelia, Angela, Anne, Annette, Annie, Anthuria, Audrey, Belladonna, Bellinda, Beryl, Betty, BigDoris, Blossom, Bluebell, Breezy, Bridget, Bubbles, Bunty, Chalice, Charlotte, Chibi, ChiChi, Chlodeswinthe, Cinnamon, Coco, Connie, Cosette, Cressida, Cynthia, Daphne, Dimpleblossom, Dimples, Druscilla, Elizabeth, Elphina, Ermengarde, Essence, Fe Fe, Finola, Floris, Foofi, Forsythia, Foxglove, Francesca, Freesia, Frida, Funnysplash, Gardenia, Georgette, Giggles, Gladys, Glimmer, Gossamer, Gwendoline, Hannah, Harriet,  Hayley, Hazel, Heidi, Helga, Hilary, Himiltrud, Honor, Honoria, Hortensia, Hyacinth, Imeena, Iris, Jane, Janet, Joanne, Juliet, Kali, Karen, Kate, Kathy, Katrina, Kay, Kerry, Kristy, Lavinia, Leeta, LiloLil, Lisa, Lizette, Lobelia, Louise, Lucretia, Lumiona, Luna, Lurleen, Lyndis, Lynette, Macey, Madonna, Maggie, Maple, Marie, Marilee,  Martha, Mary, Maude, Maureen,  Maxine, Maya, Michelle, Mikki, Mildred,  Millie, Minnie, Morningpuff, Morticia, Myrtle, Mystery, Neen, Nicolette, Nightshade, Nina, Ninja, Odette, Olive, Pansy, Pansy, Paprika, Patricia, Peachy, Pearl, Persephone, Phoebe, Pinky, Plumeria, Poppy, Posy, Primrose, Priscilla, Queenie, Quintessa, Rebbeca, Rhonda, Ronni, Rosa, Rosette, Ruby, Scarlet, Schmarina, Semolina, Serena, Severa, Sharron, Sheila, Subrina, Sunflower, Susan, Susie, Suzette, Suzie, Talula, Tamara, Tammie, Tansy, Tera, Tessa, Tiffaney, Tourmaline, Trina, Trinity, Trish, Trudi, Truffles, Tulipdance, Twinkleboots, Ukara, Ursula, Velocity, Velvet, Vervain, Violet, Violet, Wilma, Winterwillow, Xyla, Yvette");
            temp = p.getString();


            //这些是随机名字生成器使用的女性名字，保持格式不变，否则会发生不好的事情。
            p.comment = "These are the female first names used by the random name generator, keep the format the same or bad things will happen.";
            ModSim.configFemaleNames = temp.split(",");
            p = config.get("Names", "LastNames", "Acorn, Aferditie, Alebuckle, AnchorArms,  Anvilbrow, Arsette, Arsing, Astley, Bacon, Bailey, Beiber, Bijoux, Bilberry, Binkydiggle, Bitterpool, Blonk, Blueberry, Booth, Boothby, Boozewob, Brandybuck, Brocx, Bugger, Bumbletoad, Bumfondle, Bunce, Buntflog, Button, Butts, Claus, Clinton, Clutz, Cox, Creaper, Cupid, Curlynoggin, Dalek, Dapplewink, Dent, Derp, Derpy, Diaper, Diggle, Dimfury, Dimplegourd, Dimplehorn, Donglefart, Dover, Dugbloron, Dulek, Dumbledug, Eaglefeathers, Easyrider, Featherbottom, Featheroak, Firsty, Fitzwilliam, Flashheart, Flickersand, Flintrock, Freckles, Fumblemore, Garlicfeet, Gawkroger, Giggerty, Glitterbreath, Goodbody, Gravy, Grayblade, Griffin, Griswold, Grubb, Handy, Hather, Havealot, Head, Hogpen, Hunt, Jabberwocky, Jaffa, Jibberjabba, Jigglybop, Jingles, Jones, Kegbuster, Kettle, Kitchen, Kneebiter, LaForce, Laforge, Lister, Loordes, MacArse, Maplebutton, Marblemantle, Mayflower, McBucket, McBurp, McCoy, McDonald, McFries, McNugget, Merry, Moist, Moneypenny, Mucus, Mugwort, Nabaztag, Neon, Nibbles, Oaktoes, O'Brian, O'Leary, O'Mygod, O'Notch, O'Reily, Pebble,  Peculier, Persson, Picard, Plank, Plop, Plumdrop, Plunder, Plunder, Potter, Power, Reed, Riker, Rumble, Shadespyre, Shakespeare, Sherman, Silverwood, Smith, Snot, Sparklebutter, Spitznoggle, Steelfinger, Strider, Stumbletoe, Tate, Testificate, Thornburrow, Twinklefig, Twistybees, Underwood, Vader, Walsch, Windywings, Winterbottom, Wonker, Yenocheq, Yog, Zaragamba,Flooberwag,Norsepapper");
            temp = p.getString();


            //这些是随机名称生成器使用的姓氏，请保持格式不变，否则会发生错误。
            p.comment = "These are the last names used by the random name generator, keep the format the same or bad things will happen.";
            ModSim.configSurnames = temp.split(",");

        } catch (Exception var7) {
            logger.error("Could not allocate block/item ID - " + var7.toString());
        } finally {
            config.save();
        }
        logger.info("已完成加载配置... ");
    }

    public static Logger logger() {
        return logger;
    }
}
