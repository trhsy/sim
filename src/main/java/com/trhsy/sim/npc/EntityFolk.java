package com.trhsy.sim.npc;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: EntityFolk
 * @Description:
 * @date 2022/10/11 16:13
 */
public class EntityFolk extends EntityCreature implements INpc {
    public EntityFolk(World worldIn) {
        super(worldIn);
        if(!worldIn.isRemote&& ModSimLoader.hasLoadedFolks){
            //没加载，毁灭吧
            this.setDead();
        }
        //会进门
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        //破门而入
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        //会游泳
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.setSize(0.6F,1.8F);
        this.enablePersistence();

    }

    @Override
    public void onUpdate() {

    }
}
