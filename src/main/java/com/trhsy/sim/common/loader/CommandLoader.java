package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.commands.CommandChangeCredits;
import com.trhsy.sim.common.commands.CommandGenerateFolk;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandLoader {
    public CommandLoader(FMLServerStartingEvent event) {
        try {
            //命令生成NPC
            event.registerServerCommand(new CommandGenerateFolk());
            //命令修改金额
            event.registerServerCommand(new CommandChangeCredits());
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
    }
}
