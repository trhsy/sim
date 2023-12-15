package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import com.trhsy.sim.npcCode.NpcIdentity;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName PacketGetHireableFolks
 * @Description todo 获取可以受雇佣的人
 * @Author TRHSY
 * @Date 2022/10/2118:04
 **/
public class PacketGetHireableFolks implements IMessage {
    public List<NpcIdentity> folkNames = new CopyOnWriteArrayList<NpcIdentity>();
    public boolean isFromClient;

    public PacketGetHireableFolks() {
        /*for (NpcData folk:ModSimLoader.folks){
            NpcIdentity npcIdentity=folk.getClientIdentity();
            if(){}
        }*/
    }

    public PacketGetHireableFolks(boolean isFromClient) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        // TODO document why this method is empty
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // TODO document why this method is empty
    }

    public static class Handler implements IMessageHandler<PacketGetHireableFolks, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketGetHireableFolks message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketGetHireableFolks出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketGetHireableFolks message, MessageContext ctx) {
            //ModSimLoader.log.info("获取可以受雇佣的人，向客户端发送数据包");
            NetWorkLoader.net.sendTo(new PacketReturnHireableFolks(), ctx.getServerHandler().player);
        }
    }
}
