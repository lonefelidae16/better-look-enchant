package me.lonefelidae16.betterlookenchant.client.gui.widget;

import me.lonefelidae16.betterlookenchant.client.gui.IOffsetElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class OffsetTextFieldWidget extends TextFieldWidget implements IOffsetElement {
    private final int offsetX;
    private final int offsetY;

    public OffsetTextFieldWidget(TextRenderer textRenderer, int offsetX, int offsetY, int width, int height, Text text) {
        super(textRenderer, offsetX, offsetY, width, height, text);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void render(int x, int y, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.setX(x + this.offsetX);
        this.setY(y + this.offsetY);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
