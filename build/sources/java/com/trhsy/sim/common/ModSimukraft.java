package com.trhsy.sim.common;

import com.trhsy.sim.client.ClientProxy;
import com.trhsy.sim.client.event.EventSounds;
import com.trhsy.sim.client.gui.GuiRunMod;
import com.trhsy.sim.common.block.*;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.fluid.FluidMilk;
import com.trhsy.sim.common.item.*;
import com.trhsy.sim.common.item.food.ItemSUKFood;
import com.trhsy.sim.common.jobs.JobSoldier;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.packets.client.Handler;
import com.trhsy.sim.packets.client.UpdateFolkPositionMessage;
import com.trhsy.sim.packets.server.LoadBuildingMessage;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @ClassName ModSimukraft
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2319:40
 **/
@Mod(modid = ModSimukraft.MODID, name = ModSimukraft.NAME, version = ModSimukraft.VERSION, dependencies = "required-after:Forge@[9.10,)")
public class ModSimukraft {
    public static final String MODID = "sim_u";
    public static final String NAME = "sim";
    public static final String VERSION = "1.0.0 Beta";

    @SidedProxy(
            clientSide = "com.trhsy.sim.client.ClientProxy",
            serverSide = "com.trhsy.sim.common.CommonProxy"
    )
    public static CommonProxy proxy;
    public static ClientProxy clientProxy;

    public static Logger log = Logger.getLogger("Sim-U");
    /*
    用于检测我们何时进入世界（非主菜单）以及玩家何时更改世界/地图
     */
    public static String currentSavePath = "";
    /*
    方块实例以及ids
     */
    static int constructorBlockId = 0;
    /*
    建筑施工人员
     */
    public static Block buildingConstructor;
    /*
    控制块的id
     */
    public static int controlBlockId = 0;
    /*
    控制箱
     */
    public static Block controlBox;
    /*
    标记棒id
     */
    static int markerBlockId = 0;
    /*
    标记棒
     */
    static Block marker;
    /*
    采矿箱id
     */
    static int miningBlockId = 0;
    /*
    采矿箱
     */
    public static Block miningBox;
    /*
    养殖箱id
     */
    static int farmingBlockId = 0;
    /*
    养殖箱
     */
    public static Block farmingBox;
    /*
    灯箱id
     */
    public static int lightboxId = 0;
    /*
    灯箱
     */
    public static Block lightBox;
    /*
    红灯箱
     */
    static Block lightBoxRed;
    /*
        橙灯箱
         */
    static Block lightBoxOrange;
    /*
    黄灯箱
     */
    static Block lightBoxYellow;
    /*
    绿灯箱
     */
    static Block lightBoxGreen;
    /*
    蓝灯箱
     */
    static Block lightBoxBlue;
    /*
    紫灯箱
     */
    static Block lightBoxPurple;
    /*
    风车
     */
    public static Block windmill;
    /*
    风车id
     */
    public static int windmillId;
    /*
    铁粒
     */
    public static Item itemGranulesIron;
    /*
    铁粒id
     */
    public static int itemGranulesIronId;
    /*
      金粒
     */
    public static Item itemGranulesGold;
    /*
    金粒id
     */
    public static int itemGranulesGoldId;
    /*
    风车基地
     */
    public static Item itemWindmillBase;
    /*
    风车基地id
     */
    public static int itemWindmillBaseId;
    /*
    风车叶片
     */
    public static Item itemWindmillVane;
    /*
    风车叶片id
     */
    public static int itemWindmillVaneId;
    /*
    风车帆
     */
    public static Item itemWindmillSails;
    /*
    风车帆id
     */
    public static int itemWindmillSailsId;
    /*
    食物id
     */
    public static int itemFoodId;
    /*
    食物
     */
    public static Item itemFood;
    /*
    奶酪
     */
    public static Item itemFoodCheese;
    /*
    奶酪id
     */
    public static Item itemFoodFries;
    /*
    汉堡
     */
    public static Item itemFoodBurger;
    /*
    奶酪汉堡
     */
    public static Item itemFoodCheeseburger;
    /*
    复合砖
     */
    public static Block blockCompositeBrick;
    /*
    复合砖id
     */
    public static int blockCompositeBrickId;
    /*
    奶酪块
     */
    public static Block blockCheese;
    /*
    奶酪id
     */
    public static int blockCheeseId;
    /*
    液体牛奶
     */
    public static Fluid SUKfluidMilk;
    /*
    液体牛奶块
     */
    public static Block blockFluidMilk;
    /*
    液体牛奶块id
     */
    public static int blockFluidMilkId;
    /*
    所有民众的数据（用于构建和维护 EntityFolk）
     */
    public static ArrayList<FolkData> theFolks = new ArrayList();
    /*
    所有的建筑对象
     */
    public static ArrayList<Building> theBuildings = new ArrayList();
    /*
    所有快递任务
     */
    public static ArrayList<CourierTask> theCourierTasks = new ArrayList();
    /*
    所有快递点
     */
    public static ArrayList<V3> theCourierPoints = new ArrayList();
    /*
    所有的采矿箱
     */
    public static ArrayList<MiningBox> theMiningBoxes = new ArrayList();
    /*
    所有养殖箱
     */
    public static ArrayList<FarmingBox> theFarmingBoxes = new ArrayList();
    /*
    所有情感关系
     */
    public static ArrayList<Relationship> theRelationships = new ArrayList();
    /*
    包含他们正在玩的这个关卡的所有游戏状态和设置
     */
    public static GameStates states = new GameStates();
    /*
    银行目前正在销售的商品列表，每天早上都会更新新商品
     */
    public static ArrayList<Commodity> theCommodities = new ArrayList();
    /*
    用于在update（）调用中升级作物农场
     */
    public static FarmingBox farmToUpgrade = null;
    /*
    升级作物农场计数
     */
    public static int farmToUpgradeCounter = 0;
    /*
    所有的农场升级点
     */
    private static ArrayList<V3> farmToUpgradePoints = null;
    /*
    白天
     */
    public static boolean isDay = true;
    /*
    所有的拆除
     */
    public static ArrayList<V3> demolishBlocks = new ArrayList();
    /*
    拆除
     */
    public static World demolishWorld = null;
    /*
    配置文件设置
     */
    public static Configuration config;
    /*
    配置人口限制
     */
    public static int configPopulationLimit = 100;
    /*
    配置木材面积
     */
    public static int configLumberArea = 30;
    /*
    配置禁用光束效果
     */
    public static boolean configDisableBeamEffect = false;
    /*
    配置谈话
     */
    public static boolean configFolkTalking = true;
    /*
    配置启用标记对齐
     */
    public static boolean configEnableMarkerAlignmentBeams = true;
    /*

     */
    public static boolean configUseExpensiveRecipies = false;
    /*
    配置物料提醒间隔
     */
    public static int configMaterialReminderInterval = 3;
    /*
    配置偏移量
     */
    public static int configHUDoffset = 0;
    /*
    停止降雨
     */
    public static boolean configStopRain = false;
    /*
    配置说英语
     */
    public static boolean configFolkTalkingEnglish = true;
    /*
    配置男性姓名
     */
    public static String[] configMaleNames;
    /*
    配置女性姓名
     */
    public static String[] configFemaleNames;
    /*
    姓氏
     */
    public static String[] configSurnames;
    /*
    游戏模式
     */
    public static GameMode gameMode = null;
    /*
    工作关系
     */
    public static SimpleNetworkWrapper network;
    private static GuiRunMod runModui = null;
    int highest = 0;
    int m1 = 0;
    protected static final String[] dow = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public ModSimukraft() {
    }

    /**
     * 游戏模式
     *
     * @return
     */
    public static int getGameModeNumber() {
        if (gameMode == GameMode.DONOTRUN) {
            //不运行
            return -1;
        } else if (gameMode == GameMode.NORMAL) {
            //正常的
            return 0;
        } else if (gameMode == GameMode.CREATIVE) {
            //创造
            return 1;
        } else {
            //
            return gameMode == GameMode.HARDCORE ? 2 : 0;
        }
    }

    public static void setGameModeFromNumber(int gm) {
        if (gm == -1) {
            gameMode = GameMode.DONOTRUN;
        } else if (gm == 0) {
            gameMode = GameMode.NORMAL;
        } else if (gm == 1) {
            gameMode = GameMode.CREATIVE;
        } else if (gm == 2) {
            gameMode = GameMode.HARDCORE;
        }

    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        //注册客户端消息系统
        network.registerMessage(Handler.class, UpdateFolkPositionMessage.class, 1, Side.CLIENT);
        //注册服务端消息系统
        network.registerMessage(com.trhsy.sim.packets.server.Handler.class, LoadBuildingMessage.class, 0, Side.SERVER);
        //设置日志级别
        log.setLevel(Level.INFO);
        //为参数中给定的文件创建配置文件。
        config = new Configuration(event.getSuggestedConfigurationFile());

        try {
            config.load();
            config.addCustomCategoryComment("Blocks", "Blocks");
            Property p = config.get("Settings", "DisableBeamingEffect", false);
            configDisableBeamEffect = p.getBoolean(false);
            p.comment = "This enables or disables the beaming effect (purple particles) - Set to true to turn them off.";
            p = config.get("Settings", "FolkTalking", true);
            configFolkTalking = p.getBoolean(false);
            p.comment = "If the folks BLARG talking gets annoying, set this to false";
            p = config.get("Settings", "FolkTalkingEnglish", true);
            configFolkTalkingEnglish = p.getBoolean(true);
            p.comment = "If the folks ENGLISH talking gets annoying, set this to false";
            p = config.get("Settings", "LumbermillArea", 40);
            configLumberArea = p.getInt();
            p.comment = "The radius in blocks that the lumberjack will look for trees from the starting point, don't set this too high, otherwise it will slow down MC every time they scan for the nearest tree, so 30 to 1000 should be ok (1000 is 1 Kilometre)";
            p = config.get("Settings", "PopulationLimit", 200);
            configPopulationLimit = p.getInt();
            p.comment = "Limit the population from growing beyond this number if you have an older computer";
            p = config.get("Settings", "EnableMarkerAlignmentBeams", true);
            configEnableMarkerAlignmentBeams = p.getBoolean(true);
            p.comment = "When placing a marker it fires out 4 alignment beams, setting this to false will turn those beams off";
            p = config.get("Settings", "UseExpensiveRecipes", false);
            configUseExpensiveRecipies = p.getBoolean(false);
            p.comment = "If you think the mining/farming boxes are too cheap/overpowered, set this to true to make the recipies require diamond tools instead of stone tools";
            p = config.get("Settings", "MaterialReminderInterval", 3);
            configMaterialReminderInterval = p.getInt(3);
            p.comment = "When a builder runs out of materials, they will let you know about it every 3 minutes, set to 0 for no further reminders.";
            if (configMaterialReminderInterval <= 0) {
                configMaterialReminderInterval = 2000;
            }

            p = config.get("Settings", "HUDoffset", 0);
            configHUDoffset = p.getInt(0);
            p.comment = "This positions the HUD (population and money text at the top of the screen) - default is 0, which is the top, value is in pixels, so setting 320 will display it 320 pixels from the top of the screen. Alter this to suit your screen resolution and avoid clashing with other text, setting to minus 10 will display it offscreen.";
            p = config.get("Settings", "StopRain", false);
            configStopRain = p.getBoolean(false);
            p.comment = "This is just a personal mod :-) If you too find it rains ALL THE F***ING TIME in your world and it annoys you/causes lag, set this to true and you'll only have brief showers instead";
            p = config.get("Names", "MaleNames", "Aaron, Adam, Alan, Albatrude, Alexander, Amaranth, Andrew, Angelo, Baldric, Bartholomew, Basher, Beau, Ben, Benie, Bennie, Bill, Blaize, Bob, Boots, Brad, Bradley, Breaker, Brian, Bruce, Butler, Cable, Caeser, Carlos, Carrington, Cassius, Clarence, CrazyDave, Dan, Darren, Darth, David, Derek, Dorian, Dougal, Drake, Drakkar, Draven, Earl, Ed, Edward, Fane, Fark, Fernando, Frank, Frankie, Fred, Gabe, Gary, Ged, Gerry, Glynne, Godfrey, Grendel, Grunter, Happy, Harry, Hercules, Horatio, Howard, Ike, Isaac, Jack, James, Jay, Jean-Luc, Jens, Jeremy, Jerry, Jessie, Jesus, Jim, Jimmy, Joe, John, Jose, Jose, Joseph, Juan, Justin, Justin, Kellam, Ken, Kevin, Knuckles, Lars, Lazarus, Lewis, Loki, Lorenzo, Louis,Lumpy, Lynk, Malcolm, Markus, Martin, Maximus, Michael,Mozart, Noire, Norman, Notch, Obsidian, Olaf, Oswaldo, Oxnard, Ozzy, Perkin, Pete, Philip, Pumpkin, Ralph, Randy, Red, Reks, Rick, Rogue, Romeo, Roy,Samuel, Schmitty, Scott, Sean, Seifer, Seth, Seymour, Sheldon, Sid, Simon, Slash, Spud, Steele, Stephen, Steve, Steven, Storm, Stryker, Tazer, Thunder, Tidus, Todd, Tom, Uther, Valen, Vance, Velderveer, Victor, Virion, Wayne, Wendle, William, Willie, Wolfgang, Wyatt, Xensor, Yoda, Zac, Zander, Zelroth, Zero, Zorro");
            String temp = p.getString();
            p.comment = "These are the male first names used by the random name generator, keep the format the same or bad things will happen.";
            configMaleNames = temp.split(",");
            p = config.get("Names", "FemaleNames", "Adele, Agnes, Alice, Alouette, Amelia, Angela, Anne, Annette, Annie, Anthuria, Audrey, Belladonna, Bellinda, Beryl, Betty, BigDoris, Blossom, Bluebell, Breezy, Bridget, Bubbles, Bunty, Chalice, Charlotte, Chibi, ChiChi, Chlodeswinthe, Cinnamon, Coco, Connie, Cosette, Cressida, Cynthia, Daphne, Dimpleblossom, Dimples, Druscilla, Elizabeth, Elphina, Ermengarde, Essence, Fe Fe, Finola, Floris, Foofi, Forsythia, Foxglove, Francesca, Freesia, Frida, Funnysplash, Gardenia, Georgette, Giggles, Gladys, Glimmer, Gossamer, Gwendoline, Hannah, Harriet,  Hayley, Hazel, Heidi, Helga, Hilary, Himiltrud, Honor, Honoria, Hortensia, Hyacinth, Imeena, Iris, Jane, Janet, Joanne, Juliet, Kali, Karen, Kate, Kathy, Katrina, Kay, Kerry, Kristy, Lavinia, Leeta, LiloLil, Lisa, Lizette, Lobelia, Louise, Lucretia, Lumiona, Luna, Lurleen, Lyndis, Lynette, Macey, Madonna, Maggie, Maple, Marie, Marilee,  Martha, Mary, Maude, Maureen,  Maxine, Maya, Michelle, Mikki, Mildred,  Millie, Minnie, Morningpuff, Morticia, Myrtle, Mystery, Neen, Nicolette, Nightshade, Nina, Ninja, Odette, Olive, Pansy, Pansy, Paprika, Patricia, Peachy, Pearl, Persephone, Phoebe, Pinky, Plumeria, Poppy, Posy, Primrose, Priscilla, Queenie, Quintessa, Rebbeca, Rhonda, Ronni, Rosa, Rosette, Ruby, Scarlet, Schmarina, Semolina, Serena, Severa, Sharron, Sheila, Subrina, Sunflower, Susan, Susie, Suzette, Suzie, Talula, Tamara, Tammie, Tansy, Tera, Tessa, Tiffaney, Tourmaline, Trina, Trinity, Trish, Trudi, Truffles, Tulipdance, Twinkleboots, Ukara, Ursula, Velocity, Velvet, Vervain, Violet, Violet, Wilma, Winterwillow, Xyla, Yvette");
            temp = p.getString();
            p.comment = "These are the female first names used by the random name generator, keep the format the same or bad things will happen.";
            configFemaleNames = temp.split(",");
            p = config.get("Names", "LastNames", "Acorn, Aferditie, Alebuckle, AnchorArms,  Anvilbrow, Arsette, Arsing, Astley, Bacon, Bailey, Beiber, Bijoux, Bilberry, Binkydiggle, Bitterpool, Blonk, Blueberry, Booth, Boothby, Boozewob, Brandybuck, Brocx, Bugger, Bumbletoad, Bumfondle, Bunce, Buntflog, Button, Butts, Claus, Clinton, Clutz, Cox, Creaper, Cupid, Curlynoggin, Dalek, Dapplewink, Dent, Derp, Derpy, Diaper, Diggle, Dimfury, Dimplegourd, Dimplehorn, Donglefart, Dover, Dugbloron, Dulek, Dumbledug, Eaglefeathers, Easyrider, Featherbottom, Featheroak, Firsty, Fitzwilliam, Flashheart, Flickersand, Flintrock, Freckles, Fumblemore, Garlicfeet, Gawkroger, Giggerty, Glitterbreath, Goodbody, Gravy, Grayblade, Griffin, Griswold, Grubb, Handy, Hather, Havealot, Head, Hogpen, Hunt, Jabberwocky, Jaffa, Jibberjabba, Jigglybop, Jingles, Jones, Kegbuster, Kettle, Kitchen, Kneebiter, LaForce, Laforge, Lister, Loordes, MacArse, Maplebutton, Marblemantle, Mayflower, McBucket, McBurp, McCoy, McDonald, McFries, McNugget, Merry, Moist, Moneypenny, Mucus, Mugwort, Nabaztag, Neon, Nibbles, Oaktoes, O'Brian, O'Leary, O'Mygod, O'Notch, O'Reily, Pebble,  Peculier, Persson, Picard, Plank, Plop, Plumdrop, Plunder, Plunder, Potter, Power, Reed, Riker, Rumble, Shadespyre, Shakespeare, Sherman, Silverwood, Smith, Snot, Sparklebutter, Spitznoggle, Steelfinger, Strider, Stumbletoe, Tate, Testificate, Thornburrow, Twinklefig, Twistybees, Underwood, Vader, Walsch, Windywings, Winterbottom, Wonker, Yenocheq, Yog, Zaragamba,Flooberwag,Norsepapper");
            temp = p.getString();
            p.comment = "These are the last names used by the random name generator, keep the format the same or bad things will happen.";
            configSurnames = temp.split(",");
        } catch (Exception var7) {
            log.severe("Could not allocate block/item ID - " + var7.toString());
        } finally {
            config.save();
        }

        File check = new File(getSimukraftFolder());
        if (!check.exists()) {
            System.out.println("SimCity error - Mod not correctly installed, ./minecraft/mods/Simukraft/ folder is missing - copy this file from the zip provided");
        }

        SUKfluidMilk = new FluidMilk();
        blockFluidMilk = (new BlockFluidMilk()).setUnlocalizedName("fluidMilk");
        lightBox = new BlockLightBox();
//        buildingConstructor = (new BlockConstructorBox()).setStepSound(Block.soundTypeWood).setHardness(2.0F).setResistance(1.0F).setUnlocalizedName("SUKconstructorBox");
        controlBox = (new BlockControlBox()).setStepSound(Block.soundTypeWood).setHardness(10.0F).setResistance(1.0F).setUnlocalizedName("SUKcontrol");
        marker = (new BlockMarker()).setStepSound(Block.soundTypeWood).setHardness(2.0F).setResistance(1.0F).setUnlocalizedName("SUKmarker");
        miningBox = (new BlockMiningBox()).setStepSound(Block.soundTypeWood).setHardness(2.0F).setResistance(1.0F).setUnlocalizedName("SUKmining");
        farmingBox = (new BlockFarmingBox()).setStepSound(Block.soundTypeWood).setHardness(2.0F).setResistance(1.0F).setUnlocalizedName("SUKfarming");
        itemFood = (new ItemSUKFood()).setUnlocalizedName("SUKfood");
        blockCompositeBrick = (new BlockCompositeBrick(Material.rock)).setStepSound(Block.soundTypeStone).setHardness(8.0F).setResistance(7.0F).setUnlocalizedName("SUKcompositebrick");
        blockCheese = (new BlockCheeseBlock()).setStepSound(Block.soundTypeCloth).setHardness(0.1F).setResistance(0.5F).setUnlocalizedName("SUKcheeseBlock");
        itemGranulesGold = new ItemGranulesGold(itemGranulesGoldId);
        LanguageRegistry.addName(itemGranulesGold, "Gold granules");
        itemGranulesIron = new ItemGranulesIron(itemGranulesIronId);
        LanguageRegistry.addName(itemGranulesIron, "Iron granules");
        itemWindmillBase = new ItemWindmillBase(itemWindmillBaseId);
        LanguageRegistry.addName(itemWindmillBase, "Windmill base");
        itemWindmillVane = new ItemWindmillVane(itemWindmillVaneId);
        LanguageRegistry.addName(itemWindmillVane, "Windmill vane");
        itemWindmillSails = new ItemWindmillSails(itemWindmillSailsId);
        LanguageRegistry.addName(itemWindmillBase, "Windmill sails");
        MinecraftForge.EVENT_BUS.register(new EventSounds());
//        GameRegistry.registerBlock(buildingConstructor, "SUKconstructorBox");
        GameRegistry.registerBlock(controlBox, "SUKcontrol");
        GameRegistry.registerBlock(marker, "SUKmarker");
        GameRegistry.registerBlock(miningBox, "SUKmining");
        GameRegistry.registerBlock(farmingBox, "SUKfarming");
        GameRegistry.registerBlock(blockCompositeBrick, "SUKcompositebrick");
        GameRegistry.registerBlock(blockCheese, "SUKcheeseblock");
        GameRegistry.registerBlock(blockFluidMilk, "fluidMilk");
        GameRegistry.registerBlock(lightBox, "SUKlight");
        GameRegistry.registerTileEntity(TileEntityWindmill.class, "tileentitywindmill");
//        LanguageRegistry.addName(buildingConstructor, "Sim-U-Building Constructor Box");
        LanguageRegistry.addName(controlBox, "Sim-U-Control Box");
        LanguageRegistry.addName(marker, "Sim-U-Marker");
        LanguageRegistry.addName(miningBox, "Sim-U-Mining Box");
        LanguageRegistry.addName(farmingBox, "Sim-U-Farming Box");
        LanguageRegistry.addName(blockCompositeBrick, "Composite Brick");
        LanguageRegistry.addName(blockCheese, "Block of Cheese");
        LanguageRegistry.addName(blockFluidMilk, "Milk");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 0), "Cheese slice");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 1), "a Hamburger");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 2), "Fries");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 3), "a Cheeseburger");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.white.name", "Sim-U-Light (white)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.red.name", "Sim-U-Light (red)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.orange.name", "Sim-U-Light (orange)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.yellow.name", "Sim-U-Light (yellow)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.green.name", "Sim-U-Light (green)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.blue.name", "Sim-U-Light (blue)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.purple.name", "Sim-U-Light (purple)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.rainbow.name", "Sim-U-Light (rainbow)");
        GameRegistry.addRecipe(new ItemStack(buildingConstructor, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Blocks.crafting_table});
        GameRegistry.addRecipe(new ItemStack(marker, 3), new Object[]{"G", "S", 'S', Items.stick, 'G', new ItemStack(Items.dye, 1, 11)});
        if (configUseExpensiveRecipies) {
            GameRegistry.addRecipe(new ItemStack(miningBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.diamond_pickaxe});
            GameRegistry.addRecipe(new ItemStack(farmingBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.diamond_pickaxe});
        } else {
            GameRegistry.addRecipe(new ItemStack(miningBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.stone_pickaxe});
            GameRegistry.addRecipe(new ItemStack(farmingBox, 1), new Object[]{"PPP", "CWC", "CCC", 'C', Blocks.cobblestone, 'P', Blocks.planks, 'W', Items.stone_hoe});
        }

        GameRegistry.addRecipe(new ItemStack(lightBox, 2), new Object[]{"LL", "LL", 'L', Blocks.torch});
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 1), new Object[]{lightBox, new ItemStack(Items.dye, 1, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 2), new Object[]{lightBox, new ItemStack(Items.dye, 1, 14)});
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 3), new Object[]{lightBox, new ItemStack(Items.dye, 1, 11)});
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 4), new Object[]{lightBox, new ItemStack(Items.dye, 1, 10)});
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 5), new Object[]{lightBox, new ItemStack(Items.dye, 1, 4)});
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 6), new Object[]{lightBox, new ItemStack(Items.dye, 1, 5)});
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 7), new Object[]{lightBox, new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 14), new ItemStack(Items.dye, 1, 11), new ItemStack(Items.dye, 1, 10), new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 5)});
        GameRegistry.addRecipe(new ItemStack(blockCheese, 1), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(itemFood, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(itemFood, 9, 0), new Object[]{new ItemStack(blockCheese)});
        GameRegistry.addRecipe(new ItemStack(blockCompositeBrick, 1), new Object[]{"CSC", "SIS", "CSC", 'C', Blocks.hardened_clay, 'S', Blocks.stone, 'I', Blocks.fence});
        GameRegistry.addRecipe(new ItemStack(itemWindmillBase), new Object[]{" C ", "CCC", "CCC", 'C', blockCompositeBrick});

        int c;
        for (c = 0; c < 16; ++c) {
            GameRegistry.addRecipe(new ItemStack(itemWindmillVane, 1, c), new Object[]{"WWW", "SSS", 'S', Items.stick, 'W', new ItemStack(Blocks.wool, 1, c)});
        }

        for (c = 0; c < 16; ++c) {
            GameRegistry.addRecipe(new ItemStack(itemWindmillSails, 1, c), new Object[]{" V ", "VPV", " V ", 'V', new ItemStack(itemWindmillVane, 1, c), 'P', Blocks.planks});
        }

        for (c = 0; c < 16; ++c) {
            GameRegistry.addRecipe(new ItemStack(windmill, 1, c), new Object[]{"S", "B", 'S', new ItemStack(itemWindmillSails, 1, c), 'B', itemWindmillBase});
        }

        GameRegistry.addSmelting(itemGranulesGold, new ItemStack(Items.gold_ingot), 0.1F);
        GameRegistry.addSmelting(itemGranulesIron, new ItemStack(Items.iron_ingot), 0.1F);
        EntityRegistry.registerGlobalEntityID(EntityAlignBeam.class, "AlignBeam", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityAlignBeam.class, "AlignBeam", 0, this, 250, 10, false);
        EntityRegistry.registerGlobalEntityID(EntityFolk.class, "Folk", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityFolk.class, "Folk", 1, this, 250, 2, true);
        EntityRegistry.registerGlobalEntityID(EntityConBox.class, "ConBox", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityConBox.class, "ConBox", 2, this, 250, 2, true);
        EntityRegistry.registerGlobalEntityID(EntityWindmill.class, "SUKWindmill", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityWindmill.class, "SUKWindmill", 3, this, 250, 1, false);
        proxy.registerRenderInfo();
        proxy.registerMisc();
    }

    @Mod.EventHandler
    public void initLoad(FMLInitializationEvent event) {
    }

    public static void resetAndLoadNewWorld() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        log.info("RESETTING and loading world on " + side.toString() + " SIDE");
        theBuildings.clear();
        theCourierPoints.clear();
        theCourierTasks.clear();
        theMiningBoxes.clear();
        theFarmingBoxes.clear();
        theFolks.clear();
        theRelationships.clear();
        File f = new File(getSavesDataFolder() + "settings.sk2");
        if (!f.exists()) {
            f = new File(getSavesDataFolder() + "settings.suk");
        }

        if (f.exists()) {
            states.loadStates();

            try {
                setGameModeFromNumber(states.gameModeNumber);
            } catch (Exception var3) {
                setGameModeFromNumber(0);
            }
        } else {
            states = new GameStates();
            states.saveStates();
        }

        if (states == null) {
            (new File(getSavesDataFolder() + "settings.sk2")).delete();
            states = new GameStates();
            states.saveStates();
            sendChat("Your SimCity settings file was corrupted, I had to make a new one");
        }

        if (states.gameModeNumber == -1) {
            if (runModui == null) {
                GuiRunMod runModui = new GuiRunMod();
                Minecraft.getMinecraft().displayGuiScreen(runModui);
            }

        } else {
            if (states.gameModeNumber >= 0) {
                System.out.println("Startup already been run");
                proxy.ranStartup = true;
            }

            sendChat("Welcome to SimCity " + VERSION);
            theFolks.clear();
            Building.initialiseAllBuildings();
            Building.loadAllBuildings();
            CourierTask.loadCourierTasksAndPoints();
            MiningBox.loadMiningBoxes();
            FarmingBox.loadFarmingBoxes();
            FolkData.loadAndSpawnFolks();
            Relationship.loadRelationships();
            updateCheck();
            isDay = isDayTime();
            Building.checkTennants();
            proxy.ranStartup = true;
        }
    }

    public static void sendChat(String theText) {
        WorldServer[] arr$ = MinecraftServer.getServer().worldServers;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            World w = arr$[i$];
            if (!w.isRemote) {
                for (int i = 0; i < w.playerEntities.size(); ++i) {
                    EntityPlayer p = (EntityPlayer) w.playerEntities.get(i);
                    p.addChatComponentMessage(new ChatComponentText(theText));
                }
            }
        }

    }

    public static String getSavesDataFolder() {
        String worldname = MinecraftServer.getServer().getFolderName();
        String strmc = (new File(".")).getAbsolutePath();
        strmc = strmc.substring(0, strmc.length() - 1);
        File test = new File(strmc + "saves");
        String ret = "";
        if (test.exists()) {
            ret = (new File(strmc + File.separator + "saves" + File.separator + worldname + File.separator + "simukraft" + File.separator)).getAbsolutePath() + File.separator;
        } else {
            strmc = strmc + worldname + File.separator + "simukraft" + File.separator;
            ret = (new File(strmc)).getAbsolutePath();
        }

        File f = new File(ret);
        if (!f.exists()) {
            f.mkdirs();
        }

        return ret;
    }

    public static String getSimukraftFolder() {
        try {
            String strmc = (new File(".")).getAbsolutePath();
            strmc = strmc.substring(0, strmc.length() - 1);
            return (new File(strmc + File.separator + "mods" + File.separator + "Simukraft")).getAbsolutePath();
        } catch (Exception var1) {
            return "";
        }
    }

    public static boolean isDayTime() {
        return MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTime() % 24000L <= 11999L;
    }

    public static String displayMoney(float moneyin) {
        DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
        String output = myFormatter.format((double) moneyin);
        return output;
    }

    public static void updateCheck() {
    }

    public void ThreadUpdate() {
        this.start();
    }

    public void start() {
        try {
            Thread.sleep(15000L);
            File check = new File(getSimukraftFolder() + "/buildings/");
            if (!check.exists()) {
                sendChat(getSimukraftFolder() + "/buildings/  folder is missing, SimCity is not correctly installed, please copy the simukraft folder AND the zip file.");
                return;
            }

            String baseURL = "http://satscape.no-ip.info:7254/simukraftstore/";
            String ver = this.downloadFile(baseURL + "simukraft-version.txt", getSimukraftFolder() + File.separator + "simukraft.txt");
            if (ver != null) {
                ver = ver.trim();
                if (!ver.contentEquals("")) {
                    if (!VERSION.contentEquals(ver)) {
                        sendChat("**** NEW update of SimCity available (from " + VERSION + " to " + ver + ") at satscape.wordpress.com/simukraft");
                    }

                    Long now = System.currentTimeMillis();
                    states.lastUpdateCheck = now;
                    states.saveStates();
                }
            }

            int high = this.getHighestPKID("residential");
            int o = this.getHighestPKID("other");
            if (o > high) {
                high = o;
            }

            String newbs = this.downloadFile(baseURL + "backend.php?cmd=getnew&n=" + high + "&i=" + this.getTheirId() + "&v=" + VERSION, getSimukraftFolder() + File.separator + "simukraft.txt");
            if (newbs.length() == 0) {
                return;
            }

            String[] items = newbs.split("!END");

            for (int i = 0; i < items.length - 1; ++i) {
                String[] fields = items[i].split("!F");
                String url = baseURL + "catalogue/PKID" + fields[0] + "-" + fields[1] + ".txt";
                String local = getSimukraftFolder() + "/buildings/" + fields[3] + "/PKID" + fields[0] + "-" + fields[1] + ".txt";
                String ret = this.downloadFile(url, local);
                if (!ret.contentEquals("")) {
                    url = baseURL + "backend.php?cmd=got&pk=" + fields[0];
                    this.downloadFile(url, getSimukraftFolder() + File.separator + "cache.txt");
                    sendChat("SimCity: Downloaded new building - '" + fields[1] + "' by " + fields[2] + " (" + fields[3] + ")");
                }
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    private String getTheirId() {
        return null;
    }

    public int getHighestPKID(String type) {
        File actual = new File(getSimukraftFolder() + File.separator + "buildings" + File.separator + type + File.separator);
        File[] arr$ = actual.listFiles();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            File f = arr$[i$];
            if (f.getName().startsWith("PKID")) {
                this.m1 = f.getName().indexOf("-");
                if (this.m1 > 0) {
                    String id = f.getName().substring(4, this.m1);
                    if (Integer.parseInt(id) > this.highest) {
                        this.highest = Integer.parseInt(id);
                    }
                }
            }
        }

        return this.highest;
    }

    public String downloadFile(String url, String localFile) {
        String ret = "";
        log.info("Downloading file " + url);
        url = url.replace(" ", "%20");

        try {
            BufferedInputStream in = new BufferedInputStream((new URL(url)).openStream());
            FileOutputStream fos = new FileOutputStream(localFile);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[4096];
            boolean var8 = false;

            int x;
            while ((x = in.read(data, 0, 4096)) >= 0) {
                bout.write(data, 0, x);
            }

            bout.flush();
            ret = new String(data);
            bout.close();
            in.close();
        } catch (Exception var9) {
            ret = "";
            var9.printStackTrace();
        }

        return ret;
    }

    public static void dayTransitionHandler() {
        //FolkData folk1;
        FolkData folk1;
        int homeless;
        int f1;
        if (isDayTime() && !isDay) {
            isDay = true;
            log.info("Night to day transition");
            World world = proxy.getClientWorld();
            if (world != null) {
                EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                if (p != null) {
                    proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSimukraft.MODID + ":rooster", 1.0F, 1.0F, false);
                }
            }

            ++states.dayOfWeek;
            if (states.dayOfWeek > 6) {
                states.dayOfWeek = 0;
                homeless = 0;
                Iterator i$ = theFolks.iterator();

                while (i$.hasNext()) {
                    folk1 = (FolkData) i$.next();
                    if (folk1.getHome() == null) {
                        ++homeless;
                    }
                }

                if (homeless > 1) {
                    sendChat("There is a demand for more residential housing, you have " + homeless + " folks without a home.");
                }
            }

            evolveFolks();
            if (theFolks.size() > 1) {
                Random rand = new Random();
                f1 = rand.nextInt(theFolks.size());

                int f2;
                for (f2 = f1; f2 == f1; f2 = rand.nextInt(theFolks.size())) {
                }

                folk1 = (FolkData) theFolks.get(f1);
                FolkData folk2 = (FolkData) theFolks.get(f2);
                Relationship.meddleWithRelationship(folk1, folk2);
            }
        }

        if (!isDayTime() && isDay) {
            isDay = false;
            log.info("Day to Night transition");
            if (theFolks.size() > 1) {
                Random rand = new Random();
                homeless = rand.nextInt(theFolks.size());

                for (f1 = homeless; f1 == homeless; f1 = rand.nextInt(theFolks.size())) {
                }

                folk1 = (FolkData) theFolks.get(homeless);
                folk1 = (FolkData) theFolks.get(f1);
                Relationship.meddleWithRelationship(folk1, folk1);
            }

            Iterator i$ = theFolks.iterator();

            while (i$.hasNext()) {
                FolkData folk = (FolkData) i$.next();
                folk.destination = null;
                if (folk.theEntity != null) {
                    folk.theEntity.getNavigator().clearPathEntity();
                }
            }
        }

    }

    private static void evolveFolks() {
        if (theFolks.size() > 0) {
            Random rand = new Random();
            log.info("evolving folks");
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000L);
                    } catch (Exception var5) {
                    }

                    float totalRent = 0.0F;
                    float totalCorpTax = 0.0F;
                    if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                        for (int b = 0; b < ModSimukraft.theBuildings.size(); ++b) {
                            Building building = (Building) ModSimukraft.theBuildings.get(b);
                            if (building.type.contentEquals("residential") && building.tennants.size() > 0) {
                                if (building.rent == null || building.rent == 0.0F) {
                                    building.rent = 1.0F;
                                }

                                ModSimukraft.log.info("Building rent for " + building.displayNameWithoutPK + ": " + building.rent + "(" + building.blocksInBuilding + ")");
                                totalRent += building.rent;
                            }
                        }
                    }

                    if (totalRent > 0.0F) {
                        ModSimukraft.sendChat("Collected " + ModSimukraft.displayMoney(totalRent) + " Sim-u-credits in rent today.");
                        ModSimukraft.sendChat("Collected " + ModSimukraft.displayMoney(totalCorpTax) + " Sim-u-credits in corporation tax today.");
                        GameStates var10000 = ModSimukraft.states;
                        var10000.credits += totalRent;
                        var10000 = ModSimukraft.states;
                        var10000.credits += totalCorpTax;
                        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                        if (p != null) {
                            ModSimukraft.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSimukraft.MODID + ":cash", 1.0F, 1.0F, false);
                        }
                    } else if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                        ModSimukraft.sendChat("No rent collected today, you should hire a folk to build a residential house.");
                    }

                }
            });
            t.start();

            FolkData folk;
            int fl;
            int f;
            for (fl = 0; fl < theFolks.size(); ++fl) {
                folk = (FolkData) theFolks.get(fl);
                folk.greetedToday = false;
                folk.shaggingStage = -1.0F;
                if (folk.pregnancyStage > 0.0F) {
                    folk.pregnancyStage += 0.1F;
                }

                f = folk.age;
                if (f >= 18) {
                    if (states.dayOfWeek == 6) {
                        ++folk.age;
                    }
                } else if (states.dayOfWeek == 3 || states.dayOfWeek == 6) {
                    ++folk.age;
                    if (f == 17 && folk.age == 18) {
                        folk.evictThem();
                        sendChat(folk.name + " is now 18 years old, they'll start looking for a house and you can now employ them too.");
                    }
                }

                if (folk.age > 110 && rand.nextInt(10) == 5) {
                    sendChat(folk.name + " is old and not feeling very well...oh no!");
                    folk.eventDied(DamageSource.generic);
                }
            }

            if (gameMode != GameMode.CREATIVE) {
                fl = rand.nextInt(theFolks.size());

                for (f = 0; f < theFolks.size(); ++f) {
                    folk = (FolkData) theFolks.get(f);
                    if (f == fl) {
                        --folk.levelFood;
                        if (folk.levelFood == 0) {
                            sendChat(folk.name + " is VERY hungry, you should build a farm, grocery, bakery or throw some food at them.");
                        }
                    }
                }

                for (f = 0; f < theFolks.size(); ++f) {
                    folk = (FolkData) theFolks.get(f);
                    if (folk.theirJob != null && folk.vocation == Vocation.SOLDIER) {
                        JobSoldier job = (JobSoldier) folk.theirJob;
                        float pay = (float) job.kills * 0.2F;
                        if (job.kills > 0) {
                            sendChat("Paid " + folk.name + " " + displayMoney(pay) + " Sim-u-credits for killing " + job.kills + " hostile mobs yesterday.");
                            GameStates var10000 = states;
                            var10000.credits -= pay;
                            job.kills = 0;
                        }
                    }
                }

                boolean updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.planks, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.cobblestone, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.stone, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.glass, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.wool, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.brick_block, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.stonebrick, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.fence, updown);
            }

            states.saveStates();
            Commodity.refreshAvailableCommoditities();
        }
    }

    public static void demolishBlocks() {
        if (demolishBlocks.size() >= 1) {
            int count = demolishBlocks.size();
            if (count > 10) {
                count = 10;
            }

            for (int i = 0; i < count; ++i) {
                V3 blockLoc = (V3) demolishBlocks.get(0);

                try {
                    Block block = Block.getBlockFromName(blockLoc.name);
                    block.dropBlockAsItem(demolishWorld, blockLoc.x.intValue(), blockLoc.y.intValue() + 10 + (new Random()).nextInt(20), blockLoc.z.intValue(), 0, 0);
                    demolishBlocks.remove(0);
                } catch (Exception var5) {
                }
            }

        }
    }

    public static void upgradeFarm() {
        if (farmToUpgrade.level == 0) {
            farmToUpgrade.level = 1;
        }

        V3 point;
        WorldServer theWorld;
        if (farmToUpgrade.level == 1) {
            if (farmToUpgradePoints == null) {
                farmToUpgradePoints = farmToUpgrade.getPerimeterPoints();
            }

            point = (V3) farmToUpgradePoints.get(farmToUpgradeCounter);
            theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);
            Block id = theWorld.getBlock(point.x.intValue(), point.y.intValue(), point.z.intValue());
            boolean destroy = false;
            if (id != null) {
                TileEntity te = theWorld.getTileEntity(point.x.intValue(), point.y.intValue(), point.z.intValue());
                if (te == null) {
                    destroy = true;
                } else if (!(te instanceof IInventory)) {
                    destroy = true;
                }
            } else {
                destroy = true;
            }

            if (destroy) {
                theWorld.breakBlock(point.x.intValue(), point.y.intValue(), point.z.intValue(), true);
                theWorld.setBlock(point.x.intValue(), point.y.intValue(), point.z.intValue(), Blocks.fence, 0, 3);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue(), point.z.intValue());
            }

            if (farmToUpgradeCounter % 6 == 0) {
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 1, point.z.intValue(), lightBox, 0, 3);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 1, point.z.intValue());
            }
        } else if (farmToUpgrade.level == 2) {
            if (farmToUpgradePoints == null) {
                farmToUpgradePoints = farmToUpgrade.getSoilBlockPoints();
            }

            point = (V3) farmToUpgradePoints.get(farmToUpgradeCounter);
            theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);
            if (point.x.intValue() % 5 == 0 && point.z.intValue() % 5 == 0) {
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 1, point.z.intValue(), Blocks.water, 0, 3);
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 2, point.z.intValue(), lightBox, 0, 3);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 1, point.z.intValue());
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 2, point.z.intValue());
            }
        }

        ++farmToUpgradeCounter;
        if (farmToUpgradeCounter > farmToUpgradePoints.size() - 1) {
            ++farmToUpgrade.level;
            farmToUpgradePoints = null;
            farmToUpgrade = null;
            farmToUpgradeCounter = 0;
            log.info("Finished farm upgrade");
        }
    }

    public static String getDayOfWeek() {
        return dow[states.dayOfWeek];
    }

    public static ArrayList<String> loadSK2(String fullFilename) {
        ArrayList ret = new ArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fullFilename));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                ret.add(line);
            }

            br.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return ret;
    }

    public static void saveSK2(String fullFilename, ArrayList<String> strings) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFilename));
            Iterator i$ = strings.iterator();

            while (i$.hasNext()) {
                String line = (String) i$.next();
                bw.write(line + "\r\n");
            }

            bw.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

}
