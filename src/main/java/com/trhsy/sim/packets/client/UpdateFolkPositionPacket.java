package com.trhsy.sim.packets.client;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * ========================================
 *
 * @ClassName UpdateFolkPositionMessage
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:41
 * ========================================
 **/
public class UpdateFolkPositionPacket implements IMessage {
    public NBTTagCompound nbt;


    public UpdateFolkPositionPacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            //this.posString = ByteBufUtils.readUTF8String(buf);
            nbt = ByteBufUtils.readTag(buf);
            buf.release();

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("fromBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            ByteBufUtils.writeTag(buf, nbt);
            //ReferenceCountUtil.release(buf);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static class Handler implements IMessageHandler<UpdateFolkPositionPacket, IMessage> {
        @Override
        public IMessage onMessage(UpdateFolkPositionPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                final String nbt =message.nbt.getString("NPCDaTa");
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] data=nbt.split(",");
                            String pos = data[0];
                            String folkName = data[1];
                            FolkData folk = FolkData.getFolkByName(folkName);
                            String[] v=pos.split(",");
                            double x= Double.parseDouble(v[0]);
                            double y= Double.parseDouble(v[0]);
                            double z= Double.parseDouble(v[0]);
                            V3 newpos = new V3(x,y,z);
                            if (folk != null && newpos != null) {
                                folk.serverToClientLocationUpdate(newpos);
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
