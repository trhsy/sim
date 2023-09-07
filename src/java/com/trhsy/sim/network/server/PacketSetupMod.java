package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketSetupMod
 * @Description todo
 * @Author Tian
 * @Date 2022/9/2515:27
 **/
public class PacketSetupMod implements IMessage {
    public int gamemode;
    public PacketSetupMod() {
    }
    public PacketSetupMod(int gamemode) {
        this.gamemode = gamemode;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.gamemode = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.gamemode);
    }

    public static class Handler implements IMessageHandler<PacketSetupMod, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketSetupMod message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketSetupMod出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketSetupMod message, MessageContext ctx) {
            ModSimLoader.gamemode = message.gamemode;
        }
    }
}
