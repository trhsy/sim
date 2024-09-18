package com.trhsy.sim.key;

import com.trhsy.sim.ModSim;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.key
 * @ClassName: ModKeyBinding
 * @Description: 将模组中所有键位进行注册
 * @date 2023.12.6 15:21
 */
public class ModKeyBinding extends KeyBinding {


    public ModKeyBinding(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode, String category) {
        super(String.format("key.%s.%s", ModSim.MODID, description), keyConflictContext, keyModifier, keyCode, String.format("key.%s.%s", ModSim.MODID, category));
        //将模组中所有键位进行注册
        KeyboardManager.KEY_BINDINGS.add(this);
    }

}
