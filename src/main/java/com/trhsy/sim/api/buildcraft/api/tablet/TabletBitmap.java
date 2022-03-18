package com.trhsy.sim.api.buildcraft.api.tablet;


public class TabletBitmap {
    public final int width;
    public final int height;
    protected int[] data;

    public TabletBitmap(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new int[width * height];
    }

    public TabletBitmap(ITablet tablet) {
        this(tablet.getScreenWidth(), tablet.getScreenHeight());
    }

    public int[] getData() {
        return this.data;
    }

    public int get(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height ? this.data[y * this.width + x] : 0;
    }

    public void set(int x, int y, int shade) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            this.data[y * this.width + x] = shade;
        }
    }

    public void set(int x, int y, TabletBitmap bitmap) {
        for(int i = 0; i < bitmap.height && i < this.height; ++i) {
            for(int h = 0; h < bitmap.width && h < this.width; ++h) {
                this.set(x + h, y + i, bitmap.get(h, i));
            }
        }

    }

    public TabletBitmap duplicate() {
        TabletBitmap cloned = new TabletBitmap(this.width, this.height);
        cloned.data = (int[])this.data.clone();
        return cloned;
    }
}
