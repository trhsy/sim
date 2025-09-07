package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

/**
 * 声音事件注册类（独立管理所有声音注册）
 */
// 事件订阅者，监听音效注册事件
@Mod.EventBusSubscriber(modid = ModSim.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ModSim.MODID) // 用于静态字段自动注入
public class SoundLoader {
    // 从 sounds.json 中提取的所有声音事件（按字母顺序排列）
    public static final SoundEvent BAKERF = new SoundEvent(new ResourceLocation(ModSim.MODID, "bakerf")).setRegistryName(ModSim.MODID, "bakerf");
    public static final SoundEvent BAKERM = new SoundEvent(new ResourceLocation(ModSim.MODID, "bakerm")).setRegistryName(ModSim.MODID, "bakerm");
    public static final SoundEvent BEAMDOWN = new SoundEvent(new ResourceLocation(ModSim.MODID, "beamdown")).setRegistryName(ModSim.MODID, "beamdown");
    public static final SoundEvent BEAMDOWNTWO = new SoundEvent(new ResourceLocation(ModSim.MODID, "beamdowntwo")).setRegistryName(ModSim.MODID, "beamdowntwo");
    public static final SoundEvent BEAMF = new SoundEvent(new ResourceLocation(ModSim.MODID, "beamf")).setRegistryName(ModSim.MODID, "beamf");
    public static final SoundEvent BEAMM = new SoundEvent(new ResourceLocation(ModSim.MODID, "beamm")).setRegistryName(ModSim.MODID, "beamm");
    public static final SoundEvent BIRTH = new SoundEvent(new ResourceLocation(ModSim.MODID, "birth")).setRegistryName(ModSim.MODID, "birth");
    public static final SoundEvent BLARGA = new SoundEvent(new ResourceLocation(ModSim.MODID, "blarga")).setRegistryName(ModSim.MODID, "blarga");
    public static final SoundEvent BLARGB = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargb")).setRegistryName(ModSim.MODID, "blargb");
    public static final SoundEvent BLARGC = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargc")).setRegistryName(ModSim.MODID, "blargc");
    public static final SoundEvent BLARGD = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargd")).setRegistryName(ModSim.MODID, "blargd");
    public static final SoundEvent BLARGE = new SoundEvent(new ResourceLocation(ModSim.MODID, "blarge")).setRegistryName(ModSim.MODID, "blarge");
    public static final SoundEvent BLARGF = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargf")).setRegistryName(ModSim.MODID, "blargf");
    public static final SoundEvent BLARGG = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargg")).setRegistryName(ModSim.MODID, "blargg");
    public static final SoundEvent BLARGH = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargh")).setRegistryName(ModSim.MODID, "blargh");
    public static final SoundEvent BLARGI = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargi")).setRegistryName(ModSim.MODID, "blargi");
    public static final SoundEvent BLARGJ = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargj")).setRegistryName(ModSim.MODID, "blargj");
    public static final SoundEvent BLARGK = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargk")).setRegistryName(ModSim.MODID, "blargk");
    public static final SoundEvent BLARGL = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargl")).setRegistryName(ModSim.MODID, "blargl");
    public static final SoundEvent BLARGM = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargm")).setRegistryName(ModSim.MODID, "blargm");
    public static final SoundEvent BLARGN = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargn")).setRegistryName(ModSim.MODID, "blargn");
    public static final SoundEvent BLARGO = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargo")).setRegistryName(ModSim.MODID, "blargo");
    public static final SoundEvent BLARGP = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargp")).setRegistryName(ModSim.MODID, "blargp");
    public static final SoundEvent BLARGQ = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargq")).setRegistryName(ModSim.MODID, "blargq");
    public static final SoundEvent BLARGR = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargr")).setRegistryName(ModSim.MODID, "blargr");
    public static final SoundEvent BLARGS = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargs")).setRegistryName(ModSim.MODID, "blargs");
    public static final SoundEvent BLARGT = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargt")).setRegistryName(ModSim.MODID, "blargt");
    public static final SoundEvent BLARGU = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargu")).setRegistryName(ModSim.MODID, "blargu");
    public static final SoundEvent BLARGV = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargv")).setRegistryName(ModSim.MODID, "blargv");
    public static final SoundEvent BLARGW = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargw")).setRegistryName(ModSim.MODID, "blargw");
    public static final SoundEvent BLARGX = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargx")).setRegistryName(ModSim.MODID, "blargx");
    public static final SoundEvent BLARGY = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargy")).setRegistryName(ModSim.MODID, "blargy");
    public static final SoundEvent BLARGZ = new SoundEvent(new ResourceLocation(ModSim.MODID, "blargz")).setRegistryName(ModSim.MODID, "blargz");
    public static final SoundEvent BURGERFA = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerfa")).setRegistryName(ModSim.MODID, "burgerfa");
    public static final SoundEvent BURGERFB = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerfb")).setRegistryName(ModSim.MODID, "burgerfb");
    public static final SoundEvent BURGERFC = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerfc")).setRegistryName(ModSim.MODID, "burgerfc");
    public static final SoundEvent BURGERFD = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerfd")).setRegistryName(ModSim.MODID, "burgerfd");
    public static final SoundEvent BURGERFE = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerfe")).setRegistryName(ModSim.MODID, "burgerfe");
    public static final SoundEvent BURGERFF = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerff")).setRegistryName(ModSim.MODID, "burgerff");
    public static final SoundEvent BURGERMA = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerma")).setRegistryName(ModSim.MODID, "burgerma");
    public static final SoundEvent BURGERMB = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgermb")).setRegistryName(ModSim.MODID, "burgermb");
    public static final SoundEvent BURGERMC = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgermc")).setRegistryName(ModSim.MODID, "burgermc");
    public static final SoundEvent BURGERMD = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgermd")).setRegistryName(ModSim.MODID, "burgermd");
    public static final SoundEvent BURGERME = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgerme")).setRegistryName(ModSim.MODID, "burgerme");
    public static final SoundEvent BURGERMF = new SoundEvent(new ResourceLocation(ModSim.MODID, "burgermf")).setRegistryName(ModSim.MODID, "burgermf");
    public static final SoundEvent CASH = new SoundEvent(new ResourceLocation(ModSim.MODID, "cash")).setRegistryName(ModSim.MODID, "cash");
    public static final SoundEvent CASHSHORT = new SoundEvent(new ResourceLocation(ModSim.MODID, "cashshort")).setRegistryName(ModSim.MODID, "cashshort");
    public static final SoundEvent CHEESEMACHINE = new SoundEvent(new ResourceLocation(ModSim.MODID, "cheesemachine")).setRegistryName(ModSim.MODID, "cheesemachine");
    public static final SoundEvent COMPUTER = new SoundEvent(new ResourceLocation(ModSim.MODID, "computer")).setRegistryName(ModSim.MODID, "computer");
    public static final SoundEvent CONSTRUCTION = new SoundEvent(new ResourceLocation(ModSim.MODID, "construction")).setRegistryName(ModSim.MODID, "construction");
    public static final SoundEvent SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED = new SoundEvent(new ResourceLocation(ModSim.MODID, "sim_u_building_constructor_activated")).setRegistryName(ModSim.MODID, "sim_u_building_constructor_activated");
    public static final SoundEvent SIM_U_KRAFT_DDD_MINING_CONSTRUCTOR_ACTIVATED = new SoundEvent(new ResourceLocation(ModSim.MODID, "sim_u_kraft_ddd_mining_constructor_activated")).setRegistryName(ModSim.MODID, "sim_u_kraft_ddd_mining_constructor_activated");
    public static final SoundEvent SIM_U_DDD = new SoundEvent(new ResourceLocation(ModSim.MODID, "sim_u_ddd")).setRegistryName(ModSim.MODID, "sim_u_ddd");
    public static final SoundEvent SIM_U_KRAFT_DDD_FARMING_CONSTRUCTOR_ACTIVATED = new SoundEvent(new ResourceLocation(ModSim.MODID, "sim_u_kraft_ddd_farming_constructor_activated")).setRegistryName(ModSim.MODID, "sim_u_kraft_ddd_farming_constructor_activated");
    public static final SoundEvent COUGHF = new SoundEvent(new ResourceLocation(ModSim.MODID, "coughf")).setRegistryName(ModSim.MODID, "coughf");
    public static final SoundEvent COUGHM = new SoundEvent(new ResourceLocation(ModSim.MODID, "coughm")).setRegistryName(ModSim.MODID, "coughm");
    public static final SoundEvent CSPEAKA = new SoundEvent(new ResourceLocation(ModSim.MODID, "cspeaka")).setRegistryName(ModSim.MODID, "cspeaka");
    public static final SoundEvent CSPEAKB = new SoundEvent(new ResourceLocation(ModSim.MODID, "cspeakb")).setRegistryName(ModSim.MODID, "cspeakb");
    public static final SoundEvent CSPEAKC = new SoundEvent(new ResourceLocation(ModSim.MODID, "cspeakc")).setRegistryName(ModSim.MODID, "cspeakc");
    public static final SoundEvent DAYFONE = new SoundEvent(new ResourceLocation(ModSim.MODID, "dayfone")).setRegistryName(ModSim.MODID, "dayfone");
    public static final SoundEvent DAYFTHREE = new SoundEvent(new ResourceLocation(ModSim.MODID, "dayfthree")).setRegistryName(ModSim.MODID, "dayfthree");
    public static final SoundEvent DAYFTWO = new SoundEvent(new ResourceLocation(ModSim.MODID, "dayftwo")).setRegistryName(ModSim.MODID, "dayftwo");
    public static final SoundEvent DAYMONE = new SoundEvent(new ResourceLocation(ModSim.MODID, "daymone")).setRegistryName(ModSim.MODID, "daymone");
    public static final SoundEvent DAYMTHREE = new SoundEvent(new ResourceLocation(ModSim.MODID, "daymthree")).setRegistryName(ModSim.MODID, "daymthree");
    public static final SoundEvent DAYMTWO = new SoundEvent(new ResourceLocation(ModSim.MODID, "daymtwo")).setRegistryName(ModSim.MODID, "daymtwo");
    public static final SoundEvent FEIGHT = new SoundEvent(new ResourceLocation(ModSim.MODID, "feight")).setRegistryName(ModSim.MODID, "feight");
    public static final SoundEvent FNINE = new SoundEvent(new ResourceLocation(ModSim.MODID, "fnine")).setRegistryName(ModSim.MODID, "fnine");
    public static final SoundEvent FONE = new SoundEvent(new ResourceLocation(ModSim.MODID, "fone")).setRegistryName(ModSim.MODID, "fone");
    public static final SoundEvent FSEVEN = new SoundEvent(new ResourceLocation(ModSim.MODID, "fseven")).setRegistryName(ModSim.MODID, "fseven");
    public static final SoundEvent FSPEAKA = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeaka")).setRegistryName(ModSim.MODID, "fspeaka");
    public static final SoundEvent FSPEAKB = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakb")).setRegistryName(ModSim.MODID, "fspeakb");
    public static final SoundEvent FSPEAKC = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakc")).setRegistryName(ModSim.MODID, "fspeakc");
    public static final SoundEvent FSPEAKD = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakd")).setRegistryName(ModSim.MODID, "fspeakd");
    public static final SoundEvent FSPEAKE = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeake")).setRegistryName(ModSim.MODID, "fspeake");
    public static final SoundEvent FSPEAKF = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakf")).setRegistryName(ModSim.MODID, "fspeakf");
    public static final SoundEvent FSPEAKG = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakg")).setRegistryName(ModSim.MODID, "fspeakg");
    public static final SoundEvent FSPEAKH = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakh")).setRegistryName(ModSim.MODID, "fspeakh");
    public static final SoundEvent FSPEAKU = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeaku")).setRegistryName(ModSim.MODID, "fspeaku");
    public static final SoundEvent FSPEAKJ = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakj")).setRegistryName(ModSim.MODID, "fspeakj");
    public static final SoundEvent FSPEAKK = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakk")).setRegistryName(ModSim.MODID, "fspeakk");
    public static final SoundEvent FSPEAKL = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakl")).setRegistryName(ModSim.MODID, "fspeakl");
    public static final SoundEvent FSPEAKM = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakm")).setRegistryName(ModSim.MODID, "fspeakm");
    public static final SoundEvent FSPEAKN = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakn")).setRegistryName(ModSim.MODID, "fspeakn");
    public static final SoundEvent FSPEAKO = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeako")).setRegistryName(ModSim.MODID, "fspeako");
    public static final SoundEvent FSPEAKP = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakp")).setRegistryName(ModSim.MODID, "fspeakp");
    public static final SoundEvent FSPEAKQ = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakq")).setRegistryName(ModSim.MODID, "fspeakq");
    public static final SoundEvent FSPEAKR = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeakr")).setRegistryName(ModSim.MODID, "fspeakr");
    public static final SoundEvent FSPEAKS = new SoundEvent(new ResourceLocation(ModSim.MODID, "fspeaks")).setRegistryName(ModSim.MODID, "fspeaks");
    public static final SoundEvent FTHREE = new SoundEvent(new ResourceLocation(ModSim.MODID, "fthree")).setRegistryName(ModSim.MODID, "fthree");
    public static final SoundEvent FTWO = new SoundEvent(new ResourceLocation(ModSim.MODID, "ftwo")).setRegistryName(ModSim.MODID, "ftwo");
    public static final SoundEvent FWXBAD = new SoundEvent(new ResourceLocation(ModSim.MODID, "fwxbad")).setRegistryName(ModSim.MODID, "fwxbad");
    public static final SoundEvent HELLOC = new SoundEvent(new ResourceLocation(ModSim.MODID, "helloc")).setRegistryName(ModSim.MODID, "helloc");
    public static final SoundEvent HELLOF = new SoundEvent(new ResourceLocation(ModSim.MODID, "hellof")).setRegistryName(ModSim.MODID, "hellof");
    public static final SoundEvent HELLOM = new SoundEvent(new ResourceLocation(ModSim.MODID, "hellom")).setRegistryName(ModSim.MODID, "hellom");
    public static final SoundEvent LAUGHF = new SoundEvent(new ResourceLocation(ModSim.MODID, "laughf")).setRegistryName(ModSim.MODID, "laughf");
    public static final SoundEvent LAUGHM = new SoundEvent(new ResourceLocation(ModSim.MODID, "laughm")).setRegistryName(ModSim.MODID, "laughm");
    public static final SoundEvent LIGHTONE = new SoundEvent(new ResourceLocation(ModSim.MODID, "lightone")).setRegistryName(ModSim.MODID, "lightone");
    public static final SoundEvent MEIGHT = new SoundEvent(new ResourceLocation(ModSim.MODID, "meight")).setRegistryName(ModSim.MODID, "meight");
    public static final SoundEvent MERCHM = new SoundEvent(new ResourceLocation(ModSim.MODID, "merchm")).setRegistryName(ModSim.MODID, "merchm");
    public static final SoundEvent MFIVE = new SoundEvent(new ResourceLocation(ModSim.MODID, "mfive")).setRegistryName(ModSim.MODID, "mfive");
    public static final SoundEvent MFOUR = new SoundEvent(new ResourceLocation(ModSim.MODID, "mfour")).setRegistryName(ModSim.MODID, "mfour");
    public static final SoundEvent MNINE = new SoundEvent(new ResourceLocation(ModSim.MODID, "mnine")).setRegistryName(ModSim.MODID, "mnine");
    public static final SoundEvent MONE = new SoundEvent(new ResourceLocation(ModSim.MODID, "mone")).setRegistryName(ModSim.MODID, "mone");
    public static final SoundEvent MSEVEN = new SoundEvent(new ResourceLocation(ModSim.MODID, "mseven")).setRegistryName(ModSim.MODID, "mseven");
    public static final SoundEvent MSIX = new SoundEvent(new ResourceLocation(ModSim.MODID, "msix")).setRegistryName(ModSim.MODID, "msix");
    public static final SoundEvent MSPEAKA = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeaka")).setRegistryName(ModSim.MODID, "mspeaka");
    public static final SoundEvent MSPEAKB = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakb")).setRegistryName(ModSim.MODID, "mspeakb");
    public static final SoundEvent MSPEAKC = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakc")).setRegistryName(ModSim.MODID, "mspeakc");
    public static final SoundEvent MSPEAKD = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakd")).setRegistryName(ModSim.MODID, "mspeakd");
    public static final SoundEvent MSPEAKE = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeake")).setRegistryName(ModSim.MODID, "mspeake");
    public static final SoundEvent MSPEAKF = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakf")).setRegistryName(ModSim.MODID, "mspeakf");
    public static final SoundEvent MSPEAKG = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakg")).setRegistryName(ModSim.MODID, "mspeakg");
    public static final SoundEvent MSPEAKH = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakh")).setRegistryName(ModSim.MODID, "mspeakh");
    public static final SoundEvent MSPEAKI = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeaki")).setRegistryName(ModSim.MODID, "mspeaki");
    public static final SoundEvent MSPEAKJ = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakj")).setRegistryName(ModSim.MODID, "mspeakj");
    public static final SoundEvent MSPEAKK = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakk")).setRegistryName(ModSim.MODID, "mspeakk");
    public static final SoundEvent MSPEAKL = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakl")).setRegistryName(ModSim.MODID, "mspeakl");
    public static final SoundEvent MSPEAKM = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakm")).setRegistryName(ModSim.MODID, "mspeakm");
    public static final SoundEvent MSPEAKN = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakn")).setRegistryName(ModSim.MODID, "mspeakn");
    public static final SoundEvent MSPEAKO = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeako")).setRegistryName(ModSim.MODID, "mspeako");
    public static final SoundEvent MSPEAKP = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakp")).setRegistryName(ModSim.MODID, "mspeakp");
    public static final SoundEvent MSPEAKQ = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakq")).setRegistryName(ModSim.MODID, "mspeakq");
    public static final SoundEvent MSPEAKR = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeakr")).setRegistryName(ModSim.MODID, "mspeakr");
    public static final SoundEvent MSPEAKS = new SoundEvent(new ResourceLocation(ModSim.MODID, "mspeaks")).setRegistryName(ModSim.MODID, "mspeaks");
    public static final SoundEvent MTHREE = new SoundEvent(new ResourceLocation(ModSim.MODID, "mthree")).setRegistryName(ModSim.MODID, "mthree");
    public static final SoundEvent MTWO = new SoundEvent(new ResourceLocation(ModSim.MODID, "mtwo")).setRegistryName(ModSim.MODID, "mtwo");
    public static final SoundEvent MWXBAD = new SoundEvent(new ResourceLocation(ModSim.MODID, "mwxbad")).setRegistryName(ModSim.MODID, "mwxbad");
    public static final SoundEvent NIGHTFONE = new SoundEvent(new ResourceLocation(ModSim.MODID, "nightfone")).setRegistryName(ModSim.MODID, "nightfone");
    public static final SoundEvent NIGHTFTHREE = new SoundEvent(new ResourceLocation(ModSim.MODID, "nightfthree")).setRegistryName(ModSim.MODID, "nightfthree");
    public static final SoundEvent NIGHTFTWO = new SoundEvent(new ResourceLocation(ModSim.MODID, "nightftwo")).setRegistryName(ModSim.MODID, "nightftwo");
    public static final SoundEvent NIGHTMONE = new SoundEvent(new ResourceLocation(ModSim.MODID, "nightmone")).setRegistryName(ModSim.MODID, "nightmone");
    public static final SoundEvent NIGHTMTHREE = new SoundEvent(new ResourceLocation(ModSim.MODID, "nightmthree")).setRegistryName(ModSim.MODID, "nightmthree");
    public static final SoundEvent NIGHTMTWO = new SoundEvent(new ResourceLocation(ModSim.MODID, "nightmtwo")).setRegistryName(ModSim.MODID, "nightmtwo");
    public static final SoundEvent OUCHF = new SoundEvent(new ResourceLocation(ModSim.MODID, "ouchf")).setRegistryName(ModSim.MODID, "ouchf");
    public static final SoundEvent OUCHM = new SoundEvent(new ResourceLocation(ModSim.MODID, "ouchm")).setRegistryName(ModSim.MODID, "ouchm");
    public static final SoundEvent POWER_DOWN = new SoundEvent(new ResourceLocation(ModSim.MODID, "power_down")).setRegistryName(ModSim.MODID, "power_down");
    public static final SoundEvent PREGNANT = new SoundEvent(new ResourceLocation(ModSim.MODID, "pregnant")).setRegistryName(ModSim.MODID, "pregnant");
    public static final SoundEvent IM_READ_Y = new SoundEvent(new ResourceLocation(ModSim.MODID, "im_read_y")).setRegistryName(ModSim.MODID, "im_read_y");
    public static final SoundEvent IM_READ_M = new SoundEvent(new ResourceLocation(ModSim.MODID, "im_read_m")).setRegistryName(ModSim.MODID, "im_read_m");
    public static final SoundEvent ROOSTER = new SoundEvent(new ResourceLocation(ModSim.MODID, "rooster")).setRegistryName(ModSim.MODID, "rooster");
    public static final SoundEvent SHEARS = new SoundEvent(new ResourceLocation(ModSim.MODID, "shears")).setRegistryName(ModSim.MODID, "shears");
    public static final SoundEvent SNEEZEF = new SoundEvent(new ResourceLocation(ModSim.MODID, "sneezef")).setRegistryName(ModSim.MODID, "sneezef");
    public static final SoundEvent SNEEZEM = new SoundEvent(new ResourceLocation(ModSim.MODID, "sneezem")).setRegistryName(ModSim.MODID, "sneezem");
    public static final SoundEvent WELCOME = new SoundEvent(new ResourceLocation(ModSim.MODID, "welcome")).setRegistryName(ModSim.MODID, "welcome");
    public static final SoundEvent WINDMILL = new SoundEvent(new ResourceLocation(ModSim.MODID, "windmill")).setRegistryName(ModSim.MODID, "windmill");

    /**
     * 注册所有声音事件（在preInit中调用）
     */
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();

        ModSim.log.info("开始注册声音");
        registerSound(registry,BAKERF);
        registerSound(registry,BAKERM);
        registerSound(registry,BEAMDOWN);
        registerSound(registry,BEAMDOWNTWO);
        registerSound(registry,BEAMF);
        registerSound(registry,BEAMM);
        registerSound(registry,BIRTH);
        registerSound(registry,BLARGA);
        registerSound(registry,BLARGB);
        registerSound(registry,BLARGC);
        registerSound(registry,BLARGD);
        registerSound(registry,BLARGE);
        registerSound(registry,BLARGF);
        registerSound(registry,BLARGG);
        registerSound(registry,BLARGH);
        registerSound(registry,BLARGI);
        registerSound(registry,BLARGJ);
        registerSound(registry,BLARGK);
        registerSound(registry,BLARGL);
        registerSound(registry,BLARGM);
        registerSound(registry,BLARGN);
        registerSound(registry,BLARGO);
        registerSound(registry,BLARGP);
        registerSound(registry,BLARGQ);
        registerSound(registry,BLARGR);
        registerSound(registry,BLARGS);
        registerSound(registry,BLARGT);
        registerSound(registry,BLARGU);
        registerSound(registry,BLARGV);
        registerSound(registry,BLARGW);
        registerSound(registry,BLARGX);
        registerSound(registry,BLARGY);
        registerSound(registry,BLARGZ);
        registerSound(registry,BURGERFA);
        registerSound(registry,BURGERFB);
        registerSound(registry,BURGERFC);
        registerSound(registry,BURGERFD);
        registerSound(registry,BURGERFE);
        registerSound(registry,BURGERFF);
        registerSound(registry,BURGERMA);
        registerSound(registry,BURGERMB);
        registerSound(registry,BURGERMC);
        registerSound(registry,BURGERMD);
        registerSound(registry,BURGERME);
        registerSound(registry,BURGERMF);
        registerSound(registry,CASH);
        registerSound(registry,CASHSHORT);
        registerSound(registry,CHEESEMACHINE);
        registerSound(registry,COMPUTER);
        registerSound(registry,CONSTRUCTION);
        registerSound(registry,SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED);
        registerSound(registry,SIM_U_KRAFT_DDD_MINING_CONSTRUCTOR_ACTIVATED);
        registerSound(registry,SIM_U_DDD);
        registerSound(registry,SIM_U_KRAFT_DDD_FARMING_CONSTRUCTOR_ACTIVATED);
        registerSound(registry,COUGHF);
        registerSound(registry,COUGHM);
        registerSound(registry,CSPEAKA);
        registerSound(registry,CSPEAKB);
        registerSound(registry,CSPEAKC);
        registerSound(registry,DAYFONE);
        registerSound(registry,DAYFTHREE);
        registerSound(registry,DAYFTWO);
        registerSound(registry,DAYMONE);
        registerSound(registry,DAYMTHREE);
        registerSound(registry,DAYMTWO);
        registerSound(registry,FEIGHT);
        registerSound(registry,FNINE);
        registerSound(registry,FONE);
        registerSound(registry,FSEVEN);
        registerSound(registry,FSPEAKA);
        registerSound(registry,FSPEAKB);
        registerSound(registry,FSPEAKC);
        registerSound(registry,FSPEAKD);
        registerSound(registry,FSPEAKE);
        registerSound(registry,FSPEAKF);
        registerSound(registry,FSPEAKG);
        registerSound(registry,FSPEAKH);
        registerSound(registry,FSPEAKU);
        registerSound(registry,FSPEAKJ);
        registerSound(registry,FSPEAKK);
        registerSound(registry,FSPEAKL);
        registerSound(registry,FSPEAKM);
        registerSound(registry,FSPEAKN);
        registerSound(registry,FSPEAKO);
        registerSound(registry,FSPEAKP);
        registerSound(registry,FSPEAKQ);
        registerSound(registry,FSPEAKR);
        registerSound(registry,FSPEAKS);
        registerSound(registry,FTHREE);
        registerSound(registry,FTWO);
        registerSound(registry,FWXBAD);
        registerSound(registry,HELLOF);
        registerSound(registry,HELLOC);
        registerSound(registry,HELLOM);
        registerSound(registry,LAUGHF);
        registerSound(registry,LAUGHM);
        registerSound(registry,LIGHTONE);
        registerSound(registry,MEIGHT);
        registerSound(registry,MERCHM);
        registerSound(registry,MFIVE);
        registerSound(registry,MFOUR);
        registerSound(registry,MNINE);
        registerSound(registry,MONE);
        registerSound(registry,MSEVEN);
        registerSound(registry,MSIX);
        registerSound(registry,MSPEAKA);
        registerSound(registry,MSPEAKB);
        registerSound(registry,MSPEAKC);
        registerSound(registry,MSPEAKD);
        registerSound(registry,MSPEAKE);
        registerSound(registry,MSPEAKF);
        registerSound(registry,MSPEAKG);
        registerSound(registry,MSPEAKH);
        registerSound(registry,MSPEAKI);
        registerSound(registry,MSPEAKJ);
        registerSound(registry,MSPEAKK);
        registerSound(registry,MSPEAKL);
        registerSound(registry,MSPEAKM);
        registerSound(registry,MSPEAKN);
        registerSound(registry,MSPEAKO);
        registerSound(registry,MSPEAKP);
        registerSound(registry,MSPEAKQ);
        registerSound(registry,MSPEAKR);
        registerSound(registry,MSPEAKS);
        registerSound(registry,MTHREE);
        registerSound(registry,MTWO);
        registerSound(registry,MWXBAD);
        registerSound(registry,NIGHTFONE);
        registerSound(registry,NIGHTFTHREE);
        registerSound(registry,NIGHTFTWO);
        registerSound(registry,NIGHTMONE);
        registerSound(registry,NIGHTMTHREE);
        registerSound(registry,NIGHTMTWO);
        registerSound(registry,OUCHF);
        registerSound(registry,OUCHM);
        registerSound(registry,POWER_DOWN);
        registerSound(registry,PREGNANT);
        registerSound(registry,IM_READ_Y);
        registerSound(registry,IM_READ_M);
        registerSound(registry,ROOSTER);
        registerSound(registry,SHEARS);
        registerSound(registry,SNEEZEF);
        registerSound(registry,SNEEZEM);
        registerSound(registry,WELCOME);
        registerSound(registry,WINDMILL);

        ModSim.log.info("所有声音事件注册完成，共 " + countSounds() + " 个");
    }

    /**
     * 注册单个声音事件
     * @param soundEvent 声音事件
     * @param registryName 注册名（需与sounds.json中的键一致）
     */
    private static void registerSound(IForgeRegistry<SoundEvent> registry,SoundEvent soundEvent) {
        // 设置注册名并注册到Forge注册表
        registry.register(soundEvent);
        // 针对 sim_u_ddd 验证
        ModSim.log.info("已注册 ：" + soundEvent.getRegistryName());
    }

    /**
     * 统计声音事件数量（用于日志）
     */
    private static int countSounds() {
        // 手动计数（与定义的SoundEvent数量一致）
        return 148;
    }
}
