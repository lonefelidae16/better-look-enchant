package me.lonefelidae16.betterlookenchant.client.gui;

import net.minecraft.client.gui.DrawContext;

/**
 * available to change Y axis when rendering widget
 */
public interface IOffsetElement {
    void render(int x, int y, DrawContext context, int mouseX, int mouseY, float delta);
}
