package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketOpenFolkInventoryGui
 * @Description:
 * @date 2024/3/19 14:38
 */
public class PacketOpenFolkInventoryGui implements IMessage {
    public UUID uid;
    public PacketOpenFolkInventoryGui(){}
    public PacketOpenFolkInventoryGui(NpcData fd){
        this.uid=fd.ID;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.uid= UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uid.toString());
    }

    public static class Handler implements IMessageHandler<PacketOpenFolkInventoryGui, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketOpenFolkInventoryGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketOpenFolkGui出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketOpenFolkInventoryGui message, MessageContext ctx) {
            ModSimClientLoader.openFolkInventoryGui(message);
        }
    }
}
