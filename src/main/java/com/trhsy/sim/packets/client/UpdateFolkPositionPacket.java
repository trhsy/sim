package com.trhsy.sim.packets.client;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
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
 * @ClassName UpdateFolkPositionMessage
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:41
 * ========================================
 **/
public class UpdateFolkPositionPacket implements IMessage {
    private String posString;
    String[] data;
    static String folkName;
    static String pos;

    public UpdateFolkPositionPacket() {
    }

    public UpdateFolkPositionPacket(String posString) {
        try {
            this.posString = posString;
            this.data = posString.split(";");
            pos = this.data[0];
            folkName = this.data[1];
        } catch (Exception e) {
            ModSimReloaded.log.error("UpdateFolkPositionPacket出错了：" + e.getMessage());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            this.posString = ByteBufUtils.readUTF8String(buf);
        } catch (Exception e) {
            ModSimReloaded.log.error("fromBytes出错了：" + e.getMessage());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            ByteBufUtils.writeUTF8String(buf, this.posString);
        } catch (Exception e) {
            ModSimReloaded.log.error("toBytes出错了：" + e.getMessage());
        }
    }

    public static class Handler implements IMessageHandler<UpdateFolkPositionPacket, IMessage> {
        @Override
        public IMessage onMessage(UpdateFolkPositionPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FolkData folk = FolkData.getFolkByName(UpdateFolkPositionPacket.folkName);
                            V3 newpos = new V3(UpdateFolkPositionPacket.pos);
                            if (folk != null && newpos != null) {
                                folk.serverToClientLocationUpdate(newpos);
                            }
                        } catch (Exception e) {
                            ModSimReloaded.log.error("onMessage出错了：" + e.getMessage());
                        }
                    }
                });
            }
            return null;
        }
    }
}
