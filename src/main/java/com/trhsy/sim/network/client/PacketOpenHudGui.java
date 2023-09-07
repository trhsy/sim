package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimLoader;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketOpenHudGui
 * @Description todo
 * @Author TRHSY
 * @Date 2023/7/2122:05
 **/
public class PacketOpenHudGui implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketOpenHudGui, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketOpenHudGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception e) {
                StackTraceElement element=e.getStackTrace()[0];
                ModSimLoader.log.error("PacketOpenSetupGui-onMessage打开启动模组界面出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            }
            return null;
        }

        private void handle(PacketOpenHudGui message, MessageContext ctx) {
            ModSimLoader.openHudGui();
        }
    }
}