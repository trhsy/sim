package com.trhsy.sim.network.server;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PacketGetHireableFolks
 * @Description todo
 * @Author TRHSY
 * @Date 2022/10/2118:04
 **/
public class PacketGetHireableFolks implements IMessage {
    public List<NpcIdentity> folkNames = new ArrayList();
    public boolean isFromClient;

    public PacketGetHireableFolks() {
    }

    public PacketGetHireableFolks(boolean isFromClient) {
    }

    public void fromBytes(ByteBuf buf) {
    }

    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<PacketGetHireableFolks, IMessage> {
        public Handler() {
        }

        public IMessage onMessage(PacketGetHireableFolks message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketGetHireableFolks message, MessageContext ctx) {
            ModSimLoader.log.info("Sending packet to client");
            NetWorkLoader.net.sendTo(new PacketReturnHireableFolks(), ctx.getServerHandler().playerEntity);
        }
    }
}
