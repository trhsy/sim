package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.network.client.*;
import com.trhsy.sim.network.server.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: NetWorkLoader
 * @Description: 通讯服务
 * @date 2022/9/22 0022 下午 3:12
 */
public class NetWorkLoader {
    public static SimpleNetworkWrapper net = null;
    private static int nextID = 0;
    /**
     * 初始化数据包
     */
    public NetWorkLoader(FMLPreInitializationEvent event){
        net = NetworkRegistry.INSTANCE.newSimpleChannel(ModSim.MODID);
        registerMessagesAsServer();
        registerMessagesAsClient();
    }

    /**
     * 服务端
     */
    public void registerMessagesAsServer(){
        //启动
        registerMessage(PacketSetupMod.Handler.class,PacketSetupMod.class,Side.SERVER);
        //雇佣
        registerMessage(PacketHireFolk.Handler.class,PacketHireFolk.class,Side.SERVER);
        //解雇
        registerMessage(PacketFireFolk.Handler.class,PacketFireFolk.class,Side.SERVER);
        //获得蓝图
        registerMessage(PacketSendBlueprint.Handler.class,PacketSendBlueprint.class,Side.SERVER);
        //获得规划
        registerMessage(PacketSendTerrainType.Handler.class,PacketSendTerrainType.class,Side.SERVER);
        //获取可雇佣NPC
        registerMessage(PacketGetHireableFolks.Handler.class,PacketGetHireableFolks.class,Side.SERVER);
        //拆除建筑
        registerMessage(PacketDemolishBuilding.Handler.class,PacketDemolishBuilding.class,Side.SERVER);

    }

    /**
     * 客户端
     */
    public void registerMessagesAsClient(){
        //开始卷轴启动页
        registerMessage(PacketOpenSetupGui.Handler.class,PacketOpenSetupGui.class,Side.CLIENT);
        //配置文件
        registerMessage(ConfigSyncPacket.Handler.class,ConfigSyncPacket.class,Side.CLIENT);
        //发送NPC皮肤
        registerMessage(PacketSendFolkSkin.Handler.class,PacketSendFolkSkin.class,Side.CLIENT);
        //可雇佣 的NPC
        registerMessage(PacketReturnHireableFolks.Handler.class,PacketReturnHireableFolks.class,Side.CLIENT);
        //更新资金
        registerMessage(PacketUpdateMoney.Handler.class,PacketUpdateMoney.class,Side.CLIENT);
        //打开NPC互动界面
        registerMessage(PacketOpenFolkGui.Handler.class,PacketOpenFolkGui.class,Side.CLIENT);
        //打开建筑箱gui
        registerMessage(PacketOpenConstructorGui.Handler.class,PacketOpenConstructorGui.class,Side.CLIENT);
        //请求蓝图
        registerMessage(PacketSendBuildingRequirements.Handler.class,PacketSendBuildingRequirements.class,Side.CLIENT);
        //请求规划
        registerMessage(PacketSendTerrainType.Handler.class,PacketSendTerrainType.class,Side.CLIENT);

        //打开控制箱
        registerMessage(PacketOpenControlGui.Handler.class,PacketOpenControlGui.class,Side.CLIENT);


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
