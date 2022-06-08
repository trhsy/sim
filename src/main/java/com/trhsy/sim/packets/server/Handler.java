package com.trhsy.sim.packets.server;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.packets.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * ========================================
 *
 * @ClassName Handler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:46
 * ========================================
 **/
public class Handler extends AbstractServerMessageHandler<LoadBuildingMessage> {
    public Handler() {
    }

    @Override
    public IMessage handleServerMessage(EntityPlayer player, LoadBuildingMessage message, MessageContext ctx) {
        Building.loadAllBuildings();
        return null;
    }
}
