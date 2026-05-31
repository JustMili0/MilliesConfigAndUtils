package net.justmili.libs.v1.config.screen.rows;

import net.minecraft.client.gui.components.ContainerObjectSelectionList;

import static net.justmili.libs.v1.config.screen.SharedElements.DEPTH_INDENT;

public abstract class Row extends ContainerObjectSelectionList.Entry<Row> {
    protected final int depth;

    public Row(int depth) {
        this.depth = depth;
    }

    protected int indent() {
        return depth * DEPTH_INDENT;
    }
}