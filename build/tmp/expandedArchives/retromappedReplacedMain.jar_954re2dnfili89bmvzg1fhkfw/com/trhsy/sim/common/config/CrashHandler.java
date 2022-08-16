package com.trhsy.sim.common.config;

import com.trhsy.sim.common.core.PulseMeta;
import net.minecraftforge.fml.common.ICrashCallable;

import java.util.Collection;
import java.util.Iterator;

/**
 * @ClassName CrashHandler
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:38
 **/
public class CrashHandler implements ICrashCallable {
    private String id;
    private PulseManager manager;

    public CrashHandler(String modId, PulseManager manager) {
        this.id = "Pulsar/" + modId + " loaded Pulses";
        this.manager = manager;
    }

    @Override
    public String getLabel() {
        return this.id;
    }

    @Override
    public String call() {
        String out = "\n";
        try {
            String state;
            Collection<PulseMeta> pulseMetas=this.manager.getAllPulseMetadata();
            for (PulseMeta meta:pulseMetas){
                state = getStateFromMeta(meta);
                out = out + "\t\t- " + meta.getId() + " (" + state + ")\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return out;
    }

    private static String getStateFromMeta(PulseMeta meta) {
        if (meta.isForced()) {
            return "Enabled/Forced";
        } else {
            return meta.isEnabled() ? "Enabled/Not Forced" : "Disabled/Not Forced";
        }
    }
}
