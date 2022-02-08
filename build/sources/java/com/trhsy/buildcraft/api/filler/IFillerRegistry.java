package com.trhsy.buildcraft.api.filler;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import java.util.Collection;

/**
 * ========================================
 *
 * @ClassName IFillerRegistry
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:28
 * ========================================
 **/
public interface IFillerRegistry {
    void addPattern(IFillerPattern var1);

    IFillerPattern getPattern(String var1);

    IFillerPattern getNextPattern(IFillerPattern var1);

    IFillerPattern getPreviousPattern(IFillerPattern var1);

    Collection<IFillerPattern> getPatterns();
}
