package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IColorTint;
import me.lonefelidae16.betterlookenchant.gui.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ColoredButtonWidget extends ButtonWidget implements IColorTint {
    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
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

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public ColoredButtonWidget build() {
            ColoredButtonWidget result = new ColoredButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
            result.setTooltip(this.tooltip);
            result.colorBg = this.colorBg;
            return result;
        }
    }

    private int colorBg = Color.WHITE.argb();

    protected ColoredButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    public void setBackgroundTint(int rgb) {
        this.colorBg = rgb;
    }

    public void setTextFormat(Style style) {
        MutableText text = this.getMessage().copy();
        text.setStyle(style);
        this.setMessage(text);
    }

    @Override
    public int getBackgroundColor() {
        return this.colorBg;
    }
}
