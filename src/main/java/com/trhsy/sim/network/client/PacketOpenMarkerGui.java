package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.V3;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketOpenMarkerGui
 * @Description:
 * @date 2023/5/4 16:30
 */
public class PacketOpenMarkerGui implements IMessage{
    private V3 v3;
    private int dimension;
    public PacketOpenMarkerGui() {
    }

    public PacketOpenMarkerGui(V3 v3,int dimension) {
        this.v3 = v3;
        this.dimension=dimension;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.v3 = V3.fromString(ByteBufUtils.readUTF8String(buf));
        this.dimension =  buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.v3.toString());
        buf.writeInt(this.dimension);
    }
    public static class Handler implements IMessageHandler<PacketOpenMarkerGui, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketOpenMarkerGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketOpenControlGui出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketOpenMarkerGui message, MessageContext ctx) {
                ModSimClientLoader.openMarkerGui(message.v3,message.dimension);
        }
    }
}
