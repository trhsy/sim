package com.trhsy.sim.common.jobs;

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
    EGGFARMER;

    private Vocation() {
    }

    public static Vocation getVocation(String s) {
        try {
            if (s.contentEquals("Builder")) {
                return BUILDER;
            } else if (s.contentEquals("Lumberjack")) {
                return LUMBERJACK;
            } else if (s.contentEquals("Egg Farmer")) {
                return EGGFARMER;
            } else if (s.contentEquals("Miner")) {
                return MINER;
            } else if (s.contentEquals("Crop farmer")) {
                return CROPFARMER;
            } else if (s.contentEquals("Baker")) {
                return BAKER;
            } else if (s.contentEquals("Soldier")) {
                return SOLDIER;
            } else if (s.contentEquals("Shepherd")) {
                return SHEPHERD;
            } else if (s.contentEquals("Grocer")) {
                return GROCER;
            } else if (s.contentEquals("Courier")) {
                return COURIER;
            } else if (s.contentEquals("Merchant")) {
                return MERCHANT;
            } else if (s.contentEquals("Cattle farmer")) {
                return CATTLEFARMER;
            } else if (s.contentEquals("Pig farmer")) {
                return PIGFARMER;
            } else if (s.contentEquals("Chicken farmer")) {
                return CHICKENFARMER;
            } else if (s.contentEquals("Butcher")) {
                return BUTCHER;
            } else if (s.contentEquals("Terraformer")) {
                return TERRAFORMER;
            } else if (s.contentEquals("Glass maker")) {
                return GLASSMAKER;
            } else if (s.contentEquals("Fisherman")) {
                return FISHERMAN;
            } else if (s.contentEquals("Path Builder")) {
                return PATHBUILDER;
            } else if (s.contentEquals("Dairy Farmer")) {
                return DAIRYFARMER;
            } else if (s.contentEquals("Cheesemaker")) {
                return CHEESEMAKER;
            } else if (s.contentEquals("Fast Food Manager")) {
                return BURGERSMANAGER;
            } else if (s.contentEquals("Fast Food Fry Cook")) {
                return BURGERSFRYCOOK;
            } else {
                return s.contentEquals("Fast Food Waiter") ? BURGERSWAITER : null;
            }
        } catch (Exception var2) {
            return null;
        }
    }

    @Override
    public String toString() {
        if (this == BUILDER) {
            return "Builder";
        } else if (this == LUMBERJACK) {
            return "Lumberjack";
        } else if (this == EGGFARMER) {
            return "Egg Farmer";
        } else if (this == MINER) {
            return "Miner";
        } else if (this == CROPFARMER) {
            return "Crop farmer";
        } else if (this == BAKER) {
            return "Baker";
        } else if (this == SOLDIER) {
            return "Soldier";
        } else if (this == SHEPHERD) {
            return "Shepherd";
        } else if (this == GROCER) {
            return "Grocer";
        } else if (this == COURIER) {
            return "Courier";
        } else if (this == MERCHANT) {
            return "Merchant";
        } else if (this == BUTCHER) {
            return "Butcher";
        } else if (this == PIGFARMER) {
            return "Pig farmer";
        } else if (this == CATTLEFARMER) {
            return "Cattle farmer";
        } else if (this == CHICKENFARMER) {
            return "Chicken farmer";
        } else if (this == TERRAFORMER) {
            return "Terraformer";
        } else if (this == GLASSMAKER) {
            return "Glass maker";
        } else if (this == FISHERMAN) {
            return "Fisherman";
        } else if (this == PATHBUILDER) {
            return "Path Builder";
        } else if (this == DAIRYFARMER) {
            return "Dairy Farmer";
        } else if (this == CHEESEMAKER) {
            return "Cheesemaker";
        } else if (this == BURGERSMANAGER) {
            return "Fast Food Manager";
        } else if (this == BURGERSFRYCOOK) {
            return "Fast Food Fry Cook";
        } else {
            return this == BURGERSWAITER ? "Fast Food Waiter" : "";
        }
    }
}
