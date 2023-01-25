package me.lonefelidae16.betterlookenchant.client.gui;

import net.minecraft.client.util.math.MatrixStack;

/**
 * available to change Y axis when rendering widget
 */
public interface IOffsetElement {
    void render(int x, int y, MatrixStack matrices, int mouseX, int mouseY, float delta);
}
