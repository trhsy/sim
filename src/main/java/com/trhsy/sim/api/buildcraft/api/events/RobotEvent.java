package com.trhsy.sim.api.buildcraft.api.events;


import com.trhsy.sim.api.buildcraft.api.robots.EntityRobotBase;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class RobotEvent extends Event {
    public final EntityRobotBase robot;

    public RobotEvent(EntityRobotBase robot) {
        this.robot = robot;
    }

    @Cancelable
    public static class Dismantle extends RobotEvent {
        public final EntityPlayer player;

        public Dismantle(EntityRobotBase robot, EntityPlayer player) {
            super(robot);
            this.player = player;
        }
    }

    @Cancelable
    public static class Interact extends RobotEvent {
        public final EntityPlayer player;
        public final ItemStack item;

        public Interact(EntityRobotBase robot, EntityPlayer player, ItemStack item) {
            super(robot);
            this.player = player;
            this.item = item;
        }
    }

    @Cancelable
    public static class Place extends RobotEvent {
        public final EntityPlayer player;

        public Place(EntityRobotBase robot, EntityPlayer player) {
            super(robot);
            this.player = player;
        }
    }
}
