package com.trhsy.sim.common.jobs;

import net.minecraft.client.resources.I18n;

/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */
public enum Vocation {
    BUILDER,
    LUMBERJACK,
    MINER,
    CROPFARMER,
    BAKER,
    SOLDIER,
    SHEPHERD,
    GROCER,
    COURIER,
    MERCHANT,
    BUTCHER,
    PIGFARMER,
    CATTLEFARMER,
    CHICKENFARMER,
    TERRAFORMER,
    GLASSMAKER,
    FISHERMAN,
    PATHBUILDER,
    DAIRYFARMER,
    CHEESEMAKER,
    BURGERSMANAGER,
    BURGERSFRYCOOK,
    BURGERSWAITER,
    EGGFARMER,
    BRICKMAKER;

    private Vocation() {
    }

    public static Vocation getVocation(String s) {
        try {
            if (s.contentEquals(I18n.format("container.sim.Vocation1"))) {
                return BUILDER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation2"))) {
                return LUMBERJACK;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation3"))) {
                return EGGFARMER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation4"))) {
                return MINER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation5"))) {
                return CROPFARMER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation6"))) {
                return BAKER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation7"))) {
                return SOLDIER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation8"))) {
                return SHEPHERD;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation9"))) {
                return GROCER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation10"))) {
                return COURIER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation11"))) {
                return MERCHANT;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation12"))) {
                return CATTLEFARMER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation13"))) {
                return PIGFARMER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation14"))) {
                return CHICKENFARMER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation15"))) {
                return BUTCHER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation16"))) {
                return TERRAFORMER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation17"))) {
                return GLASSMAKER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation18"))) {
                return FISHERMAN;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation19"))) {
                return PATHBUILDER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation20"))) {
                return DAIRYFARMER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation21"))) {
                return CHEESEMAKER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation22"))) {
                return BURGERSMANAGER;
            } else if (s.contentEquals(I18n.format("container.sim.Vocation23"))) {
                return BURGERSFRYCOOK;
            }else if(s.contentEquals(I18n.format("container.sim.Vocation25"))){
                return BRICKMAKER;
            } else {
                return s.contentEquals(I18n.format("container.sim.Vocation24")) ? BURGERSWAITER : null;
            }
        } catch (Exception var2) {
            return null;
        }
    }

    @Override
    public String toString() {
        if (this == BUILDER) {
            return I18n.format("container.sim.Vocation1");
        } else if (this == LUMBERJACK) {
            return I18n.format("container.sim.Vocation2");
        } else if (this == EGGFARMER) {
            return I18n.format("container.sim.Vocation3");
        } else if (this == MINER) {
            return I18n.format("container.sim.Vocation4");
        } else if (this == CROPFARMER) {
            return I18n.format("container.sim.Vocation5");
        } else if (this == BAKER) {
            return I18n.format("container.sim.Vocation6");
        } else if (this == SOLDIER) {
            return I18n.format("container.sim.Vocation7");
        } else if (this == SHEPHERD) {
            return I18n.format("container.sim.Vocation8");
        } else if (this == GROCER) {
            return I18n.format("container.sim.Vocation9");
        } else if (this == COURIER) {
            return I18n.format("container.sim.Vocation10");
        } else if (this == MERCHANT) {
            return I18n.format("container.sim.Vocation11");
        } else if (this == BUTCHER) {
            return I18n.format("container.sim.Vocation12");
        } else if (this == PIGFARMER) {
            return I18n.format("container.sim.Vocation13");
        } else if (this == CATTLEFARMER) {
            return I18n.format("container.sim.Vocation14");
        } else if (this == CHICKENFARMER) {
            return I18n.format("container.sim.Vocation15");
        } else if (this == TERRAFORMER) {
            return I18n.format("container.sim.Vocation16");
        } else if (this == GLASSMAKER) {
            return I18n.format("container.sim.Vocation17");
        } else if (this == FISHERMAN) {
            return I18n.format("container.sim.Vocation18");
        } else if (this == PATHBUILDER) {
            return I18n.format("container.sim.Vocation19");
        } else if (this == DAIRYFARMER) {
            return I18n.format("container.sim.Vocation20");
        } else if (this == CHEESEMAKER) {
            return I18n.format("container.sim.Vocation21");
        } else if (this == BURGERSMANAGER) {
            return I18n.format("container.sim.Vocation22");
        } else if (this == BURGERSFRYCOOK) {
            return I18n.format("container.sim.Vocation23");
        } else if (this == BRICKMAKER) {
            return I18n.format("container.sim.Vocation25");
        }  else {
            return this == BURGERSWAITER ? I18n.format("container.sim.Vocation24") : "";
        }
    }
}
