package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketFireFolk
 * @Description todo
 * @Author TRHSY
 * @Date 2022/10/2118:00
 **/
public class PacketFireFolk implements IMessage {
    private String uuid;

    public PacketFireFolk() {
    }

    public PacketFireFolk(String uuid) {
        this.uuid = uuid;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = ByteBufUtils.readUTF8String(buf);
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuid);
    }

    public static class Handler implements IMessageHandler<PacketFireFolk, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketFireFolk message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketFireFolk出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketFireFolk message, MessageContext ctx) {
            NpcData fd = ModSimLoader.getFolkDataByUID(message.uuid);
            fd.fire();
        }
    }
}