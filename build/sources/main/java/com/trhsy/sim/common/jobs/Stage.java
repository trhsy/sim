package com.trhsy.sim.common.jobs;

import net.minecraft.client.resources.I18n;

/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */
public enum Stage {
    IDLE,
    ARRIVEDATSHOP,
    GOINGTOWHEATFARM,
    COLLECTINGWHEAT,
    GOBACKTOBAKERY,
    MAKEBREAD,
    SELLINGBREAD,
    WORKERASSIGNED,
    BLUEPRINT,
    WAITINGFORRESOURCES,
    INPROGRESS,
    INSTORE,
    COMPLETE,
    ARRIVEDATSTORE,
    MAKEFOOD,
    NOINGREDIANTS,
    PICKUPBAKERY,
    PICKUPGROCERY,
    PICKUPCHEESE,
    PICKUPBUTCHERS,
    DROPOFF,
    HANGINGOUT,
    SERVING,
    GOINGTOMEATFARM,
    COLLECTINGMEAT,
    GOBACKTOSTORE,
    SELLINGMEAT,
    ARRIVEDATFACTORY,
    GOINGTODAIRYFARM,
    COLLECTINGMILK,
    GOINGTOTANK,
    EMPTYINGMILK,
    STIRING,
    HARVESTCHEESE,
    SLICECHEESE,
    ATDEPOT,
    GOINGTOPICKUP,
    PICKINGUP,
    GOINGTODROPOFF,
    DROPPINGOFF,
    ARRIVEDATFARM,
    CHECKINGFORCHESTS,
    HOELAND,
    PLANTSEEDS,
    HARVEST,
    HANGOUT,
    WAITINGFORMILKING,
    MILKING,
    STORINGMILK,
    CANTWORK,
    FEEDINGCHICKENS,
    COLLECTINGEGGS,
    STORINGEGGS,
    ARRIVEDATDOCK,
    FISHING,
    CAUGHTFISH,
    SELLINGFISH, SCANFORSAND,
    GOTOSANDBLOCK,
    COLLECTSAND,
    RETURNSAND,
    USEFURNACE,
    GOINGTOFOODFARM,
    COLLECTINGFOOD,
    SELLINGFOOD,
    WAITINGFORMATUREANIMAL,
    SLAUGHTERING,
    ARRIVEDATMILL,
    SCANFORTREE,
    GOTOTREE,
    CHOPPINGTREE,
    RETURNWOOD,
    SCANFORCLAY,
    GOTOCLAYBLOCK,
    COLLECTCLAY,
    RETURNCLAY,
    WAITINGFORCHEST,
    BEAMINGDOWN,
    MINING,
    BEAMINGUP,
    WAITINGFORWOOL,
    SHEARING,
    ONPATROL,
    ATTACKING;

    private Stage() {
    }

    @Override
    public String toString() {
        String ret = "";
        if (this == IDLE) {
            ret = I18n.format("container.sim.sim_gui_BC_Idle");
        } else if (this == WORKERASSIGNED) {
            ret = I18n.format("container.sim.trhsy6");
        } else if (this == BLUEPRINT) {
            ret = I18n.format("container.sim.trhsy7");
        } else if (this == WAITINGFORRESOURCES) {
            ret = I18n.format("container.sim.trhsy8");
        } else if (this == INPROGRESS) {
            ret = I18n.format("container.sim.trhsy9");
        } else if (this == COMPLETE) {
            ret = I18n.format("container.sim.trhsy10");
        }

        return ret;
    }
}
