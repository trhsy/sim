package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.EntityNpc;
import com.trhsy.sim.npcCode.NpcData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

/**
 * @ClassName EventLoader
 * @Description todo 事件交互
 * @Author Tian
 * @Date 2022/9/2921:05
 **/
public class EventLoader {




    /**
     * 自定义的事件在这里被注册
     **/
    public static final EventBus EVENT_BUS = new EventBus();

    public EventLoader() {
        try {
            MinecraftForge.EVENT_BUS.register(this);
            EventLoader.EVENT_BUS.register(this);
            FMLCommonHandler.instance().bus().register(this);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EventLoader出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 当玩家加入的时候
     * @Date 19:19 2022/10/7
     * @Param [event]
     **/
    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Thread skinThread = new Thread(() -> {
            try {
                if (event.player != null) {
                    ModSimLoader.log.info("*********************玩家加入*开始重载NPC实体*****************");

                    for (NpcData fd : ModSimLoader.folks) {
                        while (fd.entity == null && FMLCommonHandler.instance() != null) {
                            try {
                                Entity  entity=FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(fd.ID);
                                ModSimLoader.log.info("找到NPC-id："+fd.ID);
                                if(!fd.isDead){
                                    entity.setDead();
                                    ModSimLoader.log.info("NPC-id："+fd.ID+"死了");
                                }else{
                                    fd.entity = (EntityNpc) entity;
                                    ModSimLoader.log.info("NPC-id："+fd.ID+",,赋予实体");
                                }
                            } catch (Exception var4) {
                                ModSimLoader.log.error("玩家加入加载数据失败了" + var4.getMessage());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }


        });
        skinThread.start();
    }

    /**
     * 世界保存
     *
     * @param event
     */
    @SubscribeEvent
    public void worldSave(WorldEvent.Save event) {
        World world=event.getWorld();
            SimmodeStart.simModSave(world);

    }

    /**
     * 世界加载
     *
     * @param event
     */
    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        World world=event.getWorld();
            // 只在服务端运行
            SimmodeStart.simModLoad(world);
    }

    /**
     * 钩子
     *
     * @param event
     */
    @SubscribeEvent
    public void worldTick(WorldTickEvent event) {
        World world=event.world;
//            SimmodeStart.simModupdate(world);
        if(world.isRemote){
            // 客户端：执行客户端专属逻辑（渲染、动画、本地状态更新）
//            SimmodeStart.clientSimModupdate(world);
//            System.out.println("客户端");
        }else{
//            SimmodeStart.clientSimModupdate(world);
//            System.out.println("客户端-服务端");
            // 服务器：执行服务器专属逻辑（实体创建、数据同步、AI逻辑）
//            SimmodeStart.serverSimModupdate(world);
//            System.out.println("服务端");

        }
        SimmodeStart.simModupdate(world);
    }

    /**
     * 实时更新人的状态
     * @param event
     */
    public void updateNpcStatus(WorldTickEvent event){


    }
    /**
     * 渲染钩子
     *
     * @param e
     */
    @SubscribeEvent
    public void renderTick(RenderTickEvent e) {
        ModSim.proxy.renderTick(e);
    }

    /**
     * 当实体加入世界赋予玩家手里第一个物品栏里一个模拟城市任命卷轴
     *
     * @param event
     */
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {

        Entity entity = event.getEntity();
        World worldObj = event.getWorld();
        SimmodeStart.hasLoadedWorld=true;

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            //欢迎来到模拟城镇,由TRHSY重制，更多资讯请关注公众号: dasha5000
            String welcome = "【" + player.getName() + "】" + new TextComponentTranslation("container.sim.welcome", new Object[0]).getUnformattedText();
            String welcomes = new TextComponentTranslation("container.sim.welcomes", new Object[0]).getUnformattedText();
            ModSimLoader.sendChat(welcome + ModSim.VERSION + welcomes);
            boolean shouldGive = ItemLoader.itemSimULoader != null && ModSimLoader.gamemode == 999;
            if (shouldGive) {
                ItemStack starter = new ItemStack(ItemLoader.itemSimULoader);
                if (!player.inventory.addItemStackToInventory(starter)) {
                    float f = 0.7F;
                    float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    EntityItem entityitem = new EntityItem(worldObj, player.posX + (double) d0, player.posY + (double) d1, player.posZ + (double) d2, new ItemStack(ItemLoader.itemSimULoader));
                    entityitem.setDefaultPickupDelay();
                    worldObj.spawnEntity(entityitem);
                }
            }
        }
    }

    /**
     * 客户端断开连接
     *
     * @param event
     */
    @SubscribeEvent
    public void clientDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
//        dedWorld = false;

        SimmodeStart.simModDisconnected();
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 世界渲染
     * @Date 17:12 2022/10/31
     * @Param [event]
     **/
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onWorldRenderLast(RenderWorldLastEvent event) {
        World world = Minecraft.getMinecraft().world;
//        if (!world.isRemote){
            for (EntityPlayer player : world.playerEntities) {
                if (ModSimClientLoader.previewPos1 != null && ModSimClientLoader.previewPos2 != null) {
                    //预览
                    drawBoundingBox(player, ModSimClientLoader.previewPos1, ModSimClientLoader.previewPos2, true, 5.0F, event);

                }
//            }
        }


    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 预览程序
     * @Date 17:19 2022/10/31
     * @Param [player, posA, posB, smooth, width, event]
     **/
    public static void drawBoundingBox(EntityPlayer player, Vec3d posA, Vec3d posB, boolean smooth, float width, RenderWorldLastEvent event) {
        GL11.glPushAttrib(8192);
        GL11.glDisable(2884);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        //获取位置
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) event.getPartialTicks();
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) event.getPartialTicks();
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();

        Vec3d pos = new Vec3d(d0, d1, d2);
        //转换
        GL11.glTranslated(-pos.x, -pos.y, -pos.z);
        //rgba  红色
        Color c = new Color(255, 0, 0, 150);
        GL11.glColor4d((double) c.getRed(), (double) c.getGreen(), (double) c.getBlue(), (double) c.getAlpha());
        //设置宽
        GL11.glLineWidth(width);
        //深度 蒙版
        GL11.glDepthMask(true);
        //镶嵌单元
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        //设置位置颜色
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);

        double dx = posA.x - posB.x > 0.0D ? -Math.abs(posA.x - posB.x) : Math.abs(posA.x - posB.x);
        double dy = Math.abs(posA.y - posB.y);
        double dz = posA.z - posB.z > 0.0D ? -Math.abs(posA.z - posB.z) : Math.abs(posA.z - posB.z);
        double xOf = dx > 0.0D ? 0.0D : 1.0D;
        double zOf = dz > 0.0D ? 0.0D : 1.0D;
        posA = posA.addVector(xOf, 0.0D, zOf);

        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        bufferBuilder.pos(posA.x, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }
}
