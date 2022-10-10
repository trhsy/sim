package com.trhsy.sim.loader;

import com.trhsy.sim.commands.CommandChangeCredits;
import com.trhsy.sim.commands.CommandGenerateFolk;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: CommandLoader
 * @Description: 命令加载
 * @date 2022/10/9 13:20
 */
public class CommandLoader {
    public CommandLoader(FMLServerStartingEvent event) {
        try {
            //命令生成NPC
            event.registerServerCommand(new CommandGenerateFolk());
            //命令修改金额
            event.registerServerCommand(new CommandChangeCredits());
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("CommandLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
