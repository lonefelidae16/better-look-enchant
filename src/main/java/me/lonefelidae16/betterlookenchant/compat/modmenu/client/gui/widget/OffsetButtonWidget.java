package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IOffsetElement;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class OffsetButtonWidget extends ColoredButtonWidget implements IOffsetElement {
    private final int offsetX;
    private final int offsetY;

    public OffsetButtonWidget(int offsetX, int offsetY, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(offsetX, offsetY, width, height, message, onPress, tooltipSupplier);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public OffsetButtonWidget(int offsetX, int offsetY, int width, int height, Text message, PressAction onPress) {
        this(offsetX, offsetY, width, height, message, onPress, EMPTY);
    }

    @Override
    public void render(int x, int y, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.x = x + this.offsetX;
        this.y = y + this.offsetY;
        super.render(matrices, mouseX, mouseY, delta);
    }
}
