package com.trhsy.sim.packets.client;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * ========================================
 *
 * @ClassName UpdateFolkPositionMessage
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:41
 * ========================================
 **/
public class UpdateFolkPositionMessage implements IMessage {
    private String posString;
    String[] data;
    static String folkName;
    static String pos;

    public UpdateFolkPositionMessage() {
    }

    public UpdateFolkPositionMessage(String posString) {
        this.posString = posString;
        this.data = posString.split(";");
        pos = this.data[0];
        folkName = this.data[1];
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.posString = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.posString);
    }
}
