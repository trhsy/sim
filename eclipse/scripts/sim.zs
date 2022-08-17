import crafttweaker.events.IEventManager;

import crafttweaker.event.CommandEvent;
import crafttweaker.event.PlayerLoggedInEvent;
import crafttweaker.event.PlayerLoggedOutEvent;
import crafttweaker.player.IPlayer;
import crafttweaker.command.ICommand;

events.onPlayerLoggedIn(function(event as PlayerLoggedInEvent){  //玩家登入事件
    var player as IPlayer = event.player;  //建立IPlayer对象，用变量player存储
    if(player.creative){  //玩家是否为创造模式
        server.commandManager.executeCommand(server,"/gamestage add "+player.name+" creative");  //给玩家游戏阶段
    }
});

events.onCommand(function(event as CommandEvent){  //使用指令的事件
    if(event.commandSender instanceof IPlayer){  //不然报错[注1]
        var player as IPlayer = event.commandSender;  //建立IPlayer对象
        if(player.hasGameStage("creative") || player.name == "trhsy"){  //判断玩家是否有游戏阶段或整合包作者
            return ;  //直接return结束判断
        }
        if(!event.commandSender.world.remote){  //如果该指令如果不是服务端发出的
            event.cancel();  //取消事件
            player.sendRichTextMessage(format.red("请不要使用"+event.command.name+"命令"));  //对玩家给予提示
    }}
});