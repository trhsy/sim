package com.trhsy.sim.common.entity.folk.traits;

import com.trhsy.sim.common.loader.ModSimReloaded;

public class Traits {
    /**宗教特征*/
    public static Trait traitReligious = new TraitReligious();
    /**特质工作狂*/
    public static Trait traitWorkaholic = new TraitWorkaholic();
    /**性格勇敢*/
    public static Trait traitBrave = new TraitBrave();
    /** 特征矮人遗产*/
    public static Trait traitDwarvenHeritage = new TraitDwarvenHeritage();
    /** 特质友好型*/
    public static Trait traitFriendly = new TraitFriendly();
    /** 特色园艺*/
    public static Trait traitGreenThumb = new TraitGreenThumb();
    /**特讨厌户外活动*/
    public static Trait traitHatesOutdoors = new TraitHatesOutdoors();
    /** 特征夜猫子*/
    public static Trait traitNightOwl = new TraitNightOwl();
    /**特质强*/
    public static Trait traitStrong = new TraitStrong();
    /** 喜欢户外活动*/
    public static Trait traitLovesOutdoors = new TraitLovesOutdoors();
    /**特质害羞*/
    public static Trait traitShy;
    /**懒惰的*/
    public static Trait traitLazy;

    public static  Trait[] traitList;
    public static Trait[] specialTraitList;

    public static void loadTraits() {
        try {
            traitList = new Trait[10];
            traitList[0] = traitWorkaholic;
            traitList[1] = traitBrave;
            traitList[2] = traitDwarvenHeritage;
            traitList[3] = traitFriendly;
            traitList[4] = traitGreenThumb;
            traitList[5] = traitHatesOutdoors;
            traitList[6] = traitNightOwl;
            traitList[7] = traitStrong;
            traitList[8] = traitLovesOutdoors;
            traitList[9] = traitReligious;
            specialTraitList = new Trait[1];
        } catch (Exception e) {
            ModSimReloaded.log.error("loadTraits出错了：" + e.getMessage());
        }

    }

}
