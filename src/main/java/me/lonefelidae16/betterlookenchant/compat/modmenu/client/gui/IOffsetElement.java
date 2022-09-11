package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui;

import net.minecraft.client.util.math.MatrixStack;

public interface IOffsetElement {
    void render(int x, int y, MatrixStack matrices, int mouseX, int mouseY, float delta);
}
