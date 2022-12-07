package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IOffsetElement;
import me.lonefelidae16.betterlookenchant.gui.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class OffsetButtonWidget extends ColoredButtonWidget implements IOffsetElement {
    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private int offsetX;
        private int offsetY;
        private int width = ButtonWidget.DEFAULT_WIDTH;
        private int height = ButtonWidget.DEFAULT_HEIGHT;
        private NarrationSupplier narrationSupplier = ButtonWidget.DEFAULT_NARRATION_SUPPLIER;
        private int colorBg = Color.WHITE.argb();

        public Builder(Text message, PressAction onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public Builder textColor(int color) {
            return this.textStyle(Style.EMPTY.withColor(color));
        }

        public Builder textStyle(Style style) {
            ((MutableText) this.message).setStyle(style);
            return this;
        }

        public Builder backgroundTint(int color) {
            this.colorBg = color;
            return this;
        }

        public Builder offset(int x, int y) {
            this.offsetX = x;
            this.offsetY = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder dimensions(int offsetX, int offsetY, int width, int height) {
            return this.offset(offsetX, offsetY).size(width, height);
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public OffsetButtonWidget build() {
            OffsetButtonWidget result = new OffsetButtonWidget(this.offsetX, this.offsetY, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
            result.setTooltip(this.tooltip);
            result.setBackgroundTint(this.colorBg);
            return result;
        }
    }

    private final int offsetX;
    private final int offsetY;

    protected OffsetButtonWidget(int offsetX, int offsetY, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(offsetX, offsetY, width, height, message, onPress, narrationSupplier);
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
