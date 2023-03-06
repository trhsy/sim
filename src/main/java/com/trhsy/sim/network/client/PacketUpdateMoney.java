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
 * @ClassName: PacketUpdateMoney
 * @Description: 更新资金
 * @date 2022/10/10 10:35
 */
public class PacketUpdateMoney implements IMessage {
    public int gamemode;
    public int dayOfWeek;
    public float money;
    public PacketUpdateMoney() {
        this.gamemode = ModSimLoader.states.gameModeNumber;
        this.dayOfWeek = ModSimLoader.states.dayOfWeek;
        this.money = ModSimLoader.states.credits;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.gamemode = buf.readInt();
        this.dayOfWeek = buf.readInt();
        this.money = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.gamemode);
        buf.writeInt(this.dayOfWeek);
        buf.writeFloat(this.money);
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
            ModSimLoader.states.gameModeNumber = message.gamemode;
            ModSimLoader.states.dayOfWeek = message.dayOfWeek;
            ModSimLoader.states.credits = message.money;
        }
    }
    }
