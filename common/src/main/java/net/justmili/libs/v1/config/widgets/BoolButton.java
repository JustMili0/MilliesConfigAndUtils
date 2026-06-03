package net.justmili.libs.v1.config.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import static net.justmili.libs.v1.config.screen.SharedElements.*;

public class BoolButton extends AbstractButton {
    public boolean state;
    private final OnPress onPress;

    public BoolButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.empty());
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderIcon(graphics, getX(), getY(), state ? W_BOOL_TRUE : W_BOOL_FALSE);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narratedElement) {

    }

    @Environment(EnvType.CLIENT)
    public interface OnPress {
        void onPress(BoolButton button);
    }
}
