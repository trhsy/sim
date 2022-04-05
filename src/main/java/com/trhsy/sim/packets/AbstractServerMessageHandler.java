package com.trhsy.sim.packets;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * ========================================
 *
 * @ClassName AbstractServerMessageHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:36
 * ========================================
 **/
public abstract class AbstractServerMessageHandler<T extends IMessage> extends AbstractMessageHandler<T>{
    public AbstractServerMessageHandler() {
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
