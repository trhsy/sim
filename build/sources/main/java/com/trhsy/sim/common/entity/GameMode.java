package com.trhsy.sim.common.entity;

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
        if (gameMode == GameMode.GAMEMODES.DONOTRUN) {
            //不运行
            return -1;
        } else if (gameMode == GameMode.GAMEMODES.NORMAL) {
            //正常的
            return 0;
        } else if (gameMode == GameMode.GAMEMODES.CREATIVE) {
            //创造
            return 1;
        } else {
            //否则就是专家模式
            return gameMode == GameMode.GAMEMODES.HARDCORE ? 2 : 0;
        }
    }

    /**
     * 从数字设置游戏模式
     * @param gm
     */
    public static void setGameModeFromNumber(int gm) {
        if (gm == -1) {
            gameMode = GameMode.GAMEMODES.DONOTRUN;
        } else if (gm == 0) {
            gameMode = GameMode.GAMEMODES.NORMAL;
        } else if (gm == 1) {
            gameMode = GameMode.GAMEMODES.CREATIVE;
        } else if (gm == 2) {
            gameMode = GameMode.GAMEMODES.HARDCORE;
        }

    }
    public static enum GAMEMODES {
        /**不运行**/
        DONOTRUN,
        /**正常模式**/
        NORMAL,
        /**创造模式**/
        CREATIVE,
        /**专家模式**/
        HARDCORE;

        private GAMEMODES() {
        }
    }
}
