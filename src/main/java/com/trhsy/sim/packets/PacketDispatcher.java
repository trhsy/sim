package com.trhsy.sim.packets;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * ========================================
 *
 * @ClassName PacketDispatcher
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:36
 * ========================================
 **/
public class PacketDispatcher {
    private static byte packetId = 0;
    static final SimpleNetworkWrapper dispatcher;

    public PacketDispatcher() {
    }

    public static final void registerPackets() {
    }

    private static final void registerMessage(Class handlerClass, Class messageClass, Side side) {
        SimpleNetworkWrapper var10000 = dispatcher;
        byte var10003 = packetId;
        packetId = (byte)(var10003 + 1);
        var10000.registerMessage(handlerClass, messageClass, var10003, side);
    }

    public static final void sendTo(IMessage message, EntityPlayerMP player) {
        dispatcher.sendTo(message, player);
    }

    public static final void sendToAllAround(IMessage message, TargetPoint point) {
        dispatcher.sendToAllAround(message, point);
    }

    public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
        sendToAllAround(message, new TargetPoint(dimension, x, y, z, range));
    }

    public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
        sendToAllAround(message, player.field_70170_p.field_73011_w.field_76574_g, player.field_70165_t, player.field_70163_u, player.field_70161_v, range);
    }

    public static final void sendToDimension(IMessage message, int dimensionId) {
        dispatcher.sendToDimension(message, dimensionId);
    }

    public static final void sendToServer(IMessage message) {
        dispatcher.sendToServer(message);
    }

    static {
        dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(ModSimukraft.modid);
    }
}
