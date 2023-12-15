package com.trhsy.sim.npcCode;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util.entity
 * @ClassName: FolkSkin
 * @Description:
 * @date 2022/10/13 9:40
 */
public class NpcSkin {
    public String UUID;
    public String skinPath;

    public NpcSkin(String id, String path) {
        this.UUID = id;
        this.skinPath = path;
    }
}
