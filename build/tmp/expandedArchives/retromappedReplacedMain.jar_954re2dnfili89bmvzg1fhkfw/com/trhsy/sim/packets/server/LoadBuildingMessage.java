package com.trhsy.sim.packets.server;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * ========================================
 *
 * @ClassName LoadBuildingMessage
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:46
 * ========================================
 **/
public class LoadBuildingMessage implements IMessage {
    private String GuiBuildingCon;

    public LoadBuildingMessage() {
    }
    public LoadBuildingMessage(String GuiBuildingCon) {
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
}
