package com.trhsy.sim.packets;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * ========================================
 *
 * @ClassName SimukraftPacket
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 3:43
 * ========================================
 **/
public class SimukraftPacket implements IMessage {

    public static String par1;
    public static String cmd = "";
    public static String folkName = "";

    public SimukraftPacket() {
    }
    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }
}
