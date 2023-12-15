package com.trhsy.sim.gui.npc;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.gui.ATMscreen;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.network.client.PacketOpenFlowerGui;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.util.PricesForBlocks;
import com.trhsy.sim.util.items.CommodityFlower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.gui.npc
 * @ClassName: GuiMerchants
 * @Description: 花店
 * @date 2023/07/24 下午 4:02
 */
public class GuiFlower extends GuiScreen {
    private UUID id;
    //花店地址
    private V3 bankLocation;
    //银屏
    private ATMscreen theScreen;
    long fuckingBodge;
    //错误文本
    private String errorText;
    //鼠标计数
    private int mouseCount = 0;
    private List<CommodityFlower> cart;

    public GuiFlower() {
    }

    public GuiFlower(PacketOpenFlowerGui message) {
        this.id = message.id;
        this.bankLocation = message.v3;
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

            //更新售卖物品
            if (CommodityFlower.theCommodities.size() == 0) {
                CommodityFlower.refreshAvailableCommoditities();
            }

            this.buttonList.clear();
            if (this.theScreen == ATMscreen.START) {
                //出售
                String sim_gui_ATMs_Deposit = new TextComponentTranslation("container.sim.Merchant10",new Object[0]).getUnformattedText();
                this.buttonList.add(new GuiButton(0, this.width / 2 - 50, 50, 100, 20, sim_gui_ATMs_Deposit));
                //购买
                String sim_gui_ATMs_Commodities = new TextComponentTranslation("container.sim.Merchant9",new Object[0]).getUnformattedText();
                this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 70, 100, 20, sim_gui_ATMs_Commodities));
            } else {
                int offset;
                int inv;
                if (this.theScreen == ATMscreen.DEPOSIT) {
                    offset = 30;

                    for (inv = 0; inv < this.mc.player.inventory.getSizeInventory(); inv++) {
                        ItemStack is = this.mc.player.inventory.getStackInSlot(inv);
                        if (is != null) {
                            //出售一个
                            String sim_gui_ATMs_Sell_1 = new TextComponentTranslation("container.sim.sim_gui_ATMs_Sell_1",new Object[0]).getUnformattedText();
                            //出售
                            String sim_gui_ATMs_Sell = new TextComponentTranslation("container.sim.sim_gui_ATMs_Sell",new Object[0]).getUnformattedText();
                            // 个
                            String sim_gui_ATMs_for = new TextComponentTranslation("container.sim.sim_gui_ATMs_for",new Object[0]).getUnformattedText();
                            //蒲公英
                            if (is.getItem() == new ItemStack(Blocks.YELLOW_FLOWER).getItem()) {

                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceDiamond)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceDiamond * (float) is.getCount())));
                                offset += 20;
                                //虞美人
                            } else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceEmerald)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceEmerald * (float) is.getCount())));
                                offset += 20;
                                //兰花
                            } else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,1).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceRedstone)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceRedstone * (float) is.getCount())));
                                offset += 20;
                                //绒球葱
                            } else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,2).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGlowstone)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGlowstone * (float) is.getCount())));
                                offset += 20;
                                //蓝花美耳草
                            } else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,3).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGold)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceGold * (float) is.getCount())));
                                offset += 20;
                                //红色郁金香
                            } else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,4).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceIron)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceIron * (float) is.getCount())));
                                offset += 20;
                                //橙色郁金香
                            } else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,5).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //白色郁金香
                            }else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,6).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //粉红色郁金香
                            }else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,7).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //滨菊
                            }else if (is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,8).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //向日葵
                            }else if (is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //丁香
                            }else if (is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT,1,1).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //玫瑰丛
                            }else if (is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT,1,4).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //牡丹
                            }else if (is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT,1,5).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                                //花盆
                            }else if (is.getItem() == new ItemStack(Blocks.FLOWER_POT).getItem()) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.getCount() + sim_gui_ATMs_for + ModSimLoader.displayMoney(PricesForBlocks.bankPriceTin * (float) is.getCount())));
                                offset += 20;
                            }

                        }
                    }
                } else if (this.theScreen == ATMscreen.COMMODITIES) {
                    offset = 30;

                    for (inv = 0; inv < CommodityFlower.theCommodities.size(); inv++) {
                        this.buttonList.add(new GuiButton(inv + 200, this.width / 2, offset, 20, 20, "-"));
                        this.buttonList.add(new GuiButton(inv + 300, this.width / 2 + 20, offset, 20, 20, "+"));
                        offset += 20;
                    }
                    //购买
                    String sim_gui_ATMs_Buy = new TextComponentTranslation("container.sim.sim_gui_ATMs_Buy",new Object[0]).getUnformattedText();
                    this.buttonList.add(new GuiButton(400, this.width - 60, this.height - 30, 50, 20, sim_gui_ATMs_Buy));
                }
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiMerchants-initGui出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
            String sim_gui_ATMs_Ltd = new TextComponentTranslation("container.sim.sim_gui_ATMs_Ltd",new Object[0]).getUnformattedText();
            this.drawCenteredString(this.fontRenderer, sim_gui_ATMs_Ltd, this.width / 2, 5, 16777215);
            if (this.theScreen == ATMscreen.START) {
                //欢迎来到花店,这里能买到一些你意想不到的物品，当然你若有一些特除物品也可卖给我
                String sim_gui_ATMs_Welcome = new TextComponentTranslation("container.sim.sim_gui_ATMs_Welcome2",new Object[0]).getUnformattedText();
                this.drawCenteredString(this.fontRenderer, sim_gui_ATMs_Welcome, this.width / 2, 15, 65280);
                //作为模拟城镇金币的交换,我们为您提供最优惠的价格
                String sim_gui_ATMs_exchange = new TextComponentTranslation("container.sim.sim_gui_ATMs_exchange",new Object[0]).getUnformattedText();
                this.drawCenteredString(this.fontRenderer, sim_gui_ATMs_exchange, this.width / 2, 25, 65280);
                //蒲公英,虞美人,兰花,绒球葱,蓝花美耳草,红色郁金香,橙色郁金香,白色郁金香,粉红色郁金香,滨菊,向日葵,丁香,玫瑰丛,牡丹,花盆
                String sim_gui_ATMs_Diamonds = new TextComponentTranslation("container.sim.sim_gui_ATMs_Diamonds2",new Object[0]).getUnformattedText();
                this.drawCenteredString(this.fontRenderer, sim_gui_ATMs_Diamonds, this.width / 2, 35, 65280);
            } else {
                int offset;
                if (this.theScreen == ATMscreen.DEPOSIT) {
                    offset = 35;
                    boolean playerHasItems = false;
                    //本行接受的库存物品：
                    String sim_gui_ATMs_Items = new TextComponentTranslation("container.sim.sim_gui_ATMs_Items1",new Object[0]).getUnformattedText();
                    this.drawCenteredString(this.fontRenderer, sim_gui_ATMs_Items, this.width / 2, 15, 65280);

                    for (int inv = 0; inv < this.mc.player.inventory.getSizeInventory(); inv++) {
                        ItemStack is = this.mc.player.inventory.getStackInSlot(inv);
                        //蒲公英,虞美人,兰花,绒球葱,蓝花美耳草,红色郁金香,橙色郁金香,白色郁金香,粉红色郁金香,滨菊,向日葵,丁香,玫瑰丛,牡丹,花盆
                        if (is != null && (is.getItem() == new ItemStack(Blocks.YELLOW_FLOWER).getItem() || is.getItem() == new ItemStack(Blocks.RED_FLOWER).getItem() || is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,1).getItem() || is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,2).getItem() || is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,3).getItem() || is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,4).getItem() || is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,5).getItem())|| is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,6).getItem()|| is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,7).getItem()|| is.getItem() == new ItemStack(Blocks.RED_FLOWER,1,8).getItem()|| is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT).getItem()|| is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT,1,1).getItem()|| is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT,1,4).getItem()|| is.getItem() == new ItemStack(Blocks.DOUBLE_PLANT,1,5).getItem()|| is.getItem() == new ItemStack(Blocks.FLOWER_POT).getItem()) {
                            this.drawString(this.fontRenderer, is.getCount() + " x " + is.getDisplayName(), 40, offset, 65280);
                            playerHasItems = true;
                            offset += 20;
                        }
                    }

                    if (!playerHasItems) {
                        //你没有我们想买的东西,抱歉。
                        String sim_gui_ATMs_sorry = new TextComponentTranslation("container.sim.sim_gui_ATMs_sorry",new Object[0]).getUnformattedText();
                        this.drawString(this.fontRenderer, sim_gui_ATMs_sorry, 40, offset, 65280);
                    }
                } else if (this.theScreen == ATMscreen.COMMODITIES) {
                    //今天可以买到的商品
                    String sim_gui_ATMs_today = new TextComponentTranslation("container.sim.sim_gui_ATMs_today",new Object[0]).getUnformattedText();
                    this.drawCenteredString(this.fontRenderer, sim_gui_ATMs_today, this.width / 2, 20, 65280);
                    offset = 35;
                    if (CommodityFlower.theCommodities.size() == 0) {
                        //目前没有物品,请稍后再来。
                        String sim_gui_ATMs_later = new TextComponentTranslation("container.sim.sim_gui_ATMs_later",new Object[0]).getUnformattedText();
                        this.drawString(this.fontRenderer, sim_gui_ATMs_later, 20, offset, 65280);
                    }

                    for (int it = 0; it < CommodityFlower.theCommodities.size(); it++) {
                        CommodityFlower item = (CommodityFlower) CommodityFlower.theCommodities.get(it);
                        this.drawString(this.fontRenderer, item.quantity + " x " + item.theItemStack.getDisplayName() + " @ " + ModSimLoader.displayMoney(item.priceEach) + " " + new TextComponentTranslation("container.sim.job.credits",new Object[0]).getUnformattedText(), 20, offset, 65280);
                        int qty = 0;

                        for (int ci = 0; ci < this.cart.size(); ++ci) {
                            CommodityFlower cartItem = (CommodityFlower) this.cart.get(ci);
                            if (cartItem.theItemStack.getDisplayName().contentEquals(item.theItemStack.getDisplayName())) {
                                qty = cartItem.quantity;
                            }
                        }

                        this.drawString(this.fontRenderer, qty + "", this.width / 2 - 30, offset, 65280);
                        offset += 20;
                    }
                }
            }

            this.drawCenteredString(this.fontRenderer, this.errorText, this.width / 2, this.height - 15, 16711680);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiMerchants-drawScreen出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                //出售
                String sim_gui_ATMs_Deposit = new TextComponentTranslation("container.sim.Merchant10",new Object[0]).getUnformattedText();
                //购买
                String sim_gui_ATMs_Commodities = new TextComponentTranslation("container.sim.Merchant9",new Object[0]).getUnformattedText();
                if (guibutton.displayString.contentEquals(sim_gui_ATMs_Deposit)) {
                    this.theScreen = ATMscreen.DEPOSIT;
                    this.initGui();
                } else if (guibutton.displayString.contentEquals(sim_gui_ATMs_Commodities)) {
                    this.theScreen = ATMscreen.COMMODITIES;
                    this.initGui();
                } else {
                    if (guibutton.id >= 100 && guibutton.id < 200) {
                        ItemStack is = this.mc.player.inventory.getStackInSlot(guibutton.id - 100);
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cashshort"));
                        Minecraft mc = Minecraft.getMinecraft();
                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                            mc.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                        }
                        String money = guibutton.displayString.substring(guibutton.displayString.indexOf(new TextComponentTranslation("container.sim.trhsy",new Object[0]).getUnformattedText()) + 1);
                        float soldFor = Float.parseFloat(money);
                        ModSimLoader.money += soldFor;
                        is.shrink(1);
                        if (is.getCount() == 0) {
                            is = null;
                        }

                        this.mc.player.inventory.setInventorySlotContents(guibutton.id - 100, is);
                        this.initGui();
                    } else if (guibutton.id >= 500 && guibutton.id < 600) {
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cashshort"));
                        Minecraft mc = Minecraft.getMinecraft();
                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                            mc.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                        }
                        String number = guibutton.displayString.substring(guibutton.displayString.indexOf(new TextComponentTranslation("container.sim.trhsy",new Object[0]).getUnformattedText()) + 1);
                        float soldFor = Float.parseFloat(number);
                        ModSimLoader.money += soldFor;
                        this.mc.player.inventory.setInventorySlotContents(guibutton.id - 500, ItemStack.EMPTY);
                        this.initGui();
                    } else {
                        int ci;
                        CommodityFlower cartItem;
                        CommodityFlower comm;
                        if (guibutton.id >= 200 && guibutton.id < 300) {
                            comm = (CommodityFlower) CommodityFlower.theCommodities.get(guibutton.id - 200);

                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = (CommodityFlower) this.cart.get(ci);
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
                            comm = (CommodityFlower) CommodityFlower.theCommodities.get(guibutton.id - 300);
                            boolean added = false;

                            for (int cj = 0; cj < this.cart.size(); ++cj) {
                                CommodityFlower cc = (CommodityFlower) this.cart.get(cj);
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
                                this.cart.add(new CommodityFlower(comm.theItemStack, 1, comm.priceEach));
                            }
                        } else if (guibutton.id == 400) {
                            if (this.cart.size() == 0) {
                                //您尚未添加任何项目。
                                String sim_gui_ATMs_added = new TextComponentTranslation("container.sim.sim_gui_ATMs_added",new Object[0]).getUnformattedText();
                                this.errorText = sim_gui_ATMs_added;
                                return;
                            }

                            float cost = 0.0F;

                            ItemStack is;
                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = this.cart.get(ci);
                                is = cartItem.theItemStack;
                                is.setCount(cartItem.quantity);
                                cost += (float) cartItem.quantity * cartItem.priceEach;
                            }

                            if (cost > ModSimLoader.money) {
                                //代价是
                                String sim_gui_ATMs_cost = new TextComponentTranslation("container.sim.sim_gui_ATMs_cost",new Object[0]).getUnformattedText();
                                //, 但你只有
                                String sim_gui_ATMs_only = new TextComponentTranslation("container.sim.sim_gui_ATMs_only",new Object[0]).getUnformattedText();
                                this.errorText = sim_gui_ATMs_cost + ModSimLoader.displayMoney(cost) + sim_gui_ATMs_only + ModSimLoader.displayMoney(ModSimLoader.money);
                                return;
                            }

                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = this.cart.get(ci);
                                is = cartItem.theItemStack;
                                is.setCount(cartItem.quantity);
                                this.mc.player.inventory.addItemStackToInventory(is);

                                for (int ai = 0; ai < CommodityFlower.theCommodities.size(); ++ai) {
                                    CommodityFlower ac = (CommodityFlower) CommodityFlower.theCommodities.get(ai);
                                    if (ac.theItemStack.getDisplayName().contentEquals(cartItem.theItemStack.getDisplayName())) {
                                        CommodityFlower.theCommodities.remove(ai);
                                        break;
                                    }
                                }
                            }

                            ModSimLoader.money -= cost;
                            //购买的商品价值
                            String sim_gui_ATMs_worth = new TextComponentTranslation("container.sim.sim_gui_ATMs_worth",new Object[0]).getUnformattedText();
                            ModSimLoader.sendChat(sim_gui_ATMs_worth + ModSimLoader.displayMoney(cost));
                            SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                            Minecraft mc = Minecraft.getMinecraft();
                            for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                                mc.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                            }
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                        }
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GUIBANJFloweractionPerformed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

}
