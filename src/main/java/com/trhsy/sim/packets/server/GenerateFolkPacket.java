package com.trhsy.sim.packets.server;

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.loader.ModSimReloaded;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 生成npc包
 */
public class GenerateFolkPacket implements IMessage {
    public NBTTagCompound nbt;
    static World world;

    /**
     * 生成npc包
     */
    public GenerateFolkPacket() {
    }

    /**
     * 生成npc包
     * @param whirld
     */
    public GenerateFolkPacket(World whirld) {
        try {
            world = whirld;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GenerateFolkPacket出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 来自字节
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            //isForced = buf.readBoolean();
            nbt = ByteBufUtils.readTag(buf);
            buf.release();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("fromBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 去字节
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        try {
            //buf.writeBoolean(isForced);
            ByteBufUtils.writeTag(buf,nbt);
            //ReferenceCountUtil.release(buf);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 消息处理程序
     */
    public static class Handler implements IMessageHandler<GenerateFolkPacket, IMessage> {

        @Override
        public IMessage onMessage(GenerateFolkPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                final boolean isForced =message.nbt.getBoolean("NPC_Packet");
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!isForced) {
                                //生成新人
                                FolkData.generateNewFolk(world);
                            } else {
                                //强制生成新人
                                FolkData.forceGenerateNewFolk(world);
                            }
                        } catch (Exception e) {
                            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onMessage出错了：" + e.getMessage()+"行数："+element.getLineNumber());
                        }
                    }
                });
            }
            return null;
        }

    }
}
