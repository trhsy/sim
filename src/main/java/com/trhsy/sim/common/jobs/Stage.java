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
        //闲置
        if (this == IDLE) {
            ret = I18n.format("container.sim.sim_gui_BC_Idle");
            //
        } else if (this == ARRIVEDATSHOP) {
            ret = I18n.format("container.sim.ARRIVEDATSHOP");
        } else if (this == GOINGTOWHEATFARM) {
            ret = I18n.format("container.sim.GOINGTOWHEATFARM");
        } else if (this == COLLECTINGWHEAT) {
            ret = I18n.format("container.sim.COLLECTINGWHEAT");
        } else if (this == GOBACKTOBAKERY) {
            ret = I18n.format("container.sim.GOBACKTOBAKERY");
        } else if (this == MAKEBREAD) {
            ret = I18n.format("container.sim.MAKEBREAD");
        } else if (this == SELLINGBREAD) {
            ret = I18n.format("container.sim.SELLINGBREAD");
        } else if (this == INSTORE) {
            ret = I18n.format("container.sim.INSTORE");
        } else if (this == ARRIVEDATSTORE) {
            ret = I18n.format("container.sim.ARRIVEDATSTORE");
        } else if (this == MAKEFOOD) {
            ret = I18n.format("container.sim.MAKEFOOD");
        } else if (this == NOINGREDIANTS) {
            ret = I18n.format("container.sim.NOINGREDIANTS");
        } else if (this == PICKUPBAKERY) {
            ret = I18n.format("container.sim.PICKUPBAKERY");
        } else if (this == PICKUPGROCERY) {
            ret = I18n.format("container.sim.PICKUPGROCERY");
        } else if (this == PICKUPCHEESE) {
            ret = I18n.format("container.sim.PICKUPCHEESE");
        } else if (this == PICKUPBUTCHERS) {
            ret = I18n.format("container.sim.PICKUPBUTCHERS");
        } else if (this == DROPOFF) {
            ret = I18n.format("container.sim.DROPOFF");
        } else if (this == HANGINGOUT) {
            ret = I18n.format("container.sim.HANGINGOUT");
        } else if (this == SERVING) {
            ret = I18n.format("container.sim.SERVING");
        } else if (this == GOINGTOMEATFARM) {
            ret = I18n.format("container.sim.GOINGTOMEATFARM");
        } else if (this == COLLECTINGMEAT) {
            ret = I18n.format("container.sim.COLLECTINGMEAT");
        } else if (this == GOBACKTOSTORE) {
            ret = I18n.format("container.sim.GOBACKTOSTORE");
        } else if (this == SELLINGMEAT) {
            ret = I18n.format("container.sim.SELLINGMEAT");
        } else if (this == ARRIVEDATFACTORY) {
            ret = I18n.format("container.sim.ARRIVEDATFACTORY");
        } else if (this == GOINGTODAIRYFARM) {
            ret = I18n.format("container.sim.GOINGTODAIRYFARM");
        } else if (this == COLLECTINGMILK) {
            ret = I18n.format("container.sim.COLLECTINGMILK");
        } else if (this == GOINGTOTANK) {
            ret = I18n.format("container.sim.GOINGTOTANK");
        } else if (this == EMPTYINGMILK) {
            ret = I18n.format("container.sim.EMPTYINGMILK");
        } else if (this == STIRING) {
            ret = I18n.format("container.sim.STIRING");
        } else if (this == HARVESTCHEESE) {
            ret = I18n.format("container.sim.HARVESTCHEESE");
        } else if (this == SLICECHEESE) {
            ret = I18n.format("container.sim.SLICECHEESE");
        } else if (this == ATDEPOT) {
            ret = I18n.format("container.sim.ATDEPOT");
        } else if (this == GOINGTOPICKUP) {
            ret = I18n.format("container.sim.GOINGTOPICKUP");
        } else if (this == PICKINGUP) {
            ret = I18n.format("container.sim.PICKINGUP");
        } else if (this == GOINGTODROPOFF) {
            ret = I18n.format("container.sim.GOINGTODROPOFF");
        } else if (this == DROPPINGOFF) {
            ret = I18n.format("container.sim.DROPPINGOFF");
        } else if (this == ARRIVEDATFARM) {
            ret = I18n.format("container.sim.ARRIVEDATFARM");
        } else if (this == CHECKINGFORCHESTS) {
            ret = I18n.format("container.sim.CHECKINGFORCHESTS");
        } else if (this == HOELAND) {
            ret = I18n.format("container.sim.HOELAND");
        } else if (this == PLANTSEEDS) {
            ret = I18n.format("container.sim.PLANTSEEDS");
        } else if (this == HARVEST) {
            ret = I18n.format("container.sim.HARVEST");
        } else if (this == HANGOUT) {
            ret = I18n.format("container.sim.HANGOUT");
        } else if (this == WAITINGFORMILKING) {
            ret = I18n.format("container.sim.WAITINGFORMILKING");
        } else if (this == MILKING) {
            ret = I18n.format("container.sim.MILKING");
        } else if (this == STORINGMILK) {
            ret = I18n.format("container.sim.STORINGMILK");
        } else if (this == CANTWORK) {
            ret = I18n.format("container.sim.CANTWORK");
        } else if (this == FEEDINGCHICKENS) {
            ret = I18n.format("container.sim.FEEDINGCHICKENS");
        } else if (this == COLLECTINGEGGS) {
            ret = I18n.format("container.sim.COLLECTINGEGGS");
        } else if (this == STORINGEGGS) {
            ret = I18n.format("container.sim.STORINGEGGS");
        } else if (this == ARRIVEDATDOCK) {
            ret = I18n.format("container.sim.ARRIVEDATDOCK");
        } else if (this == FISHING) {
            ret = I18n.format("container.sim.FISHING");
        } else if (this == CAUGHTFISH) {
            ret = I18n.format("container.sim.CAUGHTFISH");
        } else if (this == SELLINGFISH) {
            ret = I18n.format("container.sim.SELLINGFISH");
        } else if (this == SCANFORSAND) {
            ret = I18n.format("container.sim.SCANFORSAND");
        } else if (this == GOTOSANDBLOCK) {
            ret = I18n.format("container.sim.GOTOSANDBLOCK");
        } else if (this == COLLECTSAND) {
            ret = I18n.format("container.sim.COLLECTSAND");
        } else if (this == RETURNSAND) {
            ret = I18n.format("container.sim.RETURNSAND");
        } else if (this == USEFURNACE) {
            ret = I18n.format("container.sim.USEFURNACE");
        } else if (this == GOINGTOFOODFARM) {
            ret = I18n.format("container.sim.GOINGTOFOODFARM");
        } else if (this == COLLECTINGFOOD) {
            ret = I18n.format("container.sim.COLLECTINGFOOD");
        } else if (this == SELLINGFOOD) {
            ret = I18n.format("container.sim.SELLINGFOOD");
        } else if (this == WAITINGFORMATUREANIMAL) {
            ret = I18n.format("container.sim.WAITINGFORMATUREANIMAL");
        } else if (this == SLAUGHTERING) {
            ret = I18n.format("container.sim.SLAUGHTERING");
        } else if (this == ARRIVEDATMILL) {
            ret = I18n.format("container.sim.ARRIVEDATMILL");
        } else if (this == SCANFORTREE) {
            ret = I18n.format("container.sim.SCANFORTREE");
        } else if (this == GOTOTREE) {
            ret = I18n.format("container.sim.GOTOTREE");
        } else if (this == CHOPPINGTREE) {
            ret = I18n.format("container.sim.CHOPPINGTREE");
        } else if (this == RETURNWOOD) {
            ret = I18n.format("container.sim.RETURNWOOD");
        } else if (this == SCANFORCLAY) {
            ret = I18n.format("container.sim.SCANFORCLAY");
        } else if (this == GOTOCLAYBLOCK) {
            ret = I18n.format("container.sim.GOTOCLAYBLOCK");
        } else if (this == COLLECTCLAY) {
            ret = I18n.format("container.sim.COLLECTCLAY");
        } else if (this == RETURNCLAY) {
            ret = I18n.format("container.sim.RETURNCLAY");
        } else if (this == WAITINGFORCHEST) {
            ret = I18n.format("container.sim.WAITINGFORCHEST");
        } else if (this == BEAMINGDOWN) {
            ret = I18n.format("container.sim.BEAMINGDOWN");
        } else if (this == MINING) {
            ret = I18n.format("container.sim.MINING");
        } else if (this == BEAMINGUP) {
            ret = I18n.format("container.sim.BEAMINGUP");
        } else if (this == WAITINGFORWOOL) {
            ret = I18n.format("container.sim.WAITINGFORWOOL");
        } else if (this == SHEARING) {
            ret = I18n.format("container.sim.SHEARING");
        } else if (this == ONPATROL) {
            ret = I18n.format("container.sim.ONPATROL");
        } else if (this == ATTACKING) {
            ret = I18n.format("container.sim.ATTACKING");
        } else if (this == COMPLETE) {
            ret = I18n.format("container.sim.trhsy10");
        } else if (this == WORKERASSIGNED) {
            ret = I18n.format("container.sim.trhsy6");
        } else if (this == BLUEPRINT) {
            ret = I18n.format("container.sim.trhsy7");
        } else if (this == WAITINGFORRESOURCES) {
            ret = I18n.format("container.sim.trhsy8");
        } else if (this == INPROGRESS) {
            ret = I18n.format("container.sim.trhsy9");
        }
        return ret;
    }
}
