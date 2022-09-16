package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IColorTint;
import me.lonefelidae16.betterlookenchant.gui.Color;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ColoredButtonWidget extends ButtonWidget implements IColorTint {
    private Color colorBg;
    private Color colorText;

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        this(x, y, width, height, message, onPress, EMPTY);
    }

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.colorBg = Color.WHITE;
        this.colorText = Color.WHITE;
    }

    public void setBackgroundTint(int rgb) {
        this.colorBg = Color.fromARGB(rgb);
    }

    public void setTextColor(int rgb) {
        this.colorText = Color.fromARGB(rgb);
    }

    @Override
    public Color getForegroundColor() {
        return this.colorText;
    }

    @Override
    public Color getBackgroundColor() {
        return this.colorBg;
    }
}
