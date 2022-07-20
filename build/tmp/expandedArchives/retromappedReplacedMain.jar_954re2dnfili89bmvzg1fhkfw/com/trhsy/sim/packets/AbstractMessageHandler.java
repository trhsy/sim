package com.trhsy.sim.packets;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ========================================
 *
 * @ClassName AbstractMessageHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:37
 * ========================================
 **/
public abstract class AbstractMessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
    public AbstractMessageHandler() {
    }

    @SideOnly(Side.CLIENT)
    public abstract IMessage handleClientMessage(EntityPlayer var1, T var2, MessageContext var3);

    public abstract IMessage handleServerMessage(EntityPlayer var1, T var2, MessageContext var3);

    @Override
    public IMessage onMessage(T message, MessageContext ctx) {
        return ctx.side.isClient() ? this.handleClientMessage(ModSim.clientProxy.getPlayerEntity(ctx), message, ctx) : this.handleServerMessage(ModSim.proxy.getPlayerEntity(ctx), message, ctx);
    }
}
