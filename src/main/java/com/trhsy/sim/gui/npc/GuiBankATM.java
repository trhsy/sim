package com.trhsy.sim.gui.npc;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.gui.ATMscreen;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.network.client.PacketOpenBankATMGui;
import com.trhsy.sim.network.client.PacketOpenMerchantGui;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.util.Commodity;
import com.trhsy.sim.util.PricesForBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName GuiBankATM
 * @Description todo
 * @Author TRHSY
 * @Date 2023/7/1223:05
 **/
public class GuiBankATM extends GuiScreen {
    private UUID id;
    //银行地址
    private V3 bankLocation;
    //银屏
    private ATMscreen theScreen;
    long fuckingBodge;
    //错误文本
    private String errorText;
    //鼠标计数
    private int mouseCount = 0;
    private List<Commodity> cart;
    public GuiBankATM() {
    }
    public GuiBankATM(PacketOpenBankATMGui message) {
        this.id=message.id;
        this.bankLocation=message.v3;
        this.theScreen = ATMscreen.START;
        this.fuckingBodge = 0L;
        this.errorText = "";
        this.cart = new CopyOnWriteArrayList();
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
    }
    @Override
    public void initGui() {
        try {
            boolean robbed = false;
            //查钻石
            List<V3> blocks = findClosestBlocks(this.bankLocation, Blocks.DIAMOND_BLOCK, 10);
            if (blocks.size() == 0) {
                robbed = true;
            }
            //查绿宝石
            blocks = findClosestBlocks(this.bankLocation, Blocks.EMERALD_BLOCK, 10);
            if (blocks.size() == 0) {
                robbed = true;
            }
            //查金块
            blocks = findClosestBlocks(this.bankLocation, Blocks.GOLD_BLOCK, 10);
            if (blocks.size() == 0) {
                robbed = true;
            }

            if (robbed) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                //看起来你已经抢了银行！
                String sim_gui_ATMs = I18n.format("container.sim.sim_gui_ATMs");
                ModSimLoader.sendChat(sim_gui_ATMs);
            } else {
                //更新售卖物品
                if (ModSimLoader.theCommodities.size() == 0) {
                    Commodity.refreshAvailableCommoditities();
                }

                this.buttonList.clear();
                if (this.theScreen == ATMscreen.START) {
                    //存款项目
                    String sim_gui_ATMs_Deposit = I18n.format("container.sim.sim_gui_ATMs_Deposit");
                    this.buttonList.add(new GuiButton(0, this.width / 2 - 50, 50, 100, 20, sim_gui_ATMs_Deposit));
                    //购买商品
                    String sim_gui_ATMs_Commodities = I18n.format("container.sim.sim_gui_ATMs_Commodities");
                    this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 70, 100, 20, sim_gui_ATMs_Commodities));
                } else {
                    int offset;
                    int inv;
                    if (this.theScreen == ATMscreen.DEPOSIT) {
                        offset = 30;

                        for (inv = 0; inv < this.mc.thePlayer.inventory.getSizeInventory(); inv++) {
                            ItemStack is = this.mc.thePlayer.inventory.getStackInSlot(inv);
                            if (is != null) {
                                //出售一个
                                String sim_gui_ATMs_Sell_1 = I18n.format("container.sim.sim_gui_ATMs_Sell_1");
                                //出售
                                String sim_gui_ATMs_Sell = I18n.format("container.sim.sim_gui_ATMs_Sell");
                                // 个
                                String sim_gui_ATMs_for = I18n.format("container.sim.sim_gui_ATMs_for");
                                //钻石
                                if (is.getItem() == Items.DIAMOND) {

                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceDiamond)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceDiamond * (float) is.stackSize)));
                                    offset += 20;
                                    //绿宝石
                                } else if (is.getItem() == Items.EMERALD) {
                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceEmerald)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceEmerald * (float) is.stackSize)));
                                    offset += 20;
                                    //红石
                                } else if (is.getItem() == Items.REDSTONE) {
                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceRedstone)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceRedstone * (float) is.stackSize)));
                                    offset += 20;
                                    //萤石粉
                                } else if (is.getItem() == Items.GLOWSTONE_DUST) {
                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGlowstone)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGlowstone * (float) is.stackSize)));
                                    offset += 20;
                                    //金锭
                                } else if (is.getItem() == Items.GOLD_INGOT) {
                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGold)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGold * (float) is.stackSize)));
                                    offset += 20;
                                    //铁锭
                                }else if (is.getItem() == Items.IRON_INGOT) {
                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceIron)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceIron * (float) is.stackSize)));
                                    offset += 20;
                                    //锡锭
                                }else if (is.getItem() == ItemLoader.itemTinIngot) {
                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.stackSize)));
                                    offset += 20;
                                    //铜锭
                                }else if (is.getItem() == ItemLoader.itemCopperIngot) {
                                    this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceCopper)));
                                    this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.stackSize + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceCopper * (float) is.stackSize)));
                                    offset += 20;
                                }
                            }
                        }
                    } else if (this.theScreen == ATMscreen.COMMODITIES) {
                        offset = 30;

                        for (inv = 0; inv < ModSimLoader.theCommodities.size(); inv++) {
                            this.buttonList.add(new GuiButton(inv + 200, this.width / 2, offset, 20, 20, "-"));
                            this.buttonList.add(new GuiButton(inv + 300, this.width / 2 + 20, offset, 20, 20, "+"));
                            offset += 20;
                        }
                        //购买
                        String sim_gui_ATMs_Buy = I18n.format("container.sim.sim_gui_ATMs_Buy");
                        this.buttonList.add(new GuiButton(400, this.width - 60, this.height - 30, 50, 20, sim_gui_ATMs_Buy));
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiBankATM-initGui出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            //模拟城镇能力有限公司
            String sim_gui_ATMs_Ltd = I18n.format("container.sim.sim_gui_ATMs_Ltd");
            this.drawCenteredString(this.fontRendererObj, sim_gui_ATMs_Ltd, this.width / 2, 5, 16777215);
            if (this.theScreen == ATMscreen.START) {
                //欢迎来到模拟城镇银行,使用这台自动取款机你可以存放你的宝石
                String sim_gui_ATMs_Welcome = I18n.format("container.sim.sim_gui_ATMs_Welcome");
                this.drawCenteredString(this.fontRendererObj, sim_gui_ATMs_Welcome, this.width / 2, 15, 65280);
                //作为模拟城镇金币的交换,我们为您提供最优惠的价格
                String sim_gui_ATMs_exchange = I18n.format("container.sim.sim_gui_ATMs_exchange");
                this.drawCenteredString(this.fontRendererObj, sim_gui_ATMs_exchange, this.width / 2, 25, 65280);
                //钻石、翡翠、红石、辉石和黄金。
                String sim_gui_ATMs_Diamonds = I18n.format("container.sim.sim_gui_ATMs_Diamonds");
                this.drawCenteredString(this.fontRendererObj, sim_gui_ATMs_Diamonds, this.width / 2, 35, 65280);
            } else {
                int offset;
                if (this.theScreen == ATMscreen.DEPOSIT) {
                    offset = 35;
                    boolean playerHasItems = false;
                    //本行接受的库存物品：
                    String sim_gui_ATMs_Items = I18n.format("container.sim.sim_gui_ATMs_Items");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_ATMs_Items, this.width / 2, 15, 65280);

                    for (int inv = 0; inv < this.mc.thePlayer.inventory.getSizeInventory(); inv++) {
                        ItemStack is = this.mc.thePlayer.inventory.getStackInSlot(inv);
                        //钻石、绿宝石、红石、萤石粉、金锭
                        if (is != null && (is.getItem() == Items.DIAMOND || is.getItem() == Items.EMERALD || is.getItem() == Items.REDSTONE || is.getItem() == Items.GLOWSTONE_DUST || is.getItem() == Items.GOLD_INGOT||is.getItem() ==Items.IRON_INGOT||is.getItem() ==ItemLoader.itemTinIngot||is.getItem() ==ItemLoader.itemCopperIngot)) {
                            this.drawString(this.fontRendererObj, is.stackSize + " x " + is.getDisplayName(), 40, offset, 65280);
                            playerHasItems = true;
                            offset += 20;
                        }
                    }

                    if (!playerHasItems) {
                        //你没有我们想买的东西,抱歉。
                        String sim_gui_ATMs_sorry = I18n.format("container.sim.sim_gui_ATMs_sorry");
                        this.drawString(this.fontRendererObj, sim_gui_ATMs_sorry, 40, offset, 65280);
                    }
                } else if (this.theScreen == ATMscreen.COMMODITIES) {
                    //今天可以买到的商品
                    String sim_gui_ATMs_today = I18n.format("container.sim.sim_gui_ATMs_today");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_ATMs_today, this.width / 2, 20, 65280);
                    offset = 35;
                    if (ModSimLoader.theCommodities.size() == 0) {
                        //目前没有物品,请稍后再来。
                        String sim_gui_ATMs_later = I18n.format("container.sim.sim_gui_ATMs_later");
                        this.drawString(this.fontRendererObj, sim_gui_ATMs_later, 20, offset, 65280);
                    }

                    for (int it = 0; it < ModSimLoader.theCommodities.size(); it++) {
                        Commodity item = (Commodity) ModSimLoader.theCommodities.get(it);
                        this.drawString(this.fontRendererObj, item.quantity + " x " + item.theItemStack.getDisplayName() + " @ " + ModSimLoader.displayMoney(item.priceEach) + " each", 20, offset, 65280);
                        int qty = 0;

                        for (int ci = 0; ci < this.cart.size(); ++ci) {
                            Commodity cartItem = (Commodity) this.cart.get(ci);
                            if (cartItem.theItemStack.getDisplayName().contentEquals(item.theItemStack.getDisplayName())) {
                                qty = cartItem.quantity;
                            }
                        }

                        this.drawString(this.fontRendererObj, qty + "", this.width / 2 - 30, offset, 65280);
                        offset += 20;
                    }
                }
            }

            this.drawCenteredString(this.fontRendererObj, this.errorText, this.width / 2, this.height - 15, 16711680);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiBankATM-drawScreen出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }


    /**
     * 执行动作
     *
     * @param guibutton
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        try {
            if (System.currentTimeMillis() - this.fuckingBodge >= 500L) {
                this.fuckingBodge = System.currentTimeMillis();
                //存款项目
                String sim_gui_ATMs_Deposit = I18n.format("container.sim.sim_gui_ATMs_Deposit");
                //购买商品
                String sim_gui_ATMs_Commodities = I18n.format("container.sim.sim_gui_ATMs_Commodities");
                if (guibutton.displayString.contentEquals(sim_gui_ATMs_Deposit)) {
                    this.theScreen = ATMscreen.DEPOSIT;
                    this.initGui();
                } else if (guibutton.displayString.contentEquals(sim_gui_ATMs_Commodities)) {
                    this.theScreen = ATMscreen.COMMODITIES;
                    this.initGui();
                } else {
                    if (guibutton.id >= 100 && guibutton.id < 200) {
                        ItemStack is = this.mc.thePlayer.inventory.getStackInSlot(guibutton.id - 100);
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cashshort"));
                        this.mc.theWorld.playSound(null,this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
//                        ModSim.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, ModSim.MODID + ":cashshort", 1, 1, false);
                        String money = guibutton.displayString.substring(guibutton.displayString.indexOf(I18n.format("container.sim.trhsy")) + 1);
//                        NumberFormat format = NumberFormat.getInstance();
//                        String number = "";
                        //number = format.parse(money);
                        float soldFor = Float.parseFloat(money);
                        ModSimLoader.money += soldFor;
                        --is.stackSize;
                        if (is.stackSize == 0) {
                            is = null;
                        }

                        this.mc.thePlayer.inventory.setInventorySlotContents(guibutton.id - 100, is);
                        this.initGui();
                    } else if (guibutton.id >= 500 && guibutton.id < 600) {
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cashshort"));
                        this.mc.theWorld.playSound(null,this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
//                        ModSim.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, ModSim.MODID + ":cashshort", 1, 1, false);
//                        NumberFormat format = NumberFormat.getInstance();
//                        String number = "";
                        String number = guibutton.displayString.substring(guibutton.displayString.indexOf(I18n.format("container.sim.trhsy")) + 1);
                        float soldFor = Float.parseFloat(number);
                        ModSimLoader.money += soldFor;
                        this.mc.thePlayer.inventory.setInventorySlotContents(guibutton.id - 500, (ItemStack) null);
                        this.initGui();
                    } else {
                        int ci;
                        Commodity cartItem;
                        Commodity comm;
                        if (guibutton.id >= 200 && guibutton.id < 300) {
                            comm = (Commodity) ModSimLoader.theCommodities.get(guibutton.id - 200);

                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = (Commodity) this.cart.get(ci);
                                if (cartItem.theItemStack.getDisplayName().contentEquals(comm.theItemStack.getDisplayName()) && cartItem.quantity > 0) {
                                    --cartItem.quantity;
                                    break;
                                }

                                if (cartItem.quantity == 0) {
                                    this.cart.remove(ci);
                                    break;
                                }
                            }
                        } else if (guibutton.id >= 300 && guibutton.id < 400) {
                            comm = (Commodity) ModSimLoader.theCommodities.get(guibutton.id - 300);
                            boolean added = false;

                            for (int cj = 0; cj < this.cart.size(); ++cj) {
                                Commodity cc = (Commodity) this.cart.get(cj);
                                if (cc.theItemStack.getDisplayName().contentEquals(comm.theItemStack.getDisplayName())) {
                                    if (cc.quantity >= comm.quantity) {
                                        return;
                                    }

                                    ++cc.quantity;
                                    added = true;
                                    break;
                                }
                            }

                            if (!added) {
                                this.cart.add(new Commodity(comm.theItemStack, 1, comm.priceEach));
                            }
                        } else if (guibutton.id == 400) {
                            if (this.cart.size() == 0) {
                                //您尚未添加任何项目。
                                String sim_gui_ATMs_added = I18n.format("container.sim.sim_gui_ATMs_added");
                                this.errorText = sim_gui_ATMs_added;
                                return;
                            }

                            float cost = 0.0F;

                            ItemStack is;
                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = this.cart.get(ci);
                                is = cartItem.theItemStack;
                                is.stackSize = cartItem.quantity;
                                cost += (float) cartItem.quantity * cartItem.priceEach;
                            }

                            if (cost > ModSimLoader.money) {
                                //代价是
                                String sim_gui_ATMs_cost = I18n.format("container.sim.sim_gui_ATMs_cost");
                                //, 但你只有
                                String sim_gui_ATMs_only = I18n.format("container.sim.sim_gui_ATMs_only");
                                this.errorText = sim_gui_ATMs_cost + ModSimLoader.displayMoney(cost) + sim_gui_ATMs_only + ModSimLoader.displayMoney(ModSimLoader.money);
                                return;
                            }

                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = this.cart.get(ci);
                                is = cartItem.theItemStack;
                                is.stackSize = cartItem.quantity;
                                this.mc.thePlayer.inventory.addItemStackToInventory(is);

                                for (int ai = 0; ai < ModSimLoader.theCommodities.size(); ++ai) {
                                    Commodity ac = (Commodity) ModSimLoader.theCommodities.get(ai);
                                    if (ac.theItemStack.getDisplayName().contentEquals(cartItem.theItemStack.getDisplayName())) {
                                        ModSimLoader.theCommodities.remove(ai);
                                        break;
                                    }
                                }
                            }

                            ModSimLoader.money -= cost;
                            //购买的商品价值
                            String sim_gui_ATMs_worth = I18n.format("container.sim.sim_gui_ATMs_worth");
                            ModSimLoader.sendChat(sim_gui_ATMs_worth + ModSimLoader.displayMoney(cost));
                            SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                            this.mc.theWorld.playSound(null,this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
//                            ModSim.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, ModSim.MODID + ":cash", 1, 1, false);
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                        }
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GUIBANJATMactionPerformed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @Author fan
     * @Description //TODO 在区域内找方块
     * @Date 23:56 2023/7/12
     * @Param [startXYZ, block, distanceLimit]
     * @return java.util.List<com.trhsy.sim.npc.V3>
     **/
    public List<V3> findClosestBlocks(V3 startXYZ, Block block, int distanceLimit) {
        List<V3> blocksFound = new CopyOnWriteArrayList();
        int count = 0;
        List<V3> retblocksFound = new CopyOnWriteArrayList();
        try {
            World theWorld = this.mc.theWorld;
            for (int yo = -distanceLimit; yo <= distanceLimit; yo++) {
                for (int xo = -distanceLimit; xo <= distanceLimit; xo++) {
                    for (int zo = -distanceLimit; zo <= distanceLimit; zo++) {
                        try {
                            int sx = (int) (startXYZ.x + xo);
                            int sy = (int) (startXYZ.y + yo);
                            int sz = (int) (startXYZ.z + zo);
                            count++;
                            if (theWorld.getBlockState(new BlockPos(sx, sy, sz)).getBlock() == block) {
                                V3 v = new V3((double) sx, (double) sy, (double) sz, startXYZ.dimension);
                                if (!blocksFound.contains(v)) {
                                    blocksFound.add(v);
                                }
                            }
                        } catch (Exception e) {
                            //var13.printStackTrace();
                        }
                    }
                }
            }

            int ci = 0;
            double cd = 999;

            for (int i = 0; i < blocksFound.size(); i++) {
                V3 v = (V3) blocksFound.get(i);
                double distance = Math.sqrt((v.x - startXYZ.x) * (v.x - startXYZ.x) + (v.z - startXYZ.z) * (v.z - startXYZ.z));
                if (distance < cd) {
                    cd = distance;
                    ci = i;
                }
            }


            if (blocksFound.size() > 0) {
                retblocksFound.add(blocksFound.get(ci));

                for (int i = 0; i < blocksFound.size(); i++) {
                    if (i != ci) {
                        retblocksFound.add(blocksFound.get(i));
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("找到最近的街区出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return retblocksFound;
    }
}
