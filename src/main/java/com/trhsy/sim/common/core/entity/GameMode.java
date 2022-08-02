package com.trhsy.sim.common.core.entity;

import com.trhsy.sim.common.loader.ModSimReloaded;

public class GameMode {
    public static GameMode.GAMEMODES gameMode = null;

    public GameMode() {
    }

    /**
     * 游戏模式
     *
     * @return
     */
    public static int getGameModeNumber() {
        int i = 0;
        try {
            if (gameMode == GameMode.GAMEMODES.DONOTRUN) {
                //不运行
                i = -1;
            } else if (gameMode == GameMode.GAMEMODES.NORMAL) {
                //正常的
                i = 0;
            } else if (gameMode == GameMode.GAMEMODES.CREATIVE) {
                //创造
                i = 1;
            } else {
                //否则就是专家模式
                i = gameMode == GameMode.GAMEMODES.HARDCORE ? 2 : 0;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getGameModeNumber出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return i;
    }

    /**
     * 从数字设置游戏模式
     *
     * @param gm
     */
    public static void setGameModeFromNumber(int gm) {
        try {
            if (gm == -1) {
                gameMode = GameMode.GAMEMODES.DONOTRUN;
            } else if (gm == 0) {
                gameMode = GameMode.GAMEMODES.NORMAL;
            } else if (gm == 1) {
                gameMode = GameMode.GAMEMODES.CREATIVE;
            } else if (gm == 2) {
                gameMode = GameMode.GAMEMODES.HARDCORE;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setGameModeFromNumber出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static enum GAMEMODES {
        /**
         * 不运行
         **/
        DONOTRUN,
        /**
         * 正常模式
         **/
        NORMAL,
        /**
         * 创造模式
         **/
        CREATIVE,
        /**
         * 专家模式
         **/
        HARDCORE;

        private GAMEMODES() {
        }
    }
}
