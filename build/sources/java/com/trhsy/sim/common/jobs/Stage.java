package com.trhsy.sim.common.jobs;

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
            ret = "Idle";
        } else if (this == WORKERASSIGNED) {
            ret = "Builder has been hired and on their way";
        } else if (this == BLUEPRINT) {
            ret = "Builder is looking though blueprints";
        } else if (this == WAITINGFORRESOURCES) {
            ret = "Builder is checking the resources for the building";
        } else if (this == INPROGRESS) {
            ret = "Builder is busy building";
        } else if (this == COMPLETE) {
            ret = "Building work is complete";
        }

        return ret;
    }
}
