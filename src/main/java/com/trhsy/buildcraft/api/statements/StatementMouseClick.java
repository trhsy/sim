package com.trhsy.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName StatementMouseClick
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:54
 * ========================================
 **/
public final class StatementMouseClick {
    private int button;
    private boolean shift;

    public StatementMouseClick(int button, boolean shift) {
        this.button = button;
        this.shift = shift;
    }

    public boolean isShift() {
        return this.shift;
    }

    public int getButton() {
        return this.button;
    }
}
