package com.trhsy.sim.packets;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.client.UpdateFolkPositionPacket;
import com.trhsy.sim.packets.server.DemolishBuildingPacket;
import com.trhsy.sim.packets.server.GenerateFolkPacket;
import com.trhsy.sim.packets.server.LoadBuildingPacket;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetWorkLoader {
    public static SimpleNetworkWrapper net = NetworkRegistry.INSTANCE.newSimpleChannel(ModSim.MODID);
    private static int nextID = 0;
    /**
     * 初始化数据包
     */
    public NetWorkLoader(FMLPreInitializationEvent event){
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("NetWorkLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

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

}