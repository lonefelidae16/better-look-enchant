package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IColorTint;
import me.lonefelidae16.betterlookenchant.gui.Color;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class ColoredButtonWidget extends ButtonWidget implements IColorTint {
    private int colorBg;

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        this(x, y, width, height, message, onPress, EMPTY);
    }

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.colorBg = Color.WHITE.argb();
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
