package com.trhsy.sim.common.config;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @ClassName NetworkWrapper
 * @Description todo
 * @Author Tian
 * @Date 2022/5/120:36
 **/
public class NetworkWrapper {
    public final SimpleNetworkWrapper network;
    protected final NetworkWrapper.AbstactPacketHandler handler;
    private int id = 0;

    public NetworkWrapper(String channelName) {
        this.network = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        this.handler = new NetworkWrapper.AbstactPacketHandler();
    }

    public void registerPacket(Class<? extends AbstractPacket> packetClazz) {
        this.registerPacketClient(packetClazz);
        this.registerPacketServer(packetClazz);
    }

    public void registerPacketClient(Class<? extends AbstractPacket> packetClazz) {
        this.registerPacketImpl(packetClazz, Side.CLIENT);
    }

    public void registerPacketServer(Class<? extends AbstractPacket> packetClazz) {
        this.registerPacketImpl(packetClazz, Side.SERVER);
    }

    private void registerPacketImpl(Class<? extends AbstractPacket> packetClazz, Side side) {
        this.network.registerMessage(this.handler, packetClazz, this.id++, side);
    }

    public static class AbstactPacketHandler implements IMessageHandler<AbstractPacket, IMessage> {
        public AbstactPacketHandler() {
        }

        @Override
        public IMessage onMessage(AbstractPacket packet, MessageContext ctx) {
            return ctx.side == Side.SERVER ? packet.handleServer(ctx.getServerHandler()) : packet.handleClient(ctx.getClientHandler());
        }
    }
}
