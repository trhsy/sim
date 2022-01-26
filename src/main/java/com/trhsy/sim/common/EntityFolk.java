package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import javafx.stage.Stage;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.*;

/**
 * ========================================
 *
 * @ClassName EntityFolk
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:53
 * ========================================
 **/
public class EntityFolk extends EntityCreature implements INpc {
    public FolkData theData = null;
    private long ghostTimer = -1L;
    private long greetTimer = 0L;
    private long lastHurt = 0L;
    private Calendar cal = new GregorianCalendar();
    private boolean isXmas = false;
    public boolean gotPath;

    public EntityFolk(World par1World) {
        super(par1World);
        this.func_70661_as().func_75491_a(false);
        this.func_70661_as().func_75490_c(true);
        this.func_70661_as().func_75498_b(true);
        this.func_70661_as().func_75495_e(true);
        this.field_70714_bg.func_75776_a(1, new EntityAILookIdle(this));
        this.field_70714_bg.func_75776_a(2, new EntityAIMoveIndoors(this));
        this.field_70714_bg.func_75776_a(3, new EntityAIRestrictOpenDoor(this));
        this.field_70714_bg.func_75776_a(10, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.field_70714_bg.func_75776_a(9, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.field_70714_bg.func_75776_a(9, new EntityAIOpenDoor(this, true));
        this.field_70714_bg.func_75776_a(5, new EntityAIMoveTowardsRestriction(this, 0.3D));
        this.field_70714_bg.func_75776_a(4, new EntityAISwimming(this));
        if (!ModSimukraft.proxy.ranStartup) {
            ModSimukraft.log.info("EntityFolk: Killed system spawned folk");
            this.func_70106_y();
        }

        int dom = this.cal.get(5);
        int moy = this.cal.get(2);
        if (dom > 23 && dom < 27 && moy == 11) {
            this.isXmas = true;
        }

    }

    @SideOnly(Side.CLIENT)
    public String getTexture() {
        if (this.theData != null) {
            if (this.theData.gender == 0) {
                return this.isXmas ? "MrSanta.png" : "male" + this.theData.skinnumber + ".png";
            } else {
                return this.isXmas ? "MrsSanta.png" : "female" + this.theData.skinnumber + ".png";
            }
        } else {
            return "male1.png";
        }
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.0D);
    }

    public void func_70071_h_() {
        if (this.theData == null) {
            if (!this.field_70128_L) {
                if (this.ghostTimer == -1L) {
                    this.ghostTimer = System.currentTimeMillis();
                }

                this.theData = FolkData.getFolkDataByEntityId(this.func_145782_y());
                if (this.theData == null && System.currentTimeMillis() - this.ghostTimer > 5000L) {
                    ModSimukraft.log.info("EntityFolk: " + this.func_145782_y() + " - their data has been null for more than 5s, so killing");
                    this.func_70106_y();
                }
            }
        } else {
            if (this.theData.isWorking) {
                float s = (float)(Math.sin((double)System.currentTimeMillis() * 0.01D) / 10.0D) + 0.1F;
                this.field_70733_aJ = s;
            } else {
                this.field_70733_aJ = 0.0F;
            }

            if (System.currentTimeMillis() - this.greetTimer > 1000L) {
                Random r = new Random();
                double dist = (double)this.theData.getDistanceToPlayer();
                if (ModSimukraft.states != null) {
                    long var10000 = System.currentTimeMillis();
                    FolkData var10001 = this.theData;
                    Long ls = var10000 - FolkData.anyFolkLastSpoke;
                    if (ModSimukraft.configFolkTalkingEnglish && ls > 5000L && (!this.theData.greetedToday & dist < 5.0D || this.theData.vocation == Vocation.BURGERSWAITER && dist < 5.0D && r.nextInt(20) == 2)) {
                        this.theData.greetedToday = true;
                        FolkData var18 = this.theData;
                        FolkData.anyFolkLastSpoke = System.currentTimeMillis();
                        int sf = r.nextInt(25) + 1;
                        String fn = "satscapesimukraft:";
                        if (this.theData.vocation != null && this.theData.vocation == Vocation.BURGERSWAITER) {
                            sf = r.nextInt(6);
                            fn = fn + "burger";
                            switch(sf) {
                                case 0:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "ma";
                                    } else {
                                        fn = fn + "fa";
                                    }
                                    break;
                                case 1:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mb";
                                    } else {
                                        fn = fn + "fb";
                                    }
                                    break;
                                case 2:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mc";
                                    } else {
                                        fn = fn + "fc";
                                    }
                                    break;
                                case 3:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "md";
                                    } else {
                                        fn = fn + "fd";
                                    }
                                    break;
                                case 4:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "me";
                                    } else {
                                        fn = fn + "fe";
                                    }
                                    break;
                                case 5:
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mf";
                                    } else {
                                        fn = fn + "ff";
                                    }
                            }
                        } else if (this.theData.age >= 18) {
                            if (ModSimukraft.isDayTime()) {
                                if (sf == 1) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "daymone";
                                    } else {
                                        fn = fn + "dayfone";
                                    }
                                } else if (sf == 2) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "daymtwo";
                                    } else {
                                        fn = fn + "dayftwo";
                                    }
                                } else if (sf == 3) {
                                    if (!this.field_70170_p.func_72896_J()) {
                                        if (this.theData.gender == 0) {
                                            fn = fn + "daymthree";
                                        } else {
                                            fn = fn + "dayfthree";
                                        }
                                    } else if (this.theData.gender == 0) {
                                        fn = fn + "mwxbad";
                                    } else {
                                        fn = fn + "fwxbad";
                                    }
                                } else if (sf == 4) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "meight";
                                    } else {
                                        fn = fn + "feight";
                                    }
                                } else if (sf == 5) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mnine";
                                    } else {
                                        fn = fn + "fnine";
                                    }
                                } else if (sf == 6) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mseven";
                                    } else {
                                        fn = fn + "fseven";
                                    }
                                } else if (sf > 6) {
                                    if (this.theData.gender == 0) {
                                        fn = fn + "mspeak";
                                    } else {
                                        fn = fn + "fspeak";
                                    }

                                    fn = fn + Character.toString((char)(sf + 90));
                                }
                            } else if (sf == 1) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "nightmone";
                                } else {
                                    fn = fn + "nightfone";
                                }
                            } else if (sf == 2) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "nightmtwo";
                                } else {
                                    fn = fn + "nightftwo";
                                }
                            } else if (sf == 3) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "nightmthree";
                                } else {
                                    fn = fn + "nightfthree";
                                }
                            } else if (sf == 4) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "meight";
                                } else {
                                    fn = fn + "feight";
                                }
                            } else if (sf == 5) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "mnine";
                                } else {
                                    fn = fn + "fnine";
                                }
                            } else if (sf == 6) {
                                if (this.theData.gender == 0) {
                                    fn = fn + "mseven";
                                } else {
                                    fn = fn + "fseven";
                                }
                            }
                        } else {
                            sf = r.nextInt(3) + 1;
                            fn = fn + "cspeak";
                            fn = fn + Character.toString((char)(sf + 96));
                        }

                        if (r.nextBoolean()) {
                            try {
                                ModSimukraft.proxy.getClientWorld().func_72980_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, fn, 1.0F, 1.0F, false);
                            } catch (Exception var12) {
                            }
                        }
                    }
                }

                try {
                    if (this.theData.levelFood < 0) {
                        this.func_70645_a(DamageSource.field_76366_f);
                    }
                } catch (Exception var11) {
                }

                this.greetTimer = System.currentTimeMillis();
            }
        }

        List list1 = this.field_70170_p.func_72839_b(this, AxisAlignedBB.func_72330_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70165_t + 1.0D, this.field_70163_u + 1.0D, this.field_70161_v + 1.0D).func_72314_b(2.0D, 4.0D, 2.0D));
        Iterator iterator1 = list1.iterator();
        if (!list1.isEmpty()) {
            while(iterator1.hasNext()) {
                Entity entity1 = (Entity)iterator1.next();
                if (entity1 instanceof EntityItem) {
                    EntityItem entityitem = (EntityItem)entity1;
                    ItemStack is = entityitem.func_92059_d();

                    try {
                        ItemFood food = (ItemFood)is.func_77973_b();
                        if (this.theData.levelFood < 10 && food != null) {
                            this.field_70170_p.func_72956_a(this, "random.burp", 1.0F, 1.0F);
                            entityitem.func_70106_y();
                            ++this.theData.levelFood;
                        }
                    } catch (Exception var10) {
                    }
                } else if (entity1 instanceof EntityFolk && (int)this.field_70165_t == (int)entity1.field_70165_t && (int)this.field_70161_v == (int)entity1.field_70161_v) {
                    this.field_70159_w += 0.10000000149011612D;

                    try {
                        this.theData.stayPut = false;
                    } catch (Exception var9) {
                    }
                }
            }
        }

        try {
            super.func_70071_h_();
        } catch (Exception var8) {
        }

    }

    public void func_70091_d(double d, double d1, double d2) {
        if (!this.field_70128_L && this.theData != null) {
            double dist = 0.0D;
            if (this.theData.destination != null && this.theData.beamingTo == null) {
                try {
                    dist = this.func_70011_f(this.theData.destination.x, this.theData.destination.y, this.theData.destination.z);
                } catch (Exception var14) {
                    ModSimukraft.log.warning("Folk's theData.destination was null in moveEntity()");
                    return;
                }

                if (dist <= 2.0D) {
                    try {
                        ModSimukraft.log.info("EntityFolk: " + this.theData.name + " has arrived at " + this.theData.destination.toString() + " Dim:" + this.theData.destination.theDimension);
                    } catch (Exception var13) {
                    }

                    this.theData.updateLocationFromEntity();
                    this.field_70159_w = 0.0D;
                    this.field_70179_y = 0.0D;
                    this.theData.stayPut = true;
                    this.theData.destination = null;
                    this.func_70661_as().func_75499_g();
                    this.gotPath = false;
                    if (this.theData.actionArrival != null) {
                        this.theData.action = this.theData.actionArrival;
                        this.theData.actionArrival = null;
                    }
                } else {
                    try {
                        if (!this.gotPath) {
                            PathEntity path = this.field_70170_p.func_72844_a(this, this.theData.destination.x.intValue(), this.theData.destination.y.intValue(), this.theData.destination.z.intValue(), 40.0F, true, true, true, true);
                            if (path != null) {
                                this.func_70661_as().func_75484_a(path, 0.30000001192092896D);
                                this.gotPath = true;
                            }
                        }
                    } catch (Exception var12) {
                    }
                }

                boolean donttimeout = false;

                try {
                    donttimeout = this.theData.destination.doNotTimeout;
                } catch (Exception var11) {
                }

                if (this.theData.timeStartedGotoing != null && !donttimeout && System.currentTimeMillis() - this.theData.timeStartedGotoing > 40000L && this.theData.beamingTo == null) {
                    this.func_70661_as().func_75499_g();
                    if (dist > 2.0D) {
                        ModSimukraft.log.info("EntityFolk: " + this.theData.name + " took too long to walk, so beaming...");
                        this.theData.stayPut = true;
                        this.theData.timeStartedGotoing = System.currentTimeMillis();
                        this.theData.beamMeTo(this.theData.destination);
                    }
                }
            }

            if (this.theData.stayPut) {
                this.field_70159_w = 0.0D;
                this.field_70181_x = 0.0D;
                this.field_70179_y = 0.0D;
                this.func_70661_as().func_75499_g();
            } else {
                super.func_70091_d(d, d1, d2);
            }

        }
    }

    public ItemStack func_70694_bm() {
        if (this.theData == null) {
            return null;
        } else if (this.theData.theirJob == null) {
            return null;
        } else if (this.theData.vocation == Vocation.CROPFARMER) {
            return new ItemStack(Items.field_151018_J, 1);
        } else if (this.theData.vocation == Vocation.LUMBERJACK) {
            return new ItemStack(Items.field_151049_t, 1);
        } else if (this.theData.vocation == Vocation.MINER) {
            return new ItemStack(Items.field_151050_s, 1);
        } else if (this.theData.vocation == Vocation.BAKER) {
            return new ItemStack(Items.field_151038_n, 1);
        } else if (this.theData.vocation == Vocation.SOLDIER) {
            return new ItemStack(Items.field_151052_q, 1);
        } else if (this.theData.vocation == Vocation.BUILDER) {
            return new ItemStack(Blocks.field_150347_e, 1);
        } else if (this.theData.vocation == Vocation.SHEPHERD) {
            return new ItemStack(Items.field_151097_aZ, 1);
        } else if (this.theData.vocation == Vocation.GROCER) {
            return new ItemStack(Items.field_151127_ba, 1);
        } else if (this.theData.vocation == Vocation.COURIER) {
            return new ItemStack(Blocks.field_150486_ae, 1);
        } else if (this.theData.vocation == Vocation.MERCHANT) {
            return new ItemStack(Blocks.field_150336_V, 1);
        } else if (this.theData.vocation == Vocation.BUTCHER) {
            return new ItemStack(Items.field_151147_al, 1);
        } else if (this.theData.vocation == Vocation.CATTLEFARMER) {
            return new ItemStack(Items.field_151006_E, 1);
        } else if (this.theData.vocation == Vocation.PIGFARMER) {
            return new ItemStack(Items.field_151036_c, 1);
        } else if (this.theData.vocation == Vocation.CHICKENFARMER) {
            return new ItemStack(Items.field_151049_t, 1);
        } else if (this.theData.vocation == Vocation.TERRAFORMER) {
            return new ItemStack(Items.field_151047_v, 1);
        } else if (this.theData.vocation == Vocation.GLASSMAKER) {
            return new ItemStack(Blocks.field_150410_aZ, 1);
        } else if (this.theData.vocation == Vocation.DAIRYFARMER) {
            return new ItemStack(Items.field_151117_aB, 1);
        } else if (this.theData.vocation == Vocation.CHEESEMAKER) {
            return new ItemStack(ModSimukraft.blockCheese, 1);
        } else if (this.theData.vocation == Vocation.BURGERSMANAGER) {
            return new ItemStack(ModSimukraft.itemFood, 1, 3);
        } else if (this.theData.vocation == Vocation.BURGERSFRYCOOK) {
            return new ItemStack(Items.field_151037_a, 1);
        } else if (this.theData.vocation == Vocation.BURGERSWAITER) {
            return new ItemStack(ModSimukraft.itemFood, 1, 2);
        } else if (this.theData.vocation == Vocation.FISHERMAN) {
            JobFisherman jf = (JobFisherman)this.theData.theirJob;
            return jf.theStage == Stage.IDLE ? new ItemStack(Items.field_151115_aP, 1) : new ItemStack(Items.field_151112_aM, 1);
        } else {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean func_70085_c(EntityPlayer entityplayer) {
        Minecraft mc = Minecraft.func_71410_x();
        mc.field_71462_r = null;
        GuiScreen ui = null;
        if (this.theData == null) {
            this.func_70106_y();
            return false;
        } else {
            if (this.theData.theirJob != null) {
                if (this.theData.vocation == Vocation.MERCHANT && ModSimukraft.isDayTime()) {
                    ui = new GuiMerchant();
                } else {
                    ui = new GuiEntityFolk(this.theData, entityplayer);
                }
            } else {
                ui = new GuiEntityFolk(this.theData, entityplayer);
            }

            mc.func_147108_a((GuiScreen)ui);
            if (this.theData.age < 18) {
                this.field_70170_p.func_72980_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, "satscapesimukraft:helloc", 1.0F, 1.0F, false);
            } else if (this.theData.gender == 0) {
                this.field_70170_p.func_72980_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, "satscapesimukraft:hellom", 1.0F, 1.0F, false);
            } else {
                this.field_70170_p.func_72980_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, "satscapesimukraft:hellof", 1.0F, 1.0F, false);
            }

            return true;
        }
    }

    public void func_70645_a(DamageSource d) {
        this.theData.eventDied(d);
    }

    public boolean func_70104_M() {
        return true;
    }

    protected String func_70621_aR() {
        if (!this.func_70027_ad()) {
            this.func_70691_i(10.0F);
            if (this.theData != null && this.theData.stayPut) {
                this.theData.stayPut = false;
            }

            Block idX1 = this.field_70170_p.func_147439_a((int)this.field_70165_t + 1, (int)this.field_70163_u, (int)this.field_70161_v);
            Block idX2 = this.field_70170_p.func_147439_a((int)this.field_70165_t - 1, (int)this.field_70163_u, (int)this.field_70161_v);
            Block idZ1 = this.field_70170_p.func_147439_a((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v + 1);
            Block idZ2 = this.field_70170_p.func_147439_a((int)this.field_70165_t + 1, (int)this.field_70163_u, (int)this.field_70161_v - 1);
            this.field_70181_x += 0.4D;
            if (idX1 == null) {
                this.field_70159_w += 0.8999999761581421D;
            } else if (idX2 == null) {
                this.field_70159_w -= 0.8999999761581421D;
            } else if (idZ1 == null) {
                this.field_70179_y += 0.8999999761581421D;
            } else if (idZ2 == null) {
                this.field_70179_y -= 0.8999999761581421D;
            }
        }

        if (this.theData == null) {
            return null;
        } else if (ModSimukraft.configFolkTalking) {
            if (System.currentTimeMillis() - this.lastHurt > 10000L) {
                this.lastHurt = System.currentTimeMillis();
                return this.theData.gender == 0 ? "satscapesimukraft:OuchM" : "satscapesimukraft:OuchF";
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public int func_70627_aG() {
        Random r = new Random();
        return 1000 + r.nextInt(1000);
    }

    public boolean func_70650_aV() {
        return true;
    }

    public int func_70641_bl() {
        return 200;
    }

    public boolean func_70692_ba() {
        return true;
    }

    public AxisAlignedBB func_70114_g(Entity par1Entity) {
        return par1Entity.field_70121_D;
    }

    public AxisAlignedBB func_70046_E() {
        return this.field_70121_D;
    }

    public boolean func_70067_L() {
        return true;
    }

    public void onPlayerLogin(EntityPlayer player) {
    }

    public void onPlayerLogout(EntityPlayer player) {
    }

    public void onPlayerChangedDimension(EntityPlayer player) {
    }

    public void onPlayerRespawn(EntityPlayer player) {
    }
}
