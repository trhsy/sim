package com.trhsy.sim.packets.client;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("UpdateFolkPositionPacket出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            this.posString = ByteBufUtils.readUTF8String(buf);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("fromBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            ByteBufUtils.writeUTF8String(buf, this.posString);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
                            String pos=UpdateFolkPositionPacket.pos;
                            String[] v=pos.split(",");
                            double x= Double.parseDouble(v[0]);
                            double y= Double.parseDouble(v[0]);
                            double z= Double.parseDouble(v[0]);
                            V3 newpos = new V3(x,y,z);
                            if (folk != null && newpos != null) {
                                folk.serverToClientLocationUpdate(newpos);
                            }
                        } catch (Exception e) {
                            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onMessage出错了：" + e.getMessage()+"行数："+element.getLineNumber());
                        }
                    }
                });
            }
            return null;
        }
    }
}
