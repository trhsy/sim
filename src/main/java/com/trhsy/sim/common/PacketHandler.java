package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.packets.SimukraftPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName PacketHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:07
 * ========================================
 **/
public class PacketHandler implements IMessageHandler<SimukraftPacket, IMessage> {
    public PacketHandler() {
    }

    @Override
    public IMessage onMessage(SimukraftPacket message, MessageContext ctx) {
        try {
            String par1 = "";
            String cmd = SimukraftPacket.cmd;
            String val = "";
            World world = null;
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            String sside = "none";
            if (side == Side.SERVER) {
                sside = "Server";
                if (cmd.contentEquals("loadbuilding")) {
                    Building.loadAllBuildings();
                }
            } else if (side == Side.CLIENT) {
                sside = "Client";
                world = Minecraft.getMinecraft().theWorld;
            } else if (cmd.contentEquals("updateFolkPosition")) {
                FolkData folk = FolkData.getFolkByName(par1);
                V3 newpos = new V3(val);
                if (folk != null && newpos != null) {
                    folk.serverToClientLocationUpdate(newpos);
                }
            } else if (cmd.contentEquals("gamereset")) {
                ModSim.resetAndLoadNewWorld();
            }

            ModSim.log.info("PacketHandler: " + sside + "-收到了侧包: " + cmd);
            return null;
        } catch (Exception var11) {
            var11.printStackTrace();
            return message;
        }
    }
}
