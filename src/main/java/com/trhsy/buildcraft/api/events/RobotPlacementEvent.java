package com.trhsy.buildcraft.api.events;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

/**
 * ========================================
 *
 * @ClassName RobotPlacementEvent
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:23
 * ========================================
 **/
@Cancelable
public class RobotPlacementEvent extends Event {
    public EntityPlayer player;
    public String robotProgram;

    public RobotPlacementEvent(EntityPlayer player, String robotProgram) {
        this.player = player;
        this.robotProgram = robotProgram;
    }
}
