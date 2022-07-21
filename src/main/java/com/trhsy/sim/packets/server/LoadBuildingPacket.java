package com.trhsy.sim.packets.server;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.loader.ModSimReloaded;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * ========================================
 *
 * @ClassName LoadBuildingMessage
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:46
 * ========================================
 **/
public class LoadBuildingPacket implements IMessage {
    private String GuiBuildingCon;

    public LoadBuildingPacket() {
    }
    public LoadBuildingPacket(String GuiBuildingCon) {
        try {
            this.GuiBuildingCon = GuiBuildingCon;
        } catch (Exception e) {
            ModSimReloaded.log.error("LoadBuildingPacket出错了：" + e.getMessage());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            this.GuiBuildingCon = ByteBufUtils.readUTF8String(buf);
        } catch (Exception e) {
            ModSimReloaded.log.error("fromBytes出错了：" + e.getMessage());
        }

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.GuiBuildingCon);
    }
    public static class Handler implements IMessageHandler<LoadBuildingPacket, IMessage> {
        @Override
        public IMessage onMessage(LoadBuildingPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Building.loadAllBuildings();
                        } catch (Exception e) {
                            ModSimReloaded.log.error("onMessage出错了：" + e.getMessage());
                        }
                        //player.openGui(ModSimukraft.instance, message.id, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
                    }
                });
            }
            return null;
        }
    }
}
