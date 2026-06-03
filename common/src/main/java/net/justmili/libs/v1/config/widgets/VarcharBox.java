package net.justmili.libs.v1.config.widgets;

import net.justmili.libs.v1.config.screen.SharedElements;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class VarcharBox extends AbstractWidget implements Renderable {
    public VarcharBox(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.hLine(getX(), getX(), height, SharedElements.C_WHITE);
        // ???
        // No clue if I'm doing this right (probably not) but it'll be a cleaner replacement of the EditBox
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narratedElement) {

    }
}
