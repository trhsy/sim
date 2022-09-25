package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimLoader;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketOpenSetupGui
 * @Description: 打开开始模组的通讯
 * @date 2022/9/22 0022 下午 3:41
 */
public class PacketOpenSetupGui implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketOpenSetupGui, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketOpenSetupGui message, MessageContext ctx) {
            try {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                this.handle(message, ctx);
            });
            } catch (Exception e) {
                StackTraceElement element=e.getStackTrace()[0];
                ModSimLoader.log.error("PacketOpenSetupGui-onMessage打开启动模组界面出错了：" + e.getMessage()+"行数："+element.getLineNumber());
//                var4.printStackTrace();
            }
            return null;
        }

        private void handle(PacketOpenSetupGui message, MessageContext ctx) {
            ModSimLoader.openSetupGui();
        }
    }
}
