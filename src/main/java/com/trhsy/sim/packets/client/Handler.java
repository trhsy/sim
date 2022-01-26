package com.trhsy.sim.packets.client;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.packets.AbstractServerMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * ========================================
 *
 * @ClassName Handler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:42
 * ========================================
 **/
public class Handler extends AbstractServerMessageHandler<UpdateFolkPositionMessage> {
    public Handler() {
    }

    @Override
    public IMessage handleServerMessage(EntityPlayer player, UpdateFolkPositionMessage message, MessageContext ctx) {
        FolkData folk = FolkData.getFolkByName(UpdateFolkPositionMessage.folkName);
        V3 newpos = new V3(UpdateFolkPositionMessage.pos);
        if (folk != null && newpos != null) {
            folk.serverToClientLocationUpdate(newpos);
        }

        return null;
    }
}
