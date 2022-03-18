package com.trhsy.sim.api.buildcraft.api.library;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class LibraryAPI {
    private static final Set<LibraryTypeHandler> handlers = new HashSet();

    private LibraryAPI() {
    }

    public static Set<LibraryTypeHandler> getHandlerSet() {
        return handlers;
    }

    public static void registerHandler(LibraryTypeHandler handler) {
        handlers.add(handler);
    }

    public static LibraryTypeHandler getHandlerFor(String extension) {
        Iterator var1 = handlers.iterator();

        LibraryTypeHandler h;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            h = (LibraryTypeHandler)var1.next();
        } while(!h.isInputExtension(extension));

        return h;
    }
}