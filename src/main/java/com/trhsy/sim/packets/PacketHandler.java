package com.trhsy.sim.packets;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.packets.client.UpdateFolkPositionPacket;
import com.trhsy.sim.packets.server.DemolishBuildingPacket;
import com.trhsy.sim.packets.server.GenerateFolkPacket;
import com.trhsy.sim.packets.server.LoadBuildingPacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static SimpleNetworkWrapper net;

    public static void initPackets()
    {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(ModSim.MODID);
        //更新NPC位置数据包
        net.registerMessage(UpdateFolkPositionPacket.Handler.class, UpdateFolkPositionPacket.class, 0, Side.CLIENT);
        //加载建筑数据包
        net.registerMessage(LoadBuildingPacket.Handler.class, LoadBuildingPacket.class, 1, Side.SERVER);
        //拆除建筑包
        net.registerMessage(DemolishBuildingPacket.Handler.class, DemolishBuildingPacket.class, 2, Side.SERVER);
        //生成NPC数据包
        net.registerMessage(GenerateFolkPacket.Handler.class, GenerateFolkPacket.class, 3, Side.SERVER);

    }

    private static int nextPacketId = 0;

    private static void registerMessage(Class packet, Class message)
    {
        net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
        net.registerMessage(packet, message, nextPacketId, Side.SERVER);
        nextPacketId++;
    }
}