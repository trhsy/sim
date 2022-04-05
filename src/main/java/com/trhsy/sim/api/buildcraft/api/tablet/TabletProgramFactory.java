package com.trhsy.sim.api.buildcraft.api.tablet;


public abstract class TabletProgramFactory {
    public TabletProgramFactory() {
    }

    public abstract TabletProgram create(ITablet var1);

    public abstract String getName();

    public abstract TabletBitmap getIcon();
}
