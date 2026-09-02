package net.justmili.corelibs.v1.config.screen.rows;

import net.minecraft.client.gui.components.ContainerObjectSelectionList;

import static net.justmili.configscreen.SharedElements.DEPTH_INDENT;

public abstract class Row extends ContainerObjectSelectionList.Entry<Row> {
    protected final int depth;

    public Row(int depth) {
        this.depth = depth;
    }

    public int indent() {
        return depth * DEPTH_INDENT;
    }
}