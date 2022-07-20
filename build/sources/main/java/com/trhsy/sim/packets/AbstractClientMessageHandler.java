package com.trhsy.sim.packets;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * ========================================
 *
 * @ClassName AbstractClientMessageHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:39
 * ========================================
 **/
public abstract class AbstractClientMessageHandler<T extends IMessage> extends AbstractMessageHandler<T> {
    public AbstractClientMessageHandler() {
    }
    @Override
    public IMessage handleClientMessage(EntityPlayer var1, T var2, MessageContext var3) {
        return null;
    }

    @Override
    public IMessage handleServerMessage(EntityPlayer var1, T var2, MessageContext var3) {
        return null;
    }
}
