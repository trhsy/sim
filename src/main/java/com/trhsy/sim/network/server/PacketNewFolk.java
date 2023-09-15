package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketNewFolk
 * @Description todo
 * @Author TRHSY
 * @Date 2023/8/1316:55
 **/
public class PacketNewFolk implements IMessage {
    private boolean fromCommand;

    public PacketNewFolk() {
    }

    public PacketNewFolk(boolean fromCommand) {
        this.fromCommand = fromCommand;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.fromCommand = Boolean.parseBoolean(ByteBufUtils.readUTF8String(buf));
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, String.valueOf(this.fromCommand));
    }

    public static class Handler implements IMessageHandler<PacketNewFolk, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketNewFolk message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketNewFolk出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketNewFolk message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            World world = mc.world;
            if(world.isRemote){
                NpcData fd = new NpcData(world, message.fromCommand);
            }
        }
    }
}