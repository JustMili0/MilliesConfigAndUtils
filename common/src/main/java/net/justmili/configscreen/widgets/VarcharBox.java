package net.justmili.corelibs.v1.config.screen.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import static net.justmili.configscreen.SharedElements.C_WHITE;

public class VarcharBox extends EditBox {
    public VarcharBox(Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height, Component.empty());
        setBordered(false);
        setTextColor(C_WHITE);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        graphics.hLine(getX()-4, getX()+120, getY()+getHeight()-10, C_WHITE);
    }
}