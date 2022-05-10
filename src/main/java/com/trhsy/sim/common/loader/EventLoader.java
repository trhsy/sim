package com.trhsy.sim.common.loader;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @ClassName EventLoader
 * @Description todo 事件交互
 * @Author Tian
 * @Date 2022/5/921:38
 **/
public class EventLoader {
    public static final EventBus EVENT_BUS = new EventBus();
    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(this);
        EventLoader.EVENT_BUS.register(this);
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 监听一下桶被盛装的事件
     * @Date 21:40 2022/5/9
     * @Param [event]
     **/
    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        //获取区块位置
        BlockPos blockpos = event.target.getBlockPos();
        //获取区块状态
        IBlockState blockState = event.world.getBlockState(blockpos);
        //获取流体
        Fluid fluid = FluidRegistry.lookupFluidForBlock(blockState.getBlock());
        if (fluid != null && new Integer(0).equals(blockState.getValue(BlockFluidBase.LEVEL))) {
            //桶容积
            FluidStack fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
            event.world.setBlockToAir(blockpos);
            event.result = FluidContainerRegistry.fillFluidContainer(fluidStack, event.current);
            event.setResult(Event.Result.ALLOW);
        }
    }
}
