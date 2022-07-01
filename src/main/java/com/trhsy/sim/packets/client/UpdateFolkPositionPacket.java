package com.trhsy.sim.packets.client;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
        this.posString = posString;
        this.data = posString.split(";");
        pos = this.data[0];
        folkName = this.data[1];
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.posString = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.posString);
    }
    public static class Handler implements IMessageHandler<UpdateFolkPositionPacket,IMessage> {
        @Override
        public IMessage onMessage(UpdateFolkPositionPacket message, MessageContext ctx) {
            FolkData folk = FolkData.getFolkByName(UpdateFolkPositionPacket.folkName);
            V3 newpos = new V3(UpdateFolkPositionPacket.pos);
            if (folk != null && newpos != null) {
                folk.serverToClientLocationUpdate(newpos);
            }
            return null;
        }
    }
}
