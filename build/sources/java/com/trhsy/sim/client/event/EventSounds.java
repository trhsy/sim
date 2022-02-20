package com.trhsy.sim.client.event;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.sound.SoundLoadEvent;

/**
 * ========================================
 *
 * @ClassName EventSounds
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:11
 * ========================================
 **/
public class EventSounds {
    public EventSounds() {
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onSound(SoundLoadEvent event) {
        try {
            String[] sounds = new String[]{"constructoractivated", "computer", "powerdown", "hellom", "hellof", "readym", "readyf", "welcome", "construction", "cash", "beamdown", "CoughF", "CoughM", "SneezeF", "SneezeM", "LaughF", "LaughM", "OuchF", "OuchM", "lightone", "fone", "ftwo", "fthree", "mone", "mtwo", "mthree", "mfour", "mfive", "msix", "shears", "rooster", "daymone", "dayfone", "daymtwo", "dayftwo", "daymthree", "dayfthree", "nightmone", "nightfone", "nightmtwo", "nightftwo", "nightmthree", "nightfthree", "merchm", "bakerm", "bakerf", "beamm", "beamf", "beamdowntwo", "mnine", "fnine", "meight", "feight", "fseven", "mseven", "mwxbad", "fwxbad", "cashshort", "blarga", "blargb", "blargc", "blargd", "blarge", "blargf", "blargg", "blargh", "blargi", "blargj", "blargk", "blargl", "blargm", "blargn", "blargo", "blargp", "blargq", "blargr", "blargs", "blargt", "blargu", "blargv", "blargw", "blargx", "blargy", "blargz", "birth", "pregnant", "mspeaka", "mspeakb", "mspeakc", "mspeakd", "mspeake", "mspeakf", "mspeakg", "mspeakh", "mspeaki", "mspeakj", "mspeakk", "mspeakl", "mspeakm", "mspeakn", "mspeako", "mspeakp", "mspeakq", "mspeakr", "mspeaks", "fspeaka", "fspeakb", "fspeakc", "fspeakd", "fspeake", "fspeakf", "fspeakg", "fspeakh", "fspeaki", "fspeakj", "fspeakk", "fspeakl", "fspeakm", "fspeakn", "fspeako", "fspeakp", "fspeakq", "fspeakr", "fspeaks", "cspeaka", "cspeakb", "cspeakc", "helloc", "windmill", "burgerma", "burgermb", "burgermc", "burgermd", "burgerme", "burgermf", "burgerfa", "burgerfb", "burgerfc", "burgerfd", "burgerfe", "burgerff", "cheesemachine"};

            for(int x = 0; x < sounds.length; ++x) {
                ModSim.log.info("注册一个或多个声音:" +sounds[x]);
            }
        } catch (Exception var4) {
            ModSim.log.error("注册一个或多个声音失败.:" + var4.getMessage());
        }

    }
}
