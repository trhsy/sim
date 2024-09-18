package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketUpdateMoney
 * @Description: 更新资金
 * @date 2022/10/10 10:35
 */
public class PacketUpdateMoney implements IMessage {
    public int gamemode;
    public int dayOfWeek;
    public float money;
    /**模拟城市是否开始运行*/
    public Boolean sim_is_running;
    public PacketUpdateMoney() {
        this.gamemode = ModSimLoader.gamemode;
        this.dayOfWeek = ModSimLoader.dayOfWeek;
        this.money = ModSimLoader.money;
        this.sim_is_running = ModSimLoader.sim_is_running;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.gamemode = buf.readInt();
        this.dayOfWeek = buf.readInt();
        this.money = buf.readFloat();
        this.sim_is_running = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.gamemode);
        buf.writeInt(this.dayOfWeek);
        buf.writeFloat(this.money);
        buf.writeBoolean(this.sim_is_running);
    }
    public static class Handler implements IMessageHandler<PacketUpdateMoney, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketUpdateMoney message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketUpdateMoney出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }
        private void handle(PacketUpdateMoney message, MessageContext ctx) {
            ModSimClientLoader.gamemode = message.gamemode;
            ModSimClientLoader.dayOfWeek = message.dayOfWeek;
            ModSimClientLoader.money = message.money;
            ModSimClientLoader.sim_is_running = message.sim_is_running;
        }
    }
    }
