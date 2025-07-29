package com.trhsy.sim.block.enums;

import com.trhsy.sim.ModSim;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public enum ModSounds {
    WOOD(SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft:block.wood.hit"))),
    STONE(SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft:block.stone.hit"))),
    BRICK(SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft:block.brick.hit"))),
    CONSTRUCTOR(new SoundEvent(new ResourceLocation(ModSim.MODID, "sim_u_constructor_activated"))), // 自定义声音
    CHEESE_CRUNCH(SoundEvent.REGISTRY.getObject(new ResourceLocation(ModSim.MODID, "block.cheese.crunch"))),
    ERROR(new SoundEvent(new ResourceLocation("minecraft:block.note.bass")));

    private final SoundEvent soundEvent;

     ModSounds(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        // 注册自定义声音（仅MOD声音需要）
        if (this == CONSTRUCTOR) {
            ForgeRegistries.SOUND_EVENTS.register(soundEvent.setRegistryName(ModSim.MODID, "sim_u_constructor_activated"));
            // 在ModSounds枚举的CHEESE_CRUNCH项构造时注册：
            ForgeRegistries.SOUND_EVENTS.register(soundEvent.setRegistryName(ModSim.MODID, "block.cheese.crunch"));
        }
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
    // 可选：若 BlockBase 构造函数需要 SoundType，可添加转换方法
    public SoundType toSoundType() {
        return SoundType.value(soundEvent.getRegistryName().getPath()); // 需匹配原生 SoundType 名称
    }
}
