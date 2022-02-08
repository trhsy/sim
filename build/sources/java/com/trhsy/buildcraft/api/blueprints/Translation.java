package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.Position;

/**
 * ========================================
 *
 * @ClassName Translation
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:37
 * ========================================
 **/
public class Translation {
    public double x = 0.0D;
    public double y = 0.0D;
    public double z = 0.0D;

    public Translation() {
    }

    public Position translate(Position p) {
        Position p2 = new Position(p);
        p2.x = p.x + this.x;
        p2.y = p.y + this.y;
        p2.z = p.z + this.z;
        return p2;
    }

    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + ", " + this.z + "}";
    }
}
