package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import io.netty.buffer.ByteBuf;

/**
 * ========================================
 *
 * @ClassName ISerializable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:03
 * ========================================
 **/
public interface ISerializable {
    void writeData(ByteBuf var1);

    void readData(ByteBuf var1);
}
