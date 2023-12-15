package com.trhsy.sim.network.client;

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
 * @ClassName PacketOpenMerchantGui
 * @Description todo
 * @Author TRHSY
 * @Date 2023/7/421:18
 **/
public class PacketOpenMerchantGui implements IMessage {
    public UUID id;
    public PacketOpenMerchantGui() {
    }

    public PacketOpenMerchantGui(NpcData fd) {
        this.id = UUID.fromString(fd.ID);
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.id.toString());
    }

    public static class Handler implements IMessageHandler<PacketOpenMerchantGui, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketOpenMerchantGui message, MessageContext ctx) {
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

        private void handle(PacketOpenMerchantGui message, MessageContext ctx) {
            ModSimClientLoader.openMerchantGui(message);
        }
    }
}
