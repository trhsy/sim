package com.trhsy.sim.packets.server;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.Building;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * ========================================
 *
 * @ClassName LoadBuildingMessage
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:46
 * ========================================
 **/
public class LoadBuildingPacket implements IMessage {
    private String GuiBuildingCon;

    public LoadBuildingPacket() {
    }
    public LoadBuildingPacket(String GuiBuildingCon) {
        this.GuiBuildingCon = GuiBuildingCon;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.GuiBuildingCon = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.GuiBuildingCon);
    }
    public static class Handler implements IMessageHandler<LoadBuildingPacket, IMessage> {
        @Override
        public IMessage onMessage(LoadBuildingPacket message, MessageContext ctx) {

            Building.loadAllBuildings();

            //player.openGui(ModSimukraft.instance, message.id, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            return null;
        }
    }
}
