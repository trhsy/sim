package com.trhsy.sim.packets;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.client.UpdateFolkPositionPacket;
import com.trhsy.sim.packets.server.DemolishBuildingPacket;
import com.trhsy.sim.packets.server.GenerateFolkPacket;
import com.trhsy.sim.packets.server.LoadBuildingPacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static SimpleNetworkWrapper net = NetworkRegistry.INSTANCE.newSimpleChannel(ModSim.MODID);
    private static int nextID = 0;
    /**
     * 初始化数据包
     */
    public static void initPackets() {
        try {
            //更新NPC位置数据包
            registerMessage(UpdateFolkPositionPacket.Handler.class, UpdateFolkPositionPacket.class, Side.CLIENT);
            //加载建筑数据包
            registerMessage(LoadBuildingPacket.Handler.class, LoadBuildingPacket.class, Side.CLIENT);
            //拆除建筑包
            registerMessage(DemolishBuildingPacket.Handler.class, DemolishBuildingPacket.class, Side.CLIENT);
            //生成NPC数据包
            registerMessage(GenerateFolkPacket.Handler.class, GenerateFolkPacket.class, Side.CLIENT);
        } catch (Exception e) {
            ModSimReloaded.log.error("initPackets出错了：" + e.getMessage());
        }
    }

    private static int nextPacketId = 0;

    /**
     *
     * @param messageHandler
     * @param requestMessageType
     * @param side
     * @param <REQ>
     * @param <REPLY>
     */
    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        net.registerMessage(messageHandler, requestMessageType, nextID++, side);
    }

    private static void registerMessage(Class packet, Class message) {
        try {
            net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
            net.registerMessage(packet, message, nextPacketId, Side.SERVER);
            nextPacketId++;
        } catch (Exception e) {
            ModSimReloaded.log.error("registerMessage出错了：" + e.getMessage());
        }

    }
}