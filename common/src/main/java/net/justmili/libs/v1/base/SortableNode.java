/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Repackaged for Core Libraries; logic unchanged from the original.
 */

package net.justmili.libs.v1.base;

import java.util.ArrayList;
import java.util.List;

public abstract class SortableNode<N extends SortableNode<N>> {
    public final List<N> subsequentNodes = new ArrayList<>();
    public final List<N> previousNodes = new ArrayList<>();
    boolean visited = false;

    /**
     * @return Description of this node, used to print the cycle warning.
     */
    protected abstract String getDescription();

    public static <N extends SortableNode<N>> void link(N first, N second) {
        if (first == second) {
            throw new IllegalArgumentException("Cannot link a node to itself!");
        }

        first.subsequentNodes.add(second);
        second.previousNodes.add(first);
    }
}
