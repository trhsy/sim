package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.functionality.PathBox;
import net.minecraft.inventory.IInventory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobPathBuilder
 * @Description todo 路径构建器
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:53
 * ========================================
 **/
public class JobPathBuilder {
    private static final long serialVersionUID = -1177112207904272541L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient int step = 1;
    transient Long timeSinceLastGoto = 0L;
    transient List<IInventory> pathChests = null;
    transient boolean swingToggle = true;
    private PathBox thePathBox;
    public String pathDirection = "";
    public int pathOffset = 1;

    public JobPathBuilder() {
    }

    public void resetJob() {
        this.theStage = Stage.IDLE;
        this.theFolk.isWorking = false;
    }
}
